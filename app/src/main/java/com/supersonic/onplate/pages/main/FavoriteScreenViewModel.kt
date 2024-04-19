package com.supersonic.onplate.pages.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supersonic.onplate.db.RecipesRepository
import com.supersonic.onplate.models.Recipe
import com.supersonic.onplate.models.RecipeUiState
import com.supersonic.onplate.models.isValid
import com.supersonic.onplate.models.toRecipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoriteScreenViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
): ViewModel() {

    val favoriteScreenUiState: StateFlow<FavoriteScreenUiState> =
        recipesRepository.getFavoritesRecipesStream().map { FavoriteScreenUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = FavoriteScreenUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun updateRecipe(recipeUiState: RecipeUiState) {
        if (recipeUiState.isValid()){
            recipesRepository.updateRecipe(recipeUiState.toRecipe())
        }
    }

}

data class FavoriteScreenUiState(val recipeList: List<Recipe> = listOf())