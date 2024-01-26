package com.supersonic.onplate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.supersonic.onplate.pages.addRecipe.AddRecipeScreen
import com.supersonic.onplate.pages.addRecipe.AddRecipeScreenViewModel
import com.supersonic.onplate.pages.main.MainScreen
import com.supersonic.onplate.pages.main.MainScreenViewModel
import com.supersonic.onplate.pages.recipe.RecipeScreen
import com.supersonic.onplate.pages.recipe.RecipeScreenViewModel
import com.supersonic.onplate.pages.splashScreen.SplashScreen
import com.supersonic.onplate.pages.splashScreen.SplashScreenViewModel

@Composable
fun RootAppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.SplashScreen.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {

        // SplashScreen

        composable(Routes.SplashScreen.route) {
            val viewModel = hiltViewModel<SplashScreenViewModel>()

            SplashScreen(
                viewModel = viewModel,
                onNavigationNext = {
                    navController.navigate(route = Routes.MainScreen.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        // Main Screen

        composable(Routes.MainScreen.route) {
            val viewModel = hiltViewModel<MainScreenViewModel>()

            MainScreen(
                viewModel = viewModel,
                onNavigationToRecipe = {
                    navController.navigate(route = Routes.RecipeScreen.route)
                },
                onNavigationToAddRecipe = {
                    navController.navigate(route = Routes.AddRecipeScreen.route)
                })
        }

        // Recipe Screen

        composable(Routes.RecipeScreen.route) {
            val viewModel = hiltViewModel<RecipeScreenViewModel>()

            RecipeScreen(viewModel = viewModel)

        }

        // Add Recipe Screen

        composable(Routes.AddRecipeScreen.route) {
            val viewModel = hiltViewModel<AddRecipeScreenViewModel>()

            AddRecipeScreen(viewModel = viewModel)

        }

    }
}