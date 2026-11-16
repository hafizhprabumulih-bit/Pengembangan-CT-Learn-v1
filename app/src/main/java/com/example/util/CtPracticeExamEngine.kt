package com.example.util

import com.example.data.CtQuizQuestion
import kotlin.random.Random

enum class CtExamDomain(val shortLabel: String, val fullLabel: String) {
    FISIKA_ATENUASI("Fisika X-Ray", "Fisika Sinar-X & Atenuasi"),
    GANTRY_INSTRUMENTASI("Instrumentasi", "Anatomi Gantry, Tabung & Detektor"),
    MSCT_REKONSTRUKSI("MSCT & Rekon", "Geometri MSCT, Pitch & Rekonstruksi"),
    HU_DOSIMETRI("HU & Dosimetri", "Skala HU, Windowing & Dosimetri CT"),
    KONTRAS_QA("Kontras & QA", "Kontras Iodin, 3D, Artefak & QA")
}

data class CtExamQuestion(
    val id: String,
    val domain: CtExamDomain,
    val topicTag: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class CtDomainScore(
    val domain: CtExamDomain,
    val total: Int,
    val correct: Int,
    val accuracyPct: Int
)

data class CtExamEvaluation(
    val totalQuestions: Int,
    val answeredCount: Int,
    val correctCount: Int,
    val scorePercent: Int,
    val passed: Boolean,
    val gradeLabel: String,
    val domainBreakdown: List<CtDomainScore>
)

/**
 * Mesin ujian latihan & Try-Out UKOM CT Scan murni (JVM-testable).
 * Menggabungkan bank 25 soal kasus klinis CT lanjutan dengan 50 soal kurikulum inti.
 */
object CtPracticeExamEngine {

    val extendedExamBank: List<CtExamQuestion> = listOf(
        CtExamQuestion(
            id = "EX_CT_01",
            domain = CtExamDomain.FISIKA_ATENUASI,
            topicTag = "K-Edge Iodin & kVp",
            question = "Pada pemeriksaan CT Angiografi pasien berpostur kurus, mengapa penurunan tegangan tabung dari 120 kVp ke 80 kVp meningkatkan penyangatan lumen pembuluh darah?",
            options = [
                "Energi efektif spektrum mendekati K-edge Iodin (33,2 keV) sehingga efek fotolistrik meningkat tajam",
                "Hamburan Compton mendominasi pada 80 kVp sehingga kontras pembuluh meningkat",
                "Anoda Tungsten berhenti memancarkan radiasi karakteristik di bawah 100 kVp",
                "Nilai koefisien atenuasi air meningkat tiga kali lipat pada 80 kVp"
            ],
            correctIndex = 0,
            explanation = "Efek fotolistrik berbanding terbalik dengan energi pangkat tiga (~Z^3/E^3) dan melonjak tepat di atas K-edge Iodin 33,2 keV."
        ),
        CtExamQuestion(
            id = "EX_CT_02",
            domain = CtExamDomain.FISIKA_ATENUASI,
            topicTag = "Beer-Lambert & HVL",
            question = "Diketahui koefisien atenuasi linier bahan fantom homogen adalah 0,231 cm^-1. Berapakah ketebalan Half-Value Layer (HVL) berkas monokromatik pada bahan tersebut?",
            options = [
                "3,0 cm (dihitung dari HVL = 0,693 / 0,231)",
                "0,16 cm (dihitung dari 0,693 x 0,231)",
                "6,0 cm (karena intensitas sisa 25%)",
                "1,5 cm (setengah dari koefisien atenuasi)"
            ],
            correctIndex = 0,
            explanation = "Rumus Half-Value Layer adalah HVL = ln(2) / mu = 0,693 / 0,231 = 3,0 cm."
        ),
        CtExamQuestion(
            id = "EX_CT_03",
            domain = CtExamDomain.FISIKA_ATENUASI,
            topicTag = "Spektrum Tabung",
            question = "Dari total energi kinetik elektron proyektil yang menumbuk target anoda Tungsten pada tabung sinar-X CT, kira-kira berapa persen yang benar-benar berubah menjadi foton sinar-X?",
            options = [
                "Hanya sekitar 1%, sedangkan 99% sisanya berubah menjadi energi panas anoda",
                "Sekitar 50% menjadi sinar-X dan 50% menjadi cahaya tampak",
                "Sekitar 85% menjadi sinar-X diagnostik",
                "Hampir 99% menjadi berkas sinar-X monokromatik"
            ],
            correctIndex = 0,
            explanation = "Efisiensi produksi sinar-X sangat rendah (~1%), itulah sebabnya tabung CT wajib berkapasitas simpan panas jutaan Heat Unit (5-8+ MHU)."
        ),
        CtExamQuestion(
            id = "EX_CT_04",
            domain = CtExamDomain.FISIKA_ATENUASI,
            topicTag = "Filtrasi Bow-Tie",
            question = "Apa konsekuensi langsung jika sebuah pesawat CT tidak dilengkapi dengan filtrasi bawaan Aluminium maupun Bow-Tie Filter?",
            options = [
                "Foton energi rendah (soft X-rays) terserap habis di kulit pasien tanpa mencapai detektor dan memicu cupping berat",
                "Resolusi spasial tulang meningkat dua kali lipat secara otomatis",
                "Waktu rotasi gantry menjadi 0,1 detik per putaran",
                "Detektor Solid-State menghasilkan lebih sedikit arus bocor"
            ],
            correctIndex = 0,
            explanation = "Pre-filtrasi membuang foton lunak berdaya tembus rendah yang hanya menambah dosis kulit pasien dan memperparah beam hardening."
        ),
        CtExamQuestion(
            id = "EX_CT_05",
            domain = CtExamDomain.FISIKA_ATENUASI,
            topicTag = "Logaritma DAS",
            question = "Mengapa Data Acquisition System (DAS) menerapkan operasi logaritma natural ln(I0/I) sebelum data dikirim ke komputer rekonstruksi?",
            options = [
                "Untuk melinierkan hubungan eksponensial Beer-Lambert menjadi penjumlahan sederhana nilai mu sepanjang lintasan sinar",
                "Untuk mengubah nilai Hounsfield Unit menjadi satuan Sievert",
                "Untuk memutar gantry 180 derajat lebih cepat",
                "Untuk menghapus seluruh data dari baris detektor tengah"
            ],
            correctIndex = 0,
            explanation = "Karena I = I0 * exp(-sum(mu_i * dx)), maka ln(I0/I) = sum(mu_i * dx) membentuk persamaan linier yang bisa direkonstruksi."
        ),
        CtExamQuestion(
            id = "EX_CT_06",
            domain = CtExamDomain.GANTRY_INSTRUMENTASI,
            topicTag = "Off-Centering Laser",
            question = "Pada pemeriksaan CT Toraks dengan modulasi mA otomatis (AEC), meja pasien tidak sengaja dinaikkan 4 cm terlalu tinggi dari garis Laser Isocenter. Apa akibatnya terhadap citra dan dosis?",
            options = [
                "Magnifikasi topogram salah baca sehingga AEC menaikkan dosis radiasi berlebih dan bagian tebal keluar dari cekungan Bow-Tie Filter",
                "Dosis radiasi menjadi nol karena sinar-X meleset dari pasien",
                "Nilai HU udara di dalam trakea berubah menjadi +500 HU",
                "Tabung berhenti berputar dan beralih ke mode EBCT"
            ],
            correctIndex = 0,
            explanation = "Off-centering menyebabkan bagian tubuh makin mendekati tabung (tampak lebih lebar di Scout AP) sehingga AEC mengira pasien lebih besar dan memberi dosis ekstra."
        ),
        CtExamQuestion(
            id = "EX_CT_07",
            domain = CtExamDomain.GANTRY_INSTRUMENTASI,
            topicTag = "Photon-Counting CT",
            question = "Material semikonduktor apa yang digunakan sebagai konverter langsung foton sinar-X menjadi pulsa muatan listrik pada teknologi Photon-Counting Detector CT (PCD)?",
            options = [
                "Cadmium Telluride (CdTe) atau Cadmium Zinc Telluride (CZT)",
                "Gas Xenon bertekanan 25 atmosfer",
                "Kalsium Tungstat berlapis emulsi perak halida",
                "Baja tahan karat berlapis Aluminium"
            ],
            correctIndex = 0,
            explanation = "Semikonduktor CdTe/CZT langsung mengonversi tiap foton menjadi pasangan elektron-hole tanpa tahap cahaya tampak dan memisahkan level energinya ke energy bins."
        ),
        CtExamQuestion(
            id = "EX_CT_08",
            domain = CtExamDomain.GANTRY_INSTRUMENTASI,
            topicTag = "Dual-Source CT",
            question = "Pesawat Dual-Source CT memasang dua tabung sinar-X dan dua busur detektor pada sudut ortogonal (~90 derajat). Manfaat utama arsitektur ini pada CT Jantung adalah:",
            options = [
                "Memangkas waktu akuisisi data proyeksi 180 derajat menjadi seperempat rotasi (~66 ms) sehingga denyut jantung tinggi tetap tajam",
                "Memungkinkan scan tanpa memerlukan listrik dan generator tegangan tinggi",
                "Mengurangi lebar Scan FOV menjadi 5 cm saja",
                "Menghilangkan perlunya media kontras iodin pada angiografi koroner"
            ],
            correctIndex = 0,
            explanation = "Dua sistem yang terpisah ~90 derajat mengumpulkan data setengah lingkaran (180 derajat + fan) dalam ~90 derajat rotasi mekanik, menggandakan resolusi temporal."
        ),
        CtExamQuestion(
            id = "EX_CT_09",
            domain = CtExamDomain.GANTRY_INSTRUMENTASI,
            topicTag = "Tabung Sinar-X CT",
            question = "Komponen bantalan (bearing) terbaru pada tabung CT berdaya tinggi memakai logam cair Gallium-Indium-Tin (Liquid Metal Bearing) menggantikan ball bearing. Mengapa?",
            options = [
                "Bebas keausan gesek mekanis, hampir senyap pada RPM tinggi, dan menyalurkan panas anoda langsung ke sistem pendingin oli",
                "Mengubah berkas sinar-X polikromatik menjadi sinar laser optik",
                "Meningkatkan massa anoda menjadi 100 kg agar tidak ikut berputar",
                "Mempercepat laju ekstravasasi media kontras pada power injector"
            ],
            correctIndex = 0,
            explanation = "Pelumas logam cair mengalirkan panas konduktif secara terus-menerus serta meniadakan suara berisik dan keausan bola bantalan pada gaya sentrifugal tinggi."
        ),
        CtExamQuestion(
            id = "EX_CT_10",
            domain = CtExamDomain.GANTRY_INSTRUMENTASI,
            topicTag = "z-Flying Focal Spot",
            question = "Bagaimana teknologi z-Flying Focal Spot (z-FFS) meningkatkan resolusi sumbu-z dan menekan artefak kincir angin (windmill) pada MSCT?",
            options = [
                "Berkas elektron dibelokkan secara elektromagnetik di anoda sehingga menghasilkan dua proyeksi irisan bergeser sepanjang sumbu-z per rotasi",
                "Meja pasien bergerak maju-mundur secara zig-zag saat rotasi",
                "Detektor Solid-State dicopot setengahnya setiap setengah detik",
                "Filter Kernel diubah ke Soft Kernel setiap rotasi genap"
            ],
            correctIndex = 0,
            explanation = "Defleksi elektromagnetik berkas elektron memindahkan titik fokus di anoda ke dua posisi z bergantian cepat, menggandakan kerapatan sampel z-axis."
        ),
        CtExamQuestion(
            id = "EX_CT_11",
            domain = CtExamDomain.MSCT_REKONSTRUKSI,
            topicTag = "Over-Ranging",
            question = "Apakah yang dimaksud dengan fenomena Over-Ranging (Z-Overscanning) pada akuisisi helikal MSCT dan bagaimana cara menekannya?",
            options = [
                "Rotasi ekstra di awal dan akhir batas scan untuk kebutuhan interpolasi helikal; ditekan dengan kolimator dinamis Adaptive Dose Shield",
                "Kelebihan arus mA akibat pasien menggerakkan lengan; ditekan dengan menghentikan napas",
                "Pemotongan tepi tubuh karena SFOV terlalu kecil; ditekan dengan Extended FOV",
                "Pengulangan eksposi karena tabung belum di-warm up; ditekan dengan Air Calibration"
            ],
            correctIndex = 0,
            explanation = "Interpolasi helikal memerlukan data proyeksi di luar irisan batas awal/akhir; Adaptive Dose Shield memblokir bagian berkas tak terpakai tersebut."
        ),
        CtExamQuestion(
            id = "EX_CT_12",
            domain = CtExamDomain.MSCT_REKONSTRUKSI,
            topicTag = "Perhitungan Beam Pitch",
            question = "Pesawat MSCT 64-slice menggunakan kolimasi detektor 64 x 0,625 mm. Bila meja pasien bergeser sejauh 48 mm setiap satu kali rotasi gantry, berapakah nilai Beam Pitch-nya?",
            options = [
                "1,20 (dihitung dari 48 mm / 40 mm)",
                "0,83 (dihitung dari 40 mm / 48 mm)",
                "76,8 (dihitung dari 48 mm / 0,625 mm)",
                "0,75 (karena jumlah baris 64)"
            ],
            correctIndex = 0,
            explanation = "Lebar kolimasi berkas total W = N x T = 64 x 0,625 mm = 40 mm. Pitch = d / W = 48 mm / 40 mm = 1,20."
        ),
        CtExamQuestion(
            id = "EX_CT_13",
            domain = CtExamDomain.MSCT_REKONSTRUKSI,
            topicTag = "Calcium Scoring (Agatston)",
            question = "Pada pemeriksaan CT Cardiac Calcium Scoring (Skor Agatston) menggunakan Prospective ECG-Gating tanpa kontras, pada nilai ambang HU berapakah plak kalsifikasi koroner mulai dihitung oleh komputer?",
            options = [
                ">= +130 HU dengan luas area minimal tertentu pada irisan 3 mm",
                ">= -100 HU setara lemak epikardial",
                "0 s.d. +20 HU setara cairan perikardium",
                ">= +1500 HU saja"
            ],
            correctIndex = 0,
            explanation = "Protokol standar Skor Agatston mendefinisikan kalsium koroner sebagai piksel ber-atenuasi >= +130 HU di sepanjang jalur arteri koroner pada 120 kVp."
        ),
        CtExamQuestion(
            id = "EX_CT_14",
            domain = CtExamDomain.MSCT_REKONSTRUKSI,
            topicTag = "Retro-Recon & Raw Data",
            question = "Satu seri CT Toraks ter rekonstruksi memakai Soft Kernel 5 mm. Dokter paru meminta irisan tipis 1 mm Sharp Lung Kernel untuk mengevaluasi bronkiektasis tanpa memindai ulang pasien. Syarat mutlaknya adalah:",
            options = [
                "Raw Data (Sinogram) pasien tersebut masih tersimpan di penyimpanan konsol akuisisi",
                "Window Level monitor PACS dinaikkan menjadi +600 HU",
                "Pasien masuk kembali ke bore untuk topogram kedua",
                "Menjalankan Air Calibration terlebih dahulu"
            ],
            correctIndex = 0,
            explanation = "Retrospective Reconstruction memungkinkan pemilihan ulang DFOV, ketebalan irisan, dan Convolution Kernel selama Raw Data proyeksi belum dihapus."
        ),
        CtExamQuestion(
            id = "EX_CT_15",
            domain = CtExamDomain.MSCT_REKONSTRUKSI,
            topicTag = "AI DLIR vs FBP",
            question = "Mengapa banyak radiolog mengeluhkan tampilan citra 'bertekstur lilin/plastik' pada Iterative Reconstruction (IR) tingkat agresif, dan bagaimana Deep Learning Reconstruction (DLIR) mengatasinya?",
            options = [
                "IR kuat menggeser spektrum frekuensi noise ke frekuensi rendah (blotchy), sedangkan DLIR dilatih langsung pada citra dosis standar untuk menjaga tekstur alami sambil membuang noise",
                "DLIR memakai Simple Back-Projection agar bintik noise bertambah banyak",
                "IR meningkatkan dosis radiasi menjadi empat kali lipat FBP",
                "DLIR menghilangkan skala Hounsfield Unit dari gambar DICOM"
            ],
            correctIndex = 0,
            explanation = "DLIR mengekstrak noise statistik tanpa merusak spektrum frekuensi tekstur jaringan (noise power spectrum), menghasilkan citra halus namun tetap natural."
        ),
        CtExamQuestion(
            id = "EX_CT_16",
            domain = CtExamDomain.HU_DOSIMETRI,
            topicTag = "Ukuran Piksel & DFOV",
            question = "Jika matriks rekonstruksi standar adalah 512 x 512 piksel dan radiografer mengecilkan Display FOV (DFOV) dari 400 mm menjadi 200 mm pada Raw Data, bagaimana perubahan ukuran sisi piksel dan resolusi spasial in-plane?",
            options = [
                "Ukuran sisi piksel mengecil setengahnya (0,78 mm -> 0,39 mm) sehingga resolusi spasial in-plane meningkat nyata",
                "Ukuran piksel membesar dua kali lipat dan gambar menjadi buram",
                "Dosis radiasi CTDIvol pasien berlipat empat kali",
                "Ketebalan irisan sumbu-z berubah menjadi 20 mm"
            ],
            correctIndex = 0,
            explanation = "Ukuran piksel = DFOV / Matriks. DFOV lebih kecil memetakan lebih banyak piksel per sentimeter anatomi (tanpa menambah dosis radiasi)."
        ),
        CtExamQuestion(
            id = "EX_CT_17",
            domain = CtExamDomain.HU_DOSIMETRI,
            topicTag = "SSDE (Size-Specific Dose Estimate)",
            question = "Mengapa angka CTDIvol fantom tubuh 32 cm pada pasien anak kecil atau kurus seringkali meremehkan dosis organ aktual yang diterima pasien tersebut?",
            options = [
                "Karena tubuh berdiameter kecil mengalami pelemahan sendiri lebih sedikit sehingga dosis serap aktual lebih tinggi (dikoreksi oleh SSDE = f_size x CTDIvol)",
                "Karena fantom 32 cm terbuat dari timbal padat murni",
                "Karena nilai DLP harus dibagi dengan umur pasien",
                "Karena tabung CT memancarkan elektron ke arah berlawanan pada anak"
            ],
            correctIndex = 0,
            explanation = "Faktor koreksi f_size AAPM bernilai > 1 untuk diameter tubuh yang lebih kecil dari fantom standar 32 cm, sehingga SSDE lebih tinggi daripada CTDIvol konsol."
        ),
        CtExamQuestion(
            id = "EX_CT_18",
            domain = CtExamDomain.HU_DOSIMETRI,
            topicTag = "Perhitungan DLP & Dosis Efektif",
            question = "Pada pemeriksaan CT Toraks Dewasa (k = 0,014 mSv/(mGy.cm)), konsol mencatat CTDIvol = 8 mGy dan panjang scan L = 30 cm. Berapakah estimasi Dosis Efektif (E) pasien?",
            options = [
                "3,36 mSv (DLP = 240 mGy.cm, lalu E = 240 x 0,014)",
                "0,11 mSv (karena CTDIvol x k saja)",
                "24,0 mSv (karena L dikalikan kVp)",
                "5,60 mSv (karena memakai faktor kepala 0,0021)"
            ],
            correctIndex = 0,
            explanation = "DLP = CTDIvol x L = 8 x 30 = 240 mGy.cm. Dosis Efektif E = DLP x faktor konversi toraks (0,014) = 3,36 mSv."
        ),
        CtExamQuestion(
            id = "EX_CT_19",
            domain = CtExamDomain.HU_DOSIMETRI,
            topicTag = "Extended HU Scale",
            question = "Mengapa pesawat CT modern untuk radioterapi dan ortopedi menyediakan Extended Hounsfield Unit Scale (16-bit hingga +30.000 HU)?",
            options = [
                "Agar implan logam padat (amalgam gigi, titanium, baja bedah) yang melebihi batas +3071 HU skala 12-bit tidak terpotong nilainya (clipping)",
                "Agar jaringan paru dapat bernilai lebih negatif dari -10.000 HU",
                "Agar ukuran matriks berubah menjadi 16 x 16 piksel",
                "Agar air murni terbaca +3000 HU"
            ],
            correctIndex = 0,
            explanation = "Skala 12-bit standar jenuh di +3071 HU sehingga memicu salah estimasi elektron density pada perencanaan radioterapi maupun artefak logam ekstrem."
        ),
        CtExamQuestion(
            id = "EX_CT_20",
            domain = CtExamDomain.HU_DOSIMETRI,
            topicTag = "Windowing Klinis",
            question = "Pasien trauma kepala memiliki garis fraktur tipis pada kalvaria sekaligus perdarahan subdural tipis menempel di tabula interna. Pengaturan Windowing apa yang harus dinilai berurutan?",
            options = [
                "Evaluasi keduanya terpisah: Subdural Blood/Brain Window (WW ~80-150, WL 40-60) untuk perdarahan dan Bone Window (WW 2000, WL 500) untuk fraktur",
                "Cukup memakai Lung Window (WW 1500, WL -600) untuk keduanya",
                "Gunakan Stroke Window (WW 40, WL 40) untuk menilai korteks tulang kalvaria",
                "Setel WW = 10 dan WL = +1500 HU"
            ],
            correctIndex = 0,
            explanation = "Perdarahan subdural (+70 HU) dapat menyatu dengan putihnya tulang (+1400 HU) pada Brain Window sempit; evaluasi terpisah dengan Subdural & Bone Window wajib dilakukan."
        ),
        CtExamQuestion(
            id = "EX_CT_21",
            domain = CtExamDomain.KONTRAS_QA,
            topicTag = "CT Perfusion Stroke",
            question = "Dalam peta CT Perfusion otak pada stroke iskemik akut, pola manakah yang menandakan jaringan PENUMBRA (masih dapat diselamatkan dengan reperfusi/trombektomi)?",
            options = [
                "CBF menurun dan MTT/Tmax memanjang, namun CBV tetap normal atau sedikit meningkat (mismatch Core vs Penumbra)",
                "CBF, CBV, dan MTT semuanya sama-sama meningkat dua kali lipat",
                "CBV turun drastis mendekati nol bersama CBF (menandakan Core infark mati)",
                "Nilai HU cairan ventrikel naik menjadi +400 HU"
            ],
            correctIndex = 0,
            explanation = "Pada jaringan penumbra, mekanisme autoregulasi dilatasi pembuluh mempertahankan Cerebral Blood Volume (CBV) tetap cukup walau aliran CBF melambat dan MTT/Tmax memanjang."
        ),
        CtExamQuestion(
            id = "EX_CT_22",
            domain = CtExamDomain.KONTRAS_QA,
            topicTag = "Bolus Tracking vs Test Bolus",
            question = "Jika pada pemeriksaan CTA Pulmonalis untuk mencari emboli paru ROI Bolus Tracking diletakkan di Arteri Pulmonalis Utama (Main Pulmonary Artery), berapa ambang trigger HU yang umum digunakan?",
            options = [
                "Sekitar +100 hingga +150 HU di lumen Arteri Pulmonalis agar tepat sebelum kontras membanjiri Vena Pulmonalis & Aorta",
                "Ambang -500 HU di dalam parenkim paru kiri",
                "Waktu tunggu fixed delay 120 detik setelah injeksi selesai",
                "Ambang +1400 HU pada korpus vertebra torakalis"
            ],
            correctIndex = 0,
            explanation = "Penempatan ROI di Truncus Pulmonalis pada ambang +100..+150 HU memicu scan segera ketika bolus Iodin pekat memenuhi cabang arteri pulmonalis."
        ),
        CtExamQuestion(
            id = "EX_CT_23",
            domain = CtExamDomain.KONTRAS_QA,
            topicTag = "Keamanan Injektor & Ekstravasasi",
            question = "Pada CTA dengan flow rate 5,0 mL/detik melalui kanul 18G, apakah fungsi utama penyuntikan 40 mL Saline Chaser (NaCl 0,9%) langsung setelah media kontras?",
            options = [
                "Mendorong kolom kontras sisa dari selang & vena brakialis ke jantung serta mengurangi streak artifact dari kontras pekat di Vena Kava Superior",
                "Menetralkan kandungan Iodin agar tidak terlihat pada citra CTA",
                "Menghentikan pernapasan pasien secara otomatis selama 30 detik",
                "Menurunkan tegangan kVp tabung menjadi 50 kVp"
            ],
            correctIndex = 0,
            explanation = "Saline Chaser memadatkan panjang efektif bolus kontras di arteri target sekaligus membersihkan kontras statis di vena subklavia/SVC penyebab streak."
        ),
        CtExamQuestion(
            id = "EX_CT_24",
            domain = CtExamDomain.KONTRAS_QA,
            topicTag = "Artefak & Ring Correction",
            question = "Ketika melakukan evaluasi QA pagi menggunakan Fantom Air Silinder dengan 5 ROI (Pusat + Jam 12, 3, 6, 9), didapatkan hasil: Pusat = +0,5 HU, Tepian = +1,2 / -0,8 / +0,9 / -0,3 HU, tanpa Ring Artifact. Kesimpulannya adalah:",
            options = [
                "LULUS uji QA harian (akurasi air pusat 0 +- 4 HU terpenuhi & selisih keseragaman maks 1,3 HU <= 5 HU)",
                "GAGAL uji Uniformity karena nilai ROI tepi tidak sama persis dengan 0,0 HU",
                "Wajib mengganti tabung sinar-X karena air bernilai positif",
                "Perlu memindai pasien tanpa Bow-Tie Filter"
            ],
            correctIndex = 0,
            explanation = "Akurasi +0,5 HU berada jauh di dalam toleransi +-4 HU dan variasi maksimum |(-0,8) - (+0,5)| = 1,3 HU aman di bawah batas Uniformity <= 5 HU."
        ),
        CtExamQuestion(
            id = "EX_CT_25",
            domain = CtExamDomain.KONTRAS_QA,
            topicTag = "Prosedur Pagi CT Scan",
            question = "Mengapa prosedur Fast Air Calibration wajib dijalankan dalam kondisi bore gantry dan meja benar-benar kosong tanpa bantal atau kepala fantom di celah berkas?",
            options = [
                "Karena seluruh ribuan kanal detektor harus membaca transmisi udara bebas murni untuk memperbarui tabel kalibrasi Gain dan Offset agar seragam 100%",
                "Agar bantal busa tidak ikut terkena gaya tarik medan magnet 3 Tesla",
                "Supaya komputer IRS memindai meja karbon sebagai referensi tulang",
                "Agar suhu anoda turun kembali ke suhu ruang"
            ],
            correctIndex = 0,
            explanation = "Jika ada objek penghalang saat Air Calibration, koreksi gain kanal detektor akan terdistorsi oleh bayangan objek tersebut dan menciptakan cincin/bayangan di tiap scan klinis."
        )
    )

    fun domainForModule(moduleIdOrNode: String): CtExamDomain {
        val norm = moduleIdOrNode.trim().uppercase()
        return when {
            norm.startsWith("M01") -> CtExamDomain.FISIKA_ATENUASI
            norm.startsWith("M02") || norm.startsWith("M03") -> CtExamDomain.GANTRY_INSTRUMENTASI
            norm.startsWith("M04") || norm.startsWith("M05") -> CtExamDomain.MSCT_REKONSTRUKSI
            norm.startsWith("M06") || norm.startsWith("M07") -> CtExamDomain.HU_DOSIMETRI
            else -> CtExamDomain.KONTRAS_QA
        }
    }

    fun allQuestionPool(curriculumQuizzes: List<CtQuizQuestion>): List<CtExamQuestion> {
        val convertedCurriculum = curriculumQuizzes.map { q ->
            CtExamQuestion(
                id = "CURR_${q.id}",
                domain = domainForModule(q.moduleId.ifBlank { q.nodeCode }),
                topicTag = "${q.nodeCode} · ${q.levelBadge}",
                question = q.question,
                options = q.options,
                correctIndex = q.correctIndex,
                explanation = q.explanation
            )
        }
        return extendedExamBank + convertedCurriculum
    }

    /**
     * Menyusun paket ujian Try-Out berimbang di seluruh 5 domain CT Scan.
     */
    fun buildExamSession(
        questionCount: Int = 20,
        curriculumQuizzes: List<CtQuizQuestion> = emptyList(),
        seed: Int = 0
    ): List<CtExamQuestion> {
        val pool = allQuestionPool(curriculumQuizzes)
        if (pool.isEmpty()) return emptyList()
        val target = questionCount.coerceIn(1, pool.size)
        val rng = Random(seed)

        val grouped = CtExamDomain.entries.associateWith { domain ->
            pool.filter { it.domain == domain }.shuffled(rng).toMutableList()
        }

        val selected = mutableListOf<CtExamQuestion>()
        var madeProgress = true
        while (selected.size < target && madeProgress) {
            madeProgress = false
            for (domain in CtExamDomain.entries) {
                if (selected.size >= target) break
                val bucket = grouped[domain] ?: continue
                if (bucket.isNotEmpty()) {
                    selected += bucket.removeAt(0)
                    madeProgress = true
                }
            }
        }
        return selected.shuffled(rng)
    }

    /**
     * Menghitung akurasi historis per domain dari daftar pasangan `(moduleId, isCorrect)`.
     * Mengembalikan daftar domain terurut dari akurasi terendah agar sesi adaptif fokus ke titik lemah.
     */
    fun rankedDomainsByWeakness(attemptModuleOutcomes: List<Pair<String, Boolean>>): List<CtExamDomain> {
        if (attemptModuleOutcomes.isEmpty()) return CtExamDomain.entries
        val grouped = attemptModuleOutcomes.groupBy { (moduleId, _) -> domainForModule(moduleId) }
        return CtExamDomain.entries.sortedBy { domain ->
            val records = grouped[domain].orEmpty()
            if (records.isEmpty()) 0.5f else records.count { it.second }.toFloat() / records.size.toFloat()
        }
    }

    /**
     * Menyusun paket Try-Out adaptif: ~60% soal berasal dari 2 domain terlemah pengguna,
     * sisanya menjaga cakupan merata pada 3 domain lainnya.
     */
    fun buildAdaptiveWeakestSession(
        questionCount: Int = 20,
        curriculumQuizzes: List<CtQuizQuestion> = emptyList(),
        attemptModuleOutcomes: List<Pair<String, Boolean>> = emptyList(),
        seed: Int = 0
    ): List<CtExamQuestion> {
        val pool = allQuestionPool(curriculumQuizzes)
        if (pool.isEmpty()) return emptyList()
        val target = questionCount.coerceIn(1, pool.size)
        val rng = Random(seed)

        val rankedDomains = rankedDomainsByWeakness(attemptModuleOutcomes)
        val grouped = CtExamDomain.entries.associateWith { domain ->
            pool.filter { it.domain == domain }.shuffled(rng).toMutableList()
        }

        val selected = mutableListOf<CtExamQuestion>()
        val weakQuota = (target * 0.6f).toInt().coerceAtLeast(1)
        val weakestTwo = rankedDomains.take(2)
        var weakIdx = 0
        while (selected.size < weakQuota && weakestTwo.any { (grouped[it]?.isNotEmpty() == true) }) {
            val dom = weakestTwo[weakIdx % weakestTwo.size]
            val bucket = grouped[dom]
            if (!bucket.isNullOrEmpty()) {
                selected += bucket.removeAt(0)
            }
            weakIdx++
        }

        var remIdx = 0
        while (selected.size < target && rankedDomains.any { (grouped[it]?.isNotEmpty() == true) }) {
            val dom = rankedDomains[remIdx % rankedDomains.size]
            val bucket = grouped[dom]
            if (!bucket.isNullOrEmpty()) {
                selected += bucket.removeAt(0)
            }
            remIdx++
        }
        return selected.shuffled(rng)
    }

    fun recommendedDurationSeconds(questionCount: Int): Int =
        (questionCount.coerceAtLeast(1) * 60)

    fun evaluateSession(
        questions: List<CtExamQuestion>,
        selectedAnswers: Map<String, Int>
    ): CtExamEvaluation {
        if (questions.isEmpty()) {
            return CtExamEvaluation(
                totalQuestions = 0,
                answeredCount = 0,
                correctCount = 0,
                scorePercent = 0,
                passed = false,
                gradeLabel = "Belum Ada Soal",
                domainBreakdown = CtExamDomain.entries.map { CtDomainScore(it, 0, 0, 0) }
            )
        }

        val correctCount = questions.count { q -> selectedAnswers[q.id] == q.correctIndex }
        val answeredCount = questions.count { q -> selectedAnswers.containsKey(q.id) }
        val scorePct = ((correctCount.toFloat() / questions.size) * 100f).toInt().coerceIn(0, 100)
        val passed = scorePct >= 70
        val grade = when {
            scorePct >= 90 -> "Sangat Kompeten (Grade A)"
            scorePct >= 80 -> "Kompeten Baik (Grade B)"
            scorePct >= 70 -> "Lulus Kompetensi Dasar (Grade C)"
            else -> "Perlu Pendalaman Ulang"
        }

        val breakdown = CtExamDomain.entries.map { domain ->
            val domainQuestions = questions.filter { it.domain == domain }
            val domainCorrect = domainQuestions.count { q -> selectedAnswers[q.id] == q.correctIndex }
            val pct = if (domainQuestions.isEmpty()) 0 else ((domainCorrect.toFloat() / domainQuestions.size) * 100f).toInt()
            CtDomainScore(
                domain = domain,
                total = domainQuestions.size,
                correct = domainCorrect,
                accuracyPct = pct
            )
        }

        return CtExamEvaluation(
            totalQuestions = questions.size,
            answeredCount = answeredCount,
            correctCount = correctCount,
            scorePercent = scorePct,
            passed = passed,
            gradeLabel = grade,
            domainBreakdown = breakdown
        )
    }
}
