package com.supersonic.onplate.pages.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supersonic.onplate.db.RecipesRepository
import com.supersonic.onplate.models.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    val mainScreenUiState: StateFlow<MainScreenUiState> =
        recipesRepository.getAllRecipesStream().map { MainScreenUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MainScreenUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class MainScreenUiState(val recipeList: List<Recipe> = listOf())