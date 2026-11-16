package com.example.data

import com.example.R
import com.example.data.local.CurriculumNodeEntity
import com.example.data.local.CurriculumQuizEntity
import com.example.data.local.MriModuleEntity
import org.json.JSONArray

data class MedicalAvatar(
    val id: String,
    val label: String,
    val roleTitle: String,
    val badgeIcon: String,
    val colorHex: Long
)

data class MriFlashcard(
    val id: String,
    val moduleId: String = id.substringBefore("_").uppercase().ifBlank { "M01" },
    val nodeCode: String = "${id.substringBefore("_").uppercase().ifBlank { "M01" }}.T1.1",
    val category: String = "Konsep Inti CT",
    val frontTitle: String,
    val frontSubtitle: String = "Ketuk kartu untuk melihat penjelasan & rumus",
    val backContent: String,
    val clinicalAnalogyOrTip: String = backContent,
    val isGlossaryCard: Boolean = false,
    val glossaryTermKey: String = frontTitle
) {
    val front: String get() = frontTitle
    val back: String get() = backContent

    constructor(
        id: String,
        front: String,
        back: String
    ) : this(
        id = id,
        moduleId = id.substringBefore("_").uppercase().ifBlank { "M01" },
        nodeCode = "${id.substringBefore("_").uppercase().ifBlank { "M01" }}.T1.1",
        category = "Konsep Inti CT",
        frontTitle = front,
        frontSubtitle = "Ketuk kartu untuk melihat penjelasan & rumus",
        backContent = back,
        clinicalAnalogyOrTip = back,
        isGlossaryCard = false,
        glossaryTermKey = front
    )
}

data class TissueProperties(
    val id: String = "tissue",
    val name: String,
    val t1Ms: Float,
    val t2Ms: Float,
    val protonDensity: Float,
    val description: String = "",
    val clinicalNote: String = description
) {
    val pdRelative: Float get() = protonDensity

    constructor(
        name: String,
        t1Ms: Float,
        t2Ms: Float,
        protonDensity: Float,
        description: String = ""
    ) : this(
        id = name.lowercase().replace(" ", "_"),
        name = name,
        t1Ms = t1Ms,
        t2Ms = t2Ms,
        protonDensity = protonDensity,
        description = description,
        clinicalNote = description
    )
}

data class MriNode(
    val id: String = "",
    val code: String,
    val title: String,
    val level: Int, // 0: Tujuan, 1: Dasar, 2: Menengah, 3: Lanjut
    val sortOrder: Int = 0,
    val prerequisite: String? = null,
    val learningGoal: String? = null,
    val summaryShort: String,
    val detailFull: String,
    val analogi: String,
    val crossLinks: String? = null,
    val labType: String? = null
) {
    val prerequisites: List<String>
        get() = prerequisite
            ?.split(",")
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?: emptyList()

    constructor(
        code: String,
        level: Int,
        title: String,
        summaryShort: String,
        detailFull: String,
        analogi: String,
        prerequisite: String? = null,
        learningGoal: String? = null,
        crossLinks: String? = null,
        labType: String? = null,
        sortOrder: Int = 0
    ) : this(
        id = code.lowercase().replace(".", "_"),
        code = code,
        title = title,
        level = level,
        sortOrder = sortOrder,
        prerequisite = prerequisite,
        learningGoal = learningGoal,
        summaryShort = summaryShort,
        detailFull = detailFull,
        analogi = analogi,
        crossLinks = crossLinks,
        labType = labType
    )
}

data class MriModule(
    val id: String,
    val code: String = id.uppercase(),
    val title: String,
    val subtitle: String,
    val description: String = subtitle,
    val levelBadge: String = "Tingkat 1–3 • Kurikulum CT Scan",
    val iconName: String = "bolt",
    val sortOrder: Int = 0,
    val prerequisites: List<String> = emptyList(),
    val estimatedMinutes: Int = 45,
    val totalQuizzes: Int = 6,
    val nodes: List<MriNode>,
    val flashcards: List<MriFlashcard> = emptyList(),
    val badgeName: String = "Penjelajah CT Scan",
    val unlockMessage: String = "Selamat! Kamu telah menguasai modul CT Scan ini."
)

