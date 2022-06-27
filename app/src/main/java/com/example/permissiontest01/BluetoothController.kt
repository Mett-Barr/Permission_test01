package com.example.permissiontest01

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.*

class BluetoothController(private val activity: Activity) {

    val context = activity as Context

    private val manager: BluetoothManager =
        activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    private val adapter = manager.adapter

    private val builder = ScanSettings.Builder()
        // 掃描模式
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)

        // 回調類型
        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)

        // 掃描器匹配模式
        .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
        .build()


    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            Log.d("!!! scanCallback", "onScanResult  callbackType:$callbackType result:$result")
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            Log.d("!!! scanCallback", "onBatchScanResults  result:$results")
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d("!!! scanCallback", "onScanFailed  errorCode:$errorCode")
        }
    }

    private fun permission() {
        activity.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
    }

    fun enableBluetooth() {

        permission()

        if (!adapter.isEnabled) {
            // 方法一：請求打開藍牙
            Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE).also {
                activity.startActivityForResult(
                    it,
                    1
                )
            }

            // 方法二：半靜默打開藍牙
            // 低版本會靜默打開藍牙，高版本會請求打開藍牙
            // adapter.enable()
        }

    }


    fun scan() {
        // 4.3 & 4.4
        // adapter.startLeScan(BluetoothAdapter.LeScanCallback)
        // adapter.stopLeScan(BluetoothAdapter.LeScanCallback)

        // 5.0以上
        val scanner = adapter.bluetoothLeScanner


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
//                return
            }
//            scanner.startScan(null, builder, scanCallback)

            CoroutineScope(Job()).launch {
//                repeat(10) {
//                    scanner.stopScan(scanCallback)
//                    delay(3000)
//                    scanner.startScan(null, builder, scanCallback)
//                }
            }
        }


        // device

//        val device = adapter.bondedDevices

        adapter.startDiscovery()

    }


    // Permission

//    val test = activity.startActivityForResult()

//    val locationPermissionRequest = activity.registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        when {
//            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
//                // Precise location access granted.
//            }
//            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
//                // Only approximate location access granted.
//            } else -> {
//            // No location access granted.
//        }
//        }
//    }


    @Composable
    fun Test() = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        Log.d("!!!", "Test: ")

        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            }
            else -> {
                // No location access granted.
            }
        }
    }
}

open class BluetoothBroadcastReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(p0: Context?, intent: Intent) {

        if (intent.action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                BluetoothAdapter.STATE_ON -> Log.d("!!!", "onReceive: STATE_ON")
                BluetoothAdapter.STATE_OFF -> Log.d("!!!", "onReceive: STATE_OFF")
                BluetoothAdapter.STATE_TURNING_ON -> Log.d("!!!", "onReceive: STATE_TURNING_ON")
                BluetoothAdapter.STATE_TURNING_OFF -> Log.d("!!!", "onReceive: STATE_TURNING_OFF")
                else -> Log.d("!!!", "onReceive: else")
            }
        }


        when (intent.action) {
            BluetoothDevice.ACTION_FOUND -> {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                val deviceName = device?.name
                val deviceHardwareAddress = device?.address // MAC address

                Log.d("!!!", "onReceive: $deviceName   $deviceHardwareAddress")
            }
        }

    }
}