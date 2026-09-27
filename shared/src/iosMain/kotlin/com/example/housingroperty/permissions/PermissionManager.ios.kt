package com.example.housingroperty.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.CoreLocation.*
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.UserNotifications.*
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

private class LocationManagerDelegate(
    private val onAuthChanged: () -> Unit
) : NSObject(), CLLocationManagerDelegateProtocol {
    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        onAuthChanged()
    }
}

class IOSPermissionController : PermissionController {
    private var pendingLocationCallback: ((PermissionStatus) -> Unit)? = null

    private val delegate = LocationManagerDelegate {
        val status = getPermissionStatus(PermissionType.LOCATION)
        if (status != PermissionStatus.NOT_DETERMINED) {
            val cb = pendingLocationCallback
            pendingLocationCallback = null
            cb?.invoke(status)
        }
    }

    private val locationManager by lazy {
        val manager = CLLocationManager()
        manager.delegate = delegate
        manager
    }

    override fun getPermissionStatus(permission: PermissionType): PermissionStatus {
        return when (permission) {
            PermissionType.LOCATION -> {
                val status = CLLocationManager.authorizationStatus()
                when (status) {
                    kCLAuthorizationStatusAuthorizedWhenInUse,
                    kCLAuthorizationStatusAuthorizedAlways -> PermissionStatus.GRANTED
                    kCLAuthorizationStatusDenied,
                    kCLAuthorizationStatusRestricted -> PermissionStatus.DENIED
                    else -> PermissionStatus.NOT_DETERMINED
                }
            }
            PermissionType.NOTIFICATION -> {
                PermissionStatus.NOT_DETERMINED
            }
        }
    }

    override fun requestPermission(permission: PermissionType, onResult: (PermissionStatus) -> Unit) {
        when (permission) {
            PermissionType.LOCATION -> {
                val current = getPermissionStatus(PermissionType.LOCATION)
                if (current == PermissionStatus.GRANTED || current == PermissionStatus.DENIED) {
                    onResult(current)
                    return
                }
                pendingLocationCallback = onResult
                locationManager.requestWhenInUseAuthorization()
            }
            PermissionType.NOTIFICATION -> {
                val center = UNUserNotificationCenter.currentNotificationCenter()
                val options = UNAuthorizationOptionAlert or UNAuthorizationOptionBadge or UNAuthorizationOptionSound
                center.requestAuthorizationWithOptions(options) { granted, _ ->
                    dispatch_async(dispatch_get_main_queue()) {
                        val status = if (granted) PermissionStatus.GRANTED else PermissionStatus.DENIED
                        onResult(status)
                    }
                }
            }
        }
    }

    override fun openAppSettings() {
        val settingsUrl = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
        if (settingsUrl != null && UIApplication.sharedApplication.canOpenURL(settingsUrl)) {
            UIApplication.sharedApplication.openURL(settingsUrl)
        }
    }

    override fun openExternalMap(latitude: Double, longitude: Double, label: String) {
        val escapedLabel = label.replace(" ", "+")
        val appleMapsUrl = NSURL.URLWithString("http://maps.apple.com/?ll=$latitude,$longitude&q=$escapedLabel")
        if (appleMapsUrl != null && UIApplication.sharedApplication.canOpenURL(appleMapsUrl)) {
            UIApplication.sharedApplication.openURL(appleMapsUrl)
        } else {
            val googleMapsUrl = NSURL.URLWithString("https://maps.google.com/?q=$latitude,$longitude")
            if (googleMapsUrl != null) {
                UIApplication.sharedApplication.openURL(googleMapsUrl)
            }
        }
    }
}

@Composable
actual fun rememberPermissionController(): PermissionController {
    return remember { IOSPermissionController() }
}
