package com.supersonic.onplate.pages.newRecipe.components

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.supersonic.onplate.ui.components.ContentDialog
import com.supersonic.onplate.utils.permission.Permission

@Composable
fun CameraPermissionDialog(
    context: Context,
    onCancel: () -> Unit,
    onPermissionGranted: (Boolean) -> Unit
) {
    Permission(
        permissions = listOf(Manifest.permission.CAMERA),
        rationale = "You said you wanted a picture, so I'm going to have to ask for permission.",
        permissionNotAvailableContent = {
            ContentDialog(
                title = "Camera Permission",
                confirmButtonText = "Go to settings",
                cancelButtonText = "Not now",
                onConfirm = {
                    context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    })
                },
                onCancel = onCancel) {
                Text(
                    text = "ONPLATE needs access to the camera so you can take photos and add them to your recipe.\n" +
                            "Setting > Permissions > Camera",
                    style = typography.bodyMedium
                )
            }
        },
        onCancelDialog = onCancel,
        onPermissionGranted = { onPermissionGranted(it) }
    )
}

@Composable
fun ReadImagesPermissionDialog(
    context: Context,
    onCancel: () -> Unit,
    onPermissionGranted: (Boolean) -> Unit
) {
    val permissions = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        listOf(READ_MEDIA_IMAGES)
    } else {
        listOf(READ_EXTERNAL_STORAGE)
    }
    Permission(
        permissions = permissions,
        rationale = "READ_EXTERNAL_STORAGE",
        permissionNotAvailableContent = {
            ContentDialog(
                title = "Storage Permission",
                confirmButtonText = "Go to settings",
                cancelButtonText = "Not now",
                onConfirm = {
                    context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    })
                },
                onCancel = onCancel) {
                Text(
                    text = "ONPLATE needs access to the camera so you can take photos and add them to your recipe.\n" +
                            "Setting > Permissions > Camera",
                    style = typography.bodyMedium
                )
            }
        },
        onCancelDialog = onCancel,
        onPermissionGranted = onPermissionGranted
    )
}