data class GlossaryItem(
    val term: String,
    val symbolOrAbbr: String = "",
    val definition: String,
    val moduleId: String = "M01",
    val nodeCode: String = "$moduleId.T1.1",
    val relatedModules: List<String> = listOf(moduleId)
) {
    constructor(
        term: String,
        definition: String,
        relatedModules: List<String>
    ) : this(
        term = term.substringBefore(" (").trim(),
        symbolOrAbbr = if ("(" in term && ")" in term) {
            term.substringAfter("(").substringBefore(")").trim()
        } else {
            relatedModules.firstOrNull() ?: "CT"
        },
        definition = definition,
        moduleId = relatedModules.firstOrNull() ?: "M01",
        nodeCode = "${relatedModules.firstOrNull() ?: "M01"}.T1.1",
        relatedModules = relatedModules
    )
}

data class StudyNote(
    val id: String,
    val title: String,
    val content: String,
    val category: String, // "Formula", "Protokol", "Klinis", "Artefak"
    val dateString: String = "Hari ini"
) {
    val updatedAt: String get() = dateString
}

data class QuizQuestion(
    val id: String,
    val moduleId: String,
    val nodeCode: String,
    val level: Int = 1,
    val levelBadge: String = when (level) {
        1 -> "Tingkat 1 • Dasar"
        2 -> "Tingkat 2 • Menengah"
        else -> "Tingkat 3 • Lanjut"
    },
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
) {
    val moduleCode: String get() = moduleId
}

typealias MriQuizQuestion = QuizQuestion

private fun encodeStringListJson(items: List<String>): String {
    val arr = JSONArray()
    items.forEach { arr.put(it) }
    return arr.toString()
}

private fun decodeStringListJson(raw: String): List<String> {
    return try {
        val arr = JSONArray(raw)
        List(arr.length()) { idx -> arr.optString(idx, "") }
    } catch (_: Exception) {
        emptyList()
    }
}

fun CurriculumNodeEntity.toDomainModel(): MriNode = MriNode(
    id = code.lowercase().replace(".", "_"),
    code = code,
    title = title,
    level = level,
    sortOrder = sortOrder,
    prerequisite = prerequisite,
    learningGoal = learningGoal,
    summaryShort = summaryShort,
    detailFull = detailFull,
    analogi = analogi,
    crossLinks = crossLinks,
    labType = labType
)

fun CurriculumQuizEntity.toDomainModel(): QuizQuestion {
    val parsedOptions = decodeStringListJson(optionsJson)
    return QuizQuestion(
        id = id,
        moduleId = moduleId,
        nodeCode = nodeCode,
        level = level,
        question = question,
        options = parsedOptions,
        correctIndex = correctIndex,
        explanation = explanation
    )
}

fun MriModuleEntity.toDomainModel(nodes: List<MriNode> = emptyList()): MriModule {
    val staticMod = MriRepository.modules.find { it.id.equals(id, ignoreCase = true) }
    return MriModule(
        id = id,
        code = code,
        title = title,
        subtitle = subtitle,
        description = description,
        levelBadge = levelBadge,
        iconName = iconName,
        sortOrder = sortOrder,
        prerequisites = staticMod?.prerequisites ?: emptyList(),
        estimatedMinutes = staticMod?.estimatedMinutes ?: 45,
        totalQuizzes = staticMod?.totalQuizzes ?: 6,
        nodes = nodes.ifEmpty { staticMod?.nodes ?: emptyList() },
        flashcards = staticMod?.flashcards ?: emptyList(),
        badgeName = staticMod?.badgeName ?: "Penjelajah CT Scan",
        unlockMessage = staticMod?.unlockMessage ?: "Selamat! Kamu telah menguasai modul CT Scan ini."
    )
}

