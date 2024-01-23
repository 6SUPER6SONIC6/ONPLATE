 package com.supersonic.onplate

import android.os.Bundle
import android.window.SplashScreen
import android.window.SplashScreenView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.supersonic.onplate.navigation.Routes
import com.supersonic.onplate.pages.splashScreen.SplashScreen
import com.supersonic.onplate.pages.splashScreen.SplashScreenViewModel
import com.supersonic.onplate.ui.theme.ONPLATETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootAppNavigation()
            
        }
    }
}
 
 @Composable
 fun RootAppNavigation(
     modifier: Modifier = Modifier,
     navController: NavHostController = rememberNavController(),
     startDestination: String
 ) {
     NavHost(
         modifier = modifier,
         navController = navController,
         startDestination = startDestination
     ) {

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

     }
 }