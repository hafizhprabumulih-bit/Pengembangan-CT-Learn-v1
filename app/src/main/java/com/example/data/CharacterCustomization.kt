package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.R

/**
 * Aturan buka-kunci berbasis kurikulum.
 * Saat ini [CharacterCatalog.ENFORCE_CURRICULUM_LOCKS] disetel ke `false`
 * sehingga seluruh item terbuka untuk pengujian dan eksplorasi penuh.
 * Nanti cukup ubah `ENFORCE_CURRICULUM_LOCKS = true` atau edit syarat di bawah
 * untuk mengaktifkan pembukaan bertahap per modul.
 */
data class UnlockRequirement(
    val requiredModuleId: String? = null,
    val requiredModuleTitle: String? = null,
    val requiredNodesCount: Int = 0,
    val futureNote: String = "Tersedia langsung"
)

data class CharacterItem(
    val id: String,
    val name: String,
    val category: CharacterCategory,
    val description: String,
    val clinicalBenefit: String = "",
    val badgeLabel: String = "CT-Safe",
    val previewColorHex: Long = 0xFF1FB6E8,
    val secondaryColorHex: Long = 0xFF7FDCD6,
    val drawableRes: Int? = null,
    val colorHex: String? = null,
    val unlockRequirement: UnlockRequirement = UnlockRequirement()
)

enum class CharacterCategory(val title: String, val subtitle: String) {
    KARAKTER("Karakter", "Pilih spesialis medis & radiologi CT Scan"),
    RAMBUT("Kepala", "Pelindung kepala, kacamata timbal Pb & HUD konsol"),
    SERAGAM("Seragam", "Jas lab, scrub klinis & apron proteksi radiasi"),
    HEADSET("Headset", "Interkom dua arah ruang konsol–gantry CT"),
    AKSESORIS("Alat Klinis", "Instrumen dosimetri, kalibrasi & QA pesawat CT"),
    LATAR("Latar Ruang", "Suasana ruang instalasi & konsol CT Scan")
}

data class CharacterAccentPalette(
    val id: String,
    val label: String,
    val primaryHex: Long,
    val glowHex: Long
) {
    val primaryColor: Color get() = Color(primaryHex)
    val glowColor: Color get() = Color(glowHex)
}

data class SpecialistProfile(
    val avatarId: String,
    val roleTitle: String,
    val zoneSpecialty: String,
    val favoriteSequence: String,
    val greetings: List<String>
)

data class CharacterCustomizationState(
    val avatarId: String = "radiografer_ct",
    val equippedHair: String = "hair_default",
    val equippedUniform: String = "uniform_scrub_mint",
    val equippedHeadset: String = "headset_radio",
    val equippedAccessory: String = "acc_dosimeter_detector",
    val backgroundId: String = "bg_control_room"
)

object CharacterCatalog {
    /**
     * Ubah ke `true` di tahap pengembangan berikutnya bila ingin mengaktifkan
     * penguncian item berdasarkan penyelesaian modul kurikulum.
     */
    const val ENFORCE_CURRICULUM_LOCKS: Boolean = false

    val accentPalettes = listOf(
        CharacterAccentPalette("cyan", "Cyan Scanner", 0xFF1FB6E8, 0xFF7FDCD6),
        CharacterAccentPalette("mint", "Mint Klinis", 0xFF3CC4A3, 0xFF7FDCD6),
        CharacterAccentPalette("blue", "Biru Gantry", 0xFF1F4FB5, 0xFF55BBDC),
        CharacterAccentPalette("teal", "Teal Sinogram", 0xFF7FDCD6, 0xFF1FB6E8),
        CharacterAccentPalette("white", "Putih Steril", 0xFFE6F2FB, 0xFFBFD9EC),
        CharacterAccentPalette("amber", "Oranye Sinar-X", 0xFFFF8A1F, 0xFFFFC93C)
    )

    fun getAccentPalette(id: String): CharacterAccentPalette {
        return accentPalettes.find { it.id == id } ?: accentPalettes.first()
    }

