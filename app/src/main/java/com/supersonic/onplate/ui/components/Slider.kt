package com.supersonic.onplate.ui.components

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.supersonic.onplate.ui.theme.ONPLATETheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalSlider(
    modifier: Modifier = Modifier,
    sliderList: List<Uri>,
    forwardIcon: ImageVector = Icons.Filled.KeyboardArrowRight,
    backwardIcon: ImageVector = Icons.Filled.KeyboardArrowLeft,
    imageHeight : Dp = 220.dp
) {

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        sliderList.size
    }

    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {

        HorizontalPager(
            modifier = modifier.align(Alignment.Center),
            state = pagerState,
            pageSize = PageSize.Fill,

        ) {page ->
            Box(modifier = modifier
                .clip(RoundedCornerShape(8.dp)),){
                AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                    .data(sliderList[page])
                    .crossfade(true)
                    .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .height(imageHeight)
                        .alpha(if (pagerState.currentPage == page) 1f else 0.5f))
            }
        }

        IconButton(modifier = modifier
            .align(Alignment.CenterStart)
            .height(imageHeight),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = colorScheme.primary,
                disabledContentColor = colorScheme.secondary),
            enabled = pagerState.canScrollBackward,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            } ) {
            Icon(backwardIcon, contentDescription = "backward")
        }

        IconButton(modifier = modifier
            .align(Alignment.CenterEnd)
            .height(imageHeight),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = colorScheme.primary,
                disabledContentColor = colorScheme.secondary),
            enabled = pagerState.canScrollForward,
            onClick = {
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        } ) {
            Icon(forwardIcon, contentDescription = "forward")
        }

        Row(
            modifier
                .height(16.dp)
                .align(Alignment.BottomCenter),
        ) {
            repeat(sliderList.size) {
                val color = if (pagerState.currentPage == it) colorScheme.primary else colorScheme.secondary

                Box(modifier = modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .size(8.dp)
                    .background(color)
                    .clickable {
                        scope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    })
                }
            }
        }

    }



val photos = listOf(
    "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/recipe-image-legacy-id-1035708_10-fdc5ae0.jpg?quality=90&webp=true&resize=440,400",
    "https://s23209.pcdn.co/wp-content/uploads/2022/09/220223_DD_Spaghetti-Meatballs_198.jpg",
    "https://realfood.tesco.com/media/images/RFO-Meatballs-LargeHero-1400x919-070796c4-0009-4b24-ac5a-b00e1085ecea-0-1400x920.jpg",
    "https://cravinghomecooked.com/wp-content/uploads/2020/02/spaghetti-and-meatballs-1.jpg",
    "https://www.simplyrecipes.com/thmb/Boo37yZBqeSpmELBIP_BBX_yVlU=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/Simply-Recipes-Spaghetti-And-Meatballs-LEAD-3-40bdae68ea144751a8e0a4b0f972af2d.jpg",
    "https://www.courtneyssweets.com/wp-content/uploads/2018/09/garlic-olive-oil-pasta-with-meatballs-and-spinach-8.jpg",
)

@Preview
@Composable
fun SliderPreview() {
    ONPLATETheme {
        ContentCard(cardTitle = "Photos") {
            HorizontalSlider(sliderList = emptyList())
        }


    }
    
}