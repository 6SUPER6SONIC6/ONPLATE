package com.supersonic.onplate.pages.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.supersonic.onplate.R
import com.supersonic.onplate.models.Recipe
import com.supersonic.onplate.models.RecipeUiState
import com.supersonic.onplate.models.toRecipeUiState
import com.supersonic.onplate.navigation.NavigationDestination
import com.supersonic.onplate.ui.components.Fab
import com.supersonic.onplate.ui.components.RecipeCard
import com.supersonic.onplate.ui.components.RecipeTextField
import com.supersonic.onplate.ui.components.TopBar
import com.supersonic.onplate.ui.theme.ONPLATETheme
import kotlinx.coroutines.launch

object MainScreenDestination : NavigationDestination {
    override val route = "main"
    override val titleRes = R.string.app_name
}

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel,
    onNavigationToRecipe: (Int) -> Unit,
    onNavigationToAddRecipe: () -> Unit,
) {

    val allRecipesList by viewModel.searchResults.collectAsState()
    val favoriteRecipesList = allRecipesList.filter { it.favorite }
    var showFavorite by remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            MainTopBar(
                onShowFavoritesClick = { showFavorite = !showFavorite },
                favoriteClicked = showFavorite,
                search = {
                    RecipeTextField(
                        value = viewModel.searchQuery,
                        onValueChange = {viewModel.onSearchQueryChange(it)},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        },
        content = { paddingValues ->
            MainScreenContent(
                    modifier = Modifier
                        .padding(paddingValues),
                    recipeList = if (showFavorite) favoriteRecipesList else allRecipesList,
                    onRecipeClick = onNavigationToRecipe,
                    onFavoriteClick = {coroutineScope.launch {
                        viewModel.updateRecipe(it)
                    }}
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
private fun MainTopBar(
    onShowFavoritesClick: () -> Unit,
    favoriteClicked: Boolean = false,
    search: @Composable (() -> Unit)? = null
) {
    var searchEnabled by remember {
        mutableStateOf(false)
    }

    TopBar(
        title = stringResource(MainScreenDestination.titleRes),
        search = search,
        searchEnabled = searchEnabled,
        isEnableBackIcon = false,
        actions = {
            //Search
            IconButton(onClick = {
                searchEnabled = !searchEnabled
            }) {
                Icon(Icons.Filled.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            }
            //Favorite
            IconButton(onClick = onShowFavoritesClick) {
                Icon(
                    imageVector = if (favoriteClicked) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            //Settings
//            IconButton(onClick = {
//                Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show()
//            }) {
//                Icon(Icons.Filled.Settings, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
//            }
        })
}



@Composable
fun MainScreenContent(
    modifier: Modifier,
    recipeList: List<Recipe>,
    onRecipeClick: (Int) -> Unit,
    onFavoriteClick: (RecipeUiState) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (recipeList.isEmpty()) {
            Text(
                text = "You haven't saved any recipes yet.",
                textAlign = TextAlign.Center,
                style = typography.titleLarge)
        } else {
            RecipeList(
                recipeList = recipeList,
                onFavoriteClick = { onFavoriteClick(it) },
                onRecipeClick = { onRecipeClick(it) },
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RecipeList(
    recipeList: List<Recipe>,
    onRecipeClick: (Int) -> Unit,
    onFavoriteClick: (RecipeUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(recipeList, key = { it.id }){ recipe ->
            RecipeCard(
                recipe = recipe.toRecipeUiState(),
                onItemClick = { onRecipeClick(it) },
                onFavoriteClick = { onFavoriteClick(it) },
                modifier = Modifier
                    .fillMaxSize()
                    .animateItemPlacement()
            )


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