    val specialistProfiles = mapOf(
        "radiografer_ct" to SpecialistProfile(
            avatarId = "radiografer_ct",
            roleTitle = "Radiografer CT Scan Senior",
            zoneSpecialty = "Operasional Konsol, Laser Isocenter & Prosedur Tube Warm-Up",
            favoriteSequence = "Helical MSCT, Bolus Tracking & Air Calibration",
            greetings = listOf(
                "Selalu awali shift pagi dengan prosedur Tube Warm-Up dan Air Calibration agar anoda putar stabil dan bebas Ring Artifact!",
                "Gunakan garis laser Sagital, Koronal, dan Aksial untuk menempatkan organ target tepat di Isocenter Gantry!",
                "Periksa indikator CTDIvol (mGy) dan DLP (mGy·cm) pada layar konsol sebelum menekan tombol eksposi Scan.",
                "Pastikan meja pasien Carbon Fiber bergerak bebas tanpa hambatan kabel infus selama akuisisi Helikal!"
            )
        ),
        "radiolog" to SpecialistProfile(
            avatarId = "radiolog",
            roleTitle = "Dokter Spesialis Radiologi CT",
            zoneSpecialty = "Ekspertise Multi-Windowing HU, Kontras Multiphasic & 3D MPR",
            favoriteSequence = "Brain/Stroke Window, Lung Window & Multiphasic Abdomen",
            greetings = listOf(
                "Atur Window Width (WW) sempit dan Window Level (WL) tepat pada densitas organ untuk mendeteksi lesi hipodens dini!",
                "Air murni dikalibrasi pada 0 HU dan udara pada -1000 HU—gunakan ROI untuk mengukur atenuasi lesi secara kuantitatif.",
                "Selalu verifikasi nilai eGFR dan riwayat alergi sebelum injeksi media kontras Iodin intravena.",
                "Kombinasikan irisan aksial tipis isotropik dengan rekonstruksi MPR Koronal, Sagital, dan MIP untuk akurasi diagnostik!"
            )
        ),
        "ahli_neuro_ct" to SpecialistProfile(
            avatarId = "ahli_neuro_ct",
            roleTitle = "Spesialis Neuro-CT & Stroke Perfusi",
            zoneSpecialty = "CT Kepala Non-Kontras, CT Angiografi Serebral & CT Perfusi",
            favoriteSequence = "NCCT Brain, Stroke Window (WW 40 / WL 40) & CTA Neuro",
            greetings = listOf(
                "Pada kecurigaan stroke iskemik hiperakut, ubah pengaturan ke Stroke Window (WW 40, WL 40) agar tanda insular ribbon tampak jelas!",
                "Kemiringan Gantry (Gantry Tilt) sejajar garis Orbitomeatal (OML) melindungi lensa mata dan mengurangi artefak Beam Hardening fossa posterior.",
                "Teknik Bolus Tracking pada arkus aorta memastikan CT Angiografi pembuluh darah otak terekam tepat di puncak fase arterial!",
                "Gunakan Bone Window (WW 2500, WL 500) dengan Kernel tajam (Sharp) untuk mengevaluasi garis fraktur basis kranii."
            )
        ),
        "fisikawan_medis" to SpecialistProfile(
            avatarId = "fisikawan_medis",
            roleTitle = "Fisikawan Medis & Instrumentasi CT",
            zoneSpecialty = "Rantai Detektor-DAS, Kalibrasi HU, Dosimetri CTDIvol & QA Harian",
            favoriteSequence = "Filtered Back Projection (FBP), Iterative Recon & QA Fantom Air",
            greetings = listOf(
                "Di dalam Gantry, kristal sintilator Solid-State (Gd₂O₂S) mengubah sinar-X menjadi cahaya tampak, lalu Photodiode dan DAS mengubahnya menjadi data digital Sinogram!",
                "Bow-Tie Filter menyamakan profil intensitas berkas sinar-X antara bagian tengah tubuh yang tebal dan tepi tubuh yang tipis.",
                "Evaluasi QA Fantom Air harian wajib memastikan nilai rata-rata tengah berada pada 0 ± 4 HU dan keseragaman (uniformity) tepi ≤ 5 HU!",
                "Jika muncul Ring Artifact konsentris, segera jalankan Air Calibration untuk menyetarakan respons gain kanal detektor!"
            )
        ),
        "petugas_proteksi_radiasi" to SpecialistProfile(
            avatarId = "petugas_proteksi_radiasi",
            roleTitle = "Petugas Proteksi Radiasi (PPR CT)",
            zoneSpecialty = "Optimasi Dosis ALARA, AEC, Off-Centering & Audit DRL",
            favoriteSequence = "Automatic Exposure Control (AEC 3D) & Low-Dose CT",
            greetings = listOf(
                "Off-centering meja pasien 4 cm saja dari Isocenter Gantry dapat mengacaukan fungsi Bow-Tie Filter dan meningkatkan noise serta dosis permukaan!",
                "Sistem AEC (Automatic Exposure Control) memodulasi arus tabung (mA) secara otomatis mengikuti ketebalan anatomi pada sumbu x-y dan z.",
                "Pantau nilai CTDIvol, SSDE, dan DLP setiap pemeriksaan agar selalu berada di bawah Tingkat Panduan Diagnostik (DRL) nasional!",
                "Pastikan pintu berlapis timbal (Pb) tertutup rapat dan lampu indikator eksposi menyala selama tabung memancarkan sinar-X."
            )
        ),
        "ahli_jantung" to SpecialistProfile(
            avatarId = "ahli_jantung",
            roleTitle = "Spesialis Kardio-CT (Coronary CTA)",
            zoneSpecialty = "Sinkronisasi EKG, Rotasi Gantry Sub-Detik & Calcium Scoring",
            favoriteSequence = "Prospective ECG-Gated CCTA & Dual-Source CT (0,25 s)",
            greetings = listOf(
                "Teknologi Slip-Ring modern memungkinkan Gantry berputar 0,25 detik per rotasi untuk membekukan gerak denyut arteri koroner!",
                "Dual-Source CT menggunakan dua pasang tabung sinar-X dan detektor yang bersudut 90° untuk menggandakan resolusi temporal.",
                "Tempatkan ROI Bolus Tracking pada aorta asendens dengan ambang picu 100–120 HU untuk visualisasi lumen koroner yang optimal.",
                "Latih aba-aba tahan napas inspirasi pendek yang tenang agar detak jantung pasien tetap stabil selama pemindaian."
            )
        ),
        "perawat" to SpecialistProfile(
            avatarId = "perawat",
            roleTitle = "Perawat Klinis Radiologi & Kontras IV",
            zoneSpecialty = "Kanulasi IV Abocath 18–20G, Power Injector & Keamanan Pasien",
            favoriteSequence = "Dual-Head Power Injector (Iodin + Saline Chaser NaCl 0,9%)",
            greetings = listOf(
                "Gunakan kanul IV ukuran 18G–20G di vena mediana cubiti agar aman menahan laju injeksi Power Injector 4–5 mL/detik!",
                "Tabung kedua pada Dual-Head Injector berisi Saline Chaser (NaCl 0,9%) untuk mendorong bolus kontras menjadi padat dan hemat volume.",
                "Lakukan uji patensi vena dengan flush NaCl sebelum injeksi otomatis guna mencegah risiko ekstravasasi kontras di jaringan subkutan.",
                "Komunikasi dua arah melalui interkom Gantry sangat membantu menenangkan pasien saat sensasi hangat kontras Iodin muncul."
            )
        ),
        "dokter_bedah" to SpecialistProfile(
            avatarId = "dokter_bedah",
            roleTitle = "Dokter Bedah & Intervensi Terpandu CT",
            zoneSpecialty = "Biopsi Terpandu Laser CT, Rekonstruksi 3D VRT & Trauma",
            favoriteSequence = "Isotropic 3D VRT, Bone Kernel & CT-Guided Biopsy",
            greetings = listOf(
                "Akuisisi MSCT dengan voksel isotropik (Δx = Δy = Δz ≈ 0,625 mm) menghasilkan rekonstruksi 3D VRT dan MPR bebas distorsi tangga (stair-step)!",
                "Pada kasus trauma ortopedi dan kraniofasial, rekonstruksi Kernel tajam (Bone Kernel) memperlihatkan garis fraktur mikro dengan sangat tegas.",
                "Laser positioning aksial dan sagital pada Gantry menjadi panduan presisi tinggi untuk menentukan titik masuk jarum biopsi perkutan.",
                "Kolaborasi cepat tim bedah dan radiografer CT sangat krusial pada protokol Whole-Body Trauma CT (Pan-Scan)."
            )
        ),
        "dokter_robot" to SpecialistProfile(
            avatarId = "dokter_robot",
            roleTitle = "Asisten AI Rekonstruksi & Detektor CT",
            zoneSpecialty = "Deep Learning Recon (DLIR), Iterative Recon & Photon-Counting",
            favoriteSequence = "AI Deep Learning Reconstruction, MAR & Sinogram DAS",
            greetings = listOf(
                "Halo! Algoritma Iterative Reconstruction dan AI Deep Learning mampu menekan Quantum Noise secara drastis pada CT dosis rendah!",
                "Di dalam DAS (Data Acquisition System), arus analog picoampere dari Photodiode diperkuat oleh Pre-amp dan diubah oleh ADC menjadi Sinogram.",
                "Algoritma Metal Artifact Reduction (MAR) menginterpolasi proyeksi Sinogram yang terkorupsi implan logam untuk memulihkan anatomi sekitar!",
                "Ketuk komponen di bawah atau buka Simulator Bedah Gantry untuk menelusuri perjalanan sinyal pesawat CT dari tabung hingga citra!"
            )
        ),
        "dosen" to SpecialistProfile(
            avatarId = "dosen",
            roleTitle = "Dosen Fisika & Instrumentasi Pesawat CT",
            zoneSpecialty = "Kurikulum Anatomi Gantry, Rantai Detektor-DAS & Rekonstruksi",
            favoriteSequence = "M01–M09: Beer-Lambert, Slip-Ring, Sinogram FBP & Windowing HU",
            greetings = listOf(
                "Selamat datang di CT-Learn! Kita akan membedah anatomi fisik Gantry, Meja Pasien, dan Konsol secara menyeluruh dari M01 hingga M09.",
                "Pahami mengapa HV Generator dan Tabung Sinar-X berkapasitas MHU tinggi dipasang berhadapan 180° dengan Detector Array di atas Rotating Frame!",
                "Jangan lewatkan rantai sinyal di Modul 3: Slip-Ring -> HV Generator -> X-Ray Tube -> Bow-Tie Filter -> Scintillator -> Photodiode -> DAS -> Sinogram.",
                "Gunakan laboratorium simulasi interaktif di setiap modul untuk menguji pengaruh Pitch, Kernel, WW/WL, dan Air Calibration secara langsung!"
            )
        ),
        "mahasiswa" to SpecialistProfile(
            avatarId = "mahasiswa",
            roleTitle = "Mahasiswa Radiologi Klinis CT Scan",
            zoneSpecialty = "Praktikum Konsol CT, Pemosisian Laser Isocenter & Windowing",
            favoriteSequence = "Tube Warm-Up, Scout Topogram & Preset Windowing WW/WL",
            greetings = listOf(
                "Hai rekan sejawat! Yuk pelajari 9 modul CT-Learn mulai dari Fisika Atenuasi (M01), Anatomi Gantry (M02), hingga QA Pesawat (M09)!",
                "Coba buka tutup depan Gantry di Simulator 1 untuk melihat posisi jam Tabung Sinar-X, HV Generator, Slip-Ring, dan Modul DAS!",
                "Ingat rumus dasar: HU = 1000 × (μ - μ_air) / μ_air, dan Pitch = Pergerakan Meja per Rotasi dibagi Total Lebar Berkas Kolimasi (N × T).",
                "Semangat pertahankan streak belajar harian dan taklukkan kuis 3 tingkat di setiap modul!"
            )
        )
    )

