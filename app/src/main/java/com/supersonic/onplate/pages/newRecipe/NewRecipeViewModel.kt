package com.supersonic.onplate.pages.newRecipe

import android.content.ContentResolver
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supersonic.onplate.data.Media
import com.supersonic.onplate.data.MediaRepository
import com.supersonic.onplate.data.RecipesRepository
import com.supersonic.onplate.models.RecipeUiState
import com.supersonic.onplate.models.addEmptyIngredient
import com.supersonic.onplate.models.addEmptyStep
import com.supersonic.onplate.models.isValid
import com.supersonic.onplate.models.toRecipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewRecipeViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
    private val mediaRepository: MediaRepository
) : ViewModel() {
    var recipeUiState by mutableStateOf(RecipeUiState())
        private set

    private val _screenUiState = MutableStateFlow<NewRecipeUiState>(NewRecipeUiState.BaseContent)
    val screenUiState = _screenUiState.asStateFlow()
    private var previousScreenUiState: NewRecipeUiState? = null

    private val _images = MutableStateFlow<List<Media>>(emptyList())
    val images:StateFlow<List<Media>> = _images

    var isPhotoPickerOpened by mutableStateOf(false)
        private set

    var scrollPosition by mutableIntStateOf(0)
        private set

    init {
        recipeUiState.addEmptyIngredient()
        recipeUiState.addEmptyStep()
    }

    fun updateUiState(newRecipeUiState: RecipeUiState) {
        recipeUiState = newRecipeUiState.copy()
    }

    fun updateScrollPosition(updatedScrollPosition: Int){
        scrollPosition = updatedScrollPosition
    }

    suspend fun saveRecipe() {
        if (recipeUiState.isValid()) {
            recipesRepository.insertRecipe(recipeUiState.toRecipe())
        }
    }

    fun openCamera() {
        _screenUiState.value = NewRecipeUiState.Camera
    }

    fun openPhotoView(initialPhotoIndex: Int) {
        previousScreenUiState = _screenUiState.value
        _screenUiState.value = NewRecipeUiState.PhotoView(initialPhotoIndex)
    }

    fun openPhotoPicker(){
        isPhotoPickerOpened = !isPhotoPickerOpened
    }

    fun navigateBack() {
        _screenUiState.value = previousScreenUiState ?: NewRecipeUiState.BaseContent
        previousScreenUiState = null
    }

    fun loadImages(contentResolver: ContentResolver){
        viewModelScope.launch {
            val images = mediaRepository.getImages(contentResolver)
            _images.value = images
        }
    }
}