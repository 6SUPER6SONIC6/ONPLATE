package com.supersonic.onplate.pages.newRecipe.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.supersonic.onplate.R
import com.supersonic.onplate.ui.components.ContentDialog

@Composable
fun NavigateBackConfirmationDialog(
    onConfirmNavigationBack: () -> Unit,
    onCancelNavigationBack: () -> Unit
) {
    ContentDialog(
        title = "Navigate back?",
        confirmButtonText = "Navigate back",
        cancelButtonText = "Stay",
        onConfirm = onConfirmNavigationBack,
        onCancel = onCancelNavigationBack
    ) {
        Text(text = "Are you sure you want to go back?")
    }
}

@Composable
fun DeletePhotoDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit
) {
    ContentDialog(
        title = stringResource(R.string.delete_photo_dialog_title),
        icon = Icons.Outlined.Delete,
        onConfirm = onDeleteConfirm,
        onCancel = onDeleteCancel,
    ) {
        Text(text = "Delete this photo?")
    }
}