    fun getSpecialistProfile(avatarId: String): SpecialistProfile {
        return specialistProfiles[avatarId] ?: specialistProfiles.getValue("radiografer_ct")
    }

    val hairItems = listOf(
        CharacterItem(
            id = "hair_default",
            name = "Sensor Laser Isocenter",
            category = CharacterCategory.RAMBUT,
            description = "Sensor penyelarasan 3-sumbu Laser Positioning Aksial, Sagital, dan Koronal.",
            clinicalBenefit = "Kalibrasi titik pusat Isocenter Gantry otomatis",
            badgeLabel = "Standar CT",
            previewColorHex = 0xFF1FB6E8,
            secondaryColorHex = 0xFF7FDCD6,
            drawableRes = R.drawable.nano_head_ct_default,
            unlockRequirement = UnlockRequirement(null, null, 0, "Terbuka sejak awal")
        ),
        CharacterItem(
            id = "hair_visor",
            name = "Visor HUD Sinogram",
            category = CharacterCategory.RAMBUT,
            description = "Layar optik transparan pemantau matriks proyeksi Sinogram & nilai HU real-time.",
            clinicalBenefit = "Visualisasi kurva Bolus Tracking & WW/WL langsung",
            badgeLabel = "HUD Sinogram",
            previewColorHex = 0xFF7FDCD6,
            secondaryColorHex = 0xFF1FB6E8,
            drawableRes = R.drawable.nano_head_visor,
            unlockRequirement = UnlockRequirement("m01", "M01: Fisika Dasar Sinar-X & Atenuasi CT", 5, "Syarat rencana: 5 simpul M01")
        ),
        CharacterItem(
            id = "hair_goggles",
            name = "Kacamata Pelindung Pb 0,75mm",
            category = CharacterCategory.RAMBUT,
            description = "Kacamata kaca timbal (lead glasses) pelindung lensa mata saat CT Fluoroskopi/Intervensi.",
            clinicalBenefit = "Meredam >90% radiasi hambur Compton ke lensa mata operator",
            badgeLabel = "Pb 0,75 mm",
            previewColorHex = 0xFFFF8A1F,
            secondaryColorHex = 0xFFFFC93C,
            drawableRes = R.drawable.nano_head_goggles,
            unlockRequirement = UnlockRequirement("m03", "M03: Instrumentasi Tabung & Detektor CT", 4, "Syarat rencana: 4 simpul M03")
        ),
        CharacterItem(
            id = "hair_cyber",
            name = "Halo Spektral Photon-Counting",
            category = CharacterCategory.RAMBUT,
            description = "Mahkota telemetri pemilah ambang energi foton sinar-X (Energy Bins CdTe).",
            clinicalBenefit = "Dekonvolusi spektral Iodin vs Kalsium resolusi 0,2 mm",
            badgeLabel = "PCD Spectral",
            previewColorHex = 0xFF55BBDC,
            secondaryColorHex = 0xFF1F4FB5,
            drawableRes = R.drawable.nano_head_cyber,
            unlockRequirement = UnlockRequirement("m05", "M05: Rekonstruksi Citra & Sinogram", 5, "Syarat rencana: 5 simpul M05")
        ),
        CharacterItem(
            id = "hair_crown_rf",
            name = "Helm Detektor 320-Slice",
            category = CharacterCategory.RAMBUT,
            description = "Mahkota array detektor sintilator keramik Ultra-Fast 320 baris cakupan 16 cm.",
            clinicalBenefit = "Akuisisi seluruh volume otak & jantung dalam 1 rotasi tunggal",
            badgeLabel = "320-Row MSCT",
            previewColorHex = 0xFFE6F2FB,
            secondaryColorHex = 0xFF1FB6E8,
            drawableRes = R.drawable.nano_head_coil64,
            unlockRequirement = UnlockRequirement("m07", "M07: Kualitas Citra & Dosimetri CT", 5, "Syarat rencana: 5 simpul M07")
        ),
        CharacterItem(
            id = "hair_mortarboard",
            name = "Topi Toga Akademik CT",
            category = CharacterCategory.RAMBUT,
            description = "Topi toga dosen pengampu mata kuliah fisika & instrumentasi CT Scan.",
            clinicalBenefit = "Simbol bimbingan akademik & penguasaan kurikulum M01–M09",
            badgeLabel = "Akademik",
            previewColorHex = 0xFF1E3A5F,
            secondaryColorHex = 0xFF22D3EE,
            drawableRes = R.drawable.nano_head_mortarboard,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        ),
        CharacterItem(
            id = "hair_beanie_ct",
            name = "Beanie Praktikum CT",
            category = CharacterCategory.RAMBUT,
            description = "Topi rajut hangat nyaman untuk shift jaga malam di ruang konsol operator CT ber-AC dingin.",
            clinicalBenefit = "Kenyamanan belajar mahasiswa selama praktikum konsol CT",
            badgeLabel = "Shift Jaga",
            previewColorHex = 0xFF14B8D4,
            secondaryColorHex = 0xFFF97316,
            drawableRes = R.drawable.nano_head_beanie_ct,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        ),
        CharacterItem(
            id = "hair_loupe",
            name = "Loupe Inspeksi Detektor & QA",
            category = CharacterCategory.RAMBUT,
            description = "Kaca pembesar optik presisi untuk inspeksi fisik kolimator, sikat Slip-Ring & fantom QA.",
            clinicalBenefit = "Verifikasi keselarasan garis laser ±1 mm & uji resolusi lp/cm",
            badgeLabel = "QA Optik",
            previewColorHex = 0xFF334155,
            secondaryColorHex = 0xFF22D3EE,
            drawableRes = R.drawable.nano_head_loupe,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        ),
        CharacterItem(
            id = "hair_bandana_3t",
            name = "Bandana Shift Trauma CT",
            category = CharacterCategory.RAMBUT,
            description = "Ikat kepala ergonomis beremblem MSCT untuk respons cepat Whole-Body Trauma CT.",
            clinicalBenefit = "Menjaga fokus saat pemindaian helikal cepat di IGD",
            badgeLabel = "Fast Helical",
            previewColorHex = 0xFF0E7490,
            secondaryColorHex = 0xFF22D3EE,
            drawableRes = R.drawable.nano_head_bandana_3t,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        ),
        CharacterItem(
            id = "hair_cryo_cap",
            name = "Topi Termal Anoda 8 MHU",
            category = CharacterCategory.RAMBUT,
            description = "Topi teknisi elektromedis pemeliharaan sistem pendingin oli tabung sinar-X & Heat Exchanger.",
            clinicalBenefit = "Pemantauan disipasi panas anoda 1.500 kHU/menit & Liquid Metal Bearing",
            badgeLabel = "8 MHU Tube",
            previewColorHex = 0xFF1E3A8A,
            secondaryColorHex = 0xFF38BDF8,
            drawableRes = R.drawable.nano_head_cryo_cap,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        )
    )

