package com.supersonic.onplate.pages.splashScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SplashScreen(
    viewModel: SplashScreenViewModel,
    onNavigationNext: () -> Unit
) {
    Scaffold(
        topBar = { TopBar()},
        content = { SplashScreenContent(
            modifier = Modifier.padding(it),
            onNavigationNext = onNavigationNext
        )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        title = { 
            Text(text = "Splash Screen")
        },
        colors = TopAppBarDefaults.topAppBarColors(colorScheme.primary)
    )
}

@Composable
private fun SplashScreenContent(
    modifier: Modifier,
    onNavigationNext: () -> Unit
) {
    Column(modifier = modifier) {
        Text(text = "Go to main screen!",
            modifier = modifier.clickable {
                onNavigationNext.invoke()
            })

    }
}