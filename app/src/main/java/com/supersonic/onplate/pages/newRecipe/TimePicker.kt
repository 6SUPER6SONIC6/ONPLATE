package com.supersonic.onplate.pages.newRecipe

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supersonic.onplate.R
import com.supersonic.onplate.ui.components.ContentDialog
import com.supersonic.onplate.ui.theme.ONPLATETheme
import com.supersonic.onplate.utils.TimePickerLists

val timePickerListHours = TimePickerLists.getTimePickerListHours()
val timePickerListMinutes = TimePickerLists.getTimePickerListMinutes()

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimePickerDialog(
    initialHour: Int = 0,
    initialMinute: Int = 0,
    onHourSelected: (Int) -> Unit,
    onMinuteSelected: (Int) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {

    val hoursPagerState = rememberPagerState(
        initialPage = initialHour,
        initialPageOffsetFraction = 0f,
    ) {
        timePickerListHours.size
    }

    val minutesPagerState = rememberPagerState(
        initialPage = initialMinute,
        initialPageOffsetFraction = 0f
    ) {
        timePickerListMinutes.size
    }

    var selectedMinute: Int
    var selectedHour: Int

    var saveButtonEnabled by remember {
        mutableStateOf(false)
    }

    saveButtonEnabled = hoursPagerState.currentPage != 0 || minutesPagerState.currentPage != 0

    val timePickerTextStyle = typography.displayLarge.copy(fontSize = 60.sp)

    ContentDialog(
        title = stringResource(R.string.textField_label_cooking_time),
        onConfirm = {
            onHourSelected(hoursPagerState.currentPage)
            onMinuteSelected(minutesPagerState.currentPage)
            onConfirm()
            onCancel()
        },
        onCancel = { onCancel() },
        confirmButtonEnabled = saveButtonEnabled
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 24.dp)
                .border(1.dp, colorScheme.outline, shapes.medium)
                .size(width = 220.dp, height = 80.dp)
        ) {

            VerticalPager(
                modifier = Modifier
                    .size(80.dp),
                state = hoursPagerState,
            ) { hour ->

                selectedHour = hour

                val numberAlpha: Float by animateFloatAsState(
                    targetValue = if (hoursPagerState.currentPage == hour) 1f else 0.2f,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearEasing
                    ),
                    label = "Hour Alpha Animation"
                )

                Text(
                    text = if (selectedHour < 10) "0$selectedHour" else selectedHour.toString(),
                    style = timePickerTextStyle,
                    modifier = Modifier
                        .alpha(numberAlpha)
                )

            }

            Text(
                ":",
                style = timePickerTextStyle,
            )

            VerticalPager(
                modifier = Modifier
                    .size(80.dp),
                state = minutesPagerState
            ) { minute ->

                selectedMinute = minute

                val numberAlpha: Float by animateFloatAsState(
                    targetValue = if (minutesPagerState.currentPage == minute) 1f else 0.2f,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearEasing
                    ),
                    label = "Minute Alpha Animation"
                )

                Text(
                    text = if (selectedMinute < 10) "0$selectedMinute" else selectedMinute.toString(),
                    style = timePickerTextStyle,
                    modifier = Modifier
                        .alpha(numberAlpha),
                )
            }

        }
    }
}

@Preview
@Composable
private fun TimePickerDialogPreview() {
    ONPLATETheme {
        TimePickerDialog(onMinuteSelected = {}, onHourSelected = {}, onCancel = {}, onConfirm = {})
    }
}