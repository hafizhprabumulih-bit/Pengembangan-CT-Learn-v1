package com.example.util

data class CtClinicalConsoleCase(
    val id: String,
    val title: String,
    val clinicalScenario: String,
    val recommendedKvpDesc: String,
    val validKvpSet: Set<Int>,
    val idealEffectiveMasRange: IntRange,
    val idealPitchRange: ClosedFloatingPointRange<Float>,
    val maxTargetCtdiVol: Float,
    val expectedHeadPhantom: Boolean,
    val recommendedWindowHint: String,
    val keyTakeaway: String
)

data class CtConsoleCaseEvaluation(
    val passed: Boolean,
    val scoreOutOf100: Int,
    val passedChecks: List<String>,
    val warnings: List<String>,
    val summary: String
)

/**
 * Evaluator murni (JVM-testable) untuk skenario Kasus Konsol Klinis CT Scan pada Simulator 4.
 */
object CtConsoleCaseEvaluator {

    val clinicalCases: List<CtClinicalConsoleCase> = listOf(
        CtClinicalConsoleCase(
            id = "case_stroke",
            title = "Kasus 1: Code Stroke Hiperakut (Kepala Non-Kontras)",
            clinicalScenario = "Pasien dewasa 62 tahun mendadak hemiparesis 75 menit lalu. Perlu deteksi perbedaan halus substansia grisea vs alba (Low-Contrast tinggi) tanpa melewati DRL Kepala 60 mGy.",
            recommendedKvpDesc = "120 kVp (Fantom 16 cm)",
            validKvpSet = setOf(120, 140),
            idealEffectiveMasRange = 200..360,
            idealPitchRange = 0.50f..1.00f,
            maxTargetCtdiVol = 60.0f,
            expectedHeadPhantom = true,
            recommendedWindowHint = "Stroke (40/40) atau Brain (80/40)",
            keyTakeaway = "Kepala Non-Kontras butuh Eff. mAs memadai (200-360 mAs) dan Pitch <= 1,0 dengan 120 kVp serta Stroke/Brain Window agar noise sangat rendah di bawah DRL 60 mGy."
        ),
        CtClinicalConsoleCase(
            id = "case_ctpa",
            title = "Kasus 2: CTPA Emboli Paru Akut (CTA Toraks Cepat)",
            clinicalScenario = "Pasien sesak napas berat dengan curiga emboli arteri pulmonalis (hanya kuat tahan napas 4 detik). Maksimalkan penyangatan Iodin & kecepatan scan dengan dosis tubuh <= 15 mGy.",
            recommendedKvpDesc = "80 atau 100 kVp (Fantom 32 cm)",
            validKvpSet = setOf(80, 100),
            idealEffectiveMasRange = 75..190,
            idealPitchRange = 1.05f..1.50f,
            maxTargetCtdiVol = 15.0f,
            expectedHeadPhantom = false,
            recommendedWindowHint = "Angio/CTA (700/200)",
            keyTakeaway = "Pada CTPA pasien sesak, Pitch > 1,0 mempercepat akuisisi bebas artefak napas dan 80-100 kVp memaksimalkan efek fotolistrik Iodin (K-edge 33,2 keV) sekaligus menekan dosis."
        ),
        CtClinicalConsoleCase(
            id = "case_pediatric",
            title = "Kasus 3: CT Abdomen Anak 5 Tahun (18 kg, Proteksi ALARA)",
            clinicalScenario = "Anak 5 tahun nyeri kuadran kanan bawah curiga apendisitis perforasi. Wajib terapkan prinsip ALARA pediatrik: turunkan kVp & mAs dengan CTDIvol 32 cm <= 7,0 mGy.",
            recommendedKvpDesc = "80 atau 100 kVp (Fantom 32 cm)",
            validKvpSet = setOf(80, 100),
            idealEffectiveMasRange = 40..115,
            idealPitchRange = 0.90f..1.40f,
            maxTargetCtdiVol = 7.0f,
            expectedHeadPhantom = false,
            recommendedWindowHint = "Abdomen (350/40)",
            keyTakeaway = "Pasien anak jauh lebih radiosensitif; pemilihan 80-100 kVp + Eff. mAs rendah (40-115 mAs) menjaga CTDIvol <= 7 mGy sehingga nilai SSDE anak tetap aman."
        ),
        CtClinicalConsoleCase(
            id = "case_trauma_head",
            title = "Kasus 4: Trauma Kepala & Fraktur Basis Cranii",
            clinicalScenario = "Pasien kecelakaan lalu lintas mengalami otorea dan hematoma periorbital. Evaluasi fraktur halus os petrosum dan perdarahan epidural dengan CTDIvol Kepala <= 65 mGy.",
            recommendedKvpDesc = "120 kVp (Fantom 16 cm)",
            validKvpSet = setOf(120, 140),
            idealEffectiveMasRange = 180..340,
            idealPitchRange = 0.50f..0.95f,
            maxTargetCtdiVol = 65.0f,
            expectedHeadPhantom = true,
            recommendedWindowHint = "Bone (2000/500) + Brain (80/40)",
            keyTakeaway = "Trauma kepala membutuhkan retro-rekonstruksi ganda: Bone Window (WW 2000, WL 500) dengan Sharp Kernel untuk fraktur basis cranii, serta Brain Window untuk perdarahan intrakranial."
        ),
        CtClinicalConsoleCase(
            id = "case_hrct_lung",
            title = "Kasus 5: HRCT Parenkim Paru (Suspek ILD / Emfisema)",
            clinicalScenario = "Evaluasi bronkiektasis dan pola retikuler halus pada parenkim paru. Jaga keseimbangan dosis toraks <= 12 mGy dan kecepatan akuisisi bebas artefak napas.",
            recommendedKvpDesc = "100 atau 120 kVp (Fantom 32 cm)",
            validKvpSet = setOf(100, 120),
            idealEffectiveMasRange = 60..145,
            idealPitchRange = 0.95f..1.40f,
            maxTargetCtdiVol = 12.0f,
            expectedHeadPhantom = false,
            recommendedWindowHint = "Lung (1500/-600)",
            keyTakeaway = "HRCT paru memerlukan Lung Window (WW 1500, WL -600) dengan Kernel tajam dan CTDIvol <= 12 mGy untuk memisahkan septa alveolar tanpa membebani dosis jaringan sensitif payudara/tiroid."
        )
    )

