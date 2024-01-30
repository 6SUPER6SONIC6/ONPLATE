package com.supersonic.onplate.pages.recipe

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.supersonic.onplate.R
import com.supersonic.onplate.ui.components.ContentCard
import com.supersonic.onplate.ui.components.TopBar
import com.supersonic.onplate.ui.theme.ONPLATETheme

val ingredients = listOf("8 good-quality pork sausages",
        "1 kg beef mince",
        "1 onion, finely chopped",
        "½ a large bunch flat-leaf parsley, finely chopped",
        "85g parmesan, grated, plus extra to serve (optional)",
        "100g fresh breadcrumbs",
        "2 eggs, beaten with a fork",
        "olive oil, for roasting",
        "spaghetti, to serve (about 100g per portion)")

val directions = listOf("First, make the meatballs." +
        "Split the skins of the sausages and squeeze out the" +
        " meat into a large mixing bowl.",
    "Add the mince, onion, parsley, parmesan, breadcrumbs, eggs and lots of seasoning." +
            " Get your hands in and mix together really well – the more you squeeze" +
            " and mash the mince, the more tender the meatballs will be.",
    "Heat the oven to 220C/200C fan/gas 7. Roll the mince mixture into about" +
            " 50 golf-ball-sized meatballs. Set aside any meatballs for freezing," +
            " allowing about five per portion, then spread the rest out in a large" +
            " roasting tin – the meatballs will brown better if spaced out a bit.",
    "Drizzle with a little oil (about 1 tsp per portion), shake to coat, then roast for 20-30 mins until browned.",
    "Meanwhile, make the sauce. Heat the olive oil in a large saucepan. Add the garlic cloves and sizzle for 1 min.",
    "Stir in the chopped tomatoes, red wine, if using, caster sugar, parsley and seasoning. Simmer for 15-20 mins " +
            "until slightly thickened.",
    "Stir in a few basil leaves, if using, spoon out any portions for freezing, then add the cooked meatballs to the " +
            "pan to keep warm while you cook the spaghetti in a pan of boiling, salted water.",
    "Spoon the sauce and meatballs over spaghetti, or stir them all together and serve with extra " +
            "parmesan and a few basil leaves, if you like."
    )

val photos = listOf(
    "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/recipe-image-legacy-id-1035708_10-fdc5ae0.jpg?quality=90&webp=true&resize=440,400",
    "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/recipe-image-legacy-id-1035708_10-fdc5ae0.jpg?quality=90&webp=true&resize=440,400",
    "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/recipe-image-legacy-id-1035708_10-fdc5ae0.jpg?quality=90&webp=true&resize=440,400",
    "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/recipe-image-legacy-id-1035708_10-fdc5ae0.jpg?quality=90&webp=true&resize=440,400",
    "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/recipe-image-legacy-id-1035708_10-fdc5ae0.jpg?quality=90&webp=true&resize=440,400",
    "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/recipe-image-legacy-id-1035708_10-fdc5ae0.jpg?quality=90&webp=true&resize=440,400",
    "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/recipe-image-legacy-id-1035708_10-fdc5ae0.jpg?quality=90&webp=true&resize=440,400",
    "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/recipe-image-legacy-id-1035708_10-fdc5ae0.jpg?quality=90&webp=true&resize=440,400",
    "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/recipe-image-legacy-id-1035708_10-fdc5ae0.jpg?quality=90&webp=true&resize=440,400"
)

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
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        //Overview Card
        ContentCard(cardTitle = stringResource(R.string.cardTitle_overview), modifier = Modifier.padding(8.dp)){
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
                    .padding(vertical = 16.dp, horizontal = 8.dp),
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

        //Ingredients
        ContentCard(cardTitle = stringResource(R.string.cardTitle_ingredients), modifier = Modifier.padding(8.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp)
            ) {
                ingredients.forEach{ingredient ->
                    Text(text = "$ingredient;", modifier = Modifier.padding(2.dp), style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        //Directions
        ContentCard(cardTitle = stringResource(R.string.cardTitle_directions), modifier = Modifier.padding(8.dp)) {
            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 8.dp)
            ) {
                directions.forEach {step ->
                    val stepCount = directions.indexOf(step) + 1
                    Column {
                        Text(text = "Step $stepCount", modifier = Modifier.padding(2.dp), style = MaterialTheme.typography.titleMedium)
                        Text(text = step, modifier = Modifier.padding(2.dp), style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }

        //Photos
        ContentCard(cardTitle = stringResource(R.string.cardTitle_photos), modifier = Modifier.padding(8.dp)) {

            LazyRow(
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
            ) {
                items(photos.size) {photo ->
                    Surface(
                        modifier = Modifier
                            .size(120.dp, 100.dp)
                            .padding(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, colorScheme.onSecondaryContainer)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(photos[photo])
                                .crossfade(true)
                                .build(),
                            contentScale = ContentScale.Crop,
                            contentDescription = null)
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecipeScreenContentPreview() {

    ONPLATETheme {
        RecipeScreenContent(modifier = Modifier)
    }

}