object MriRepository {
    const val GAMMA_1H: Float = 42.58f
    const val GAMMA_19F: Float = 40.05f
    const val GAMMA_31P: Float = 17.24f
    const val GAMMA_23NA: Float = 11.26f
    const val GAMMA_13C: Float = 10.71f

    val avatars = listOf(
        MedicalAvatar("radiografer_mri", "Radiografer", "Ahli Akuisisi & Konsol CT", "RF", 0xFF0284C7),
        MedicalAvatar("radiolog", "Radiolog", "Spesialis Diagnostik Imajing CT", "MD", 0xFF4F46E5),
        MedicalAvatar("ahli_neuro_mri", "Neuro-Radiolog", "Pakar CT Kepala & Perfusi Stroke", "NR", 0xFF7C3AED),
        MedicalAvatar("fisikawan_medis", "Fisikawan Medis", "Pakar Instrumentasi, QA & Dosimetri CT", "MP", 0xFF059669),
        MedicalAvatar("petugas_proteksi_radiasi", "MRSO", "Spesialis Proteksi Radiasi & ALARA CT", "SO", 0xFFD97706),
        MedicalAvatar("ahli_jantung", "Kardiolog", "Spesialis CT Angiografi Koroner", "CR", 0xFFE11D48),
        MedicalAvatar("perawat", "Perawat", "Ahli Kanulasi IV & Kontras Iodin", "RN", 0xFF0D9488),
        MedicalAvatar("dokter_bedah", "Dokter Bedah", "Pakar Perencanaan Bedah 3D VRT", "SG", 0xFF2563EB),
        MedicalAvatar("dokter_robot", "Asisten AI", "Sistem Rekonstruksi Iteratif & DLIR", "AI", 0xFF6366F1),
        MedicalAvatar("dosen", "Dosen", "Pendidik Fisika & Instrumentasi CT", "PR", 0xFF8B5CF6),
        MedicalAvatar("mahasiswa", "Mahasiswa", "Residen & Mahasiswa Radiologi", "ST", 0xFF10B981)
    )

    fun getAvatarDrawableRes(avatarId: String): Int = when (avatarId) {
        "radiografer_mri" -> R.drawable.avatar_radiografer_mri
        "radiolog" -> R.drawable.avatar_radiolog
        "ahli_neuro_mri" -> R.drawable.avatar_ahli_neuro_mri
        "fisikawan_medis" -> R.drawable.avatar_fisikawan_medis
        "petugas_proteksi_radiasi" -> R.drawable.avatar_petugas_proteksi_radiasi
        "ahli_jantung" -> R.drawable.avatar_ahli_jantung
        "perawat" -> R.drawable.avatar_perawat
        "dokter_bedah" -> R.drawable.avatar_dokter_bedah
        "dokter_robot" -> R.drawable.avatar_dokter_robot
        "dosen" -> R.drawable.avatar_dosen
        "mahasiswa" -> R.drawable.avatar_mahasiswa
        else -> R.drawable.avatar_radiografer_mri
    }

    val tissues = listOf(
        TissueProperties("csf", "Air Murni / CSF (0 HU)", 2500f, 1500f, 1.00f, "Standar kalibrasi skala Hounsfield Unit (0 HU); tampak hipodens gelap pada Brain Window (WW 80, WL 40)."),
        TissueProperties("gm", "Substansia Grisea (+40 HU)", 920f, 100f, 0.85f, "Korteks dan nukleus basal otak (+35 s.d. +45 HU); tampak sedikit lebih terang dibanding substansia alba."),
        TissueProperties("wm", "Substansia Alba (+28 HU)", 780f, 90f, 0.72f, "Jaras serabut mielin otak (+20 s.d. +30 HU); kandungan lipid mielin membuatnya lebih hipodens dibanding korteks."),
        TissueProperties("muscle", "Parenkim Hepar / Otot (+55 HU)", 850f, 50f, 0.90f, "Jaringan lunak parenkim padat (+45 s.d. +65 HU non-kontras); menyengat hingga +100 s.d. +120 HU pada fase vena porta."),
        TissueProperties("blood", "Darah Beku Akut (+75 HU)", 650f, 60f, 0.92f, "Hematoma intrakranial atau hemoperitoneum akut (+60 s.d. +85 HU); hiperdens terang pada CT non-kontras karena konsentrasi globin."),
        TissueProperties("fat", "Jaringan Lemak (-80 HU)", 260f, 80f, 0.60f, "Jaringan adiposa subkutis dan retroperitoneal (-50 s.d. -100 HU); menjadi bantalan kontras alami antar-organ abdomen."),
        TissueProperties("lung", "Parenkim Paru (-800 HU)", 1200f, 40f, 0.20f, "Alveoli berisi udara dan kapiler paru (-700 s.d. -900 HU); dievaluasi optimal menggunakan Lung Window (WW 1500, WL -600)."),
        TissueProperties("bone", "Tulang Kortikal (+1500 HU)", 500f, 10f, 0.99f, "Kalsium apatit padat ber-Z efektif tinggi (+1000 s.d. +3000 HU); dievaluasi dengan Sharp Kernel dan Bone Window (WW 2000, WL 500).")
    )

