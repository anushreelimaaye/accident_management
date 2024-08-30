package com.quantbit.accidentmanagement.ui

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat


abstract class BaseFragment : Fragment() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPermissionsLauncher()
    }

    private fun setupPermissionsLauncher() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.values.all { it }
            onPermissionsResult(allGranted)
        }
    }

    protected fun requestPermissions(permissions: Array<String>) {
        val permissionsNeeded = permissions.filter {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsNeeded.isNotEmpty()) {
            permissionLauncher.launch(permissionsNeeded)
        } else {
            onPermissionsResult(true)
        }
    }

    abstract fun onPermissionsResult(allGranted: Boolean)
}