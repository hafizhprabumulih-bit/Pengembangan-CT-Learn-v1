package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.example.data.CurriculumSnapshot

@Database(
    entities = [
        NodeProgressEntity::class,
        StudyNoteEntity::class,
        QuizAttemptEntity::class,
        StudyDayEntity::class,
        CtModuleEntity::class,
        CurriculumNodeEntity::class,
        CurriculumQuizEntity::class
    ],
    version = DB_VERSION,
    exportSchema = true
)
abstract class CtDatabase : RoomDatabase() {
    abstract fun nodeProgressDao(): NodeProgressDao
    abstract fun studyNoteDao(): StudyNoteDao
    abstract fun quizAttemptDao(): QuizAttemptDao
    abstract fun studyDayDao(): StudyDayDao
    abstract fun ctCurriculumDao(): CtCurriculumDao
    abstract fun curriculumDao(): CurriculumDao

    suspend fun replaceCurriculum(snapshot: CurriculumSnapshot) {
        if (!snapshot.isComplete) return
        val ctDao = ctCurriculumDao()
        val currDao = curriculumDao()
        withTransaction {
            ctDao.clearModules()
            currDao.clearNodes()
            currDao.clearQuizzes()
            ctDao.insertModules(snapshot.modules)
            currDao.insertNodes(snapshot.curriculumNodes)
            currDao.insertQuizzes(snapshot.curriculumQuizzes)
        }
    }

    suspend fun isCurriculumPresent(snapshot: CurriculumSnapshot): Boolean =
        ctCurriculumDao().getModulesCount() == snapshot.modules.size &&
        curriculumDao().getNodesCount() == snapshot.curriculumNodes.size &&
        curriculumDao().getQuizzesCount() == snapshot.curriculumQuizzes.size

    suspend fun seedCurriculumIfNeeded() = replaceCurriculum(CurriculumSnapshot.build())

    companion object {
        @Volatile
        private var INSTANCE: CtDatabase? = null

        @Suppress("DEPRECATION")
        /**
         * Jangan menambah fallbackToDestructiveMigration*() di sini. Tabel data pengguna
         * (node_progress, study_notes, quiz_attempts, study_days) ada di file DB yang sama,
         * sehingga fallback apa pun akan menghapus progres pengguna tanpa peringatan.
         * Perubahan skema HARUS lewat Migration yang nyata. Downgrade dibiarkan gagal keras
         * agar data tidak hilang.
         */
        fun getDatabase(context: Context): CtDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CtDatabase::class.java,
                    "ct_learn.db"
                )
                    .addMigrations(*ALL_MIGRATIONS)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
