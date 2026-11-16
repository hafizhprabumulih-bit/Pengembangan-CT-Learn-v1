package com.example

import com.example.data.CtRepository
import com.example.data.CtTissueAttenuation
import com.example.viewmodel.CtUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testEffectiveMasAndPixelSizeCalculation() {
        val state = CtUiState(
            tubeMa = 250f,
            rotationTimeSec = 0.5f,
            helicalPitch = 1.0f,
            displayFovMm = 256f,
            reconstructionMatrix = 512
        )
        assertEquals(125f, state.effectiveMas, 0.01f)
        assertEquals(0.5f, state.pixelSizeMm, 0.001f)

        val highPitchState = state.copy(helicalPitch = 1.25f)
        assertEquals(100f, highPitchState.effectiveMas, 0.01f)
    }

    @Test
    fun testCtDosimetryAndDrlLimitCalculation() {
        val standardBody = CtUiState(
            tubeKvp = 120,
            tubeMa = 200f,
            rotationTimeSec = 0.5f,
            helicalPitch = 1.0f,
            scanLengthCm = 30f,
            isHeadPhantom = false
        )
        // effectiveMas = 100 mAs, CTDIvol (Body 32cm @ 120kVp) = 8.5 mGy, DLP = 255 mGy.cm, E = 255 * 0.015 = 3.825 mSv
        assertEquals(8.5f, standardBody.ctdiVolMgy, 0.05f)
        assertEquals(255f, standardBody.dlpMgyCm, 0.5f)
        assertEquals(3.825f, standardBody.effectiveDoseMsv, 0.02f)
        assertFalse(standardBody.isDrlExceeded)

        val highDoseBody = standardBody.copy(tubeKvp = 140, tubeMa = 450f)
        assertTrue("High 140 kVp + 450 mA body exposure must flag DRL warning", highDoseBody.isDrlExceeded)
    }

    @Test
    fun testIterativeReconNoiseReductionAndHuCalculation() {
        val fbpState = CtUiState(isIterativeReconActive = false)
        val irState = CtUiState(isIterativeReconActive = true)
        assertTrue(irState.estimatedNoiseSdHu < fbpState.estimatedNoiseSdHu)

        val water = CtTissueAttenuation(
            id = "water_test",
            name = "Air Murni",
            huMin = 0,
            huMax = 0,
            huTypical = 0f,
            muPerCmAt120kVp = CtRepository.MU_WATER_120_KVP,
            recommendedWindow = "Brain",
            description = "Test",
            clinicalNote = "Test"
        )
        val huWater = CtUiState(selectedTissue = water).calculatedHu
        assertEquals(0, huWater)
    }

    @Test
    fun testCompletedCountExcludesOrphanNodeCodes() {
        val state = CtUiState(
            modulesList = CtRepository.modules,
            completedNodeCodes = setOf("M01.T1.1", "M02.T1.1", "ORPHAN.LEGACY.999", "M03A.KUIS")
        )
        assertEquals(2, state.completedCount)
    }

    @Test
    fun testCurriculumRepositoryIntegrity() {
        assertTrue("Modules should not be empty", CtRepository.modules.isNotEmpty())
        assertTrue("Glossary should contain key CT terms", CtRepository.glossary.size >= 8)
        assertTrue("Quizzes should contain questions", CtRepository.quizzes.size >= 5)

        CtRepository.modules.forEach { module ->
            assertNotNull(module.id)
            assertNotNull(module.title)
            assertTrue("Module ${module.id} should have nodes", module.nodes.isNotEmpty())
            module.nodes.forEach { node ->
                assertTrue(node.code.isNotEmpty())
                assertTrue(node.title.isNotEmpty())
                assertTrue(node.summaryShort.isNotEmpty())
                assertTrue(node.analogi.isNotEmpty())
            }
        }
    }

    @Test
    fun testStreakCalendarSevenDaysModuloLogic() {
        fun activeDaysInWeek(streakDays: Int): Int =
            if (streakDays <= 0) 0 else ((streakDays - 1) % 7) + 1

        assertEquals(0, activeDaysInWeek(0))
        assertEquals(1, activeDaysInWeek(1))
        assertEquals(6, activeDaysInWeek(6))
        assertEquals("Kelipatan 7 hari harus menyalakan seluruh 7 hari", 7, activeDaysInWeek(7))
        assertEquals(1, activeDaysInWeek(8))
        assertEquals("Kelipatan 14 hari harus menyalakan seluruh 7 hari", 7, activeDaysInWeek(14))
        assertEquals("Kelipatan 21 hari harus menyalakan seluruh 7 hari", 7, activeDaysInWeek(21))
    }

    @Test
    fun testFlashcardsAndBookmarkStateMatching() {
        val allCards = CtRepository.flashcards
        assertTrue("Flashcards deck should not be empty", allCards.isNotEmpty())

        val sampleNodeCard = allCards.first { !it.isGlossaryCard }
        val sampleGlossaryCard = allCards.first { it.isGlossaryCard }

        val stateWithBookmarks = CtUiState(
            bookmarkedNodeCodes = setOf(sampleNodeCard.nodeCode),
            bookmarkedGlossaryTerms = setOf(sampleGlossaryCard.glossaryTermKey)
        )

        assertTrue(
            "Node flashcard should be recognized as bookmarked when its nodeCode is in bookmarkedNodeCodes",
            stateWithBookmarks.isFlashcardBookmarked(sampleNodeCard)
        )
        assertTrue(
            "Glossary flashcard should be recognized as bookmarked when its term is in bookmarkedGlossaryTerms",
            stateWithBookmarks.isFlashcardBookmarked(sampleGlossaryCard)
        )
    }
}
