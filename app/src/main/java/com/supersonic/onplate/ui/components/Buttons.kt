package com.supersonic.onplate.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.supersonic.onplate.ui.theme.ONPLATETheme

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {  }
) {
    Box(
        modifier = modifier
    ) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(8.dp),
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = text,
                style = typography.titleMedium)
        }
    }
}

@Composable
fun Fab(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    text: String,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    onClick: () -> Unit = {  }
) {
    Box(
        modifier = modifier
    ) {
        ExtendedFloatingActionButton(
            onClick = onClick,
            elevation = elevation
        ) {
            icon.invoke()
            Text(text = text)
        }
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    ONPLATETheme {
        PrimaryButton(text = "Save")
    }
}

@Preview
@Composable
fun FabPreview() {
    ONPLATETheme {
        Fab(icon = {
            Icon(Icons.Filled.Add , contentDescription = null)
        },
            text = "Add Recipe")
    }
}