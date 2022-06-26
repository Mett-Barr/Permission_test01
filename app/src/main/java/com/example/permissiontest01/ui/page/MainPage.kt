package com.example.permissiontest01.ui.page

import android.Manifest
import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.permissiontest01.BluetoothController

val PERMISSION_ARRAY: Array<String> = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION)

@Composable
fun MainPage() {

    val activity = LocalContext.current as Activity

    val bluetoothController = BluetoothController(activity)
//    var bluetoothController: BluetoothController

//    val test = BluetoothController(activity).Test().launch(arrayOf(
//        Manifest.permission.ACCESS_FINE_LOCATION,
//        Manifest.permission.ACCESS_COARSE_LOCATION))

    val test = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        Log.d("!!!", "Test: $permissions")

        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.

                bluetoothController.scan()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            } else -> {
            // No location access granted.
        }
        }
    }

    val test2 = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissions ->

        Log.d("!!!", "Test: $permissions")

    }


//    BluetoothController(activity).also {
//        bluetoothController = it
//        test = it.Test().launch(PERMISSION_ARRAY)
//    }

    @Composable
    fun test() {
        bluetoothController.Test()
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.align(Alignment.Center)) {
            Button(onClick = {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                    launcher12.launch(Manifest.permission.BLUETOOTH_SCAN)
//                }

//                bluetoothController.scan()

//                test.launch(PERMISSION_ARRAY)
//                test2.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                test2.launch(Manifest.permission.ACCESS_COARSE_LOCATION)

                bluetoothController.enableBluetooth()

//                bluetoothController.scan()

            }) {
                Text(text = "Permission")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Permissions")
            }
        }
    }

    @Composable
    fun Test() = bluetoothController.Test().launch(arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION))
}