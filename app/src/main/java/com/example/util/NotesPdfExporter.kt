package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import androidx.core.content.FileProvider
import com.example.data.StudyNote
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.time.LocalDate

data class PrintableNoteSection(
    val title: String,
    val metaLine: String,
    val bodyLines: List<String>
)

object NotesPdfExporter {

    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842
    private const val MARGIN_X = 44f
    private const val MAX_LINE_CHARS = 74

    fun wrapTextLines(text: String, maxCharsPerLine: Int = MAX_LINE_CHARS): List<String> {
        if (text.isBlank()) return emptyList()
        val limit = maxCharsPerLine.coerceAtLeast(16)
        val out = mutableListOf<String>()
        text.replace("\r\n", "\n").split('\n').forEach { paragraph ->
            val trimmed = paragraph.trim()
            if (trimmed.isEmpty()) {
                out.add("")
                return@forEach
            }
            val words = trimmed.split(Regex("\\s+"))
            val current = StringBuilder()
            for (word in words) {
                if (current.isEmpty()) {
                    if (word.length <= limit) {
                        current.append(word)
                    } else {
                        word.chunked(limit).forEachIndexed { idx, chunk ->
                            if (idx < (word.length - 1) / limit) out.add(chunk) else current.append(chunk)
                        }
                    }
                } else if (current.length + 1 + word.length <= limit) {
                    current.append(' ').append(word)
                } else {
                    out.add(current.toString())
                    current.clear()
                    if (word.length <= limit) {
                        current.append(word)
                    } else {
                        word.chunked(limit).forEachIndexed { idx, chunk ->
                            if (idx < (word.length - 1) / limit) out.add(chunk) else current.append(chunk)
                        }
                    }
                }
            }
            if (current.isNotEmpty()) out.add(current.toString())
        }
        return out
    }

    fun buildCompetencyReportNote(
        userName: String,
        completedNodes: Int,
        totalNodes: Int,
        totalXp: Int,
        tryoutBestPercent: Int,
        completedConsoleCasesCount: Int
    ): StudyNote {
        val today = LocalDate.now().toString()
        val pct = if (totalNodes > 0) ((completedNodes * 100f) / totalNodes).toInt() else 0
        val body = buildString {
            appendLine("Peserta: ${userName.ifBlank { "Radiografer CT" }}")
            appendLine("Tanggal Rapor: $today")
            appendLine("Progres Kurikulum: $completedNodes/$totalNodes simpul ($pct%)")
            appendLine("Total XP Kompetensi: $totalXp XP")
            appendLine("Skor Terbaik Try-Out UKOM CT: $tryoutBestPercent%")
            appendLine("Kasus Konsol Klinis Lulus: $completedConsoleCasesCount / 5 kasus")
        }.trimEnd()
        return StudyNote(
            id = "ct_competency_report_$today",
            title = "Rapor Ringkasan Kompetensi CT-Learn",
            content = body,
            category = "Rapor Kompetensi",
            dateString = today
        )
    }

    fun buildCtProtocolReferenceNotes(): List<StudyNote> {
        val today = LocalDate.now().toString()
        return listOf(
            StudyNote(
                id = "ct_ref_hu",
                title = "Lembar Saku CT: Skala Hounsfield Unit (HU) & Windowing",
                content = "Rumus: HU = 1000 x (mu_jaringan - mu_air) / mu_air\n" +
                    "Udara: -1000 HU | Paru: -750..-600 HU | Lemak: -100..-50 HU | Air: 0 HU | CSF: +5..+15 HU\n" +
                    "Alba Otak: +25..+30 HU | Grisea Otak: +38..+42 HU | Darah Akut: +55..+80 HU | Tulang Korteks: +1000..+1500 HU\n" +
                    "Preset: Stroke (WW 40, WL 40), Brain (WW 80, WL 40), Abdomen (WW 350, WL 40), Lung (WW 1500, WL -600), Bone (WW 2000, WL 500).",
                category = "Protokol Klinis",
                dateString = today
            ),
            StudyNote(
                id = "ct_ref_dosimetry_qa",
                title = "Lembar Saku CT: Rumus Pitch, Dosimetri & QA Fantom Air",
                content = "Beam Pitch = d / (N x T) | Effective mAs = (mA x t_rot) / Pitch\n" +
                    "DLP = CTDIvol x L (cm) | Dosis Efektif E = DLP x faktor k | SSDE = f_size x CTDIvol\n" +
                    "Bolus Tracking CTA: ROI aorta/pulmonalis, trigger +100..+150 HU, Saline Chaser 40 mL.\n" +
                    "QA Fantom Air Harian (5-ROI): Akurasi pusat 0 +/- 4 HU; Uniformity |perifer - pusat| <= 5 HU.",
                category = "Proteksi Dosis & QA",
                dateString = today
            )
        )
    }

