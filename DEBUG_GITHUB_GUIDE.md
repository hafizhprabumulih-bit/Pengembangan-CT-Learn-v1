# Panduan Lengkap Debug, Push, & Pengambilan APK di GitHub (MRI EDU v4.0.3)

Dokumen ini adalah panduan teknis resmi bagi pengembang dan kontributor proyek **MRI EDU v4.0.3** untuk melakukan sinkronisasi kode ke GitHub, memantau pipeline otomatisasi GitHub Actions CI, mengunduh file `app-debug.apk` langsung dari server GitHub, serta melakukan pembaruan versi di ponsel tanpa kendala tanda tangan (*keystore conflict*).

---

## 1. Solusi Penting: Kenapa di HP "Masih Versi Lama" atau Tidak Bisa Terpasang?

Jika sebelumnya Anda mengunduh APK dari GitHub tetapi di HP **tetap menampilkan versi lama** atau muncul pesan **"Aplikasi tidak terpasang"**:

1. **Penyebab:**
   - Mesin virtual CI GitHub Actions sebelumnya menghasilkan sertifikat kunci acak (*random debug.keystore*) pada setiap run, berbeda dari sertifikat APK yang terpasang di HP Anda.
   - Sistem keamanan Android **menolak memasang APK baru yang kuncinya berbeda** (`INSTALL_FAILED_UPDATE_INCOMPATIBLE`), sehingga HP tetap menjalankan aplikasi versi lama yang ada.
2. **Solusi Permanen yang Sudah Diterapkan:**
   - Pipeline CI sekarang **otomatis memulihkan sertifikat resmi proyek** dari `debug.keystore.base64`. Kunci penandatanganan kini **100% konsisten dan identik** selamanya.
   - Versi aplikasi telah dinaikkan ke **`versionCode = 43`** dan **`versionName = "4.0.3"`**.
3. **Langkah Sekali Saja pada HP Anda Saat Ini:**
   - **Copot pemasangan (Uninstall/Hapus)** aplikasi MRI EDU lama yang sedang terpasang di HP Anda.
   - Pasang file **`app-debug.apk`** versi 4.0.3 yang baru diunduh dari GitHub Actions.
   - Setelah langkah ini, seluruh pembaruan berikutnya dapat langsung di-*update* (ditimpa) tanpa perlu uninstall lagi!

---

## 2. Pembaruan Logika Review & Materi Belajar

- **Mode Pelajaran Bertahap Otomatis**:
  - Sebelumnya, tombol "Lanjutkan Belajar" dan "Ulasan Hari Ini" membuka reader teks lama.
  - Sekarang, routing otomatis diarahkan ke **Mode Pelajaran Bertahap (`LessonScreen`)** dengan penjelasan terstruktur, kartu interaktif, kuis simpul, dan maskot Nano pendamping.
- **Dukungan Berkas & Provider (FileProvider)**:
  - Konfigurasi `file_paths.xml` telah diperluas mencakup cache, files, dan external storage sehingga ekspor PDF catatan belajar tidak mengalami galat akses berkas di HP.

---

## 3. Persiapan & Push Kode ke GitHub

Jalankan serangkaian perintah git berikut dari root repositori:

```bash
# 1. Periksa status file yang berubah
git status

# 2. Tambahkan seluruh berkas perubahan ke staging
git add .

# 3. Lakukan commit dengan pesan terstruktur
git commit -m "fix: pulihkan keystore konsisten ci, bump versionCode 43 v4.0.3, route ulasan materi ke lesson mode, dan fileprovider paths"

# 4. Kirimkan perubahan ke cabang utama di GitHub
git push origin main
```

> **Catatan Pengaturan Remote Awal:** Jika remote GitHub belum dikonfigurasi pada direktori lokal Anda:
> ```bash
> git remote add origin https://github.com/<username_anda>/<nama_repo>.git
> git branch -M main
> git push -u origin main
> ```

---

## 4. Cara Mengambil APK Hasil Build di GitHub Actions

