package com.supersonic.onplate.utils

import android.Manifest
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.supersonic.onplate.ui.components.ContentDialog

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Permission(
    permission: String = Manifest.permission.CAMERA,
    rationale: String = "This permission is important for this app. Please grant the permission.",
    permissionNotAvailableContent: @Composable () -> Unit = {},
    onCancelDialog: () -> Unit,
    onPermissionGranted: (Boolean) -> Unit
) {
    val permissionState = rememberPermissionState(permission = permission)

    when {
        permissionState.hasPermission -> onPermissionGranted(permissionState.hasPermission)
        permissionState.shouldShowRationale -> permissionNotAvailableContent()
        !permissionState.hasPermission -> Rational(
            text = rationale,
            onRequestPermission = { permissionState.launchPermissionRequest() },
            onCancelDialog = onCancelDialog
        )
    }
}

@Composable
private fun Rational(
    text: String,
    onRequestPermission: () -> Unit,
    onCancelDialog: () -> Unit
) {
    ContentDialog(title = "Permission request", onConfirm = onRequestPermission , onCancel = onCancelDialog) {
        Text(text = text)
    }
}