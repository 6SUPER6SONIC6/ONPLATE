package com.supersonic.onplate.pages.recipe

import androidx.lifecycle.ViewModel
import com.supersonic.onplate.models.Recipe
import com.supersonic.onplate.utils.MockUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeScreenViewModel @Inject constructor() : ViewModel() {

    private val recipesList = MockUtils.loadMockRecipes()
    fun getRecipeById(recipeId: Int): Recipe{
        return recipesList[recipeId]
    }
}