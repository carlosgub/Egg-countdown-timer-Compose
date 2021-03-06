/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.green
import com.example.androiddevchallenge.ui.theme.orange
import com.example.androiddevchallenge.ui.theme.orangeMenu
import com.example.androiddevchallenge.ui.theme.red
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var showStart by mutableStateOf(true)
var cooking by mutableStateOf(false)
var isPause by mutableStateOf(true)
var progress by mutableStateOf(362f)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxHeight()
            .background(
                Brush.verticalGradient(
                    listOf(Color.Transparent, orange)
                )
            )
    ) {
        item {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menu",
                tint = orangeMenu,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Soft Boiled",
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold,
                        color = orangeMenu
                    ),
                    modifier = Modifier
                        .padding(
                            top = 20.dp
                        )
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Eggs",
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold,
                        color = orangeMenu
                    ),
                    modifier = Modifier
                        .padding(
                            top = 4.dp
                        )
                        .align(Alignment.CenterHorizontally)
                )
                Image(
                    painter = painterResource(R.drawable.ic_boiled_egg),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .size(140.dp)
                )
                Text(
                    text = "3 minutes",
                    style = MaterialTheme.typography.subtitle2.copy(
                        color = orangeMenu
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp)
                )
                ConstraintLayout(
                    Modifier
                        .fillMaxWidth()
                        .size(220.dp)
                        .padding(top = 20.dp)
                ) {
                    val (box, circle, text) = createRefs()
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .constrainAs(box) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .background(Color.White)
                    )
                    AnimatedCircle(
                        Modifier
                            .height(190.dp)
                            .fillMaxWidth()
                            .constrainAs(circle) {
                                top.linkTo(box.top, margin = 5.dp)
                                start.linkTo(box.start)
                                end.linkTo(box.end)
                            },
                        progress
                    )
                    var seconds = (((progress - 2) % 120) / 2).toInt()
                    Text(
                        text = "${((progress - 2) / 120).toInt()}:${if (seconds < 10) "0$seconds" else seconds}",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier
                            .constrainAs(text) {
                                top.linkTo(circle.top, margin = 4.dp)
                                start.linkTo(circle.start, margin = 4.dp)
                                end.linkTo(circle.end, margin = 4.dp)
                                bottom.linkTo(circle.bottom, margin = 4.dp)
                            }
                    )
                }
            }
            if (showStart) {
                ShowStartButton(coroutineScope)
            } else {
                ShowBottomButtons(coroutineScope)
            }
        }
    }
}

@Composable
fun ShowStartButton(coroutineScope: CoroutineScope) {
    Row(
        modifier = Modifier
            .padding(
                start = 48.dp,
                end = 48.dp,
                bottom = 24.dp,
                top = 20.dp
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                showStart = false
                cooking = true
                coroutineScope.launch {
                    setAnimation()
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = green),
            modifier = Modifier
                .clip(CircleShape)
                .size(80.dp)
        ) {
            Text(
                text = "Start",
                color = Color.White,
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Composable
fun ShowBottomButtons(coroutineScope: CoroutineScope) {
    Row(
        modifier = Modifier
            .padding(
                start = 48.dp,
                end = 48.dp,
                bottom = 24.dp,
                top = 20.dp
            )
            .fillMaxWidth()
    ) {
        Button(
            onClick = {
                cooking = !isPause
                coroutineScope.launch {
                    setAnimation()
                }
                isPause = !isPause
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            modifier = Modifier
                .clip(CircleShape)
                .size(80.dp)
        ) {
            Text(
                text = if (isPause) "Pause" else "Resume",
                color = orangeMenu,
                style = MaterialTheme.typography.caption
            )
        }
        Spacer(
            modifier = Modifier
                .weight(1f)
        )
        Button(
            onClick = {
                showStart = true
                cooking = false
                isPause = true
                progress = 362f
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = red),
            modifier = Modifier
                .clip(CircleShape)
                .size(80.dp)
        ) {
            Text(
                text = "Stop",
                color = Color.White,
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}

private const val DividerLengthInDegrees = 1.8f

@Composable
fun AnimatedCircle(
    modifier: Modifier = Modifier,
    sweep: Float
) {
    val stroke = with(LocalDensity.current) { Stroke(5.dp.toPx()) }
    Canvas(modifier) {
        val innerRadius = (size.minDimension - stroke.width) / 2
        val halfSize = size / 2.0f
        val topLeft = Offset(
            halfSize.width - innerRadius,
            halfSize.height - innerRadius
        )
        val size = Size(innerRadius * 2, innerRadius * 2)
        val startAngle = -90f
        drawArc(
            color = orangeMenu,
            startAngle = startAngle + DividerLengthInDegrees / 2,
            sweepAngle = sweep - DividerLengthInDegrees,
            topLeft = topLeft,
            size = size,
            useCenter = false,
            style = stroke
        )
    }
}

suspend fun setAnimation() {
    while (cooking) {
        progress -= 1
        if (progress <= 2) {
            progress = 362f
            cooking = false
            showStart = true
        }
        delay(500)
    }
}
