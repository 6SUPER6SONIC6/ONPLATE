package com.supersonic.onplate.pages.main

import androidx.lifecycle.ViewModel
import com.supersonic.onplate.models.Recipe
import com.supersonic.onplate.utils.MockUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {

    fun loadRecipes(): List<Recipe> {
        return MockUtils.loadMockRecipes()
    }
}