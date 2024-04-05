package com.supersonic.onplate.pages.editRecipe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.supersonic.onplate.R
import com.supersonic.onplate.navigation.NavigationDestination
import com.supersonic.onplate.pages.newRecipe.NewRecipeScreenBody
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
    val recipeUiState = viewModel.recipeUiState

    NewRecipeScreenBody(
        modifier = Modifier,
        recipeUiState = recipeUiState,
        onRecipeValueChange = viewModel::updateUiState,
        screenUiState = viewModel.screenUiState.collectAsState().value,
        onBackClick = onBackClick,
        topBarTitle = stringResource(EditRecipeScreenDestination.titleRes),
        openCamera = { viewModel.openCamera() },
        openPhotoView = {viewModel.openPhotoView(it)},
        onNavigateBack = {viewModel.navigateBack()},
        onSaveClick = {
            coroutineScope.launch {
                viewModel.updateRecipe()
                onBackClick()
            }
        })


}