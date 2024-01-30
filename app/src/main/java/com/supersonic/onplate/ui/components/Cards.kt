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
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.supersonic.onplate.ui.theme.ONPLATETheme

@Composable
fun RecipeCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    cookingTime: Int,
) {
    Card(
        modifier = modifier
            .padding(12.dp)
            .fillMaxWidth()
            .height(240.dp),
        shape = CardDefaults.shape,
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Surface(shape = RoundedCornerShape(8.dp),
            modifier = modifier
                .fillMaxWidth()
                .height(140.dp),) {
            Box(modifier = modifier
                .fillMaxSize()
                .background(colorScheme.tertiary)){
                Text(text = "IMAGE", modifier = modifier.align(Alignment.Center))
            }
        }
        Box(modifier = modifier
            .fillMaxSize()
            ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    modifier = modifier,
                    style = typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = description,
                    modifier = modifier.padding(start = 32.dp, end = 32.dp, bottom = 4.dp, top = 2.dp),
                    style = typography.bodySmall,
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = "$cookingTime min",
                modifier = modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                style = typography.labelSmall
            )

            Icon(Icons.Filled.Delete, contentDescription = null,
                modifier = modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
                    .size(22.dp)
                    .clickable { })
            
            Icon(Icons.Outlined.FavoriteBorder, contentDescription = null,
                modifier = modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
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
    ONPLATETheme {
        RecipeCard(
            title = "Pasta",
            description = "Spaghetti and meatballs are a classic family-friendly dinner." +
                " This recipe is great for batch cooking so you can save extra portions in the freezer.",
            cookingTime = 45)
    }
}

@Preview(widthDp = 480, showBackground = true)
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