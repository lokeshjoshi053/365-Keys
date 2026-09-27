package com.example.housingroperty.permissions

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

class AndroidPermissionController(
    private val context: Context,
    private val onRequestNotification: ((PermissionStatus) -> Unit) -> Unit,
    private val onRequestLocation: ((PermissionStatus) -> Unit) -> Unit
) : PermissionController {

    override fun getPermissionStatus(permission: PermissionType): PermissionStatus {
        return when (permission) {
            PermissionType.NOTIFICATION -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val granted = context.checkSelfPermission(
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                    if (granted) PermissionStatus.GRANTED else PermissionStatus.DENIED
                } else {
                    val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                    val enabled = nm?.areNotificationsEnabled() ?: true
                    if (enabled) PermissionStatus.GRANTED else PermissionStatus.DENIED
                }
            }
            PermissionType.LOCATION -> {
                val fineGranted = context.checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                val coarseGranted = context.checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                if (fineGranted || coarseGranted) {
                    PermissionStatus.GRANTED
                } else {
                    PermissionStatus.DENIED
                }
            }
        }
    }

    override fun requestPermission(permission: PermissionType, onResult: (PermissionStatus) -> Unit) {
        val currentStatus = getPermissionStatus(permission)
        if (currentStatus == PermissionStatus.GRANTED) {
            onResult(PermissionStatus.GRANTED)
            return
        }

        when (permission) {
            PermissionType.NOTIFICATION -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    onRequestNotification(onResult)
                } else {
                    val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                    if (nm?.areNotificationsEnabled() == false) {
                        openAppSettings()
                    }
                    onResult(getPermissionStatus(permission))
                }
            }
            PermissionType.LOCATION -> {
                onRequestLocation(onResult)
            }
        }
    }

    override fun openAppSettings() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent(Settings.ACTION_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun openExternalMap(latitude: Double, longitude: Double, label: String) {
        try {
            val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude(${Uri.encode(label)})")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(mapIntent)
        } catch (e: Exception) {
            try {
                val fallbackUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude")
                val browserIntent = Intent(Intent.ACTION_VIEW, fallbackUri).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(browserIntent)
            } catch (_: Exception) {}
        }
    }
}

@Composable
actual fun rememberPermissionController(): PermissionController {
    val context = LocalContext.current
    var notificationCallback by remember { mutableStateOf<((PermissionStatus) -> Unit)?>(null) }
    var locationCallback by remember { mutableStateOf<((PermissionStatus) -> Unit)?>(null) }

    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        val status = if (isGranted) PermissionStatus.GRANTED else PermissionStatus.DENIED
        notificationCallback?.invoke(status)
        notificationCallback = null
    }

    val locationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val status = if (isGranted) PermissionStatus.GRANTED else PermissionStatus.DENIED
        locationCallback?.invoke(status)
        locationCallback = null
    }

    return remember(context) {
        AndroidPermissionController(
            context = context,
            onRequestNotification = { cb ->
                notificationCallback = cb
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                    val enabled = nm?.areNotificationsEnabled() ?: true
                    cb(if (enabled) PermissionStatus.GRANTED else PermissionStatus.DENIED)
                }
            },
            onRequestLocation = { cb ->
                locationCallback = cb
                locationLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        )
    }
}
