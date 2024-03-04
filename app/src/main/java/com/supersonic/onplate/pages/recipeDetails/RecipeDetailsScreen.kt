package com.supersonic.onplate.pages.recipeDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.supersonic.onplate.R
import com.supersonic.onplate.models.Recipe
import com.supersonic.onplate.models.toRecipe
import com.supersonic.onplate.navigation.NavigationDestination
import com.supersonic.onplate.pages.newRecipe.directions.Step
import com.supersonic.onplate.pages.newRecipe.ingredients.Ingredient
import com.supersonic.onplate.ui.components.ContentCard
import com.supersonic.onplate.ui.components.ContentDialog
import com.supersonic.onplate.ui.components.HorizontalSlider
import com.supersonic.onplate.ui.components.TopBar
import com.supersonic.onplate.ui.theme.ONPLATETheme
import com.supersonic.onplate.utils.MockUtils
import kotlinx.coroutines.launch

object RecipeScreenDestination : NavigationDestination {
    override val route = "recipe_details"
    override val titleRes = R.string.screenTitle_recipe
    const val recipeIdArg = "recipeId"
    val routeWithArgs = "$route/{$recipeIdArg}"
}

@Composable
fun RecipeDetailsScreen(
    viewModel: RecipeDetailsViewModel,
    navigateToEditRecipe: (Int) -> Unit,
    onBackClick: () -> Unit,
) {

    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            RecipeTopBar(
                onBackClick = onBackClick,
                navigateToEditRecipe = { navigateToEditRecipe(uiState.value.id) },
                onDelete = {
                    coroutineScope.launch {
                        viewModel.deleteRecipe()
                        onBackClick()
                    }
                }
            )
        },
        content = {
            RecipeScreenContent(modifier = Modifier.padding(it), recipe = uiState.value.toRecipe())
        }
    )

}

@Composable
private fun RecipeTopBar(onBackClick: () -> Unit, navigateToEditRecipe: () -> Unit, onDelete: () -> Unit) {
    
    var openDeleteConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    
    TopBar(
        title = stringResource(RecipeScreenDestination.titleRes),
        onBackClick = onBackClick,
        actions = {
            IconButton(onClick = navigateToEditRecipe, colors = IconButtonDefaults.iconButtonColors(contentColor = colorScheme.onPrimary)) {
                Icon(Icons.Outlined.Edit, contentDescription = null)
            }
            IconButton(onClick = { openDeleteConfirmationDialog = true }, colors = IconButtonDefaults.iconButtonColors(contentColor = colorScheme.onPrimary)) {
                Icon(Icons.Outlined.Delete, contentDescription = null)
            }
        })

    if (openDeleteConfirmationDialog){
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                openDeleteConfirmationDialog = false
                onDelete()
            },
            onDeleteCancel = { openDeleteConfirmationDialog = false }
        )


    }
}

@Composable
private fun RecipeScreenContent(modifier: Modifier, recipe: Recipe) {

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        OverviewCard(recipe.title, recipe.description, recipe.cookingTimeString)
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
private fun OverviewCard(title: String, description: String, cookingTime: String) {

    ContentCard(cardTitle = stringResource(R.string.cardTitle_overview), modifier = Modifier.padding(8.dp)){
        Text(
            text = cookingTime,
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            directionsList.forEach {step ->
                val stepCount = directionsList.indexOf(step) + 1
                Column {
                    Text(
                        text = "Step $stepCount",
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = typography.titleMedium
                    )
                    Text(
                        text = step.value,
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = typography.bodyLarge
                    )
                }
            }
        }
    }

}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit
) {
    ContentDialog(
        title = "Delete",
        onConfirm = onDeleteConfirm,
        onCancel = onDeleteCancel,
        icon = { Icon(Icons.Outlined.Delete, contentDescription = null, modifier = Modifier.size(46.dp)) }) {
        Text(text = "Do you wanna delete the recipe?")
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
