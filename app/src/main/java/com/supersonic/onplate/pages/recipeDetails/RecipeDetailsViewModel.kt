package com.supersonic.onplate.pages.recipeDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supersonic.onplate.db.RecipesRepository
import com.supersonic.onplate.models.RecipeUiState
import com.supersonic.onplate.models.toRecipe
import com.supersonic.onplate.models.toRecipeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    private val recipeId: Int = checkNotNull(savedStateHandle[RecipeScreenDestination.recipeIdArg])

    val uiState: StateFlow<RecipeUiState> =
        recipesRepository.getRecipeStream(recipeId)
            .filterNotNull()
            .map {
                it.toRecipeUiState()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = RecipeUiState()
            )

    suspend fun deleteRecipe() {
        recipesRepository.deleteRecipe(uiState.value.toRecipe())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}