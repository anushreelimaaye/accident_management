package com.quantbit.accidentmanagement.ui.login.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.quantbit.accidentmanagement.R
import com.quantbit.accidentmanagement.ui.login.data.LoginRepository
import com.quantbit.accidentmanagement.ui.login.data.Result
import com.quantbit.accidentmanagement.ui.login.data.model.LoggedInUser
import kotlinx.coroutines.launch


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    suspend fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        viewModelScope.launch {
            // Call the login function from the repository
            val result = loginRepository.login(username, password)

            // Handle the result
            when (result) {
                is Result.Success -> {
                    // Ensure `result.data` is not null
                    val loggedInUser = result.data
                    if (loggedInUser != null) {
                        _loginResult.value = LoginResult(
                            success = loggedInUser
                        )
                    } else {
                        // Handle case where `loggedInUser` might be null
                        _loginResult.value = LoginResult(
                            error = R.string.login_failed
                        )
                    }
                }
                is Result.Error -> {
                    // Handle error case
                    _loginResult.value = LoginResult(
                        error = R.string.login_failed
                    )
                }
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}