Setiap kali Anda melakukan `git push` ke cabang `main` atau membuka Pull Request, alur kerja **GitHub Actions (`.github/workflows/android-ci.yml`)** otomatis terpicu:

1. **Buka Web GitHub:**
   Akses repositori Anda di browser: `https://github.com/<username_anda>/<nama_repo>`.
2. **Masuk ke Tab Actions:**
   Klik tab **"Actions"** di bilah menu atas repositori.
3. **Pilih Workflow Run Terbaru:**
   Klik alur kerja paling atas yang baru saja berjalan (bernama **"Android CI"** dengan pesan commit Anda).
4. **Periksa Status Pipeline:**
   - Bila bertanda centang hijau **✅ Build Debug APK & Run Unit Tests**, seluruh 33 unit test lulus dan file APK berhasil dikompilasi!
   - Di halaman ringkasan (**Summary**), terdapat laporan run otomatis berformat markdown yang mencantumkan status, branch, commit SHA, dan panduan unduhan.
5. **Unduh Artefak APK:**
   - Gulir ke bagian paling bawah ke tabel **Artifacts**.
   - Klik **`mri-edu-debug-apk`** untuk mengunduh arsip zip.
   - Ekstrak berkas zip tersebut untuk mendapatkan file **`app-debug.apk`**.
   - Pasang file `app-debug.apk` ke ponsel Android Anda.
6. **Memicu Build Manual (Workflow Dispatch):**
   Jika Anda ingin memicu build ulang kapan saja tanpa push commit baru:
   Masuk ke tab **Actions** → pilih **Android CI** di daftar sebelah kiri → klik tombol **"Run workflow"** → pilih cabang `main` → klik **Run workflow**.

---

## 5. Matriks Diagnostik Masalah Build & CI

| Gejala / Pesan Galat | Kemungkinan Penyebab | Tindakan Solusi |
|---|---|---|
| **Aplikasi tidak terpasang / Masih versi lama** | Keystore versi sebelumnya bentrok dengan keystore baru. | Copot (Uninstall) aplikasi lama di HP sekali saja, lalu pasang APK v4.0.3 baru. CI kini memakai `debug.keystore.base64` resmi secara konsisten. |
| **`INSTALL_FAILED_UPDATE_INCOMPATIBLE`** | Penandatanganan APK berbeda antara dua build. | Jalankan `adb uninstall com.example` sebelum memasang APK baru, atau hapus aplikasi manual dari layar ponsel terlebih dahulu. |
| **`java.lang.NullPointerException: Cannot invoke ... Application.getService` pada KSP** | Konflik runtime cache atau Gradle daemon thread pada KSP. | Jalankan `gradle --stop` lalu ulangi kompilasi dengan `gradle assembleDebug --stacktrace`. |
| **SDK Licenses not accepted** | Mesin CI belum menyetujui lisensi SDK Android Google. | File `.github/workflows/android-ci.yml` telah dipersenjatai skrip auto-accept lisensi pada `android-sdk-license` dan `android-sdk-preview-license`. |
| **Unit Test FAILED di GitHub Actions** | Terdapat regresi logika pada perhitungan poin, Larmor, kurikulum, atau database. | Unduh artefak **`unit-test-reports`** pada tab Artifacts, buka `index.html` di browser untuk melihat baris pengujian yang gagal. |

---

## 6. Pengujian & Kompilasi Lokal di Komputer Anda

Sebelum melakukan push ke GitHub, Anda dapat memverifikasi aplikasi secara mandiri di komputer lokal:

### A. Menjalankan Seluruh Unit Test (JVM & Robolectric)
```bash
gradle testDebugUnitTest
```
Laporan HTML pengujian unit akan otomatis tersedia di:
`app/build/reports/tests/testDebugUnitTest/index.html`

### B. Membangun APK Debug Secara Lokal
```bash
gradle assembleDebug
```
File APK debug siap pakai akan dihasilkan di:
`app/build/outputs/apk/debug/app-debug.apk`

### C. Memasang Langsung ke Perangkat Fisik via Kabel USB (ADB)
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```
