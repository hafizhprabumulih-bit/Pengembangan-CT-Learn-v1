package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.MriDatabase
import com.example.data.local.NodeProgressEntity
import com.example.data.local.QuizAttemptEntity
import com.example.data.local.StudyDayEntity
import com.example.data.local.StudyNoteEntity
import com.example.data.local.normalizeLegacyNodeCodes
import com.example.data.toDomainModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class RoomDaoTest {
    private lateinit var db: MriDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MriDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testStudyNoteDaoCrud() = runBlocking {
        val dao = db.studyNoteDao()
        val note = StudyNoteEntity(
            id = "note_test_1",
            title = "Test Note",
            content = "Testing Room note content",
            category = "Fisika",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        dao.insert(note)
        val notes = dao.observeNotes().first()
        assertEquals(1, notes.size)
        assertEquals("Test Note", notes[0].title)

        dao.delete("note_test_1")
        val notesAfterDelete = dao.observeNotes().first()
        assertTrue(notesAfterDelete.isEmpty())
    }

    @Test
    fun testNodeProgressDao() = runBlocking {
        val dao = db.nodeProgressDao()
        val progress = NodeProgressEntity(nodeCode = "M02.T1.1", completedAt = System.currentTimeMillis())
        dao.insert(progress)

        val completed = dao.observeCompletedNodes().first()
        assertEquals(1, completed.size)
        assertEquals("M02.T1.1", completed[0].nodeCode)

        dao.delete("M02.T1.1")
        val completedAfter = dao.observeCompletedNodes().first()
        assertTrue(completedAfter.isEmpty())
    }

    @Test
    fun testQuizAttemptDao() = runBlocking {
        val dao = db.quizAttemptDao()
        val attempt = QuizAttemptEntity(
            quizId = "q_m02_t1_1",
            nodeCode = "M02.T1.1",
            moduleId = "M02",
            selectedIndex = 1,
            isCorrect = true,
            answeredAt = System.currentTimeMillis()
        )
        dao.insert(attempt)
        val attempts = dao.observeQuizAttempts().first()
        assertEquals(1, attempts.size)
        assertEquals("q_m02_t1_1", attempts[0].quizId)
        assertTrue(attempts[0].isCorrect)
    }

    @Test
    fun testMriCurriculumDaoSeedingAndModule2Integration() = runBlocking {
        db.seedCurriculumIfNeeded()
        val curriculumDao = db.curriculumDao()

        val m02Nodes = curriculumDao.observeNodesByModule("M02").first()
        val nodeCodes = m02Nodes.map { it.code }.toSet()
        assertTrue(nodeCodes.contains("M02.T1.1"))
        assertTrue(nodeCodes.contains("M02.T1.2"))
        assertTrue(nodeCodes.contains("M02.T2.1"))
        assertTrue(nodeCodes.contains("M02.T2.2"))

        val t1Node = m02Nodes.first { it.code == "M02.T1.2" }
        assertTrue(t1Node.detailFull.contains("Aperture") || t1Node.detailFull.contains("Gantry Tilt"))

        val t2Node = m02Nodes.first { it.code == "M02.T2.1" }
        assertTrue(t2Node.detailFull.contains("Bow-Tie Filter") || t2Node.detailFull.contains("Rotating Frame"))

        val level1Nodes = curriculumDao.observeNodesByModuleAndLevel("M02", 1).first()
        assertTrue(level1Nodes.all { it.level == 1 && it.moduleId == "M02" })

        val m02Quizzes = curriculumDao.observeQuizzesByModule("M02").first()
        assertEquals(6, m02Quizzes.size)
        val firstDomainQuiz = m02Quizzes.first { it.id == "q_m02_t1_1" }.toDomainModel()
        assertEquals(4, firstDomainQuiz.options.size)
        assertEquals(1, firstDomainQuiz.correctIndex)
    }

    @Test
    fun testCurriculumDaoModule1NodesAndThreeLevelQuizzes() = runBlocking {
        db.seedCurriculumIfNeeded()
        val dao = db.curriculumDao()

        val m01Nodes = dao.observeNodesByModule("M01").first()
        assertEquals(6, m01Nodes.size)

        val expectedCodes = listOf(
            "M01.T0",
            "M01.T1.1", "M01.T1.2",
            "M01.T2.1", "M01.T2.2",
            "M01.T3.1"
        )
        val actualCodes = m01Nodes.map { it.code }.toSet()
        expectedCodes.forEach { code ->
            assertTrue("Expected node code $code in curriculum_nodes", actualCodes.contains(code))
        }

        val level0Nodes = dao.observeNodesByModuleAndLevel("M01", 0).first()
        assertEquals(1, level0Nodes.size)
        val level1Nodes = dao.observeNodesByModuleAndLevel("M01", 1).first()
        assertEquals(2, level1Nodes.size)
        val level2Nodes = dao.observeNodesByModuleAndLevel("M01", 2).first()
        assertEquals(2, level2Nodes.size)
        val level3Nodes = dao.observeNodesByModuleAndLevel("M01", 3).first()
        assertEquals(1, level3Nodes.size)

        val m01Quizzes = dao.observeQuizzesByModule("M01").first()
        assertEquals(6, m01Quizzes.size)
        assertEquals(3, dao.observeQuizzesByModuleAndLevel("M01", 1).first().size)
        assertEquals(2, dao.observeQuizzesByModuleAndLevel("M01", 2).first().size)
        assertEquals(1, dao.observeQuizzesByModuleAndLevel("M01", 3).first().size)
    }

    @Test
    fun testCurriculumDaoModule9NodesAndQuizzes() = runBlocking {
        db.seedCurriculumIfNeeded()
        val dao = db.curriculumDao()

        val m09Nodes = dao.observeNodesByModule("M09").first()
        assertEquals(5, m09Nodes.size)

        val expectedM09Codes = listOf(
            "M09.T0", "M09.T1.1", "M09.T1.2", "M09.T2.1", "M09.T3.1"
        )
        val actualCodes = m09Nodes.map { it.code }.toSet()
        expectedM09Codes.forEach { code ->
            assertTrue("Expected node code $code in M09", actualCodes.contains(code))
        }

        assertEquals(2, dao.observeNodesByModuleAndLevel("M09", 1).first().size)
        assertEquals(1, dao.observeNodesByModuleAndLevel("M09", 2).first().size)
        assertEquals(1, dao.observeNodesByModuleAndLevel("M09", 3).first().size)

        assertEquals("ct_m09_artefak_qa", m09Nodes.first { it.code == "M09.T1.1" }.labType)
        assertEquals("ct_m09_artefak_qa", m09Nodes.first { it.code == "M09.T2.1" }.labType)
        assertEquals("ct_m09_artefak_qa", m09Nodes.first { it.code == "M09.T3.1" }.labType)

        val m09Quizzes = dao.observeQuizzesByModule("M09").first()
        assertEquals(5, m09Quizzes.size)
        assertEquals(3, dao.observeQuizzesByModuleAndLevel("M09", 1).first().size)
        assertEquals(1, dao.observeQuizzesByModuleAndLevel("M09", 2).first().size)
        assertEquals(1, dao.observeQuizzesByModuleAndLevel("M09", 3).first().size)
    }

    @Test
    fun testCurriculumDaoModule4NodesAndQuizzes() = runBlocking {
        db.seedCurriculumIfNeeded()
        val dao = db.curriculumDao()

        val m04Nodes = dao.observeNodesByModule("M04").first()
        assertEquals(6, m04Nodes.size)

        val m04Quizzes = dao.observeQuizzesByModule("M04").first()
        assertEquals(6, m04Quizzes.size)
    }

    @Test
    fun testModule5RoomDatabaseIntegration() = runBlocking {
        db.seedCurriculumIfNeeded()
        val mriDao = db.mriCurriculumDao()
        val curriculumDao = db.curriculumDao()

        val allModules = mriDao.observeAllModules().first()
        assertTrue(allModules.any { it.id == "M05" && it.title.contains("Rekonstruksi") })

        val m05Nodes = curriculumDao.observeNodesByModule("M05").first()
        assertEquals(5, m05Nodes.size)

        val expectedM05Codes = listOf(
            "M05.T0", "M05.T1.1", "M05.T1.2", "M05.T2.1", "M05.T3.1"
        )
        val actualM05Codes = m05Nodes.map { it.code }.toSet()
        expectedM05Codes.forEach { code ->
            assertTrue("Expected M05 node code $code in curriculum_nodes", actualM05Codes.contains(code))
        }

        assertEquals(2, curriculumDao.observeNodesByModuleAndLevel("M05", 1).first().size)
        assertEquals(1, curriculumDao.observeNodesByModuleAndLevel("M05", 2).first().size)
        assertEquals(1, curriculumDao.observeNodesByModuleAndLevel("M05", 3).first().size)

        val t11 = m05Nodes.first { it.code == "M05.T1.1" }
        assertEquals("ct_m05_rekonstruksi", t11.labType)

        val m05Quizzes = curriculumDao.observeQuizzesByModule("M05").first()
        assertEquals(5, m05Quizzes.size)
        assertEquals(3, curriculumDao.observeQuizzesByModuleAndLevel("M05", 1).first().size)
        assertEquals(1, curriculumDao.observeQuizzesByModuleAndLevel("M05", 2).first().size)
        assertEquals(1, curriculumDao.observeQuizzesByModuleAndLevel("M05", 3).first().size)

        val q1 = m05Quizzes.first { it.id == "q_m05_t0" }
        assertEquals(0, q1.correctIndex)
    }

    @Test
    fun testModule6AAnd6BRoomDatabaseIntegration() = runBlocking {
        db.seedCurriculumIfNeeded()
        val mriDao = db.mriCurriculumDao()
        val curriculumDao = db.curriculumDao()

        val allModules = mriDao.observeAllModules().first()
        assertTrue(allModules.any { it.id == "M06" && it.title.contains("Hounsfield") })

        val m06Nodes = curriculumDao.observeNodesByModule("M06").first()
        assertEquals(5, m06Nodes.size)
        assertEquals(2, curriculumDao.observeNodesByModuleAndLevel("M06", 1).first().size)
        assertEquals(1, curriculumDao.observeNodesByModuleAndLevel("M06", 2).first().size)
        assertEquals(1, curriculumDao.observeNodesByModuleAndLevel("M06", 3).first().size)

        val m06T21 = m06Nodes.first { it.code == "M06.T2.1" }
        assertEquals("ct_m06_hu_windowing", m06T21.labType)

        val m06Quizzes = curriculumDao.observeQuizzesByModule("M06").first()
        assertEquals(5, m06Quizzes.size)
        assertEquals(3, curriculumDao.observeQuizzesByModuleAndLevel("M06", 1).first().size)
        assertEquals(1, curriculumDao.observeQuizzesByModuleAndLevel("M06", 2).first().size)
        assertEquals(1, curriculumDao.observeQuizzesByModuleAndLevel("M06", 3).first().size)
    }

    @Test
    fun testModule7ABCIntegration() = runBlocking {
        db.seedCurriculumIfNeeded()
        val mriDao = db.mriCurriculumDao()
        val curriculumDao = db.curriculumDao()

        val allModules = mriDao.observeAllModules().first()
        assertTrue(allModules.any { it.id == "M07" && it.title.contains("Parameter") })

        val m07Nodes = curriculumDao.observeNodesByModule("M07").first()
        assertEquals(5, m07Nodes.size)
        assertEquals(2, curriculumDao.observeNodesByModuleAndLevel("M07", 1).first().size)
        assertEquals(1, curriculumDao.observeNodesByModuleAndLevel("M07", 2).first().size)
        assertEquals(1, curriculumDao.observeNodesByModuleAndLevel("M07", 3).first().size)
        assertEquals("ct_m07_dosimetri_kualitas", m07Nodes.first { it.code == "M07.T1.1" }.labType)

        val m07Quizzes = curriculumDao.observeQuizzesByModule("M07").first()
        assertEquals(5, m07Quizzes.size)
        assertEquals(3, curriculumDao.observeQuizzesByModuleAndLevel("M07", 1).first().size)
        assertEquals(1, curriculumDao.observeQuizzesByModuleAndLevel("M07", 2).first().size)
        assertEquals(1, curriculumDao.observeQuizzesByModuleAndLevel("M07", 3).first().size)
    }

    @Test
    fun testModule8ABCIntegration() = runBlocking {
        db.seedCurriculumIfNeeded()
        val mriDao = db.mriCurriculumDao()
        val curriculumDao = db.curriculumDao()

        val allModules = mriDao.observeAllModules().first()
        assertTrue(allModules.any { it.id == "M08" && it.title.contains("Kontras") })

        val m08Nodes = curriculumDao.observeNodesByModule("M08").first()
        assertEquals(5, m08Nodes.size)
        assertEquals(2, curriculumDao.observeNodesByModuleAndLevel("M08", 1).first().size)
        assertEquals(1, curriculumDao.observeNodesByModuleAndLevel("M08", 2).first().size)
        assertEquals(1, curriculumDao.observeNodesByModuleAndLevel("M08", 3).first().size)
        assertEquals("ct_m08_kontras_3d", m08Nodes.first { it.code == "M08.T1.1" }.labType)

        val m08Quizzes = curriculumDao.observeQuizzesByModule("M08").first()
        assertEquals(5, m08Quizzes.size)
    }

    @Test
    fun testModule3ABCIntegration() = runBlocking {
        db.seedCurriculumIfNeeded()
        val mriDao = db.mriCurriculumDao()
        val curriculumDao = db.curriculumDao()

        val modules = mriDao.observeAllModules().first()
        val m03 = modules.find { it.id == "M03" }
        assertTrue(m03 != null)
        assertEquals("M03", m03?.code)

        val m03Nodes = curriculumDao.observeNodesByModule("M03").first()
        assertEquals(7, m03Nodes.size)
        assertEquals(2, curriculumDao.observeNodesByModuleAndLevel("M03", 1).first().size)
        assertEquals(2, curriculumDao.observeNodesByModuleAndLevel("M03", 2).first().size)
        assertEquals(2, curriculumDao.observeNodesByModuleAndLevel("M03", 3).first().size)
        assertEquals("ct_m03_instrumentasi_rantai", m03Nodes.first { it.code == "M03.T1.1" }.labType)

        val m03Quizzes = curriculumDao.observeQuizzesByModule("M03").first()
        assertEquals(7, m03Quizzes.size)
    }

    @Test
    fun testModule3ABCUserProgressNotesAndQuizzes() = runBlocking {
        val progressDao = db.nodeProgressDao()
        progressDao.insert(NodeProgressEntity(nodeCode = "M03.T1.1", completedAt = 1000L))
        progressDao.insert(NodeProgressEntity(nodeCode = "M03.T1.2", completedAt = 1001L))
        progressDao.insert(NodeProgressEntity(nodeCode = "M03.T2.1", completedAt = 1002L))
        progressDao.insert(NodeProgressEntity(nodeCode = "M03.T3.1", completedAt = 1003L))

        assertEquals(4, progressDao.getAll().size)
        assertEquals(4, progressDao.getCountByModule("M03"))
        assertEquals(4, progressDao.getCountByPrefix("M03"))

        val m03Nodes = progressDao.observeCompletedNodesByModule("M03").first()
        assertEquals(4, m03Nodes.size)
        assertEquals("M03", m03Nodes[0].moduleId)

        val quizDao = db.quizAttemptDao()
        quizDao.insert(
            QuizAttemptEntity(
                quizId = "q_m03_t1_1",
                nodeCode = "M03.T1.1",
                moduleId = "M03",
                selectedIndex = 0,
                isCorrect = true,
                answeredAt = 2000L
            )
        )
        quizDao.insert(
            QuizAttemptEntity(
                quizId = "q_m03_t1_2",
                nodeCode = "M03.T1.2",
                moduleId = "M03",
                selectedIndex = 1,
                isCorrect = false,
                answeredAt = 2001L
            )
        )

        assertEquals(2, quizDao.getTotalCountByModule("M03"))
        assertEquals(1, quizDao.getCorrectCountByModule("M03"))

        val noteDao = db.studyNoteDao()
        noteDao.insert(
            StudyNoteEntity(
                id = "note_m3",
                title = "Slip-Ring & Tabung Sinar-X CT",
                content = "Slip-ring mengalirkan daya listrik dan data tanpa kabel putus",
                category = "Instrumentasi",
                createdAt = 3000L,
                updatedAt = 3000L,
                nodeCode = "M03.T1.1",
                moduleId = "M03"
            )
        )

        val m03Notes = noteDao.observeNotesByModule("M03").first()
        assertEquals(1, m03Notes.size)
        assertEquals("M03.T1.1", m03Notes[0].nodeCode)

        val dayDao = db.studyDayDao()
        dayDao.insert(
            StudyDayEntity(
                dateIso = "2026-11-11",
                nodesCompletedCount = 4,
                lastNodeCode = "M03.T3.1",
                lastModuleId = "M03"
            )
        )
        val day = dayDao.getByDate("2026-11-11")
        assertTrue(day != null)
        assertEquals(4, day?.nodesCompletedCount)
        assertEquals("M03.T3.1", day?.lastNodeCode)
        assertEquals("M03", day?.lastModuleId)
    }

    @Test
    fun testToggleNodeCompletionMappingModule3ABC() = runBlocking {
        val progressDao = db.nodeProgressDao()
        val dayDao = db.studyDayDao()

        val rawCodes = listOf("M03.T1.1", "M03.T2.1", "M03.T3.1", "M03A.T1.1")
        rawCodes.forEachIndexed { idx, code ->
            val normalizedCode = com.example.data.LegacyNodeCodes.normalize(code)
            val modId = com.example.data.LegacyNodeCodes.moduleIdOf(normalizedCode)
            progressDao.insert(
                NodeProgressEntity(
                    nodeCode = normalizedCode,
                    completedAt = System.currentTimeMillis() + idx,
                    moduleId = modId
                )
            )
            dayDao.insert(
                StudyDayEntity(
                    dateIso = "2026-11-10",
                    nodesCompletedCount = idx + 1,
                    lastNodeCode = normalizedCode,
                    lastModuleId = modId
                )
            )
        }

        // Because "M03A.T1.1" normalized to "M03.T1.1", total unique completed nodes is 3
        assertEquals(3, progressDao.getAll().size)
        assertEquals(3, progressDao.getCountByModule("M03"))
        assertEquals(3, progressDao.getCountByPrefix("M03"))

        val today = dayDao.getByDate("2026-11-10")
        assertTrue(today != null)
        assertEquals(4, today?.nodesCompletedCount)
        assertEquals("M03.T1.1", today?.lastNodeCode)
        assertEquals("M03", today?.lastModuleId)
    }

    @Test
    fun testBugB03AndB05TransactionalNodeToggleAndNonDestructiveStudyDay() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = com.example.data.UserDataRepository(context, db)
        val dateIso = java.time.LocalDate.now().toString()

        repo.toggleNodeCompletionTransactional("M01.T1.1", "M01", dateIso)
        repo.toggleNodeCompletionTransactional("M01.T1.2", "M01", dateIso)

        val dayAfterNodes = db.studyDayDao().getByDate(dateIso)
        assertTrue(dayAfterNodes != null)
        assertEquals(2, dayAfterNodes?.nodesCompletedCount)
        assertEquals("M01.T1.2", dayAfterNodes?.lastNodeCode)
        assertEquals("M01", dayAfterNodes?.lastModuleId)

        repo.recordStudyActivity(dateIso)

        val dayAfterFlashcard = db.studyDayDao().getByDate(dateIso)
        assertTrue(dayAfterFlashcard != null)
        assertEquals(2, dayAfterFlashcard?.nodesCompletedCount)
        assertEquals("M01.T1.2", dayAfterFlashcard?.lastNodeCode)
        assertEquals("M01", dayAfterFlashcard?.lastModuleId)

        repo.toggleNodeCompletionTransactional("M01.T1.2", "M01", dateIso)
        val dayAfterUncheck = db.studyDayDao().getByDate(dateIso)
        assertEquals(1, dayAfterUncheck?.nodesCompletedCount)
    }

    @Test
    fun testBugB08PreserveNoteCreatedAtOnEdit() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = com.example.data.UserDataRepository(context, db)
        val originalCreatedAt = 1700000000000L
        val initialNote = StudyNoteEntity(
            id = "note_b08",
            title = "Judul Awal",
            content = "Isi Awal",
            category = "Fisika Dasar",
            createdAt = originalCreatedAt,
            updatedAt = originalCreatedAt,
            nodeCode = "M01.T1.1",
            moduleId = "M01"
        )
        repo.insertNote(initialNote)

        val existing = repo.getNoteById("note_b08")
        assertTrue(existing != null)
        assertEquals(originalCreatedAt, existing?.createdAt)

        val editedNote = StudyNoteEntity(
            id = "note_b08",
            title = "Judul Diedit",
            content = "Isi Diedit",
            category = "Fisika Dasar",
            createdAt = existing?.createdAt ?: 9999999999999L,
            updatedAt = originalCreatedAt + 50000L,
            nodeCode = "M01.T1.1",
            moduleId = "M01"
        )
        repo.insertNote(editedNote)

        val afterEdit = repo.getNoteById("note_b08")
        assertEquals("Judul Diedit", afterEdit?.title)
        assertEquals(originalCreatedAt, afterEdit?.createdAt)
    }

    @Test
    fun testBugB02AndB13ViewModelQuizAndSeparatedFlashcards() = runBlocking {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val vm = com.example.viewmodel.MriViewModel(app)

        vm.setGlossaryFlashcardMode(true)
        vm.nextFlashcard(10)
        vm.flipFlashcard()
        assertEquals(1, vm.uiState.value.glossaryFlashcardIndex)
        assertTrue(vm.uiState.value.isGlossaryFlashcardFlipped)
        assertEquals(0, vm.uiState.value.moduleFlashcardIndex)
        assertTrue(!vm.uiState.value.isModuleFlashcardFlipped)

        val firstQuiz = vm.uiState.value.filteredQuizzes.first()
        val correctOpt = firstQuiz.correctIndex
        vm.selectQuizOption(correctOpt)
        vm.submitQuizAnswer()
        vm.submitQuizAnswer()
        assertEquals(1, vm.uiState.value.quizScore)
        assertTrue(vm.uiState.value.isAnswerSubmitted)
        assertEquals(correctOpt, vm.uiState.value.quizAnswers[firstQuiz.id])

        vm.nextQuizQuestion()
        assertTrue(!vm.uiState.value.isAnswerSubmitted)
        vm.prevQuizQuestion()
        assertTrue("Soal ke-1 harus tetap berstatus sudah dijawab saat kembali", vm.uiState.value.isAnswerSubmitted)
        assertEquals(correctOpt, vm.uiState.value.selectedOptionIndex)
    }

    @Test
    fun testNB01IdempotentMarkNodeCompletedAndNB03ActiveCurriculumObservers() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = com.example.data.UserDataRepository(context, db)
        val dateIso = java.time.LocalDate.now().toString()

        val firstCall = repo.markNodeCompletedIfNotYet("M03.T1.1", dateIso = dateIso)
        val secondCall = repo.markNodeCompletedIfNotYet("M03.T1.1", dateIso = dateIso)
        val thirdCall = repo.markNodeCompletedIfNotYet("M03.T1.2", dateIso = dateIso)

        assertTrue("Panggilan pertama harus menandai selesai", firstCall)
        assertTrue("Panggilan kedua harus idempoten (mengembalikan false tanpa menghapus)", !secondCall)
        assertTrue("Panggilan simpul kedua harus menandai selesai", thirdCall)

        val completedCodes = db.nodeProgressDao().getAll().map { it.nodeCode }.toSet()
        assertTrue(completedCodes.contains("M03.T1.1"))
        assertTrue(completedCodes.contains("M03.T1.2"))
        assertEquals(2, db.studyDayDao().getByDate(dateIso)?.nodesCompletedCount)

        repo.toggleNodeCompletionTransactional("M03.T1.1", "M03", dateIso)
        repo.toggleNodeCompletionTransactional("M03.T1.2", "M03", dateIso)
        assertEquals(null, db.studyDayDao().getByDate(dateIso))

        db.seedCurriculumIfNeeded()
        assertTrue(repo.observeCurriculumNodes().first().isNotEmpty())
        assertTrue(repo.observeCurriculumNodesByModule("M01").first().isNotEmpty())
        assertTrue(repo.observeCurriculumQuizzes().first().isNotEmpty())
    }

    @Test
    fun testFinal02AndK07AndK09FlashcardStreakProtectionAndLastNodeCleanup() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = com.example.data.UserDataRepository(context, db)
        val dateIso = java.time.LocalDate.now().toString()

        repo.toggleNodeCompletionTransactional("M01.T1.1", "M01", dateIso)
        kotlinx.coroutines.delay(5)
        repo.toggleNodeCompletionTransactional("M01.T1.2", "M01", dateIso)
        assertEquals("M01.T1.2", db.studyDayDao().getByDate(dateIso)?.lastNodeCode)

        repo.toggleNodeCompletionTransactional("M01.T1.2", "M01", dateIso)
        val dayAfterUncheckSecond = db.studyDayDao().getByDate(dateIso)
        assertEquals(1, dayAfterUncheckSecond?.nodesCompletedCount)
        assertEquals("M01.T1.1", dayAfterUncheckSecond?.lastNodeCode)

        repo.recordStudyActivity(dateIso = dateIso, lastNodeCode = "FLASHCARD_REVIEW")
        assertEquals("FLASHCARD_REVIEW", db.studyDayDao().getByDate(dateIso)?.lastNodeCode)

        repo.toggleNodeCompletionTransactional("M01.T1.1", "M01", dateIso)
        val dayAfterAllNodesUnchecked = db.studyDayDao().getByDate(dateIso)
        assertTrue("Streak hari belajar dari flashcard harus tetap dipertahankan", dayAfterAllNodesUnchecked != null)
        assertEquals(0, dayAfterAllNodesUnchecked?.nodesCompletedCount)
        assertEquals("FLASHCARD_REVIEW", dayAfterAllNodesUnchecked?.lastNodeCode)

        repo.toggleNodeCompletionTransactional("M01.T2.1", "M01", dateIso)
        assertEquals("M01.T2.1", db.studyDayDao().getByDate(dateIso)?.lastNodeCode)
        repo.toggleNodeCompletionTransactional("M01.T2.1", "M01", dateIso)
        val dayAfterP3Uncheck = db.studyDayDao().getByDate(dateIso)
        assertTrue("P3: Hari belajar dari flashcard tetap tersimpan meski lastNodeCode sempat tertimpa simpul", dayAfterP3Uncheck != null)
        assertTrue(dayAfterP3Uncheck?.hasFlashcardActivity == true)

        db.seedCurriculumIfNeeded()
        val m09Quizzes = repo.observeCurriculumQuizzesByModule("M09").first().map { it.toDomainModel() }
        val capstoneQuiz = m09Quizzes.find { it.id == "q_m09_t3_1" }
        assertTrue(capstoneQuiz != null)
        assertTrue("Level 3 badge harus dipertahankan", capstoneQuiz?.levelBadge?.contains("Tingkat 3") == true)

        val allQuizzes = repo.observeCurriculumQuizzes().first().map { it.toDomainModel() }
        val shuffledQuizzes = allQuizzes.map { com.example.util.QuizShuffle.shuffled(it) }
        allQuizzes.zip(shuffledQuizzes).forEach { (orig, shuf) ->
            assertEquals(orig.options[orig.correctIndex], shuf.options[shuf.correctIndex])
        }
        val distinctCorrectIndices = shuffledQuizzes.map { it.correctIndex }.toSet()
        assertTrue("P2: Opsi kuis harus teracak ke berbagai indeks", distinctCorrectIndices.size >= 3)

        repo.clearAllBookmarks()
        assertTrue(repo.bookmarkedNodesFlow.first().isEmpty())
        assertTrue(repo.bookmarkedGlossaryFlow.first().isEmpty())

        repo.toggleNodeBookmark("M01.T1.1")
        repo.toggleNodeBookmark("M02.T1.1")
        repo.toggleGlossaryBookmark("Hounsfield Unit (HU / CT Number)")
        assertTrue(repo.bookmarkedNodesFlow.first().containsAll(listOf("M01.T1.1", "M02.T1.1")))
        assertTrue(repo.bookmarkedGlossaryFlow.first().contains("Hounsfield Unit (HU / CT Number)"))

        repo.removeNodeBookmark("M01.T1.1")
        assertEquals(setOf("M02.T1.1"), repo.bookmarkedNodesFlow.first())

        repo.removeGlossaryBookmark("Hounsfield Unit (HU / CT Number)")
        assertTrue(repo.bookmarkedGlossaryFlow.first().isEmpty())

        repo.clearAllBookmarks()
        assertTrue(repo.bookmarkedNodesFlow.first().isEmpty())
    }

    @Test
    fun normalizeLegacyNodeCodes_preservesSkipTestSourceAndUpgradesTargetToRead() = runBlocking {
        val dao = db.nodeProgressDao()
        dao.insert(
            NodeProgressEntity(
                nodeCode = "M03A.T1.1",
                completedAt = 1000L,
                moduleId = "M03A",
                source = "SKIP_TEST"
            )
        )
        dao.insert(
            NodeProgressEntity(
                nodeCode = "M03A.T1.3",
                completedAt = 2000L,
                moduleId = "M03A",
                source = "READ"
            )
        )
        dao.insert(
            NodeProgressEntity(
                nodeCode = "M03.T1.2",
                completedAt = 1500L,
                moduleId = "M03",
                source = "SKIP_TEST"
            )
        )

        db.normalizeLegacyNodeCodes()

        val migratedSkipped = dao.getByNodeCode("M03.T1.1")
        assertEquals("SKIP_TEST", migratedSkipped?.source)
        assertEquals("M03", migratedSkipped?.moduleId)

        val upgradedRead = dao.getByNodeCode("M03.T1.2")
        assertEquals("READ", upgradedRead?.source)
        assertEquals("M03", upgradedRead?.moduleId)
    }
}
