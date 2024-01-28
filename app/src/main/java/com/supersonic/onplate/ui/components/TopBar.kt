package com.supersonic.onplate.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.supersonic.onplate.R
import com.supersonic.onplate.ui.theme.ONPLATETheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    onBackClick: () -> Unit = {},
    isEnableBackIcon: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(title = {
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = colorScheme.onPrimary,
            style = typography.titleLarge
        )
    },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(colorScheme.primary),
        navigationIcon = {
            if (isEnableBackIcon) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = colorScheme.onPrimary)
                }
            }
        },
        actions = actions,
    )
}


@Preview
@Composable
private fun TopBarPreview() {
    ONPLATETheme {
            TopBar(
                title = stringResource(id = R.string.app_name),
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = colorScheme.onPrimary)
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Settings, contentDescription = null, tint = colorScheme.onPrimary)
                    }
                })
    }

}