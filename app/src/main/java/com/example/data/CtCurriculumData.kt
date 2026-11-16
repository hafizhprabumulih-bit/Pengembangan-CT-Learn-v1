package com.example.data

import com.example.data.local.CtModuleEntity
import com.example.data.local.CurriculumNodeEntity
import com.example.data.local.CurriculumQuizEntity

data class CtInstrumentSpec(
    val id: String,
    val subsystem: String,
    val materialOrTech: String,
    val nominalRating: String,
    val roleInChain: String
)

object CtCurriculumData {
    val instrumentSpecs: List<CtInstrumentSpec> = listOf(
        CtInstrumentSpec(
            id = "spec_tube",
            subsystem = "X-Ray Tube CT Assembly",
            materialOrTech = "Anoda Tungsten-Rhenium-Molybdenum-Graphite + Liquid Metal Bearing (Ga-In-Sn)",
            nominalRating = "5–8+ MHU (disipasi 800–1.500 kHU/mnt), 8.000–10.000 RPM, Focal Spot 0,5/1,2 mm + z-FFS",
            roleInChain = "Menghasilkan berkas sinar-X intensitas tinggi kontinu saat gantry berputar dengan gaya sentrifugal 15–30G."
        ),
        CtInstrumentSpec(
            id = "spec_hv_gen",
            subsystem = "High-Frequency Inverter HV Generator",
            materialOrTech = "Inverter Solid-State 10–100 kHz + Transformator Inti Ferit di atas Rotating Frame",
            nominalRating = "80–140 kVp (ripple < 1%), daya 50–120 kW, arus 20–800+ mA",
            roleInChain = "Mengubah tegangan AC masuk menjadi tegangan tinggi DC ultra-stabil langsung di rangka putar tanpa kabel HV."
        ),
        CtInstrumentSpec(
            id = "spec_slip_ring",
            subsystem = "Slip-Ring & Optical Data Link",
            materialOrTech = "Cincin Ag/Cu konsentris + Sikat Perak-Grafit & Transceiver Laser/IR Nirsentuh",
            nominalRating = "Low-Voltage Slip-Ring 400–600 V; Bandwidth optik 5–40 Gbps; Rotasi kontinu 0,25–1,0 s",
            roleInChain = "Menyalurkan daya listrik ke Rotating Frame dan mengirim aliran Sinogram ke komputer konsol tanpa kabel terbelit."
        ),
        CtInstrumentSpec(
            id = "spec_beam_shaping",
            subsystem = "Bow-Tie Filter & Dual Collimation",
            materialOrTech = "Filter komposit profil kupu-kupu + Daun Tungsten Pra-Pasien & Septa Anti-Scatter",
            nominalRating = "Kolimasi z-axis 1,25–160 mm; Penolakan hambur Compton arah miring",
            roleInChain = "Menyamakan fluks foton di seluruh busur detektor, memangkas dosis kulit tepi hingga 50%, dan memblokir scatter."
        ),
        CtInstrumentSpec(
            id = "spec_detector_das",
            subsystem = "Solid-State Detector Array & Modul DAS",
            materialOrTech = "Kristal Sintilator Gd2O2S/UFC + Silicon Photodiode + Pre-Amp/Integrator/Log-ADC 16–24 bit",
            nominalRating = "QDE 95–99%, afterglow < 0,01%, 16–320 baris (T = 0,5–0,625 mm), 1.000–2.400 views/rotasi",
            roleInChain = "Mengubah foton sinar-X menjadi cahaya tampak, arus pA–nA, lalu data biner logaritmik ln(I0/I) Sinogram."
        )
    )

