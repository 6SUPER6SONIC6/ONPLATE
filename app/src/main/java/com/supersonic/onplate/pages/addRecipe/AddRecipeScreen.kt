package com.supersonic.onplate.pages.addRecipe

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
fun AddRecipeScreen(
    viewModel: AddRecipeScreenViewModel
) {
    Scaffold(
        topBar = {
                 TopBar()
        },
        content = {
                  AddRecipeScreenContent(modifier = Modifier.padding(it))
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(title = {
        Text(text = "Add Recipe",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,)
    },
        colors = TopAppBarDefaults.topAppBarColors(colorScheme.primary))
}

@Composable
private fun AddRecipeScreenContent(modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        Text(text = "Add new Recipe")
    }

}