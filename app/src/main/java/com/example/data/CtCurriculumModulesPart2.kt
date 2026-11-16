package com.example.data

internal object CtCurriculumModulesPart2 {
    val modulesM06ToM09: List<MriModule> = listOf(
        // ==========================================
        // MODUL 6 (M06): Matriks, Voxel Isotropik, Hounsfield Unit (HU) & Windowing
        // ==========================================
        MriModule(
            id = "M06",
            code = "M06",
            title = "Matriks, Voxel Isotropik, Hounsfield Unit (HU) & Windowing",
            subtitle = "Skala kuantitatif Hounsfield Unit, ukuran piksel/voxel isotropik, dan teknik pengaturan Window Width/Level.",
            description = "Kuantifikasi citra CT digital: hubungan SFOV, DFOV, dan ukuran matriks terhadap ukuran piksel, konsep voxel isotropik untuk reformasi 3D, rumus skala Hounsfield Unit (HU), nilai atenuasi jaringan normal/patologis, pengaturan Window Width (WW) & Window Level (WL), serta Partial Volume Averaging.",
            levelBadge = "Tingkat 1–3 • Kuantifikasi HU & Windowing",
            iconName = "grid",
            sortOrder = 6,
            prerequisites = listOf("M05"),
            estimatedMinutes = 40,
            totalQuizzes = 5,
            badgeName = "Ahli Kuantifikasi HU",
            unlockMessage = "Kamu telah menguasai perhitungan skala Hounsfield Unit, voxel isotropik, serta optimasi Window Width dan Window Level!",
            flashcards = listOf(
                MriFlashcard("M06_F1", "Rumus Hounsfield Unit (HU)", "HU = 1000 x (mu_jaringan - mu_air) / mu_air; Air murni = 0 HU, Udara = -1000 HU."),
                MriFlashcard("M06_F2", "Ukuran Piksel (Pixel Size)", "Pixel Size = DFOV / Matrix Size (umumnya matriks 512 x 512)."),
                MriFlashcard("M06_F3", "Voxel Isotropik", "Elemen volume 3D berbentuk kubus sempurna (dx = dy = dz, misalnya 0,625 x 0,625 x 0,625 mm) untuk reformasi MPR bebas distorsi."),
                MriFlashcard("M06_F4", "Window Width (WW) vs Window Level (WL)", "WW mengatur rentang kontras (sempit = kontras tinggi, lebar = kontras luas); WL mengatur titik tengah kecerahan sesuai HU organ target.")
            ),
            nodes = listOf(
                MriNode(
                    code = "M06.T0",
                    title = "Tujuan Pembelajaran Matriks, HU & Windowing",
                    level = 0,
                    sortOrder = 1,
                    prerequisite = null,
                    learningGoal = "Menguasai perhitungan ukuran piksel/voxel isotropik, rumus skala Hounsfield Unit, dan pengaturan Window Width/Level klinis.",
                    summaryShort = "Target kompetensi perhitungan ukuran piksel/voxel, skala Hounsfield Unit (HU), dan pengaturan Window Width/Level.",
                    detailFull = "Setelah mempelajari Modul 6, Anda mampu menghitung ukuran piksel dan memahami syarat voxel isotropik pada akuisisi MSCT.\n\n" +
                        "Anda juga akan menguasai rumus kuantitatif Hounsfield Unit (HU), menghafal rentang nilai HU jaringan normal maupun patologis, serta terampil menerapkan preset Window Width (WW) dan Window Level (WL) untuk berbagai indikasi klinis.",
                    analogi = "Mengatur Windowing pada CT Scan seperti menyetel jendela kaca berfilter khusus agar Anda bisa mengamati detail awan tipis (paru) atau detail bebatuan (tulang) dari satu foto berderajat keabuan 4.096 tingkat.",
                    crossLinks = "Berkaitan dengan M05.T1.2 (Matriks Rekonstruksi) dan M08.T3.1 (MPR/MIP 3D).",
                    labType = "ct_m06_hu_windowing"
                ),
                MriNode(
                    code = "M06.T1.1",
                    title = "Field of View (SFOV vs DFOV), Matriks, Piksel & Voxel Isotropik",
                    level = 1,
                    sortOrder = 2,
                    prerequisite = "M06.T0",
                    summaryShort = "Perbedaan Scan FOV dan Display FOV, rumus ukuran piksel (DFOV / Matrix), dan pentingnya voxel isotropik.",
                    detailFull = "Citra digital CT tersusun atas kisi dua dimensi yang disebut matriks (umumnya 512 x 512 piksel, atau 1024 x 1024 pada HRCT resolusi ultra-tinggi). Setiap elemen gambar 2D disebut piksel (pixel), namun karena setiap irisan memiliki ketebalan tertentu (slice thickness / z-axis), setiap piksel sesungguhnya mewakili satu balok volume jaringan 3D yang disebut voxel (volume element).\n\n" +
                        "Terdapat dua jenis Field of View: Scan FOV (SFOV) menentukan diameter area di dalam gantry yang diukur oleh detektor dan dikalibrasi oleh bow-tie filter (misalnya Head 25 cm, Body 50 cm), sedangkan Display FOV (DFOV) menentukan seberapa besar area anatomi yang direkonstruksi ke layar matriks. Ukuran fisik satu piksel ditentukan oleh rasio DFOV terhadap ukuran matriks:\n\n" +
                        "Rumus:\n" +
                        "Pixel Size (mm) = DFOV (mm) / Matrix Size\n\n" +
                        "Sebagai contoh, jika DFOV kepala adalah 256 mm dengan matriks 512 x 512, maka ukuran piksel (dx = dy) adalah 256 / 512 = 0,5 mm. Apabila ketebalan irisan (dz) juga diatur sebesar 0,5 mm, maka terbentuklah Voxel Isotropik (dx = dy = dz) berbentuk kubus sempurna yang memungkinkan rekonstruksi multiplanar (koronal/sagital) memiliki ketajaman identik dengan potongan aksial.",
                    analogi = "Bayangkan menyusun Lego: jika balok Lego berbentuk kubus sama sisi (voxel isotropik), bangunan akan tampak mulus dilihat dari depan, samping, maupun atas. Jika baloknya pipih panjang (anisotropik), tampilan samping akan terlihat bergerigi seperti anak tangga.",
                    crossLinks = "Terhubung dengan M04.T1.2 (Kolimasi Detektor MSCT) dan M08.T3.1 (Reformasi 3D).",
                    labType = "ct_m06_hu_windowing"
                ),
                MriNode(
                    code = "M06.T1.2",
                    title = "Skala Kuantitatif Hounsfield Unit (HU) & Peta Nilai Jaringan",
                    level = 1,
                    sortOrder = 3,
                    prerequisite = "M06.T1.1",
                    summaryShort = "Standarisasi koefisien atenuasi linier menjadi bilangan CT (Hounsfield Unit) relatif terhadap air murni.",
                    detailFull = "Sir Godfrey Hounsfield menciptakan skala kuantitatif terstandarisasi untuk mengekspresikan koefisien atenuasi linier jaringan (mu_jaringan) relatif terhadap koefisien atenuasi air murni (mu_air) pada energi efektif berkas sinar-X tertentu. Nilai ini dikenal sebagai CT Number atau Hounsfield Unit (HU):\n\n" +
                        "Rumus:\n" +
                        "HU = 1000 x (mu_jaringan - mu_air) / mu_air\n\n" +
                        "Berdasarkan definisi di atas, air murni memiliki nilai tepat 0 HU, sedangkan udara (mu mendekati nol) bernilai -1000 HU. Setiap perubahan 1 HU setara dengan perubahan 0,1% koefisien atenuasi linier terhadap air.\n\n" +
                        "Rentang nilai HU standar berbagai jaringan tubuh pada 120 kVp:\n" +
                        "- Udara bebas (sinus/trakea): -1000 HU\n" +
                        "- Parenkim paru normal: -700 s.d. -900 HU\n" +
                        "- Jaringan lemak (adiposa): -50 s.d. -100 HU\n" +
                        "- Air murni / cairan serebrospinal (CSF): 0 s.d. +15 HU\n" +
                        "- Substansia alba (White Matter) otak: +20 s.d. +30 HU\n" +
                        "- Substansia grisea (Gray Matter) otak: +35 s.d. +45 HU\n" +
                        "- Otot & organ parenkim (hepar, ginjal, limpa): +40 s.d. +60 HU\n" +
                        "- Darah beku akut (hematoma intrakranial akut): +60 s.d. +80 HU\n" +
                        "- Tulang trabekular/spongiosa: +150 s.d. +400 HU\n" +
                        "- Tulang kortikal padat: +1000 s.d. +3000 HU",
                    analogi = "Skala Hounsfield Unit mirip dengan skala suhu Celcius yang menjadikan titik beku air sebagai patokan 0 derajat: jaringan yang lebih renggang dari air bernilai negatif (gelap/hipodens), sedangkan jaringan yang lebih padat dari air bernilai positif (terang/hiperdens).",
                    crossLinks = "Kelanjutan dari M01.T2.2 (Koefisien Atenuasi Linier) dan dasar bagi M09.T3.1 (QA Fantom Air).",
                    labType = "ct_m06_hu_windowing"
                ),
                MriNode(
                    code = "M06.T2.1",
                    title = "Prinsip Windowing (WW & WL) & Preset Klinis CT",
                    level = 2,
                    sortOrder = 4,
                    prerequisite = "M06.T1.2",
                    summaryShort = "Pemetaan rentang 4.096 tingkat HU ke layar monitor melalui pengaturan Window Width (WW) dan Window Level (WL).",
                    detailFull = "Data rekonstruksi CT modern disimpan dalam kedalaman 12-bit (4.096 nilai HU, dari -1024 hingga +3071 HU), sementara mata manusia hanya mampu membedakan sekitar 30 hingga 60 gradasi abu-abu sekaligus pada layar monitor. Teknik Windowing memetakan rentang HU spesifik yang relevan secara klinis ke dalam skala putih-ke-hitam penuh pada layar.\n\n" +
                        "Dua parameter utama Windowing adalah:\n" +
                        "1. Window Level (WL) / Window Center: Nilai HU tepat di tengah jendela yang akan ditampilkan sebagai abu-abu sedang (50% brightness). Aturan praktis: pilih WL mendekati nilai HU rata-rata organ target.\n" +
                        "2. Window Width (WW): Lebar rentang HU yang ditampilkan sebagai gradasi abu-abu. Batas atas putih murni adalah (WL + WW/2) dan batas bawah hitam murni adalah (WL - WW/2).\n\n" +
                        "Rumus:\n" +
                        "HU_putih_maks = WL + (WW / 2)\n" +
                        "HU_hitam_min = WL - (WW / 2)\n\n" +
                        "Preset Windowing klinis yang wajib dikuasai radiografer dan klinisi:\n" +
                        "- Brain Rutin (WW 80, WL 40): Rentang 0 s.d. +80 HU; membedakan substansia grisea (+40 HU) dan alba (+25 HU) yang hanya terpaut ~15 HU.\n" +
                        "- Stroke Hiperakut (WW 40, WL 40): Window ekstra sempit (+20 s.d. +60 HU) untuk menajamkan kontras hilangnya diferensiasi kortiko-subkortikal dini.\n" +
                        "- Mediastinum / Abdomen (WW 350–400, WL 40–50): Kontras sedang untuk organ lunak dan pembuluh darah.\n" +
                        "- Lung Window (WW 1500, WL -600): Window lebar berpusat di densitas udara-paru untuk melihat vaskularisasi dan interstisial paru.\n" +
                        "- Bone Window (WW 2000–2500, WL 400–600): Window sangat lebar untuk memisahkan korteks dan trabekula tulang tanpa saturasi putih.",
                    analogi = "Window Width yang sempit (WW 80 pada otak) bagaikan menggunakan kaca pembesar kontras tinggi untuk membedakan dua warna cat krem yang hampir identik, sedangkan Window Width lebar (WW 2000 pada tulang) bagaikan kamera HDR yang mampu menangkap bayangan gelap hingga sorot lampu terang sekaligus.",
                    crossLinks = "Dipadukan dengan pemilihan Kernel pada M05.T3.1 (Soft vs Sharp Kernel).",
                    labType = "ct_m06_hu_windowing"
                ),
                MriNode(
                    code = "M06.T3.1",
                    title = "Partial Volume Effect & Akurasi Pengukuran ROI Kuantitatif",
                    level = 3,
                    sortOrder = 5,
                    prerequisite = "M06.T2.1",
                    summaryShort = "Kesalahan rata-rata nilai HU akibat dua jaringan berbeda di dalam satu voxel dan cara pencegahannya dengan irisan tipis.",
                    detailFull = "Nilai HU pada satu piksel merepresentasikan rata-rata tertimbang koefisien atenuasi seluruh materi yang berada di dalam volume voxel tersebut. Apabila satu voxel yang berukuran cukup tebal (misalnya slice thickness 5 mm) ditempati oleh dua jaringan dengan densitas sangat berbeda—seperti cairan kista ginjal (0 HU) dan parenkim ginjal berpenyengatan kontras (+120 HU)—maka komputer akan menghitung nilai HU rata-rata di antaranya:\n\n" +
                        "Rumus:\n" +
                        "HU_voxel = (f_1 x HU_1) + (f_2 x HU_2)\n\n" +
                        "Fenomena ini disebut Partial Volume Averaging (atau Partial Volume Effect). Akibatnya, kista jinak berukuran kecil dapat tampak seolah-olah memiliki nilai HU solid (+45 HU, pseudo-enhancement) atau tepi tulang basis cranii yang miring tampak kabur menyerupai perdarahan.\n\n" +
                        "Solusi teknis utama untuk mengeliminasi Partial Volume Effect adalah merekonstruksi irisan tipis (thin slice, misalnya 0,625 mm atau 1 mm) dan meletakkan Region of Interest (ROI) tepat di bagian tengah lesi dengan menghindari area perbatasan tepi organ.",
                    analogi = "Jika Anda mencampur setengah gelas air bening (0 HU) dan setengah gelas kopi pekat (+100 HU) di dalam satu gelas besar (voxel tebal), hasil pengukurannya tampak seperti teh cokelat (+50 HU) padahal aslinya tidak ada teh di sana.",
                    crossLinks = "Berkaitan dengan M04.T1.2 (Ketebalan Irisan MSCT) dan M09.T1.1 (Artefak Klinis).",
                    labType = "ct_m06_hu_windowing"
                )
            )
        ),

        // ==========================================
        // MODUL 7 (M07): Parameter Konsol, Kualitas Citra & Dosimetri CT
        // ==========================================
        MriModule(
            id = "M07",
            code = "M07",
            title = "Parameter Konsol, Kualitas Citra & Dosimetri CT (CTDIvol, DLP, AEC)",
            subtitle = "Pengaturan kVp, mA, waktu rotasi, segitiga kualitas citra, indeks dosis CTDIvol/DLP, dan modulasi arus otomatis (AEC).",
            description = "Penguasaan parameter konsol operator CT (kVp, mA, waktu rotasi, mAs efektif), analisis segitiga kualitas citra (Resolusi Spasial MTF, Kontras Rendah CNR, Quantum Noise), perhitungan indeks dosis CTDI100, CTDIw, CTDIvol, DLP, dan Dosis Efektif (mSv), serta cara kerja Automatic Exposure Control (AEC).",
            levelBadge = "Tingkat 1–3 • Kualitas Citra & Dosimetri",
            iconName = "shield",
            sortOrder = 7,
            prerequisites = listOf("M06"),
            estimatedMinutes = 50,
            totalQuizzes = 5,
            badgeName = "Spesialis Dosimetri & Optimasi CT",
            unlockMessage = "Kamu telah menguasai perhitungan mAs efektif, hubungan kuantum noise dengan dosis, serta pembacaan CTDIvol, DLP, dan AEC!",
            flashcards = listOf(
                MriFlashcard("M07_F1", "Effective mAs", "mAs_efektif = (mA x t_rot) / Pitch; menentukan jumlah foton per irisan dan tingkat quantum noise."),
                MriFlashcard("M07_F2", "Hubungan Quantum Noise & mAs", "Noise (sigma) berbanding terbalik dengan akar mAs (sigma ~ 1 / sqrt(mAs)); menurunkan noise 2x lipat butuh mAs 4x lipat."),
                MriFlashcard("M07_F3", "Rumus CTDIvol & DLP", "CTDIvol = CTDIw / Pitch (mGy); DLP = CTDIvol x Panjang Scan L (mGy.cm)."),
                MriFlashcard("M07_F4", "Automatic Exposure Control (AEC)", "Modulasi arus tabung (mA) otomatis sepanjang sumbu z (longitudinal) dan x-y (angular) berdasarkan scout topogram.")
            ),
            nodes = listOf(
                MriNode(
                    code = "M07.T0",
                    title = "Tujuan Pembelajaran Parameter Konsol, Kualitas Citra & Dosimetri CT",
                    level = 0,
                    sortOrder = 1,
                    prerequisite = null,
                    learningGoal = "Menganalisis hubungan parameter eksposi konsol CT terhadap kualitas citra (MTF, CNR, noise) dan metrik dosimetri (CTDIvol, DLP, mSv).",
                    summaryShort = "Target kompetensi pengaturan kVp/mAs, analisis kualitas citra (MTF, CNR, noise), dan perhitungan dosis CTDIvol/DLP.",
                    detailFull = "Setelah menyelesaikan Modul 7, Anda mampu menganalisis dampak perubahan kVp, mA, waktu rotasi (t_rot), dan pitch terhadap kualitas citra maupun dosis radiasi pasien.\n\n" +
                        "Anda juga dapat menghitung mAs efektif, CTDIw, CTDIvol, Dose Length Product (DLP), dan estimasi Dosis Efektif (mSv), serta menerapkan Automatic Exposure Control (AEC) dan prinsip ALARA pada protokol anak maupun dewasa.",
                    analogi = "Mengatur parameter konsol CT bagaikan menyeimbangkan timbangan presisi antara ketajaman diagnostik yang dibutuhkan dokter dan penghematan dosis radiasi yang melindungi pasien.",
                    crossLinks = "Menerapkan konsep tabung pada M03.T2.1 dan mendasari protokol kontras M08.T2.1.",
                    labType = "ct_m07_dosimetri_kualitas"
                ),
                MriNode(
                    code = "M07.T1.1",
                    title = "Parameter Eksposi Konsol: kVp, mA, Waktu Rotasi & Effective mAs",
                    level = 1,
                    sortOrder = 2,
                    prerequisite = "M07.T0",
                    summaryShort = "Peran tegangan puncak (kVp), kuat arus tabung (mA), rotation time, dan konsep mAs efektif pada pemindaian helikal.",
                    detailFull = "Pada konsol operator CT, terdapat empat parameter eksposi primer yang dikendalikan oleh radiografer:\n" +
                        "1. Tegangan Puncak Tabung (kVp: 80, 100, 120, atau 140 kV): Menentukan daya tembus (kualitas/energi rata-rata) berkas sinar-X. Intensitas keluaran sinar-X dan dosis radiasi meningkat secara eksponensial terhadap perubahan kVp (sebanding dengan (kVp)^2 hingga (kVp)^2,5). Menurunkan kVp dari 120 kV ke 80/100 kV pada pasien anak atau protokol CT Angiografi iodin sangat efektif memangkas dosis sekaligus meningkatkan kontras iodin (karena mendekati K-edge iodin 33,2 keV).\n" +
                        "2. Kuat Arus Tabung (mA) dan Waktu Rotasi (t_rot dalam detik): Hasil kali keduanya (mAs = mA x t_rot) menentukan kuantitas foton sinar-X per putaran gantry dan berbanding lurus (linier) dengan dosis radiasi.\n" +
                        "3. Effective mAs (mAs Efektif): Pada pemindaian helikal, jumlah foton yang menembus satu volume irisan jaringan juga dipengaruhi oleh cepat-lambatnya meja bergerak (Pitch). Oleh sebab itu, konsol CT modern mendefinisikan:\n\n" +
                        "Rumus:\n" +
                        "mAs_efektif = (mA x t_rot) / Pitch\n\n" +
                        "Apabila operator menaikkan Pitch pada sistem yang mengunci mAs_efektif konstan, pesawat akan otomatis menaikkan mA secara proporsional agar tingkat noise gambar tetap sama.",
                    analogi = "kVp adalah seberapa kuat lemparan bola menembus dinding (daya tembus), mA adalah jumlah bola yang dilempar per detik, dan Effective mAs adalah jumlah bola yang benar-benar mengenai setiap petak sasaran saat sasaran tersebut berjalan melewati Anda.",
                    crossLinks = "Berkaitan dengan M01.T1.1 (Spektrum Sinar-X) dan M04.T2.2 (Helical Pitch).",
                    labType = "ct_m07_dosimetri_kualitas"
                ),
                MriNode(
                    code = "M07.T1.2",
                    title = "Segitiga Kualitas Citra CT: Spatial Resolution, Low-Contrast & Quantum Noise",
                    level = 1,
                    sortOrder = 3,
                    prerequisite = "M07.T1.1",
                    summaryShort = "Hubungan matematis antara resolusi spasial (MTF), resolusi kontras rendah (CNR), ketebalan irisan, dan quantum mottle.",
                    detailFull = "Kualitas citra CT ditentukan oleh keseimbangan tiga parameter utama:\n" +
                        "1. Resolusi Spasial (High-Contrast Spatial Resolution): Kemampuan memisahkan dua objek kecil berdensitas tinggi yang berdekatan, diukur menggunakan kurva Modulation Transfer Function (MTF, dalam satuan line pairs per centimeter / lp/cm). Resolusi spasial meningkat bila focal spot kecil, elemen detektor sempit, DFOV dikecilkan, matriks diperbesar, slice thickness tipis, dan menggunakan kernel tajam (bone/sharp).\n" +
                        "2. Quantum Noise (Derau Statistik): Fluktuasi acak nilai HU dari piksel ke piksel (diukur sebagai standar deviasi / SD nilai HU pada fantom homogen) akibat terbatasnya jumlah foton sinar-X yang mencapai detektor. Secara matematis, standar deviasi noise berbanding terbalik dengan akar kuadrat jumlah foton (mAs efektif dan ketebalan irisan T):\n\n" +
                        "Rumus:\n" +
                        "Noise (sigma) sebanding dengan 1 / sqrt(mAs_efektif x Slice_Thickness)\n\n" +
                        "Konsekuensi penting: untuk mengurangi noise menjadi setengahnya (50%), dibutuhkan peningkatan mAs (dan dosis radiasi) sebesar 4 kali lipat! Demikian pula, menipiskan irisan dari 5 mm menjadi 1,25 mm (4x lebih tipis) akan meningkatkan noise sebesar 2 kali lipat (akar 4) jika mAs tidak dinaikkan.\n" +
                        "3. Resolusi Kontras Rendah (Low-Contrast Detectability / CNR): Kemampuan membedakan lesi yang hanya berbeda beberapa HU dari jaringan sekitarnya (misalnya tumor hepar atau infark otak dini). CNR sangat dibatasi oleh quantum noise; semakin tinggi noise (butiran kasar/mottle), semakin buruk kemampuan mendeteksi lesi kontras rendah.",
                    analogi = "Memotret di ruangan redup (mAs rendah) menghasilkan foto berbintik-bintik kasar (quantum noise tinggi) sehingga bayangan benda yang warnanya mirip tembok (kontras rendah) menjadi tidak terlihat.",
                    crossLinks = "Berkaitan dengan M05.T3.1 (Pemilihan Kernel) dan M09.T3.1 (Pengukuran SD Noise pada Fantom Air).",
                    labType = "ct_m07_dosimetri_kualitas"
                ),
                MriNode(
                    code = "M07.T2.1",
                    title = "Dosimetri Pesawat CT: CTDI100, CTDIw, CTDIvol, DLP & Dosis Efektif",
                    level = 2,
                    sortOrder = 4,
                    prerequisite = "M07.T1.2",
                    summaryShort = "Perhitungan indeks dosis standar pada fantom akrilik PMMA (16 cm Kepala & 32 cm Tubuh) dari CTDI100 hingga mSv.",
                    detailFull = "Berbeda dengan radiografi konvensional, berkas sinar-X pada CT memutar 360 derajat dan menghasilkan ekor hamburan (scatter tails) ke irisan di sebelahnya. Standar internasional menggunakan fantom silinder akrilik (PMMA) berdiameter 16 cm (untuk Kepala/Pediatrik) dan 32 cm (untuk Tubuh Dewasa) serta bilik ionisasi pensil (pencil ionization chamber) sepanjang 100 mm:\n\n" +
                        "1. CTDI_100 (mGy): Dosis terintegrasi sepanjang 100 mm dari satu rotasi aksial tunggal, diukur pada lubang tengah fantom (CTDI_100,center) dan empat lubang tepi/perifer pada kedalaman 1 cm dari permukaan (CTDI_100,periphery).\n" +
                        "2. CTDI_w (Weighted CTDI, mGy): Rata-rata tertimbang dosis di dalam bidang aksial fantom:\n" +
                        "Rumus:\n" +
                        "CTDI_w = (1/3 x CTDI_100,center) + (2/3 x CTDI_100,periphery)\n\n" +
                        "3. CTDI_vol (Volume CTDI, mGy): Mengoreksi CTDI_w terhadap kerapatan atau renggangnya spiral helikal (Pitch). CTDI_vol merepresentasikan rata-rata dosis terserap per satuan volume di dalam fantom standar:\n" +
                        "Rumus:\n" +
                        "CTDI_vol = CTDI_w / Pitch\n\n" +
                        "4. DLP (Dose Length Product, mGy.cm): Mengalikan intensitas dosis rata-rata (CTDI_vol) dengan total panjang rentang anatomi yang dipindai (Scan Length L dalam cm). Membatasi panjang scan (z-coverage) seperlunya adalah cara paling langsung menurunkan DLP:\n" +
                        "Rumus:\n" +
                        "DLP (mGy.cm) = CTDI_vol (mGy) x Panjang_Scan_L (cm)\n\n" +
                        "5. Estimasi Dosis Efektif (E, dalam mSv): Menghitung risiko biologis stokastik seluruh tubuh dengan mengalikan DLP terhadap faktor konversi anatomis k (mSv / mGy.cm, misalnya Kepala dewasa k ~ 0,0021; Toraks k ~ 0,014; Abdomen-Pelvis k ~ 0,015):\n" +
                        "Rumus:\n" +
                        "Dosis Efektif E (mSv) = k x DLP",
                    analogi = "CTDIvol dapat dianalogikan sebagai curah hujan per meter persegi (intensitas lokal), sedangkan DLP adalah total volume air hujan yang mengguyur seluruh panjang lapangan yang disiram (intensitas dikali luas/panjang area).",
                    crossLinks = "Terhubung langsung dengan M04.T2.2 (Pitch Helikal) dan M02.T3.1 (Rentang Pergerakan Meja z-axis).",
                    labType = "ct_m07_dosimetri_kualitas"
                ),
                MriNode(
                    code = "M07.T3.1",
                    title = "Automatic Exposure Control (AEC), Diagnostic Reference Level (DRL) & Proteksi Pediatrik",
                    level = 3,
                    sortOrder = 5,
                    prerequisite = "M07.T2.1",
                    summaryShort = "Cara kerja modulasi arus tabung 3D (x-y-z), syarat isocentering untuk AEC, dan protokol Image Gently pada anak.",
                    detailFull = "Tubuh manusia tidak memiliki ketebalan dan densitas yang seragam dari bahu hingga panggul maupun dari arah depan-belakang (AP) dibanding samping (Lateral). Sistem Automatic Exposure Control (AEC) mengatur kuat arus tabung (mA) secara dinamis selama pemindaian berlangsung berdasarkan profil atenuasi yang diukur pada citra Scout/Topogram:\n\n" +
                        "1. Modulasi Longitudinal (z-axis modulation): Menurunkan mA saat melintasi rongga paru yang penuh udara (atenuasi rendah) dan menaikkan mA saat melintasi bahu atau panggul yang padat tulang, guna menjaga tingkat quantum noise konstan di seluruh irisan.\n" +
                        "2. Modulasi Angular (x-y modulation): Mengurangi mA saat tabung berada di posisi AP/PA (diameter tubuh lebih tipis) dan meningkatkan mA saat tabung berada di posisi Lateral (diameter bahu/panggul lebih tebal), menghemat dosis hingga 20–40%.\n" +
                        "3. Organ-Based Tube Current Modulation: Menurunkan mA secara spesifik saat tabung berada di sisi anterior tubuh untuk melindungi organ radiosensitif superfisial seperti lensa mata, tiroid, dan payudara.\n\n" +
                        "Peringatan Instrumentasi Kritis: AEC mengasumsikan pasien berada tepat di Isocenter gantry! Jika meja disetel terlalu tinggi (dekat tabung pada proyeksi AP scout), bayangan tubuh pada scout tampak membesar (magnifikasi), sehingga AEC salah mengira pasien bertubuh besar dan menyemburkan mA berlebih (overexposure). Sebaliknya, pada pasien anak (prinsip Image Gently), wajib digunakan protokol kVp rendah (80–100 kVp), kurva AEC pediatrik, dan pembatasan fase scan.",
                    analogi = "AEC bekerja seperti lampu sorot pintar yang otomatis meredup saat menyinari tirai tipis (paru-paru) dan menyala terang saat menembus tirai tebal (panggul) agar kecerahan bayangan di balik tirai selalu seragam.",
                    crossLinks = "Sangat bergantung pada ketepatan Laser Isocenter di M02.T3.1.",
                    labType = "ct_m07_dosimetri_kualitas"
                )
            )
        ),

        // ==========================================
        // MODUL 8 (M08): Injektor Kontras Iodin IV, Bolus Tracking & Post-Processing 3D
        // ==========================================
        MriModule(
            id = "M08",
            code = "M08",
            title = "Injektor Kontras Iodin IV, Bolus Tracking & Post-Processing 3D",
            subtitle = "Media kontras iodin, dual-head power injector, sinkronisasi fase vaskular (Bolus Tracking), serta MPR, MIP, MinIP, dan VRT.",
            description = "Aplikasi klinis CT dengan media kontras Iodin intravaskular (Z = 53, K-edge 33,2 keV), skrining eGFR/Metformin, instrumentasi Dual-Head Power Injector & Saline Chaser, sinkronisasi waktu akuisisi Bolus Tracking (ROI HU Trigger) pada berbagai fase organ, serta rekonstruksi 3D (MPR, MIP, MinIP, VRT).",
            levelBadge = "Tingkat 1–3 • Kontras IV, CTA & 3D",
            iconName = "heart",
            sortOrder = 8,
            prerequisites = listOf("M07"),
            estimatedMinutes = 45,
            totalQuizzes = 5,
            badgeName = "Master Angio & Rekonstruksi 3D",
            unlockMessage = "Kamu telah menguasai protokol kontras Iodin IV, Bolus Tracking ROI HU, dan teknik post-processing 3D (MPR/MIP/MinIP/VRT)!",
            flashcards = listOf(
                MriFlashcard("M08_F1", "Nomor Atom & K-Edge Iodin", "Iodin memiliki Z = 53 dan energi ikat kulit K = 33,2 keV, meningkatkan efek fotolistrik secara drastis pada berkas sinar-X."),
                MriFlashcard("M08_F2", "Saline Chaser (Dual-Head Injector)", "Pendorong NaCl 0,9% setelah kontras untuk memadatkan bolus, menghemat volume kontras, dan mengurangi streak artifact di vena kava superior."),
                MriFlashcard("M08_F3", "Bolus Tracking (SmartPrep / CareBolus)", "Pemantauan nilai HU real-time pada ROI pembuluh darah target; akuisisi otomatis mulai saat ambang batas (misal +100 s.d. +150 HU) tercapai."),
                MriFlashcard("M08_F4", "MIP vs MinIP", "MIP menampilkan nilai HU tertinggi (angiografi/nodul paru); MinIP menampilkan nilai HU terendah (percabangan trakeobronkial/emfisema).")
            ),
            nodes = listOf(
                MriNode(
                    code = "M08.T0",
                    title = "Tujuan Pembelajaran Kontras Iodin, Bolus Tracking & 3D Post-Processing",
                    level = 0,
                    sortOrder = 1,
                    prerequisite = null,
                    learningGoal = "Menguasai protokol keamanan media kontras Iodin, pengoperasian Dual-Head Power Injector, Bolus Tracking, dan teknik post-processing 3D.",
                    summaryShort = "Target kompetensi keamanan media kontras iodin, instrumentasi power injector, fase vaskular, dan pengolahan citra 3D.",
                    detailFull = "Setelah menyelesaikan Modul 8, Anda memahami fisika peningkatan atenuasi oleh media kontras berbahan dasar Iodin, skrining fungsi ginjal (eGFR), dan pencegahan ekstravasasi pada penggunaan Dual-Head Power Injector.\n\n" +
                        "Anda juga akan menguasai teknik sinkronisasi waktu akuisisi menggunakan Bolus Tracking (ROI HU Trigger) untuk berbagai fase klinis (arteri, vena porta, nefrografik, delayed) serta terampil memilih algoritma post-processing 3D (MPR, MIP, MinIP, VRT).",
                    analogi = "Menggunakan Bolus Tracking dan Power Injector ibarat memotret kereta cepat yang melintas di jembatan: sensor otomatis (ROI trigger) memastikan kamera tepat memotret saat lokomotif utama (puncak bolus kontras) berada di tengah jembatan.",
                    crossLinks = "Menerapkan Efek Fotolistrik dari M01.T2.1 dan Voxel Isotropik dari M06.T1.1.",
                    labType = "ct_m08_kontras_3d"
                ),
                MriNode(
                    code = "M08.T1.1",
                    title = "Fisika & Keamanan Klinis Media Kontras Iodin Intravaskular",
                    level = 1,
                    sortOrder = 2,
                    prerequisite = "M08.T0",
                    summaryShort = "Efek fotolistrik Iodin (Z=53, K-edge 33,2 keV), osmolalitas monomer/dimer non-ionik, dan skrining eGFR/Metformin.",
                    detailFull = "Jaringan lunak dan pembuluh darah memiliki koefisien atenuasi yang hampir serupa (+40 hingga +60 HU), sehingga diperlukan media kontras intravaskular berbasis Iodin (Z = 53). Karena probabilitas serapan fotolistrik sebanding dengan Z^3 dan energi ikat elektron kulit K (K-edge) iodin berada pada 33,2 keV—sangat dekat dengan energi rata-rata spektrum sinar-X 80–120 kVp—kehadiran iodin meningkatkan nilai HU darah secara drastis menjadi +250 hingga +500 HU.\n\n" +
                        "Di klinik modern, digunakan media kontras Iodin Non-Ionik Low-Osmolar (LOCM, seperti Iohexol, Iopamidol, Iomeprol) atau Iso-Osmolar (IOCM, Iodixanol ~290 mOsm/kg H2O) yang jauh lebih aman dibandingkan kontras ionik hiperosmolar generasi lama.\n\n" +
                        "Protokol keselamatan pasien pra-injeksi kontras meliputi:\n" +
                        "1. Skrining Fungsi Ginjal: Memeriksa kadar kreatinin serum dan menghitung eGFR untuk mencegah Contrast-Induced Acute Kidney Injury (CI-AKI). Risiko meningkat signifikan bila eGFR < 30 mL/menit/1,73 m^2, sehingga memerlukan pertimbangan hidrasi IV NaCl 0,9% atau alternatif modalitas.\n" +
                        "2. Manajemen Metformin: Pada pasien diabetes dengan gangguan fungsi ginjal (eGFR < 30 atau AKI), pemberian Metformin ditunda selama 48 jam pasca-kontras untuk mencegah komplikasi asidosis laktat bila terjadi penurunan filtrasi ginjal.\n" +
                        "3. Riwayat Alergi & Tiroid: Mengidentifikasi riwayat reaksi hipersensitivitas kontras sebelumnya dan hipertiroidisme aktif (risiko krisis tirotoksikosis Jod-Basedow).",
                    analogi = "Iodin di dalam pembuluh darah bertindak seperti cat neon penyerap sinar-X yang membuat jalan raya pembuluh darah menyala terang di tengah lanskap organ lunak yang semula berwarna abu-abu seragam.",
                    crossLinks = "Berhubungan erat dengan M01.T2.1 (Efek Fotolistrik Z^3) dan M07.T1.1 (Pemilihan 80/100 kVp pada CTA).",
                    labType = "ct_m08_kontras_3d"
                ),
                MriNode(
                    code = "M08.T1.2",
                    title = "Instrumentasi Dual-Head Power Injector, Flow Rate & Saline Chaser",
                    level = 1,
                    sortOrder = 3,
                    prerequisite = "M08.T1.1",
                    summaryShort = "Pengaturan laju alir (mL/detik), batas tekanan (PSI), ukuran kanul IV (18G-20G), dan pencegahan ekstravasasi.",
                    detailFull = "Pemeriksaan CT dinamis dan CT Angiografi (CTA) membutuhkan penyuntikan media kontras dengan laju alir (flow rate) yang presisi dan konsisten menggunakan Dual-Head Automated Power Injector. Alat ini memiliki dua tabung suntik bermotor: Tabung A berisi media kontras Iodin (dilengkapi penghangat/heater pada suhu tubuh 37 derajat C untuk menurunkan viskositas/kekentalan) dan Tabung B berisi larutan Saline (NaCl 0,9%).\n\n" +
                        "Fungsi Saline Chaser (penyuntikan 30–50 mL NaCl 0,9% tepat setelah kontras selesai) adalah:\n" +
                        "- Mendorong sisa kontras yang tertinggal di selang ekstensi dan vena brakialis/subklavia menuju jantung kanan, sehingga bolus kontras menjadi lebih padat (tight bolus) dan volume kontras dapat dihemat.\n" +
                        "- Membilas vena kava superior dan atrium kanan dari kontras yang terlalu pekat pada saat akuisisi CTA Toraks/Koroner, sehingga menghilangkan pervelation/streak artifact yang sering mengaburkan arteri koroner kanan atau trunkus pulmonalis.\n\n" +
                        "Rumus:\n" +
                        "Durasi Injeksi (detik) = Volume Kontras (mL) / Flow Rate (mL/detik)\n\n" +
                        "Keamanan Kanul & Tekanan (Pressure Limit): Untuk CTA dengan flow rate tinggi (4,0–6,0 mL/detik), wajib digunakan IV kateter ukuran besar (18G atau minimal 20G fenestrated/power-injectable) di vena fossa antekubiti, dengan batas tekanan injektor disetel <= 300–325 PSI. Sebelum meninggalkan ruang gantry, radiografer wajib melakukan uji patensi saline sambil meraba lokasi infus untuk mencegah ekstravasasi (bocornya kontras ke jaringan subkutis).",
                    analogi = "Saline chaser bekerja seperti air pendorong di belakang pasta di dalam pipa: seluruh pasta terdorong masuk ke ruang utama secara utuh tanpa ada yang terbuang menempel di dinding pipa pengantar.",
                    crossLinks = "Mencegah streak artifact kontras pekat yang dibahas pada M09.T1.1.",
                    labType = "ct_m08_kontras_3d"
                ),
                MriNode(
                    code = "M08.T2.1",
                    title = "Teknik Sinkronisasi Kontras: Fixed Delay, Test Bolus & Bolus Tracking (ROI Trigger)",
                    level = 2,
                    sortOrder = 4,
                    prerequisite = "M08.T1.2",
                    summaryShort = "Mekanisme pemantauan Time-Attenuation Curve secara real-time dan penentuan waktu tunda fase vaskular/organ.",
                    detailFull = "Waktu tempuh media kontras dari vena lengan menuju aorta atau organ target sangat bervariasi antar-individu karena dipengaruhi oleh curah jantung (cardiac output). Terdapat tiga metode penentuan waktu mulai scan (scan delay):\n" +
                        "1. Empirical Fixed Delay: Menggunakan waktu tunda tetap (misalnya 65–70 detik untuk fase vena porta abdomen rutin). Tidak akurat untuk CT Angiografi karena variasi curah jantung pasien.\n" +
                        "2. Test Bolus: Menyuntikkan bolus kecil uji coba (10–20 mL kontras diikuti saline) sambil melakukan scan dinamis dosis rendah pada level pembuluh target untuk menggambar kurva Time-to-Peak Enhancement sebelum melakukan injeksi utama.\n" +
                        "3. Automated Bolus Tracking (SmartPrep / CareBolus / SureStart): Metode standar emas modern. Operator meletakkan lingkaran Region of Interest (ROI) pada citra potongan tunggal di pembuluh darah target (misalnya Aorta Ascendens untuk CTA Koroner, Aorta Descendens untuk CTA Aorta/Abdomen, atau Trunkus Pulmonalis untuk CTPA Emboli Paru) dan menetapkan nilai ambang pemicu (Trigger Threshold, umumnya +100 hingga +150 HU).\n\n" +
                        "Cara Kerja Bolus Tracking: Beberapa detik setelah injektor mulai menyuntik, pesawat mengambil irisan pemantauan dosis rendah setiap 1–2 detik dan memplot kurva kenaikan HU terhadap waktu. Begitu nilai HU di dalam ROI menembus ambang batas (misal 120 HU), meja otomatis bergerak ke posisi awal scan, memberikan aba-aba tahan napas (post-trigger delay 4–6 detik), dan memulai akuisisi helikal utama tepat di puncak penyengatan arteri.\n\n" +
                        "Fase Klinis Utama Pasca-Injeksi:\n" +
                        "- Fase Arteri Dini (CTA, 15–25 detik / Bolus Tracking): Visualisasi lumen arteri murni sebelum kontras masuk ke parenkim/vena.\n" +
                        "- Fase Arteri Akhir / Late Arterial (35–40 detik): Deteksi lesi hipervaskular hepar (seperti Hepatocellular Carcinoma / HCC) dan pankreas.\n" +
                        "- Fase Vena Porta / Portal Venous (60–75 detik): Penyengatan maksimal parenkim hepar normal dan vena porta; deteksi metastasis hipovaskular.\n" +
                        "- Fase Nefrografik (80–100 detik) & Fase Ekskresi / Delayed (5–15 menit): Evaluasi parenkim ginjal, pencucian adenoma adrenal (washout), kolangiokarsinoma, serta pengisian sistem pelviokalises dan ureter (CT Urografi).",
                    analogi = "Bolus Tracking seperti sensor pintu otomatis yang terus memantau keramaian di lorong masuk; tepat saat rombongan utama tamu (kontras mencapai +120 HU) tiba di depan pintu, kamera langsung memotret secara beruntun.",
                    crossLinks = "Menggunakan pengukuran ROI HU dari M06.T1.2 dan akuisisi helikal cepat dari M04.T2.2.",
                    labType = "ct_m08_kontras_3d"
                ),
                MriNode(
                    code = "M08.T3.1",
                    title = "Teknik Post-Processing 3D: MPR, Curved MPR, MIP, MinIP & Volume Rendering (VRT)",
                    level = 3,
                    sortOrder = 5,
                    prerequisite = "M08.T2.1",
                    summaryShort = "Algoritma proyeksi 2D/3D dari dataset volume voxel isotropik untuk evaluasi vaskular, saluran napas, dan perencanaan bedah.",
                    detailFull = "Dataset volume irisan tipis dengan voxel isotropik (misalnya 0,625 mm) dari akuisisi MSCT dapat diolah menjadi berbagai tampilan diagnostik pada workstation rekonstruksi:\n\n" +
                        "1. Multiplanar Reformation (MPR) & Curved MPR: Menyusun ulang tumpukan voxel aksial menjadi bidang Koronal, Sagital, maupun Oblik tanpa kehilangan resolusi spasial. Curved MPR memungkinkan operator melacak jalur anatomi yang berkelok-kelok (seperti arteri koroner, ureter, atau kanalis mandibularis) dan merentangkannya menjadi satu bidang datar tunggal.\n" +
                        "2. Maximum Intensity Projection (MIP): Algoritma yang menembakkan sinar imajiner melalui slab volume tertentu (misalnya tebal 10–20 mm) dan hanya menampilkan satu nilai voxel dengan HU paling tinggi di sepanjang garis pandang tersebut. Sangat unggul untuk memvisualisasikan pembuluh darah berisikan kontras (CTA intrakranial/renal) dan mendeteksi nodul paru kecil yang padat di antara jaringan paru yang gelap.\n" +
                        "3. Minimum Intensity Projection (MinIP): Kebalikan dari MIP; hanya menampilkan voxel dengan nilai HU paling rendah (paling gelap/udara) di sepanjang slab. Digunakan secara spesifik untuk mengevaluasi percabangan trakeobronkial (airway), emfisema paru, bronkiektasis, dan air-trapping.\n" +
                        "4. 3D Volume Rendering Technique (VRT): Memanfaatkan seluruh data voxel di dalam volume dengan memberikan fungsi transfer opasitas (transparansi) dan warna berdasarkan rentang HU serta pencahayaan bayangan semu (shading). Menghasilkan model 3D realistis untuk perencanaan bedah ortopedi (fraktur kompleks panggul/wajah) dan anatomi aneurisma vaskular.",
                    analogi = "MIP adalah kacamata yang hanya melihat benda paling berkilau (pembuluh berkontras dan tulang), MinIP hanya melihat ruang paling gelap (kantong dan pipa udara), sedangkan VRT memahat seluruh patung 3D berwarna dengan tingkat ketembusan pandang yang dapat diatur.",
                    crossLinks = "Membutuhkan syarat Voxel Isotropik dari M06.T1.1 dan rekonstruksi irisan tipis dari M05.T3.1.",
                    labType = "ct_m08_kontras_3d"
                )
            )
        ),

        // ==========================================
        // MODUL 9 (M09): Artefak Pesawat CT, Prosedur Pemanasan/Kalibrasi & QA Harian
        // ==========================================
        MriModule(
            id = "M09",
            code = "M09",
            title = "Artefak Pesawat CT, Prosedur Pemanasan/Kalibrasi & QA Harian",
            subtitle = "Identifikasi artefak klinis/instrumentasi, prosedur Tube Warm-Up & Air Calibration, serta uji QA fantom air harian.",
            description = "Evaluasi komprehensif artefak CT berbasis fisika dan pasien (Beam Hardening, Metal Streak & MAR, Motion, Partial Volume) maupun berbasis instrumentasi (Ring Artifact detektor Gen-3, Out-of-Field, Cone-Beam/Windmill), prosedur wajib pagi hari (Tube Warm-Up & Fast Air Calibration), serta protokol uji QA Fantom Air 5-ROI harian.",
            levelBadge = "Tingkat 1–3 • Artefak, Kalibrasi & QA",
            iconName = "verified",
            sortOrder = 9,
            prerequisites = listOf("M08"),
            estimatedMinutes = 45,
            totalQuizzes = 5,
            badgeName = "Inspektur QA & Fisika Klinis CT",
            unlockMessage = "Kamu telah menuntaskan seluruh kurikulum CT-Learn v1 dan siap melakukan analisis artefak serta Quality Assurance pesawat CT!",
            flashcards = listOf(
                MriFlashcard("M09_F1", "Ring Artifact (Artefak Cincin)", "Cincin konsentris berpusat di isocenter pada CT Generasi ke-3 akibat satu/beberapa elemen detektor tidak terkalibrasi atau rusak."),
                MriFlashcard("M09_F2", "Beam Hardening (Cupping & Dark Bands)", "Peningkatan energi rata-rata berkas polikromatik karena foton energi rendah terserap oleh tulang tebal (fossa posterior/bahu)."),
                MriFlashcard("M09_F3", "Tube Warm-Up & Air Calibration", "Prosedur wajib pagi hari untuk memanaskan anoda tabung secara bertahap dan menyamakan tabel gain/offset seluruh kanal detektor di udara."),
                MriFlashcard("M09_F4", "Toleransi QA Fantom Air Harian", "Akurasi CT Number air: 0 +/- 4 HU; Keseragaman (Uniformity tengah vs tepi): <= 5 HU.")
            ),
            nodes = listOf(
                MriNode(
                    code = "M09.T0",
                    title = "Tujuan Pembelajaran Artefak CT, Kalibrasi Pesawat & Quality Assurance",
                    level = 0,
                    sortOrder = 1,
                    prerequisite = null,
                    learningGoal = "Mengidentifikasi mekanisme fisik artefak CT, menjalankan prosedur Tube Warm-Up dan Air Calibration, serta mengevaluasi uji QA Fantom Air harian.",
                    summaryShort = "Target kompetensi mengenali penyebab fisik artefak CT, menjalankan kalibrasi harian, dan mengevaluasi QA fantom air.",
                    detailFull = "Setelah menyelesaikan Modul 9, Anda mampu mengklasifikasikan berbagai artefak pada citra CT berdasarkan mekanisme penyebabnya (fisika berkas, pasien, maupun kerusakan instrumentasi detektor/helikal) beserta langkah perbaikannya.\n\n" +
                        "Anda juga menguasai prosedur operasional standar pra-pelayanan setiap pagi (Tube Warm-Up dan Fast Air Calibration) serta protokol pengujian Quality Assurance (QA) fantom air harian sesuai standar fisika medis.",
                    analogi = "Seorang operator pesawat CT andal bukan hanya tahu cara menekan tombol Scan, melainkan mampu membaca tanda-tanda ketidaknormalan detektor dari artefak gambar dan menjaga presisi mesin setiap pagi melalui kalibrasi.",
                    crossLinks = "Mengintegrasikan pengetahuan detektor M03.T3.1 dan skala HU air pada M06.T1.2.",
                    labType = "ct_m09_artefak_qa"
                ),
                MriNode(
                    code = "M09.T1.1",
                    title = "Artefak Berbasis Fisika & Pasien: Beam Hardening, Metal Streak & Motion",
                    level = 1,
                    sortOrder = 2,
                    prerequisite = "M09.T0",
                    summaryShort = "Mekanisme cupping artifact, dark bands fossa posterior, photon starvation pada implan logam, dan motion artifact.",
                    detailFull = "Artefak pada CT adalah perbedaan sistematis antara nilai HU yang direkonstruksi pada citra dengan koefisien atenuasi jaringan yang sesungguhnya. Tiga artefak utama yang bersumber dari sifat fisik berkas sinar-X dan kondisi pasien meliputi:\n\n" +
                        "1. Beam Hardening Artifact (Pengerasan Berkas): Karena tabung sinar-X memancarkan berkas polikromatik (banyak tingkat energi), foton berenergi rendah (soft X-rays) diserap lebih cepat saat melewati objek padat sehingga berkas yang tersisa menjadi lebih tinggi energi rata-ratanya (lebih 'keras' dan kurang ter-atenuasi). Hal ini memicu dua manifestasi:\n" +
                        "   - Cupping Artifact: Bagian tengah silinder homogen tampak lebih gelap (nilai HU lebih rendah) dibandingkan bagian tepinya karena berkas yang melewati tengah menempuh lintasan paling tebal.\n" +
                        "   - Dark Bands / Streaks (Artefak Hounsfield): Pita gelap di antara dua tulang padat, paling klasik terlihat di fossa posterior otak (di antara kedua os petrosum) yang dapat menyerupai infark batang otak. Solusi: penggunaan bow-tie filter, kenaikan kVp (120/140 kV), kalibrasi koreksi beam hardening, dan pemiringan gantry untuk menghindari objek padat.\n" +
                        "2. Metal Artifact (Streak & Photon Starvation): Implan logam ber-nomor atom sangat tinggi (tambalan amalgam gigi, sekrup pedikel tulang belakang, prostesis panggul, kliping aneurisma) menyerap hampir 100% foton (photon starvation) dan memicu beam hardening ekstrem, menghasilkan garis-garis pancaran terang-gelap (starburst streaks) yang menutupi jaringan sekitar. Solusi: menaikkan kVp, merekonstruksi irisan tipis, menggunakan algoritma Metal Artifact Reduction (MAR) iteratif, atau akuisisi Dual-Energy CT.\n" +
                        "3. Motion Artifact: Gerakan kepala, napas, atau denyut jantung selama rotasi gantry menyebabkan ketidakcocokan posisi proyeksi di sinogram, tampil sebagai bayangan ganda (double contour), shading, atau garis kabur. Solusi: instruksi tahan napas yang jelas, fiksasi kepala, mempercepat waktu rotasi gantry (0,28–0,35 detik), atau ECG-gating pada jantung.",
                    analogi = "Metal artifact seperti memotret cermin yang memantulkan sinar matahari langsung ke lensa kamera: silau garis-garis cahayanya menutupi benda-benda di sekeliling cermin tersebut.",
                    crossLinks = "Berkaitan dengan M01.T1.2 (Polikromatisitas Berkas) dan M02.T2.2 (Bow-Tie Filter).",
                    labType = "ct_m09_artefak_qa"
                ),
                MriNode(
                    code = "M09.T1.2",
                    title = "Artefak Instrumentasi Pesawat: Ring Artifact, Out-of-Field & Windmill",
                    level = 1,
                    sortOrder = 3,
                    prerequisite = "M09.T1.1",
                    summaryShort = "Penyebab mekanis artefak cincin pada CT Generasi ke-3, pemotongan SFOV, dan artefak kincir angin pada MSCT.",
                    detailFull = "Kelompok artefak kedua berkaitan langsung dengan geometri instrumentasi pesawat CT dan sistem detektor:\n\n" +
                        "1. Ring Artifact (Artefak Cincin): Tampak sebagai satu atau beberapa lingkaran penuh maupun busur konsentris yang berpusat tepat di titik Isocenter citra aksial. Artefak ini khas pada pesawat CT Generasi ke-3 (Rotate-Rotate) di mana setiap elemen detektor pada busur memiliki lintasan geometris tetap terhadap pusat putaran. Apabila satu elemen detektor mengalami pergeseran gain/offset (drift akibat suhu), kotoran pada kolimator, atau kerusakan fotodioda/kanal DAS, kesalahan pembacaan yang konsisten pada setiap sudut proyeksi (garis lurus vertikal pada sinogram) akan direkonstruksi balik menjadi sebuah cincin. Solusi pertama: lakukan prosedur Air Calibration (kalibrasi udara); jika cincin menetap, teknisi elektromedis harus mengganti modul detektor terkait.\n" +
                        "2. Out-of-Field (Incomplete Projection) Artifact: Terjadi ketika sebagian tubuh pasien (misalnya kedua lengan yang diletakkan di samping perut pada pasien obesitas) berada di luar batas Scan Field of View (SFOV), tetapi masih menghalangi berkas sinar-X pada sudut proyeksi lateral. Komputer rekonstruksi kekurangan data atenuasi di luar SFOV sehingga memunculkan pita terang tebal (bright shading) di tepi gambar. Solusi: angkat kedua lengan ke atas kepala saat scan toraks/abdomen dan gunakan SFOV maksimal atau algoritma Extended FOV.\n" +
                        "3. Cone-Beam & Windmill (Helical) Artifact: Muncul sebagai pola baling-baling gelap-terang di sekitar struktur ber-kontras tinggi yang berubah cepat sepanjang sumbu-z akibat sudut kerucut detektor multi-baris dan interpolasi pitch tinggi. Solusi: turunkan nilai pitch dan gunakan algoritma rekonstruksi 3D cone-beam atau z-flying focal spot.",
                    analogi = "Ring artifact dapat diibaratkan seperti kuas cat yang salah satu helai bulunya macet dan meneteskan tinta terus-menerus saat kanvas bundar diputar di bawahnya: tetesan itu pasti membentuk lingkaran sempurna yang mengelilingi titik pusat.",
                    crossLinks = "Berkaitan erat dengan M03.T3.1 (Busur Detektor Solid-State) dan M04.T1.1 (Geometri Generasi ke-3).",
                    labType = "ct_m09_artefak_qa"
                ),
                MriNode(
                    code = "M09.T2.1",
                    title = "Prosedur Operasional Standar Pagi Hari: Tube Warm-Up & Air Calibration",
                    level = 2,
                    sortOrder = 4,
                    prerequisite = "M09.T1.2",
                    summaryShort = "Tahapan pemanasan anoda tabung sinar-X dan kalibrasi udara multi-kVp/kolimasi sebelum pelayanan pasien.",
                    detailFull = "Setiap pagi sebelum melayani pasien (atau setelah pesawat tidak melakukan eksposi selama lebih dari beberapa jam), radiografer wajib menjalankan dua prosedur pemeliharaan preventif di konsol operator:\n\n" +
                        "1. Tube Warm-Up (Pemanasan Tabung Sinar-X): Setelah semalaman mati, target anoda Tungsten berada pada suhu ruang (dingin). Apabila tabung dingin langsung dipaksa melakukan eksposi klinis berdaya tinggi (misalnya CTA dengan kVp dan mA maksimal), gradien pemuaian termal yang mendadak akan menyebabkan keretakan mikro (thermal shock / anode cracking), pelepasan gas di dalam ruang vakum (arcing/loncatan bunga api tegangan tinggi), dan memperpendek umur tabung yang bernilai miliaran rupiah. Prosedur Tube Warm-Up menembakkan seri eksposi otomatis dari energi rendah ke tinggi secara bertahap hingga kapasitas panas anoda mencapai suhu kerja stabil (~15–25% OLP/MHU).\n\n" +
                        "2. Fast Air Calibration (Kalibrasi Udara): Dilakukan segera setelah Tube Warm-Up selesai dengan memastikan ruang bore gantry benar-benar kosong (meja pasien ditarik keluar dari celah berkas dan tidak ada bantal/fantom di dalam gantry). Pesawat akan memancarkan sinar-X menembus udara murni pada berbagai kombinasi kVp (80, 100, 120, 140 kV), ukuran focal spot, dan lebar kolimasi. Komputer DAS membaca respons setiap kanal detektor dan memperbarui tabel koreksi Gain dan Offset agar seluruh ribuan elemen detektor menghasilkan pembacaan yang 100% seragam, sehingga mencegah munculnya Ring Artifact dan pergeseran nilai HU.",
                    analogi = "Tube Warm-Up bagaikan memanaskan mesin mobil balap di pagi hari yang dingin agar blok mesin tidak retak saat digas penuh, sedangkan Air Calibration bagaikan mengenolkan (tare) ribuan timbangan digital sekaligus dalam keadaan kosong sebelum menimbang barang.",
                    crossLinks = "Melindungi komponen tabung pada M03.T2.1 dan mencegah Ring Artifact pada M09.T1.2.",
                    labType = "ct_m09_artefak_qa"
                ),
                MriNode(
                    code = "M09.T3.1",
                    title = "Protokol Uji Quality Assurance (QA) Fantom Air Harian & Parameter Evaluasi",
                    level = 3,
                    sortOrder = 5,
                    prerequisite = "M09.T2.1",
                    summaryShort = "Pengukuran 5 ROI pada fantom air silinder untuk menguji akurasi HU air (0 +/- 4 HU), keseragaman (<= 5 HU), dan noise.",
                    detailFull = "Program Jaminan Mutu (Quality Assurance / QA) dan Kendali Mutu (Quality Control / QC) pesawat CT wajib dilakukan secara berkala (harian oleh radiografer dan berkala oleh fisikawan medis) menggunakan Fantom Air Silinder Standar yang dipasang tepat di Isocenter menggunakan penanda laser.\n\n" +
                        "Pada citra potongan aksial fantom air, ditempatkan 5 lingkaran Region of Interest (ROI): satu ROI di titik pusat (Center) dan empat ROI di tepi perifer pada posisi jam 12, jam 3, jam 6, dan jam 9. Empat parameter kuantitatif yang dievaluasi adalah:\n\n" +
                        "1. Akurasi CT Number Air (Water HU Accuracy): Rata-rata nilai HU pada ROI pusat harus berada pada nilai 0 HU dengan batas toleransi maksimal +/- 4 HU (atau +/- 5 HU menurut regulasi uji kesesuaian nasional).\n" +
                        "2. Keseragaman Citra (Image Uniformity): Selisih absolut antara nilai rata-rata HU di ROI pusat dengan rata-rata HU di keempat ROI perifer tidak boleh melebihi 5 HU. Jika bagian tengah jauh lebih rendah/tinggi dari tepi (> 5 HU), hal tersebut mengindikasikan adanya cupping atau capping artifact akibat kegagalan koreksi beam hardening:\n" +
                        "Rumus:\n" +
                        "Uniformity = maks |HU_rata_perifer - HU_rata_pusat| <= 5 HU\n\n" +
                        "3. Quantum Noise (Standar Deviasi / SD): Nilai SD (sigma) pada ROI pusat harus konsisten dengan baseline komisioning pesawat (tidak menyimpang melebihi +/- 10% hingga 20% dari baseline).\n" +
                        "4. Evaluasi Visual Bebas Artefak: Citra fantom air harus bersih dari Ring Artifact maupun garis streak. Selain uji harian ini, uji berkala bulanan/tahunan mencakup verifikasi ketebalan irisan (Slice Thickness Accuracy), keselarasan lampu laser isocenter (+/- 1–2 mm), akurasi pergerakan meja pasien, serta pengukuran dosis CTDI menggunakan dosimeter bilik ionisasi.",
                    analogi = "Uji fantom air dengan 5 ROI seperti mencicipi kemurnian air dari tengah dan empat sudut kolam: baik rasa (nilai rata-rata 0 HU) maupun kejernihannya (keseragaman <= 5 HU) harus sama persis di setiap titik.",
                    crossLinks = "Memverifikasi akurasi skala HU pada M06.T1.2 dan tingkat Quantum Noise pada M07.T1.2.",
                    labType = "ct_m09_artefak_qa"
                )
            )
        )
    )
}
