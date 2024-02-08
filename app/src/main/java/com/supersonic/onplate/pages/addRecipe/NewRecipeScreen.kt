package com.supersonic.onplate.pages.addRecipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.supersonic.onplate.R
import com.supersonic.onplate.ui.components.ContentCard
import com.supersonic.onplate.ui.components.TopBar
import com.supersonic.onplate.ui.theme.ONPLATETheme

@Composable
fun NewRecipeScreen(
    viewModel: NewRecipeScreenViewModel,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
                 AddRecipeTopBar(onBackClick)
        },
        content = {
                  AddRecipeScreenContent(modifier = Modifier.padding(it))
        },
    )
}


@Composable
private fun AddRecipeTopBar(onBackClick: () -> Unit) {
    TopBar(title = stringResource(R.string.screen_NewRecipe), onBackClick = onBackClick)
}

@Composable
private fun AddRecipeScreenContent(modifier: Modifier) {
    Column(
        modifier = modifier
    ) {

        /*Photos
        ContentCard(cardTitle = stringResource(R.string.cardTitle_photos), modifier = Modifier.padding(8.dp)) {

            LazyRow(
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
            ) {
                items(photos.size) { photo ->
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

                item {
                    Surface(
                        modifier = Modifier
                            .size(120.dp, 100.dp)
                            .padding(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, colorScheme.onSecondaryContainer)
                    ){
                        Box(modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = null)
                                Text(text = "Add Photo")
                            }
                        }
                    }
                }
            }

        } */

    }

}

@Composable
private fun OverviewCard() {

    ContentCard(cardTitle = stringResource(id = R.string.cardTitle_overview),
        modifier = Modifier.padding(8.dp)) {

        var title by remember { mutableStateOf("") }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier,
                label = { Text("Title") }
            )
        }

    }
}

@Preview(showSystemUi = true)
@Composable
private fun OverviewCardPreview() {
    ONPLATETheme {
        OverviewCard()
    }
}