    val tissues: List<CtTissueAttenuation> = listOf(
        CtTissueAttenuation("air", "Udara Bebas (-1000 HU)", -1000, -980, -1000f, 0.0002f, "Lung (WW 1500, WL -600)", "Referensi batas bawah skala Hounsfield Unit (-1000 HU) untuk kalibrasi udara pagi."),
        CtTissueAttenuation("lung", "Parenkim Paru (-800 HU)", -900, -700, -800f, 0.038f, "Lung (WW 1500, WL -600)", "Alveoli berisi udara dengan vaskularisasi halus; optimal pada Lung Window."),
        CtTissueAttenuation("fat", "Jaringan Adiposa (-80 HU)", -100, -50, -80f, 0.175f, "Abdomen (WW 350, WL 40)", "Jaringan lemak subkutan & retroperitoneal ber-HU negatif sebagai pemisah kontras alami organ."),
        CtTissueAttenuation("water", "Air Murni / Fantom QA (0 HU)", -4, 4, 0f, 0.200f, "Standard QA (WW 100, WL 0)", "Patokan titik nol skala HU (0 ± 4 HU pada uji QA harian fantom air; mu_air ≈ 0,200 cm⁻¹ pada energi efektif ~65 keV)."),
        CtTissueAttenuation("wm", "Substansia Alba (+28 HU)", 20, 30, 28f, 0.195f, "Brain (WW 80, WL 40)", "Serabut bermielin otak; tampak lebih gelap dibanding korteks grisea pada Brain Window."),
        CtTissueAttenuation("gm", "Substansia Grisea (+40 HU)", 35, 45, 40f, 0.198f, "Brain (WW 80, WL 40)", "Korteks serebri dan ganglia basalis; dibedakan dari substansia alba dengan WW sempit 80 HU."),
        CtTissueAttenuation("blood", "Darah Beku Akut (+75 HU)", 60, 85, 75f, 0.205f, "Stroke/Brain (WW 80, WL 40)", "Hematoma intrakranial hiperdens pada CT non-kontras akibat konsentrasi hemoglobin tinggi."),
        CtTissueAttenuation("bone", "Tulang Kortikal (+1500 HU)", 1000, 3000, 1500f, 0.475f, "Bone (WW 2000, WL 500)", "Matriks kalsium-hidroksiapatit ber-Z efektif tinggi yang memicu efek fotolistrik dan beam hardening.")
    )

