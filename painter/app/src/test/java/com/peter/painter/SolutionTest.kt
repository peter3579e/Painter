package com.peter.painter

import android.text.BoringLayout
import junit.framework.TestCase
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class SolutionTest {

    private val input: List<IntArray> = listOf(intArrayOf(0,0,2,2),intArrayOf(0,0,1,1),intArrayOf(0,0,1,1))
    private val secondInput: List<IntArray> = listOf(intArrayOf(1,1,3,3),intArrayOf(1,0,2,1),intArrayOf(2,2,3,3))
    private val output:List<Boolean> = listOf(true,false,false)
    @Test
    fun isRectangleOverlapTest(){
        for (i in input.indices){
            val result = Solution.isRectangleOverlap(input[i],secondInput[i])
            assertThat(result).isEqualTo(output[i])
        }
    }
}