package com.example.util

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.random.Random

enum class CtMinigameKind(val key: String, val title: String, val subtitle: String, val durationSec: Int) {
    BORE_SCREENING(
        key = "BORE_SCREENING",
        title = "Skrining Persiapan Bore CT",
        subtitle = "Loloskan benda radiolusen aman atau singkirkan logam pemicu Streak Artifact",
        durationSec = 35
    ),
    BOLUS_TRIGGER_TAP(
        key = "BOLUS_TRIGGER_TAP",
        title = "Pemicu ROI Bolus Tracking",
        subtitle = "Ketuk Trigger tepat saat kurva atenuasi menyentuh ambang +HU target",
        durationSec = 35
    ),
    WINDOW_ARTIFACT_FIX(
        key = "WINDOW_ARTIFACT_FIX",
        title = "Windowing HU & Artefak CT",
        subtitle = "Pilih preset Window Width/Level & solusi artefak pesawat CT secara cepat",
        durationSec = 35
    )
}

enum class CtSafetyClass { CT_SAFE, CT_CONDITIONAL_MAR, CT_HIGH_RISK }

data class CtScreeningItem(
    val id: String,
    val name: String,
    val codeTag: String,
    val safetyClass: CtSafetyClass,
    val allowIntoGantry: Boolean,
    val reason: String,
    val assetKey: String = ""
)

enum class TapAccuracy(val label: String, val basePoints: Int) {
    PERFECT("Sempurna!", 120),
    GOOD("Mendekati", 70),
    MISS("Meleset", 0)
}

data class BolusTriggerChallenge(
    val id: String,
    val protocolLabel: String,
    val targetHuLabel: String,
    val targetNormalized: Float,
    val speedMultiplier: Float
)

