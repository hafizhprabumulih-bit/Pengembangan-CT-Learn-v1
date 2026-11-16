package com.example.data

internal object CtCurriculumQuizzes {
    val quizzes: List<QuizQuestion> = listOf(
        // ==========================================
        // MODUL 1 (M01): Fisika Dasar Sinar-X & Prinsip Atenuasi CT (6 kuis)
        // ==========================================
        QuizQuestion(
            id = "q_m01_t0",
            moduleId = "M01",
            nodeCode = "M01.T0",
            level = 1,
            question = "Apa keunggulan mendasar pencitraan tomografi (CT Scan) dibandingkan radiografi proyeksi konvensional?",
            options = listOf(
                "Menghilangkan superposisi (tumpang-tindih) struktur anatomi 3D ke dalam potongan aksial dan memiliki resolusi kontras jaringan lunak yang jauh lebih tinggi",
                "Menggunakan gelombang suara berfrekuensi tinggi sehingga bebas radiasi pengion sama sekali",
                "Menghasilkan citra proyeksi 2D tunggal tanpa memerlukan rekonstruksi komputer",
                "Tidak memerlukan tabung sinar-X maupun detektor dalam akuisisi datanya"
            ),
            correctIndex = 0,
            explanation = "CT Scan mengatasi masalah superposisi organ pada radiografi konvensional dengan mengakuisisi proyeksi atenuasi dari berbagai sudut 360 derajat dan merekonstruksinya menjadi potongan melintang beresolusi kontras tinggi."
        ),
        QuizQuestion(
            id = "q_m01_t1_1",
            moduleId = "M01",
            nodeCode = "M01.T1.1",
            level = 1,
            question = "Pada tabung sinar-X pesawat CT dengan target anoda Tungsten (Z = 74), bagaimana pengaruh peningkatan kVp dari 80 kV ke 120 kV terhadap spektrum foton sinar-X?",
            options = listOf(
                "Menurunkan energi maksimum dan mengurangi jumlah foton yang dipancarkan",
                "Meningkatkan energi maksimum foton (keV puncak), menggeser energi efektif ke arah lebih tinggi, dan meningkatkan kuantitas foton secara eksponensial",
                "Hanya meningkatkan jumlah elektron tanpa mengubah daya tembus berkas sinar-X",
                "Menghilangkan seluruh radiasi Bremsstrahlung sehingga hanya menyisakan sinar-X karakteristik"
            ),
            correctIndex = 1,
            explanation = "Kenaikan kVp mempercepat elektron dengan energi kinetik lebih tinggi sehingga baik daya tembus (energi puncak & efektif) maupun intensitas total sinar-X meningkat secara signifikan."
        ),
        QuizQuestion(
            id = "q_m01_t1_2",
            moduleId = "M01",
            nodeCode = "M01.T1.2",
            level = 1,
            question = "Mengapa filtrasi pada tabung pesawat CT (inherent + bow-tie filter) sangat penting terhadap berkas sinar-X yang bersifat polikromatik?",
            options = listOf(
                "Untuk menyerap foton berenergi rendah (soft X-rays) yang hanya menambah dosis kulit pasien tanpa berkontribusi pada pembentukan citra di detektor",
                "Untuk mengubah foton sinar-X menjadi gelombang radio frekuensi rendah",
                "Untuk menurunkan suhu ruangan operator konsol",
                "Untuk memperbesar ukuran focal spot pada anoda"
            ),
            correctIndex = 0,
            explanation = "Filtrasi menyerap foton energi rendah dari spektrum polikromatik sehingga berkas menjadi lebih homogen (beam hardening terkontrol) dan dosis permukaan tubuh pasien berkurang drastis."
        ),
        QuizQuestion(
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
        QuizQuestion(
            id = "q_m01_t2_2",
            moduleId = "M01",
            nodeCode = "M01.T2.2",
            level = 2,
            question = "Berdasarkan Hukum Beer-Lambert I = I0 * exp(-mu * x), jika seberkas sinar-X menembus jaringan homogen setebal 2 kali nilai Half-Value Layer (2 x HVL), berapa persen intensitas foton yang berhasil diteruskan?",
            options = listOf(
                "75% dari intensitas awal (I0)",
                "50% dari intensitas awal (I0)",
                "25% dari intensitas awal (I0)",
                "12,5% dari intensitas awal (I0)"
            ),
            correctIndex = 2,
            explanation = "Satu HVL mereduksi intensitas menjadi 50% (1/2). Melewati ketebalan 2 x HVL mereduksi intensitas menjadi (1/2)^2 = 1/4 atau 25% dari I0."
        ),
        QuizQuestion(
            id = "q_m01_t3_1",
            moduleId = "M01",
            nodeCode = "M01.T3.1",
            level = 3,
            question = "Dalam akuisisi CT, bagian Data Acquisition System (DAS) melakukan transformasi logaritmik ln(I0 / I) terhadap sinyal detektor. Nilai fisik apakah yang diperoleh dari operasi matematis ln(I0 / I) tersebut?",
            options = listOf(
                "Integral garis koefisien atenuasi linier (Sigma mu_i * delta_x) di sepanjang lintasan berkas sinar-X",
                "Suhu pemanasan anoda tabung sinar-X dalam satuan Mega Heat Unit (MHU)",
                "Kecepatan pergerakan meja pasien dalam milimeter per detik",
                "Jumlah total hamburan Compton yang keluar dari ruang pemeriksaan"
            ),
            correctIndex = 0,
            explanation = "Dari persamaan I = I0 * exp(-Integral mu dx), pengambilan logaritma natural ln(I0 / I) menghasilkan nilai total penjumlahan koefisien atenuasi linier sepanjang garis proyeksi (ray sum) yang menjadi bahan baku sinogram."
        ),

        // ==========================================
        // MODUL 2 (M02): Anatomi Pesawat CT Scan & Arsitektur Gantry (6 kuis)
        // ==========================================
        QuizQuestion(
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
        QuizQuestion(
            id = "q_m02_t1_1",
            moduleId = "M02",
            nodeCode = "M02.T1.1",
            level = 1,
            question = "Di manakah posisi komputer akuisisi & rekonstruksi serta panel kontrol eksposi ditempatkan untuk melindungi radiografer dari radiasi hambur?",
            options = listOf(
                "Tepat di sebelah meja pasien di dalam ruang gantry tanpa pelindung",
                "Di Ruang Konsol Operator yang dipisahkan oleh dinding berlapis timbal (Pb) dan dilengkapi kaca pengawas ekuivalen timbal (lead glass) serta sistem interkom dua arah",
                "Di dalam penutup berputar (rotating frame) gantry",
                "Di ruang tunggu pasien tanpa jendela pengawas"
            ),
            correctIndex = 1,
            explanation = "Konsol operator wajib berada di ruang terpisah yang dilapisi perisai timbal (Pb) dengan jendela kaca timbal agar operator dapat mengawasi dan berkomunikasi dengan pasien secara aman."
        ),
        QuizQuestion(
            id = "q_m02_t1_2",
            moduleId = "M02",
            nodeCode = "M02.T1.2",
            level = 1,
            question = "Berapakah rentang diameter lubang bukaan gantry (gantry aperture/bore) standar pada pesawat CT diagnostik dan apa manfaat klinis kemiringan gantry (gantry tilt)?",
            options = listOf(
                "Diameter 20-30 cm; gantry tilt digunakan untuk mempercepat putaran tabung",
                "Diameter 70-85 cm; gantry tilt (+/- 30 derajat) memungkinkan penyudutan bidang irisan kepala sejajar garis orbitomeatal guna melindungi lensa mata dari berkas primer dan mengurangi artefak fossa posterior",
                "Diameter 150-200 cm; gantry tilt digunakan untuk memiringkan meja pasien ke samping",
                "Diameter 40 cm; gantry tilt hanya berfungsi saat pesawat dimatikan"
            ),
            correctIndex = 1,
            explanation = "Bore gantry umumnya berukuran 70 cm (standar) hingga 80-85 cm (wide-bore untuk radioterapi/bariatrik/intervensi), dan gantry tilt membantu menyudutkan berkas agar terhindar dari lensa mata atau tambalan gigi."
        ),
        QuizQuestion(
            id = "q_m02_t2_1",
            moduleId = "M02",
            nodeCode = "M02.T2.1",
            level = 2,
            question = "Ketika penutup depan (front cover) Gantry pesawat CT dibuka, bagaimana susunan geometris antara Tabung Sinar-X (X-Ray Tube) dan Busur Detektor (Detector Array Arc) pada rangka putar (Rotating Frame)?",
            options = listOf(
                "Tabung Sinar-X dan Busur Detektor dipasang berdampingan pada sisi yang sama",
                "Tabung Sinar-X dan Busur Detektor dipasang saling berhadapan tepat 180 derajat mengapit pusat putaran (Isocenter), diimbangi oleh High-Voltage Generator dan Heat Exchanger pada rangka putar",
                "Tabung Sinar-X berputar sementara Busur Detektor diletakkan di ruang konsol operator",
                "Busur Detektor berada di bawah meja pasien dan tidak ikut berputar"
            ),
            correctIndex = 1,
            explanation = "Pada CT Generasi ke-3 modern, X-Ray Tube dan Detector Array Arc dipasang saling berhadapan 180 derajat pada drum berputar (rotating frame), sementara HV Generator dan sistem pendingin dipasang di sisi penyeimbang untuk menjaga keseimbangan sentrifugal."
        ),
        QuizQuestion(
            id = "q_m02_t2_2",
            moduleId = "M02",
            nodeCode = "M02.T2.2",
            level = 2,
            question = "Komponen apakah yang terletak di antara tabung sinar-X dan pasien, berbentuk tebal di kedua ujung tepi dan tipis di bagian tengah, yang berfungsi menyeragamkan intensitas berkas sesuai penampang tubuh pasien yang elips/silindris?",
            options = listOf(
                "Post-Patient Anti-Scatter Grid",
                "Bow-Tie Filter (Filter Dasi Kupu-Kupu)",
                "Slip-Ring Brush Block",
                "Photodiode Array"
            ),
            correctIndex = 1,
            explanation = "Bow-Tie Filter berbentuk menyerupai dasi kupu-kupu (tipis di tengah, tebal di tepi) untuk mengatenuasi lebih banyak sinar-X di bagian tepi tubuh yang tipis sehingga mengurangi dosis kulit perifer dan menyeimbangkan sinyal yang masuk ke detektor."
        ),
        QuizQuestion(
            id = "q_m02_t3_1",
            moduleId = "M02",
            nodeCode = "M02.T3.1",
            level = 3,
            question = "Apa konsekuensi teknis apabila radiografer menyetel ketinggian vertikal meja pasien terlalu rendah atau terlalu tinggi (Off-Centering vertikal 4-6 cm dari titik Laser Isocenter)?",
            options = listOf(
                "Bagian tubuh yang tebal meleset dari bagian tengah Bow-Tie Filter, menyebabkan peningkatan quantum noise, potensi kesalahan modulasi arus AEC (over/under-exposure), dan degradasi keseragaman nilai HU",
                "Kualitas citra justru meningkat tajam dan dosis radiasi turun menjadi nol",
                "Pesawat CT otomatis berubah menjadi pesawat MRI",
                "Tidak ada pengaruh sama sekali karena Bow-Tie Filter memiliki ketebalan yang rata di semua titik"
            ),
            correctIndex = 0,
            explanation = "Bow-Tie Filter dan sistem AEC dirancang dengan asumsi pusat anatomi pasien berada tepat di Isocenter. Off-centering vertikal menyebabkan ketidaksesuaian profil atenuasi bow-tie filter dan magnifikasi topogram yang mengacaukan dosis AEC serta menambah noise."
        ),

        // ==========================================
        // MODUL 3 (M03): Instrumentasi Tabung Sinar-X CT, Generator, Slip-Ring & Detektor (7 kuis)
        // ==========================================
        QuizQuestion(
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
        QuizQuestion(
            id = "q_m03_t1_1",
            moduleId = "M03",
            nodeCode = "M03.T1.1",
            level = 1,
            question = "Mengapa generator tegangan tinggi (High-Frequency Inverter HV Generator) pada pesawat CT modern dipasang langsung di atas rangka berputar (rotating frame) gantry?",
            options = listOf(
                "Untuk menghilangkan kebutuhan kabel tegangan tinggi (HV cables) yang kaku dan memungkinkan putaran gantry 360 derajat secara kontinu bersama teknologi Slip-Ring",
                "Agar operator dapat menyentuh generator secara langsung saat gantry berputar",
                "Karena generator tegangan tinggi tidak membutuhkan pasokan listrik sama sekali",
                "Untuk menambah beban meja pasien saat bergerak longitudinal"
            ),
            correctIndex = 0,
            explanation = "Dengan menempatkan HV Generator kompak berfrekuensi tinggi langsung di atas rotating frame dan menyuplai tegangan rendah/menengah lewat Slip-Ring, pesawat CT terbebas dari lilitan kabel tegangan tinggi."
        ),
        QuizQuestion(
            id = "q_m03_t1_2",
            moduleId = "M03",
            nodeCode = "M03.T1.2",
            level = 1,
            question = "Inovasi elektromekanis apakah yang menggantikan kabel konvensional dengan cincin konduktif melingkar sehingga memungkinkan lahirnya pemindaian Spiral / Helical CT?",
            options = listOf(
                "Bucky Grid Bergerak",
                "Teknologi Slip-Ring (Cincin Luncur Elektromekanis & Transmisi Data Optik Nirkabel)",
                "Tabung Anoda Diam (Stationary Anode)",
                "Detektor Gas Xenon Bertekanan"
            ),
            correctIndex = 1,
            explanation = "Teknologi Slip-Ring memungkinkan transfer daya listrik dan data berkecepatan tinggi melintasi antarmuka statis-ke-putar tanpa kabel yang terlilit, menjadi kunci utama akuisisi helikal/spiral."
        ),
        QuizQuestion(
            id = "q_m03_t2_1",
            moduleId = "M03",
            nodeCode = "M03.T2.1",
            level = 2,
            question = "Apa fungsi teknologi z-Flying Focal Spot (defleksi elektromagnetik berkas elektron pada anoda) pada tabung sinar-X CT modern?",
            options = listOf(
                "Mengayunkan titik fokus secara bolak-balik sepanjang sumbu-z di antara dua posisi sehingga menggandakan jumlah irisan yang disampel per rotasi (misal 64 baris detektor menjadi 128 slice) dan menekan artefak windmill",
                "Mematikan tabung sinar-X setiap setengah putaran gantry",
                "Mengubah bahan anoda dari Tungsten menjadi Tembaga",
                "Memperbesar ukuran focal spot menjadi 10 mm agar gambar tampak kabur"
            ),
            correctIndex = 0,
            explanation = "z-Flying Focal Spot menggeser titik tumbukan berkas elektron secara elektromagnetik sepanjang sumbu-z sehingga setiap baris detektor membaca dua proyeksi z yang saling berselangseling (double z-sampling)."
        ),
        QuizQuestion(
            id = "q_m03_t2_2",
            moduleId = "M03",
            nodeCode = "M03.T2.2",
            level = 2,
            question = "Apa perbedaan utama antara Low-Voltage Slip-Ring dan High-Voltage Slip-Ring pada desain pesawat CT?",
            options = listOf(
                "Pada Low-Voltage Slip-Ring, cincin luncur menyalurkan tegangan AC rendah/menengah menuju HV Generator yang ikut berputar di dalam gantry; pada High-Voltage Slip-Ring, generator HV berada di luar gantry dan cincin menyalurkan tegangan tinggi langsung ke tabung",
                "Low-Voltage Slip-Ring menggunakan kabel panjang yang harus diputar balik setiap 360 derajat",
                "High-Voltage Slip-Ring tidak memerlukan tabung sinar-X",
                "Tidak ada perbedaan lokasi penempatan generator tegangan tinggi pada keduanya"
            ),
            correctIndex = 0,
            explanation = "Desain modern umumnya memilih Low-Voltage Slip-Ring karena lebih aman dari loncatan bunga api tegangan tinggi (arcing), dengan menempatkan inverter HV Generator langsung di rangka putar gantry."
        ),
        QuizQuestion(
            id = "q_m03_t3_1",
            moduleId = "M03",
            nodeCode = "M03.T3.1",
            level = 3,
            question = "Mengapa seluruh pesawat Multi-Slice CT (MSCT) modern menggunakan Solid-State Scintillation Detector (seperti Gd2O2S / GOS atau CdWO4) dan meninggalkan detektor tabung gas Xenon?",
            options = listOf(
                "Karena detektor Solid-State memiliki efisiensi penyerapan kuantum (DQE ~95-99%) yang sangat tinggi, afterglow sangat singkat, dan dapat disusun menjadi matriks multi-baris sepanjang sumbu-z tanpa kendala sekat ruang gas",
                "Karena detektor gas Xenon terlalu padat sehingga memantulkan sinar-X kembali ke tabung",
                "Karena detektor Solid-State tidak membutuhkan fotodioda maupun listrik",
                "Karena gas Xenon hanya dapat mendeteksi gelombang cahaya tampak"
            ),
            correctIndex = 0,
            explanation = "Detektor solid-state keramik memiliki kerapatan tinggi (efisiensi >95%) dan dapat dipotong menjadi petak-petak matriks 2D untuk MSCT, sedangkan detektor gas Xenon membutuhkan bilik panjang searah berkas yang tidak praktis dibagi menjadi multi-baris."
        ),
        QuizQuestion(
            id = "q_m03_t3_2",
            moduleId = "M03",
            nodeCode = "M03.T3.2",
            level = 3,
            question = "Apa peran Post-Patient Collimator (Anti-Scatter Septa) dan fungsi penguat logaritmik di dalam modul Data Acquisition System (DAS)?",
            options = listOf(
                "Septa anti-scatter menolak foton hamburan Compton yang datang miring ke detektor; sementara DAS mengubah arus analog fotodioda menjadi data digital dan melakukan operasi ln(I0/I) sebelum dikirim via pemancar optik ke komputer rekonstruksi",
                "Septa anti-scatter berfungsi memfokuskan cahaya laser isocenter, sedangkan DAS berfungsi memutar meja pasien",
                "DAS berfungsi mendinginkan oli tabung sinar-X menggunakan kipas udara",
                "Post-Patient Collimator berfungsi menghasilkan tegangan 140 kVp"
            ),
            correctIndex = 0,
            explanation = "Anti-scatter grid di permukaan detektor mencegah hamburan Compton merusak kontras citra, sedangkan DAS mengintegrasikan arus fotodioda, mendigitalkannya (ADC), dan menghitung atenuasi logaritmik ln(I0/I)."
        ),

        // ==========================================
        // MODUL 4 (M04): Evolusi Generasi Pesawat CT, Geometri MSCT & Akuisisi Helikal (6 kuis)
        // ==========================================
        QuizQuestion(
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
        QuizQuestion(
            id = "q_m04_t1_1",
            moduleId = "M04",
            nodeCode = "M04.T1.1",
            level = 1,
            question = "Apa perbedaan mekanis utama antara pesawat CT Generasi ke-3 dan Generasi ke-4?",
            options = listOf(
                "Pada Generasi ke-3 tabung dan busur detektor berputar bersama-sama; sedangkan pada Generasi ke-4 hanya tabung sinar-X yang berputar di dalam cincin detektor statis 360 derajat (Rotate-Stationary)",
                "Generasi ke-3 menggunakan berkas pensil, sedangkan Generasi ke-4 menggunakan gerakan translasi-rotasi",
                "Generasi ke-4 tidak menggunakan tabung sinar-X",
                "Generasi ke-3 memiliki detektor yang diam, sedangkan Generasi ke-4 memiliki detektor yang bergeser ke kiri dan kanan"
            ),
            correctIndex = 0,
            explanation = "CT Generasi ke-4 menggunakan cincin detektor penuh 360 derajat yang diam (stationary ring) sementara hanya tabung sinar-X yang berputar; desain ini kebal terhadap ring artifact namun terlalu mahal untuk dijadikan MSCT multi-baris."
        ),
        QuizQuestion(
            id = "q_m04_t1_2",
            moduleId = "M04",
            nodeCode = "M04.T1.2",
            level = 1,
            question = "Pada pesawat Multi-Slice CT (MSCT), komponen apakah yang sebenarnya menentukan ketebalan irisan minimum (minimum slice thickness) yang dapat diakuisisi?",
            options = listOf(
                "Lebar fisik elemen baris detektor tunggal sepanjang sumbu-z dan pengelompokan elektroniknya (detector row binning) di dalam DAS, bukan semata-mata lebar bukaan kolimator pra-pasien",
                "Kecepatan putaran anoda di dalam tabung sinar-X",
                "Diameter lubang bore gantry",
                "Ketebalan matras busa di atas meja pasien"
            ),
            correctIndex = 0,
            explanation = "Berbeda dengan SSCT di mana ketebalan irisan murni diatur oleh bukaan kolimator, pada MSCT berkas sinar-X berbentuk kerucut lebar dan ketebalan irisan ditentukan oleh lebar baris detektor individu (misal 0,5 atau 0,625 mm) serta penggabungan sinyal baris di DAS."
        ),
        QuizQuestion(
            id = "q_m04_t2_1",
            moduleId = "M04",
            nodeCode = "M04.T2.1",
            level = 2,
            question = "Sebuah detektor MSCT tipe Matrix Array memiliki 64 baris aktif dengan lebar masing-masing baris 0,625 mm (konfigurasi 64 x 0,625 mm). Berapakah lebar cakupan berkas total (Total Beam Collimation W) per satu rotasi pada Isocenter?",
            options = listOf(
                "16 mm",
                "32 mm",
                "40 mm",
                "64 mm"
            ),
            correctIndex = 2,
            explanation = "Total lebar kolimasi berkas W = Jumlah baris detektor aktif (N) x lebar satu baris (T) = 64 x 0,625 mm = 40 mm (4 cm) per rotasi."
        ),
        QuizQuestion(
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
        QuizQuestion(
            id = "q_m04_t3_1",
            moduleId = "M04",
            nodeCode = "M04.T3.1",
            level = 3,
            question = "Apa dampak peningkatan nilai Pitch dari 0,8 menjadi 1,4 terhadap kurva Slice Sensitivity Profile (SSP) dan proses z-interpolation pada akuisisi helikal?",
            options = listOf(
                "Waktu pemindaian menjadi lebih cepat, namun kurva SSP melebar (ketebalan irisan efektif meningkat) sehingga resolusi aksial sumbu-z sedikit menurun dan potensi artefak helikal meningkat",
                "Kurva SSP menjadi semakin sempit dan ketebalan irisan efektif mengecil menjadi nol",
                "Waktu pemindaian menjadi dua kali lebih lambat",
                "Interpolasi z tidak lagi diperlukan pada pemindaian helikal"
            ),
            correctIndex = 0,
            explanation = "Pitch > 1 mempercepat cakupan anatomi dan menurunkan dosis (bila mA tetap), tetapi merenggangkan titik sampel z sehingga interpolasi memperlebar profil sensitivitas irisan (effective slice thickness bertambah)."
        ),

        // ==========================================
        // MODUL 5 (M05): Rekonstruksi Citra: Sinogram, FBP, Iterative & Kernel (5 kuis)
        // ==========================================
        QuizQuestion(
            id = "q_m05_t0",
            moduleId = "M05",
            nodeCode = "M05.T0",
            level = 1,
            question = "Mengapa algoritma Simple Back-Projection (proyeksi balik tanpa filter) tidak dapat digunakan langsung untuk menghasilkan citra CT diagnostik?",
            options = listOf(
                "Karena menghasilkan efek pengaburan bintang (star-like blurring 1/r) sehingga batas organ tampak sangat buram dan tidak tajam",
                "Karena membutuhkan waktu komputasi berhari-hari untuk satu irisan",
                "Karena menghapus seluruh struktur tulang dari gambar",
                "Karena hanya bekerja pada pesawat MRI"
            ),
            correctIndex = 0,
            explanation = "Proyeksi balik sederhana menumpuk nilai atenuasi di sepanjang garis lintasan sehingga setiap titik objek menyebar dengan fungsi kabur 1/r; diperlukan filter konvolusi frekuensi tinggi (Filtered Back-Projection) untuk meniadakan efek kabur tersebut."
        ),
        QuizQuestion(
            id = "q_m05_t1_1",
            moduleId = "M05",
            nodeCode = "M05.T1.1",
            level = 1,
            question = "Bagaimanakah jejak sebuah objek titik yang berada di luar pusat putaran (off-center) ketika dipetakan ke dalam matriks data mentah Sinogram (posisi detektor s terhadap sudut proyeksi theta)?",
            options = listOf(
                "Membentuk kurva gelombang sinus (sinusoidal) yang mulus sepanjang rotasi 360 derajat",
                "Membentuk garis lurus horizontal yang terputus-putus",
                "Membentuk lingkaran konsentris di sudut kiri atas sinogram",
                "Tidak meninggalkan jejak apa pun di dalam sinogram"
            ),
            correctIndex = 0,
            explanation = "Saat gantry memutari objek off-center, posisi proyeksi objek pada busur detektor berayun ke kiri dan ke kanan mengikuti fungsi sinus terhadap sudut putar, itulah sebabnya representasi data mentah disebut Sinogram."
        ),
        QuizQuestion(
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
        QuizQuestion(
            id = "q_m05_t2_1",
            moduleId = "M05",
            nodeCode = "M05.T2.1",
            level = 2,
            question = "Apa keunggulan klinis utama algoritma Iterative Reconstruction (IR) dan Deep Learning Image Reconstruction (DLIR) dibandingkan FBP konvensional?",
            options = listOf(
                "Memodelkan statistik noise foton dan geometri optik sistem sehingga mampu menekan quantum noise dan streak artifact secara signifikan pada protokol CT dosis rendah (Low-Dose CT)",
                "Meningkatkan dosis radiasi pasien hingga 5 kali lipat",
                "Menghilangkan kebutuhan kalibrasi udara pada detektor",
                "Mengubah akuisisi aksial menjadi pemindaian ultrasonografi"
            ),
            correctIndex = 0,
            explanation = "Berbeda dengan FBP yang mengasumsikan data bebas noise, IR dan DLIR memisahkan sinyal anatomi dari derau statistik sehingga memungkinkan pengurangan dosis radiasi 30-60% dengan kualitas citra tetap prima."
        ),
        QuizQuestion(
            id = "q_m05_t3_1",
            moduleId = "M05",
            nodeCode = "M05.T3.1",
            level = 3,
            question = "Seorang radiografer ingin melakukan rekonstruksi retrospektif (Retro-Recon) dari data mentah (raw data) trauma kepala menjadi dua seri: seri Parenkim Otak dan seri Tulang Tengkorak. Kombinasi Kernel manakah yang tepat?",
            options = listOf(
                "Seri Otak menggunakan Soft/Standard Kernel (noise rendah, kontras jaringan lunak baik); Seri Tulang menggunakan Sharp/Bone Kernel (resolusi spasial tepi tinggi, noise lebih tinggi)",
                "Seri Otak menggunakan Bone Kernel ultra-tajam; Seri Tulang menggunakan Smooth Kernel sangat halus",
                "Kedua seri harus dipindai ulang (re-scan) dua kali karena kernel tidak dapat diubah dari raw data",
                "Kernel hanya mengubah warna layar monitor tanpa memengaruhi frekuensi spasial citra"
            ),
            correctIndex = 0,
            explanation = "Selama raw data (sinogram) masih tersimpan di konsol, operator dapat melakukan Retro-Recon tanpa eksposi ulang: Soft Kernel meredam noise untuk diferensiasi substansia grisea-alba otak, sedangkan Bone Kernel menajamkan garis fraktur tulang."
        ),

        // ==========================================
        // MODUL 6 (M06): Matriks, Voxel Isotropik, Hounsfield Unit (HU) & Windowing (5 kuis)
        // ==========================================
        QuizQuestion(
            id = "q_m06_t0",
            moduleId = "M06",
            nodeCode = "M06.T0",
            level = 1,
            question = "Apa syarat geometris agar sebuah elemen volume (voxel) pada dataset CT disebut sebagai Voxel Isotropik?",
            options = listOf(
                "Ukuran piksel pada bidang x-y sama persis dengan ketebalan irisan pada sumbu-z (dx = dy = dz), membentuk kubus sempurna",
                "Ketebalan irisan (dz) harus 10 kali lebih besar daripada ukuran piksel (dx)",
                "Nilai Hounsfield Unit di seluruh piksel harus bernilai 0 HU",
                "Scan FOV harus disetel maksimal 70 cm"
            ),
            correctIndex = 0,
            explanation = "Voxel isotropik memiliki dimensi yang sama di ketiga sumbu (misal 0,5 x 0,5 x 0,5 mm), sehingga potongan reformasi koronal dan sagital (MPR) memiliki resolusi spasial setajam potongan aksial."
        ),
        QuizQuestion(
            id = "q_m06_t1_1",
            moduleId = "M06",
            nodeCode = "M06.T1.1",
            level = 1,
            question = "Jika pemeriksaan CT Kepala direkonstruksi menggunakan Display Field of View (DFOV) sebesar 256 mm pada matriks 512 x 512, berapakah ukuran satu piksel (Pixel Size)?",
            options = listOf(
                "2,0 mm",
                "1,0 mm",
                "0,5 mm",
                "0,25 mm"
            ),
            correctIndex = 2,
            explanation = "Pixel Size = DFOV / Matrix Size = 256 mm / 512 = 0,5 mm."
        ),
        QuizQuestion(
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
        QuizQuestion(
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
        QuizQuestion(
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
        QuizQuestion(
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
        QuizQuestion(
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
        QuizQuestion(
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
        QuizQuestion(
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
        QuizQuestion(
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
        QuizQuestion(
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
        QuizQuestion(
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
        QuizQuestion(
            id = "q_m08_t1_2",
            moduleId = "M08",
            nodeCode = "M08.T1.2",
            level = 1,
            question = "Apa tujuan pemberian Saline Chaser (30-50 mL NaCl 0,9%) menggunakan Dual-Head Power Injector tepat setelah penyuntikan media kontras pada CT Angiografi Toraks/Koroner?",
            options = listOf(
                "Mendorong sisa kontras di vena perifer agar bolus lebih padat sekaligus membilas vena kava superior dan jantung kanan untuk mencegah streak artifact yang mengaburkan arteri koroner",
                "Mengencerkan kontras di dalam aorta agar nilai HU turun menjadi 0 HU",
                "Meningkatkan tekanan di dalam vena hingga melebihi 600 PSI",
                "Menggantikan fungsi media kontras iodin sepenuhnya"
            ),
            correctIndex = 0,
            explanation = "Saline flush mendorong kolom kontras yang tersisa di selang/vena lengan menuju sirkulasi sentral dan membersihkan sisa kontras pekat di vena kava superior yang kerap menimbulkan artefak streak."
        ),
        QuizQuestion(
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
        QuizQuestion(
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
        QuizQuestion(
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
        QuizQuestion(
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
        QuizQuestion(
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
        QuizQuestion(
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
        QuizQuestion(
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