    val modules: List<CtModule> = listOf(
        CtModule(
            id = "M02",
            code = "M02",
            title = "Anatomi Pesawat CT & Arsitektur Gantry",
            subtitle = "Gantry, Rotating Frame, Laser Isocenter, Bow-Tie Filter & Meja Carbon Fiber",
            description = "Bedah komponen eksternal dan internal Gantry pesawat CT Scan, sistem pendingin, kolimasi ganda, serta sinkronisasi meja pasien berbahan serat karbon.",
            iconName = "view_in_ar",
            sortOrder = 1,
            estimatedMinutes = 40,
            totalQuizzes = 3,
            badgeName = "Arsitek Gantry CT",
            flashcards = listOf(
                CtFlashcard("M02_FC1", "M02", "M02.T1.1", "Gantry & Meja", "Bore Aperture & Gantry Tilt", "Diameter bore CT 70–85 cm; tilt mekanis ±30° untuk menghindari orbit mata dan meratakan bidang basilar.", "Selalu kembalikan tilt ke 0° sebelum pemindaian helikal rutin."),
                CtFlashcard("M02_FC2", "M02", "M02.T2.1", "Geometri Isocenter", "Laser Positioning & Off-Centering", "Laser Aksial, Sagital, dan Koronal menempatkan pusat organ tepat di Isocenter agar Bow-Tie Filter dan AEC bekerja akurat.", "Pergeseran vertikal > 20 mm menaikkan quantum noise atau dosis permukaan hingga 20–40%.")
            ),
            nodes = listOf(
                CtNode(
                    code = "M02.T0",
                    level = 0,
                    sortOrder = 1,
                    title = "Tujuan Belajar Anatomi & Tata Letak Pesawat CT",
                    learningGoal = "Memetakan susunan komponen Gantry, Meja Carbon Fiber, Laser Isocenter, dan Konsol Operator.",
                    summaryShort = "Gambaran sistem tiga unit instalasi CT: Gantry, Patient Couch, dan Operator Console.",
                    detailFull = "Pesawat CT modern terdiri atas tiga subsistem utama: (1) Gantry yang menampung Rotating Frame (Tabung Sinar-X, HV Generator, Heat Exchanger, Kolimator, Bow-Tie Filter, Busur Detektor, dan DAS); (2) Meja Pasien Carbon Fiber berakurasi posisi ±0,25 mm; dan (3) Konsol Operator & Image Reconstruction System (IRS).",
                    analogi = "Gantry adalah panggung berputar berkecepatan tinggi tempat kamera sinar-X dan layar sensor selalu berhadapan tepat 180°.",
                    labType = "ct_m02_anatomi_pesawat"
                ),
                CtNode(
                    code = "M02.T1.1",
                    level = 1,
                    sortOrder = 2,
                    prerequisite = "M02.T0",
                    title = "Tata Letak Rotating Frame, Bow-Tie Filter & Kolimator Ganda",
                    summaryShort = "Keseimbangan massa Rotating Frame 180° serta peran Bow-Tie Filter dan kolimator pra/pasca-pasien.",
                    detailFull = "Di dalam Gantry terdapat cincin putar (Rotating Frame) yang menyeimbangkan Tabung Sinar-X berhadapan 180° dengan Busur Detektor. Berkas dari tabung terlebih dahulu dibentuk oleh Bow-Tie Filter (tipis di tengah, tebal di tepi) untuk menyeragamkan fluks foton dan menurunkan dosis kulit perifer hingga 50%, lalu dibatasi lebar sumbu-z oleh Pre-Patient Collimator Tungsten. Di depan detektor, Post-Patient Anti-Scatter Septa menyaring foton hambur Compton.",
                    analogi = "Bow-Tie Filter ibarat kacamata koreksi kontur tubuh agar pinggiran pasien tidak kelebihan dosis sedangkan bagian tengah tetap terang.",
                    labType = "ct_m02_anatomi_pesawat"
                ),
                CtNode(
                    code = "M02.T2.1",
                    level = 2,
                    sortOrder = 3,
                    prerequisite = "M02.T1.1",
                    title = "Meja Carbon Fiber, Laser Isocenter & Dampak Vertical Off-Centering",
                    summaryShort = "Daun meja serat karbon ber-atenuasi rendah dan peran 3 sinar laser menjaga posisi pasien di Isocenter.",
                    detailFull = "Daun meja dibuat dari komposit Carbon Fiber (Z = 6) agar kuat menahan beban 200–300 kg dengan defleksi < 2 mm namun nyaris tidak menyerap sinar-X. Tiga laser (Aksial, Sagital, Koronal) menyelaraskan pusat organ tepat di Isocenter Gantry. Jika ketinggian meja meleset (off-centering), magnifikasi geometris pada topogram mengacaukan perhitungan AEC dan menggeser profil atenuasi terhadap Bow-Tie Filter.",
                    analogi = "Meja Carbon Fiber adalah jembatan bening bagi sinar-X namun kokoh bak baja untuk pasien.",
                    labType = "ct_m02_anatomi_pesawat"
                )
            )
        ),
        CtModule(
            id = "M03",
            code = "M03",
            title = "Instrumentasi Inti: Tabung CT, HV Generator, Slip-Ring & Detektor",
            subtitle = "Tabung 5–8 MHU, Liquid Metal Bearing, HF Inverter, Slip-Ring, Detektor Gd2O2S & DAS",
            description = "Analisis rekayasa rinci pembangkitan sinar-X berkapasitas panas tinggi, transfer daya tanpa kabel via Slip-Ring, penangkapan foton oleh detektor Solid-State vs Gas Xenon, hingga digitalisasi DAS.",
            iconName = "sensors",
            sortOrder = 2,
            prerequisites = listOf("M02"),
            estimatedMinutes = 55,
            totalQuizzes = 4,
            badgeName = "Inspektur Instrumentasi CT",
            flashcards = listOf(
                CtFlashcard("M03_FC1", "M03", "M03.T1.1", "Tabung Sinar-X CT", "Kapasitas Termal (MHU) & LMB", "HU = 1,4 × kVp × mA × t_scan; tabung CT berkapasitas 5–8+ MHU memakai Liquid Metal Bearing (Ga-In-Sn) 10.000 RPM.", "LMB hening, bebas aus mekanis, dan membuang panas secara konduksi langsung."),
                CtFlashcard("M03_FC2", "M03", "M03.T2.1", "Slip-Ring & HV Gen", "Low-Voltage Slip-Ring + HF Inverter", "Inverter 10–100 kHz dipasang di Rotating Frame menerima daya via sikat Ag-grafit (400–600 V); data dikirim via link optik.", "Menghilangkan kabel HV sehingga gantry dapat berputar kontinu untuk scan helikal."),
                CtFlashcard("M03_FC3", "M03", "M03.T3.1", "Detektor & DAS", "Solid-State Gd2O2S + Photodiode", "Kristal keramik Gd2O2S/UFC menyerap 95–99% foton (afterglow < 0,01%) dan diubah menjadi arus pA–nA lalu ln(I0/I) di DAS.", "Tidak seperti Gas Xenon, detektor solid-state dapat dipotong menjadi matriks 16–320 baris MSCT.")
            ),
            nodes = listOf(
                CtNode(
                    code = "M03.T0",
                    level = 0,
                    sortOrder = 1,
                    title = "Tujuan Belajar Rantai Konversi Sinyal Instrumentasi CT",
                    learningGoal = "Menguasai spesifikasi perangkat keras tabung sinar-X CT, HV Generator, Slip-Ring, Detektor Solid-State, dan modul DAS.",
                    summaryShort = "Alur lengkap 5 tahap konversi sinyal: Daya Slip-Ring -> HV Generator -> Foton Sinar-X -> Cahaya Detektor -> Biner DAS.",
                    detailFull = "Rantai instrumentasi pesawat CT mengubah daya listrik menjadi data proyeksi digital melalui lima subsistem terintegrasi: (1) Slip-Ring menyalurkan daya kontinu; (2) HF Inverter HV Generator menghasilkan 80–140 kVp ultra-stabil; (3) Tabung Sinar-X CT berkapasitas 5–8+ MHU memancarkan foton dengan z-Flying Focal Spot; (4) Detektor Solid-State Gd2O2S/UFC + Silicon Photodiode mengubah foton menjadi arus mikro; dan (5) DAS mendigitalkan sinyal logaritmik ln(I0/I) ke IRS.",
                    analogi = "Seperti lari estafet 5 tahap dari listrik PLN hingga menjadi angka Sinogram berkecepatan multi-Gigabit.",
                    labType = "ct_m03_instrumentasi_rantai"
                ),
                CtNode(
                    code = "M03.T1.1",
                    level = 1,
                    sortOrder = 2,
                    prerequisite = "M03.T0",
                    title = "Spesifikasi Tabung Sinar-X CT: Anoda MHU Tinggi, LMB & z-Flying Focal Spot",
                    summaryShort = "Tabung CT memakai piringan anoda grafit-Tungsten-Rhenium 5–8 MHU, Liquid Metal Bearing 10.000 RPM, dan defleksi z-FFS.",
                    detailFull = "Untuk menahan eksposi helikal panjang di bawah gaya sentrifugal tinggi, tabung CT menggunakan piringan anoda berdiameter 15–20 cm dengan kapasitas simpan panas 5–8+ MHU (HU = 1,4 × kVp × mA × waktu) serta pendingin sirkulasi oli-ke-udara (laju 800–1.500 kHU/menit). Liquid Metal Bearing (Gallium-Indium-Tin) menggantikan bantalan bola konvensional agar hening dan menghantarkan panas cepat. Kumparan elektromagnetik z-Flying Focal Spot membelokkan berkas elektron secara kilat untuk menggandakan pencuplikan irisan sumbu-z.",
                    analogi = "Lampu sorot stadion raksasa yang diputar 10.000 RPM di atas bantalan logam cair sambil disiram oli pendingin.",
                    labType = "ct_m03_instrumentasi_rantai"
                ),
                CtNode(
                    code = "M03.T2.1",
                    level = 2,
                    sortOrder = 3,
                    prerequisite = "M03.T1.1",
                    title = "High-Frequency HV Generator, Slip-Ring & Transmisi Data Optik",
                    summaryShort = "Generator inverter 10–100 kHz di atas Rotating Frame dan cincin Slip-Ring menggantikan kabel tegangan tinggi.",
                    detailFull = "Generator High-Frequency (10–100 kHz) memperkecil ukuran transformator ferit sehingga dapat dipasang langsung pada Rotating Frame dengan fluktuasi tegangan (ripple) < 1%. Daya listrik disalurkan dari bagian diam ke rangka berputar melalui cincin konduktor Low-Voltage Slip-Ring (~400–600 V) dan sikat perak-grafit, sementara data digital DAS puluhan Gbps dikirim melalui kopling optik laser/inframerah nirsentuh agar bebas percikan listrik.",
                    analogi = "Seperti rel listrik melingkar pada kereta listrik yang memungkinkan putaran 360° terus-menerus tanpa kabel kusut.",
                    labType = "ct_m03_instrumentasi_rantai"
                ),
                CtNode(
                    code = "M03.T3.1",
                    level = 3,
                    sortOrder = 4,
                    prerequisite = "M03.T2.1",
                    title = "Detektor Solid-State (Gd2O2S) vs Gas Xenon, DAS & Photon-Counting CdTe",
                    summaryShort = "Efisiensi penyerapan 95–99% detektor Solid-State MSCT, tahap konversi di DAS, serta detektor konversi langsung CdTe.",
                    detailFull = "Pesawat MSCT modern meninggalkan detektor Gas Xenon (QDE hanya 45–60% dan tidak dapat dibagi menjadi matriks 2D) dan beralih ke detektor Solid-State: kristal sintilator Gd2O2S/UFC (QDE 95–99%, afterglow < 0,01%) yang direkatkan ke Silicon Photodiode. Arus orde pA–nA diperkuat oleh Pre-Amplifier, diintegrasikan oleh Current Integrator tiap sudut sampling (1.000–2.400 views/rotasi), lalu dikonversi oleh Logarithmic ADC 16–24 bit menjadi nilai P = ln(I0/I). Generasi terbaru Photon-Counting CT memakai semikonduktor CdTe untuk menghitung foton satu per satu berdasarkan energi tanpa tahap cahaya.",
                    analogi = "Detektor Solid-State ibarat ubin panel surya berlapis kristal pendar superpadat yang langsung mengubah tiap foton menjadi angka biner di DAS.",
                    labType = "ct_m03_instrumentasi_rantai"
                )
            )
        )
    )