    val modules: List<MriModule> = CtCurriculumModulesPart1.modulesM01ToM05 + CtCurriculumModulesPart2.modulesM06ToM09

    val quizzes: List<QuizQuestion> = CtCurriculumQuizzes.quizzes

    val glossary = listOf(
        GlossaryItem("Air Calibration (Kalibrasi Udara)", "Prosedur pemindaian udara kosong di dalam bore gantry setiap pagi setelah Tube Warm-Up untuk menyamakan tabel gain dan offset seluruh kanal detektor dan mencegah Ring Artifact.", listOf("M03", "M09")),
        GlossaryItem("AEC (Automatic Exposure Control)", "Sistem modulasi arus tabung (mA) otomatis sepanjang sumbu longitudinal (z) dan sudut rotasi (x-y) berdasarkan topogram scout untuk menjaga tingkat noise konstan sekaligus menekan dosis radiasi.", listOf("M02", "M07")),
        GlossaryItem("Beam Hardening (Pengerasan Berkas)", "Peningkatan energi efektif rata-rata berkas sinar-X polikromatik saat menembus materi karena foton berenergi rendah terserap lebih dulu; memicu cupping artifact dan dark bands.", listOf("M01", "M02", "M09")),
        GlossaryItem("Beer-Lambert Law (Hukum Beer-Lambert)", "Persamaan penurunan intensitas foton sinar-X secara eksponensial saat menembus materi: I = I0 * exp(-mu * x).", listOf("M01", "M05")),
        GlossaryItem("Bolus Tracking (SmartPrep / CareBolus)", "Teknik sinkronisasi kontras otomatis dengan memantau nilai HU di dalam ROI pembuluh darah target secara real-time hingga melampaui nilai ambang pemicu (misal +120 HU).", listOf("M08")),
        GlossaryItem("Bow-Tie Filter (Filter Dasi Kupu-Kupu)", "Filter pembentuk berkas berbentuk dasi kupu-kupu (tipis di tengah, tebal di tepi) yang menyeragamkan intensitas sinar-X pada detektor dan mengurangi dosis permukaan kulit perifer pasien.", listOf("M01", "M02")),
        GlossaryItem("CTDIvol (Volume CT Dose Index)", "Indeks dosis rata-rata per satuan volume irisan pada fantom akrilik standar (16 cm atau 32 cm) yang telah memperhitungkan faktor Pitch: CTDIvol = CTDIw / Pitch (dalam satuan mGy).", listOf("M04", "M07")),
        GlossaryItem("DAS (Data Acquisition System)", "Rangkaian elektronik berkecepatan tinggi di belakang busur detektor yang mengintegrasikan arus fotodioda, melakukan konversi analog-ke-digital (ADC), dan menghitung transformasi logaritmik ln(I0/I).", listOf("M02", "M03")),
        GlossaryItem("DLP (Dose Length Product)", "Hasil kali antara CTDIvol (mGy) dan panjang total rentang pemindaian L (cm) dalam satuan mGy.cm; mencerminkan total energi radiasi yang diberikan selama satu seri pemeriksaan.", listOf("M07")),
        GlossaryItem("Filtered Back-Projection (FBP)", "Algoritma rekonstruksi analitik klasik yang menerapkan filter konvolusi frekuensi tinggi (Ramp Filter) pada data proyeksi sinogram sebelum diproyeksikan balik ke matriks piksel untuk menghilangkan kabur 1/r.", listOf("M05")),
        GlossaryItem("Gantry Isocenter (Pusat Rotasi Gantry)", "Titik pusat perpotongan sumbu rotasi tabung-detektor di tengah bore gantry; penempatan organ target tepat di isocenter menggunakan laser positioning sangat krusial untuk kinerja Bow-Tie Filter dan AEC.", listOf("M02", "M07")),
        GlossaryItem("Hounsfield Unit (HU / CT Number)", "Skala kuantitatif terstandarisasi koefisien atenuasi linier jaringan relatif terhadap air murni: HU = 1000 * (mu_jaringan - mu_air) / mu_air, di mana Air = 0 HU dan Udara = -1000 HU.", listOf("M01", "M06", "M09")),
        GlossaryItem("Iterative Reconstruction (IR & DLIR)", "Algoritma rekonstruksi modern berbasis pemodelan statistik foton dan jaringan saraf tiruan yang menekan quantum noise secara selektif sehingga memungkinkan protokol CT dosis rendah.", listOf("M05", "M07")),
        GlossaryItem("Kernel (Filter Konvolusi)", "Fungsi matematika dalam rekonstruksi CT yang mengatur keseimbangan antara ketajaman tepi (resolusi spasial) dan kehalusan citra (noise), misalnya Soft/Standard Kernel vs Sharp/Bone Kernel.", listOf("M05", "M06")),
        GlossaryItem("MIP & MinIP (Proyeksi Intensitas)", "Maximum Intensity Projection (menampilkan nilai HU tertinggi untuk angiografi dan nodul paru) serta Minimum Intensity Projection (menampilkan nilai HU terendah untuk pohon trakeobronkial dan emfisema).", listOf("M08")),
        GlossaryItem("Pitch (Helical Beam Pitch)", "Rasio pergeseran meja pasien per satu rotasi gantry penuh (d) terhadap lebar kolimasi total berkas sinar-X pada isocenter (W): Pitch = d / (N * T).", listOf("M04", "M07")),
        GlossaryItem("Ring Artifact (Artefak Cincin)", "Artefak berbentuk lingkaran konsentris berpusat di isocenter pada pesawat CT Generasi ke-3 akibat satu atau beberapa elemen detektor mengalami drift kalibrasi atau kerusakan kanal.", listOf("M03", "M04", "M09")),
        GlossaryItem("Sinogram (Data Proyeksi Mentah)", "Matriks data mentah (raw data) 2D yang memetakan profil atenuasi logaritmik ln(I0/I) sepanjang posisi kanal detektor (s) terhadap sudut rotasi proyeksi gantry (theta).", listOf("M03", "M05")),
        GlossaryItem("Slip-Ring (Cincin Luncur)", "Antarmuka elektromekanis berupa cincin konduktif dan sikat penyapu (serta pemancar data optik) yang menyalurkan daya listrik dan data tanpa kabel sehingga gantry dapat berputar 360 derajat secara kontinu.", listOf("M02", "M03", "M04")),
        GlossaryItem("Solid-State Scintillation Detector (GOS)", "Detektor matriks MSCT modern yang memadukan kristal sintilator keramik (Gd2O2S / GOS) pengubah sinar-X menjadi cahaya tampak dan fotodioda silikon pengubah cahaya menjadi arus listrik.", listOf("M03", "M04")),
        GlossaryItem("Tube Warm-Up (Pemanasan Tabung)", "Prosedur eksposi bertahap wajib di pagi hari untuk menaikkan suhu anoda tabung sinar-X ke tingkat kerja aman guna mencegah retak termal (thermal shock) dan arcing tegangan tinggi.", listOf("M03", "M09")),
        GlossaryItem("Voxel Isotropik (Kubus 3D)", "Elemen volume 3D berbentuk kubus sempurna dengan ukuran piksel bidang x-y sama persis dengan ketebalan irisan sumbu-z (dx = dy = dz), syarat utama reformasi MPR bebas distorsi.", listOf("M04", "M06", "M08")),
        GlossaryItem("Window Width & Window Level (WW/WL)", "Parameter tampilan layar CT di mana WL menentukan titik tengah kecerahan sesuai HU organ target dan WW menentukan lebar rentang HU yang dipetakan dari hitam ke putih.", listOf("M06")),
        GlossaryItem("z-Flying Focal Spot (Double z-Sampling)", "Defleksi elektromagnetik berkas elektron pada anoda tabung yang menggeser titik fokus bolak-balik sepanjang sumbu-z untuk menggandakan jumlah irisan sampel per rotasi dan menekan artefak windmill.", listOf("M03", "M04"))
    )

