package com.supersonic.onplate.pages.main

sealed class MainScreenUiState {
    data object Search: MainScreenUiState()
    data object Favorite: MainScreenUiState()
}