    val uniformItems = listOf(
        CharacterItem(
            id = "uniform_scrub_mint",
            name = "Scrub Mint Konsol CT",
            category = CharacterCategory.SERAGAM,
            description = "Seragam harian operator ruang konsol CT Scan dengan saku dosimeter TLD.",
            clinicalBenefit = "Ergonomis saat memposisikan pasien di meja Carbon Fiber",
            badgeLabel = "Operator CT",
            previewColorHex = 0xFF3CC4A3,
            secondaryColorHex = 0xFF7FDCD6,
            drawableRes = R.drawable.nano_uniform_scrub_mint,
            unlockRequirement = UnlockRequirement(null, null, 0, "Terbuka sejak awal")
        ),
        CharacterItem(
            id = "uniform_standard",
            name = "Jas Lab Radiologi CT",
            category = CharacterCategory.SERAGAM,
            description = "Jas putih resmi dokter spesialis radiologi dan fisikawan medis CT Scan.",
            clinicalBenefit = "Dilengkapi klip lencana dosimeter personal & buku protokol kontras",
            badgeLabel = "Klinis",
            previewColorHex = 0xFFE6F2FB,
            secondaryColorHex = 0xFF1FB6E8,
            drawableRes = R.drawable.nano_uniform_labcoat,
            unlockRequirement = UnlockRequirement("m01", "M01: Fisika Dasar Sinar-X & Atenuasi CT", 6, "Syarat rencana: 6 simpul M01")
        ),
        CharacterItem(
            id = "uniform_scrub_blue",
            name = "Scrub Intervensi Biopsi CT",
            category = CharacterCategory.SERAGAM,
            description = "Pakaian tindakan steril untuk prosedur biopsi perkutan & drainase terpandu laser CT.",
            clinicalBenefit = "Sterilitas tinggi pada tindakan CT Fluoroscopy",
            badgeLabel = "CT Intervensi",
            previewColorHex = 0xFF1F4FB5,
            secondaryColorHex = 0xFF55BBDC,
            drawableRes = R.drawable.nano_uniform_scrub_blue,
            unlockRequirement = UnlockRequirement("m03", "M03: Instrumentasi Tabung & Detektor CT", 5, "Syarat rencana: 5 simpul M03")
        ),
        CharacterItem(
            id = "uniform_scrub_teal",
            name = "Scrub Trauma & Kontras IV",
            category = CharacterCategory.SERAGAM,
            description = "Seragam tim perawat radiologi dan anestesi pemantau injeksi kontras Iodin.",
            clinicalBenefit = "Visibilitas tinggi di ruang kontrol & kesiapan penanganan alergi kontras",
            badgeLabel = "Tim Kontras",
            previewColorHex = 0xFF7FDCD6,
            secondaryColorHex = 0xFF3CC4A3,
            drawableRes = R.drawable.nano_uniform_scrub_teal,
            unlockRequirement = UnlockRequirement("m04", "M04: Generasi CT, MSCT & Helikal Pitch", 5, "Syarat rencana: 5 simpul M04")
        ),
        CharacterItem(
            id = "uniform_apron_shield",
            name = "Apron Timbal Proteksi (0,5 mm Pb)",
            category = CharacterCategory.SERAGAM,
            description = "Apron pelindung radiasi berlapis ekuivalen timbal 0,5 mm Pb lengkap dengan Thyroid Shield.",
            clinicalBenefit = "Menyerap >95% radiasi hambur Compton saat mendampingi pasien di ruang Gantry",
            badgeLabel = "0,5 mm Pb",
            previewColorHex = 0xFFFF8A1F,
            secondaryColorHex = 0xFFFFC93C,
            drawableRes = R.drawable.nano_uniform_vest_ppr,
            unlockRequirement = UnlockRequirement("m09", "M09: Artefak, Kalibrasi & QA Harian CT", 5, "Syarat rencana: 5 simpul M09")
        ),
        CharacterItem(
            id = "uniform_armor",
            name = "Suit Insinyur Gantry & HV",
            category = CharacterCategory.SERAGAM,
            description = "Seragam pelindung teknisi kalibrasi High-Frequency HV Generator, Slip-Ring, dan tabung CT.",
            clinicalBenefit = "Keamanan isolasi tegangan tinggi saat pemeliharaan Rotating Frame",
            badgeLabel = "HV Service",
            previewColorHex = 0xFF1FB6E8,
            secondaryColorHex = 0xFF1F4FB5,
            drawableRes = R.drawable.nano_uniform_cryo,
            unlockRequirement = UnlockRequirement("m05", "M05: Rekonstruksi Citra & Sinogram", 5, "Syarat rencana: Selesaikan M05")
        ),
        CharacterItem(
            id = "uniform_dosen",
            name = "Blazer Akademik Dosen CT",
            category = CharacterCategory.SERAGAM,
            description = "Blazer navy rapi dengan dasi cyan dan kartu pengajar departemen radiologi.",
            clinicalBenefit = "Seragam presentasi kuliah, ujian OSCE & supervisi residen CT",
            badgeLabel = "Dosen",
            previewColorHex = 0xFF1E3A5F,
            secondaryColorHex = 0xFF06B6D4,
            drawableRes = R.drawable.nano_uniform_dosen,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        ),
        CharacterItem(
            id = "uniform_mahasiswa",
            name = "Rompi Praktikum Kampus",
            category = CharacterCategory.SERAGAM,
            description = "Rompi praktikum biru cerah lengkap dengan saku buku catatan rumus HU & Pitch.",
            clinicalBenefit = "Mobilitas nyaman saat rotasi praktik konsol CT Scan",
            badgeLabel = "Mahasiswa",
            previewColorHex = 0xFF38BDF8,
            secondaryColorHex = 0xFFBFDBFE,
            drawableRes = R.drawable.nano_uniform_mahasiswa,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        ),
        CharacterItem(
            id = "uniform_leadfree",
            name = "Apron Komposit Bebas-Timbal",
            category = CharacterCategory.SERAGAM,
            description = "Apron proteksi radiasi ringan berbahan komposit Bismuth-Antimony setara 0,35 mm Pb.",
            clinicalBenefit = "Mengurangi beban punggung operator saat prosedur intervensi CT panjang",
            badgeLabel = "ALARA Shield",
            previewColorHex = 0xFF115E59,
            secondaryColorHex = 0xFF34D399,
            drawableRes = R.drawable.nano_uniform_leadfree,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        ),
        CharacterItem(
            id = "uniform_cardio_navy",
            name = "Jas Kardio-CTA Navy",
            category = CharacterCategory.SERAGAM,
            description = "Jas navy beraksen gelombang EKG cyan untuk spesialis CT Angiografi Koroner.",
            clinicalBenefit = "Mendukung koordinasi Prospective ECG-Gating (70–75% R-R) & Dual-Source CT",
            badgeLabel = "CCTA ECG",
            previewColorHex = 0xFF172554,
            secondaryColorHex = 0xFF22D3EE,
            drawableRes = R.drawable.nano_uniform_cardio_navy,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        ),
        CharacterItem(
            id = "uniform_pediatric_teal",
            name = "Scrub Pediatrik Image Gently",
            category = CharacterCategory.SERAGAM,
            description = "Scrub teal ramah anak dengan lencana kampanye pengurangan dosis radiasi pediatrik.",
            clinicalBenefit = "Mengingatkan penerapan protokol 80–100 kVp & kurva AEC pediatrik",
            badgeLabel = "Image Gently",
            previewColorHex = 0xFF14B8A6,
            secondaryColorHex = 0xFFFBBF24,
            drawableRes = R.drawable.nano_uniform_pediatric_teal,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        ),
        CharacterItem(
            id = "uniform_physics_pro",
            name = "Rompi Fisika Sinogram & QA",
            category = CharacterCategory.SERAGAM,
            description = "Rompi laboratorium fisikawan medis dengan diagram kurva MTF dan matriks Sinogram.",
            clinicalBenefit = "Identitas tim komisioning CTDIvol, kalibrasi HU & audit DRL",
            badgeLabel = "Fisika Medis",
            previewColorHex = 0xFF1E293B,
            secondaryColorHex = 0xFF38BDF8,
            drawableRes = R.drawable.nano_uniform_physics_pro,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        )
    )