data class WindowArtifactQuestion(
    val id: String,
    val prompt: String,
    val badge: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

/**
 * Engine murni untuk 3 minigame edukatif CT Scan berdurasi singkat ("Zona Cepat CT").
 */
object CtMinigameEngine {

    val screeningBank: List<CtScreeningItem> = listOf(
        CtScreeningItem("scr_o2", "Tabung O₂ Baja di Atas Meja Scan", "O2", CtSafetyClass.CT_HIGH_RISK, false, "Logam baja tebal di lintasan berkas memicu photon starvation dan streak artifact parah.", "TABUNG_O2"),
        CtScreeningItem("scr_clip", "Jepit Rambut & Anting Logam di Kepala", "KLIP", CtSafetyClass.CT_HIGH_RISK, false, "Logam di area kepala wajib dilepas karena memicu starburst metal streak pada CT Brain.", "KLIP_LOGAM"),
        CtScreeningItem("scr_ecg_lead", "Kabel EKG & Lead Logam Melintasi FOV Toraks", "LEAD", CtSafetyClass.CT_HIGH_RISK, false, "Lead logam yang melintasi area scan harus dirapikan ke luar FOV agar tidak menimbulkan streak.", "PACEMAKER"),
        CtScreeningItem("scr_scissors", "Gunting Instrumen Baja di Saku Pasien", "GUNTING", CtSafetyClass.CT_HIGH_RISK, false, "Benda logam padat di saku menyerap habis sinar-X (photon starvation) dan merusak citra.", "GUNTING_BAJA"),
        CtScreeningItem("scr_watch", "Jam Tangan Logam di Pergelangan (Posisi di Dada)", "JAM", CtSafetyClass.CT_HIGH_RISK, false, "Jam logam wajib dilepas agar tidak menimbulkan beam hardening & streak artifact.", "JAM_MEKANIK"),
        CtScreeningItem("scr_coin", "Koin & Kunci Logam di Saku Celana (CT Pelvis)", "KUNCI", CtSafetyClass.CT_HIGH_RISK, false, "Seluruh koin dan kunci logam di saku wajib dilepas sebelum pemindaian abdomen-pelvis.", "KOIN_KUNCI"),
        CtScreeningItem("scr_card", "Kalung & Lanyard Klip Besi di Leher", "KLIP_L", CtSafetyClass.CT_HIGH_RISK, false, "Kalung/klip logam di leher mengaburkan evaluasi tiroid, laring, dan apeks paru.", "KARTU_ATM"),
        CtScreeningItem("scr_bore_obj", "Benda Asing di Bore saat Air Calibration", "BORE", CtSafetyClass.CT_HIGH_RISK, false, "Bore gantry wajib kosong total saat Fast Air Calibration agar tabel gain DAS akurat.", "KURSI_RODA_BESI"),
        CtScreeningItem("scr_pillow", "Bantal Fiksasi Busa Radiolusen", "BANTAL", CtSafetyClass.CT_SAFE, true, "Busa poliuretan ber-atenuasi sangat rendah aman menjaga kepala tetap diam di Isocenter.", "BANTAL_BUSA"),
        CtScreeningItem("scr_cradle_cf", "Meja & Cradle Carbon Fiber (Z = 6)", "CARBON", CtSafetyClass.CT_SAFE, true, "Serat karbon kuat menahan beban pasien tanpa menyerap sinar-X maupun memicu artefak.", "KURSI_RODA_MR"),
        CtScreeningItem("scr_sponge", "Spons Imobilisasi Kepala Non-Logam", "SPONS", CtSafetyClass.CT_SAFE, true, "Bebas logam dan mencegah motion artifact selama rotasi gantry.", "EARPLUG"),
        CtScreeningItem("scr_gown", "Gaun Pasien Katun Tanpa Kancing Logam", "GAUN", CtSafetyClass.CT_SAFE, true, "Kain katun polos tanpa ritsleting/kancing logam bebas dari streak artifact pada CT.", "GAUN_KATUN"),
        CtScreeningItem("scr_iodine", "Syringe Kontras Iodin Non-Ionik (LOCM)", "IODIN", CtSafetyClass.CT_SAFE, true, "Media kontras Iodin (Z=53) disiapkan di Dual-Head Power Injector untuk CT Angiografi.", "VIAL_KONTRAS"),
        CtScreeningItem("scr_phantom", "Fantom Air Silinder Akrilik QA (0 HU)", "PHANTOM", CtSafetyClass.CT_SAFE, true, "Digunakan untuk uji QA harian 5-ROI guna memverifikasi akurasi air 0 ± 4 HU.", "PHANTOM_ACR"),
        CtScreeningItem("scr_iv_cannula", "Kanul IV Abocath 18G–20G di Fossa Antekubiti", "IV_18G", CtSafetyClass.CT_CONDITIONAL_MAR, true, "Ukuran kanul standar yang aman untuk laju alir Power Injector 4–5 mL/detik (≤ 300 PSI).", "STETOSKOP"),
        CtScreeningItem("scr_stent_cond", "Stent Koroner (Dievaluasi dengan Sharp Kernel)", "STENT", CtSafetyClass.CT_CONDITIONAL_MAR, true, "Aman dipindai pada CCTA; gunakan Sharp Kernel & irisan tipis untuk menekan blooming artifact.", "STENT_COND"),
        CtScreeningItem("scr_phone", "Ponsel & Sabuk Gesper Besi di Pinggang", "PONSEL", CtSafetyClass.CT_HIGH_RISK, false, "Gesper besi dan ponsel di pinggang wajib dilepas karena merusak citra CT Abdomen.", "PONSEL_LOGAM"),
        CtScreeningItem("scr_heavy_metal", "Tabung Logam Berat di Samping Tubuh Pasien", "LOGAM", CtSafetyClass.CT_HIGH_RISK, false, "Logam berat di dalam SFOV menyebabkan saturasi atenuasi dan pita gelap hebat.", "TABUNG_PEMADAM"),
        CtScreeningItem("scr_glasses", "Kacamata Bingkai Logam (CT Kepala / Sinus)", "KACAMATA", CtSafetyClass.CT_HIGH_RISK, false, "Bingkai logam wajib dilepas pada CT Kepala/Orbita/Sinus karena memicu streak di orbita.", "KACA_MATA_LOGAM"),
        CtScreeningItem("scr_infusion", "Pompa Infus Terlalu Dekat Tanpa Jalur Panjang", "POMPA", CtSafetyClass.CT_HIGH_RISK, false, "Pastikan selang infus cukup panjang dan bebas sangkutan saat meja bergerak helikal cepat.", "POMPA_INFUS_BIASA"),
        CtScreeningItem("scr_bottle", "Botol Termos Baja di Atas Dada Pasien", "BOTOL", CtSafetyClass.CT_HIGH_RISK, false, "Benda logam eksternal wajib disingkirkan dari area pemindaian.", "BOTOL_MINUM_LOGAM"),
        CtScreeningItem("scr_strap", "Sabuk Fiksasi Velcro Non-Logam", "VELCRO", CtSafetyClass.CT_SAFE, true, "Sabuk pengikat bebas logam aman mencegah pergerakan pasien di atas meja karbon.", "SANDBAG_NONMAG"),
        CtScreeningItem("scr_blanket", "Selimut Katun Polos Hangat", "SELIMUT", CtSafetyClass.CT_SAFE, true, "Katun polos bebas kancing logam menjaga kenyamanan pasien di ruang CT ber-AC dingin.", "SELIMUT_KATUN"),
        CtScreeningItem("scr_saline", "Saline Chaser NaCl 0,9% (Tabung B Injektor)", "SALINE", CtSafetyClass.CT_CONDITIONAL_MAR, true, "Mendorong bolus kontras lebih padat dan membilas vena kava superior dari streak artifact.", "TABUNG_O2_ALU"),
        CtScreeningItem("scr_ti_plate", "Implan Ortopedi Titanium (Aktifkan Algoritma MAR)", "TITANIUM", CtSafetyClass.CT_CONDITIONAL_MAR, true, "Implan permanen dapat dipindai dengan menaikkan kVp (120–140 kV) dan mengaktifkan MAR.", "IMPLAN_TITANIUM"),
        CtScreeningItem("scr_shield", "Pelindung Gonad / Thyroid Shield di Luar FOV", "SHIELD", CtSafetyClass.CT_SAFE, true, "Pelindung timbal di luar area scan primer mengurangi paparan radiasi hambur.", "HEADSET_PNEUMATIK"),
        CtScreeningItem("scr_o2_mask", "Kanul Nasal Oksigen Polimer Bebas Kawat", "KANUL_O2", CtSafetyClass.CT_SAFE, true, "Selang oksigen plastik murni tidak menimbulkan artefak pada pemindaian kepala/toraks.", "MASKER_OKSIGEN_PLASTIK"),
        CtScreeningItem("scr_scalpel", "Instrumen Bedah Baja Tertinggal di Atas Kain", "SKALPEL", CtSafetyClass.CT_HIGH_RISK, false, "Instrumen logam tajam di atas tubuh pasien memicu streak artifact dan membahayakan pasien.", "PISAU_BEDAH_BAJA"),
        CtScreeningItem("scr_hammer", "Palu Refleks Besi di Saku Baju Pasien", "PALU", CtSafetyClass.CT_HIGH_RISK, false, "Batang besi wajib dikeluarkan dari pakaian pasien sebelum pengambilan Topogram Scout.", "PALU_REFLEKS_BESI"),
        CtScreeningItem("scr_ring", "Bros, Peniti & Perhiasan Logam di Dada", "CINCIN", CtSafetyClass.CT_HIGH_RISK, false, "Peniti dan perhiasan logam wajib dilepas agar tidak mengaburkan parenkim paru & mediastinum.", "CINCIN_FEROMAGNETIK"),
        CtScreeningItem("scr_lanyard", "Kalung Rantai Logam pada CT Leher/Toraks", "LANYARD", CtSafetyClass.CT_HIGH_RISK, false, "Rantai logam menyebabkan starburst streak artifact di area servikal dan toraks atas.", "KARTU_AKSES_MAGNET")
    )

    val bolusTriggerChallenges: List<BolusTriggerChallenge> = listOf(
        BolusTriggerChallenge("bol_ctpa", "CTPA Emboli Paru (ROI Trunkus Pulmonalis)", "+100 HU", 0.22f, 0.60f),
        BolusTriggerChallenge("bol_cta_brain", "CTA Karotis & Serebral (ROI Arkus Aorta)", "+110 HU", 0.35f, 0.72f),
        BolusTriggerChallenge("bol_ccta", "CTA Koroner / CCTA (ROI Aorta Ascendens)", "+120 HU", 0.52f, 0.85f),
        BolusTriggerChallenge("bol_aorta_thor", "CTA Aorta Torakalis (Bolus Tracking 100 kVp)", "+130 HU", 0.44f, 0.95f),
        BolusTriggerChallenge("bol_aorta_abd", "CTA Aorta Abdominalis (ROI Aorta Descendens)", "+150 HU", 0.72f, 1.10f),
        BolusTriggerChallenge("bol_liver", "Fase Arteri Akhir Hepar (Triphasic Liver)", "+140 HU", 0.62f, 1.20f),
        BolusTriggerChallenge("bol_runoff", "CTA Runoff Ekstremitas Bawah (High Peak)", "+160 HU", 0.84f, 1.35f)
    )

    val windowArtifactBank: List<WindowArtifactQuestion> = listOf(
        WindowArtifactQuestion(
            id = "wa_1",
            prompt = "Untuk membedakan substansia grisea (+40 HU) dan substansia alba (+28 HU) pada CT Kepala rutin, preset Windowing manakah yang digunakan?",
            badge = "Windowing Otak",
            options = listOf("Brain Window (WW 80, WL 40)", "Lung Window (WW 1500, WL -600)", "Bone Window (WW 2000, WL 500)"),
            correctIndex = 0,
            explanation = "WW sempit (80 HU) dengan pusat WL 40 HU memberikan kontras tinggi untuk memisahkan jaringan otak yang hanya berselisih ~12–15 HU."
        ),
        WindowArtifactQuestion(
            id = "wa_2",
            prompt = "Pada kecurigaan stroke iskemik hiperakut, pengaturan Windowing manakah yang dipilih untuk menajamkan hilangnya diferensiasi kortiko-subkortikal?",
            badge = "Stroke Window",
            options = listOf("Stroke Window (WW 40, WL 40)", "Abdomen Window (WW 350, WL 40)", "Lung Window (WW 1500, WL -600)"),
            correctIndex = 0,
            explanation = "Mempersempit WW menjadi 40 HU (rentang +20 s.d. +60 HU) menonjolkan perubahan hipodensitas dini edema sitotoksik."
        ),
        WindowArtifactQuestion(
            id = "wa_3",
            prompt = "Tampak lingkaran konsentris tajam tepat mengelilingi pusat Isocenter pada potongan aksial CT Generasi ke-3. Apa langkah korektif pertamanya?",
            badge = "Ring Artifact",
            options = listOf("Kosongkan bore gantry dan jalankan Fast Air Calibration", "Menaikkan nilai Pitch menjadi 2,0", "Meminta pasien menahan napas"),
            correctIndex = 0,
            explanation = "Ring artifact disebabkan oleh drift gain/offset satu kanal detektor pada geometri Rotate-Rotate Gen-3; Air Calibration menyamakan kembali tabel gain DAS."
        ),
        WindowArtifactQuestion(
            id = "wa_4",
            prompt = "Muncul pita gelap (dark bands) di antara kedua os petrosum pada fossa posterior kepala serta efek cupping di tengah silinder. Ini adalah ...",
            badge = "Beam Hardening",
            options = listOf("Beam Hardening Artifact (Hounsfield Streak)", "Ring Artifact detektor", "Out-of-Field Artifact"),
            correctIndex = 0,
            explanation = "Penyerapan foton energi rendah oleh tulang petrosus yang padat mengeraskan berkas polikromatik dan memicu pita gelap (dark bands)."
        ),
        WindowArtifactQuestion(
            id = "wa_5",
            prompt = "Perdarahan intrakranial akut (hematoma epidural/intraserebral akut) pada CT Kepala Non-Kontras akan tampak ...",
            badge = "Skala HU Klinis",
            options = listOf("Hiperdens terang (+60 s.d. +85 HU)", "Hipodens gelap seperti udara (-1000 HU)", "Isodens sama dengan air murni (0 HU)"),
            correctIndex = 0,
            explanation = "Konsentrasi protein globin yang tinggi pada bekuan darah akut meningkatkan atenuasi sinar-X menjadi +60 hingga +85 HU."
        ),
        WindowArtifactQuestion(
            id = "wa_6",
            prompt = "Bayangan ganda (double contour) dan garis kabur muncul pada CT Toraks karena pasien bernapas saat scan. Solusi teknisnya adalah ...",
            badge = "Motion Artifact",
            options = listOf("Instruksi tahan napas jelas, percepat rotasi gantry (0,28–0,35 s) & naikkan Pitch", "Menurunkan kVp menjadi 60 kV", "Mengganti detektor menjadi gas Xenon"),
            correctIndex = 0,
            explanation = "Mempercepat waktu rotasi gantry dan meningkatkan Pitch helikal mempersingkat total waktu akuisisi sehingga meminimalkan artefak gerak napas."
        ),
        WindowArtifactQuestion(
            id = "wa_7",
            prompt = "Untuk mengevaluasi parenkim paru (-800 HU) sekaligus mencari garis fraktur kosta (+1500 HU) dari satu kali scan CT Toraks, diperlukan ...",
            badge = "Retro-Recon & Kernel",
            options = listOf("Retro-Recon dari Raw Data dengan Sharp/Lung Kernel (WW 1500, WL -600) & Bone Window (WW 2000, WL 500)", "Memindai ulang pasien 3 kali berturut-turut", "Menggunakan Soft Kernel dengan WW 40, WL 40"),
            correctIndex = 0,
            explanation = "Raw Data Sinogram dapat direkonstruksi ulang (Retro-Recon) dengan Kernel tajam serta ditampilkan pada Lung Window dan Bone Window tanpa eksposi tambahan."
        ),
        WindowArtifactQuestion(
            id = "wa_8",
            prompt = "Tambalan logam gigi atau prostesis panggul memicu garis pancaran bintang terang-gelap (starburst streaks) akibat photon starvation. Solusinya?",
            badge = "Metal Artifact",
            options = listOf("Naikkan kVp (120–140 kV), gunakan irisan tipis, dan aktifkan algoritma MAR / Dual-Energy", "Turunkan kVp ke 70 kV dan tebalkan irisan menjadi 10 mm", "Lepaskan Bow-Tie Filter dari gantry"),
            correctIndex = 0,
            explanation = "Tegangan tinggi 120–140 kVp meningkatkan daya tembus foton, irisan tipis mengurangi partial volume, dan algoritma MAR menginterpolasi proyeksi sinogram yang rusak."
        ),
        WindowArtifactQuestion(
            id = "wa_9",
            prompt = "Berapakah nilai standar Hounsfield Unit (HU) untuk Air Murni (fantom kalibrasi) dan Udara Bebas?",
            badge = "Patokan Skala HU",
            options = listOf("Air Murni = 0 HU dan Udara Bebas = -1000 HU", "Air Murni = +1000 HU dan Udara Bebas = 0 HU", "Air Murni = -500 HU dan Udara Bebas = +500 HU"),
            correctIndex = 0,
            explanation = "Dari rumus HU = 1000 x (mu - mu_air) / mu_air, air murni bernilai tepat 0 HU dan udara (mu ~ 0) bernilai -1000 HU."
        ),
        WindowArtifactQuestion(
            id = "wa_10",
            prompt = "Jika ketinggian meja pasien meleset 5 cm terlalu tinggi dari garis Laser Koronal Isocenter saat mengambil Topogram Scout AP, apa dampaknya pada AEC?",
            badge = "Off-Centering & AEC",
            options = listOf("Bayangan tubuh di topogram tampak membesar (magnifikasi) sehingga AEC memberikan mA berlebih (overdosis)", "Sistem AEC otomatis mematikan tabung sinar-X", "Kualitas citra menjadi bebas noise dengan dosis nol"),
            correctIndex = 0,
            explanation = "Posisi pasien yang terlalu dekat ke tabung saat Scout AP memperbesar bayangan topogram, membuat AEC salah mengira tubuh pasien lebih tebal dan menaikkan arus mA."
        ),
        WindowArtifactQuestion(
            id = "wa_11",
            prompt = "Teknik Post-Processing 3D manakah yang hanya menampilkan nilai HU paling rendah di sepanjang slab untuk menilai emfisema dan percabangan bronkus?",
            badge = "3D Post-Processing",
            options = listOf("Minimum Intensity Projection (MinIP)", "Maximum Intensity Projection (MIP)", "Simple Back-Projection"),
            correctIndex = 0,
            explanation = "MinIP memproyeksikan voxel ber-HU terendah (udara di trakeobronkial dan area air-trapping emfisema), berkebalikan dengan MIP yang menonjolkan pembuluh berkontras."
        ),
        WindowArtifactQuestion(
            id = "wa_12",
            prompt = "Berapakah syarat kelulusan Akurasi HU Air di ROI pusat dan Keseragaman (Uniformity) pada uji QA Fantom Air 5-ROI harian?",
            badge = "QA Fantom Air Harian",
            options = listOf("HU Air pusat = 0 ± 4 HU dan selisih Uniformity tepi vs pusat ≤ 5 HU", "HU Air pusat = +100 ± 50 HU dan Uniformity ≤ 100 HU", "HU Air pusat = -1000 HU dan Uniformity = 0 HU"),
            correctIndex = 0,
            explanation = "Standar kendali mutu harian pesawat CT mewajibkan rata-rata CT Number air di pusat berada pada 0 ± 4 HU dan selisih perifer terhadap pusat maksimal 5 HU."
        )
    )

    /**
     * Mengacak deck tantangan tanpa mengulang elemen terakhir pada awal siklus berikutnya.
     */
    fun <T> buildNonRepeatingDeck(
        items: List<T>,
        previousLast: T? = null,
        random: Random = Random.Default
    ): List<T> {
        if (items.size <= 1) return items
        val shuffled = items.shuffled(random).toMutableList()
        if (previousLast != null && shuffled.first() == previousLast) {
            val swapIndex = 1 + random.nextInt(shuffled.size - 1)
            val tmp = shuffled[0]
            shuffled[0] = shuffled[swapIndex]
            shuffled[swapIndex] = tmp
        }
        return shuffled
    }

    /**
     * Mengakumulasi fase osilasi jarum (0f..2f) secara kontinu per delta frame
     * agar perubahan kecepatan kombo tidak membuat jarum melompat.
     */
    fun advanceBolusPhase(
        currentPhase: Float,
        deltaSec: Float,
        speedMultiplier: Float,
        combo: Int
    ): Float {
        if (deltaSec <= 0f) return currentPhase
        val comboBoost = 1f + (combo.coerceIn(0, 8) * 0.10f)
        val halfCyclesPerSec = 0.44f * speedMultiplier.coerceAtLeast(0.35f) * comboBoost
        return (currentPhase + deltaSec.coerceAtMost(0.05f) * halfCyclesPerSec) % 2f
    }

    /**
     * Memetakan fase [0..2) ke posisi jarum ter-normalisasi [0.06..0.94] dengan kurva kosinus halus.
     */
    fun needleFromPhase(phase: Float): Float {
        val norm = ((phase % 2f) + 2f) % 2f
        val eased = (1f - cos(norm * PI.toFloat())) * 0.5f
        return (0.06f + eased * 0.88f).coerceIn(0.06f, 0.94f)
    }

    fun evaluateBolusTriggerTap(needleNormalized: Float, targetNormalized: Float): TapAccuracy {
        val diff = abs(needleNormalized - targetNormalized)
        return when {
            diff <= 0.06f -> TapAccuracy.PERFECT
            diff <= 0.14f -> TapAccuracy.GOOD
            else -> TapAccuracy.MISS
        }
    }

    fun computePoints(basePoints: Int, comboStreak: Int): Int {
        if (basePoints <= 0) return 0
        val bonus = (comboStreak.coerceAtLeast(0) * 20).coerceAtMost(120)
        return basePoints + bonus
    }

    fun evaluateRank(score: Int, accuracyPercent: Int): String = when {
        score >= 900 && accuracyPercent >= 85 -> "Ahli Konsol CT Kilat"
        score >= 500 && accuracyPercent >= 65 -> "Radiografer CT Sigap"
        else -> "Peserta Shift CT-Learn"
    }
}