    val quizzes: List<CtQuizQuestion> = listOf(
        CtQuizQuestion(
            id = "ct_q_m02_t0",
            moduleId = "M02",
            nodeCode = "M02.T0",
            level = 1,
            question = "Manakah pasangan tiga subsistem utama dalam instalasi pesawat CT Scan klinis?",
            options = listOf(
                "Gantry, Meja Pasien Carbon Fiber, dan Konsol Operator/Komputer Rekonstruksi",
                "Magnet Superkonduktor, Koil Gradien, dan Sangkar Faraday",
                "Transduser Piezoelektrik, Gel Akustik, dan Doppler Console",
                "Kolimator Paralel, Kristal NaI(Tl), dan Tabung Photomultiplier Tunggal"
            ),
            correctIndex = 0,
            explanation = "Arsitektur pesawat CT terdiri atas Gantry (sumber sinar-X, detektor, DAS), Meja Pasien Carbon Fiber, dan Konsol Operator/IRS."
        ),
        CtQuizQuestion(
            id = "ct_q_m02_t1_1",
            moduleId = "M02",
            nodeCode = "M02.T1.1",
            level = 1,
            question = "Apa fungsi utama Bow-Tie Filter yang dipasang di bawah jendela tabung sinar-X CT?",
            options = listOf(
                "Menyeragamkan intensitas foton di seluruh busur detektor sesuai kontur elips tubuh dan memangkas dosis kulit tepi",
                "Mempercepat putaran anoda tabung hingga 10.000 RPM",
                "Mengubah sinyal cahaya sintilator menjadi bilangan biner",
                "Menggerakkan meja pasien secara longitudinal"
            ),
            correctIndex = 0,
            explanation = "Bow-Tie Filter lebih tipis di tengah dan tebal di tepi sehingga mencegah saturasi detektor perifer, menekan beam hardening, dan menghemat dosis permukaan."
        ),
        CtQuizQuestion(
            id = "ct_q_m02_t2_1",
            moduleId = "M02",
            nodeCode = "M02.T2.1",
            level = 2,
            question = "Mengapa penyetelan ketinggian meja terlalu rendah atau terlalu tinggi (Vertical Off-Centering) berbahaya pada pesawat CT modern?",
            options = listOf(
                "Mengacaukan kompensasi Bow-Tie Filter dan memicu magnifikasi/pengecilan topogram yang menyesatkan modulasi mA oleh AEC",
                "Menyebabkan kabel tegangan tinggi di lantai terputus saat gantry berputar",
                "Mengubah nilai Hounsfield Unit udara dari -1000 HU menjadi +1000 HU",
                "Menghentikan sirkulasi oli pendingin di dalam heat exchanger"
            ),
            correctIndex = 0,
            explanation = "AEC dan profil Bow-Tie Filter dirancang dengan asumsi pusat objek berada tepat di Isocenter; off-centering memicu peningkatan noise atau overdosis 20–40%."
        ),
        CtQuizQuestion(
            id = "ct_q_m03_t0",
            moduleId = "M03",
            nodeCode = "M03.T0",
            level = 1,
            question = "Urutan rantai konversi energi yang benar pada instrumentasi pesawat CT modern adalah...",
            options = listOf(
                "Daya Slip-Ring -> HV Generator -> Foton Sinar-X -> Cahaya Sintilator -> Arus Photodiode -> Biner DAS",
                "Foton Sinar-X -> HV Generator -> Slip-Ring -> Tabung Gas -> Kaset Film",
                "Gelombang RF -> Koil Gradien -> Photodiode -> Slip-Ring -> FBP",
                "Arus Photodiode -> Bow-Tie Filter -> Slip-Ring -> Tabung Sinar-X"
            ),
            correctIndex = 0,
            explanation = "Listrik melewati Slip-Ring ke HV Generator untuk memicu Tabung Sinar-X; foton yang lolos diubah oleh Sintilator menjadi cahaya tampak, ke Photodiode menjadi arus, dan didigitalkan di DAS."
        ),
        CtQuizQuestion(
            id = "ct_q_m03_t1_1",
            moduleId = "M03",
            nodeCode = "M03.T1.1",
            level = 1,
            question = "Apa keunggulan rekayasa Liquid Metal Bearing (paduan Ga-In-Sn) pada tabung sinar-X CT berkapasitas 5–8+ MHU?",
            options = listOf(
                "Operasi hening bebas keausan bantalan bola mekanis dan mampu menghantarkan panas anoda secara konduksi langsung dengan cepat",
                "Menggantikan kebutuhan vakum di dalam selubung kaca/logam tabung",
                "Menghilangkan emisi radiasi Bremsstrahlung secara total",
                "Memungkinkan gantry berhenti berputar di tengah pemindaian helikal"
            ),
            correctIndex = 0,
            explanation = "Liquid Metal Bearing mendukung putaran anoda 8.000–10.000 RPM secara senyap tanpa aus mekanis sekaligus menjadi jalur konduksi termal cepat."
        ),
        CtQuizQuestion(
            id = "ct_q_m03_t2_1",
            moduleId = "M03",
            nodeCode = "M03.T2.1",
            level = 2,
            question = "Bagaimana pesawat CT modern mengalirkan daya listrik dan data sinogram berkecepatan gigabit saat gantry berputar 360° terus-menerus?",
            options = listOf(
                "Daya dialirkan via cincin Low-Voltage Slip-Ring dengan sikat perak-grafit ke HF Inverter, sedangkan data dikirim nirsentuh melalui link optik",
                "Menggunakan kabel koaksial sepanjang 20 meter yang digulung balik tiap satu putaran",
                "Mengirimkan berkas sinar-X langsung ke ruang operator tanpa kabel maupun cincin",
                "Mengandalkan baterai lithium tunggal tanpa sumber listrik eksternal"
            ),
            correctIndex = 0,
            explanation = "Kombinasi HF HV Generator kompak di atas Rotating Frame, Low-Voltage Slip-Ring, dan transmisi optik memungkinkan rotasi kontinu bebas artefak percikan."
        ),
        CtQuizQuestion(
            id = "ct_q_m03_t3_1",
            moduleId = "M03",
            nodeCode = "M03.T3.1",
            level = 3,
            question = "Mengapa seluruh pesawat MSCT modern memakai detektor Solid-State (Gd2O2S/UFC + Photodiode) dibanding bilik Gas Xenon?",
            options = listOf(
                "Efisiensi penyerapan kuantum (QDE) mencapai 95–99%, afterglow < 0,01%, dan mudah disusun sebagai matriks 2D multi-baris",
                "Gas Xenon hanya bekerja pada energi sinar gamma kedokteran nuklir",
                "Detektor Solid-State tidak memerlukan konversi ADC di dalam DAS",
                "Kristal Gd2O2S tidak memancarkan cahaya saat terkena sinar-X"
            ),
            correctIndex = 0,
            explanation = "Detektor solid-state keramik berdensitas tinggi menyerap hampir seluruh foton sinar-X, luruh dalam mikrodetik, dan memungkinkan susunan 16–320 baris detektor."
        )
    )

