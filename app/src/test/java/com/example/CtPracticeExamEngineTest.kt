package com.example

import com.example.data.CtRepository
import com.example.util.CtExamDomain
import com.example.util.CtPracticeExamEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CtPracticeExamEngineTest {

    @Test
    fun `extendedExamBank contains at least 25 valid unique clinical CT questions`() {
        val bank = CtPracticeExamEngine.extendedExamBank
        assertTrue("Bank tambahan harus >= 25 soal", bank.size >= 25)
        assertEquals("ID soal harus unik", bank.size, bank.map { it.id }.distinct().size)

        bank.forEach { q ->
            assertEquals("Soal ${q.id} wajib punya 4 opsi", 4, q.options.size)
            assertEquals("Opsi soal ${q.id} wajib unik", 4, q.options.map { it.trim() }.distinct().size)
            assertTrue("correctIndex ${q.id} di luar rentang", q.correctIndex in 0..3)
            assertTrue("Pembahasan ${q.id} tidak boleh kosong", q.explanation.isNotBlank())
        }
    }

    @Test
    fun `buildExamSession balances questions across all 5 CT domains`() {
        val session = CtPracticeExamEngine.buildExamSession(
            questionCount = 20,
            curriculumQuizzes = CtRepository.quizzes,
            seed = 7
        )
        assertEquals(20, session.size)
        assertEquals(20, session.map { it.id }.distinct().size)

        val coveredDomains = session.map { it.domain }.toSet()
        assertEquals(CtExamDomain.entries.toSet(), coveredDomains)
    }

    @Test
    fun `evaluateSession computes exact overall and domain accuracy`() {
        val session = CtPracticeExamEngine.buildExamSession(
            questionCount = 10,
            curriculumQuizzes = CtRepository.quizzes,
            seed = 12
        )
        val allRight = session.associate { it.id to it.correctIndex }
        val evalPass = CtPracticeExamEngine.evaluateSession(session, allRight)
        assertEquals(100, evalPass.scorePercent)
        assertTrue(evalPass.passed)
        assertEquals(10, evalPass.correctCount)

        val allWrong = session.associate { it.id to ((it.correctIndex + 1) % 4) }
        val evalFail = CtPracticeExamEngine.evaluateSession(session, allWrong)
        assertEquals(0, evalFail.scorePercent)
        assertFalse(evalFail.passed)
        assertEquals(CtExamDomain.entries.size, evalFail.domainBreakdown.size)
    }

    @Test
    fun `adaptive weakest session prioritizes low accuracy domains`() {
        val outcomes = listOf(
            "M04" to false, "M04" to false, "M05" to false,
            "M01" to true, "M01" to true, "M02" to true, "M06" to true, "M08" to true
        )
        val ranked = CtPracticeExamEngine.rankedDomainsByWeakness(outcomes)
        assertEquals(CtExamDomain.MSCT_REKONSTRUKSI, ranked.first())

        val adaptive = CtPracticeExamEngine.buildAdaptiveWeakestSession(
            questionCount = 20,
            curriculumQuizzes = CtRepository.quizzes,
            attemptModuleOutcomes = outcomes,
            seed = 5
        )
        assertEquals(20, adaptive.size)
        val weakCount = adaptive.count { it.domain in ranked.take(2) }
        assertTrue("Expected majority of adaptive questions from weakest 2 domains, got $weakCount", weakCount >= 11)
    }
}
