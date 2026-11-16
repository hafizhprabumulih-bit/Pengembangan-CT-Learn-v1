package com.example

import com.example.data.LegacyNodeCodes
import com.example.data.CtRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tes audit kurikulum (JVM murni) untuk memverifikasi fakta data sebelum membangun Peta Belajar.
 */
class CurriculumPathGuardTest {

    @Test
    fun totalModulesAndNodesMatchExpected() {
        val modules = CtRepository.modules
        val totalNodes = modules.sumOf { it.nodes.size }
        assertEquals("Total modul harus 9", 9, modules.size)
        assertEquals("Total simpul harus 50", 50, totalNodes)
    }

    @Test
    fun sortOrderPerModuleIsUniqueAndAscending() {
        val rawViolatingModules = mutableListOf<String>()

        CtRepository.modules.forEach { module ->
            val rawOrders = module.nodes.map { it.sortOrder }
            val isRawUniqueAndAscending = rawOrders.distinct().size == rawOrders.size &&
                rawOrders.zipWithNext().all { (a, b) -> a < b }
            if (!isRawUniqueAndAscending) {
                rawViolatingModules += module.id
            }

            val effectiveOrders = module.nodes.mapIndexed { idx, node ->
                if (node.sortOrder != 0) node.sortOrder else idx + 1
            }
            assertEquals(
                "Effective sortOrder pada modul ${module.id} harus unik",
                effectiveOrders.size,
                effectiveOrders.distinct().size
            )
            assertTrue(
                "Effective sortOrder pada modul ${module.id} harus berurutan menaik",
                effectiveOrders.zipWithNext().all { (a, b) -> a < b }
            )
        }

        println("Modul dengan raw CtNode.sortOrder belum diisi/tidak menaik (${rawViolatingModules.size}): $rawViolatingModules")
        assertEquals(EXPECTED_MODULES_WITH_UNSET_RAW_SORT_ORDER, rawViolatingModules)
    }

    @Test
    fun nodeLevelsAreWithinZeroToThree() {
        val invalidNodes = CtRepository.modules
            .flatMap { it.nodes }
            .filter { it.level !in 0..3 }
            .map { "${it.code} (level=${it.level})" }

        assertTrue("Semua simpul harus memiliki level 0..3, ditemukan: $invalidNodes", invalidNodes.isEmpty())
    }

    @Test
    fun prerequisitesResolveOrMatchKnownLegacyAnomalies() {
        val moduleIds = CtRepository.modules.map { it.id }.toSet()
        val nodeCodes = CtRepository.modules.flatMap { it.nodes }.map { it.code }.toSet()

        fun canResolveSingle(moduleId: String, rawValue: String): Boolean {
            val trimmed = rawValue.trim()
            if (trimmed.isEmpty()) return true
            val normalized = LegacyNodeCodes.normalize(trimmed)
            if (normalized in moduleIds) return true
            if (normalized in nodeCodes) return true
            val relativeNormalized = LegacyNodeCodes.normalize("$moduleId.$normalized")
            return relativeNormalized in nodeCodes
        }

        val unresolvedSingle = mutableListOf<String>()
        val unresolvedTokens = mutableListOf<String>()

        CtRepository.modules.forEach { module ->
            module.nodes.forEach { node ->
                val rawPrereq = node.prerequisite?.trim().orEmpty()
                if (rawPrereq.isNotEmpty()) {
                    if (!canResolveSingle(module.id, rawPrereq)) {
                        unresolvedSingle += "${node.code} -> $rawPrereq"
                    }
                    rawPrereq.split(",")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                        .forEach { token ->
                            if (!canResolveSingle(module.id, token)) {
                                unresolvedTokens += "${node.code} -> $token"
                            }
                        }
                }
            }
        }

        println("Prasyarat tidak terselesaikan sebagai token tunggal (${unresolvedSingle.size}):")
        unresolvedSingle.forEach { println("  - $it") }
        println("Token prasyarat tidak terselesaikan setelah pemisahan koma (${unresolvedTokens.size}):")
        unresolvedTokens.forEach { println("  - $it") }

        assertEquals(EXPECTED_NON_SINGLE_PREREQUISITES, unresolvedSingle)
        assertEquals(EXPECTED_UNRESOLVED_PREREQUISITE_TOKENS, unresolvedTokens)
    }

    @Test
    fun reportNodesWithoutLabType() {
        val allNodes = CtRepository.modules.flatMap { it.nodes }
        val withoutLab = allNodes.filter { it.labType.isNullOrBlank() }.map { it.code }
        println("Simpul tanpa labType (${withoutLab.size} dari ${allNodes.size}): $withoutLab")
        assertTrue(allNodes.isNotEmpty())
    }

    @Test
    fun reportQuizCountsAndNodesWithoutQuiz() {
        val quizzes = CtRepository.quizzes
        val countsByModule = quizzes.groupingBy { it.moduleId }.eachCount()
        println("Total kuis: ${quizzes.size}. Jumlah kuis per modul:")
        CtRepository.modules.forEach { module ->
            println("  - ${module.id}: ${countsByModule[module.id] ?: 0}")
        }

        val quizNodeCodes = quizzes.map { LegacyNodeCodes.normalize(it.nodeCode) }.toSet()
        val nodesWithoutQuiz = CtRepository.modules
            .flatMap { it.nodes }
            .map { it.code }
            .filter { LegacyNodeCodes.normalize(it) !in quizNodeCodes }

        println("Simpul tanpa kuis sama sekali (${nodesWithoutQuiz.size}): $nodesWithoutQuiz")
        assertTrue(quizzes.isNotEmpty())
    }

    @Test
    fun nodeCodesAreGloballyUnique() {
        val allCodes = CtRepository.modules.flatMap { it.nodes }.map { it.code }
        val duplicates = allCodes.groupingBy { it }.eachCount().filter { it.value > 1 }
        assertTrue("Tidak boleh ada kode simpul duplikat, ditemukan: $duplicates", duplicates.isEmpty())
    }

    companion object {
        // Seluruh modul CT-Learn v1 memiliki raw CtNode.sortOrder menaik (1..N).
        private val EXPECTED_MODULES_WITH_UNSET_RAW_SORT_ORDER = emptyList<String>()

        // Seluruh prasyarat CT-Learn v1 menggunakan kode simpul tunggal yang valid.
        private val EXPECTED_NON_SINGLE_PREREQUISITES = emptyList<String>()

        private val EXPECTED_UNRESOLVED_PREREQUISITE_TOKENS = emptyList<String>()
    }
}
