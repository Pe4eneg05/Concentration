package com.pechenegmobilecompanyltd.concentration.presentation.utils

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.swipeable(
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {},
    onSwipeUp: () -> Unit = {},
    onSwipeDown: () -> Unit = {},
    swipeThreshold: Float = 100f
): Modifier = this.pointerInput(Unit) {
    detectDragGestures { change, dragAmount ->
        change.consume()

        when {
            dragAmount.x > swipeThreshold -> onSwipeRight()
            dragAmount.x < -swipeThreshold -> onSwipeLeft()
            dragAmount.y > swipeThreshold -> onSwipeDown()
            dragAmount.y < -swipeThreshold -> onSwipeUp()
        }
    }
}