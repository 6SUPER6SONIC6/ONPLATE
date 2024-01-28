package com.supersonic.onplate.pages.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.supersonic.onplate.ui.components.ContentCard
import com.supersonic.onplate.ui.components.TopBar

@Composable
fun RecipeScreen(
    viewModel: RecipeScreenViewModel,
) {
    Scaffold(
        topBar = {
            RecipeTopBar()
        },
        content = {
            RecipeScreenContent(modifier = Modifier.padding(it))
        }
    )


    
}

@Composable
private fun RecipeTopBar() {
    TopBar(title = "Recipe Screen")
}

@Composable
private fun RecipeScreenContent(modifier: Modifier) {

    Column(
        modifier = modifier.padding(top = 8.dp)
    ) {
        ContentCard(cardTitle = "Overview") {
            Text(
                text = "45 min",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )

            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pasta",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Spaghetti and meatballs are a classic family-friendly dinner." +
                            " This recipe is great for batch cooking so you can save extra portions in the freezer.",
                    modifier = Modifier.padding(top = 2.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
