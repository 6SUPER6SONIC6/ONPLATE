package com.supersonic.onplate.pages.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    val searchBarTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        focusedTextColor = colorScheme.onPrimary,
        unfocusedTextColor = colorScheme.onPrimary,
        cursorColor = colorScheme.tertiaryContainer,
        selectionColors = TextSelectionColors(handleColor = colorScheme.tertiaryContainer, backgroundColor = colorScheme.tertiary)
    )

    val allRecipesList by viewModel.recipesList.collectAsState()
    val favoriteRecipesList = allRecipesList.filter { it.favorite }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            MainTopBar(
                onShowFavoritesClick = { viewModel.updateUi(MainScreenUiState.Favorite) },
                favoriteClicked = viewModel.isFavoriteEnabled,
                onSearchClick = { viewModel.updateUi(MainScreenUiState.Search) },
                isSearchBarEnabled = viewModel.isSearchEnabled,
                searchBar = {
                    OutlinedTextField(
                        value = viewModel.searchQuery,
                        onValueChange = viewModel::onSearchQueryChange,
                        singleLine = true,
                        trailingIcon = {
                            //Search filters button
                            IconButton(onClick = {}) {
                                Icon(Icons.AutoMirrored.Outlined.List, contentDescription = null, tint = colorScheme.onPrimary)
                            }},
                        colors = searchBarTextFieldColors,
                        placeholder = {
                            Text(
                            text = "Search...",
                                color = colorScheme.background
                            ) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        },
        content = { paddingValues ->
            MainScreenContent(
                    modifier = Modifier
                        .padding(paddingValues),
                    recipeList = if (viewModel.isFavoriteEnabled) favoriteRecipesList else allRecipesList,
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
    onSearchClick: () -> Unit,
    isSearchBarEnabled: Boolean = false,
    searchBar: @Composable (() -> Unit)? = null
) {
    TopBar(
        title = stringResource(MainScreenDestination.titleRes),
        searchBar = searchBar,
        isSearchBarEnabled = isSearchBarEnabled,
        isBackIconEnabled = false,
        actions = {
            //Search
            IconButton(
                onClick = onSearchClick
            ) {
                if (isSearchBarEnabled){
                    Icon(Icons.Filled.Close, contentDescription = null, tint = colorScheme.onPrimary)
                } else {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = colorScheme.onPrimary)
                }

            }
            //Favorite
            IconButton(onClick = onShowFavoritesClick) {
                Icon(
                    imageVector = if (favoriteClicked) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = colorScheme.onPrimary
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

@Composable
private fun RecipeList(
    recipeList: List<Recipe>,
    onRecipeClick: (Int) -> Unit,
    onFavoriteClick: (RecipeUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(recipeList, key = { it.id }){ recipe ->
            Modifier
                .fillMaxSize()
            RecipeCard(
                recipe = recipe.toRecipeUiState(),
                onItemClick = { onRecipeClick(it) },
                onFavoriteClick = { onFavoriteClick(it) },
                modifier = Modifier.animateItem()
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


