package com.example

import com.example.util.CircularProgressMotion
import com.example.util.CtHapticPattern
import com.example.util.CtHaptics
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CtHapticsAndMotionTest {

    @Test
    fun `completion buzz is noticeably stronger and longer than light toggle`() {
        val light = CtHaptics.specFor(CtHapticPattern.LIGHT_TOGGLE)
        val medium = CtHaptics.specFor(CtHapticPattern.MEDIUM_ACTION)
        val warning = CtHaptics.specFor(CtHapticPattern.WARNING_BUZZ)
        val completion = CtHaptics.specFor(CtHapticPattern.COMPLETION_BUZZ)

        assertTrue(light.totalDurationMs in 10L..25L)
        assertTrue(medium.totalDurationMs > light.totalDurationMs)
        assertTrue(warning.timingsMs.size >= 4)
        assertTrue(completion.totalDurationMs > warning.totalDurationMs)
        assertTrue((completion.amplitudes.maxOrNull() ?: 0) > (light.amplitudes.maxOrNull() ?: 0))
    }

    @Test
    fun `circular progress motion normalizes four fifths and rotates angle proportionally`() {
        val progress = CircularProgressMotion.normalizeProgress(done = 4, total = 5)
        assertEquals(0.8f, progress, 0.001f)

        val startAtZero = CircularProgressMotion.calculateStartAngleDeg(progress = 0f)
        val startAtFourFifths = CircularProgressMotion.calculateStartAngleDeg(progress = progress)
        assertTrue(startAtFourFifths > startAtZero)

        val sweepHalf = CircularProgressMotion.calculateSweepAngleDeg(progress = 0.5f, total = 5)
        val sweepFull = CircularProgressMotion.calculateSweepAngleDeg(progress = 1.0f, total = 5)
        assertEquals(180f, sweepHalf, 0.1f)
        assertEquals(360f, sweepFull, 0.1f)
    }
}
