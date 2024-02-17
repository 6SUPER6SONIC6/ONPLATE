package com.supersonic.onplate.pages.main

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.supersonic.onplate.R
import com.supersonic.onplate.models.Recipe
import com.supersonic.onplate.ui.components.Fab
import com.supersonic.onplate.ui.components.RecipeCard
import com.supersonic.onplate.ui.components.TopBar
import com.supersonic.onplate.ui.theme.ONPLATETheme

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel,
    onNavigationToRecipe: (Recipe) -> Unit,
    onNavigationToAddRecipe: () -> Unit,
) {

    val recipes = viewModel.loadRecipes()

    Scaffold(
        topBar = {
            MainTopBar()
        },
        content = {
            MainScreenContent(
                modifier = Modifier.padding(it),
                recipes = recipes,
                onNavigationToRecipe = onNavigationToRecipe,
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            Fab(
                onClick = { onNavigationToAddRecipe.invoke() },
                icon = {
                    Icon(Icons.Filled.Add, contentDescription = null)
                },
                text = stringResource(R.string.fab_newRecipe)
            )
        }
    )

}

@Composable
private fun MainTopBar() {
    val context = LocalContext.current

    TopBar(title = stringResource(id = R.string.app_name),
        isEnableBackIcon = false,
        actions = {
            IconButton(onClick = {
                Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Filled.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            }
            IconButton(onClick = {
                Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Filled.Settings, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            }
        })
}
@Composable
private fun MainScreenContent(
    modifier: Modifier,
    recipes: List<Recipe>,
    onNavigationToRecipe: (Recipe) -> Unit,
) {

    LazyColumn(modifier = modifier) {
        items(recipes){recipe ->
            RecipeCard(recipe = recipe){
                onNavigationToRecipe.invoke(recipe)
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    ONPLATETheme {
        MainScreen(viewModel = hiltViewModel<MainScreenViewModel>(),
            onNavigationToRecipe = { }
        ) { }

    }
}


