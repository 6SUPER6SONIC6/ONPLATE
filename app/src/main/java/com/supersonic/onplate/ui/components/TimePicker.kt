package com.supersonic.onplate.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.supersonic.onplate.R
import com.supersonic.onplate.ui.theme.ONPLATETheme
import com.supersonic.onplate.utils.TimePickerLists


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimePickerDialog(
    initialHour: Int = 0,
    initialMinute: Int = 0,
    onHourSelected: (Int) -> Unit,
    onMinuteSelected: (Int) -> Unit,
    onCancel: () -> Unit) {

    val timePickerListHours = TimePickerLists.getTimePickerListHours()
    val timePickerListMinutes = TimePickerLists.getTimePickerListMinutes()

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

    
    Dialog(onDismissRequest = onCancel) {

        Surface(
            shape = shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {



            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.textField_label_cooking_time),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(4.dp), style = typography.titleLarge)
                Row(
                    modifier = Modifier.align(Alignment.Center).border(2.dp, colorScheme.outline, shapes.medium).padding(4.dp)
                ) {

                    VerticalPager(
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.CenterVertically),
                        state = hoursPagerState
                    ) { hour ->

                        selectedHour = hour

                        val numberAlpha: Float by animateFloatAsState(
                            targetValue = if (hoursPagerState.currentPage == hour) 1f else 0.2f,
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = LinearEasing),
                            label = "Hour Alpha Animation"
                        )
                        Text(
                            text = if (selectedHour < 10) "0$selectedHour" else selectedHour.toString(),
                            fontSize = 60.sp,
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                                .alpha(numberAlpha),
                            textAlign = TextAlign.Center
                        )

                    }

                    Box(modifier = Modifier) {
                        Text(
                            ":",
                            fontSize = 60.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .wrapContentSize(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    }



                    VerticalPager(
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.CenterVertically),
                        state = minutesPagerState
                    ) { minute ->
                        selectedMinute = minute
                        val numberAlpha: Float by animateFloatAsState(
                            targetValue = if (minutesPagerState.currentPage == minute) 1f else 0.2f,
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = LinearEasing),
                            label = "Minute Alpha Animation"
                        )

                        Text(
                            text = if (selectedMinute < 10) "0$selectedMinute" else selectedMinute.toString(),
                            fontSize = 60.sp,
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                                .alpha(numberAlpha),
                            textAlign = TextAlign.Center
                        )
                    }

                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 8.dp)

                ){

                    TextButton(onClick = {onCancel()}){
                        Text("Cancel", style = typography.bodyLarge)
                    }
                    TextButton(onClick = {
                        onHourSelected(hoursPagerState.currentPage)
                        onMinuteSelected(minutesPagerState.currentPage)
                        onCancel()
                    },
                        enabled = saveButtonEnabled
                        ) {
                        Text("Save", style = typography.bodyLarge)
                    }

                }

            }
        }
    }
}

@Preview
@Composable
private fun TimePickerDialogPreview() {
    ONPLATETheme {
        TimePickerDialog(onMinuteSelected = {}, onHourSelected = {}, onCancel = {})
    }
}