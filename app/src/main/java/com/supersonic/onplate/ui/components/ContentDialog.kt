package com.supersonic.onplate.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.supersonic.onplate.R
import com.supersonic.onplate.ui.theme.ONPLATETheme

@Composable
fun ContentDialog(
    title: String? = null,
    onConfirm: () -> Unit,
    confirmButtonText: String? = null,
    onCancel: () -> Unit,
    cancelButtonText: String? = null,
    confirmButtonEnabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    body: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onCancel, properties = DialogProperties(usePlatformDefaultWidth = false)) {

        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .widthIn(min = 280.dp, max = 560.dp)
                .padding(24.dp)
                .alpha(0.95f)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (icon != null) {
                    icon()
                }

                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
//                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                body()

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 24.dp)

                ) {
                        TextButton(onClick = onCancel) {
                            Text(
                                text = cancelButtonText ?: stringResource(R.string.dialog_button_cancel),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        TextButton(
                            onClick = onConfirm,
                            enabled = confirmButtonEnabled
                        ) {
                            Text(
                                text = confirmButtonText ?: stringResource(R.string.dialog_button_confirm),
                                style = MaterialTheme.typography.bodyLarge
                            )

                        }
                }

            }

        }
    }
}

@Preview
@Composable
private fun ContentDialogPreview() {
    ONPLATETheme {
        ContentDialog(title = "Delete", onCancel = {}, onConfirm = {}, icon = {
            Icon(
                Icons.Outlined.Delete,
                modifier = Modifier.size(32.dp),
                contentDescription = null
            )
        }
        ) {
            Text(text = "Do you wanna delete the recipe?")
        }
    }
}