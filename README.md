# CT-Learn (Android · Jetpack Compose)

Aplikasi pembelajaran interaktif fisika sinar-X, anatomi dan instrumentasi pesawat Computed Tomography (CT Scan), generasi MSCT, rekonstruksi Sinogram/FBP/IR/DLIR, skala Hounsfield Unit (HU), dosimetri CTDIvol/DLP/SSDE, media kontras Iodin & Bolus Tracking, serta kendali mutu (QA) harian untuk mahasiswa radiologi dan radiografer.

## Fitur Utama CT-Learn
- **Kurikulum Terstruktur 9 Modul (`M01`–`M09`)**: Mencakup **50 simpul materi** lengkap dengan analogi klinis, *nanoHints*, rangkuman interaktif (`lesson_extras.json`), tes lompat (`SkipTestBuilder`), dan **50 soal kuis kurikulum** berbasis tingkat pemahaman.
- **5 Laboratorium Simulator Konsol CT**:
  1. Gantry Gen-3, Slip-Ring & Rantai Sinyal Detektor Solid-State/DAS + Efek Off-Centering Laser Isocenter.
  2. Windowing HU (WW/WL) 3 Potongan Anatomi (Kepala, Toraks, Abdomen) & Kalkulator Atenuasi Beer-Lambert.
  3. Geometri MSCT Helikal, Pitch (`d / (N × T)`), Filtered Back-Projection vs Iterative Recon/DLIR, dan Kernel.
  4. Konsol Eksposi, Dosimetri (`CTDIvol`, `SSDE`, `DLP`, `Dosis Efektif`), Kurva Time-Attenuation Bolus Tracking, 3D MPR/MIP/MinIP/VRT & **5 Tantangan Kasus Konsol Klinis** (`CtConsoleCaseEvaluator`).
  5. Artefak Klinis (Ring, Beam Hardening, Motion, Metal Streak/MAR) & Uji QA Fantom Air 5-ROI Harian.
- **Try-Out Uji Kompetensi (UKOM) CT Scan (`CtPracticeExamEngine`)**: Bank soal terpadu (50 soal kurikulum + 25 kasus klinis UKOM CT lanjutan) lintas 5 domain kompetensi, lengkap dengan hitung mundur, preservasi jawaban (`rememberSaveable`), dan **Mode Adaptif Prioritas Domain Terlemah**.
- **Animasi Fisika Interaktif 4 Mode (`LearningAnimationScreen`)**: Rotasi Gantry & Rantai Sinyal, Atenuasi Beer-Lambert & Kontras HU, Sinogram & Rekonstruksi FBP, serta Pitch Helikal & Bow-Tie Filter.
- **Arcade & Minigame Edukatif (`CtArcadeEngine`, `CtMinigameEngine`)**: *Skrining Persiapan Bore CT*, *Pemicu ROI Bolus Tracking*, *Windowing HU & Artefak CT*, *Metal Streak Defense*, dan *Gantry Sync Rhythm*.
- **Ruang Karakter & Ekspor Catatan PDF (`NotesPdfExporter`)**: 11 peran spesialis radiologi & CT, sakelar Mode Progresi Kunci Item & Transisi Cepat, serta ekspor Catatan & Kartu Referensi Protokol CT ke PDF/JSON.

## Struktur Arsitektur Utama
- `app/src/main/java/com/example/viewmodel/CtViewModel.kt` — State holder utama (`CtUiState`, `CtSimType`), navigasi riwayat sesi, dan orkestrasi Room/DataStore.
- `app/src/main/java/com/example/util/CtPhysicsCalculator.kt` — Fungsi fisika medis & dosimetri CT murni (*Single Source of Truth*).
- `app/src/main/java/com/example/util/` — Logika domain murni & dapat diuji di JVM (`PathStateResolver`, `HomePathPlanner`, `XpCalculator`, `CtPracticeExamEngine`, `CtConsoleCaseEvaluator`, `CtArcadeEngine`, `CtMinigameEngine`, `NotesPdfExporter`).
- `app/src/main/java/com/example/data/` — Repositori kurikulum (`CtCurriculumRepository`, `CtCurriculumModulesPart1/2`, `CtCurriculumQuizzes`), `UserDataRepository`, dan `lesson/LessonExtras.kt`.
- `app/src/main/java/com/example/data/local/` — Room Database `CtDatabase` (`ct_learn.db`) versi `14` dengan skema ekspor di `app/schemas/com.example.data.local.CtDatabase/` dan migrasi non-destruktif `Migrations.kt`.

## Pengujian & Build CI (GitHub Actions & Google AI Studio)
Alur GitHub Actions tersedia pada `.github/workflows/android-ci.yml` dengan JDK 17 dan Gradle 9.3.1:

```bash
./gradlew testDebugUnitTest assembleDebug --stacktrace
```
*(Bila `./gradlew` tidak ada pada mesin lokal, gunakan `gradle testDebugUnitTest assembleDebug --stacktrace` dengan Gradle 9.3.1 & JDK 17).*
