package com.peter.painter

import com.peter.painter.Solution.Companion.isRectangleOverlap

// Java program to check if rectangles overlap
class Solution {
    // Returns true if two rectangles (l1, r1) and (l2, r2) overlap
    companion object{
        fun isRectangleOverlap(rect1: IntArray, rect2: IntArray): Boolean {
            val (x1, y1, x2, y2) = rect1
            val (X1, Y1, X2, Y2) = rect2
            val sharedAreaBottomX = maxOf(x1, X1)
            val sharedAreaBottomY = maxOf(y1, Y1)
            val sharedAreaTopX = minOf(x2, X2)
            val sharedAreaTopY = minOf(y2, Y2)
            if (sharedAreaBottomX < sharedAreaTopX && sharedAreaBottomY < sharedAreaTopY)
                return true
            return false
        }
    }
}


fun main() {

    val firstRectangle = intArrayOf(0,0,1,1)
    val secondRectangle = intArrayOf(2,2,3,3)

    if (isRectangleOverlap(firstRectangle,secondRectangle)) {
        println("Rectangles Overlap")
    } else {
        println("Rectangles Don't Overlap")
    }
}

