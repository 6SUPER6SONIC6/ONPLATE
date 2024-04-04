package com.supersonic.onplate.pages.editRecipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supersonic.onplate.db.RecipesRepository
import com.supersonic.onplate.models.RecipeUiState
import com.supersonic.onplate.models.isValid
import com.supersonic.onplate.models.toRecipe
import com.supersonic.onplate.models.toRecipeUiState
import com.supersonic.onplate.pages.newRecipe.NewRecipeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRecipeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    var recipeUiState by mutableStateOf(RecipeUiState())
        private set

    private val _screenUiState = MutableStateFlow<NewRecipeUiState>(NewRecipeUiState.BaseContent)
    val screenUiState = _screenUiState.asStateFlow()
    private var previousScreenUiState: NewRecipeUiState? = null

    private val recipeId: Int = checkNotNull(savedStateHandle[EditRecipeScreenDestination.recipeIdArg])

    init {
        viewModelScope.launch {
            recipeUiState = recipesRepository.getRecipeStream(recipeId)
                .filterNotNull()
                .first()
                .toRecipeUiState()
        }
    }

    fun updateUiState(newRecipeUiState: RecipeUiState) {
        recipeUiState = newRecipeUiState.copy()
    }

    suspend fun updateRecipe() {
        if (recipeUiState.isValid()){
            recipesRepository.updateRecipe(recipeUiState.toRecipe())
        }
    }

    fun openCamera() {
        _screenUiState.value = NewRecipeUiState.Camera
    }

    fun openPhotoView(initialPhotoIndex: Int) {
        previousScreenUiState = _screenUiState.value
        _screenUiState.value = NewRecipeUiState.PhotoView(initialPhotoIndex)
    }

    fun navigateBack() {
        _screenUiState.value = previousScreenUiState ?: NewRecipeUiState.BaseContent
        previousScreenUiState = null
    }

}