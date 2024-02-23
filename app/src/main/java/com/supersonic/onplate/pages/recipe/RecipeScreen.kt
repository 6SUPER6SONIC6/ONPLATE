package com.supersonic.onplate.pages.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.supersonic.onplate.R
import com.supersonic.onplate.models.Recipe
import com.supersonic.onplate.pages.newRecipe.directions.Step
import com.supersonic.onplate.pages.newRecipe.ingredients.Ingredient
import com.supersonic.onplate.ui.components.ContentCard
import com.supersonic.onplate.ui.components.HorizontalSlider
import com.supersonic.onplate.ui.components.TopBar
import com.supersonic.onplate.ui.theme.ONPLATETheme
import com.supersonic.onplate.utils.MockUtils


@Composable
fun RecipeScreen(
    viewModel: RecipeScreenViewModel,
    onBackClick: () -> Unit,
    recipe: Recipe
) {

    Scaffold(
        topBar = {
            RecipeTopBar(onBackClick)
        },
        content = {
            RecipeScreenContent(modifier = Modifier.padding(it), recipe = recipe)
        }
    )

}

@Composable
private fun RecipeTopBar(onBackClick: () -> Unit) {
    TopBar(title = stringResource(R.string.screenTitle_recipe), onBackClick = onBackClick)
}

@Composable
private fun RecipeScreenContent(modifier: Modifier, recipe: Recipe) {

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        PhotosCard()
        OverviewCard(recipe.title, recipe.description, recipe.cookingTime)
        IngredientsCard(recipe.ingredients)
        DirectionsCard(recipe.directions)
    }
}

@Composable
private fun PhotosCard(photosList: List<String> = emptyList()) {

    ContentCard(cardTitle = stringResource(R.string.cardTitle_photos), modifier = Modifier.padding(8.dp)) {
        HorizontalSlider(sliderList = photosList)
    }

}

@Composable
private fun OverviewCard(title: String, description: String, cookingTime: Int) {

    ContentCard(cardTitle = stringResource(R.string.cardTitle_overview), modifier = Modifier.padding(8.dp)){
        Text(
            text = "$cookingTime min",
            style = typography.labelSmall,
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
                text = title,
                style = typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = description,
                modifier = Modifier.padding(top = 2.dp),
                style = typography.bodyLarge,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun IngredientsCard(ingredientsList: List<Ingredient>) {

    ContentCard(cardTitle = stringResource(R.string.cardTitle_ingredients), modifier = Modifier.padding(8.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ingredientsList.forEach{ingredient ->
                Text(text = ingredient.value, modifier = Modifier.padding(2.dp), style = typography.bodyLarge)
            }
        }
    }

}

@Composable
private fun DirectionsCard(directionsList: List<Step>) {

    ContentCard(cardTitle = stringResource(R.string.cardTitle_directions), modifier = Modifier.padding(8.dp)) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            directionsList.forEach {step ->
                val stepCount = directionsList.indexOf(step) + 1
                Column {
                    Text(text = "Step $stepCount", modifier = Modifier
                        .padding(2.dp)
                        .align(Alignment.CenterHorizontally), style = typography.titleMedium)
                    Text(text = step.value, modifier = Modifier.padding(2.dp), style = typography.bodyLarge)
                }
            }
        }
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RecipeScreenContentPreview() {

    val recipe = MockUtils.loadMockRecipes()[0]

    ONPLATETheme {
        RecipeScreenContent(modifier = Modifier, recipe)
    }

}

@Preview(heightDp = 1800 ,showBackground = true)
@Composable
private fun ContentCardsPreview() {
    ONPLATETheme {
        Column {
//            PhotosCard()
//            OverviewCard()
//            IngredientsCard()
//            DirectionsCard()
        }
    }
}