    val flashcards: List<MriFlashcard> by lazy {
        val nodeCards = modules.flatMap { mod ->
            mod.nodes.filter { it.level >= 1 }.map { node ->
                MriFlashcard(
                    id = "fc_${node.code}",
                    moduleId = mod.id,
                    nodeCode = node.code,
                    category = "Materi ${mod.code}",
                    frontTitle = node.title,
                    frontSubtitle = "Modul ${mod.code} • Tingkat ${node.level}",
                    backContent = node.summaryShort,
                    clinicalAnalogyOrTip = node.analogi,
                    isGlossaryCard = false,
                    glossaryTermKey = node.title
                )
            }
        }
        val glossaryCards = glossary.mapIndexed { idx, g ->
            MriFlashcard(
                id = "fc_glossary_$idx",
                moduleId = g.moduleId,
                nodeCode = g.nodeCode,
                category = "Glosarium CT",
                frontTitle = g.term,
                frontSubtitle = "Simbol/Singkatan: ${g.symbolOrAbbr}",
                backContent = g.definition,
                clinicalAnalogyOrTip = "Rujukan Modul: ${g.moduleId} (${g.nodeCode})",
                isGlossaryCard = true,
                glossaryTermKey = g.term
            )
        }
        nodeCards + glossaryCards
    }

