package com.quantbit.accidentmanagement

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.navigation.ui.NavigationUI
import com.quantbit.accidentmanagement.databinding.ActivityMainBinding
import com.quantbit.accidentmanagement.ui.login.ui.login.LoginActivity
import com.quantbit.accidentmanagement.utility.SharedUtility

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var enableBluetoothLauncher: ActivityResultLauncher<Intent>

    private val REQUEST_BLUETOOTH_PERMISSIONS = 2
    lateinit var sharedUtility: SharedUtility

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = com.quantbit.accidentmanagement.databinding.ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        sharedUtility = SharedUtility(context = applicationContext)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Passing each menu ID as a set of Ids because each menu should be considered as top-level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_contact_list, R.id.nav_bluetooth_device,R.id.nav_bluetooth_event,R.id.nav_notification_log
            ), drawerLayout
        )
        val headerView = navView.getHeaderView(0)
        val headerTextView = headerView.findViewById<TextView>(R.id.textView)
        val user = sharedUtility.getString("user")
        // Set the text to the TextView
        headerTextView.text =user
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_log_out -> {
                    handleLogout() // Your custom logout handling function
                    true // Indicate that the logout item has been handled
                }
                else -> {
                    // Default behavior: Let the NavController handle the navigation
                    NavigationUI.onNavDestinationSelected(menuItem, navController)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
            }
        }

        setupBluetoothLauncher()
        checkBluetoothPermissions()
    }

    private fun setupBluetoothLauncher() {
        enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                // Bluetooth is enabled
                Snackbar.make(binding.root, "Bluetooth enabled", Snackbar.LENGTH_SHORT)
                 //   .setAnchorView(R.id.fab)
                    .show()
            } else {
                // Bluetooth is not enabled
                Snackbar.make(binding.root, "Bluetooth not enabled", Snackbar.LENGTH_SHORT)
                   // .setAnchorView(R.id.fab)
                    .show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun checkBluetoothPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.SEND_SMS,
            Manifest.permission.POST_NOTIFICATIONS // For Android 13 and above
        )

        if (permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            checkBluetoothState()
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_BLUETOOTH_PERMISSIONS)
        }
    }

    private fun checkBluetoothState() {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableBluetoothLauncher.launch(enableBtIntent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_BLUETOOTH_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    checkBluetoothState()
                } else {
                    Snackbar.make(binding.root, "Permissions not granted.", Snackbar.LENGTH_LONG)
                        //.setAnchorView(R.id.fab)
                        .show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_log_out -> {
                handleLogout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleLogout() {
        clearUserSession()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()  // Finish the current activity
    }

    private fun clearUserSession() {
        // Example for clearing SharedPreferences
       val sharedUtility = SharedUtility(applicationContext)
        sharedUtility.clear()

        // If using authentication tokens or other session data, clear them here
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}