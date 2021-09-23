package com.peter.painter

import android.graphics.Path

data class Stroke(
    var color: Int, // color of the stroke
    var strokeWidth: Int, // width of the stroke
    var path: Path
)