package com.example.housingroperty.permissions

import androidx.compose.runtime.Composable

enum class PermissionType {
    LOCATION,
    NOTIFICATION
}

enum class PermissionStatus {
    GRANTED,
    DENIED,
    NOT_DETERMINED
}

interface PermissionController {
    fun getPermissionStatus(permission: PermissionType): PermissionStatus
    fun requestPermission(permission: PermissionType, onResult: (PermissionStatus) -> Unit)
    fun openAppSettings()
    fun openExternalMap(latitude: Double, longitude: Double, label: String)
}

@Composable
expect fun rememberPermissionController(): PermissionController
