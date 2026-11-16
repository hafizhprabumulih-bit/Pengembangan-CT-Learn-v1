package com.example.data

internal object CtCurriculumModulesPart1 {
    val modulesM01ToM05: List<CtModule> = listOf(
        // =========================================================================
        // MODUL 1: FISIKA DASAR SINAR-X & PRINSIP ATENUASI CT
        // =========================================================================
        CtModule(
            id = "M01",
            code = "M01",
            title = "Fisika Dasar Sinar-X & Prinsip Atenuasi CT",
            subtitle = "Produksi sinar-X, interaksi foton, Hukum Beer-Lambert & keterbatasan radiografi proyeksi",
            description = "Fondasi fisika pencitraan CT Scan: keterbatasan superposisi radiografi konvensional, mekanisme produksi sinar-X (Bremsstrahlung & Karakteristik), interaksi foton dengan materi (Fotolistrik & Hamburan Compton), Hukum Atenuasi Eksponensial Beer-Lambert, serta konsep Beam Hardening dan pra-filtrasi.",
            levelBadge = "Tingkat 1–3 • Fisika Dasar CT",
            iconName = "bolt",
            prerequisites = emptyList(),
            estimatedMinutes = 40,
            totalQuizzes = 6,
            badgeName = "Ahli Fisika Atenuasi CT",
            unlockMessage = "Selamat! Kamu telah menguasai produksi sinar-X, Efek Fotolistrik vs Compton, dan Hukum Beer-Lambert!",
            flashcards = listOf(
                CtFlashcard("M01_F1", "Bremsstrahlung vs Karakteristik", "80–85% foton tabung CT berasal dari pengereman elektron (Bremsstrahlung polikromatik); sisanya transisi kulit K Tungsten (59 & 67 keV)."),
                CtFlashcard("M01_F2", "Efek Fotolistrik vs Compton", "Fotolistrik sebanding Z^3/E^3 (kontras tulang & Iodin); Compton sebanding kerapatan elektron dan menimbulkan radiasi hambur."),
                CtFlashcard("M01_F3", "Hukum Beer-Lambert & DAS", "I = I0 * exp(-mu * x) sehingga sinyal proyeksi logaritmik di DAS adalah P = ln(I0/I) = Sigma(mu_i * dx)."),
                CtFlashcard("M01_F4", "Beam Hardening & HVL", "Penyerapan foton energi rendah menaikkan energi rata-rata berkas; HVL = 0,693 / mu.")
            ),
            sortOrder = 1,
            nodes = listOf(
                CtNode(
                    code = "M01.T0",
                    level = 0,
                    title = "Tujuan Belajar & Peta Konsep Modul 1",
                    summaryShort = "Memahami perjalanan berkas sinar-X dari tabung hingga menembus jaringan tubuh serta bagaimana perbedaan atenuasi menjadi dasar pembentukan citra CT Scan.",
                    detailFull = """Selamat datang di Modul 1 CT-Learn! Pada modul pembuka ini, Anda akan mempelajari fondasi fisika radiasi pengion yang mendasari cara kerja seluruh pesawat Computed Tomography (CT Scan) modern.

Kompetensi utama yang akan Anda kuasai meliputi:
1. Mengidentifikasi dua keterbatasan fundamental radiografi proyeksi 2D konvensional (superposisi anatomi 3D dan rendahnya resolusi kontras jaringan lunak) serta bagaimana akuisisi tomografi multi-sudut mengatasinya.
2. Menjelaskan mekanisme produksi foton sinar-X di dalam tabung CT: radiasi pengereman (Bremsstrahlung) yang membentuk spektrum polikromatik kontinu dan radiasi Karakteristik pada target anoda Wolfram/Tungsten (Z = 74).
3. Membedakan interaksi Efek Fotolistrik (bergantung kuat pada Z³/E³) dan Hamburan Compton (bergantung pada densitas elektron ρe) pada rentang energi diagnostik CT (80–140 kVp).
4. Menerapkan Hukum Atenuasi Eksponensial Beer-Lambert (I = I₀ · e^(−μx)) dalam menghitung transmisi berkas sinar-X melintasi matriks voksel pasien.
5. Menganalisis fenomena pengerasan berkas (Beam Hardening) dan peran krusial filter pra-pasien serta Bow-Tie Filter.""",
                    analogi = "Analogi: Jika foto Rontgen biasa seperti memotret bayangan seluruh isi roti tawar dari satu sisi sehingga kismis di depan dan belakang saling bertumpuk, maka CT Scan memotret roti tersebut dari 360 derajat lalu mengirisnya tipis-tipis secara digital sehingga isi tiap lembar roti terlihat jelas.",
                    learningGoal = "Menguasai prinsip produksi sinar-X, interaksi foton dengan materi, dan Hukum Atenuasi Beer-Lambert sebagai fondasi pencitraan CT.",
                    crossLinks = "Fondasi bagi M02 (Anatomi Gantry), M03 (Tabung & Detektor CT), dan M06 (Skala Hounsfield Unit).",
                    labType = "ct_m01_atenuasi",
                    sortOrder = 1
                ),
                CtNode(
                    code = "M01.T1.1",
                    level = 1,
                    title = "Keterbatasan Radiografi Konvensional vs Solusi Tomografi",
                    summaryShort = "Radiografi konvensional mereduksi objek 3D menjadi bayangan 2D sehingga organ saling bertumpuk (superposisi) dan tidak mampu membedakan selisih atenuasi jaringan lunak < 1%.",
                    detailFull = """Radiografi konvensional (foto Rontgen proyeksi) memproyeksikan seluruh ketebalan anatomi tiga dimensi (3D) tubuh manusia ke atas satu bidang detektor dua dimensi (2D) dari satu arah berkas sinar-X saja. Akibatnya muncul dua kelemahan fisik utama:

1. Superposisi Struktur Anatomi: Seluruh organ di sepanjang lintasan berkas sinar-X—mulai dari kulit anterior, otot, kosta, parenkim paru, mediastinum, hingga vertebra posterior—saling tumpang-tindih pada satu piksel yang sama. Lesi kecil di belakang struktur padat seperti tulang tengkorak atau jantung sering kali tertutup total.

2. Keterbatasan Resolusi Kontras Jaringan Lunak: Radiografi proyeksi konvensional hanya mampu membedakan struktur jika terdapat perbedaan koefisien atenuasi minimal sekitar 5–10% (seperti udara, lemak, jaringan lunak secara umum, tulang, dan logam). Ia tidak dapat membedakan substansia grisea (gray matter) terhadap substansia alba (white matter) di otak.

Pesawat CT Scan mengatasi kedua masalah ini secara elegan: tabung sinar-X dan detektor berputar 360° mengelilingi pasien untuk mengukur ratusan hingga ribuan profil proyeksi atenuasi dari berbagai sudut. Komputer kemudian merekonstruksi penampang melintang (axial cross-section) setebal 0,5–5 mm tanpa tumpang-tindih organ, sekaligus mencapai resolusi kontras sangat tinggi yang mampu memisahkan perbedaan densitas jaringan lunak hingga 0,3–0,5% (setara 3–5 Hounsfield Unit).""",
                    analogi = "Analogi: Menyinari koper tertutup dengan senter dari satu arah hanya menampilkan siluet tumpukan barang di dalamnya. Namun bila kita menyinari koper dari sekelilingnya sambil memetakan irisan demi irisan, kita tahu persis posisi tiap benda tanpa membuka koper.",
                    prerequisite = "M01.T0",
                    crossLinks = "Berkaitan langsung dengan M05 (Rekonstruksi Sinogram) dan M06 (Resolusi Kontras HU).",
                    labType = "ct_m01_atenuasi",
                    sortOrder = 2
                ),
                CtNode(
                    code = "M01.T1.2",
                    level = 1,
                    title = "Produksi Sinar-X pada Tabung CT (Bremsstrahlung & Karakteristik)",
                    summaryShort = "Elektron berkecepatan tinggi dari katoda menumbuk anoda Tungsten (Z=74): 99% energi berubah menjadi panas, dan ~1% menjadi foton sinar-X Bremsstrahlung serta Karakteristik.",
                    detailFull = """Di dalam tabung sinar-X pesawat CT yang hampa udara (vakum tinggi), filamen katoda dipanaskan oleh arus filamen hingga memancarkan awan elektron melalui emisi termionik. Beda potensial tegangan tinggi (80 hingga 140 kVp) dari High-Voltage Generator mempercepat elektron-elektron tersebut menuju target anoda putar berbahan paduan Tungsten-Rhenium (Wolfram, nomor atom Z = 74, titik leleh ~3.422 °C).

Saat elektron berenergi kinetik tinggi menumbuk anoda, sekitar 99% energinya terkonversi menjadi panas termal, sedangkan ~1% sisanya berubah menjadi foton sinar-X melalui dua mekanisme:

1. Radiasi Bremsstrahlung (Braking Radiation / Pengereman): Elektron proyektil melintas dekat inti atom Tungsten yang bermuatan positif kuat sehingga lintasannya dibelokkan dan kecepatannya melambat drastis. Energi kinetik yang hilang dipancarkan sebagai foton sinar-X dengan spektrum energi kontinu (polikromatik) hingga energi maksimum E_max = kVp (misalnya 120 keV pada pengaturan 120 kVp). Sekitar 80–85% berkas sinar-X diagnostik berasal dari Bremsstrahlung.

2. Radiasi Sinar-X Karakteristik: Jika elektron proyektil memiliki energi kinetik melebihi energi ikat kulit K atom Tungsten (≥ 69,5 keV), elektron tersebut dapat menendang keluar elektron kulit K. Kekosongan kulit K segera diisi oleh elektron dari kulit L atau M sambil memancarkan foton sinar-X berenergi diskrit khas Tungsten (sekitar 59 keV dan 67 keV).""",
                    analogi = "Analogi: Bremsstrahlung seperti mobil balap yang mengerem mendadak saat menikung tajam di dekat tiang magnet raksasa—semakin tajam pengeremannya, semakin nyaring derit energi foton yang dilepaskan. Radiasi karakteristik seperti bola biliar yang menendang bola di lubang K, lalu bola dari rak atas jatuh mengisi tempatnya sambil berbunyi 'klik' dengan nada yang selalu sama.",
                    prerequisite = "M01.T1.1",
                    crossLinks = "Diperdalam pada M03.T1.1 (Arsitektur Tabung Sinar-X CT & Kapasitas Panas MHU).",
                    labType = "ct_m01_atenuasi",
                    sortOrder = 3
                ),
                CtNode(
                    code = "M01.T2.1",
                    level = 2,
                    title = "Interaksi Sinar-X dengan Materi: Efek Fotolistrik vs Hamburan Compton",
                    summaryShort = "Pada energi CT (80–140 kVp), Efek Fotolistrik (∝ Z³/E³) memberikan kontras tinggi pada tulang dan kontras Iodin, sedangkan Hamburan Compton (∝ ρe) mendominasi jaringan lunak.",
                    detailFull = """Ketika berkas foton sinar-X menembus tubuh pasien menuju detektor CT, foton dapat diserap habis atau dihamburkan melalui dua mekanisme interaksi utama pada rentang energi diagnostik CT (rata-rata energi efektif 50–80 keV):

Rumus: P_Fotolistrik ∝ Z³ / E³  |  P_Compton ∝ ρ_elektron (hampir independen dari Z)

1. Efek Fotolistrik (Photoelectric Absorption): Foton sinar-X datang menyerahkan seluruh energinya kepada elektron kulit dalam (kulit K) atom jaringan dan lenyap sepenuhnya, melepaskan fotoelektron. Probabilitas interaksi ini berbanding lurus dengan pangkat tiga nomor atom efektif jaringan (Z³) dan berbanding terbalik dengan pangkat tiga energi foton (E³). Inilah alasan mengapa tulang kortikal (Kalsium Z = 20) dan media kontras vaskular Iodin (Z = 53, K-edge = 33,2 keV) menyerap sinar-X jauh lebih kuat daripada jaringan lunak (Z efektif ≈ 7,4), serta mengapa menurunkan kVp dari 120 kVp ke 80–100 kVp pada CT Angiografi meningkatkan kontras Iodin secara dramatis.

2. Hamburan Compton (Compton Scatter): Foton sinar-X menumbuk elektron kulit luar yang terikat lemah, menyerahkan sebagian energinya ke elektron recoil, lalu foton terhambur ke arah baru dengan energi lebih rendah. Probabilitas Compton bergantung terutama pada kerapatan elektron (ρe) per satuan volume jaringan dan hampir tidak bergantung pada nomor atom Z. Pada jaringan lunak dan air di 120 kVp, Hamburan Compton mendominasi atenuasi tetapi foton hambur yang mencapai detektor menurunkan kontras citra (menimbulkan kabut noise/cupping), sehingga harus ditangkal oleh kolimator pasca-pasien (anti-scatter septa).""",
                    analogi = "Analogi: Efek Fotolistrik seperti bola tenis yang langsung tertangkap penuh oleh sarung tangan penjaga gawang (foton berhenti total, menghasilkan bayangan kontras yang tajam). Hamburan Compton seperti bola yang memantul liar mengenai bahu pemain dan nyasar ke tribun penonton (mengaburkan sinyal detektor).",
                    prerequisite = "M01.T1.2",
                    crossLinks = "Dasar pemilihan kVp pada M07.T1.1 dan penguatan kontras Iodin K-edge pada M08.T1.1.",
                    labType = "ct_m01_atenuasi",
                    sortOrder = 4
                ),
                CtNode(
                    code = "M01.T2.2",
                    level = 2,
                    title = "Hukum Beer-Lambert & Koefisien Atenuasi Linier (μ)",
                    summaryShort = "Penurunan intensitas sinar-X sepanjang jaringan mengikuti fungsi eksponensial I = I₀·e^(−∫μ dx); pesawat CT menghitung distribusi spasial μ(x,y) pada setiap voksel.",
                    detailFull = """Setiap jenis jaringan di dalam tubuh memiliki nilai Koefisien Atenuasi Linier (μ, satuan cm⁻¹) yang menyatakan fraksi foton sinar-X yang dikeluarkan dari berkas utama (baik karena diserap fotolistrik maupun dihamburkan Compton) per satuan ketebalan lintasan.

Untuk berkas monokromatik yang menembus jaringan homogen setebal x (cm) dengan intensitas awal I₀ (diukur oleh detektor referensi udara sebelum pasien), intensitas foton I yang berhasil menembus pasien dan ditangkap detektor CT mengikuti Hukum Beer-Lambert:

Rumus: I = I₀ · e^(−μ · x)   ⟹   ln(I₀ / I) = μ · x

Karena tubuh manusia bersifat heterogen (terdiri atas deretan N voksel dengan nilai atenuasi μ₁, μ₂, ..., μₙ dan lebar voksel Δx), persamaan transmisi sepanjang satu garis proyeksi sinar menjadi:

Rumus: I = I₀ · exp( − ∑ᵢ μᵢ · Δx )   ⟹   Proyeksi P = ln(I₀ / I) = ∑ᵢ μᵢ · Δx

Transformasi logaritmik ln(I₀ / I) yang dilakukan oleh sirkuit pengolah sinyal di dalam Data Acquisition System (DAS) mengubah hubungan eksponensial menjadi penjumlahan linier sederhana (integral garis). Dari ribuan persamaan penjumlahan linier pada berbagai sudut rotasi Gantry inilah komputer CT dapat memecahkan nilai μ(x,y) untuk setiap voksel secara akurat.""",
                    analogi = "Analogi: Bayangkan berkas sinar-X melewati barisan kaca film bertingkat. Jika tiap lembar kaca mengurangi cahaya secara proporsional, pengukur cahaya di ujung dapat menghitung tingkat kegelapan masing-masing kaca dengan mengambil logaritma rasio cahaya awal dibagi cahaya akhir.",
                    prerequisite = "M01.T2.1",
                    crossLinks = "Dikonversi menjadi nilai Hounsfield Unit (HU) pada M06.T1.2 dan Sinogram pada M05.T1.1.",
                    labType = "ct_m01_atenuasi",
                    sortOrder = 5
                ),
                CtNode(
                    code = "M01.T3.1",
                    level = 3,
                    title = "Spektrum Polikromatik, Beam Hardening & Peran Filtrasi",
                    summaryShort = "Karena berkas sinar-X tabung bersifat polikromatik, foton berenergi rendah terserap lebih dulu sehingga energi rata-rata berkas meningkat (Beam Hardening) saat menembus tulang tebal.",
                    detailFull = """Hukum Beer-Lambert murni mengasumsikan bahwa berkas sinar-X bersifat monokromatik (hanya memiliki satu nilai energi foton). Dalam kenyataannya, tabung sinar-X pesawat CT memancarkan spektrum Bremsstrahlung yang bersifat polikromatik: saat konsol diatur pada 120 kVp, energi foton tersebar mulai dari belasan keV hingga maksimum 120 keV, dengan energi rata-rata efektif berkisar antara 60–75 keV.

Ketika berkas polikromatik ini melintasi jaringan tubuh atau tulang kortikal yang padat (seperti os petrosum di basis kranii), foton-foton berenergi rendah ('soft X-rays') jauh lebih mudah diserap melalui Efek Fotolistrik (karena probabilitas ∝ 1/E³). Akibatnya, berkas sinar-X yang berhasil lolos memiliki proporsi foton berenergi tinggi yang lebih besar: energi rata-rata berkas bergeser naik (berkas menjadi lebih 'keras' atau lebih daya tembusnya). Fenomena ini disebut Pengerasan Berkas (Beam Hardening).

Rumus: E_efektif ≈ (1/3 s.d. 1/2) × E_puncak (kVp)  |  HVL (Half-Value Layer) = 0,693 / μ

Jika tidak dikoreksi, Beam Hardening menyebabkan nilai μ terukur di bagian tengah objek silinder tampak lebih rendah daripada bagian tepinya (artefak cupping) serta memunculkan pita gelap (dark streaks) di antara dua tulang padat. Untuk meminimalkan efek ini sejak dari hulu, pesawat CT menerapkan dua lapis filtrasi fisik di dalam kolimator pra-pasien:
1. Filtrasi Inheren & Flat Filter (Aluminium / Tembaga): Menyaring foton energi sangat rendah (< 30 keV) yang hanya akan diserap kulit pasien tanpa pernah sampai ke detektor.
2. Bow-Tie Filter (Filter Dasi Kupu-Kupu): Filter berbentuk cekung di tengah dan tebal di kedua tepi untuk menyamakan kekerasan berkas antara bagian tengah tubuh pasien yang tebal dan bagian perifer tubuh yang tipis.""",
                    analogi = "Analogi: Seperti pelari maraton dengan berbagai tingkat stamina yang melewati tanjakan terjal: pelari yang lemah (foton energi rendah) gugur di awal tanjakan, sehingga yang sampai di garis finis hanyalah pelari elit berstamina tinggi (berkas menjadi 'lebih keras'). Filter di awal lomba menyaring pelari yang belum siap agar lintasan tetap tertib.",
                    prerequisite = "M01.T2.2",
                    crossLinks = "Terhubung ke Bow-Tie Filter pada M02.T2.2 dan artefak Beam Hardening pada M09.T1.1.",
                    labType = "ct_m01_atenuasi",
                    sortOrder = 6
                )
            )
        ),

        // =========================================================================
        // MODUL 2: ANATOMI PESAWAT CT SCAN & ARSITEKTUR GANTRY
        // =========================================================================
        CtModule(
            id = "M02",
            code = "M02",
            title = "Anatomi Pesawat CT Scan & Arsitektur Gantry",
            subtitle = "Bedah fisik Gantry, Rotating Frame, Kolimator, Bow-Tie Filter, Laser Isocenter, Meja Carbon Fiber & Konsol",
            description = "Bedah tuntas anatomi perangkat keras pesawat CT Scan: tata letak ruang pemeriksaan berlapis timbal (Pb), anatomi eksternal dan internal Gantry (Rotating Frame), sistem Laser Positioning & bahaya Off-Centering, kolimator pra-pasien & pasca-pasien, mekanisme Meja Pasien Carbon Fiber, serta arsitektur komputer Konsol Operator.",
            levelBadge = "Tingkat 1–3 • Anatomi Pesawat CT",
            iconName = "precision_manufacturing",
            prerequisites = listOf("M01"),
            estimatedMinutes = 45,
            totalQuizzes = 6,
            badgeName = "Arsitek Gantry & Isocenter CT",
            unlockMessage = "Selamat! Kamu telah menguasai anatomi internal Gantry, Laser Isocenter, Topogram Scout, Bow-Tie Filter, dan Meja Carbon Fiber!",
            flashcards = listOf(
                CtFlashcard("M02_F1", "Rotating Frame 180 Derajat", "Tabung Sinar-X dan Busur Detektor berhadapan 180 derajat di atas rangka putar, diseimbangkan oleh HV Generator dan Heat Exchanger."),
                CtFlashcard("M02_F2", "Laser Isocenter & Topogram Scout", "Laser Aksial, Sagital, dan Koronal menempatkan pusat organ tepat di Isocenter agar kompensasi Bow-Tie Filter dan kalkulasi mA dari Topogram Scout akurat."),
                CtFlashcard("M02_F3", "Bow-Tie Filter & Kolimator Ganda", "Bow-Tie Filter menyeragamkan fluks foton sesuai kontur tubuh; Pre-patient collimator membatasi berkas z-axis; Post-patient septa menolak hambur Compton."),
                CtFlashcard("M02_F4", "Meja Carbon Fiber & IRS", "Daun meja komposit serat karbon (Z=6) kuat menahan beban tanpa menyerap sinar-X; komputer IRS merekonstruksi Sinogram menjadi citra DICOM.")
            ),
            sortOrder = 2,
            nodes = listOf(
                CtNode(
                    code = "M02.T0",
                    level = 0,
                    title = "Tujuan Belajar & Peta Anatomi Pesawat CT",
                    summaryShort = "Mengenali seluruh komponen fisik di Ruang Pemeriksaan dan Ruang Kontrol serta memahami tata letak komponen internal di balik penutup Gantry.",
                    detailFull = """Selamat datang di Modul 2 CT-Learn! Sebelum mengoperasikan parameter konsol atau menganalisis citra klinis, seorang radiografer dan fisikawan medis wajib menguasai anatomi fisik pesawat CT Scan luar dan dalam.

Pada modul ini Anda akan mempelajari:
1. Tiga subsistem utama instalasi pesawat CT Scan: Gantry, Meja Pasien (Patient Couch), dan Ruang Konsol Operator (Control Console) beserta spesifikasi proteksi radiasi ruang pemeriksaan.
2. Anatomi eksternal Gantry (Bore Aperture 70–85 cm, Panel Kontrol Gantry, Gantry Tilt, Mikrofon/Speaker Interkom) dan bedah anatomi internal pada Rotating Frame saat penutup Gantry dibuka.
3. Fungsi tiga bidang Laser Positioning (Aksial, Sagital, Koronal) dalam menempatkan organ tepat di titik Isocenter Gantry, serta dampak fatal Off-Centering meja pasien.
4. Perbedaan fisik dan fungsi Bow-Tie Filter, Pre-Patient Collimator, serta Post-Patient Collimator (Anti-Scatter Grid).
5. Konstruksi Meja Pasien berbahan Carbon Fiber dan akurasi motor penggerak longitudinal untuk penentuan Pitch helikal.
6. Arsitektur sistem komputer rekonstruksi cepat (IRS), penyimpanan PACS/DICOM, dan sistem Chiller/PDU.""",
                    analogi = "Analogi: Mempelajari anatomi pesawat CT seperti seorang pilot yang mengenali kokpit, mesin turbin, dan sayap pesawat terbang—ketika tahu fungsi tiap komponen di balik kap Gantry, Anda tahu persis apa yang terjadi saat tombol Scan ditekan.",
                    learningGoal = "Mampu mengidentifikasi posisi, fungsi fisik, dan interaksi mekanis seluruh komponen Gantry, Meja Pasien, dan Konsol CT.",
                    crossLinks = "Berlanjut ke bedah rantai sinyal elektrik-foton-digital pada M03.",
                    labType = "ct_m02_anatomi_pesawat",
                    sortOrder = 1
                ),
                CtNode(
                    code = "M02.T1.1",
                    level = 1,
                    title = "Tiga Subsistem Utama Instalasi CT & Tata Letak Ruangan",
                    summaryShort = "Instalasi CT terdiri atas Gantry dan Meja Pasien di ruang pemeriksaan berlapis timbal (Pb 2 mm) serta Konsol Operator di balik kaca intip timbal.",
                    detailFull = """Instalasi pesawat CT Scan klinis terdiri atas tiga subsistem perangkat keras utama yang bekerja secara terintegrasi:
1. Gantry: Struktur berbentuk cincin raksasa (donat) yang menampung sumber pembangkit sinar-X, sistem mekanik rotasi cepat, deretan detektor, dan sistem akuisisi data (DAS).
2. Meja Pasien (Patient Table / Couch): Platform elektromekanis presisi tinggi yang menopang tubuh pasien dan menggerakkannya secara sinkron menembus lubang Gantry (bore) selama akuisisi sekuensial maupun helikal.
3. Konsol Operator (Operator Control Console): Stasiun kerja komputer di ruang kontrol tempat radiografer merencanakan protokol eksposi, memantau rekonstruksi citra secara real-time, dan berkomunikasi dengan pasien.

Selain ketiga unit utama tersebut, terdapat kabinet penunjang teknis berupa PDU (Power Distribution Unit) yang menstabilkan suplai daya listrik 3-fasa berdaya tinggi (50–100 kVA) serta sistem pendingin gedung/Chiller.

Karena pesawat CT menggunakan radiasi pengion sinar-X berintensitas tinggi dengan hamburan (scatter) ke segala arah dari tubuh pasien, ruang pemeriksaan CT wajib dirancang dengan dinding bata/beton berlapis lembaran timbal (Pb setara 2,0 mm), pintu geser berlapis Pb dengan interlock indikator lampu merah tanda radiasi aktif, serta jendela kaca timbal (Lead Glass) yang memungkinkan radiografer mengawasi pasien secara visual langsung dari meja konsol.""",
                    analogi = "Analogi: Ruang Gantry adalah panggung studio pemotretan berpelindung perisai timbal, Meja Pasien adalah rel kamera berjalan yang presisi, dan Ruang Konsol adalah ruang sutradara di balik kaca pelindung yang mengendalikan seluruh jalannya pemindaian.",
                    prerequisite = "M02.T0",
                    crossLinks = "Terkait dengan keselamatan radiasi dan optimasi dosis pada M07.",
                    labType = "ct_m02_anatomi_pesawat",
                    sortOrder = 2
                ),
                CtNode(
                    code = "M02.T1.2",
                    level = 1,
                    title = "Bedah Anatomi Eksternal & Internal Gantry (Rotating Frame)",
                    summaryShort = "Di balik penutup Gantry terdapat Rotating Frame aluminium yang memutar Tabung Sinar-X, HV Generator, Heat Exchanger, Kolimator, serta Detector Array & DAS 360° mengelilingi Bore.",
                    detailFull = """Dari sisi luar (eksternal), Gantry memiliki lubang tengah yang disebut Gantry Aperture / Bore dengan diameter standar 70 cm (atau 80–85 cm pada Wide-Bore CT untuk pasien bariatric, trauma, dan perencanaan radioterapi). Di bagian depan atas penutup Gantry terdapat panel kontrol fisik (tombol pengatur tinggi/masuk-keluar meja, tombol nyala/mati Laser Positioning, tombol Zero/Reset posisi meja, serta tombol merah Emergency Stop), layar indikator digital posisi meja & sudut kemiringan Gantry (Gantry Tilt umumnya ±30°), serta mikrofon dan speaker interkom dua arah.

Ketika penutup depan (front cover) Gantry dibuka, tampak sebuah rangka cincin logam paduan aluminium kekuatan tinggi yang disebut Rotating Frame (Drum Putar) yang ditopang oleh bantalan presisi pada Stationary Frame. Seluruh komponen utama di atas Rotating Frame ini berputar 360° tanpa henti dengan kecepatan 0,25 hingga 1,0 detik per putaran (mencapai gaya sentrifugal 15G–30G!):

1. Posisi Atas (12 Jam) — Tabung Sinar-X (X-Ray Tube) & Kolimator Pra-Pasien: Memancarkan berkas kipas/kerucut (fan/cone beam) sinar-X menembus pusat Bore.
2. Posisi Kiri/Kanan (9 Jam & 3 Jam) — High-Voltage (HV) Generator & Oil-to-Air Heat Exchanger: Generator tegangan tinggi (80–140 kV) dan pendingin oli tabung ikut diposisikan di sisi berlawanan pada Rotating Frame agar menjadi penyeimbang beban mekanis (counter-balance) saat berputar cepat.
3. Posisi Bawah (6 Jam, tepat berhadapan 180° dengan Tabung) — Busur Detector Array & Modul DAS: Menangkap foton sinar-X yang telah menembus tubuh pasien dan mengubahnya menjadi data digital.
4. Lingkar Dalam/Belakang — Cincin Slip-Ring & Transmisi Data Optik: Menyalurkan daya listrik dan data digital antara bagian Gantry yang diam dan Rotating Frame yang berputar.""",
                    analogi = "Analogi: Rotating Frame di dalam Gantry bekerja seperti bianglala berkecepatan tinggi yang diseimbangkan sempurna: Tabung Sinar-X dan Detektor selalu berhadapan 180 derajat seperti juru foto dan layar tangkapnya, sementara HV Generator dan Pendingin dipasang di sisi kiri-kanan sebagai pemberat penyeimbang agar putaran bianglala tidak oleng.",
                    prerequisite = "M02.T1.1",
                    crossLinks = "Gunakan Simulator 1 (Buka Penutup Gantry) untuk mengeksplorasi ketujuh komponen internal Gantry.",
                    labType = "ct_m02_anatomi_pesawat",
                    sortOrder = 3
                ),
                CtNode(
                    code = "M02.T2.1",
                    level = 2,
                    title = "Sistem Laser Positioning, Isocenter Gantry & Bahaya Off-Centering",
                    summaryShort = "Tiga sinar laser (Aksial, Koronal, Sagital) memandu penempatan pusat anatomi pasien tepat di Isocenter Gantry; Off-Centering mengacaukan kerja Bow-Tie Filter dan AEC.",
                    detailFull = """Titik pusat geometris tempat sumbu rotasi Gantry berpotongan dengan berkas tengah sinar-X disebut Isocenter Gantry. Agar kualitas citra optimal dan dosis radiasi akurat, pusat anatomi organ yang diperiksa wajib ditempatkan tepat di titik Isocenter ini menggunakan panduan tiga sinar Laser Positioning:
1. Laser Aksial (Transversal): Menentukan batas awal dan akhir irisan sepanjang sumbu kepala-kaki (z-axis), terdiri dari laser eksternal (beberapa sentimeter di luar bidang fokus untuk kemudahan penandaan) dan laser internal tepat pada bidang berkas sinar-X.
2. Laser Sagital (Garis Tengah Vertikal): Memastikan garis tengah tubuh pasien (Mid-Sagittal Plane) lurus simetris di tengah meja tanpa rotasi kiri-kanan.
3. Laser Koronal (Garis Samping Horizontal di sisi kiri dan kanan Bore): Panduan paling krusial untuk mengatur ketinggian vertikal meja (Table Height) sehingga pertengahan ketebalan anterior-posterior tubuh pasien berada tepat di garis koronal (Mid-Axillary Line).

Rumus: ΔDosis & ΔNoise meningkat tajam bila |Δy_meja| > 20 mm dari Isocenter

Apa yang terjadi bila radiografer menyetel meja terlalu rendah atau terlalu tinggi (Vertical Off-Centering)?
• Kegagalan Kompensasi Bow-Tie Filter: Bow-Tie Filter dirancang tipis di tengah (untuk bagian tubuh paling tebal) dan tebal di pinggir (untuk bagian tubuh tipis). Jika meja terlalu rendah, bagian tengah tubuh pasien yang tebal justru terkena berkas pinggir Bow-Tie Filter yang sudah teredam kuat, sehingga foton yang sampai ke detektor sangat sedikit dan menimbulkan Quantum Noise (bintik kasar) hebat. Sebaliknya, bagian tepi tubuh terkena berkas tengah yang intensitasnya tinggi sehingga menerima dosis radiasi berlebih.
• Kesalahan Perhitungan AEC (Automatic Exposure Control) dari Topogram/Scout (Localizer): Sebelum akuisisi utama, pesawat membuat citra proyeksi 2D Topogram/Scout dengan tabung diam (posisi AP 0°, PA 180°, atau Lateral 90°) sambil meja bergerak lurus. Posisi sudut tabung saat Scout dan ketepatan Isocenter sangat menentukan profil modulasi mA: jika meja terlalu tinggi (dekat tabung pada Scout AP 0°), bayangan tubuh mengalami magnifikasi geometris (tampak lebih lebar) sehingga AEC menyemburkan mA berlebih dan memberikan overdosis radiasi 20–40%. Sebaliknya jika meja terlalu rendah, bayangan Scout mengecil sehingga AEC menurunkan mA terlalu rendah dan citra menjadi sangat berbintik (noisy). Penggunaan Scout dua proyeksi orthogonal (AP/PA + Lateral) sangat dianjurkan agar AEC menghitung dimensi elips tubuh secara akurat.""",
                    analogi = "Analogi: Bow-Tie Filter ibarat kacamata fokus yang bagian tengahnya bening untuk objek tebal dan pinggirnya gelap untuk objek tipis. Jika wajah pasien digeser turun dari tengah kacamata (Off-Centering), mata justru tertutup bagian kacamata yang gelap sehingga pandangan menjadi buram dan berbintik.",
                    prerequisite = "M02.T1.2",
                    crossLinks = "Diuji langsung lewat slider Tinggi Meja (Off-Centering) di Simulator 1 dan berhubungan dengan AEC di M07.T3.1.",
                    labType = "ct_m02_anatomi_pesawat",
                    sortOrder = 4
                ),
                CtNode(
                    code = "M02.T2.2",
                    level = 2,
                    title = "Sistem Kolimasi Ganda (Pre-Patient & Post-Patient) serta Bow-Tie Filter",
                    summaryShort = "Pre-Patient Collimator membatasi lebar berkas z-axis sesuai kebutuhan irisan untuk menekan dosis, sedangkan Post-Patient Collimator di depan detektor menolak radiasi hambur Compton.",
                    detailFull = """Setelah berkas sinar-X keluar dari jendela tabung (tube port), berkas tersebut melewati susunan perangkat pembentuk berkas (beam-shaping assembly) sebelum dan sesudah menembus tubuh pasien:

1. Bow-Tie Filter (Filter Pembentuk Kontur Tubuh): Terletak tepat setelah flat filter di dekat tabung sinar-X. Karena penampang tubuh manusia berbentuk elips/silinder (tebal di bagian tengah AP dan tipis di tepi lateral), tanpa Bow-Tie Filter detektor di tepi lateral akan mengalami saturasi (kelebihan foton) sementara detektor tengah kekurangan foton. Bow-Tie Filter yang berbentuk seperti dasi kupu-kupu menyamakan intensitas foton yang tiba di seluruh busur detektor, menurunkan dosis kulit perifer hingga 50%, dan menekan artefak Beam Hardening. Pesawat CT modern memiliki beberapa ukuran Bow-Tie Filter bermotor (Small/Head, Medium, Large/Body) yang berganti otomatis sesuai pilihan Field of View (SFOV).

2. Kolimator Pra-Pasien (Pre-Patient / Source Collimator): Sepasang daun penutup berbahan Tungsten/Molibdenum bermotor tepat di bawah Bow-Tie Filter yang mengatur lebar bukaan berkas sinar-X sepanjang sumbu longitudinal (z-axis), misalnya bukaan total 20 mm atau 40 mm pada MSCT. Fungsi utamanya adalah membatasi volume jaringan pasien yang terpapar sinar-X hanya pada lebar detektor aktif, serta meminimalkan bayangan kabur tepi (penumbra) dari focal spot.

3. Kolimator Pasca-Pasien (Post-Patient / Detector Collimator & Anti-Scatter Septa): Terletak tepat di permukaan atas busur detektor setelah sinar-X melewati pasien. Pada MSCT, komponen ini berupa kisi-kisi pelat tipis Tungsten (septa) yang sejajar arah fokus tabung. Fungsinya adalah menyerap foton Hamburan Compton (scatter) yang datang miring dari tubuh pasien agar tidak masuk ke elemen detektor tetangga, sehingga menjaga ketajaman kontras jaringan lunak.""",
                    analogi = "Analogi: Pre-Patient Collimator bekerja seperti tirai jendela panggung yang hanya membuka sorotan lampu selebar aktor utama (agar penonton di luar panggung tidak silau/terkena dosis), sedangkan Post-Patient Anti-Scatter Septa bekerja seperti sekat kacamata kuda di depan tiap sensor kamera agar pantulan cahaya liar dari samping tidak mengaburkan foto.",
                    prerequisite = "M02.T2.1",
                    crossLinks = "Terkait erat dengan geometri detektor MSCT pada M04.T2.1 dan artefak di M09.",
                    labType = "ct_m02_anatomi_pesawat",
                    sortOrder = 5
                ),
                CtNode(
                    code = "M02.T3.1",
                    level = 3,
                    title = "Mekanika Meja Pasien Carbon Fiber & Arsitektur Komputer Konsol",
                    summaryShort = "Daun meja (tabletop) dibuat dari serat karbon ber-atenuasi sangat rendah dan kekuatan tarik tinggi; motor servo menggerakkan meja dengan akurasi sub-milimeter yang tersinkronisasi dengan rotasi Gantry.",
                    detailFull = """Meja Pasien (Patient Couch) bukan sekadar tempat tidur biasa, melainkan instrumen mekanik presisi yang menjadi bagian dari geometri optik sinar-X dan penentu koordinat z-axis:

1. Material Daun Meja Carbon Fiber (Serat Karbon): Karena berkas sinar-X berputar 360° mengelilingi pasien, separuh proyeksi sinar-X harus menembus daun meja sebelum atau sesudah melewati tubuh pasien. Oleh sebab itu, daun meja tidak boleh mengandung rangka logam di area pemindaian. Digunakanlah komposit Serat Karbon (Carbon Fiber Reinforced Polymer, Z = 6) yang menggabungkan dua sifat fisik ideal: koefisien atenuasi sinar-X yang sangat rendah dan seragam (tidak menimbulkan streak artifact maupun menyerap dosis) sekaligus kekuatan mekanik super tinggi yang mampu menahan beban pasien 200–300 kg dengan defleksi (melengkung) kurang dari 1–2 mm saat menjorok masuk ke dalam Bore (cantilevered).

2. Sistem Penggerak Vertikal & Longitudinal (Scabbard Drive): Motor hidrolik/elektrik di kaki meja mengatur tinggi vertikal meja menuju Isocenter, sedangkan motor servo stepper di sepanjang rel atas menggerakkan cradle meja masuk-keluar Bore dengan kecepatan presisi tinggi (hingga 150–400 mm/detik pada pemindaian helikal cepat) dan akurasi posisi ±0,25 mm. Jika kalibrasi kecepatan meja meleset, nilai Pitch helikal dan pengukuran jarak anatomi 3D akan terdistorsi.

3. Arsitektur Komputer Ruang Konsol Operator:
• Host Computer (Master Control): Mengendalikan antarmuka pengguna (GUI), pemilihan protokol klinis, pergerakan Gantry/Meja, dan pemicuan generator sinar-X.
• Image Reconstruction System (IRS / Array Processor berbasis GPU): Menerima aliran data mentah (Raw Data / Sinogram) bergigabit per detik dari DAS lalu menjalankan algoritma Filtered Back Projection (FBP), Iterative Reconstruction, atau Deep Learning Recon untuk menghasilkan puluhan citra per detik.
• Penyimpanan Lokal & Jaringan DICOM/PACS/RIS: Menyimpan Raw Data (yang masih bisa direkonstruksi ulang ke Kernel/ketebalan irisan berbeda) serta Image Data (matriks 512×512 dalam format DICOM 12-bit/16-bit) untuk dikirim ke PACS dan printer medis.""",
                    analogi = "Analogi: Daun meja Carbon Fiber adalah jembatan kaca tak terlihat bagi sinar-X namun sekuat baja untuk menopang beban pasien, sedangkan komputer IRS di ruang konsol adalah mesin penerjemah supercepat yang mengubah jutaan angka sandi detektor menjadi gambar anatomi 3D dalam hitungan detik.",
                    prerequisite = "M02.T2.2",
                    crossLinks = "Menghubungkan kecepatan meja ke rumus Helikal Pitch pada M04.T2.2 dan rekonstruksi Raw Data pada M05.",
                    labType = "ct_m02_anatomi_pesawat",
                    sortOrder = 6
                )
            )
        ),

        // =========================================================================
        // MODUL 3: INSTRUMENTASI TABUNG SINAR-X CT, GENERATOR, SLIP-RING & DETEKTOR
        // =========================================================================
        CtModule(
            id = "M03",
            code = "M03",
            title = "Instrumentasi Tabung Sinar-X CT, Generator, Slip-Ring & Detektor",
            subtitle = "Tabung MHU tinggi, Flying Focal Spot, HV Generator HF, Slip-Ring, Detektor Solid-State Gd₂O₂S & DAS",
            description = "Inti rekayasa instrumentasi pesawat CT: desain khusus tabung sinar-X CT beban termal tinggi (MHU, Straton, Liquid Metal Bearing, Flying Focal Spot), generator frekuensi tinggi (HF), mekanisme cincin Slip-Ring pengganti kabel, teknologi detektor Solid-State Scintillator + Photodiode vs Gas Xenon, hingga konversi sinyal analog-ke-digital di dalam modul DAS.",
            levelBadge = "Tingkat 1–3 • Instrumentasi Inti CT",
            iconName = "sensors",
            prerequisites = listOf("M02"),
            estimatedMinutes = 55,
            totalQuizzes = 7,
            badgeName = "Inspektur Instrumentasi Inti CT",
            unlockMessage = "Selamat! Kamu telah menguasai Tabung CT MHU tinggi, Flying Focal Spot, Slip-Ring, Detektor Solid-State, DAS, dan Photon-Counting CT!",
            flashcards = listOf(
                CtFlashcard("M03_F1", "Kapasitas Panas Tabung (MHU) & LMB", "HU = 1,4 x kVp x mA x t; tabung CT 5–8+ MHU memakai Liquid Metal Bearing (Ga-In-Sn) 10.000 RPM yang hening dan konduktif termal."),
                CtFlashcard("M03_F2", "z-Flying Focal Spot (z-FFS)", "Defleksi elektromagnetik berkas elektron pada anoda menggandakan pencuplikan irisan sumbu-z dan menekan artefak windmill."),
                CtFlashcard("M03_F3", "HF HV Generator & Slip-Ring", "Generator HF 10–100 kHz di atas Rotating Frame menerima daya via Low-Voltage Slip-Ring dan mengirim data sinogram via link optik."),
                CtFlashcard("M03_F4", "Detektor Solid-State (GOS), DAS & PCD", "Kristal Gd2O2S (QDE 95–99%) + Photodiode diubah oleh DAS menjadi ln(I0/I); Photon-Counting CdTe menghitung foton secara langsung.")
            ),
            sortOrder = 3,
            nodes = listOf(
                CtNode(
                    code = "M03.T0",
                    level = 0,
                    title = "Tujuan Belajar & Rantai Sinyal Instrumentasi CT",
                    summaryShort = "Menelusuri rantai instrumentasi pesawat CT dari suplai daya Slip-Ring dan HV Generator, pancaran foton Tabung Sinar-X, hingga konversi cahaya-ke-digital pada Detektor dan DAS.",
                    detailFull = """Selamat datang di Modul 3 CT-Learn! Modul ini adalah jantung pemahaman rekayasa instrumentasi pesawat CT Scan. Kita akan membedah bagaimana energi listrik diubah menjadi foton sinar-X, menembus tubuh pasien, lalu diubah menjadi sinyal cahaya, arus listrik lemah, hingga deretan angka biner Sinogram.

Rantai transformasi sinyal pesawat CT yang akan Anda kuasai secara menyeluruh:
1. Energi Listrik (Slip-Ring & High-Frequency HV Generator) ➔ menyuplai tegangan 80–140 kV DC yang stabil ke tabung saat Gantry berputar kontinu.
2. Energi Foton Sinar-X (Tabung Sinar-X CT berkapasitas 5–8 MHU dengan Anoda Putar & Flying Focal Spot) ➔ memancarkan berkas sinar-X intensitas tinggi secara terus-menerus.
3. Energi Cahaya Tampak (Kristal Sintilator Solid-State Gd₂O₂S / UFC) ➔ menyerap foton sinar-X yang lolos dari pasien dan memendarkannya menjadi kilatan cahaya tampak.
4. Sinyal Listrik Analog (Photodiode Silikon) ➔ mengubah intensitas cahaya tampak menjadi arus listrik searah (DC) proporsional berskala picoampere–nanoampere.
5. Data Digital Biner (Modul DAS: Pre-amplifier ➔ Integrator ➔ Logarithmic ADC) ➔ mengirim nilai proyeksi atenuasi digital melalui transmisi optik nirsentuh ke komputer rekonstruksi.""",
                    analogi = "Analogi: Rantai instrumentasi CT bekerja seperti estafet lari 5 pelari: Pelari 1 (Listrik HV) menyerahkan tongkat ke Pelari 2 (Foton Sinar-X), diteruskan ke Pelari 3 (Kilatan Cahaya Sintilator), Pelari 4 (Arus Listrik Photodiode), dan diselesaikan oleh Pelari 5 (Angka Biner DAS) menuju layar monitor.",
                    learningGoal = "Memahami spesifikasi fisik dan alur konversi sinyal dari Slip-Ring, HV Generator, Tabung Sinar-X, Detektor Solid-State, hingga DAS.",
                    crossLinks = "Fondasi langsung bagi M04 (Geometri MSCT), M05 (Sinogram), dan M09 (Air Calibration & Ring Artifact).",
                    labType = "ct_m03_instrumentasi_rantai",
                    sortOrder = 1
                ),
                CtNode(
                    code = "M03.T1.1",
                    level = 1,
                    title = "Desain Tabung Sinar-X CT: Kapasitas Panas MHU, Anoda Putar & Pendingin",
                    summaryShort = "Tabung sinar-X CT harus menahan beban eksposi helikal panjang dengan kapasitas simpan panas 5–8 MHU, anoda putar 10.000 RPM berbantalan logam cair, dan pendingin oli-udara.",
                    detailFull = """Berbeda dari tabung radiografi umum yang hanya memancarkan sinar-X selama sekian milidetik, tabung sinar-X pesawat CT harus memancarkan sinar-X pada arus tinggi (200–800 mA) secara kontinu selama 5 hingga 60 detik sambil ikut berputar di atas Gantry dengan beban gaya sentrifugal hingga 15G–30G. Oleh karena itu, tabung sinar-X CT memiliki konstruksi rekayasa tingkat lanjut:

1. Kapasitas Simpan Panas Tinggi (Heat Storage Capacity dalam MHU): Satuan kapasitas termal anoda dinyatakan dalam Mega Heat Unit (MHU, di mana 1 HU = 0,71 Joule untuk generator DC/3-fasa). Jika tabung Rontgen konvensional hanya berkapasitas 0,3–0,5 MHU, tabung CT modern memiliki kapasitas 5 hingga 8 MHU (atau setara laju disipasi panas sangat tinggi pada desain tabung berpendingin langsung seperti Straton/Vectron yang memutar seluruh selubung tabung di dalam oli pendingin).

Rumus: Heat Units (HU) = 1,4 × kVp × mA × Waktu_Scan (detik)

2. Piringan Anoda Putar Berdiameter Besar (15–20 cm) & Liquid Metal Bearing (LMB): Piringan anoda dibuat dari grafit tebal berlapis trek target paduan Tungsten-Rhenium (90% W / 10% Re) agar tahan retak termal, dan diputar oleh motor induksi pada kecepatan tinggi 8.000–10.000 RPM guna menyebarkan panas tumbukan elektron ke sepanjang keliling trek anoda. Sebagai pengganti bantalan bola baja biasa yang cepat aus dan berisik, tabung CT modern menggunakan bantalan luncur logam cair (Liquid Metal Bearing berbahan paduan Gallium-Indium-Tin) yang hening, bebas aus mekanis, dan mampu menghantarkan panas keluar secara langsung (konduksi).

3. Sistem Pendingin Oli-ke-Udara (Oil-to-Air Heat Exchanger): Selubung tabung (tube housing) direndam oli dielektrik bersirkulasi yang dipompa menuju radiator penukar panas di dalam Gantry untuk membuang panas dengan laju mencapai 800–1.500 kHU/menit.""",
                    analogi = "Analogi: Jika tabung Rontgen biasa seperti lampu blitz kamera yang hanya menyala sekejap, maka tabung sinar-X CT adalah lampu sorot stadion berdaya raksasa yang harus menyala terus-menerus tanpa meleleh—sehingga targetnya harus terus diputar kencang di atas bantalan logam cair sambil disiram sirkulasi oli pendingin.",
                    prerequisite = "M03.T0",
                    crossLinks = "Mendasari prosedur wajib Pemanasan Tabung (Tube Warm-Up) pagi hari pada M09.T2.1.",
                    labType = "ct_m03_instrumentasi_rantai",
                    sortOrder = 2
                ),
                CtNode(
                    code = "M03.T1.2",
                    level = 1,
                    title = "Fokus Ganda (Dual Filament) & Teknologi Flying Focal Spot (z-FFS)",
                    summaryShort = "Tabung CT memiliki filamen fokus kecil (0,5–0,7 mm) untuk resolusi spasial tinggi dan fokus besar (1,0–1,2 mm) untuk beban mA tinggi, ditambah defleksi elektromagnetik Flying Focal Spot.",
                    detailFull = """Ukuran titik tumbukan elektron pada anoda yang disebut Focal Spot menentukan ketajaman geometris (resolusi spasial) citra CT sekaligus membatasi arus tabung maksimum (mA) yang diizinkan agar trek anoda tidak meleleh:

1. Dual Filament (Fokus Kecil vs Fokus Besar): Katoda tabung CT dilengkapi dua kumparan filamen Tungsten di dalam Focusing Cup bermuatan negatif:
• Fokus Kecil (Small Focal Spot, ~0,5–0,7 mm): Digunakan saat membutuhkan ketajaman tepi sangat tinggi (misalnya CT Tulang Temporal/Inner Ear, HRCT Paru, atau CT Koroner) karena meminimalkan kabur tepi geometris (geometric unsharpness/penumbra).
• Fokus Besar (Large Focal Spot, ~1,0–1,2 mm): Digunakan saat membutuhkan arus tabung (mA) sangat besar pada pemindaian abdomen/pelvis pasien bertubuh besar agar panas tersebar di area yang lebih luas.

2. Prinsip Line-Focus (Sudut Target Anoda 7°–12°): Permukaan anoda dimiringkan dengan sudut kecil (7°–12°) sehingga area tumbukan elektron sebenarnya (Actual Focal Spot) cukup luas untuk menahan panas, namun proyeksi bayangannya ke arah pasien (Effective Focal Spot) tampak jauh lebih kecil dan tajam.

3. Teknologi Flying Focal Spot (FFS / z-Sharp): Pada pesawat MSCT modern, kumparan defleksi elektromagnetik di leher tabung membelokkan berkas elektron secara bergantian dengan sangat cepat (ribuan kali per detik) antara dua titik fokus yang sedikit bergeser pada sumbu z (z-Flying Focal Spot) maupun sumbu x-y. Hasilnya, setiap baris detektor memperoleh dua pengukuran proyeksi yang saling menyisip (double sampling / interleaved), menggandakan resolusi spasial longitudinal dan menghilangkan artefak kincir angin (windmill artifact) tanpa perlu memperkecil ukuran fisik elemen detektor.""",
                    analogi = "Analogi: Dual Filament seperti memilih ujung pena: spidol ujung halus (Small Focus) untuk menggambar detail arsitektur tulang telinga yang rumit, dan kuas lebar (Large Focus) untuk mengecat bidang abdomen yang luas dengan cepat. Flying Focal Spot seperti menggeser posisi kamera setengah piksel secara kilat untuk menghasilkan foto beresolusi dua kali lipat.",
                    prerequisite = "M03.T1.1",
                    crossLinks = "Berkaitan dengan Resolusi Spasial pada M07.T1.2.",
                    labType = "ct_m03_instrumentasi_rantai",
                    sortOrder = 3
                ),
                CtNode(
                    code = "M03.T2.1",
                    level = 2,
                    title = "High-Frequency HV Generator & Revolusi Mekanisme Slip-Ring",
                    summaryShort = "Generator Frekuensi Tinggi (HF) berukuran kompak dipasang langsung di atas Rotating Frame dan menerima daya listrik kontinu melalui cincin sikat konduktif Slip-Ring tanpa kabel terbelit.",
                    detailFull = """Pada pesawat CT generasi lama (sebelum akhir 1980-an), tabung sinar-X dihubungkan ke generator tegangan tinggi di lantai menggunakan kabel tegangan tinggi yang tebal dan kaku. Akibatnya, setelah Gantry berputar 360° searah jarum jam, Gantry harus berhenti, mengerem, lalu berputar balik 360° berlawanan arah jarum jam agar kabel tidak putus terbelit ('stop-and-shoot'). Waktu jeda antar-irisan sangat lama dan tidak mungkin melakukan pemindaian spiral/helikal.

Terobosan rekayasa yang melahirkan CT Helikal/Spiral modern adalah kombinasi **Generator Frekuensi Tinggi (High-Frequency / HF Inverter Generator)** dan **Teknologi Slip-Ring**:

1. High-Frequency (HF) HV Generator di Atas Rotating Frame:
Arus bolak-balik (AC) jala-jala 50/60 Hz disearahkan menjadi DC, lalu diubah oleh inverter solid-state menjadi frekuensi tinggi (10–100 kHz) sebelum dinaikkan tegangannya oleh transformator berinti ferit hingga 80–140 kV dan disearahkan kembali. Keunggulan frekuensi tinggi membuat ukuran transformator menyusut drastis (cukup ringan dan kompak untuk dipasang ikut berputar di atas Rotating Frame Gantry!), sehingga tidak diperlukan lagi kabel tegangan tinggi panjang dari luar Gantry. Selain itu, voltage ripple (fluktuasi tegangan) menjadi sangat kecil (< 1%), menghasilkan keluaran foton sinar-X yang sangat stabil dan efisien.

2. Mekanisme Cincin Slip-Ring (Low-Voltage / High-Voltage Slip-Ring):
Slip-Ring adalah sekumpulan cincin konduktor melingkar (berbahan paduan perak/tembaga) yang dipasang konsentris di sepanjang keliling rangka Gantry, berpasangan dengan sikat kontak geser (sliding contact brushes berbahan komposit perak-grafit) yang menyalurkan daya listrik dari bagian Gantry yang diam ke Rotating Frame yang berputar 360° terus-menerus tanpa kabel.
• Untuk penyaluran daya listrik ke HF Generator dan motor anoda digunakan sikat kontak geser konduktif (Low-Voltage Slip-Ring ~400–600 V).
• Untuk pengiriman data digital Sinogram berkecepatan tinggi (puluhan Gbps) dari DAS ke komputer konsol digunakan transmisi optik nirsentuh (laser/inframerah) atau kopling kapasitif frekuensi radio pada cincin Slip-Ring agar bebas dari derau percikan mekanis (electrical contact noise).""",
                    analogi = "Analogi: Pesawat CT generasi kabel lama seperti komidi putar yang diikat kabel colokan listrik dari tanah—baru berputar satu putaran, harus berhenti dan putar balik agar kabelnya tidak kusut. Slip-Ring bekerja seperti rel listrik melingkar pada kereta bumper car atau roda bianglala modern: sikat penghantar menyentuh cincin logam sambil meluncur mulus, sehingga Gantry bebas berputar ribuan kali searah jarum jam tanpa henti!",
                    prerequisite = "M03.T1.2",
                    crossLinks = "Fondasi langsung bagi akuisisi Helikal (Spiral) dan Pitch pada M04.T2.2.",
                    labType = "ct_m03_instrumentasi_rantai",
                    sortOrder = 4
                ),
                CtNode(
                    code = "M03.T2.2",
                    level = 2,
                    title = "Arsitektur Detektor CT: Gas Xenon vs Solid-State Scintillator + Photodiode",
                    summaryShort = "Seluruh pesawat MSCT modern menggunakan detektor Solid-State (kristal sintilator Gd₂O₂S/UFC + Photodiode silikon) dengan efisiensi penyerapan 95–99% dan afterglow nyaris nol.",
                    detailFull = """Detektor CT adalah 'mata' pesawat CT yang bertugas menangkap foton sinar-X yang berhasil menembus tubuh pasien dan mengubahnya menjadi sinyal listrik yang berbanding lurus dengan intensitas sinar-X. Dalam sejarah instrumentasi CT, terdapat dua jenis teknologi detektor utama:

1. Detektor Ionisasi Gas Xenon Bertekanan Tinggi (Generasi Lama / Single-Slice CT Klasik):
Berupa tabung bilik logam panjang yang diisi gas mulia Xenon (Xe) bertekanan tinggi (20–30 atm) dengan sekat-sekat elektroda Tungsten. Saat foton sinar-X masuk, gas Xenon terionisasi menjadi pasangan ion positif dan elektron yang ditarik ke elektroda menjadi arus listrik.
• Keunggulan: Respons antar-kanal sangat seragam karena gas mengalir merata di seluruh bilik, sehingga jarang mengalami Ring Artifact.
• Kelemahan Fatal: Karena kerapatan gas rendah, efisiensi penyerapan sinar-X (Quantum Detection Efficiency / QDE) hanya sekitar 45–60% (memerlukan ruang bilik yang dalam sehingga sinar harus masuk searah sekat). Selain itu, sekat depannya yang tebal membuat detektor gas Xenon **tidak mungkin dipotong-potong menjadi matriks multi-baris 2D (Multi-Slice CT / MSCT)**.

2. Detektor Solid-State Scintillator + Photodiode (Standar Mutlak Seluruh Pesawat MSCT Modern):
Setiap elemen piksel detektor (ukuran ~0,5–1,0 mm) terdiri atas dua lapisan padat yang direkatkan secara presisi (konversi tak langsung 2 tahap):
• Lapisan Atas — Kristal Sintilator Keramik Padat: Menggunakan material Gadolinium Oxysulfide (Gd₂O₂S / GOS), Ultra-Fast Ceramics (UFC), atau Gemstone/Garnet. Saat foton sinar-X diserap oleh kristal ini, energinya diubah menjadi pancaran kilatan cahaya tampak (visible light photons). Kristal lama seperti Cadmium Tungstate (CdWO₄) atau CsI memiliki kelemahan *afterglow* (cahaya masih berpendar lama setelah sinar-X mati), sedangkan Gd₂O₂S/UFC memiliki waktu luruh sangat cepat (beberapa mikrodetik, *afterglow* < 0,01%) sehingga mampu mengimbangi rotasi Gantry 0,25 detik.
• Lapisan Bawah — Silicon Photodiode (Fotodioda Semikonduktor): Menempel tepat di dasar kristal sintilator untuk menangkap kilatan cahaya tampak dan mengubahnya menjadi arus listrik searah (DC) yang sangat proporsional.
• Keunggulan Mutlak Solid-State: Memiliki kerapatan atom sangat tinggi sehingga efisiensi penyerapan foton (QDE & Geometric Efficiency) mencapai **90–99%** (dosis pasien jauh lebih hemat!) dan dapat dicetak menjadi matriks ubin 2D sebanyak 16, 64, 128, hingga 320 baris untuk MSCT.""",
                    analogi = "Analogi: Detektor Gas Xenon seperti jaring ikan berlubang renggang yang hanya mampu menangkap separuh ikan (foton) yang lewat dan tidak bisa disusun bertingkat. Detektor Solid-State seperti susunan ubin panel surya berlapis kristal pendar superpadat: begitu foton sinar-X menyentuh kristal sintilator, kristal langsung berkedip menjadi cahaya tampak dan fotodioda di bawahnya langsung mengubah kedipan itu menjadi arus listrik dengan efisiensi hampir 100%!",
                    prerequisite = "M03.T2.1",
                    crossLinks = "Ketidakseragaman gain antar-elemen Solid-State dapat memicu Ring Artifact yang dibahas pada M09.T1.2.",
                    labType = "ct_m03_instrumentasi_rantai",
                    sortOrder = 5
                ),
                CtNode(
                    code = "M03.T3.1",
                    level = 3,
                    title = "Data Acquisition System (DAS): Pre-Amplifier, Integrator & Logarithmic ADC",
                    summaryShort = "Modul DAS yang menempel di belakang busur detektor mengintegrasikan arus lemah Photodiode tiap sudut sampling (view), memperkuatnya, dan mengubahnya menjadi angka biner logaritmik ln(I₀/I).",
                    detailFull = """Arus listrik yang dihasilkan oleh setiap elemen Silicon Photodiode ketika menerima cahaya sintilator nilainya sangat kecil—hanya berkisar pada orde picoampere (10⁻¹² A) hingga nanoampere (10⁻⁹ A)—serta memiliki rentang dinamis (dynamic range) yang luar biasa lebar hingga 1.000.000 : 1 (antara sinar-X yang langsung lewat udara di samping pasien vs sinar-X yang menembus panggul tebal). Jika sinyal analog selemah ini dikirim keluar Gantry melalui kabel panjang, sinyal akan hancur tertimpa derau elektronik (electronic noise).

Oleh sebab itu, tepat di punggung busur Detector Array di dalam Rotating Frame dipasang papan sirkuit elektronik berkecepatan tinggi yang disebut **DAS (Data Acquisition System)**. Dalam satu putaran Gantry 360°, DAS melakukan pencuplikan pembacaan (sampling) sebanyak **1.000 hingga 2.400 kali per putaran** (disebut *views* atau *projections*). Pada setiap *view*, DAS menjalankan tiga tahap pemrosesan sinyal:

Rumus: I_photodiode (pA–nA) ➔ [Pre-Amp & Integrator] ➔ Tegangan V ➔ [Log Amplifier & ADC 16–24 bit] ➔ P = ln(I₀ / I)

1. Low-Noise Pre-Amplifier & Current Integrator: Mengumpulkan (mengintegrasikan) muatan arus listrik dari setiap kanal fotodioda selama interval waktu satu sudut pandang (misalnya 0,25 milidetik per view) dan mengubahnya menjadi sinyal tegangan analog yang bersih dan teramplifikasi.
2. Logarithmic Amplification & Analog-to-Digital Converter (ADC): Karena Hukum Beer-Lambert membutuhkan nilai ln(I₀ / I) = ∑ μ·Δx, sinyal diperkuat secara logaritmik dan dikonversi oleh ADC berpresisi tinggi (16-bit hingga 24-bit) menjadi bilangan bulat biner digital.
3. Multiplexer & Optical Transmitter: Menggabungkan data biner dari puluhan ribu kanal detektor (misal 800 kanal × 64 baris × 1.200 view/detik) menjadi satu aliran data serial berkecepatan multi-Gigabit per detik yang dipancarkan melewati Slip-Ring optik menuju Image Reconstruction System (IRS) di ruang konsol.""",
                    analogi = "Analogi: Photodioda berbicara dengan bisikan yang sangat pelan (arus picoampere). Modul DAS yang menempel tepat di belakangnya bertindak sebagai mikrofon penguat suara (Pre-amp), pencatat kalimat per milidetik (Integrator), sekaligus penerjemah ke bahasa kode angka komputer (Logarithmic ADC) sebelum dikirim lewat serat cahaya ke ruang operator.",
                    prerequisite = "M03.T2.2",
                    crossLinks = "Data keluaran DAS inilah yang disusun menjadi matriks Sinogram pada M05.T1.1.",
                    labType = "ct_m03_instrumentasi_rantai",
                    sortOrder = 6
                ),
                CtNode(
                    code = "M03.T3.2",
                    level = 3,
                    title = "Inovasi Instrumentasi Lanjut: Dual-Energy CT & Photon-Counting Detector (PCD)",
                    summaryShort = "Dual-Energy CT membedakan komposisi material menggunakan dua spektrum energi (80 & 140 kVp), sedangkan Photon-Counting Detector (CdTe) menghitung foton satu per satu secara langsung tanpa sintilator.",
                    detailFull = """Perkembangan mutakhir rekayasa instrumentasi pesawat CT melampaui detektor pengintegrasi energi (Energy-Integrating Detector / EID) konvensional menuju pencitraan spektral kuantitatif:

1. Instrumentasi Dual-Energy CT (DECT / Spectral CT):
Pada CT konvensional satu energi (misal 120 kVp), kalsifikasi plak pembuluh darah dan kontras Iodin di lumen arteri bisa memiliki nilai HU yang persis sama (misal +350 HU) meskipun nomor atomnya berbeda jauh (Kalsium Z=20 vs Iodin Z=53). Dual-Energy CT mengakuisisi data menggunakan dua spektrum energi sinar-X berbeda (umumnya 80 kVp dan 140 kVp) melalui salah satu dari tiga arsitektur perangkat keras:
• Dual-Source CT (DSCT): Dua tabung sinar-X dan dua busur detektor dipasang bersudut ~90° di dalam satu Gantry (Tabung A beroperasi pada 80 kVp, Tabung B pada 140 kVp dengan filter Timah/Sn).
• Fast kVp-Switching: Satu tabung sinar-X dan generator ultra-cepat yang berganti tegangan antara 80 kVp dan 140 kVp setiap < 0,25 milidetik secara bergantian.
• Dual-Layer Detector (Sandwich Detector): Satu tabung 120 kVp dipadukan dengan detektor berlapis dua: lapisan atas (Yttrium-based scintillator) menyerap foton energi rendah, sedangkan lapisan bawah (Gd₂O₂S) menyerap foton energi tinggi.
Dengan membandingkan rasio atenuasi pada 80 vs 140 kVp, komputer dapat membuat citra Virtual Non-Contrast (menghapus Iodin secara digital), peta perfusi Iodin, dan membedakan batu asam urat vs batu kalsium ginjal.

2. Photon-Counting Detector (PCD-CT):
Berbeda dengan detektor Solid-State konvensional yang mengubah sinar-X menjadi cahaya terlebih dahulu (tak langsung) dan menjumlahkan seluruh foton sekaligus, PCD menggunakan kristal semikonduktor konversi langsung (Cadmium Telluride / CdTe atau CZT) yang diberi tegangan bias tinggi. Setiap satu foton sinar-X yang masuk langsung menghasilkan satu pulsa awan muatan listrik yang dihitung satu per satu dan diukur energinya ke dalam beberapa ambang energi (energy bins). Hasilnya: derau elektronik (electronic noise) tereliminasi total, ukuran piksel detektor menyusut hingga 0,15–0,2 mm (resolusi spasial ultra-tinggi), dan informasi spektral multi-energi selalu tersedia di setiap pemindaian!""",
                    analogi = "Analogi: Detektor CT biasa (Energy-Integrating) seperti menimbang seember koin campuran sekaligus—kita hanya tahu berat totalnya tanpa tahu berapa keping koin emas, perak, atau perunggu di dalamnya. Photon-Counting Detector (PCD) seperti mesin penghitung koin digital berkecepatan kilat yang menghitung setiap keping koin satu per satu sekaligus memilah nilainya!",
                    prerequisite = "M03.T3.1",
                    crossLinks = "Terkait dengan aplikasi vaskular kontras Iodin pada M08 dan pengurangan artefak logam pada M09.",
                    labType = "ct_m03_instrumentasi_rantai",
                    sortOrder = 7
                )
            )
        ),

        // =========================================================================
        // MODUL 4: EVOLUSI GENERASI PESAWAT CT, GEOMETRI MSCT & AKUISISI HELIKAL (PITCH)
        // =========================================================================
        CtModule(
            id = "M04",
            code = "M04",
            title = "Evolusi Generasi Pesawat CT, Geometri MSCT & Akuisisi Helikal (Pitch)",
            subtitle = "Generasi I–IV, EBCT, Dual-Source, Array Detektor MSCT (Matrix vs Adaptive), Mode Aksial vs Helikal & Rumus Pitch",
            description = "Perjalanan arsitektur geometri berkas dan detektor dari Generasi ke-1 (Pencil Beam Translate-Rotate) hingga Generasi ke-3 (Rotate-Rotate Wide Fan/Cone Beam) yang mendominasi rumah sakit saat ini, geometri matriks detektor Multi-Slice CT (16–320 slice), serta fisika kuantitatif akuisisi Helikal/Spiral dan faktor Pitch.",
            levelBadge = "Tingkat 1–3 • Geometri & Helikal",
            iconName = "route",
            prerequisites = listOf("M03"),
            estimatedMinutes = 50,
            totalQuizzes = 6,
            badgeName = "Ahli Geometri MSCT & Helikal",
            unlockMessage = "Selamat! Kamu telah menguasai Generasi CT, Konfigurasi Baris Detektor MSCT, ECG-Gating, dan Perhitungan Helikal Pitch!",
            flashcards = listOf(
                CtFlashcard("M04_F1", "CT Generasi Ke-3 (Rotate-Rotate)", "Tabung dan busur detektor berputar sinkron 360 derajat; unggul karena mendukung sekat Tungsten anti-scatter terfokus dan matriks MSCT."),
                CtFlashcard("M04_F2", "Dual-Source CT & ECG-Gating", "2 tabung bersudut 90 derajat memangkas rotasi jantung menjadi seperempat putaran (~66 ms); Prospective ECG-Gating memicu sinar-X hanya pada fase diastolik tenang (70–75% R-R)."),
                CtFlashcard("M04_F3", "Total Kolimasi MSCT (W = N x T)", "Ketebalan irisan minimum MSCT ditentukan oleh lebar fisik satu baris detektor (T); W_total = Jumlah baris aktif (N) x lebar baris (T)."),
                CtFlashcard("M04_F4", "Helical Beam Pitch & Over-ranging", "Pitch = Table Feed per rotasi (d) / (N x T); Over-ranging adalah tambahan setengah putaran spiral di ujung scan untuk interpolasi z-axis yang ditangkal oleh Adaptive Dose Shield.")
            ),
            sortOrder = 4,
            nodes = listOf(
                CtNode(
                    code = "M04.T0",
                    level = 0,
                    title = "Tujuan Belajar & Evolusi Geometri Pemindaian CT",
                    summaryShort = "Memahami evolusi mekanik Generasi 1–4 pesawat CT, arsitektur susunan baris detektor Multi-Slice CT (MSCT), dan perhitungan matematis Pitch Helikal.",
                    detailFull = """Selamat datang di Modul 4 CT-Learn! Bagaimana pesawat CT yang pada tahun 1971 membutuhkan waktu 5 menit hanya untuk memindai satu irisan otak kini mampu memindai seluruh jantung dan toraks-abdomen hanya dalam hitungan detik? Jawabannya terletak pada evolusi geometri berkas sinar-X, susunan detektor 2D (MSCT), dan lintasan akuisisi Helikal.

Pada modul ini Anda akan mempelajari:
1. Karakteristik geometri dan gerakan mekanis Generasi ke-1 (Pencil Beam Translate-Rotate), Generasi ke-2 (Narrow Fan Beam Translate-Rotate), Generasi ke-3 (Wide Fan Beam Rotate-Rotate), dan Generasi ke-4 (Rotate-Stationary Ring Detector), serta alasan mengapa **Generasi ke-3** menjadi pemenang desain seluruh MSCT modern.
2. Perkembangan khusus Electron Beam CT (EBCT), Dual-Source CT (DSCT), dan Wide-Area Cone-Beam 320-slice CT.
3. Perbedaan fisik susunan detektor Single-Slice CT (SSCT) vs Multi-Slice CT (MSCT): Matrix Array (simetris) vs Adaptive/Hybrid Array, serta rumus Total Lebar Kolimasi Berkas (N × T).
4. Perbandingan mode akuisisi Axial Step-and-Shoot (Sequential) vs Helical/Spiral Volume Scanning.
5. Perhitungan kuantitatif Beam Pitch (Pitch = Pergerakan Meja per Rotasi / (N × T)) dan trade-off antara Pitch < 1, Pitch = 1, dan Pitch > 1 terhadap kecepatan scan, z-axis blurring (SSP), dan dosis radiasi.""",
                    analogi = "Analogi: Evolusi generasi CT seperti mengupas apel: Generasi 1 memotong apel titik demi titik dengan pisau kecil (sangat lambat), Generasi 3 memutar pisau kipas lebar mengelilingi apel sekaligus, dan Helikal MSCT mengupas kulit apel secara spiral tanpa putus dari ujung atas sampai bawah dengan 64 mata pisau sejajar!",
                    learningGoal = "Mampu membedakan generasi pesawat CT, menghitung total kolimasi detektor MSCT, dan menghitung parameter Helikal Pitch secara akurat.",
                    crossLinks = "Terhubung langsung ke Simulator 3 (Geometri MSCT & Helikal Pitch) dan M07 (mAs Efektif & CTDIvol).",
                    labType = "ct_m04_generasi_pitch",
                    sortOrder = 1
                ),
                CtNode(
                    code = "M04.T1.1",
                    level = 1,
                    title = "Generasi Pesawat CT I s.d. IV & Mengapa Generasi Ke-3 Mendominasi",
                    summaryShort = "Generasi 1 & 2 menggunakan gerakan Translate-Rotate yang lambat; Generasi 3 (Rotate-Rotate) mendominasi seluruh MSCT modern karena tabung dan busur detektor berputar bersama sehingga mudah dikolimasi dan hemat biaya detektor.",
                    detailFull = """Sejak Sir Godfrey Hounsfield dan Allan Cormack memperkenalkan pemindai CT klinis pertama (EMI Mark I, 1971), geometri pasangan tabung sinar-X dan detektor berkembang melalui empat generasi klasik:

1. Generasi Pertama (Translate-Rotate, Pencil Beam):
Menggunakan **satu berkas sinar-X sempit seperti pensil (pencil beam)** dan **hanya 1 atau 2 detektor** (kristal NaI). Tabung dan detektor bergerak bergeser lurus (translasi) melintasi kepala pasien untuk merekam 160 titik, lalu berputar (rotasi) 1°, kemudian bergeser lurus lagi hingga 180°. Waktu akuisisi 1 irisan mencapai 4,5–5 menit dan hanya dapat memindai kepala yang difiksasi kantong air.

2. Generasi Kedua (Translate-Rotate, Narrow Fan Beam ~10°):
Menggunakan berkas kipas sempit (narrow fan beam) dengan deretan 10–30 detektor. Karena sudut kipas lebih lebar, langkah rotasi tiap translasi bisa diperbesar (misal 5°–10° per langkah), memangkas waktu pindai menjadi 18–90 detik per irisan, namun masih terhambat inersia mekanik gerak maju-mundur (translasi).

3. Generasi Ketiga (Rotate-Rotate, Wide Fan Beam 45°–60°):
Menghilangkan gerak translasi sepenuhnya! Berkas kipas diperlebar hingga mencakup seluruh diameter tubuh pasien (FOV 50 cm) dan ditangkap oleh busur lengkung yang memuat **700 hingga 1.000 elemen detektor**. Tabung sinar-X dan busur detektor dipasang saling berhadapan pada Rotating Frame dan **berputar bersama-sama 360° (Rotate-Rotate)** dalam waktu < 0,3–1,0 detik.

4. Generasi Keempat (Rotate-Stationary, Full 360° Detector Ring):
Cincin detektor berjumlah 4.000–5.000 elemen dipasang **diam (stationary)** melingkari seluruh 360° Gantry, sedangkan hanya tabung sinar-X yang berputar di dalamnya. Desain ini bebas dari Ring Artifact karena tiap detektor dapat terkalibrasi udara terus-menerus.

**Mengapa Generasi Ke-3 (bukan Generasi Ke-4) yang Mendominasi Seluruh Pesawat MSCT Modern?**
Ada dua alasan rekayasa yang menentukan:
• Efektivitas Anti-Scatter Grid: Pada Generasi ke-3, posisi detektor terhadap tabung sinar-X selalu tetap, sehingga sekat kolimator pasca-pasien (anti-scatter septa Tungsten) dapat diarahkan fokus tepat ke titik fokus tabung untuk menolak radiasi hambur secara sangat efektif. Pada Generasi ke-4, karena tabung terus berpindah sudut terhadap detektor yang diam, sekat tetap tidak bisa digunakan.
• Biaya & Kelayakan Matriks Multi-Slice: Membangun 64 hingga 320 baris detektor sepanjang busur 60° (Generasi ke-3) jauh lebih ekonomis dan praktis dibandingkan harus membangun 360° cincin penuh (Generasi ke-4) yang memerlukan ratusan ribu kristal detektor.""",
                    analogi = "Analogi: Generasi 1 seperti menyapu lantai dengan satu batang lidi sambil melangkah bergeser satu per satu. Generasi 3 seperti menyapu dengan sapu kipas lebar yang langsung mencakup seluruh lebar lorong dalam sekali putar bersama pengkinya!",
                    prerequisite = "M04.T0",
                    crossLinks = "Kelemahan khas geometri Rotate-Rotate Generasi ke-3 adalah potensi Ring Artifact jika satu kanal detektor bermasalah (dibahas di M09.T1.2).",
                    labType = "ct_m04_generasi_pitch",
                    sortOrder = 2
                ),
                CtNode(
                    code = "M04.T1.2",
                    level = 1,
                    title = "Arsitektur Khusus: EBCT (Generasi Ke-5), Dual-Source CT & Wide-Detector CT",
                    summaryShort = "EBCT menggunakan belokan berkas elektron tanpa bagian mekanik berputar; kini digantikan oleh Dual-Source CT (2 tabung bersudut 90°) dan Wide-Detector 320-slice CT untuk pencitraan jantung.",
                    detailFull = """Untuk menangkap organ yang bergerak sangat cepat seperti denyut jantung (arteri koroner), para insinyur mengembangkan beberapa arsitektur pesawat CT khusus:

1. Electron Beam CT (EBCT / Ultrafast CT, sering dijuluki Generasi Ke-5):
Tidak memiliki tabung sinar-X maupun detektor yang berputar secara mekanis! Sebagai gantinya, Gantry berbentuk tabung vakum raksasa tempat meriam elektron (electron gun) menembakkan berkas elektron yang dibelokkan secara elektromagnetik oleh kumparan fokus menyapu 4 cincin target anoda Tungsten berbentuk setengah lingkaran (210°) di bawah pasien. Waktu sapuan mencapai 50 milidetik per irisan. Meskipun sangat cepat untuk skor kalsium koroner pada zamannya, EBCT kini tidak diproduksi lagi karena rasio sinyal-terhadap-derau (SNR) rendah, biaya tinggi, dan tidak mendukung akuisisi volume MSCT serbaguna.

2. Dual-Source CT (DSCT):
Memasang **dua tabung sinar-X dan dua busur detektor MSCT** yang saling tegak lurus (bersudut ~90° atau 95°) di atas satu Rotating Frame Generasi ke-3. Karena kedua tabung bekerja serentak, untuk mengumpulkan data proyeksi 180° yang dibutuhkan guna merekonstruksi satu citra jantung, Gantry cukup berputar **seperempat putaran saja (90°)**! Hasilnya, resolusi temporal melonjak dua kali lipat mencapai ~66 milidetik—mampu membekukan gerak arteri koroner tanpa artefak gerak meski detak jantung pasien tinggi, sekaligus memungkinkan pencitraan Dual-Energy (80 kVp & 140 kVp).

3. Wide-Area Cone-Beam CT (256–320 Detector Rows):
Memiliki busur detektor dengan lebar cakupan z-axis mencapai **16 cm** (320 baris × 0,5 mm) dalam satu putaran tunggal. Seluruh organ jantung atau seluruh volume otak (Whole-Brain CT Perfusion) dapat dipindai tuntas dalam satu rotasi Gantry tunggal (0,275 detik) tanpa menggerakkan meja pasien sama sekali!

4. Sinkronisasi EKG pada CT Jantung (Prospective vs Retrospective ECG-Gating):
• Prospective ECG-Triggering (Step-and-Shoot): Sinar-X hanya dinyalakan pada fase jantung paling tenang (mid-diastole, sekitar 70–75% interval gelombang R-R EKG), sehingga menghemat dosis radiasi hingga 70–80% pada pasien dengan irama jantung teratur (< 65 bpm).
• Retrospective ECG-Gating (Helical Pitch Rendah 0,2–0,3): Sinar-X menyala kontinu sepanjang siklus R-R dengan modulasi arus ECG-pulsing; memungkinkan evaluasi fungsi gerak katup dan fraksi ejeksi ventrikel namun dengan dosis lebih tinggi.""",
                    analogi = "Analogi: Jika memotret bola yang berputar dengan satu kamera butuh memutar kamera 180 derajat, maka Dual-Source CT memasang dua kamera pada sudut 90 derajat yang memotret bersamaan—cukup memutar 90 derajat, seluruh sisi bola sudah terekam dalam separuh waktu!",
                    prerequisite = "M04.T1.1",
                    crossLinks = "Terkait dengan aplikasi Dual-Energy di M03.T3.2 dan CT Angiografi Koroner di M08.",
                    labType = "ct_m04_generasi_pitch",
                    sortOrder = 3
                ),
                CtNode(
                    code = "M04.T2.1",
                    level = 2,
                    title = "Geometri Detektor Single-Slice (SSCT) vs Multi-Slice CT (MSCT / MDCT)",
                    summaryShort = "Pada SSCT tebal irisan ditentukan oleh bukaan kolimator pra-pasien; pada MSCT tebal irisan minimum ditentukan oleh lebar fisik satu baris elemen detektor (T) dan konfigurasi penggabungan kanal DAS.",
                    detailFull = """Perbedaan paling mendasar antara pesawat Single-Slice CT (SSCT) dan Multi-Slice / Multi-Detector Row CT (MSCT / MDCT) terletak pada bagaimana sumbu longitudinal (z-axis) dibagi di atas busur detektor:

1. Single-Slice CT (SSCT):
Busur detektor hanya memiliki **1 baris tunggal** sepanjang sumbu z (misalnya lebar detektor 10–20 mm). Jika radiografer ingin membuat irisan tipis 2 mm, maka daun Kolimator Pra-Pasien ditutup rapat hingga hanya menyisakan celah 2 mm. Artinya, pada SSCT **ketebalan irisan (slice thickness) ditentukan langsung oleh lebar bukaan kolimator**.

2. Multi-Slice CT (MSCT — 4, 16, 64, 128, hingga 320 Baris):
Satu baris detektor yang lebar tadi dipotong-potong secara fisik sepanjang sumbu z menjadi banyak baris elemen detektor kecil (misalnya N = 64 baris dengan lebar tiap baris T = 0,5 mm atau 0,625 mm). Pada MSCT, **ketebalan irisan minimum ditentukan oleh lebar fisik elemen detektor terkecil (T)** dan bagaimana saklar multiplexer elektronik di dalam DAS menggabungkan (binning) sinyal antar-baris tersebut.

Rumus: Total Lebar Kolimasi Berkas (W_total) = N × T

Contoh pada pesawat CT 64-slice dengan lebar elemen detektor T = 0,625 mm:
• Total Lebar Kolimasi Berkas (Beam Collimation Width) = 64 × 0,625 mm = 40 mm per satu putaran Gantry.
• Dengan satu akuisisi data mentah 64 × 0,625 mm ini, komputer dapat merekonstruksi irisan tipis 0,625 mm (untuk rekonstruksi 3D isotropik) sekaligus menggabungkan tiap 8 baris menjadi irisan tebal 5,0 mm (untuk pembacaan klinis rutin dengan noise rendah) **tanpa perlu memindai ulang pasien**!

Terdapat dua desain tata letak baris detektor MSCT:
• Matrix (Fixed/Symmetrical) Array: Seluruh baris detektor memiliki lebar z-axis yang sama persis (misal 64 baris × 0,625 mm).
• Adaptive / Hybrid Array: Baris-baris di bagian tengah dibuat sangat tipis (misal 0,5 mm atau 0,625 mm) untuk resolusi tinggi, sedangkan baris di bagian tepi luar dibuat lebih lebar (misal 1,0 mm atau 1,25 mm) untuk mengurangi jumlah sekat mati (dead space septa) dan memperluas cakupan volume.""",
                    analogi = "Analogi: SSCT seperti mencetak kue lapis satu demi satu dengan satu cetakan yang lebarnya harus diatur ulang tiap kali. MSCT seperti cetakan kue berisi 64 sekat tipis 0,625 mm sekaligus: sekali panggang (sekali putar Gantry), Anda langsung mendapat 64 iris tipis, dan bila ingin potongan tebal 5 mm tinggal menyatukan 8 iris tipis secara digital!",
                    prerequisite = "M04.T1.2",
                    crossLinks = "Dasar pembentukan Voksel Isotropik pada M06.T1.1 dan Rekonstruksi MPR/3D pada M08.T3.1.",
                    labType = "ct_m04_generasi_pitch",
                    sortOrder = 4
                ),
                CtNode(
                    code = "M04.T2.2",
                    level = 2,
                    title = "Mode Akuisisi Axial (Step-and-Shoot) vs Helical (Spiral) & Rumus Pitch",
                    summaryShort = "Pada mode Helikal, Gantry berputar kontinu sambil meja bergerak maju dengan kecepatan tetap; kerapatan lintasan spiral ditentukan oleh Beam Pitch = Pergerakan Meja per Rotasi / (N × T).",
                    detailFull = """Pesawat CT modern menyediakan dua mode utama pergerakan meja dan rotasi Gantry:

1. Mode Axial / Sequential ('Step-and-Shoot'):
Meja pasien berhenti diam sementara Gantry berputar 360° sambil memancarkan sinar-X untuk merekam satu irisan (atau satu blok detektor). Setelah eksposi selesai, sinar-X mati, meja maju satu langkah menuju posisi berikutnya, berhenti lagi, lalu Gantry memindai lagi. Mode ini menghasilkan profil irisan yang sangat datar (tanpa interpolasi z-axis) dan masih sering digunakan pada CT Kepala Non-Kontras rutin atau HRCT Paru sela-irisan, tetapi lebih lambat dan rentan terhadap ketidakkonsistenan tahan napas antar-irisan (misregistration).

2. Mode Helical / Spiral (Volume Scanning):
Dimungkinkan oleh teknologi Slip-Ring: tabung sinar-X menyala dan Gantry berputar 360° terus-menerus **bersamaan dengan bergeraknya meja pasien secara kontinu dengan kecepatan konstan** menembus Bore. Lintasan relatif fokus sinar-X mengelilingi tubuh pasien membentuk ulir pegas (heliks/spiral), merekam volume 3D utuh dalam satu kali tahan napas singkat.

Parameter kunci yang mengatur seberapa rapat atau renggang ulir spiral tersebut adalah **Pitch (Beam Pitch / Faktor Pitch menurut standar IEC)**:

Rumus: Pitch (P) = d / (N × T) = Pergerakan Meja per Rotasi 360° (mm) / Total Lebar Berkas Kolimasi (mm)

Di mana:
• d = Table Feed per Rotation (jarak tempuh meja dalam mm selama 1 kali putaran Gantry 360°).
• N = Jumlah baris detektor aktif (misal 64 baris).
• T = Ketebalan satu baris detektor (misal 0,625 mm), sehingga N × T = lebar total berkas sinar-X pada sumbu z (misal 40 mm).

Contoh Perhitungan Klinis:
Jika pesawat CT 64-slice memiliki konfigurasi detektor 64 × 0,5 mm (total kolimasi N × T = 32 mm) dan meja pasien bergerak maju sejauh d = 25,6 mm setiap satu kali putaran Gantry, maka:
Pitch = 25,6 mm / 32 mm = 0,8.
Jika waktu satu rotasi Gantry (t_rot) adalah 0,5 detik, maka kecepatan gerak meja adalah v = d / t_rot = 25,6 mm / 0,5 s = 51,2 mm/detik.""",
                    analogi = "Analogi: Bayangkan melilitkan perban elastis pada lengan pasien. Jika lilitan berikutnya menumpuk sebagian di atas lilitan sebelumnya, itu adalah Pitch < 1 (tumpang-tindih rapat, sangat kuat dan halus). Jika tepi perban pas bertemu tepi perban sebelahnya tanpa celah, itu adalah Pitch = 1. Jika lilitannya ditarik cepat sehingga renggang, itu adalah Pitch > 1 (cepat selesai sampai ujung lengan, tapi cakupan perbannya lebih tipis).",
                    prerequisite = "M04.T2.1",
                    crossLinks = "Uji langsung perubahan ulir spiral dan kecepatan meja pada Simulator 3 (Geometri MSCT & Helikal Pitch).",
                    labType = "ct_m04_generasi_pitch",
                    sortOrder = 5
                ),
                CtNode(
                    code = "M04.T3.1",
                    level = 3,
                    title = "Interpolasi Z-Axis, Slice Sensitivity Profile (SSP) & Trade-Off Nilai Pitch",
                    summaryShort = "Karena meja bergerak selama Gantry berputar pada mode Helikal, komputer wajib melakukan interpolasi z-axis (180° LI) untuk membentuk irisan datar; Pitch > 1 memperlebar SSP (mengurangi resolusi z-axis).",
                    detailFull = """Pada pemindaian Helikal, karena meja pasien terus bergerak maju selama Gantry berputar dari sudut 0° ke 360°, titik awal proyeksi (0°) dan titik akhir proyeksi (360°) tidak lagi berada pada koordinat z anatomi yang sama! Jika data 360° helikal ini langsung direkonstruksi tanpa koreksi, citra akan mengalami artefak gerak tidak konsisten (helical streak artifact).

Untuk mengatasinya, sebelum menjalankan algoritma rekonstruksi FBP/Iteratif, komputer melakukan **Interpolasi Linier Z-Axis (Z-Interpolation, umumnya 180° Linear Interpolation / 180° LI atau Cone-Beam Filter)**: komputer menghitung estimasi data proyeksi pada satu bidang datar z tertentu dengan merata-ratakan titik proyeksi spiral di depan dan di belakang bidang tersebut berdasarkan jaraknya.

Bagaimana pemilihan nilai **Pitch** memengaruhi kualitas citra, kecepatan pemindaian, dan dosis radiasi?

Rumus: Waktu Scan Total (T_scan) = Panjang Area Scan (L) / (Pitch × N × T / t_rot)  |  CTDI_vol = CTDI_w / Pitch

1. Pitch < 1,0 (Misalnya Pitch = 0,5 – 0,8; Overlapping / Overscanning):
• Lintasan berkas sinar-X saling tumpang-tindih (overlap) pada sumbu z.
• Keunggulan: Profil sensitivitas irisan (Slice Sensitivity Profile / SSP) sangat sempit dan tajam (effective slice thickness terjaga), bebas dari helical windmill artifact, dan rasio sinyal-terhadap-derau (SNR) tinggi. Sangat ideal untuk **CT Angiografi Koroner (Pitch 0,2–0,3)**, CT Tulang Temporal, atau struktur vaskular halus.
• Konsekuensi: Waktu pemindaian lebih lama dan dosis radiasi (CTDIvol pada mA tetap) lebih tinggi.

2. Pitch = 1,0 (Contiguous Helical):
• Pergerakan meja per rotasi sama persis dengan lebar total kolimasi berkas (d = N × T). Keseimbangan standar antara resolusi longitudinal, waktu pindai, dan dosis.

3. Pitch > 1,0 (Misalnya Pitch = 1,2 – 1,5; Extended / Stretched Helical):
• Meja bergerak lebih jauh daripada lebar berkas per rotasi.
• Keunggulan: Waktu pemindaian volume panjang (seperti Toraks-Abdomen-Pelvis pada pasien trauma atau sesak napas berat) menjadi **jauh lebih cepat** dan dosis radiasi (pada mA tetap) lebih rendah.
• Konsekuensi Fisik: Karena jarak titik interpolasi z-axis semakin renggang, kurva **Slice Sensitivity Profile (SSP) melebar** (effective slice thickness bertambah tebal sekitar 15–30%), sehingga resolusi spasial sepanjang sumbu z menurun dan risiko partial volume effect meningkat.

4. Fenomena Over-Beaming vs Over-Ranging (Z-Overscanning) pada MSCT Helikal:
• Over-Beaming (Efek Penumbra): Berkas sinar-X dibuka sedikit lebih lebar dari total lebar detektor aktif N × T agar baris detektor paling tepi tetap mendapat sinyal seragam.
• Over-Ranging (Z-Overscanning): Agar irisan pertama dan irisan terakhir dari volume target memiliki data interpolasi z-axis yang lengkap, Gantry wajib melakukan tambahan eksposi setengah hingga satu putaran ekstra di luar batas atas dan batas bawah area klinis. Pada kolimasi lebar dan Pitch tinggi, Over-Ranging menambah DLP secara signifikan sehingga pesawat CT modern menggunakan **Dynamic / Adaptive Dose Shield (Kolimator Asimetris Dinamis)** yang menutup bilah Tungsten secara otomatis di awal dan akhir scan helikal.""",
                    analogi = "Analogi: Interpolasi z-axis seperti membuat foto panorama datar di lantai ke-3 sementara kamera Anda berjalan menaiki tangga spiral dari lantai 2 ke lantai 4: komputer menggabungkan potret dari anak tangga tepat di bawah lantai 3 dan tepat di atas lantai 3 sesuai jaraknya agar menghasilkan satu gambar lantai 3 yang rata.",
                    prerequisite = "M04.T2.2",
                    crossLinks = "Terhubung langsung ke rumus mAs Efektif = mAs / Pitch dan CTDIvol = CTDIw / Pitch pada M07.T2.1.",
                    labType = "ct_m04_generasi_pitch",
                    sortOrder = 6
                )
            )
        ),

        // =========================================================================
        // MODUL 5: REKONSTRUKSI CITRA: SINOGRAM, FBP, ITERATIVE & KERNEL
        // =========================================================================
        CtModule(
            id = "M05",
            code = "M05",
            title = "Rekonstruksi Citra: Sinogram, FBP, Iterative & Kernel",
            subtitle = "Transformasi Radon, Sinogram, Back-Projection, Filter Konvolusi (Kernel Soft vs Sharp), Iterative & AI DLIR",
            description = "Bagaimana ribuan profil proyeksi sinar-X dari DAS diubah menjadi citra penampang anatomi: konsep Sinogram (Transformasi Radon), mengapa Simple Back-Projection menghasilkan gambar kabur (star-artifact 1/r), solusi Filtered Back Projection (FBP), pemilihan Convolution Kernel (Soft/Standard vs Sharp/Bone), hingga algoritma Iterative Reconstruction dan AI Deep Learning.",
            levelBadge = "Tingkat 1–3 • Rekonstruksi Citra",
            iconName = "grid_on",
            prerequisites = listOf("M04"),
            estimatedMinutes = 45,
            totalQuizzes = 5,
            badgeName = "Arsitek Rekonstruksi Sinogram & AI",
            unlockMessage = "Selamat! Kamu telah menguasai Transformasi Radon Sinogram, FBP, Convolution Kernel, serta Iterative & AI DLIR!",
            flashcards = listOf(
                CtFlashcard("M05_F1", "Sinogram (Transformasi Radon)", "Matriks Raw Data 2D yang memetakan profil atenuasi ln(I0/I) sepanjang kanal detektor terhadap sudut proyeksi 0–360 derajat."),
                CtFlashcard("M05_F2", "Filtered Back-Projection (FBP)", "Menerapkan filter konvolusi frekuensi tinggi (Ramp Filter) pada profil proyeksi sebelum back-projection untuk menghilangkan kabur bintang 1/r."),
                CtFlashcard("M05_F3", "Soft Kernel vs Sharp/Bone Kernel", "Soft Kernel menekan noise untuk kontras jaringan lunak (otak/abdomen); Sharp Kernel mempertajam batas tepi tulang/paru dengan konsekuensi noise naik."),
                CtFlashcard("M05_F4", "Iterative Recon (IR) & AI DLIR", "Memodelkan statistik foton dan jaringan saraf dalam untuk menekan quantum noise sehingga memangkas dosis radiasi 30–60%.")
            ),
            sortOrder = 5,
            nodes = listOf(
                CtNode(
                    code = "M05.T0",
                    level = 0,
                    title = "Tujuan Belajar & Alur Rekonstruksi Citra CT",
                    summaryShort = "Memahami perjalanan matematis dan komputasi dari Raw Data (Sinogram) menjadi Image Data matriks piksel melalui FBP, Kernel Konvolusi, dan Rekonstruksi Iteratif/AI.",
                    detailFull = """Selamat datang di Modul 5 CT-Learn! Setelah modul DAS di dalam Gantry mengirimkan ribuan profil atenuasi digital ke komputer konsol, data tersebut belum berupa gambar anatomi yang bisa dibaca dokter, melainkan deretan gelombang proyeksi yang disebut **Raw Data (Sinogram)**.

Pada modul ini Anda akan mempelajari:
1. Apa itu Proyeksi (View), Ray (Sinar), Transformasi Radon, dan bagaimana tumpukan profil atenuasi dari sudut 0° hingga 360° membentuk peta **Sinogram**.
2. Mengapa metode proyeksi balik sederhana (**Simple Back-Projection**) gagal menghasilkan citra tajam karena munculnya kabur bintang (*star-like blurring* 1/r), serta bagaimana **Filtered Back Projection (FBP)** menyelesaikannya.
3. Fungsi **Filter Konvolusi / Reconstruction Kernel (Algorithm)** di konsol CT: aturan emas trade-off antara **Soft/Smooth Kernel** (noise rendah, kontras jaringan lunak tinggi) vs **Sharp/Bone Kernel** (ketajaman tepi tulang/paru tinggi, tetapi noise meningkat).
4. Perbedaan mendasar antara rekonstruksi ulang dari **Raw Data (Retrospective Reconstruction)** vs pembesaran dari **Image Data (Zooming)**.
5. Prinsip kerja **Iterative Reconstruction (ASIR, iDose, ADMIRE, AIDR 3D)** dan **AI Deep Learning Image Reconstruction (DLIR)** dalam memangkas dosis radiasi pasien hingga 50–75%.""",
                    analogi = "Analogi: Jika Sinogram adalah kumpulan lembaran partitur musik dari ratusan alat musik yang direkam dari sekeliling panggung, maka algoritma Rekonstruksi (FBP & Iteratif) adalah konduktor jenius yang menyatukan seluruh partitur itu menjadi harmoni gambar anatomi yang jernih.",
                    learningGoal = "Menguasai konsep Sinogram, mekanisme FBP, pemilihan Kernel rekonstruksi klinis, serta keunggulan Iterative dan AI Reconstruction.",
                    crossLinks = "Dipraktikkan langsung pada Simulator 3 (Kernel & Rekonstruksi) dan mendasari koreksi artefak Sinogram pada M09.",
                    labType = "ct_m05_rekonstruksi",
                    sortOrder = 1
                ),
                CtNode(
                    code = "M05.T1.1",
                    level = 1,
                    title = "Dari Proyeksi ke Sinogram (Transformasi Radon & Raw Data)",
                    summaryShort = "Setiap sudut posisi Gantry menghasilkan satu profil proyeksi atenuasi; ketika ratusan profil proyeksi dari 0°–360° ditumpuk secara berurutan, terbentuklah citra gelombang sinus yang disebut Sinogram.",
                    detailFull = """Ketika tabung sinar-X dan busur detektor berada pada satu sudut tertentu θ (misalnya θ = 0° di posisi jam 12), berkas sinar-X menembus tubuh pasien dan ditangkap oleh deretan kanal detektor (t). Setiap kanal detektor mengukur satu garis lintasan sinar (*ray*) dan menghasilkan nilai logaritmik total atenuasi sepanjang garis tersebut: P(t, θ) = ln(I₀ / I). Himpunan seluruh pembacaan kanal detektor pada satu sudut θ ini disebut satu **Profil Proyeksi (Projection / View)**.

Dalam satu putaran Gantry 360°, DAS mengumpulkan sekitar 1.000 hingga 2.400 *views*. Jika kita menyusun setiap baris profil proyeksi P(t, θ) secara bertumpuk dari atas ke bawah—dengan sumbu horizontal menyatakan posisi kanal detektor (t) dan sumbu vertikal menyatakan sudut rotasi Gantry (θ dari 0° sampai 360°)—maka setiap objek titik di dalam tubuh pasien (di luar titik pusat Isocenter) akan membentuk jejak kurva menyerupai **gelombang sinus (sinusoid)**. Oleh sebab itu, peta 2D kumpulan seluruh data mentah proyeksi ini dinamakan **Sinogram** (secara matematis merupakan representasi **Transformasi Radon**).

Memahami sifat fisik Sinogram sangat penting bagi radiografer dan fisikawan medis:
• Satu titik objek yang berada persis di titik pusat **Isocenter Gantry** akan tampak sebagai **garis lurus vertikal** tepat di tengah Sinogram.
• Objek yang berada jauh di tepi perifer tubuh akan membentuk gelombang sinus dengan amplitudo simpangan kiri-kanan yang lebar.
• Jika **satu elemen detektor rusak atau tidak terkalibrasi**, posisi kanal horizontal (t) tersebut akan cacat terus-menerus di semua sudut θ, membentuk **garis lurus vertikal cacat pada Sinogram** yang ketika direkonstruksi berubah menjadi **Ring Artifact (cincin konsentris)** pada citra anatomi!""",
                    analogi = "Analogi: Bayangkan sebuah mercusuar yang berputar mengelilingi pulau sambil memotret siluet menara di pulau tersebut dari tiap derajat sudut putar. Jika ratusan strip foto siluet itu ditempel berjejer dari atas ke bawah, posisi menara akan meliuk membentuk gelombang sinus yang mulus—itulah Sinogram!",
                    prerequisite = "M05.T0",
                    crossLinks = "Cacat garis vertikal pada Sinogram menyebabkan Ring Artifact yang disimulasikan pada M09.T1.2.",
                    labType = "ct_m05_rekonstruksi",
                    sortOrder = 2
                ),
                CtNode(
                    code = "M05.T1.2",
                    level = 1,
                    title = "Simple Back-Projection vs Filtered Back Projection (FBP)",
                    summaryShort = "Simple Back-Projection menyebarkan balik nilai proyeksi secara merata sehingga menimbulkan kabur bintang (1/r blurring); FBP menerapkan filter frekuensi tinggi (Ramp Filter) terlebih dahulu agar tepi objek kembali tajam.",
                    detailFull = """Bagaimana komputer mengubah Sinogram kembali menjadi citra penampang anatomi 2D? Pendekatan paling intuitif adalah **Proyeksi Balik Sederhana (Simple Back-Projection)**: nilai atenuasi yang tercatat pada setiap detektor disorotkan balik (dicorengkan merata) sepanjang garis lintasan sinar asalnya melintasi matriks gambar dari setiap sudut θ. Di titik tempat objek padat benar-benar berada, corengan dari berbagai sudut akan saling berpotongan dan menjumlahkan nilai yang tinggi.

Namun Simple Back-Projection memiliki cacat matematis bawaan: karena setiap garis proyeksi juga meninggalkan jejak nilai di sepanjang lintasan kosong di luar objek, sebuah titik padat tunggal akan menghasilkan bayangan pancaran jeruji bintang di sekelilingnya (**Star-Artifact**) yang menyebabkan seluruh citra tampak sangat kabur (intensitas kabur meluruh sebesar 1/r terhadap jarak r).

Untuk menghilangkan kabur 1/r tersebut, digunakanlah algoritma standar **Filtered Back Projection (FBP)** yang terdiri atas dua tahap:

Rumus: Profil Proyeksi P(t, θ) ➔ [Konvolusi dengan Filter Kernel h(t) / Ramp Filter] ➔ P*(t, θ) ➔ [Back-Projection] ➔ Citra Tajam μ(x,y)

1. Tahap Konvolusi / Filtering (Di Domain Spasial atau Domain Frekuensi Fourier):
Sebelum diproyeksikan balik, setiap profil proyeksi Sinogram P(t, θ) dikalikan (dikonvolusikan) dengan fungsi filter matematis (secara teoritis disebut **Filter Lak / Ramp Filter** yang memperkuat frekuensi spasial tinggi). Di domain spasial, filter ini memberikan bobot positif tinggi pada puncak sinyal objek dan **bobot negatif (side-lobes negatif)** tepat di sebelah kiri dan kanan puncak sinyal.
2. Tahap Back-Projection:
Ketika profil yang sudah memiliki bahu negatif ini diproyeksikan balik dari 360°, nilai-nilai negatif di sekeliling puncak akan **saling meniadakan (membatalkan) corengan kabur bintang 1/r**, sehingga menyisakan citra objek yang tajam dan akurat sesuai kontur anatomi aslinya!""",
                    analogi = "Analogi: Simple Back-Projection seperti menyorotkan ratusan senter berlensa buram dari sekeliling panggung ke arah patung di tengah—patungnya terlihat, tapi seluruh lantai panggung ikut terang berkabut. Filtered Back Projection (FBP) memasang cetakan pembatas gelap (bobot negatif) di pinggir tiap sorotan senter sehingga cahaya liar di luar patung saling memadamkan dan batas patung tampak tajam!",
                    prerequisite = "M05.T1.1",
                    crossLinks = "Bentuk kurva filter pada FBP diatur lewat pilihan Reconstruction Kernel di M05.T2.1.",
                    labType = "ct_m05_rekonstruksi",
                    sortOrder = 3
                ),
                CtNode(
                    code = "M05.T2.1",
                    level = 2,
                    title = "Convolution Kernel (Filter Rekonstruksi): Soft/Standard vs Sharp/Bone",
                    summaryShort = "Pemilihan Kernel rekonstruksi menentukan kompromi antara ketajaman tepi (resolusi spasial) dan kehalusan bintik noise (resolusi kontras); rekonstruksi ulang Kernel wajib menggunakan Raw Data.",
                    detailFull = """Filter Ramp murni pada FBP memperkuat seluruh frekuensi spasial tinggi secara agresif. Padahal di dalam sinyal frekuensi tinggi tidak hanya terdapat informasi tepi anatomi yang tajam (seperti trabekula tulang dan bronkiolus paru), melainkan juga **Quantum Noise (bintik derau statistik foton)**. Oleh sebab itu, pabrikan pesawat CT memodifikasi kurva filter tersebut menjadi berbagai pilihan **Convolution Kernel / Reconstruction Algorithm** yang dapat dipilih radiografer di konsol:

1. Soft / Smooth / Standard Kernel (misal B20f/B30f, FC10, Standard/Soft):
• Mekanisme: Menekan (apodisasi) penguatan frekuensi spasial tinggi pada kurva filter.
• Dampak Visual: **Quantum Noise berkurang drastis** sehingga citra tampak halus (smooth) dan **resolusi kontras rendah (Low-Contrast Detectability) meningkat**. Namun batas tepi tulang tampak sedikit membulat/kurang tajam.
• Indikasi Klinis Utama: Parenkim Otak (Brain), Hati, Ginjal, Abdomen/Pelvis jaringan lunak, dan evaluasi lesi hipodens/hiperdens halus.

2. Sharp / Edge-Enhancing / Bone / Lung Kernel (misal B60f/B70f, FC50/FC80, Bone/Lung):
• Mekanisme: Mempertahankan atau menonjolkan penguatan frekuensi spasial tinggi untuk mempertegas lonjakan batas tepi (edge enhancement).
• Dampak Visual: **Resolusi spasial (ketajaman tepi) meningkat tajam** sehingga garis fraktur rambut pada tulang atau dinding bronkus paru terlihat sangat tegas. Konsekuensinya, **Quantum Noise (bintik kasar) ikut meningkat tajam** sehingga tidak cocok untuk menilai lesi jaringan lunak.
• Indikasi Klinis Utama: Korteks & Trabekula Tulang (Bone/Orthopedic), HRCT Parenkim Paru (Interstitial Lung Disease), Sinus Paranasal, dan Tulang Temporal (Mastoid/Cochlea).

**Catatan Penting Konsol — Retrospective Reconstruction vs Image Zooming:**
Untuk mengubah citra dari *Standard Kernel* menjadi *Bone Kernel*, atau memperkecil *Display Field of View (DFOV)* agar menghasilkan resolusi matriks baru yang benar-benar tajam (**Targeted Retrospective Reconstruction**), **Raw Data (Sinogram) di dalam komputer konsol WAJIB masih tersimpan**! Jika Raw Data sudah terhapus dan kita hanya memperbesar gambar yang sudah jadi (Image Data), itu hanyalah *Magnifikasi/Zooming Piksel* biasa yang tidak menambah resolusi spasial.""",
                    analogi = "Analogi: Memilih Kernel seperti mengatur efek penyuntingan foto: Soft Kernel adalah mode 'Penghalus Wajah' (bintik noise hilang sehingga gradasi bayangan halus terlihat jelas, cocok untuk organ lunak), sedangkan Sharp/Bone Kernel adalah mode 'Pertajam Garis Sketsa' (tiap retakan halus tulang terlihat sangat tegas, tetapi bintik pasir di latar belakang ikut menonjol).",
                    prerequisite = "M05.T1.2",
                    crossLinks = "Bandingkan langsung efek Soft vs Sharp Kernel pada Simulator 3, serta kaitannya dengan Windowing (WW/WL) di M06.T2.1.",
                    labType = "ct_m05_rekonstruksi",
                    sortOrder = 4
                ),
                CtNode(
                    code = "M05.T3.1",
                    level = 3,
                    title = "Iterative Reconstruction (IR) & AI Deep Learning Reconstruction (DLIR)",
                    summaryShort = "Berbeda dari FBP yang rentan terhadap lonjakan noise pada dosis rendah, Iterative Reconstruction dan AI DLIR memodelkan statistik foton dan geometri optik untuk menghasilkan citra rendah noise pada dosis radiasi sangat hemat.",
                    detailFull = """Selama lebih dari tiga dekade, Filtered Back Projection (FBP) menjadi satu-satunya algoritma rekonstruksi CT karena kecepatannya yang sangat tinggi. Namun FBP memiliki kelemahan fundamental: ia mengabaikan sifat statistik kuantum foton sinar-X (distribusi Poisson) dan mengasumsikan titik fokus tabung serta elemen detektor berukuran titik nol ideal. Akibatnya, ketika radiografer menurunkan dosis radiasi (menurunkan mAs atau kVp), FBP memperlakukan kekurangan foton tersebut sebagai sinyal asli dan menghasilkan **lonjakan Quantum Noise serta streak artifact yang parah**.

Berkat kemajuan prosesor GPU modern, pesawat CT masa kini melengkapi atau menggantikan FBP dengan dua generasi algoritma mutakhir:

1. Iterative Reconstruction (IR — seperti ASIR/Veo, SAFIRE/ADMIRE, iDose⁴/IMR, AIDR 3D):
Alih-alih hanya memproyeksikan balik satu kali, algoritma Iteratif bekerja dalam siklus umpan balik (loop koreksi berulang):
• Langkah 1: Komputer membuat citra perkiraan awal (initial image estimate, misalnya dari FBP).
• Langkah 2 (Forward Projection): Komputer melakukan simulasi pemindaian buatan terhadap citra perkiraan tersebut untuk menghasilkan *Simulated Sinogram*.
• Langkah 3 (Perbandingan & Koreksi): *Simulated Sinogram* dibandingkan dengan *Measured Sinogram* asli dari DAS sambil memasukkan model statistik derau foton (Statistical/Hybrid IR) serta geometri fisik focal spot dan detektor (Model-Based Iterative Reconstruction / MBIR).
• Langkah 4 (Update): Selisih kesalahan diproyeksikan balik untuk memperbarui citra perkiraan secara berulang hingga konvergen. Hasilnya: noise berkurang drastis tanpa mengaburkan tepi anatomi, memungkinkan **penurunan dosis radiasi (CTDIvol) sebesar 30–60%**!

2. AI Deep Learning Image Reconstruction (DLIR — seperti TrueFidelity, AiCE, Precise Image):
Pada rekonstruksi berbasis iteratif tingkat tinggi, citra terkadang tampak terlalu licin seperti plastik (*plastic/waxy texture* atau pergeseran spektrum frekuensi noise). Algoritma DLIR menggunakan jaringan saraf tiruan konvolusional dalam (Deep Convolutional Neural Network / DNN) yang dilatih menggunakan jutaan pasang citra dosis rendah dan citra FBP dosis tinggi berkualitas sempurna. Saat merekonstruksi Sinogram klinis, AI membedakan struktur anatomi sejati dari derau kuantum dan elektronik dalam sepersekian detik, menghasilkan tekstur citra yang natural dan tajam pada dosis radiasi ultra-rendah (Low-Dose CT Paru maupun CT Pediatrik).""",
                    analogi = "Analogi: FBP seperti pelukis cepat yang melukis wajah dalam sekali goresan kuas—jika cahayanya redup (dosis rendah), lukisannya penuh bercak kasar. Iterative Reconstruction seperti pelukis teliti yang membandingkan lukisannya dengan wajah asli berkali-kali lalu menghapus bercak yang salah sedikit demi sedikit. Sedangkan AI DLIR adalah maestro pelukis berpengalaman ribuan karya yang langsung mengenali mana detail wajah asli dan mana bintik debu hanya dalam sekali pandang!",
                    prerequisite = "M05.T2.1",
                    crossLinks = "Mendukung prinsip optimasi dosis ALARA, mAs rendah, dan CT Pediatrik pada M07.",
                    labType = "ct_m05_rekonstruksi",
                    sortOrder = 5
                )
            )
        )
    )
}
