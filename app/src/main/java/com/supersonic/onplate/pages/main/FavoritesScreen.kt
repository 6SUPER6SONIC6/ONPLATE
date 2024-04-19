package com.supersonic.onplate.pages.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.supersonic.onplate.R
import com.supersonic.onplate.navigation.NavigationDestination
import com.supersonic.onplate.ui.components.TopBar
import kotlinx.coroutines.launch

object FavoritesScreenDestination : NavigationDestination {
    override val route = "favorite"
    override val titleRes = R.string.app_name
}

@Composable
fun FavoritesScreen(
    viewModel: FavoriteScreenViewModel,
    onNavigationToRecipe: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val favoriteScreenUiState by viewModel.favoriteScreenUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            FavoriteTopBar(onBackClick = onBackClick)
        }
    ) {
        MainScreenContent(
            modifier = Modifier.padding(it),
            recipeList = favoriteScreenUiState.recipeList,
            onRecipeClick = onNavigationToRecipe,
            onFavoriteClick = {coroutineScope.launch {
                viewModel.updateRecipe(it)
            } }
        )
    }
}

@Composable
private fun FavoriteTopBar(
    onBackClick: () -> Unit
) {
    TopBar(
        title ="Favorite Recipes",
        onBackClick = onBackClick
    )
}