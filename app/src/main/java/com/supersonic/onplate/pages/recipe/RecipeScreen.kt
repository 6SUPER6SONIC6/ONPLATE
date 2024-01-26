package com.supersonic.onplate.pages.recipe

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
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun RecipeScreen(
    viewModel: RecipeScreenViewModel,
) {
    Scaffold(
        topBar = {
            TopBar()
        },
        content = {
            RecipeScreenContent(modifier = Modifier.padding(it))
        }
    )


    
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(title = {
        Text(text = "Recipe Screen",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis)
    },
        colors = TopAppBarDefaults.topAppBarColors(colorScheme.primary))
}

@Composable
private fun RecipeScreenContent(modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        Text(text = "Recipe")
    }
}
