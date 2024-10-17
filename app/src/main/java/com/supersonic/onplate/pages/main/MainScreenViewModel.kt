package com.supersonic.onplate.pages.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supersonic.onplate.data.RecipesRepository
import com.supersonic.onplate.models.Recipe
import com.supersonic.onplate.models.RecipeUiState
import com.supersonic.onplate.models.isValid
import com.supersonic.onplate.models.toRecipe
import com.supersonic.onplate.models.toRecipeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    private val recipesFlow = recipesRepository.getAllRecipesStream()

    var searchQuery by mutableStateOf("")
        private set

    var isSearchEnabled by mutableStateOf(false)
        private set

    var isFavoriteEnabled by mutableStateOf(false)
        private set



    val recipesList: StateFlow<List<Recipe>> =
        snapshotFlow { searchQuery }
            .combine(recipesFlow){ searchQuery, recipes ->
                when {
                    searchQuery.isNotEmpty() -> recipes.filter { recipe ->
                        recipe.toRecipeUiState().searchText.contains(searchQuery, ignoreCase = true)
                    }

                    else -> recipes
                }

            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = emptyList()
            )


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun updateUi(newUiState: MainScreenUiState) {
        when(newUiState){
            MainScreenUiState.Search -> onToggleSearch()
            MainScreenUiState.Favorite -> onToggleFavorite()
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }

    private fun onToggleSearch(){
        if (searchQuery.isNotEmpty()){
            onSearchQueryChange("")
        } else {
            isSearchEnabled = !isSearchEnabled
        }
    }

    private fun onToggleFavorite(){
        isFavoriteEnabled = !isFavoriteEnabled
    }

    suspend fun updateRecipe(recipeUiState: RecipeUiState) {
        if (recipeUiState.isValid()){
            recipesRepository.updateRecipe(recipeUiState.toRecipe())
        }
    }
}

