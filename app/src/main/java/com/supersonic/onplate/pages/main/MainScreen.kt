package com.supersonic.onplate.pages.main

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.supersonic.onplate.R
import com.supersonic.onplate.ui.components.Fab
import com.supersonic.onplate.ui.components.RecipeCard
import com.supersonic.onplate.ui.components.TopBar
import com.supersonic.onplate.ui.theme.ONPLATETheme

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel,
    onNavigationToRecipe: () -> Unit,
    onNavigationToAddRecipe: () -> Unit,
) {
    Scaffold(
        topBar = {
            MainTopBar()
        },
        content = {
            MainScreenContent(
                modifier = Modifier.padding(it),
                onNavigationToRecipe = onNavigationToRecipe,
                onNavigationToAddRecipe = onNavigationToAddRecipe
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            Fab(
                icon = {
                    Icon(Icons.Filled.Add, contentDescription = null)
                },
                text = "Add Recipe")
        }
    )

}

@Composable
private fun MainTopBar() {
    val context = LocalContext.current

    TopBar(title = stringResource(id = R.string.app_name),
        isEnableBackIcon = false,
        actions = {
            IconButton(onClick = {
                Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Filled.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            }
            IconButton(onClick = {
                Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Filled.Settings, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            }
        })
}
@Composable
private fun MainScreenContent(
    modifier: Modifier,
    onNavigationToRecipe: () -> Unit,
    onNavigationToAddRecipe: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Navigate to Recipe Screen",
            modifier = Modifier.clickable {
                onNavigationToRecipe.invoke()
            }
        )
        Text(
            text = "Navigate to Add Recipe Screen",
            modifier = Modifier.clickable {
                onNavigationToAddRecipe.invoke()
            }
        )

        RecipeCard(title = "Pasta", description = "Pasta with meatballs", cookingTime = 45)

    }
}

@Preview
@Composable
fun MainScreenPreview() {
    ONPLATETheme {
        MainScreen(viewModel = hiltViewModel<MainScreenViewModel>(),
            onNavigationToRecipe = { },
            onNavigationToAddRecipe = { })

    }
}