    fun evaluateCase(
        caseId: String,
        kvp: Int,
        effectiveMas: Float,
        pitch: Float,
        ctdiVol: Float,
        isHeadPhantom: Boolean,
        selectedWindowLabel: String? = null
    ): CtConsoleCaseEvaluation {
        val spec = clinicalCases.firstOrNull { it.id == caseId } ?: clinicalCases.first()
        val passedChecks = mutableListOf<String>()
        val warnings = mutableListOf<String>()
        var score = 0

        if (kvp in spec.validKvpSet) {
            score += 20
            passedChecks += "kVp ($kvp kVp) sesuai kebutuhan energi berkas kasus."
        } else {
            warnings += "Tegangan $kvp kVp kurang optimal (anjuran: ${spec.recommendedKvpDesc})."
        }

        val effMasInt = effectiveMas.toInt()
        if (effMasInt in spec.idealEffectiveMasRange) {
            score += 25
            passedChecks += "Effective mAs ($effMasInt mAs) seimbang antara noise & dosis."
        } else if (effMasInt < spec.idealEffectiveMasRange.first) {
            warnings += "Eff. mAs ($effMasInt mAs) terlalu rendah — risiko quantum noise menenggelamkan lesi (target ${spec.idealEffectiveMasRange.first}..${spec.idealEffectiveMasRange.last})."
        } else {
            warnings += "Eff. mAs ($effMasInt mAs) terlalu tinggi — boros dosis radiasi (target ${spec.idealEffectiveMasRange.first}..${spec.idealEffectiveMasRange.last})."
        }

        if (pitch in spec.idealPitchRange) {
            score += 20
            passedChecks += "Pitch (${String.format(java.util.Locale.US, "%.2f", pitch)}) ideal untuk kebutuhan kecepatan & kualitas z."
        } else {
            warnings += "Pitch (${String.format(java.util.Locale.US, "%.2f", pitch)}) di luar rentang anjuran (${spec.idealPitchRange.start}..${spec.idealPitchRange.endInclusive})."
        }

        if (isHeadPhantom == spec.expectedHeadPhantom) {
            score += 15
            passedChecks += "Pemilihan fantom acuan CTDI (${if (isHeadPhantom) "16 cm Kepala" else "32 cm Tubuh"}) tepat."
        } else {
            warnings += "Fantom acuan dosis salah pilih (seharusnya ${if (spec.expectedHeadPhantom) "16 cm Kepala" else "32 cm Tubuh"})."
        }

        if (ctdiVol <= spec.maxTargetCtdiVol) {
            score += 20
            passedChecks += "CTDIvol (${String.format(java.util.Locale.US, "%.1f", ctdiVol)} mGy) aman di bawah batas target ${spec.maxTargetCtdiVol} mGy."
        } else {
            warnings += "CTDIvol (${String.format(java.util.Locale.US, "%.1f", ctdiVol)} mGy) melebihi target keselamatan ${spec.maxTargetCtdiVol} mGy!"
        }

        if (!selectedWindowLabel.isNullOrBlank()) {
            passedChecks += "Rujukan preset tampilan konsol: ${spec.recommendedWindowHint} (aktif di tab Windowing: $selectedWindowLabel)."
        }

        val passed = score >= 80 && warnings.isEmpty()
        val summary = when {
            passed -> "LULUS OPTIMAL ($score/100): Protokol konsol aman & diagnostik untuk ${spec.title}."
            score >= 60 -> "CUKUP ($score/100): Beberapa parameter perlu disetel ulang agar sesuai target klinis."
            else -> "PERLU PERBAIKAN ($score/100): Periksa keseimbangan kVp, mAs, Pitch, dan batas CTDIvol."
        }

        return CtConsoleCaseEvaluation(
            passed = passed,
            scoreOutOf100 = score.coerceIn(0, 100),
            passedChecks = passedChecks,
            warnings = warnings,
            summary = summary
        )
    }
}
