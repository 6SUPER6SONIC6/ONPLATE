package com.supersonic.onplate.pages.newRecipe

sealed class NewRecipeUiState {
    data object BaseContent : NewRecipeUiState()
    data object Camera: NewRecipeUiState()
    data class PhotoView(val initialPhotoIndex: Int): NewRecipeUiState()
}