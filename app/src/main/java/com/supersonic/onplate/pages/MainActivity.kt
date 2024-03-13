 package com.supersonic.onplate.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.supersonic.onplate.navigation.RootAppNavigation
import com.supersonic.onplate.ui.theme.ONPLATETheme
import dagger.hilt.android.AndroidEntryPoint

 @AndroidEntryPoint
 class MainActivity : ComponentActivity() {

//     private val requestPermissionLauncher = registerForActivityResult(
//         ActivityResultContracts.RequestPermission()
//     ) { isGranted ->
//         if (isGranted){
//             Log.i("camera", "Permission granted")
//         } else {
//             Log.i("camera", "Permission denied")
//         }
//     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ONPLATETheme {
                RootAppNavigation()
            }
        }
    }


}
