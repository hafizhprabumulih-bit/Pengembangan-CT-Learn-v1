# Aturan Proyek CT-Learn (AGENTS.md)

Panduan dan batasan teknis untuk agen & kontributor yang bekerja di repositori **CT-Learn**:

1. **Pemisahan Logika `CtViewModel.kt`**: Logika perhitungan fisika CT, pemetaan Peta Belajar, XP, reward, evaluasi kasus konsol, kuis tingkat, dan Try-Out diletakkan pada fungsi/kelas murni di `com.example.util` atau state holder UI (`com.example.ui.path`).
2. **Keamanan Database Room**:
   - DILARANG keras menggunakan `fallbackToDestructiveMigration()` pada kode produksi (`CtDatabase.kt`). Tabel data pengguna (`node_progress`, `study_notes`, `quiz_attempts`, `study_days`) berada di file DB yang sama (`ct_learn.db`).
   - Setiap perubahan skema wajib menaikkan `DB_VERSION`, mengekspor schema Room ke `app/schemas/com.example.data.local.CtDatabase/`, dan menulis `Migration` nyata di `Migrations.kt`.
3. **Kompatibilitas Kode Simpul**:
   - Selalu pertahankan `LegacyNodeCodes.normalize(...)` dan `LegacyNodeCodes.normalizeAll(...)` saat membandingkan atau menyimpan kode simpul.
   - Jangan mengubah kode simpul di basis data atau data kurikulum yang ada (`CtCurriculumModulesPart1.kt`, `CtCurriculumModulesPart2.kt`), dan pertahankan ID/nodeCode unik serta minimal 1 kuis per simpul (`CtCurriculumQuizzes.kt`).
4. **Navigasi & Pemulihan Scroll**:
   - Pertahankan alur `NavSession`, `BackHandler` alami (Pembaca -> Peta Modul -> Beranda), `rememberSaveableStateHolder` antar-tab, dan pemulihan scroll (`RememberSavedScrollState`).
5. **Tema & Aksesibilitas**:
   - Gunakan token tema aplikasi (`Color.kt` / `MaterialTheme.colorScheme`) dan komponen yang ada; jangan menyalin warna mentah di luar palet tema.
   - Pastikan target sentuh minimal `48.dp` untuk tombol interaktif dan `contentDescription` deskriptif.
6. **Pengujian & Verifikasi**:
   - Ekstrak logika baru ke fungsi murni di `com.example.util` disertai unit test JVM (`app/src/test/java/com/example/`).
   - Jangan jalankan build Gradle/emulator secara lokal jika pengguna meminta verifikasi lewat GitHub Actions / Google AI Studio.