    val headsetItems = listOf(
        CharacterItem(
            id = "headset_none",
            name = "Tanpa Headset",
            category = CharacterCategory.HEADSET,
            description = "Mode komunikasi langsung untuk edukasi persiapan pasien & skrining eGFR/alergi.",
            clinicalBenefit = "Optimal untuk anamnesis & penjelasan prosedur kontras IV",
            badgeLabel = "Langsung",
            previewColorHex = 0xFF8FB3CC,
            secondaryColorHex = 0xFFBFD9EC,
            drawableRes = null,
            unlockRequirement = UnlockRequirement(null, null, 0, "Terbuka sejak awal")
        ),
        CharacterItem(
            id = "headset_radio",
            name = "Headset Konsol Gantry",
            category = CharacterCategory.HEADSET,
            description = "Headset komunikasi ruang kontrol untuk memantau pasien di balik kaca timbal.",
            clinicalBenefit = "Suara jernih saat memantau injeksi kontras & akuisisi helikal",
            badgeLabel = "Konsol CT",
            previewColorHex = 0xFF1FB6E8,
            secondaryColorHex = 0xFF7FDCD6,
            drawableRes = R.drawable.nano_headset_radio,
            unlockRequirement = UnlockRequirement(null, null, 0, "Terbuka sejak awal")
        ),
        CharacterItem(
            id = "headset_neural",
            name = "Interkom Aba-Aba Napas",
            category = CharacterCategory.HEADSET,
            description = "Headset operator dengan mikrofon terarah untuk instruksi tarik & tahan napas otomatis.",
            clinicalBenefit = "Mencegah motion artifact pada CT Toraks, Abdomen & CTA Koroner",
            badgeLabel = "Dual-Mic",
            previewColorHex = 0xFF3CC4A3,
            secondaryColorHex = 0xFF1FB6E8,
            drawableRes = R.drawable.nano_headset_intercom,
            unlockRequirement = UnlockRequirement("m06", "M06: Matriks, HU & Windowing", 5, "Syarat rencana: 5 simpul M06")
        ),
        CharacterItem(
            id = "headset_pneumatic",
            name = "Headset Pasien Radiolusen",
            category = CharacterCategory.HEADSET,
            description = "Headset tabung akustik bebas komponen logam untuk pasien selama pemindaian kepala/leher.",
            clinicalBenefit = "Nol metal streak artifact pada pemindaian otak & basis kranii",
            badgeLabel = "Bebas Logam",
            previewColorHex = 0xFF7FDCD6,
            secondaryColorHex = 0xFFE6F2FB,
            drawableRes = R.drawable.nano_headset_pneumatic,
            unlockRequirement = UnlockRequirement("m08", "M08: Kontras Iodin, Bolus Tracking & 3D", 5, "Syarat rencana: 5 simpul M08")
        ),
        CharacterItem(
            id = "headset_holo",
            name = "Headset Telemetri Dosis DRL",
            category = CharacterCategory.HEADSET,
            description = "Headset pintar dengan notifikasi peringatan Dose Notification (CTDIvol & DLP).",
            clinicalBenefit = "Peringatan real-time bila estimasi dosis melampaui ambang DRL rumah sakit",
            badgeLabel = "Dose Alert",
            previewColorHex = 0xFFFF8A1F,
            secondaryColorHex = 0xFF1FB6E8,
            drawableRes = R.drawable.nano_headset_sar,
            unlockRequirement = UnlockRequirement("m09", "M09: Artefak, Kalibrasi & QA Harian CT", 5, "Syarat rencana: 5 simpul M09")
        ),
        CharacterItem(
            id = "headset_lecture",
            name = "Mic Klip Kuliah Dosen",
            category = CharacterCategory.HEADSET,
            description = "Mikrofon boom ringkas untuk penjelasan teori fisika sinar-X & demonstrasi workstation PACS.",
            clinicalBenefit = "Suara jernih tanpa mengganggu komunikasi konsol klinis",
            badgeLabel = "Kuliah",
            previewColorHex = 0xFF94A3B8,
            secondaryColorHex = 0xFF06B6D4,
            drawableRes = R.drawable.nano_headset_lecture,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        ),
        CharacterItem(
            id = "headset_comm_pro",
            name = "Headset Dual-Channel Trauma",
            category = CharacterCategory.HEADSET,
            description = "Headset komunikasi dua kanal antara ruang konsol CT, perawat kontras, dan dokter IGD.",
            clinicalBenefit = "Koordinasi cepat saat protokol Code Stroke & Whole-Body Trauma CT",
            badgeLabel = "Code Stroke",
            previewColorHex = 0xFF1E3A5F,
            secondaryColorHex = 0xFF06B6D4,
            drawableRes = R.drawable.nano_headset_comm_pro,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        ),
        CharacterItem(
            id = "headset_stealth_nr",
            name = "Earmuff Servis Gantry",
            category = CharacterCategory.HEADSET,
            description = "Pelindung telinga teknisi saat pengujian mekanis putaran drum Gantry 10.000 RPM.",
            clinicalBenefit = "Kenyamanan akustik saat kalibrasi kecepatan rotasi 0,25 detik",
            badgeLabel = "QA Mekanis",
            previewColorHex = 0xFF1E293B,
            secondaryColorHex = 0xFF10B981,
            drawableRes = R.drawable.nano_headset_stealth_nr,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        ),
        CharacterItem(
            id = "headset_bone_cond",
            name = "Headset Konduksi Tulang",
            category = CharacterCategory.HEADSET,
            description = "Headset ramping pelipis agar operator tetap mendengar alarm injektor kontras & interkom.",
            clinicalBenefit = "Komunikasi aba-aba tahan napas tetap jelas sambil memantau ruang Gantry",
            badgeLabel = "CT Interkom",
            previewColorHex = 0xFF06B6D4,
            secondaryColorHex = 0xFFE2E8F0,
            drawableRes = R.drawable.nano_headset_bone_cond,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        )
    )

