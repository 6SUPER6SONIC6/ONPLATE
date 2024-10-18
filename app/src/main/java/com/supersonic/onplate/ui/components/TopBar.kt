package com.supersonic.onplate.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
    searchBar: @Composable (() -> Unit)? = null,
    isSearchBarEnabled: Boolean = false,
    onBackClick: () -> Unit = {},
    isBackIconEnabled: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            when(isSearchBarEnabled){
                true -> { searchBar?.invoke() }
                false -> {
                    Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
//                    color = colorScheme.onPrimary,
                    style = typography.titleLarge
                )}
            }
    },
//        colors = TopAppBarDefaults.topAppBarColors(colorScheme.primary),
        navigationIcon = {
            if (isBackIconEnabled) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
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