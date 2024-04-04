package com.supersonic.onplate.pages.newRecipe

sealed class NewRecipeUiState {
    object BaseContent : NewRecipeUiState()
    object Camera: NewRecipeUiState()
    data class PhotoView(val initialPhotoIndex: Int): NewRecipeUiState()
}