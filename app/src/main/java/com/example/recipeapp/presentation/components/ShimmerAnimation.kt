package com.example.recipeapp.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BuildDrawCacheParams
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerAnimation() {
    val infiniteTransition = rememberInfiniteTransition()
    val positionState = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, 100, easing = LinearEasing)
        )
    )

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        repeat(5) {
            BoxWithConstraints(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                val widthPx = with(LocalDensity.current) {
                    ((maxWidth - 32.dp).toPx())
                }
                val heightPx = with(LocalDensity.current) {
                    ((maxHeight - 16.dp).toPx())
                }
                Card(
                    shape = MaterialTheme.shapes.small
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(maxHeight)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.LightGray,
                                        Color.White,
                                        Color.LightGray
                                    ),
                                    start = Offset(
                                        widthPx * positionState.value - 200,
                                        heightPx * positionState.value - 200
                                    ),
                                    end = Offset(
                                        widthPx * positionState.value,
                                        heightPx * positionState.value
                                    )
                                )
                            )
                    )
                }

            }

            BoxWithConstraints(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .fillMaxWidth()
                    .height(30.dp)
            ) {
                val widthPx = with(LocalDensity.current) {
                    ((maxWidth - 32.dp).toPx())
                }
                val heightPx = with(LocalDensity.current) {
                    ((maxHeight - 16.dp).toPx())
                }
                Card(
                    shape = MaterialTheme.shapes.small
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(maxHeight)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.LightGray,
                                        Color.White,
                                        Color.LightGray
                                    ),
                                    start = Offset(
                                        widthPx * positionState.value - 200,
                                        heightPx * positionState.value - 200
                                    ),
                                    end = Offset(
                                        widthPx * positionState.value,
                                        heightPx * positionState.value
                                    )
                                )
                            )
                    )
                }

            }
        }
    }
}