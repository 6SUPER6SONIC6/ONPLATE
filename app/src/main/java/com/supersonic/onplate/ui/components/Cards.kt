package com.supersonic.onplate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.supersonic.onplate.models.Recipe
import com.supersonic.onplate.ui.theme.ONPLATETheme
import com.supersonic.onplate.utils.MockUtils

@Composable
fun RecipeCard(
    modifier: Modifier = Modifier,
    recipe: Recipe,
    onItemClick: (Recipe) -> Unit,
) {
    Card(
        modifier = modifier
            .padding(12.dp)
            .fillMaxWidth()
            .height(360.dp)
            .clickable { onItemClick(recipe) },
        shape = CardDefaults.shape,
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(colorScheme.secondaryContainer)
    ) {

            if (recipe.photos.isNotEmpty()){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(recipe.photos.first())
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }


        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
            .fillMaxSize()
            ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 16.dp)
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = recipe.title,
                    style = typography.titleLarge,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = recipe.description,
                    style = typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 8.dp)
                )


            }

            Text(
                text = recipe.cookingTimeString,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                ,
                style = typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )


            
            Icon(Icons.Outlined.FavoriteBorder, contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(22.dp)
                    .clickable { })
        }
    }
}

/**
 * A customizable card with a title and variable content.
 *
 * @param modifier The modifier to be applied to the entire card.
 * @param cardTitle The title displayed at the top of the card.
 * @param content The content to be displayed inside the card.
 */
@Composable
fun ContentCard(
    modifier: Modifier = Modifier,
    cardTitle: String,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 128.dp)
                .padding(top = 16.dp)
                .border(1.dp, colorScheme.onSecondaryContainer, RoundedCornerShape(8.dp))
                .background(colorScheme.secondaryContainer, RoundedCornerShape(8.dp)),
            content = content)
        Surface(
            modifier = Modifier
            .align(Alignment.TopCenter),
            color =  colorScheme.onSecondaryContainer,
            shape = RoundedCornerShape(4.dp)) {
            Text(
                text = cardTitle,
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp),
                style = typography.titleLarge,
                color = colorScheme.surface)
        }
    }
}

@Preview(widthDp = 480)
@Composable
private fun RecipeCardPreview() {

    val recipe = MockUtils.loadMockRecipes()[0]

    ONPLATETheme {
        RecipeCard(recipe = recipe, onItemClick = {})
    }
}

@Preview(widthDp = 480, showBackground = false)
@Composable
private fun ContentCardPreview() {
    ONPLATETheme {

            ContentCard(cardTitle = "Overview"){
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
                        style = typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Spaghetti and meatballs are a classic family-friendly dinner." +
                                " This recipe is great for batch cooking so you can save extra portions in the freezer.",
                        modifier = Modifier.padding(top = 2.dp),
                        style = typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }




    }
}