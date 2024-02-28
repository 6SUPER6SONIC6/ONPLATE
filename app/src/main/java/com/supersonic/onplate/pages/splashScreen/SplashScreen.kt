package com.supersonic.onplate.pages.splashScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.supersonic.onplate.R
import com.supersonic.onplate.navigation.NavigationDestination
import com.supersonic.onplate.ui.components.TopBar

object SplashScreenDestination : NavigationDestination {
    override val route = "splash"
    override val titleRes = R.string.app_name
}

@Composable
fun SplashScreen(
    viewModel: SplashScreenViewModel,
    onNavigationNext: () -> Unit
) {
    Scaffold(
        topBar = { SplashTopBar()},
        content = { SplashScreenContent(
            modifier = Modifier.padding(it),
            onNavigationNext = onNavigationNext
        )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SplashTopBar() {
    TopBar(title = stringResource(SplashScreenDestination.titleRes))
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