    val glossary: List<CtGlossaryItem> = listOf(
        CtGlossaryItem("Anode Heat Storage Capacity", "MHU", "Kapasitas simpan panas maksimum piringan anoda tabung CT (umumnya 5–8+ MHU) untuk menahan pemindaian helikal berarus tinggi.", "M03", "M03.T1.1", listOf("M03")),
        CtGlossaryItem("Liquid Metal Bearing", "LMB", "Bantalan luncur berbahan logam cair Gallium-Indium-Tin pada rotor anoda CT untuk putaran 10.000 RPM yang hening dan konduktif termal.", "M03", "M03.T1.1", listOf("M03")),
        CtGlossaryItem("z-Flying Focal Spot", "z-FFS", "Defleksi elektromagnetik titik fokus elektron sepanjang sumbu-z guna menggandakan pencuplikan irisan per rotasi tanpa memperkecil detektor.", "M03", "M03.T1.1", listOf("M03", "M04")),
        CtGlossaryItem("High-Frequency HV Generator", "HF Inverter", "Pembangkit tegangan tinggi 10–100 kHz di atas Rotating Frame dengan ripple < 1% untuk menghasilkan 80–140 kVp stabil.", "M03", "M03.T2.1", listOf("M03")),
        CtGlossaryItem("Slip-Ring", "LV Slip-Ring / Optical", "Cincin konduktor dan sikat geser penyalur daya serta pemancar optik nirsentuh penyalur data sinogram agar gantry berputar kontinu 360°.", "M03", "M03.T2.1", listOf("M02", "M03", "M04")),
        CtGlossaryItem("Solid-State Scintillator", "Gd2O2S / UFC", "Kristal sintilator keramik ber-QDE 95–99% dan afterglow < 0,01% yang dipadukan dengan Silicon Photodiode pada seluruh MSCT modern.", "M03", "M03.T3.1", listOf("M03", "M09")),
        CtGlossaryItem("Data Acquisition System", "DAS", "Elektronik di belakang detektor yang mengintegrasikan arus pA–nA, mengamplifikasi secara logaritmik ln(I0/I), dan mendigitalkannya lewat ADC 16–24 bit.", "M03", "M03.T3.1", listOf("M03", "M05")),
        CtGlossaryItem("Photon-Counting Detector", "PCD / CdTe", "Detektor semikonduktor konversi langsung Cadmium Telluride yang menghitung tiap foton sinar-X satu per satu ke dalam ambang energi spektral.", "M03", "M03.T3.1", listOf("M03")),
        CtGlossaryItem("Bow-Tie Filter", "Beam Shaping", "Filter berbentuk dasi kupu-kupu (tipis di pusat, tebal di tepi) untuk menyeragamkan fluks foton dan menurunkan dosis kulit perifer.", "M02", "M02.T1.1", listOf("M02")),
        CtGlossaryItem("Post-Patient Anti-Scatter Septa", "Detector Collimator", "Kisi-kisi pelat Tungsten tipis di permukaan busur detektor untuk menolak foton hamburan Compton yang datang miring.", "M02", "M02.T1.1", listOf("M02", "M03"))
    )

    val flashcards: List<CtFlashcard> = modules.flatMap { it.flashcards }

    val scannerInstruments: List<CTScannerInstrument> = CTScannerInstrumentCatalog.presets


    fun toCtModules(): List<CtModule> = modules
    fun toQuizQuestions(): List<CtQuizQuestion> = quizzes
    fun toCtFlashcards(): List<CtFlashcard> = flashcards
    fun toTissueAttenuationList(): List<CtTissueAttenuation> = tissues
    fun toGlossaryItems(): List<CtGlossaryItem> = glossary

    fun toCtModuleEntities(): List<CtModuleEntity> = modules.mapIndexed { idx, mod ->
        mod.toEntity(fallbackOrder = idx + 1)
    }

    fun toCurriculumNodeEntities(): List<CurriculumNodeEntity> = modules.flatMap { mod ->
        mod.nodes.mapIndexed { idx, node -> node.toEntity(mod.id, fallbackOrder = idx + 1) }
    }

    fun toCurriculumQuizEntities(): List<CurriculumQuizEntity> = quizzes.map { it.toEntity() }
}