    val accessoryItems = listOf(
        CharacterItem(
            id = "acc_dosimeter_detector",
            name = "Surveymeter & Pendeteksi Logam",
            category = CharacterCategory.AKSESORIS,
            description = "Sensor genggam pemantau paparan radiasi hambur & skrining atribut logam pasien.",
            clinicalBenefit = "Memastikan keamanan radiasi ruangan & mencegah Metal Streak Artifact",
            badgeLabel = "Surveymeter",
            previewColorHex = 0xFF1FB6E8,
            secondaryColorHex = 0xFFFF8A1F,
            drawableRes = R.drawable.nano_tool_detector,
            unlockRequirement = UnlockRequirement(null, null, 0, "Terbuka sejak awal")
        ),
        CharacterItem(
            id = "acc_badge_zona4",
            name = "Dosimeter Personal TLD / OSL",
            category = CharacterCategory.AKSESORIS,
            description = "Lencana dosimeter termoluminesensi wajib bagi pekerja radiasi di instalasi CT Scan.",
            clinicalBenefit = "Merekam akumulasi dosis efektif bulanan personel sesuai prinsip ALARA",
            badgeLabel = "TLD Badge",
            previewColorHex = 0xFF3CC4A3,
            secondaryColorHex = 0xFF7FDCD6,
            drawableRes = R.drawable.nano_tool_badge,
            unlockRequirement = UnlockRequirement("m01", "M01: Fisika Dasar Sinar-X & Atenuasi CT", 5, "Syarat rencana: 5 simpul M01")
        ),
        CharacterItem(
            id = "acc_phantom",
            name = "Fantom Air Silinder QA (0 HU)",
            category = CharacterCategory.AKSESORIS,
            description = "Silinder akrilik berisi air murni untuk kalibrasi harian 5-ROI pesawat CT Scan.",
            clinicalBenefit = "Memverifikasi akurasi CT Number air (0 ± 4 HU) & keseragaman (≤ 5 HU)",
            badgeLabel = "QA 5-ROI",
            previewColorHex = 0xFF55BBDC,
            secondaryColorHex = 0xFF1F4FB5,
            drawableRes = R.drawable.nano_tool_phantom,
            unlockRequirement = UnlockRequirement("m08", "M08: Kontras Iodin, Bolus Tracking & 3D", 5, "Syarat rencana: 5 simpul M08")
        ),
        CharacterItem(
            id = "acc_clipboard",
            name = "Tablet Konsol WW/WL & Pitch",
            category = CharacterCategory.AKSESORIS,
            description = "Panel digital pengaturan kVp, mAs efektif, Pitch helikal, Kernel, & preset Windowing.",
            clinicalBenefit = "Simulasi kontras Brain, Stroke, Lung, dan Bone Window secara instan",
            badgeLabel = "Parameter CT",
            previewColorHex = 0xFF7FDCD6,
            secondaryColorHex = 0xFF1FB6E8,
            drawableRes = R.drawable.nano_tool_tablet,
            unlockRequirement = UnlockRequirement("m02", "M02: Anatomi Pesawat CT & Gantry", 5, "Syarat rencana: 5 simpul M02")
        ),
        CharacterItem(
            id = "acc_oximeter",
            name = "Monitor EKG & SpO₂ Gating",
            category = CharacterCategory.AKSESORIS,
            description = "Sensor pemantau detak jantung R-R dan saturasi oksigen untuk CT Angiografi Koroner.",
            clinicalBenefit = "Sinkronisasi Prospective ECG-Gating pada fase diastolik tenang (70–75% R-R)",
            badgeLabel = "ECG-Gating",
            previewColorHex = 0xFFFF8A1F,
            secondaryColorHex = 0xFF3CC4A3,
            drawableRes = R.drawable.nano_tool_oximeter,
            unlockRequirement = UnlockRequirement("m09", "M09: Artefak, Kalibrasi & QA Harian CT", 5, "Syarat rencana: 5 simpul M09")
        ),
        CharacterItem(
            id = "acc_coil_surface",
            name = "Modul Detektor Solid-State GOS",
            category = CharacterCategory.AKSESORIS,
            description = "Unit matriks kristal sintilator Gd₂O₂S + Silicon Photodiode + sekat Tungsten anti-scatter.",
            clinicalBenefit = "Menyerap 98% foton sinar-X dan mengubahnya menjadi sinyal digital Sinogram di DAS",
            badgeLabel = "DQE 98%",
            previewColorHex = 0xFFE6F2FB,
            secondaryColorHex = 0xFF1FB6E8,
            drawableRes = R.drawable.nano_tool_coil,
            unlockRequirement = UnlockRequirement("m07", "M07: Kualitas Citra & Dosimetri CT", 5, "Syarat rencana: 5 simpul M07")
        ),
        CharacterItem(
            id = "acc_laser_pointer",
            name = "Pointer Presentasi PACS 3D",
            category = CharacterCategory.AKSESORIS,
            description = "Tongkat penunjuk kuliah untuk mendemonstrasikan anatomi potongan MPR, MIP, dan VRT.",
            clinicalBenefit = "Memperjelas ajaran proyeksi Sinogram, skala HU & artefak klinis CT",
            badgeLabel = "Ajar CT",
            previewColorHex = 0xFFE2E8F0,
            secondaryColorHex = 0xFFF43F5E,
            drawableRes = R.drawable.nano_tool_pointer,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        ),
        CharacterItem(
            id = "acc_handbook_ct",
            name = "Buku Saku Protokol & Rumus CT",
            category = CharacterCategory.AKSESORIS,
            description = "Panduan ringkas rumus Beer-Lambert, skala HU, Pitch helikal, CTDIvol/DLP, dan Bolus Tracking.",
            clinicalBenefit = "Rujukan cepat mahasiswa saat menyusun protokol eksposi & evaluasi kontras IV",
            badgeLabel = "Referensi CT",
            previewColorHex = 0xFF06B6D4,
            secondaryColorHex = 0xFF0E7490,
            drawableRes = R.drawable.nano_tool_handbook,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        ),
        CharacterItem(
            id = "acc_ctdi_meter",
            name = "Pencil Ionization Chamber 100mm",
            category = CharacterCategory.AKSESORIS,
            description = "Dosimeter bilik ionisasi pensil sepanjang 100 mm untuk pengukuran CTDI₁₀₀ pada fantom PMMA.",
            clinicalBenefit = "Mengukur dosis tengah & perifer fantom 16/32 cm untuk verifikasi CTDIw dan CTDIvol",
            badgeLabel = "CTDI Meter",
            previewColorHex = 0xFFF59E0B,
            secondaryColorHex = 0xFF34D399,
            drawableRes = R.drawable.nano_tool_gaussmeter,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        ),
        CharacterItem(
            id = "acc_cryo_gauge",
            name = "Indikator Kapasitas Panas MHU",
            category = CharacterCategory.AKSESORIS,
            description = "Meter pemantau suhu anoda tabung sinar-X (% OLP / Mega Heat Units) & tekanan injektor PSI.",
            clinicalBenefit = "Memastikan Tube Warm-Up pagi hari tercapai dan mencegah batas tekanan >300 PSI",
            badgeLabel = "MHU & PSI",
            previewColorHex = 0xFFCBD5E1,
            secondaryColorHex = 0xFF38BDF8,
            drawableRes = R.drawable.nano_tool_cryo_gauge,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        ),
        CharacterItem(
            id = "acc_ct_keycard",
            name = "Kartu Otorisasi Konsol CT",
            category = CharacterCategory.AKSESORIS,
            description = "Kartu akses personel berlisensi untuk mengaktifkan eksposi sinar-X & kalibrasi udara.",
            clinicalBenefit = "Menjamin keselamatan ruang pemeriksaan berlapis timbal (Pb) sebelum eksposi",
            badgeLabel = "Lisensi PPR",
            previewColorHex = 0xFF0E7490,
            secondaryColorHex = 0xFF22D3EE,
            drawableRes = R.drawable.nano_tool_ct_keycard,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        )
    )