    val defaultNotes = listOf(
        StudyNote(
            id = "note_1",
            title = "Rumus Inti Fisika, Geometri Helikal & Dosimetri CT",
            content = "1. Hukum Beer-Lambert: I = I0 * exp(-mu * x) dan HVL = 0,693 / mu\n" +
                "2. Skala Hounsfield Unit: HU = 1000 * (mu_jaringan - mu_air) / mu_air\n" +
                "3. Ukuran Piksel: Pixel Size (mm) = DFOV (mm) / Matrix Size\n" +
                "4. Beam Pitch Helikal: Pitch = Table Feed per rotasi (d) / Total Lebar Kolimasi Berkas (N * T)\n" +
                "5. Effective mAs: mAs_efektif = (mA * t_rot) / Pitch\n" +
                "6. Indeks Dosis CT: CTDIvol = CTDIw / Pitch (mGy) | DLP = CTDIvol * Panjang Scan L (mGy.cm) | Dosis Efektif E = k * DLP (mSv)",
            category = "Formula",
            dateString = "Hari ini"
        ),
        StudyNote(
            id = "note_2",
            title = "Protokol Operasional Pagi & Checklist Konsol CT",
            content = "- Sebelum pasien pertama: Jalankan Tube Warm-Up hingga OLP mencapai ~15-25%, kosongkan bore gantry, lalu jalankan Fast Air Calibration.\n" +
                "- Posisi Pasien: Atur ketinggian meja menggunakan Laser Aksial & Koronal agar pusat anatomi berada tepat di Isocenter (mencegah distorsi Bow-Tie Filter dan kesalahan mA pada AEC).\n" +
                "- Preset Windowing: Brain (WW 80, WL 40), Stroke (WW 40, WL 40), Mediastinum/Abdomen (WW 350, WL 40), Lung (WW 1500, WL -600), Bone (WW 2000, WL 500).",
            category = "Protokol",
            dateString = "Hari ini"
        ),
        StudyNote(
            id = "note_3",
            title = "Protokol Injeksi Kontras Iodin IV & Bolus Tracking",
            content = "- Skrining Keamanan: Periksa kreatinin serum & eGFR (waspada CI-AKI bila eGFR < 30 mL/menit/1,73 m^2), riwayat alergi kontras, hipertiroidisme, dan tunda Metformin 48 jam bila fungsi ginjal menurun.\n" +
                "- Dual-Head Injector: Gunakan IV line 18G-20G di fossa antekubiti, uji patensi NaCl 0,9%, batas tekanan <= 300-325 PSI, selalu ikuti kontras dengan Saline Chaser 30-50 mL.\n" +
                "- Bolus Tracking: CTA Pulmonal (ROI di Trunkus Pulmonalis, trigger +100 s.d. +120 HU); CTA Aorta/Koroner (ROI di Aorta Ascendens/Descendens, trigger +120 s.d. +150 HU); Fase Vena Porta Hepar (60-75 detik).",
            category = "Klinis",
            dateString = "Hari ini"
        ),
        StudyNote(
            id = "note_4",
            title = "Panduan Cepat Troubleshooting Artefak & QA Fantom Air",
            content = "- Ring Artifact (lingkaran konsentris di pusat): Detektor Gen-3 drift/out-of-calibration -> Kosongkan bore & jalankan Air Calibration.\n" +
                "- Beam Hardening (cupping / pita gelap fossa posterior): Naikkan kVp, pastikan posisi di tengah Bow-Tie Filter, gunakan irisan tipis & koreksi iteratif.\n" +
                "- Metal Streak Artifact: Gunakan kVp tinggi (120-140 kV), mAs memadai, irisan tipis, dan aktifkan algoritma MAR (Metal Artifact Reduction).\n" +
                "- Uji QA Fantom Air Harian (5 ROI): Akurasi HU pusat = 0 +/- 4 HU; Keseragaman (Uniformity tepi vs pusat) <= 5 HU.",
            category = "Artefak",
            dateString = "Hari ini"
        )
    )

