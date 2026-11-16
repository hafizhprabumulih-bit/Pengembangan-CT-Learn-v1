package com.example.data

internal object CtCurriculumQuizzes {
    val quizzes: List<CtQuizQuestion> = listOf(
        // ==========================================
        // MODUL 1 (M01): Fisika Dasar Sinar-X & Prinsip Atenuasi CT (6 kuis)
        // ==========================================
        CtQuizQuestion(
            id = "q_m01_t0",
            moduleId = "M01",
            nodeCode = "M01.T0",
            level = 1,
            question = "Manakah rangkaian prinsip fisika dasar yang menjadi fondasi pembentukan citra pada seluruh pesawat CT Scan modern?",
            options = listOf(
                "Pembangkitan foton sinar-X di tabung anoda Tungsten, atenuasi diferensial (Fotolistrik & Compton) mengikuti Hukum Beer-Lambert, dan pengukuran proyeksi 360 derajat untuk menghilangkan superposisi",
                "Eksitasi spin proton hidrogen oleh pulsa frekuensi radio di dalam medan magnet statis kuat",
                "Pantulan gelombang suara ultrasonik frekuensi megahertz oleh batas akustik antar-organ",
                "Emisi sinar gamma spontan dari peluruhan radiofarmaka yang disuntikkan ke vena pasien"
            ),
            correctIndex = 0,
            explanation = "Pencitraan CT Scan bekerja berdasarkan transmisi dan atenuasi eksponensial berkas foton sinar-X dari berbagai sudut proyeksi mengelilingi tubuh pasien."
        ),
        CtQuizQuestion(
            id = "q_m01_t1_1",
            moduleId = "M01",
            nodeCode = "M01.T1.1",
            level = 1,
            question = "Apa dua kelemahan fisik utama radiografi proyeksi 2D konvensional yang berhasil diatasi secara tuntas oleh akuisisi tomografi pesawat CT Scan?",
            options = listOf(
                "Tidak memerlukan listrik dan tidak memiliki komputer pengolah citra",
                "Superposisi (tumpang-tindih) organ 3D ke dalam satu bidang 2D serta ketidakmampuan membedakan selisih koefisien atenuasi jaringan lunak yang kecil (< 1% atau 3–5 HU)",
                "Waktu eksposi yang terlalu singkat dan ketajaman kontras jaringan lunak yang terlalu tinggi",
                "Penggunaan gelombang mikro yang memanaskan permukaan kulit pasien"
            ),
            correctIndex = 1,
            explanation = "CT Scan mengakuisisi proyeksi dari 360 derajat untuk merekonstruksi irisan melintang bebas superposisi sekaligus mencapai resolusi kontras jaringan lunak hingga 0,3–0,5% (3–5 HU)."
        ),
        CtQuizQuestion(
            id = "q_m01_t1_2",
            moduleId = "M01",
            nodeCode = "M01.T1.2",
            level = 1,
            question = "Pada tabung sinar-X pesawat CT dengan target anoda Tungsten (Z = 74), bagaimana mekanisme utama pembentukan foton sinar-X saat elektron berenergi tinggi menumbuk anoda?",
            options = listOf(
                "Sekitar 99% energi elektron berubah menjadi panas termal, sedangkan ~1% menjadi foton sinar-X melalui pengereman Bremsstrahlung (spektrum polikromatik kontinu) dan transisi kulit K Karakteristik (≥ 69,5 keV)",
                "Seluruh 100% energi elektron berubah menjadi sinar-X monokromatik tanpa menghasilkan panas pada anoda",
                "Elektron memantul kembali ke katoda tanpa berinteraksi dengan inti maupun kulit elektron Tungsten",
                "Hanya menghasilkan gelombang cahaya tampak tanpa radiasi pengion"
            ),
            correctIndex = 0,
            explanation = "Pada anoda Tungsten (Z = 74), 99% energi kinetik elektron terkonversi menjadi panas, dan ~1% menjadi sinar-X (80–85% Bremsstrahlung polikromatik hingga E_max = kVp serta radiasi Karakteristik pada 59 & 67 keV)."
        ),
        CtQuizQuestion(
            id = "q_m01_t2_1",
            moduleId = "M01",
            nodeCode = "M01.T2.1",
            level = 2,
            question = "Interaksi foton sinar-X manakah yang probabilitasnya berbanding lurus dengan pangkat tiga nomor atom materi (Z^3) dan berbanding terbalik dengan pangkat tiga energi foton (1/E^3)?",
            options = listOf(
                "Hamburan Koheren (Rayleigh)",
                "Efek Fotolistrik (Photoelectric Absorption)",
                "Hamburan Compton (Incoherent Scattering)",
                "Produksi Pasangan (Pair Production)"
            ),
            correctIndex = 1,
            explanation = "Efek fotolistrik sangat bergantung pada Z^3/E^3, yang menjelaskan mengapa tulang padat (Z tinggi) dan media kontras Iodin (Z=53) memberikan kontras sangat terang pada citra CT."
        ),
        CtQuizQuestion(
            id = "q_m01_t2_2",
            moduleId = "M01",
            nodeCode = "M01.T2.2",
            level = 2,
            question = "Berdasarkan Hukum Beer-Lambert I = I0 * exp(-mu * x) dan transformasi sinyal di DAS, berapa persen intensitas foton yang lolos setelah melewati 2 x HVL dan apa hasil operasi logaritmik ln(I0 / I)?",
            options = listOf(
                "75% lolos; ln(I0 / I) menghasilkan suhu anoda dalam satuan MHU",
                "50% lolos; ln(I0 / I) menghasilkan kecepatan meja pasien",
                "25% dari I0 berhasil lolos; dan operasi ln(I0 / I) menghasilkan penjumlahan linier koefisien atenuasi voksel (Sigma mu_i * dx) sepanjang lintasan berkas",
                "12,5% lolos; ln(I0 / I) menghapus seluruh data proyeksi"
            ),
            correctIndex = 2,
            explanation = "Melewati 2 x HVL menyisakan (1/2)^2 = 25% intensitas awal, dan transformasi logaritmik ln(I0 / I) di DAS mengubah fungsi eksponensial Beer-Lambert menjadi integral garis atenuasi."
        ),
        CtQuizQuestion(
            id = "q_m01_t3_1",
            moduleId = "M01",
            nodeCode = "M01.T3.1",
            level = 3,
            question = "Mengapa berkas sinar-X polikromatik mengalami pengerasan berkas (Beam Hardening) saat menembus tulang tebal, dan bagaimana filter pra-pasien serta Bow-Tie Filter mengatasinya?",
            options = listOf(
                "Foton berenergi rendah terserap lebih dulu sehingga energi rata-rata berkas naik (memicu cupping/dark streaks); filter menyaring foton energi rendah sejak awal dan menyeragamkan kekerasan berkas sesuai ketebalan tubuh",
                "Foton berenergi tinggi terserap habis di kulit sehingga berkas menjadi semakin lunak di tengah tubuh",
                "Bow-Tie Filter berfungsi memperbesar ukuran titik fokus anoda menjadi 5 mm",
                "Beam Hardening hanya terjadi pada berkas laser penanda posisi meja"
            ),
            correctIndex = 0,
            explanation = "Karena serapan fotolistrik sebanding 1/E^3, foton energi rendah terserap lebih dulu di jaringan/tulang sehingga berkas menjadi lebih keras. Filtrasi inheren dan Bow-Tie Filter menyeragamkan spektrum dan mengurangi dosis kulit."
        ),

        // ==========================================
        // MODUL 2 (M02): Anatomi Pesawat CT Scan & Arsitektur Gantry (6 kuis)
        // ==========================================
        CtQuizQuestion(
            id = "q_m02_t0",
            moduleId = "M02",
            nodeCode = "M02.T0",
            level = 1,
            question = "Tiga unit perangkat keras utama dalam instalasi pesawat CT Scan modern terdiri atas:",
            options = listOf(
                "Gantry, Meja Pemeriksaan Pasien (Patient Couch), dan Konsol Operator (beserta Komputer Rekonstruksi)",
                "Magnet Superkonduktor, Koil Gradien, dan Kabinet Quench Pipe",
                "Transduser Piezoelektrik, Gel Akustik, dan Monitor Doppler",
                "Kaset Film Screen, Kamar Gelap, dan Developer Otomatis"
            ),
            correctIndex = 0,
            explanation = "Arsitektur perangkat keras CT Scan terdiri dari Gantry (tempat tabung, generator HV, kolimator, dan detektor berputar), Meja Pasien bermotor presisi, dan Konsol Operator di ruang kendali."
        ),
        CtQuizQuestion(
            id = "q_m02_t1_1",
            moduleId = "M02",
            nodeCode = "M02.T1.1",
            level = 1,
            question = "Di manakah posisi komputer akuisisi & rekonstruksi serta panel kontrol eksposi ditempatkan untuk melindungi radiografer dari radiasi hambur?",
            options = listOf(
                "Tepat di sebelah meja pasien di dalam ruang gantry tanpa pelindung",
                "Di Ruang Konsol Operator yang dipisahkan oleh dinding berlapis timbal (Pb 2 mm) dan dilengkapi kaca pengawas timbal (lead glass) serta sistem interkom dua arah",
                "Di dalam penutup berputar (rotating frame) gantry",
                "Di ruang tunggu pasien tanpa jendela pengawas"
            ),
            correctIndex = 1,
            explanation = "Konsol operator wajib berada di ruang terpisah yang dilapisi perisai timbal (Pb) dengan jendela kaca timbal agar operator dapat mengawasi dan berkomunikasi dengan pasien secara aman."
        ),
        CtQuizQuestion(
            id = "q_m02_t1_2",
            moduleId = "M02",
            nodeCode = "M02.T1.2",
            level = 1,
            question = "Ketika penutup depan Gantry dibuka, bagaimana susunan geometris komponen pada Rotating Frame serta berapa diameter bore standar pesawat CT?",
            options = listOf(
                "Diameter bore 20 cm; Tabung Sinar-X dan Detektor dipasang berdampingan di sisi yang sama",
                "Diameter bore 70–85 cm (tilt ±30°); Tabung Sinar-X (jam 12) dan Busur Detektor & DAS (jam 6) dipasang berhadapan 180° mengapit Isocenter, diseimbangkan oleh HV Generator dan Heat Exchanger",
                "Diameter bore 150 cm; Busur Detektor diletakkan di ruang konsol operator",
                "Diameter bore 40 cm; hanya meja pasien yang berputar mengelilingi tabung"
            ),
            correctIndex = 1,
            explanation = "Bore gantry berukuran 70–85 cm, dan di atas Rotating Frame dipasang Tabung Sinar-X berhadapan tepat 180 derajat dengan Detector Array + DAS, diseimbangkan oleh HV Generator dan pendingin."
        ),
        CtQuizQuestion(
            id = "q_m02_t2_1",
            moduleId = "M02",
            nodeCode = "M02.T2.1",
            level = 2,
            question = "Apa konsekuensi teknis apabila radiografer menyetel ketinggian vertikal meja terlalu tinggi atau terlalu rendah (Off-Centering 4–6 cm dari Laser Koronal Isocenter) saat mengambil Topogram Scout dan akuisisi AEC?",
            options = listOf(
                "Kualitas citra meningkat tajam dan dosis radiasi otomatis turun menjadi nol",
                "Bagian tebal tubuh meleset dari bagian tengah Bow-Tie Filter (memicu peningkatan quantum noise), serta magnifikasi/pengecilan bayangan Topogram Scout menyesatkan modulasi arus mA oleh AEC hingga overdosis/underdosis 20–40%",
                "Kabel tegangan tinggi di lantai langsung terputus saat gantry berputar",
                "Tidak ada pengaruh sama sekali karena Bow-Tie Filter memiliki ketebalan rata"
            ),
            correctIndex = 1,
            explanation = "Bow-Tie Filter dan kalkulasi ukuran tubuh dari Topogram Scout oleh AEC mengasumsikan pusat anatomi berada tepat di Isocenter Gantry; off-centering vertikal merusak keseragaman HU, menambah noise, dan mengacaukan dosis AEC."
        ),
        CtQuizQuestion(
            id = "q_m02_t2_2",
            moduleId = "M02",
            nodeCode = "M02.T2.2",
            level = 2,
            question = "Bagaimana pembagian peran antara Bow-Tie Filter, Pre-Patient Collimator, dan Post-Patient Collimator (Anti-Scatter Septa) di dalam Gantry CT?",
            options = listOf(
                "Ketiganya hanya berfungsi mendinginkan oli di dalam tabung sinar-X",
                "Bow-Tie Filter menyeragamkan fluks foton sesuai kontur elips tubuh & memangkas dosis kulit tepi; Pre-Patient Collimator membatasi lebar berkas z-axis sesuai detektor aktif; Post-Patient Septa menolak foton hamburan Compton",
                "Post-Patient Septa menghasilkan sinar-X, sedangkan Bow-Tie Filter mengubah cahaya menjadi sinyal biner",
                "Pre-Patient Collimator dipasang di belakang fotodioda untuk memperkuat arus listrik"
            ),
            correctIndex = 1,
            explanation = "Susunan pembentuk berkas terdiri atas Bow-Tie Filter (kompensasi kontur tubuh), Pre-Patient Collimator Tungsten (pembatas lebar kolimasi sumbu-z), dan Post-Patient Anti-Scatter Septa di permukaan detektor (penolak scatter Compton)."
        ),
        CtQuizQuestion(
            id = "q_m02_t3_1",
            moduleId = "M02",
            nodeCode = "M02.T3.1",
            level = 3,
            question = "Mengapa daun meja pasien (tabletop) pesawat CT wajib dibuat dari komposit Carbon Fiber (Z = 6), dan komputer manakah di ruang konsol yang mengolah Raw Data Sinogram menjadi citra DICOM?",
            options = listOf(
                "Carbon Fiber memiliki atenuasi sinar-X sangat rendah (bebas artefak streak) namun sangat kaku menahan beban 200–300 kg (defleksi < 2 mm); sedangkan pengolahan cepat Sinogram menjadi citra dilakukan oleh Image Reconstruction System (IRS)",
                "Carbon Fiber dipilih karena menyerap 100% sinar-X; sedangkan rekonstruksi dilakukan oleh motor hidrolik meja",
                "Daun meja dibuat dari baja timbal tebal agar sinar-X tidak dapat menembus dari bawah",
                "Komputer IRS hanya berfungsi menyalakan lampu laser ruangan tanpa memproses data detektor"
            ),
            correctIndex = 0,
            explanation = "Komposit serat karbon (Z = 6) menggabungkan sifat radiolusen (transparan terhadap sinar-X) dengan kekuatan mekanik tinggi, sementara array processor/GPU di dalam IRS merekonstruksi ribuan proyeksi Sinogram per detik."
        ),

        // ==========================================
        // MODUL 3 (M03): Instrumentasi Tabung Sinar-X CT, Generator, Slip-Ring & Detektor (7 kuis)
        // ==========================================
        CtQuizQuestion(
            id = "q_m03_t0",
            moduleId = "M03",
            nodeCode = "M03.T0",
            level = 1,
            question = "Urutan rantai konversi energi dan sinyal yang benar dalam sistem akuisisi pesawat CT adalah:",
            options = listOf(
                "Energi Listrik (Slip-Ring & HV Generator) -> Foton Sinar-X (Tabung) -> Cahaya Tampak (Kristal Sintilator) -> Arus Listrik Analog (Fotodioda) -> Sinyal Digital Biner (DAS)",
                "Foton Sinar-X -> Cahaya Tampak -> Energi Listrik -> Gelombang Suara -> Sinyal Analog",
                "Sinyal Digital DAS -> Kristal Sintilator -> Tabung Sinar-X -> Fotodioda -> Slip-Ring",
                "Arus Listrik -> Fotodioda -> Kristal Sintilator -> Tabung Sinar-X -> Film Rontgen"
            ),
            correctIndex = 0,
            explanation = "Daya listrik disalurkan via Slip-Ring ke HV Generator, diubah oleh tabung menjadi foton sinar-X, ditangkap oleh sintilator menjadi kilatan cahaya tampak, diubah oleh fotodioda menjadi arus listrik proporsional, lalu didigitalisasi oleh DAS."
        ),
        CtQuizQuestion(
            id = "q_m03_t1_1",
            moduleId = "M03",
            nodeCode = "M03.T1.1",
            level = 1,
            question = "Mengapa tabung sinar-X CT modern dirancang dengan kapasitas simpan panas anoda 5–8+ MHU serta menggunakan Liquid Metal Bearing (paduan Ga-In-Sn) pada putaran 8.000–10.000 RPM?",
            options = listOf(
                "Untuk menahan beban termal eksposi helikal panjang di bawah gaya sentrifugal 15–30G, di mana Liquid Metal Bearing memungkinkan putaran hening bebas aus mekanis sekaligus menghantarkan panas anoda keluar secara konduksi langsung",
                "Agar tabung sinar-X tidak memerlukan ruang vakum maupun pendingin oli",
                "Untuk menurunkan kecepatan putaran anoda menjadi 10 RPM",
                "Karena tabung CT hanya menyala selama 1 milidetik setiap pemeriksaan"
            ),
            correctIndex = 0,
            explanation = "Eksposi helikal kontinu menghasilkan beban panas tinggi (HU = 1,4 x kVp x mA x t); piringan anoda besar 5–8+ MHU berbantalan logam cair (LMB) dan penukar panas oli-udara mencegah pelelehan trek target Tungsten-Rhenium."
        ),
        CtQuizQuestion(
            id = "q_m03_t1_2",
            moduleId = "M03",
            nodeCode = "M03.T1.2",
            level = 1,
            question = "Apa fungsi pemilihan Small vs Large Focal Spot (Dual Filament) serta teknologi z-Flying Focal Spot (z-FFS) pada tabung sinar-X CT?",
            options = listOf(
                "Small Focus meminimalkan penumbra untuk resolusi spasial tinggi (tulang temporal/koroner), Large Focus menahan arus mA tinggi (abdomen tebal), dan z-FFS membelokkan berkas elektron secara elektromagnetik untuk menggandakan sampling irisan sumbu-z",
                "Large Focus digunakan untuk memperkecil arus tabung menjadi 1 mA",
                "z-Flying Focal Spot berfungsi menghentikan putaran Gantry setiap setengah detik",
                "Dual Filament berfungsi mengubah warna cahaya laser penanda meja"
            ),
            correctIndex = 0,
            explanation = "Dual filament menyeimbangkan kebutuhan ketajaman geometris (Small Focus ~0,5–0,7 mm) dan kapasitas daya (Large Focus ~1,0–1,2 mm), sedangkan z-FFS menggandakan jumlah irisan yang disampel per rotasi (misal 64 baris menjadi 128 slice) dan menekan artefak windmill."
        ),
        CtQuizQuestion(
            id = "q_m03_t2_1",
            moduleId = "M03",
            nodeCode = "M03.T2.1",
            level = 2,
            question = "Bagaimana kombinasi High-Frequency (HF) Inverter HV Generator dan mekanisme cincin Slip-Ring memungkinkan pemindaian Helikal berputar 360 derajat terus-menerus tanpa kabel terbelit?",
            options = listOf(
                "Low-Voltage Slip-Ring menyalurkan daya listrik melalui sikat perak-grafit ke HF Inverter kompak (10–100 kHz, ripple < 1%) yang ikut berputar di atas Rotating Frame, sementara data Sinogram dikirim nirsentuh melalui link optik",
                "Menggunakan kabel tegangan tinggi sepanjang 50 meter yang harus digulung balik setiap satu putaran",
                "Menempatkan generator transformator besi raksasa di ruang tunggu pasien",
                "Mengandalkan baterai kering sekali pakai di dalam tabung sinar-X"
            ),
            correctIndex = 0,
            explanation = "Frekuensi tinggi 10–100 kHz membuat transformator HV cukup kompak untuk dipasang di atas Rotating Frame, menerima suplai daya kontinu via sikat Low-Voltage Slip-Ring dan mengirim data multi-Gbps melalui pemancar optik nirsentuh."
        ),
        CtQuizQuestion(
            id = "q_m03_t2_2",
            moduleId = "M03",
            nodeCode = "M03.T2.2",
            level = 2,
            question = "Mengapa seluruh pesawat Multi-Slice CT (MSCT) modern menggunakan detektor Solid-State Scintillator (Gd2O2S / UFC + Silicon Photodiode) dan meninggalkan detektor tabung gas Xenon?",
            options = listOf(
                "Karena detektor Solid-State memiliki efisiensi penyerapan kuantum sangat tinggi (QDE 95–99%), waktu luruh sangat cepat (afterglow < 0,01%), dan dapat dipotong menjadi matriks ubin 2D multi-baris sepanjang sumbu-z",
                "Karena gas Xenon memiliki efisiensi penyerapan 100% namun memantulkan sinar-X kembali ke tabung",
                "Karena detektor Solid-State tidak memerlukan fotodioda maupun konversi sinyal listrik",
                "Karena kristal Gd2O2S hanya bereaksi terhadap gelombang suara"
            ),
            correctIndex = 0,
            explanation = "Detektor keramik solid-state berdensitas tinggi menyerap hampir seluruh foton sinar-X (hemat dosis pasien) dan dapat disusun menjadi 16 hingga 320 baris detektor MSCT, berbeda dengan bilik gas Xenon yang ber-QDE rendah (45–60%) dan tidak bisa dibagi multi-baris."
        ),
        CtQuizQuestion(
            id = "q_m03_t3_1",
            moduleId = "M03",
            nodeCode = "M03.T3.1",
            level = 3,
            question = "Di dalam modul Data Acquisition System (DAS) yang menempel di belakang busur detektor, tahapan pemrosesan sinyal apakah yang dijalankan pada setiap sudut pencuplikan (1.000–2.400 views/rotasi)?",
            options = listOf(
                "Pre-Amplifier & Current Integrator mengumpulkan arus lemah fotodioda (pA–nA), kemudian Logarithmic Amplifier & ADC (16–24 bit) mengubahnya menjadi nilai atenuasi digital P = ln(I0 / I) untuk dikirim via transmitter optik",
                "DAS langsung mencetak lembaran film Rontgen di dalam Gantry yang sedang berputar",
                "DAS berfungsi memompa oli pendingin menuju anoda tabung sinar-X",
                "DAS mengubah sinyal digital dari komputer konsol menjadi tegangan tinggi 140 kVp"
            ),
            correctIndex = 0,
            explanation = "Sinyal arus analog orde picoampere–nanoampere dari ribuan fotodioda harus langsung diintegrasikan, diamplifikasi secara logaritmik ln(I0/I), dan didigitalkan oleh ADC di dalam DAS tepat di belakang detektor agar tidak rusak oleh derau elektronik."
        ),
        CtQuizQuestion(
            id = "q_m03_t3_2",
            moduleId = "M03",
            nodeCode = "M03.T3.2",
            level = 3,
            question = "Apa keunggulan fundamental teknologi Dual-Energy CT (80 & 140 kVp) dan Photon-Counting Detector CT (PCD berbasis semikonduktor CdTe) dibandingkan detektor pengintegrasi energi (EID) konvensional?",
            options = listOf(
                "Dual-Energy memisahkan material ber-Z berbeda (Iodin vs Kalsium & Virtual Non-Contrast), sedangkan PCD CdTe mengonversi foton sinar-X secara langsung menjadi pulsa muatan dan menghitung tiap foton ke dalam energy bins tanpa electronic noise serta dengan resolusi spasial ultra-tinggi (0,15–0,2 mm)",
                "PCD menggunakan bilik gas Xenon bertekanan rendah yang membutuhkan waktu pindai 5 menit per irisan",
                "Dual-Energy CT hanya dapat bekerja tanpa tabung sinar-X",
                "Detektor CdTe harus mengubah sinar-X menjadi gelombang suara terlebih dahulu sebelum dibaca fotodioda"
            ),
            correctIndex = 0,
            explanation = "DECT memanfaatkan perbedaan kurva atenuasi pada dua spektrum energi (80 vs 140 kVp), sedangkan PCD (CdTe) melakukan konversi langsung tanpa tahap cahaya sintilator sehingga mengeliminasi electronic noise dan menghasilkan pencitraan spektral resolusi ultra-tinggi."
        ),

        // ==========================================
        // MODUL 4 (M04): Evolusi Generasi Pesawat CT, Geometri MSCT & Akuisisi Helikal (6 kuis)
        // ==========================================
        CtQuizQuestion(
            id = "q_m04_t0",
            moduleId = "M04",
            nodeCode = "M04.T0",
            level = 1,
            question = "Geometri generasi pesawat CT manakah yang menjadi tulang punggung hampir seluruh pesawat MSCT diagnostik di rumah sakit saat ini?",
            options = listOf(
                "Generasi ke-1 (Translate-Rotate Pencil Beam)",
                "Generasi ke-3 (Rotate-Rotate Wide Fan/Cone Beam dengan tabung dan busur detektor berputar sinkron)",
                "Generasi ke-2 (Translate-Rotate Narrow Fan Beam)",
                "Generasi ke-5 (Electron Beam CT tanpa bagian berputar)"
            ),
            correctIndex = 1,
            explanation = "Geometri Generasi ke-3 (Rotate-Rotate) memungkinkan desain matriks detektor multi-baris yang kompak, efisien menggunakan anti-scatter septa, dan mampu berputar sangat cepat."
        ),
        CtQuizQuestion(
            id = "q_m04_t1_1",
            moduleId = "M04",
            nodeCode = "M04.T1.1",
            level = 1,
            question = "Apa perbedaan mekanis utama antara pesawat CT Generasi ke-3 dan Generasi ke-4, serta mengapa Generasi ke-3 lebih unggul untuk MSCT?",
            options = listOf(
                "Pada Generasi ke-3 tabung dan busur detektor berputar bersama-sama sehingga sekat anti-scatter septa dapat difokuskan ke tabung dan biaya matriks multi-baris jauh lebih efisien dibanding cincin detektor 360° statis pada Generasi ke-4",
                "Generasi ke-3 menggunakan berkas pensil, sedangkan Generasi ke-4 menggunakan gerakan translasi-rotasi",
                "Generasi ke-4 tidak menggunakan tabung sinar-X",
                "Generasi ke-3 memiliki detektor yang diam, sedangkan Generasi ke-4 memiliki detektor yang bergeser ke kiri dan kanan"
            ),
            correctIndex = 0,
            explanation = "Geometri Rotate-Rotate Generasi ke-3 menjaga sudut tetap antara fokus tabung dan busur detektor sehingga kisi anti-scatter septa bekerja optimal dan jumlah kristal detektor MSCT jauh lebih ekonomis dibanding cincin penuh 360 derajat Generasi ke-4."
        ),
        CtQuizQuestion(
            id = "q_m04_t1_2",
            moduleId = "M04",
            nodeCode = "M04.T1.2",
            level = 1,
            question = "Bagaimana pesawat Dual-Source CT (DSCT) dan teknik Prospective ECG-Triggering mengoptimalkan pemeriksaan CT Angiografi Koroner?",
            options = listOf(
                "DSCT memasang 2 tabung dan 2 detektor bersudut ~90° sehingga cukup berputar seperempat lingkaran (resolusi temporal ~66 ms), sedangkan Prospective ECG-Triggering memicu sinar-X hanya pada fase diastolik tenang (70–75% R-R) untuk menghemat dosis secara drastis",
                "DSCT menggunakan meriam elektron vakum tanpa detektor sama sekali",
                "Prospective ECG-Triggering menyalakan sinar-X maksimum terus-menerus selama 60 detik penuh",
                "Wide-Detector 320-slice hanya dapat memindai lebar 1 mm per rotasi"
            ),
            correctIndex = 0,
            explanation = "Dua sistem tabung-detektor bersudut 90 derajat pada DSCT menggandakan resolusi temporal untuk membekukan denyut koroner, dan Prospective Step-and-Shoot ECG-Gating membatasi eksposi hanya pada fase mid-diastolik."
        ),
        CtQuizQuestion(
            id = "q_m04_t2_1",
            moduleId = "M04",
            nodeCode = "M04.T2.1",
            level = 2,
            question = "Pada pesawat MSCT dengan konfigurasi detektor Matrix Array 64 baris aktif x 0,625 mm, komponen apakah yang menentukan ketebalan irisan minimum dan berapakah lebar kolimasi berkas total (W) per rotasi?",
            options = listOf(
                "Ditentukan oleh kecepatan anoda; lebar kolimasi total W = 16 mm",
                "Ditentukan oleh diameter bore gantry; lebar kolimasi total W = 32 mm",
                "Ketebalan irisan minimum ditentukan oleh lebar fisik satu baris detektor (0,625 mm) & binning DAS; total lebar kolimasi berkas W = N x T = 64 x 0,625 mm = 40 mm",
                "Ditentukan oleh tebal matras meja; lebar kolimasi total W = 64 mm"
            ),
            correctIndex = 2,
            explanation = "Pada MSCT, ketebalan irisan minimum ditentukan oleh lebar fisik elemen baris detektor terkecil (T = 0,625 mm) dan penggabungan kanal di DAS, dengan total cakupan kolimasi berkas W = N x T = 40 mm per rotasi."
        ),
        CtQuizQuestion(
            id = "q_m04_t2_2",
            moduleId = "M04",
            nodeCode = "M04.T2.2",
            level = 2,
            question = "Pesawat MSCT dengan kolimasi berkas total W = 40 mm melakukan scan helikal dengan pergeseran meja (Table Feed d) sebesar 48 mm per rotasi dan waktu rotasi 0,5 detik. Berapakah nilai Beam Pitch dan Kecepatan Meja (Table Speed)?",
            options = listOf(
                "Pitch = 1,2 dan Kecepatan Meja = 96 mm/detik",
                "Pitch = 0,83 dan Kecepatan Meja = 24 mm/detik",
                "Pitch = 1,0 dan Kecepatan Meja = 48 mm/detik",
                "Pitch = 1,5 dan Kecepatan Meja = 120 mm/detik"
            ),
            correctIndex = 0,
            explanation = "Beam Pitch = d / W = 48 mm / 40 mm = 1,2 (undersampling/celah helikal). Kecepatan meja (Table Speed) = d / t_rot = 48 mm / 0,5 detik = 96 mm/detik."
        ),
        CtQuizQuestion(
            id = "q_m04_t3_1",
            moduleId = "M04",
            nodeCode = "M04.T3.1",
            level = 3,
            question = "Apa dampak peningkatan Pitch dari 0,8 menjadi 1,4 terhadap kurva Slice Sensitivity Profile (SSP), serta apa fungsi kolimator Adaptive Dose Shield terkait fenomena Over-Ranging (Z-Overscanning)?",
            options = listOf(
                "Pitch 1,4 mempercepat waktu scan namun memperlebar kurva SSP (effective slice thickness bertambah tebal); sedangkan Adaptive Dose Shield menutup bilah kolimator secara dinamis di awal dan akhir scan helikal untuk memangkas dosis ekstra akibat putaran tambahan Over-Ranging",
                "Pitch 1,4 mempersempit kurva SSP menjadi nol dan membuat waktu scan dua kali lebih lambat",
                "Over-Ranging hanya terjadi pada mode Axial Step-and-Shoot saat meja diam",
                "Interpolasi z-axis tidak diperlukan sama sekali pada pemindaian helikal"
            ),
            correctIndex = 0,
            explanation = "Pitch > 1 merenggangkan titik sampel interpolasi z-axis sehingga kurva SSP melebar. Selain itu, mode helikal membutuhkan putaran ekstra di ujung volume (Over-Ranging) untuk interpolasi, yang dosisnya berhasil ditekan oleh kolimator asimetris Adaptive Dose Shield."
        ),

        // ==========================================
        // MODUL 5 (M05): Rekonstruksi Citra: Sinogram, FBP, Iterative & Kernel (5 kuis)
        // ==========================================
        CtQuizQuestion(
            id = "q_m05_t0",
            moduleId = "M05",
            nodeCode = "M05.T0",
            level = 1,
            question = "Mengapa algoritma Simple Back-Projection (proyeksi balik tanpa filter) tidak dapat digunakan langsung untuk menghasilkan citra CT diagnostik?",
            options = listOf(
                "Karena menghasilkan efek pengaburan bintang (star-like blurring 1/r) sehingga batas organ tampak sangat buram dan tidak tajam",
                "Karena membutuhkan waktu komputasi berhari-hari untuk satu irisan",
                "Karena menghapus seluruh struktur tulang dari gambar",
                "Karena hanya bekerja pada pesawat ultrasonografi"
            ),
            correctIndex = 0,
            explanation = "Proyeksi balik sederhana menumpuk nilai atenuasi di sepanjang garis lintasan sehingga setiap titik objek menyebar dengan fungsi kabur 1/r; diperlukan filter konvolusi frekuensi tinggi (Filtered Back-Projection) untuk meniadakan efek kabur tersebut."
        ),
        CtQuizQuestion(
            id = "q_m05_t1_1",
            moduleId = "M05",
            nodeCode = "M05.T1.1",
            level = 1,
            question = "Bagaimanakah jejak sebuah objek titik yang berada di luar pusat putaran (off-center) serta jejak satu kanal detektor yang rusak ketika dipetakan ke dalam matriks data mentah Sinogram?",
            options = listOf(
                "Objek off-center membentuk kurva gelombang sinus (sinusoidal) sepanjang rotasi 360°, sedangkan satu kanal detektor yang rusak membentuk garis lurus vertikal cacat yang memicu Ring Artifact",
                "Membentuk garis lurus horizontal yang terputus-putus",
                "Membentuk lingkaran konsentris di sudut kiri atas sinogram",
                "Tidak meninggalkan jejak apa pun di dalam sinogram"
            ),
            correctIndex = 0,
            explanation = "Saat gantry memutari objek off-center, posisi proyeksinya pada busur detektor berayun mengikuti fungsi sinus (Sinogram), sementara satu kanal detektor yang cacat pada posisi tetap akan menggambar garis vertikal lurus di Sinogram."
        ),
        CtQuizQuestion(
            id = "q_m05_t1_2",
            moduleId = "M05",
            nodeCode = "M05.T1.2",
            level = 1,
            question = "Apa fungsi tahapan konvolusi (filtering) menggunakan Ramp Filter (Lak-Filter) pada algoritma Filtered Back-Projection (FBP)?",
            options = listOf(
                "Mengkompensasi pengaburan 1/r dengan memperkuat frekuensi spasial tinggi dan menciptakan tepi gelombang negatif (negative side-lobes) yang saling menghilangkan kabur saat diproyeksikan balik",
                "Menambah jumlah foton sinar-X yang keluar dari tabung",
                "Mengubah nilai Hounsfield Unit air menjadi +1000 HU",
                "Menghapus seluruh struktur berfrekuensi tinggi seperti korteks tulang"
            ),
            correctIndex = 0,
            explanation = "Ramp filter memperkuat komponen frekuensi spasial tinggi pada profil proyeksi sebelum back-projection sehingga tepi objek kembali tajam dan bebas dari artefak kabur 1/r."
        ),
        CtQuizQuestion(
            id = "q_m05_t2_1",
            moduleId = "M05",
            nodeCode = "M05.T2.1",
            level = 2,
            question = "Seorang radiografer ingin melakukan rekonstruksi retrospektif (Retro-Recon) dari Raw Data trauma kepala menjadi dua seri: seri Parenkim Otak dan seri Tulang Tengkorak. Kombinasi Convolution Kernel manakah yang tepat?",
            options = listOf(
                "Seri Otak menggunakan Soft/Standard Kernel (meredam quantum noise agar kontras jaringan lunak grisea-alba jelas); Seri Tulang menggunakan Sharp/Bone Kernel (memperkuat frekuensi tinggi agar garis fraktur tajam meski noise meningkat)",
                "Seri Otak menggunakan Bone Kernel ultra-tajam; Seri Tulang menggunakan Smooth Kernel sangat halus",
                "Kedua seri harus dipindai ulang dua kali karena Kernel tidak dapat diubah dari Raw Data",
                "Kernel hanya mengubah kecerahan layar monitor tanpa memengaruhi ketajaman tepi maupun noise"
            ),
            correctIndex = 0,
            explanation = "Selama Raw Data (Sinogram) masih tersimpan di konsol, operator dapat melakukan Retro-Recon tanpa eksposi ulang: Soft Kernel meredam noise untuk diferensiasi jaringan lunak otak, sedangkan Sharp/Bone Kernel menajamkan trabekula dan garis fraktur tulang."
        ),
        CtQuizQuestion(
            id = "q_m05_t3_1",
            moduleId = "M05",
            nodeCode = "M05.T3.1",
            level = 3,
            question = "Apa keunggulan klinis utama algoritma Iterative Reconstruction (IR) dan AI Deep Learning Image Reconstruction (DLIR) dibandingkan FBP konvensional?",
            options = listOf(
                "Memodelkan statistik noise foton (Poisson), geometri optik sistem, dan jaringan saraf dalam sehingga mampu menekan quantum noise serta streak artifact secara drastis pada protokol Low-Dose CT (penghematan dosis 30–60%)",
                "Meningkatkan dosis radiasi pasien hingga 5 kali lipat agar gambar tampak lebih berbintik",
                "Menghilangkan kebutuhan kalibrasi udara pada detektor Generasi ke-3",
                "Mengubah akuisisi sinar-X menjadi pemindaian gelombang suara"
            ),
            correctIndex = 0,
            explanation = "Berbeda dengan FBP yang memperkuat noise pada dosis rendah, IR (melalui siklus forward/back-projection korektif) dan AI DLIR memisahkan sinyal anatomi dari derau statistik sehingga dosis radiasi dapat dipangkas 30–60% dengan tekstur citra tetap tajam."
        ),

        // ==========================================
        // MODUL 6 (M06): Matriks, Voxel Isotropik, Hounsfield Unit (HU) & Windowing (5 kuis)
        // ==========================================
        CtQuizQuestion(
            id = "q_m06_t0",
            moduleId = "M06",
            nodeCode = "M06.T0",
            level = 1,
            question = "Apa hubungan antara ukuran piksel matriks rekonstruksi, skala kuantitatif Hounsfield Unit (HU), dan pengaturan Windowing (WW/WL) pada citra CT?",
            options = listOf(
                "Ukuran piksel ditentukan oleh DFOV/Matrix, nilai atenuasi tiap voxel dikuantifikasi terhadap air murni dalam skala HU (Air = 0 HU, Udara = -1000 HU), dan WW/WL memetakan rentang HU target ke gradasi abu-abu layar monitor",
                "Nilai HU jaringan selalu berubah-ubah ketika radiografer menggeser Window Width (WW) dan Window Level (WL)",
                "Ukuran piksel hanya ditentukan oleh suhu ruangan konsol operator",
                "Air murni memiliki nilai +1000 HU dan tulang kortikal memiliki nilai -1000 HU"
            ),
            correctIndex = 0,
            explanation = "Geometri voxel ditentukan oleh DFOV/Matrix dan slice thickness, densitas jaringan distandarisasi dalam skala HU relatif terhadap air (0 HU), sedangkan WW/WL hanya mengatur tampilan kontras dan kecerahan di layar tanpa mengubah nilai HU asli di dalam data."
        ),
        CtQuizQuestion(
            id = "q_m06_t1_1",
            moduleId = "M06",
            nodeCode = "M06.T1.1",
            level = 1,
            question = "Jika pemeriksaan CT Kepala direkonstruksi menggunakan Display Field of View (DFOV) sebesar 256 mm pada matriks 512 x 512 dengan tebal irisan 0,5 mm, berapakah ukuran satu piksel dan apakah sifat voxel yang terbentuk?",
            options = listOf(
                "Ukuran piksel 2,0 mm (Voxel Anisotropik)",
                "Ukuran piksel 1,0 mm (Voxel Anisotropik)",
                "Ukuran piksel 0,5 mm; karena dx = dy = dz = 0,5 mm maka terbentuk Voxel Isotropik (kubus sempurna) untuk reformasi MPR bebas distorsi",
                "Ukuran piksel 0,25 mm"
            ),
            correctIndex = 2,
            explanation = "Pixel Size = DFOV / Matrix Size = 256 mm / 512 = 0,5 mm. Karena tebal irisan (dz) juga 0,5 mm, terbentuklah Voxel Isotropik (0,5 x 0,5 x 0,5 mm)."
        ),
        CtQuizQuestion(
            id = "q_m06_t1_2",
            moduleId = "M06",
            nodeCode = "M06.T1.2",
            level = 1,
            question = "Diketahui koefisien atenuasi linier air (mu_air) adalah 0,20 cm^-1. Jika suatu jaringan lesi memiliki mu_jaringan = 0,21 cm^-1, berapakah nilai Hounsfield Unit (HU) lesi tersebut?",
            options = listOf(
                "+50 HU",
                "+100 HU",
                "-50 HU",
                "0 HU"
            ),
            correctIndex = 0,
            explanation = "HU = 1000 x (0,21 - 0,20) / 0,20 = 1000 x (0,01 / 0,20) = 1000 x 0,05 = +50 HU (konsisten dengan densitas jaringan lunak/parenkim)."
        ),
        CtQuizQuestion(
            id = "q_m06_t2_1",
            moduleId = "M06",
            nodeCode = "M06.T2.1",
            level = 2,
            question = "Pada pengaturan Windowing CT Kepala rutin dengan Window Width (WW) = 80 HU dan Window Level (WL) = 40 HU, berapakah rentang nilai HU yang ditampilkan sebagai gradasi abu-abu sebelum menjadi hitam murni atau putih murni?",
            options = listOf(
                "Dari 0 HU (hitam murni) hingga +80 HU (putih murni)",
                "Dari -40 HU hingga +40 HU",
                "Dari +40 HU hingga +120 HU",
                "Dari -1000 HU hingga +1000 HU"
            ),
            correctIndex = 0,
            explanation = "Batas bawah hitam = WL - (WW/2) = 40 - 40 = 0 HU. Batas atas putih = WL + (WW/2) = 40 + 40 = +80 HU. Semua jaringan > +80 HU akan tampak putih jenuh."
        ),
        CtQuizQuestion(
            id = "q_m06_t3_1",
            moduleId = "M06",
            nodeCode = "M06.T3.1",
            level = 3,
            question = "Sebuah kista ginjal sederhana berukuran 3 mm (cairan murni 0 HU) dipindai dengan ketebalan irisan 5 mm sehingga separuh volume voxel terisi parenkim ginjal berpenyengatan kontras (+100 HU). Nilai terukur pada layar menjadi +50 HU. Fenomena apakah ini dan bagaimana cara mengatasinya?",
            options = listOf(
                "Partial Volume Effect (Averaging); diatasi dengan merekonstruksi irisan tipis (thin slice, misal 0,625 - 1 mm) agar voxel hanya berisi cairan kista murni",
                "Ring Artifact; diatasi dengan mengganti tabung sinar-X",
                "Motion Artifact; diatasi dengan memperlebar slice thickness menjadi 10 mm",
                "Metal Artifact; diatasi dengan menyuntikkan kontras tambahan"
            ),
            correctIndex = 0,
            explanation = "Ketika satu voxel tebal mencakup dua jaringan berbeda densitas, nilai HU yang dihitung adalah rata-rata proporsional keduanya (Partial Volume Averaging). Irisan tipis meminimalkan pencampuran volume di sumbu-z."
        ),

        // ==========================================
        // MODUL 7 (M07): Parameter Konsol, Kualitas Citra & Dosimetri CT (5 kuis)
        // ==========================================
        CtQuizQuestion(
            id = "q_m07_t0",
            moduleId = "M07",
            nodeCode = "M07.T0",
            level = 1,
            question = "Satuan standar internasional untuk menyatakan CTDIvol (Volume CT Dose Index) dan DLP (Dose Length Product) pada layar konsol CT berturut-turut adalah:",
            options = listOf(
                "mGy (milliGray) untuk CTDIvol dan mGy.cm (milliGray-centimeter) untuk DLP",
                "mSv untuk CTDIvol dan Becquerel untuk DLP",
                "kVp untuk CTDIvol dan mAs untuk DLP",
                "Hounsfield Unit untuk CTDIvol dan Tesla untuk DLP"
            ),
            correctIndex = 0,
            explanation = "CTDIvol mengukur intensitas dosis rata-rata per volume irisan dalam satuan mGy, sedangkan DLP mengalikan CTDIvol dengan panjang rentang scan (cm) sehingga satuannya mGy.cm."
        ),
        CtQuizQuestion(
            id = "q_m07_t1_1",
            moduleId = "M07",
            nodeCode = "M07.T1.1",
            level = 1,
            question = "Suatu protokol CT Abdomen menggunakan kuat arus tabung 300 mA, waktu rotasi gantry 0,5 detik, dan Pitch = 0,75. Berapakah nilai Effective mAs (mAs_efektif) protokol tersebut?",
            options = listOf(
                "150 mAs",
                "200 mAs",
                "112,5 mAs",
                "400 mAs"
            ),
            correctIndex = 1,
            explanation = "mAs per rotasi = mA x t_rot = 300 x 0,5 = 150 mAs. Effective mAs = mAs / Pitch = 150 / 0,75 = 200 mAs."
        ),
        CtQuizQuestion(
            id = "q_m07_t1_2",
            moduleId = "M07",
            nodeCode = "M07.T1.2",
            level = 1,
            question = "Berdasarkan hubungan statistik Quantum Noise sigma ~ 1 / sqrt(mAs), jika dokter radiologi meminta tingkat noise (standar deviasi HU) diturunkan menjadi setengah (50%) dari nilai semula tanpa mengubah slice thickness maupun kernel, bagaimana pengaturan mAs harus diubah?",
            options = listOf(
                "Menaikkan mAs menjadi 4 kali lipat dari nilai semula",
                "Menaikkan mAs menjadi 2 kali lipat dari nilai semula",
                "Menurunkan mAs menjadi setengah dari nilai semula",
                "Mengurangi waktu rotasi menjadi seperempatnya dengan mA tetap"
            ),
            correctIndex = 0,
            explanation = "Karena noise berbanding terbalik dengan akar kuadrat jumlah foton (mAs), untuk menekan noise menjadi 1/2 kali diperlukan peningkatan jumlah foton (mAs) sebesar 2^2 = 4 kali lipat."
        ),
        CtQuizQuestion(
            id = "q_m07_t2_1",
            moduleId = "M07",
            nodeCode = "M07.T2.1",
            level = 2,
            question = "Diketahui nilai CTDIw suatu protokol adalah 12 mGy dengan Pitch = 0,8 dan panjang area pemindaian (Scan Length L) sepanjang 30 cm. Berapakah nilai CTDIvol dan DLP pemeriksaan tersebut?",
            options = listOf(
                "CTDIvol = 15 mGy dan DLP = 450 mGy.cm",
                "CTDIvol = 9,6 mGy dan DLP = 288 mGy.cm",
                "CTDIvol = 12 mGy dan DLP = 360 mGy.cm",
                "CTDIvol = 20 mGy dan DLP = 600 mGy.cm"
            ),
            correctIndex = 0,
            explanation = "CTDIvol = CTDIw / Pitch = 12 mGy / 0,8 = 15 mGy. Selanjutnya DLP = CTDIvol x L = 15 mGy x 30 cm = 450 mGy.cm."
        ),
        CtQuizQuestion(
            id = "q_m07_t3_1",
            moduleId = "M07",
            nodeCode = "M07.T3.1",
            level = 3,
            question = "Mengapa posisi pasien yang tepat di Isocenter sangat krusial ketika fitur Automatic Exposure Control (AEC / Tube Current Modulation) diaktifkan?",
            options = listOf(
                "Karena jika meja disetel terlalu tinggi (dekat tabung pada scout AP), bayangan pasien pada topogram mengalami magnifikasi sehingga AEC salah menduga tubuh pasien lebih besar dan memberikan mA berlebih (overexposure)",
                "Karena AEC hanya bekerja jika pasien berbaring miring di tepi meja",
                "Karena posisi isocenter akan mematikan sistem modulasi arus tabung",
                "Karena AEC tidak menggunakan citra scout/topogram dalam menghitung arus mA"
            ),
            correctIndex = 0,
            explanation = "Algoritma AEC menghitung ukuran dan densitas pasien dari citra Scout/Topogram. Kesalahan penempatan ketinggian meja mengubah magnifikasi proyeksi scout dan memicu kesalahan estimasi arus mA secara sistematis."
        ),

        // ==========================================
        // MODUL 8 (M08): Injektor Kontras Iodin IV, Bolus Tracking & Post-Processing 3D (5 kuis)
        // ==========================================
        CtQuizQuestion(
            id = "q_m08_t0",
            moduleId = "M08",
            nodeCode = "M08.T0",
            level = 1,
            question = "Mengapa unsur Iodin (Z = 53, K-edge = 33,2 keV) sangat efektif digunakan sebagai bahan dasar media kontras intravaskular pada pencitraan CT Scan?",
            options = listOf(
                "Karena nomor atomnya yang tinggi dan energi K-edge 33,2 keV sangat sesuai dengan energi efektif berkas sinar-X diagnostik, sehingga meningkatkan serapan fotolistrik dan menaikkan nilai HU pembuluh darah secara tajam",
                "Karena Iodin bersifat paramagnetik yang memperpendek waktu relaksasi proton T1",
                "Karena Iodin memantulkan gelombang ultrasonik di dalam pembuluh darah",
                "Karena Iodin menurunkan nilai HU darah menjadi -1000 HU"
            ),
            correctIndex = 0,
            explanation = "Efek fotolistrik meningkat drastis tepat di atas energi ikat kulit K iodin (33,2 keV), terutama ketika dipadukan dengan tegangan tabung 80-120 kVp."
        ),
        CtQuizQuestion(
            id = "q_m08_t1_1",
            moduleId = "M08",
            nodeCode = "M08.T1.1",
            level = 1,
            question = "Pemeriksaan laboratorium apakah yang wajib dievaluasi sebelum menyuntikkan media kontras Iodin intravena guna mencegah risiko Contrast-Induced Acute Kidney Injury (CI-AKI)?",
            options = listOf(
                "Kadar Kreatinin Serum dan estimasi Laju Filtrasi Glomerulus (eGFR)",
                "Kadar Kolesterol Total dan Asam Urat",
                "Hitung Trombosit dan Waktu Perdarahan saja",
                "Kadar Bilirubin Direk dan Indirek"
            ),
            correctIndex = 0,
            explanation = "Media kontras iodin diekskresikan hampir seluruhnya melalui filtrasi glomerulus ginjal; skrining eGFR (< 30 mL/menit/1,73 m^2 berisiko tinggi) wajib dilakukan sebelum injeksi."
        ),
        CtQuizQuestion(
            id = "q_m08_t1_2",
            moduleId = "M08",
            nodeCode = "M08.T1.2",
            level = 1,
            question = "Apa tujuan pemberian Saline Chaser (30-50 mL NaCl 0,9%) menggunakan Dual-Head Power Injector tepat setelah penyuntikan media kontras pada CT Angiografi Toraks/Koroner?",
            options = listOf(
                "Mendorong sisa kontras di vena perifer agar bolus lebih padat sekaligus membilas vena kava superior dan jantung kanan untuk mencegah perivenous streak artifact yang mengaburkan arteri koroner",
                "Mengencerkan kontras di dalam aorta agar nilai HU turun menjadi 0 HU",
                "Meningkatkan tekanan di dalam vena hingga melebihi 600 PSI",
                "Menggantikan fungsi media kontras iodin sepenuhnya"
            ),
            correctIndex = 0,
            explanation = "Saline flush mendorong kolom kontras yang tersisa di selang/vena lengan menuju sirkulasi sentral dan membersihkan sisa kontras pekat di vena kava superior yang kerap menimbulkan artefak streak."
        ),
        CtQuizQuestion(
            id = "q_m08_t2_1",
            moduleId = "M08",
            nodeCode = "M08.T2.1",
            level = 2,
            question = "Pada pemeriksaan CT Pulmonary Angiography (CTPA) untuk mendeteksi emboli paru akut dengan teknik Bolus Tracking, di manakah ROI pemantauan harus diletakkan dan berapa kisaran ambang picu (trigger threshold) yang tepat?",
            options = listOf(
                "ROI diletakkan pada Trunkus Pulmonalis (Arteri Pulmonalis Utama) dengan trigger threshold sekitar +100 hingga +120 HU",
                "ROI diletakkan pada Aorta Abdominalis setinggi ginjal dengan delay 5 menit",
                "ROI diletakkan pada Vena Porta Hepatis dengan trigger +500 HU",
                "ROI diletakkan pada jaringan lemak subkutis dinding dada"
            ),
            correctIndex = 0,
            explanation = "Untuk memvisualisasikan pohon arteri pulmonalis secara optimal sebelum kontras memenuhi aorta dan vena pulmonalis, ROI Bolus Tracking ditempatkan langsung di Trunkus Pulmonalis."
        ),
        CtQuizQuestion(
            id = "q_m08_t3_1",
            moduleId = "M08",
            nodeCode = "M08.T3.1",
            level = 3,
            question = "Manakah pasangan teknik Post-Processing 3D dan indikasi klinis utamanya yang PALING TEPAT di bawah ini?",
            options = listOf(
                "Maximum Intensity Projection (MIP) untuk evaluasi cabang vaskular CT Angiografi & deteksi nodul paru; Minimum Intensity Projection (MinIP) untuk evaluasi percabangan trakeobronkial & emfisema",
                "MinIP untuk melihat tulang kortikal padat; MIP untuk melihat kantong udara emfisema",
                "Volume Rendering (VRT) hanya dapat menampilkan gambar hitam-putih 2D satu piksel",
                "Multiplanar Reformation (MPR) mengharuskan pasien dipindai ulang dalam posisi berdiri"
            ),
            correctIndex = 0,
            explanation = "MIP menonjolkan voxel ber-HU tertinggi (pembuluh darah berkontras, kalsifikasi, nodul paru), sedangkan MinIP menonjolkan voxel ber-HU terendah (saluran napas berisi udara dan area air-trapping)."
        ),

        // ==========================================
        // MODUL 9 (M09): Artefak Pesawat CT, Prosedur Pemanasan/Kalibrasi & QA Harian (5 kuis)
        // ==========================================
        CtQuizQuestion(
            id = "q_m09_t0",
            moduleId = "M09",
            nodeCode = "M09.T0",
            level = 1,
            question = "Dua prosedur pemeliharaan harian apakah yang wajib dijalankan oleh radiografer di konsol pesawat CT setiap pagi sebelum memulai pemeriksaan pasien pertama?",
            options = listOf(
                "Tube Warm-Up (Pemanasan Tabung) diikuti dengan Fast Air Calibration (Kalibrasi Udara dengan bore gantry kosong)",
                "Mengganti kristal detektor dan menguras oli pendingin tabung",
                "Melakukan eksposi energi maksimal langsung pada tabung dingin tanpa pemanasan",
                "Mencabut Slip-Ring dan mengubah tegangan listrik gedung"
            ),
            correctIndex = 0,
            explanation = "Tube Warm-Up mencegah thermal shock/retak pada anoda dingin, sedangkan Air Calibration menyamakan respons gain/offset seluruh kanal detektor di udara guna mencegah artefak cincin."
        ),
        CtQuizQuestion(
            id = "q_m09_t1_1",
            moduleId = "M09",
            nodeCode = "M09.T1.1",
            level = 1,
            question = "Pada citra CT Kepala non-kontras, tampak pita-pita gelap (dark bands) yang melintang di antara kedua tulang petrosus di fossa posterior sehingga mengaburkan batang otak. Artefak apakah ini dan apa penyebab fisiknya?",
            options = listOf(
                "Beam Hardening Artifact (Hounsfield Streak); disebabkan oleh penyerapan foton energi rendah secara selektif saat berkas polikromatik menembus tulang petrosus yang sangat padat",
                "Ring Artifact; disebabkan oleh kerusakan satu kanal detektor pada busur Generasi ke-3",
                "Motion Artifact; disebabkan oleh kedipan kelopak mata pasien",
                "Windmill Artifact; disebabkan oleh pergerakan meja yang berhenti total"
            ),
            correctIndex = 0,
            explanation = "Tulang petrosus yang padat menyerap foton berenergi rendah dan mengeraskan berkas sinar-X secara drastis, menghasilkan pita gelap (dark bands) dan cupping jika tidak dikoreksi."
        ),
        CtQuizQuestion(
            id = "q_m09_t1_2",
            moduleId = "M09",
            nodeCode = "M09.T1.2",
            level = 1,
            question = "Pada hasil potongan aksial CT Kepala, terlihat lingkaran konsentris tajam berpusat tepat di tengah gambar (Isocenter). Apa penyebab mekanis artefak ini dan tindakan korektif pertamanya?",
            options = listOf(
                "Ring Artifact akibat ketidakseimbangan gain/offset atau kerusakan pada satu elemen detektor di CT Generasi ke-3; tindakan pertama adalah mengosongkan bore gantry dan menjalankan Air Calibration",
                "Metal Artifact akibat anting telinga pasien; tindakan pertama adalah menaikkan Pitch menjadi 2,0",
                "Partial Volume Artifact; tindakan pertama adalah mempertebal irisan menjadi 10 mm",
                "Out-of-Field Artifact; tindakan pertama adalah menurunkan kVp menjadi 60 kV"
            ),
            correctIndex = 0,
            explanation = "Pada geometri Rotate-Rotate (Generasi ke-3), satu detektor yang melenceng kalibrasinya akan menggambar lintasan lingkaran konsentris terhadap isocenter; kalibrasi udara (Air Calibration) memperbarui tabel koreksi gain detektor tersebut."
        ),
        CtQuizQuestion(
            id = "q_m09_t2_1",
            moduleId = "M09",
            nodeCode = "M09.T2.1",
            level = 2,
            question = "Mengapa di dalam lubang bore gantry tidak boleh ada objek apa pun (termasuk bantal kepala atau meja pasien di lintasan berkas) saat menjalankan prosedur Air Calibration?",
            options = listOf(
                "Karena DAS merekam profil transmisi sinar-X menembus udara murni sebagai referensi dasar (flat-field); jika ada objek di dalam bore, atenuasi objek tersebut akan terekam ke dalam tabel kalibrasi dan justru menciptakan artefak cincin/shading pada seluruh pasien berikutnya",
                "Karena objek di dalam bore akan tersedot oleh medan magnet gantry",
                "Karena bantal kepala akan meledak jika terkena berkas kalibrasi",
                "Karena Air Calibration hanya mengukur suhu udara menggunakan termometer"
            ),
            correctIndex = 0,
            explanation = "Air Calibration membaca sinyal referensi udara tanpa halangan untuk menyamakan gain ribuan kanal detektor. Kehadiran benda asing di lintasan berkas akan mengacaukan tabel referensi DAS."
        ),
        CtQuizQuestion(
            id = "q_m09_t3_1",
            moduleId = "M09",
            nodeCode = "M09.T3.1",
            level = 3,
            question = "Dalam uji Quality Assurance (QA) harian menggunakan Fantom Air Silinder dengan 5 ROI (1 Pusat dan 4 Perifer di posisi jam 12, 3, 6, 9), berapakah batas toleransi standar untuk Akurasi CT Number Air dan Keseragaman (Uniformity)?",
            options = listOf(
                "Akurasi HU Air di ROI pusat berada dalam rentang 0 +/- 4 HU, dan selisih keseragaman (Uniformity) antara ROI perifer terhadap ROI pusat <= 5 HU",
                "Akurasi HU Air boleh antara -100 hingga +100 HU, dan Uniformity <= 50 HU",
                "Nilai HU Air harus tepat +1000 HU di pusat dan -1000 HU di tepi",
                "Standar deviasi (noise) harus bernilai tepat 0,0 HU tanpa fluktuasi"
            ),
            correctIndex = 0,
            explanation = "Standar fisika medis menetapkan nilai rata-rata CT Number air murni di pusat fantom wajib berada pada 0 +/- 4 HU (atau +/- 5 HU) dan perbedaan rata-rata HU tepi terhadap pusat (Uniformity) tidak boleh melampaui 5 HU."
        )
    )
}
