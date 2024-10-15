package com.supersonic.onplate.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.supersonic.onplate.ui.components.ContentDialog

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Permission(
    permissions: List<String>,
    rationale: String = "This permission is important for this app. Please grant the permission.",
    permissionNotAvailableContent: @Composable () -> Unit = {},
    onCancelDialog: () -> Unit,
    onPermissionGranted: (Boolean) -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(permissions)

    when {
        permissionState.allPermissionsGranted -> onPermissionGranted(permissionState.allPermissionsGranted)
        permissionState.shouldShowRationale -> permissionNotAvailableContent()
        !permissionState.allPermissionsGranted -> Rational(
            text = rationale,
            onRequestPermission = { permissionState.launchMultiplePermissionRequest()},
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