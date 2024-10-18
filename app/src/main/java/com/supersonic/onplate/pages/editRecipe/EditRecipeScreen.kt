package com.supersonic.onplate.pages.editRecipe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.supersonic.onplate.R
import com.supersonic.onplate.navigation.NavigationDestination
import com.supersonic.onplate.pages.newRecipe.NewRecipeScreenBody
import com.supersonic.onplate.pages.newRecipe.NewRecipeViewModel
import kotlinx.coroutines.launch

object EditRecipeScreenDestination : NavigationDestination {
    override val route = "edit_recipe"
    override val titleRes = R.string.screenTitle_editRecipe
    const val recipeIdArg = "recipeId"
    val routeWithArgs = "$route/{$recipeIdArg}"
}

@Composable
fun EditRecipeScreen(
    editRecipeViewModel: EditRecipeViewModel,
    newRecipeViewModel: NewRecipeViewModel,
    onBackClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val recipeUiState = editRecipeViewModel.recipeUiState

    NewRecipeScreenBody(
        modifier = Modifier,
        recipeUiState = recipeUiState,
        screenUiState = editRecipeViewModel.screenUiState.collectAsState().value,
        onRecipeValueChange = editRecipeViewModel::updateUiState,
        scrollPosition = editRecipeViewModel.scrollPosition,
        onScrollPositionChange = editRecipeViewModel::updateScrollPosition,
        topBarTitle = stringResource(EditRecipeScreenDestination.titleRes),
        openPhotoView = {editRecipeViewModel.openPhotoView(it)},
        openCamera = { editRecipeViewModel.openCamera() },
        onNavigateBack = {editRecipeViewModel.navigateBack()},
        onBackClick = onBackClick,
        galleryPhotos = newRecipeViewModel.images.collectAsState().value,
        updateImages = {},
        onSaveClick = {
            coroutineScope.launch {
                editRecipeViewModel.updateRecipe()
                onBackClick()
            }
        })


}