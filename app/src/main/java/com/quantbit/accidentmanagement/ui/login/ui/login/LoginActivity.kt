package com.quantbit.accidentmanagement.ui.login.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.quantbit.accidentmanagement.MainActivity
import com.quantbit.accidentmanagement.R
import com.quantbit.accidentmanagement.databinding.ActivityLoginBinding
import com.quantbit.accidentmanagement.ui.login.data.model.LoggedInUser
import com.quantbit.accidentmanagement.utility.SharedUtility
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedUtility:SharedUtility

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
         sharedUtility = SharedUtility(context = applicationContext)
        if(sharedUtility.getBoolean("logged_in") == true){
            navigateToHome()
        }


        val username = binding.username
        val password = binding.password
        val login = binding.login
        //val loading = binding.loading


        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

           // loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)

            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful

        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        lifecycleScope.launch {
                            loginViewModel.login(
                                username.text.toString(),
                                password.text.toString()

                            )
                        }
                }
                false
            }

            login.setOnClickListener {
              //  loading.visibility = View.VISIBLE

                lifecycleScope.launch {
                    loginViewModel.login(username.text.toString(), password.text.toString())
                }

            }
        }
    }
    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }


    private fun updateUiWithUser(model: LoggedInUser) {
        val welcome = getString(R.string.welcome)
        val displayName = model.user

        sharedUtility.saveString("user",model.user)
        sharedUtility.saveString("name",model.name)
        sharedUtility.saveString("token","token ${model.apiKey}:${model.apiSecret}")
        sharedUtility.saveBoolean("logged_in",true)


        // Show welcome message
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()

        // Navigate to Home screen
        navigateToHome()
    }


    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}