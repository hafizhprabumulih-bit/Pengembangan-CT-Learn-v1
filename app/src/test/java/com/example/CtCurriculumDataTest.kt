package com.example

import com.example.data.CtCurriculumData
import com.example.data.toDomainModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CtCurriculumDataTest {

    @Test
    fun ctCurriculumDataMatchesMriContractAndEntityMappers() {
        val moduleEntities = CtCurriculumData.toMriModuleEntities()
        val nodeEntities = CtCurriculumData.toCurriculumNodeEntities()
        val quizEntities = CtCurriculumData.toCurriculumQuizEntities()

        assertEquals(CtCurriculumData.modules.size, moduleEntities.size)
        assertEquals(CtCurriculumData.modules.sumOf { it.nodes.size }, nodeEntities.size)
        assertEquals(CtCurriculumData.quizzes.size, quizEntities.size)

        // Ensure Room entities round-trip cleanly into MriCurriculumData domain models
        val roundTrippedModules = moduleEntities.map { it.toDomainModel() }
        val roundTrippedNodes = nodeEntities.map { it.toDomainModel() }
        val roundTrippedQuizzes = quizEntities.map { it.toDomainModel() }

        assertEquals(moduleEntities.size, roundTrippedModules.size)
        assertEquals(nodeEntities.size, roundTrippedNodes.size)
        assertEquals(quizEntities.size, roundTrippedQuizzes.size)

        // Ensure adapter helpers produce identical counts
        assertEquals(CtCurriculumData.modules.size, CtCurriculumData.toMriModules().size)
        assertEquals(CtCurriculumData.quizzes.size, CtCurriculumData.toQuizQuestions().size)
        assertEquals(CtCurriculumData.flashcards.size, CtCurriculumData.toMriFlashcards().size)
        assertEquals(CtCurriculumData.tissues.size, CtCurriculumData.toTissuePropertiesList().size)
        assertEquals(CtCurriculumData.glossary.size, CtCurriculumData.toGlossaryItems().size)
    }

    @Test
    fun ctInstrumentationSpecsAndNodesAreValid() {
        assertEquals(5, CtCurriculumData.instrumentSpecs.size)
        assertTrue(CtCurriculumData.instrumentSpecs.any { it.subsystem.contains("Tube", ignoreCase = true) })
        assertTrue(CtCurriculumData.instrumentSpecs.any { it.subsystem.contains("Slip-Ring", ignoreCase = true) })
        assertTrue(CtCurriculumData.instrumentSpecs.any { it.subsystem.contains("Detector", ignoreCase = true) })

        val nodeCodes = CtCurriculumData.modules.flatMap { it.nodes }.map { it.code }.toSet()
        assertEquals(7, nodeCodes.size)

        CtCurriculumData.quizzes.forEach { quiz ->
            assertTrue("Quiz ${quiz.id} nodeCode ${quiz.nodeCode} missing", quiz.nodeCode in nodeCodes)
            assertEquals(4, quiz.options.size)
            assertTrue(quiz.correctIndex in quiz.options.indices)
            assertTrue(quiz.explanation.isNotBlank())
        }
    }

    @Test
    fun ctScannerInstrumentSpecsAndCalculationsAreValid() {
        val instruments = CtCurriculumData.scannerInstruments
        assertEquals(4, instruments.size)

        instruments.forEach { inst ->
            assertTrue(inst.tubeHeatCapacity.isNotBlank())
            assertTrue(inst.slipRingType.isNotBlank())
            assertTrue(inst.detectorArrayConfiguration.isNotBlank())
            assertTrue(inst.hvGeneratorType.isNotBlank())
            assertTrue(inst.detectorQdePercent > 90f)
            assertTrue(inst.calculateBeamCollimationMm() > 0f)
            assertTrue(inst.toSummaryText().contains(inst.tubeHeatCapacity))
        }

        val msct64 = instruments.first { it.id == "ct_msct_64_workhorse" }
        assertEquals(40.0f, msct64.calculateBeamCollimationMm(), 0.01f)
        assertEquals(0.8f, msct64.calculatePitch(tableFeedMmPerRot = 32.0f), 0.001f)
        assertEquals(1.4f * 120 * 300 * 10f, msct64.calculateHeatUnits(120, 300, 10f), 0.1f)
    }
}
