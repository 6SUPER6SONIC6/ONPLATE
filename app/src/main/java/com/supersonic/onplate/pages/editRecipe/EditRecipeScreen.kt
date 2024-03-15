package com.supersonic.onplate.pages.editRecipe

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.supersonic.onplate.R
import com.supersonic.onplate.navigation.NavigationDestination
import com.supersonic.onplate.pages.newRecipe.NewRecipeScreenContent
import com.supersonic.onplate.ui.components.TopBar
import kotlinx.coroutines.launch

object EditRecipeScreenDestination : NavigationDestination {
    override val route = "edit_recipe"
    override val titleRes = R.string.screenTitle_editRecipe
    const val recipeIdArg = "recipeId"
    val routeWithArgs = "$route/{$recipeIdArg}"
}

@Composable
fun EditRecipeScreen(
    viewModel: EditRecipeViewModel,
    onBackClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { EditRecipeTopBar(onBackClick) }
    ){ innerPadding ->
        NewRecipeScreenContent(
            modifier = Modifier.padding(innerPadding),
            recipeUiState = viewModel.recipeUiState,
            onRecipeValueChange = viewModel::updateUiState
        ) {
            coroutineScope.launch {
                viewModel.updateRecipe()
                onBackClick()
            }
        }
    }


}

@Composable
private fun EditRecipeTopBar(onBackClick: () -> Unit) {
    TopBar(title = stringResource(EditRecipeScreenDestination.titleRes), onBackClick = onBackClick)
}