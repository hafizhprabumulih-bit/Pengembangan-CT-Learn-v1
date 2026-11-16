package com.example.data

import com.example.data.local.CtModuleEntity
import com.example.data.local.CurriculumNodeEntity
import com.example.data.local.CurriculumQuizEntity
import java.security.MessageDigest
import java.util.Locale

class CurriculumSnapshot(
    val modules: List<CtModuleEntity>,
    val curriculumNodes: List<CurriculumNodeEntity>,
    val curriculumQuizzes: List<CurriculumQuizEntity>
) {
    val isComplete: Boolean
        get() = modules.isNotEmpty() && curriculumNodes.isNotEmpty() && curriculumQuizzes.isNotEmpty()

    fun fingerprint(): String {
        val digest = MessageDigest.getInstance("SHA-256")
        fun feed(s: String) {
            digest.update(s.toByteArray(Charsets.UTF_8))
            digest.update(0.toByte())
        }
        feed("tag:$SEED_TAG")
        modules.forEach { feed(it.toString()) }
        curriculumNodes.forEach { feed(it.toString()) }
        curriculumQuizzes.forEach { feed(it.toString()) }
        return digest.digest().joinToString("") { String.format(Locale.ROOT, "%02x", it) }
    }

    companion object {
        const val SEED_TAG = "ct_v2_standalone"

        fun build() = CurriculumSnapshot(
            modules = CtRepository.toCtModuleEntities(),
            curriculumNodes = CtRepository.toCurriculumNodeEntities(),
            curriculumQuizzes = CtRepository.toCurriculumQuizEntities()
        )
    }
}
