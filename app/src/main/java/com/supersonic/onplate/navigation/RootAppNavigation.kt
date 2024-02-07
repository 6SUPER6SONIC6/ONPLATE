package com.supersonic.onplate.navigation

import androidx.compose.material3.Text
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
                onNavigationToRecipe = {selectedRecipe ->
                    navController.navigate("${Routes.RecipeScreen.route}/${selectedRecipe.id}") {
                    }
                },
                onNavigationToAddRecipe = { navController.navigate(route = Routes.AddRecipeScreen.route) },
            )


        }

        // Recipe Screen

        composable(Routes.RecipeScreen.route + "/{recipeId}") { backStackEntry ->

            val viewModel = hiltViewModel<RecipeScreenViewModel>()

            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull()

            if (recipeId != null) {
                val recipe = viewModel.getRecipeById(recipeId)

                RecipeScreen(viewModel = viewModel, onBackClick = {
                                                                  navController.navigateUp()
                }, recipe = recipe)
            } else {
                Text("Error: Recipe ID not found =(")
            }


        }

        // Add Recipe Screen

        composable(Routes.AddRecipeScreen.route) {
            val viewModel = hiltViewModel<AddRecipeScreenViewModel>()

            AddRecipeScreen(viewModel = viewModel)

        }

    }
}