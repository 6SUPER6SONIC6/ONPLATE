package com.supersonic.onplate.pages.newRecipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.supersonic.onplate.db.RecipesRepository
import com.supersonic.onplate.models.RecipeUiState
import com.supersonic.onplate.models.addEmptyIngredient
import com.supersonic.onplate.models.addEmptyStep
import com.supersonic.onplate.models.isValid
import com.supersonic.onplate.models.toRecipe
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewRecipeViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    var recipeUiState by mutableStateOf(RecipeUiState())
        private set

    init {
        recipeUiState.addEmptyIngredient()
        recipeUiState.addEmptyStep()
    }

    fun updateUiState(newRecipeUiState: RecipeUiState) {
        recipeUiState = newRecipeUiState.copy()
    }

    suspend fun saveRecipe() {
        if (recipeUiState.isValid()) {
            recipesRepository.insertRecipe(recipeUiState.toRecipe())
        }
    }
}