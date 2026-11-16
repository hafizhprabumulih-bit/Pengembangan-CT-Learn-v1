package com.example

import com.example.util.CtConsoleCaseEvaluator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CtConsoleCaseEvaluatorTest {

    @Test
    fun `optimal protocols pass all 5 clinical console challenges with 100`() {
        val stroke = CtConsoleCaseEvaluator.evaluateCase(
            caseId = "case_stroke",
            kvp = 120,
            effectiveMas = 260f,
            pitch = 0.85f,
            ctdiVol = 45f,
            isHeadPhantom = true
        )
        assertTrue(stroke.passed)
        assertEquals(100, stroke.scoreOutOf100)

        val ctpa = CtConsoleCaseEvaluator.evaluateCase(
            caseId = "case_ctpa",
            kvp = 100,
            effectiveMas = 120f,
            pitch = 1.25f,
            ctdiVol = 9.5f,
            isHeadPhantom = false
        )
        assertTrue(ctpa.passed)
        assertEquals(100, ctpa.scoreOutOf100)

        val ped = CtConsoleCaseEvaluator.evaluateCase(
            caseId = "case_pediatric",
            kvp = 80,
            effectiveMas = 70f,
            pitch = 1.1f,
            ctdiVol = 4.2f,
            isHeadPhantom = false
        )
        assertTrue(ped.passed)
        assertEquals(100, ped.scoreOutOf100)

        val trauma = CtConsoleCaseEvaluator.evaluateCase(
            caseId = "case_trauma_head",
            kvp = 120,
            effectiveMas = 240f,
            pitch = 0.80f,
            ctdiVol = 48f,
            isHeadPhantom = true,
            selectedWindowLabel = "Bone (2000/500)"
        )
        assertTrue(trauma.passed)
        assertEquals(100, trauma.scoreOutOf100)

        val hrct = CtConsoleCaseEvaluator.evaluateCase(
            caseId = "case_hrct_lung",
            kvp = 120,
            effectiveMas = 95f,
            pitch = 1.15f,
            ctdiVol = 8.1f,
            isHeadPhantom = false,
            selectedWindowLabel = "Lung (1500/-600)"
        )
        assertTrue(hrct.passed)
        assertEquals(100, hrct.scoreOutOf100)
    }

    @Test
    fun `excessive dose and wrong phantom fail pediatric case with actionable warnings`() {
        val badPed = CtConsoleCaseEvaluator.evaluateCase(
            caseId = "case_pediatric",
            kvp = 140,
            effectiveMas = 300f,
            pitch = 0.6f,
            ctdiVol = 28f,
            isHeadPhantom = true
        )
        assertFalse(badPed.passed)
        assertTrue(badPed.scoreOutOf100 < 50)
        assertTrue(badPed.warnings.size >= 4)
    }

    @Test
    fun `excessive noise and wrong window fail stroke case even when dose is low`() {
        val noisyStroke = CtConsoleCaseEvaluator.evaluateCase(
            caseId = "case_stroke",
            kvp = 120,
            effectiveMas = 220f,
            pitch = 0.8f,
            ctdiVol = 40f,
            isHeadPhantom = true,
            noiseSdHu = 16.5f,
            selectedWindowLabel = "Lung (1500/-600)"
        )
        assertFalse(noisyStroke.passed)
        assertTrue(noisyStroke.warnings.any { it.contains("noise", ignoreCase = true) })
        assertTrue(noisyStroke.warnings.any { it.contains("Windowing", ignoreCase = true) })
    }
}