    // Mapping helpers for Room Seed
    fun toMriModuleEntities(): List<MriModuleEntity> = modules.mapIndexed { idx, mod ->
        MriModuleEntity(
            id = mod.id,
            code = mod.code,
            title = mod.title,
            subtitle = mod.subtitle,
            description = mod.description,
            levelBadge = mod.levelBadge,
            iconName = mod.iconName,
            sortOrder = if (mod.sortOrder != 0) mod.sortOrder else idx + 1
        )
    }

    fun toCurriculumNodeEntities(): List<CurriculumNodeEntity> {
        val list = mutableListOf<CurriculumNodeEntity>()
        modules.forEach { mod ->
            mod.nodes.forEachIndexed { index, node ->
                list.add(
                    CurriculumNodeEntity(
                        code = node.code,
                        moduleId = mod.id,
                        level = node.level,
                        title = node.title,
                        summaryShort = node.summaryShort,
                        detailFull = node.detailFull,
                        analogi = node.analogi,
                        prerequisite = node.prerequisite,
                        learningGoal = node.learningGoal,
                        crossLinks = node.crossLinks,
                        labType = node.labType,
                        sortOrder = if (node.sortOrder != 0) node.sortOrder else index + 1
                    )
                )
            }
        }
        return list
    }

    fun toCurriculumQuizEntities(): List<CurriculumQuizEntity> = quizzes.map { q ->
        CurriculumQuizEntity(
            id = q.id,
            moduleId = q.moduleId,
            nodeCode = q.nodeCode,
            level = q.level,
            question = q.question,
            optionsJson = encodeStringListJson(q.options),
            correctIndex = q.correctIndex,
            explanation = q.explanation
        )
    }
}