    val backgroundItems = listOf(
        CharacterItem(
            id = "bg_control_room",
            name = "Ruang Konsol Operator CT",
            category = CharacterCategory.LATAR,
            description = "Meja konsol operator di balik jendela kaca timbal (Lead Glass) dengan monitor rekonstruksi MPR.",
            clinicalBenefit = "Pusat kendali parameter kVp/mAs, Bolus Tracking & pemantauan pasien",
            badgeLabel = "Konsol CT",
            previewColorHex = 0xFF1FB6E8,
            secondaryColorHex = 0xFF0E5F86,
            drawableRes = R.drawable.bg_control_room,
            unlockRequirement = UnlockRequirement(null, null, 0, "Terbuka sejak awal")
        ),
        CharacterItem(
            id = "bg_changing_room",
            name = "Ruang Persiapan & Skrining Pasien",
            category = CharacterCategory.LATAR,
            description = "Ruang ganti pakaian pasien dengan loker penyimpanan perhiasan/logam & meja kanulasi IV.",
            clinicalBenefit = "Area pelepasan atribut logam pemicu streak artifact & pemasangan Abocath 18G–20G",
            badgeLabel = "Persiapan",
            previewColorHex = 0xFF3CC4A3,
            secondaryColorHex = 0xFF0F2A45,
            drawableRes = R.drawable.bg_changing_room,
            unlockRequirement = UnlockRequirement(null, null, 0, "Terbuka sejak awal")
        ),
        CharacterItem(
            id = "bg_scanner_suite",
            name = "Suite Gantry MSCT 128-Slice",
            category = CharacterCategory.LATAR,
            description = "Ruang pemeriksaan berlapis timbal (Pb 2 mm) dengan Gantry Wide-Bore & meja Carbon Fiber.",
            clinicalBenefit = "Dilengkapi Laser Positioning 3-sumbu & Dual-Head Power Injector",
            badgeLabel = "Suite Gantry",
            previewColorHex = 0xFF1F4FB5,
            secondaryColorHex = 0xFF55BBDC,
            drawableRes = R.drawable.bg_scanner_suite,
            unlockRequirement = UnlockRequirement("m02", "M02: Anatomi Pesawat CT & Gantry", 5, "Syarat rencana: Selesaikan M02")
        ),
        CharacterItem(
            id = "bg_reading_room",
            name = "Ruang Baca PACS & 3D Workstation",
            category = CharacterCategory.LATAR,
            description = "Ruang diagnostik dokter radiolog dengan monitor kalibrasi DICOM untuk evaluasi multi-window.",
            clinicalBenefit = "Analisis kuantitatif ROI HU, MPR, MIP, MinIP, VRT & CT Perfusi",
            badgeLabel = "Diagnostik 3D",
            previewColorHex = 0xFFFF8A1F,
            secondaryColorHex = 0xFF1F4FB5,
            drawableRes = R.drawable.bg_reading_room,
            unlockRequirement = UnlockRequirement("m03", "M03: Instrumentasi Tabung & Detektor CT", 6, "Syarat rencana: Selesaikan M03")
        ),
        CharacterItem(
            id = "bg_physics_lab",
            name = "Lab Fisika Medis & Dosimetri CT",
            category = CharacterCategory.LATAR,
            description = "Laboratorium uji kurva atenuasi Beer-Lambert, MTF, fantom air QA & dosimetri CTDIvol.",
            clinicalBenefit = "Eksperimen kalibrasi skala Hounsfield Unit & keseragaman detektor",
            badgeLabel = "Lab QA CT",
            previewColorHex = 0xFF7FDCD6,
            secondaryColorHex = 0xFF3CC4A3,
            drawableRes = R.drawable.bg_physics_lab,
            unlockRequirement = UnlockRequirement("m04", "M04: Generasi CT, MSCT & Helikal Pitch", 6, "Syarat rencana: Selesaikan M04")
        ),
        CharacterItem(
            id = "bg_led_stage",
            name = "Panggung Hologram Sinogram & 3D",
            category = CharacterCategory.LATAR,
            description = "Panggung visualisasi futuristik proyeksi Transformasi Radon, FBP, dan AI Deep Learning Recon.",
            clinicalBenefit = "Simulasi lintasan spiral Helikal Pitch & matriks voxel isotropik",
            badgeLabel = "Holografik",
            previewColorHex = 0xFF55BBDC,
            secondaryColorHex = 0xFF7FDCD6,
            drawableRes = R.drawable.bg_led_stage,
            unlockRequirement = UnlockRequirement("m07", "M07: Kualitas Citra & Dosimetri CT", 5, "Syarat rencana: Selesaikan M07")
        ),
        CharacterItem(
            id = "bg_contrast_room",
            name = "Ruang Persiapan Kontras Iodin IV",
            category = CharacterCategory.LATAR,
            description = "Ruang persiapan Dual-Head Power Injector, penghangat kontras Iodin 37 °C & Saline NaCl 0,9%.",
            clinicalBenefit = "Persiapan protokol CT Angiografi, Multiphasic Hepar & troli emergensi alergi",
            badgeLabel = "Injektor Iodin",
            previewColorHex = 0xFF1FB6E8,
            secondaryColorHex = 0xFF3CC4A3,
            drawableRes = R.drawable.bg_contrast_room,
            unlockRequirement = UnlockRequirement("m08", "M08: Kontras Iodin & 3D Post-Processing", 5, "Syarat rencana: 5 simpul M08")
        ),
        CharacterItem(
            id = "bg_chiller_room",
            name = "Ruang PDU, Generator HV & Chiller",
            category = CharacterCategory.LATAR,
            description = "Instalasi Power Distribution Unit (PDU) 3-fasa, sistem pendingin Chiller & kabinet IRS.",
            clinicalBenefit = "Menjaga kestabilan daya 80–140 kVp dan pendinginan anoda tabung CT",
            badgeLabel = "Teknis • PDU",
            previewColorHex = 0xFFFFC93C,
            secondaryColorHex = 0xFF1F4FB5,
            drawableRes = R.drawable.bg_chiller_room,
            unlockRequirement = UnlockRequirement("m05", "M05: Rekonstruksi Citra & Sinogram", 5, "Syarat rencana: 5 simpul M05")
        ),
        CharacterItem(
            id = "bg_lecture_hall",
            name = "Auditorium Kuliah Radiologi CT",
            category = CharacterCategory.LATAR,
            description = "Auditorium kuliah interaktif lengkap dengan proyektor anatomi Gantry CT & podium dosen.",
            clinicalBenefit = "Suasana diskusi akademik fisika CT Scan & pembahasan kasus klinis",
            badgeLabel = "Kuliah CT",
            previewColorHex = 0xFF29598A,
            secondaryColorHex = 0xFF22D3EE,
            drawableRes = R.drawable.bg_lecture_hall,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        ),
        CharacterItem(
            id = "bg_simulation_center",
            name = "Pusat Riset Photon-Counting & AI",
            category = CharacterCategory.LATAR,
            description = "Laboratorium riset mutakhir detektor semikonduktor CdTe, Dual-Energy & rekonstruksi DLIR.",
            clinicalBenefit = "Eksperimen rekonstruksi spektral multi-energi & reduksi dosis ultra-rendah",
            badgeLabel = "Riset • PCD",
            previewColorHex = 0xFF1F4975,
            secondaryColorHex = 0xFF06B6D4,
            drawableRes = R.drawable.bg_simulation_center,
            unlockRequirement = UnlockRequirement(futureNote = "Tersedia sejak awal")
        )
    )

    val allItems: List<CharacterItem> =
        hairItems + uniformItems + headsetItems + accessoryItems + backgroundItems

    fun getItem(id: String): CharacterItem? {
        return allItems.find { it.id == id }
    }

    /**
     * Menghitung apakah item sudah terbuka.
     * Selama [ENFORCE_CURRICULUM_LOCKS] bernilai `false`, fungsi ini selalu mengembalikan `true`
     * sehingga semua item dapat langsung dipilih oleh pengguna.
     */
    fun isItemUnlocked(
        item: CharacterItem,
        completedNodeCodes: Set<String> = emptySet(),
        modules: List<CtModule> = emptyList(),
        enforceLocks: Boolean = ENFORCE_CURRICULUM_LOCKS
    ): Boolean {
        if (!enforceLocks) return true
        val req = item.unlockRequirement
        val moduleId = req.requiredModuleId ?: return true
        if (req.requiredNodesCount <= 0) return true
        val module = modules.find { it.id.equals(moduleId, ignoreCase = true) || it.code.equals(moduleId, ignoreCase = true) }
        val completedInModule = module?.nodes?.count { it.code in completedNodeCodes } ?: completedNodeCodes.size
        return completedInModule >= req.requiredNodesCount
    }
}
