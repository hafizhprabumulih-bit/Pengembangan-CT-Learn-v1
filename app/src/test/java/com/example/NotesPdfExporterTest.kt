package com.example

import com.example.data.StudyNote
import com.example.util.NotesPdfExporter
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotesPdfExporterTest {

    @Test
    fun `wrapTextLines wraps long paragraphs within limit without dropping words`() {
        val longText = "Hukum atenuasi linier Beer-Lambert berbanding terbalik terhadap jarak lintasan berkas sinar-X pada jaringan."
        val lines = NotesPdfExporter.wrapTextLines(longText, maxCharsPerLine = 32)
        assertTrue(lines.size >= 3)
        assertTrue(lines.all { it.length <= 32 })
        assertTrue(lines.joinToString(" ").contains("Beer-Lambert"))
        assertTrue(lines.joinToString(" ").contains("jaringan."))
    }

    @Test
    fun `buildPlainPreviewText formats notes cleanly for PDF preview instead of JSON`() {
        val notes = listOf(
            StudyNote(
                id = "n1",
                title = "Ringkasan Window Width dan Window Level",
                content = "Brain window memakai WW 80 dan WL 40.",
                category = "Protokol Klinis",
                dateString = "2026-11-16"
            )
        )
        val preview = NotesPdfExporter.buildPlainPreviewText(notes, "Radiografer")
        assertTrue(preview.contains("PDF"))
        assertTrue(preview.contains("Ringkasan Window Width dan Window Level"))
        assertFalse(preview.trimStart().startsWith("{"))
    }

    @Test
    fun `buildPlainPreviewText appends CT protocol reference when includeCtReferenceSheet is true`() {
        val preview = NotesPdfExporter.buildPlainPreviewText(
            notes = emptyList(),
            userName = "Radiografer CT",
            includeCtReferenceSheet = true
        )
        assertTrue(preview.contains("Hounsfield Unit"))
        assertTrue(preview.contains("CTDIvol"))
    }

    @Test
    fun `buildCompetencyReportNote includes XP tryout and console progress`() {
        val report = NotesPdfExporter.buildCompetencyReportNote(
            userName = "Budi Radiografer",
            completedNodes = 40,
            totalNodes = 50,
            totalXp = 640,
            tryoutBestPercent = 85,
            completedConsoleCasesCount = 4
        )
        val preview = NotesPdfExporter.buildPlainPreviewText(listOf(report), "Budi Radiografer", includeCtReferenceSheet = false)
        assertTrue(preview.contains("40/50 simpul (80%)"))
        assertTrue(preview.contains("640 XP"))
        assertTrue(preview.contains("85%"))
    }
}