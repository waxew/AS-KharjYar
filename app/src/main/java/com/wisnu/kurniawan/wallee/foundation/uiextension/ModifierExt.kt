package com.wisnu.kurniawan.wallee.foundation.uiextension

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp

fun Modifier.guide() = background(Color.Red)

fun Modifier.guide2() = background(Color.Green)

fun Modifier.guide3() = background(Color.Blue)

fun Modifier.drawGrowingCircle(
    color: Color,
    center: Offset,
    radius: Float
) = drawWithContent {
    drawContent()
    clipRect {
        drawCircle(
            color = color,
            radius = radius,
            center = center
        )
    }
}

fun Modifier.onPositionInParentChanged(
    onChange: (LayoutCoordinates) -> Unit
) = composed {
    var lastPosition by remember { mutableStateOf(Offset.Zero) }
    onGloballyPositioned { coordinates ->
        if (coordinates.positionInParent() != lastPosition) {
            lastPosition = coordinates.positionInParent()
            onChange(coordinates)
        }
    }
}

@Stable
fun Modifier.paddingCell() = padding(horizontal = 16.dp, vertical = 8.dp)
