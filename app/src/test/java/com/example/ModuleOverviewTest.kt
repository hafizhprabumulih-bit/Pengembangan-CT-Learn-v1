package com.example

import com.example.data.CtRepository
import com.example.util.ModuleOverview
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ModuleOverviewTest {

    @Test
    fun m01OverviewHas23NodesAndLevelQuizzes10_8_5() {
        val m01 = CtRepository.modules.first { it.id == "M01" }
        val summary = ModuleOverview.compute(
            module = m01,
            allModules = CtRepository.modules,
            completedCodes = emptySet(),
            quizzes = CtRepository.quizzes,
            enforceCurriculumLocks = false
        )

        assertEquals(6, summary.totalNodes)
        assertEquals(0, summary.completedNodes)
        assertEquals(6, summary.totalQuizzes)
        assertTrue(summary.hasSimulation)
        assertTrue(summary.isNotStarted)
        assertFalse(summary.isComplete)
        assertTrue(summary.isRecommended)
        assertNull(summary.rewardItemName)
        assertEquals(listOf(3, 2, 1), summary.levels.map { it.quizCount })
    }

    @Test
    fun m04bOverviewHandlesModuleWithoutLevel1() {
        val m04 = CtRepository.modules.first { it.id == "M04" }
        val summary = ModuleOverview.compute(
            module = m04,
            allModules = CtRepository.modules,
            completedCodes = emptySet(),
            quizzes = CtRepository.quizzes,
            enforceCurriculumLocks = false
        )

        assertEquals(listOf(1, 2, 3), summary.levels.map { it.level })
        assertEquals(m04.nodes.size, summary.totalNodes)
        assertEquals(6, summary.totalQuizzes)
    }

    @Test
    fun fullCompletionMarksCompleteAndNormalizesCrossModulePrereqs() {
        val m01 = CtRepository.modules.first { it.id == "M01" }
        val allM01Codes = m01.nodes.map { it.code }.toSet()
        val summary = ModuleOverview.compute(
            module = m01,
            allModules = CtRepository.modules,
            completedCodes = allM01Codes,
            quizzes = CtRepository.quizzes,
            enforceCurriculumLocks = false
        )

        assertTrue(summary.isComplete)
        assertEquals(6, summary.completedNodes)

        val known = CtRepository.modules.map { it.code.uppercase() }.toSet()
        assertEquals("M01", ModuleOverview.extractCrossModuleCode("M01", "M02", known))
        assertEquals("M05", ModuleOverview.extractCrossModuleCode("M05.T1.1", "M06", known))
        assertNull(ModuleOverview.extractCrossModuleCode("T2.2", "M06", known))
        assertNull(ModuleOverview.extractCrossModuleCode("M06.T1.1", "M06", known))
    }
}
