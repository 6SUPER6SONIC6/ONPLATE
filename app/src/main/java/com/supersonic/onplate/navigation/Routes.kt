package com.supersonic.onplate.navigation

sealed class Routes(val route: String) {
    object SplashScreen : Routes("splashScreen")
    object MainScreen : Routes("mainScreen")
    object AddRecipeScreen : Routes("addRecipeScreen")
    object RecipeScreen : Routes("recipeScreen")
}