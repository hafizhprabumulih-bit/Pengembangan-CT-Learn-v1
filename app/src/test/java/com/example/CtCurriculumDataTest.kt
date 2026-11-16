package com.example

import com.example.data.CtCurriculumData
import com.example.data.CtRepository
import com.example.data.toDomainModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CtCurriculumDataTest {

    @Test
    fun ctCurriculumDataMatchesCtContractAndEntityMappers() {
        val moduleEntities = CtCurriculumData.toCtModuleEntities()
        val nodeEntities = CtCurriculumData.toCurriculumNodeEntities()
        val quizEntities = CtCurriculumData.toCurriculumQuizEntities()

        assertEquals(CtCurriculumData.modules.size, moduleEntities.size)
        assertEquals(CtCurriculumData.modules.sumOf { it.nodes.size }, nodeEntities.size)
        assertEquals(CtCurriculumData.quizzes.size, quizEntities.size)

        val roundTrippedModules = moduleEntities.map { it.toDomainModel() }
        val roundTrippedNodes = nodeEntities.map { it.toDomainModel() }
        val roundTrippedQuizzes = quizEntities.map { it.toDomainModel() }

        assertEquals(moduleEntities.size, roundTrippedModules.size)
        assertEquals(nodeEntities.size, roundTrippedNodes.size)
        assertEquals(quizEntities.size, roundTrippedQuizzes.size)

        assertEquals(CtCurriculumData.modules.size, CtCurriculumData.toCtModules().size)
        assertEquals(CtCurriculumData.quizzes.size, CtCurriculumData.toQuizQuestions().size)
        assertEquals(CtCurriculumData.flashcards.size, CtCurriculumData.toCtFlashcards().size)
        assertEquals(CtCurriculumData.tissues.size, CtCurriculumData.toTissueAttenuationList().size)
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

        val modules = CtRepository.modules
        val quizzes = CtRepository.quizzes
        val allNodes = modules.flatMap { it.nodes }
        val allNodeCodes = allNodes.map { it.code }.toSet()

        assertEquals(9, modules.size)
        assertEquals(50, allNodes.size)
        assertEquals(50, quizzes.size)

        modules.forEach { mod ->
            assertEquals("totalQuizzes pada ${mod.id} harus sesuai jumlah soal", quizzes.count { it.moduleId == mod.id }, mod.totalQuizzes)
            assertTrue("Flashcards modul ${mod.id} tidak boleh kosong", mod.flashcards.isNotEmpty())
        }

        allNodes.forEach { node ->
            assertTrue("Simpul ${node.code} wajib memiliki labType", !node.labType.isNullOrBlank())
            val codePattern = Regex("M\\d{2}\\.T\\d(?:\\.\\d)?")
            codePattern.findAll(node.crossLinks.orEmpty()).forEach { match ->
                assertTrue(
                    "Rujukan crossLinks '${match.value}' pada simpul ${node.code} tidak ditemukan di kurikulum",
                    match.value in allNodeCodes
                )
            }
        }
    }
}
