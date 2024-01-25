package com.supersonic.onplate.pages.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.supersonic.onplate.ui.theme.Purple40

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel,
    onNavigationToRecipe: () -> Unit,
    onNavigationToAddRecipe: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar()
        },
        content = {
            MainScreenContent(
                modifier = Modifier.padding(it),
                onNavigationToRecipe = onNavigationToRecipe,
                onNavigationToAddRecipe = onNavigationToAddRecipe
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        title = {
        Text(text = "Main Screen",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis)
    },
        colors = TopAppBarDefaults.topAppBarColors(Purple40))
}
@Composable
private fun MainScreenContent(
    modifier: Modifier,
    onNavigationToRecipe: () -> Unit,
    onNavigationToAddRecipe: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Navigate to Recipe Screen",
            modifier = Modifier.clickable {
                onNavigationToRecipe.invoke()
            }
        )
        Text(
            text = "Navigate to Add Recipe Screen",
            modifier = Modifier.clickable {
                onNavigationToAddRecipe.invoke()
            }
        )
    }
}