    fun buildPrintableSections(
        notes: List<StudyNote>,
        includeCtReferenceSheet: Boolean = false
    ): List<PrintableNoteSection> {
        val combined = if (includeCtReferenceSheet) notes + buildCtProtocolReferenceNotes() else notes
        return combined.mapIndexed { idx, note ->
            val safeTitle = note.title.ifBlank { "Catatan Tanpa Judul" }
            val safeCat = note.category.ifBlank { "Catatan Bebas" }
            val safeDate = note.dateString.ifBlank { LocalDate.now().toString() }
            PrintableNoteSection(
                title = "${idx + 1}. $safeTitle",
                metaLine = "Kategori: $safeCat  |  Tanggal: $safeDate",
                bodyLines = wrapTextLines(note.content.ifBlank { "(Catatan kosong)" })
            )
        }
    }

    fun buildPlainPreviewText(
        notes: List<StudyNote>,
        userName: String,
        includeCtReferenceSheet: Boolean = false
    ): String {
        val owner = userName.ifBlank { "Peserta CT-Learn" }
        val sections = buildPrintableSections(notes, includeCtReferenceSheet)
        if (sections.isEmpty()) {
            return "Dokumen PDF Catatan Belajar CT Scan\nPemilik: $owner\nBelum ada catatan untuk dicetak."
        }
        return buildString {
            appendLine("DOKUMEN PDF · CATATAN BELAJAR CT SCAN")
            appendLine(
                "Pemilik: $owner · Total: ${notes.size} catatan" +
                    if (includeCtReferenceSheet) " + Lembar Saku Protokol CT" else ""
            )
            appendLine("Format: A4 Siap Cetak (.pdf)")
            appendLine("----------------------------------------")
            sections.forEach { sec ->
                appendLine(sec.title)
                appendLine(sec.metaLine)
                sec.bodyLines.forEach { line -> appendLine("  $line") }
                appendLine()
            }
        }
    }

