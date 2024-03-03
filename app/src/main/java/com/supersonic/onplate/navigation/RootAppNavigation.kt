package com.supersonic.onplate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.supersonic.onplate.pages.editRecipe.EditRecipeScreen
import com.supersonic.onplate.pages.editRecipe.EditRecipeScreenDestination
import com.supersonic.onplate.pages.editRecipe.EditRecipeViewModel
import com.supersonic.onplate.pages.main.MainScreen
import com.supersonic.onplate.pages.main.MainScreenDestination
import com.supersonic.onplate.pages.main.MainScreenViewModel
import com.supersonic.onplate.pages.newRecipe.NewRecipeScreen
import com.supersonic.onplate.pages.newRecipe.NewRecipeScreenDestination
import com.supersonic.onplate.pages.newRecipe.NewRecipeViewModel
import com.supersonic.onplate.pages.recipeDetails.RecipeDetailsScreen
import com.supersonic.onplate.pages.recipeDetails.RecipeDetailsViewModel
import com.supersonic.onplate.pages.recipeDetails.RecipeScreenDestination
import com.supersonic.onplate.pages.splashScreen.SplashScreen
import com.supersonic.onplate.pages.splashScreen.SplashScreenDestination
import com.supersonic.onplate.pages.splashScreen.SplashScreenViewModel

@Composable
fun RootAppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainScreenDestination.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {

        // SplashScreen
        composable(route = SplashScreenDestination.route) {
            val viewModel = hiltViewModel<SplashScreenViewModel>()

            SplashScreen(
                viewModel = viewModel,
                onNavigationNext = {
                    navController.navigate(route = MainScreenDestination.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        // Main Screen
        composable(route = MainScreenDestination.route) {
            val viewModel = hiltViewModel<MainScreenViewModel>()

            MainScreen(
                viewModel = viewModel,
                onNavigationToRecipe = {
                    navController.navigate("${RecipeScreenDestination.route}/${it}") {
                    }
                },
                onNavigationToAddRecipe = { navController.navigate(route = NewRecipeScreenDestination.route) },
            )


        }

        // Recipe Details Screen
        composable(route = RecipeScreenDestination.routeWithArgs, arguments = listOf(navArgument("recipeId"){
            type = NavType.IntType
        })) {

            val viewModel = hiltViewModel<RecipeDetailsViewModel>()

                RecipeDetailsScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.navigateUp() },
                    navigateToEditRecipe = { navController.navigate("${EditRecipeScreenDestination.route}/$it") })
        }

        // New Recipe Screen

        composable(route = NewRecipeScreenDestination.route) {
            val viewModel = hiltViewModel<NewRecipeViewModel>()

            NewRecipeScreen(viewModel = viewModel, onBackClick = {
                navController.navigateUp()
            })

        }

        composable(route = EditRecipeScreenDestination.routeWithArgs, arguments = listOf(navArgument(EditRecipeScreenDestination.recipeIdArg){
            type = NavType.IntType }
        )
        ) {
            val viewModel = hiltViewModel<EditRecipeViewModel>()

            EditRecipeScreen(
                viewModel = viewModel,
                onBackClick = { navController.navigate(MainScreenDestination.route) }
            )
        }

    }
}