    fun createPdfDocument(
        notes: List<StudyNote>,
        userName: String,
        includeCtReferenceSheet: Boolean = false
    ): PdfDocument {
        val doc = PdfDocument()
        val sections = buildPrintableSections(notes, includeCtReferenceSheet)
        val owner = userName.ifBlank { "Peserta CT-Learn" }
        val dateLabel = LocalDate.now().toString()

        val headerBgPaint = Paint().apply { color = Color.rgb(10, 29, 51) }
        val accentPaint = Paint().apply { color = Color.rgb(34, 211, 238) }
        val titleHeaderPaint = Paint().apply {
            color = Color.WHITE
            textSize = 18f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        val subtitleHeaderPaint = Paint().apply {
            color = Color.rgb(165, 243, 252)
            textSize = 10.5f
            isAntiAlias = true
        }
        val sectionTitlePaint = Paint().apply {
            color = Color.rgb(11, 27, 50)
            textSize = 13f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        val metaPaint = Paint().apply {
            color = Color.rgb(14, 116, 144)
            textSize = 9.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        val bodyPaint = Paint().apply {
            color = Color.rgb(30, 41, 59)
            textSize = 10.8f
            isAntiAlias = true
        }
        val dividerPaint = Paint().apply {
            color = Color.rgb(203, 213, 225)
            strokeWidth = 1f
        }
        val footerPaint = Paint().apply {
            color = Color.rgb(100, 116, 139)
            textSize = 9f
            isAntiAlias = true
        }

        var pageNumber = 1
        var page = doc.startPage(PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create())
        var canvas = page.canvas

        fun drawHeader(c: Canvas) {
            c.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), 84f, headerBgPaint)
            c.drawRect(0f, 80f, PAGE_WIDTH.toFloat(), 84f, accentPaint)
            c.drawText("Catatan Belajar CT Scan", MARGIN_X, 38f, titleHeaderPaint)
            c.drawText(
                "Pemilik: $owner  ·  Total: ${notes.size} catatan" +
                    (if (includeCtReferenceSheet) " + Lembar Saku" else "") +
                    "  ·  Dicetak: $dateLabel",
                MARGIN_X,
                60f,
                subtitleHeaderPaint
            )
        }

        fun drawFooter(c: Canvas, num: Int) {
            c.drawLine(MARGIN_X, PAGE_HEIGHT - 44f, PAGE_WIDTH - MARGIN_X, PAGE_HEIGHT - 44f, dividerPaint)
            c.drawText("CT-Learn · Ringkasan Catatan Klinis & Fisika CT Scan", MARGIN_X, PAGE_HEIGHT - 26f, footerPaint)
            c.drawText("Hal. $num", PAGE_WIDTH - MARGIN_X - 36f, PAGE_HEIGHT - 26f, footerPaint)
        }

        drawHeader(canvas)
        var y = 114f
        val maxY = PAGE_HEIGHT - 64f

        if (sections.isEmpty()) {
            canvas.drawText("Belum ada catatan tersimpan untuk dicetak.", MARGIN_X, y, bodyPaint)
            drawFooter(canvas, pageNumber)
            doc.finishPage(page)
            return doc
        }

        sections.forEach { sec ->
            if (y + 52f > maxY) {
                drawFooter(canvas, pageNumber)
                doc.finishPage(page)
                pageNumber++
                page = doc.startPage(PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create())
                canvas = page.canvas
                drawHeader(canvas)
                y = 114f
            }

            canvas.drawText(sec.title, MARGIN_X, y, sectionTitlePaint)
            y += 16f
            canvas.drawText(sec.metaLine, MARGIN_X, y, metaPaint)
            y += 18f

            sec.bodyLines.forEach { line ->
                if (y + 16f > maxY) {
                    drawFooter(canvas, pageNumber)
                    doc.finishPage(page)
                    pageNumber++
                    page = doc.startPage(PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create())
                    canvas = page.canvas
                    drawHeader(canvas)
                    y = 114f
                }
                if (line.isNotEmpty()) {
                    canvas.drawText(line, MARGIN_X + 6f, y, bodyPaint)
                }
                y += 15f
            }

            y += 8f
            if (y < maxY - 12f) {
                canvas.drawLine(MARGIN_X, y, PAGE_WIDTH - MARGIN_X, y, dividerPaint)
                y += 18f
            }
        }

        drawFooter(canvas, pageNumber)
        doc.finishPage(page)
        return doc
    }

    fun writeNotesPdf(
        notes: List<StudyNote>,
        userName: String,
        outputStream: OutputStream,
        includeCtReferenceSheet: Boolean = false
    ) {
        val doc = createPdfDocument(notes, userName, includeCtReferenceSheet)
        try {
            doc.writeTo(outputStream)
        } finally {
            doc.close()
        }
    }

    fun printNotesPdf(
        context: Context,
        notes: List<StudyNote>,
        userName: String,
        includeCtReferenceSheet: Boolean = false
    ): Result<Unit> = runCatching {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
            ?: error("Layanan cetak PDF sistem tidak tersedia di perangkat ini.")
        val jobName = "Catatan_Belajar_CT_${LocalDate.now()}"
        val adapter = object : PrintDocumentAdapter() {
            override fun onLayout(
                oldAttributes: PrintAttributes?,
                newAttributes: PrintAttributes?,
                cancellationSignal: CancellationSignal?,
                callback: LayoutResultCallback,
                extras: Bundle?
            ) {
                if (cancellationSignal?.isCanceled == true) {
                    callback.onLayoutCancelled()
                    return
                }
                val info = PrintDocumentInfo.Builder("$jobName.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                    .build()
                callback.onLayoutFinished(info, true)
            }

            override fun onWrite(
                pages: Array<out PageRange>,
                destination: ParcelFileDescriptor,
                cancellationSignal: CancellationSignal?,
                callback: WriteResultCallback
            ) {
                if (cancellationSignal?.isCanceled == true) {
                    callback.onWriteCancelled()
                    return
                }
                runCatching {
                    FileOutputStream(destination.fileDescriptor).use { out ->
                        writeNotesPdf(notes, userName, out, includeCtReferenceSheet)
                    }
                }.onSuccess {
                    callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                }.onFailure { err ->
                    callback.onWriteFailed(err.localizedMessage ?: "Gagal menulis dokumen PDF.")
                }
            }
        }
        printManager.print(jobName, adapter, PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_A4).build())
    }

    fun shareNotesPdf(
        context: Context,
        notes: List<StudyNote>,
        userName: String,
        includeCtReferenceSheet: Boolean = false
    ): Result<Unit> = runCatching {
        val sharedDir = File(context.cacheDir, "shared_cards").apply { mkdirs() }
        val fileName = "catatan_belajar_ct_${LocalDate.now()}.pdf"
        val pdfFile = File(sharedDir, fileName)
        FileOutputStream(pdfFile).use { out ->
            writeNotesPdf(notes, userName, out, includeCtReferenceSheet)
        }
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", pdfFile)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_SUBJECT, "Catatan Belajar CT Scan (PDF)")
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Bagikan Catatan PDF"))
    }
}
