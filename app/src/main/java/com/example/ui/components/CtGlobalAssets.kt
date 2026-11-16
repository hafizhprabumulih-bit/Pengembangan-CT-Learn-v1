package com.example.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CharAccentMint
import com.example.ui.theme.CharActionOrange
import com.example.ui.theme.CyanBright
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.EmeraldSuccess
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

enum class CtGlobalAssetKey(val contentDescriptionId: String) {
    // Aset raster bawaan game & fallback global
    GLOBAL_CT_EMBLEM("Emblem CT-Learn Global"),
    BOLUS_MONITOR("Monitor Kurva Time-Attenuation Bolus Tracking CT"),
    SCREENING_GATE("Portal Skrining Logam & Persiapan Bore CT"),
    METAL_DEFENSE_ARENA("Arena Pencegahan Metal Streak Bore CT"),
    GANTRY_RHYTHM_STAGE("Panggung Akuisisi Helikal, DAS & FBP CT"),
    CT_GANTRY("Mesin Pemindai Multi-Slice CT Scan"),

    // Simbol status & lencana pengganti stiker emoji
    TROPHY_MEDAL("Lencana Rekor & Pangkat"),
    SHIELD_ALARA("Perisai Proteksi Radiasi & ALARA CT"),
    SPARK_BOLT("Energi Berkas Sinar-X & Combo"),
    GAME_CONTROLLER("Mode Arcade & Minigame CT"),
    PULSE_WAVE("Gelombang Proyeksi Sinogram CT"),
    HU_SLICE("Citra Irisan Aksial & Windowing HU CT"),
    FIRE_STREAK("Lencana Streak Harian"),
    CLINICAL_ID_BADGE("Lencana Identitas Radiografer"),
    STAR_BADGE("Bintang Penguasaan Simpul"),
    CHECK_VERIFIED("Tanda Terverifikasi Klinis"),
    RADIOGRAPHER_CREST("Emblem Spesialis Radiologi & CT Scan"),

    // Benda skrining persiapan bore CT & proyektil logam / radiolusen aman
    TABUNG_O2("Tabung Baja Oksigen"),
    GUNTING_BAJA("Gunting Bedah Baja"),
    KLIP_LOGAM("Jepit / Klip Logam"),
    PACEMAKER("Kabel Lead EKG / Elektronik di FOV"),
    KUNCI_PAS("Kunci Pas Logam"),
    JAM_MEKANIK("Jam Tangan Logam"),
    KOIN_KUNCI("Koin & Kunci Logam"),
    KARTU_ATM("Lanyard Klip & Kartu di Leher"),
    KURSI_RODA_BESI("Benda Besi di Bore Gantry"),
    PINSET("Pinset Logam"),
    STETOSKOP("Kanul IV / Stetoskop Klinis"),
    BANTAL_BUSA("Bantal Fiksasi Busa Radiolusen"),
    KURSI_RODA_MR("Cradle & Meja Carbon Fiber"),
    EARPLUG("Spons Imobilisasi Kepala Non-Logam"),
    GAUN_KATUN("Gaun Pasien Katun Tanpa Logam"),
    VIAL_KONTRAS("Kontras Iodin Non-Ionik (LOCM)"),
    PHANTOM_ACR("Fantom Air Kalibrasi QA (0 HU)"),
    STENT_COND("Stent Koroner (Sharp Kernel CCTA)"),
    PONSEL_LOGAM("Ponsel & Gesper Logam"),
    TABUNG_PEMADAM("Tabung Logam Berat di FOV"),
    KACA_MATA_LOGAM("Kacamata Bingkai Logam"),
    POMPA_INFUS_BIASA("Pompa Infus Jalur Pendek"),
    BOTOL_MINUM_LOGAM("Botol Termos Baja"),
    SANDBAG_NONMAG("Sabuk Fiksasi Velcro Non-Logam"),
    SELIMUT_KATUN("Selimut Katun Polos Bebas Logam"),
    TABUNG_O2_ALU("Saline Chaser NaCl 0,9%"),
    IMPLAN_TITANIUM("Implan Titanium (Algoritma MAR)"),
    HEADSET_PNEUMATIK("Pelindung Radiasi di Luar FOV"),
    MASKER_OKSIGEN_PLASTIK("Kanul Nasal Polimer Bebas Kawat"),
    PISAU_BEDAH_BAJA("Skalpel Bedah Baja Karbon"),
    PALU_REFLEKS_BESI("Palu Refleks Batang Besi"),
    CINCIN_FEROMAGNETIK("Perhiasan & Peniti Logam"),
    KARTU_AKSES_MAGNET("Kalung Rantai Logam"),

    // Fallback global resmi
    FALLBACK_CT("Ilustrasi Gambar CT-Learn Global")
}

object CtGlobalAssetCatalog {
    fun resolveKey(rawKey: String?): CtGlobalAssetKey {
        if (rawKey.isNullOrBlank()) return CtGlobalAssetKey.FALLBACK_CT
        val norm = rawKey.trim().uppercase()
        return CtGlobalAssetKey.entries.firstOrNull { it.name == norm }
            ?: when {
                "TABUNG" in norm || "O2" in norm -> CtGlobalAssetKey.TABUNG_O2
                "GUNTING" in norm -> CtGlobalAssetKey.GUNTING_BAJA
                "KLIP" in norm -> CtGlobalAssetKey.KLIP_LOGAM
                "PACER" in norm || "PACEMAKER" in norm || "LEAD" in norm -> CtGlobalAssetKey.PACEMAKER
                "KUNCI_PAS" in norm -> CtGlobalAssetKey.KUNCI_PAS
                "KOIN" in norm || "KUNCI" in norm -> CtGlobalAssetKey.KOIN_KUNCI
                "JAM" in norm -> CtGlobalAssetKey.JAM_MEKANIK
                "ATM" in norm || "KARTU" in norm -> CtGlobalAssetKey.KARTU_ATM
                "KURSI_RODA_MR" in norm || "CARBON" in norm -> CtGlobalAssetKey.KURSI_RODA_MR
                "KURSI" in norm || "BORE" in norm -> CtGlobalAssetKey.KURSI_RODA_BESI
                "PINSET" in norm -> CtGlobalAssetKey.PINSET
                "STETO" in norm || "COND" in norm -> CtGlobalAssetKey.STETOSKOP
                "BANTAL" in norm -> CtGlobalAssetKey.BANTAL_BUSA
                "EARPLUG" in norm || "SPONS" in norm -> CtGlobalAssetKey.EARPLUG
                "GAUN" in norm -> CtGlobalAssetKey.GAUN_KATUN
                "VIAL" in norm || "IODIN" in norm -> CtGlobalAssetKey.VIAL_KONTRAS
                "PHANTOM" in norm -> CtGlobalAssetKey.PHANTOM_ACR
                "STENT" in norm -> CtGlobalAssetKey.STENT_COND
                "PONSEL" in norm || "HP" in norm || "SMARTPHONE" in norm -> CtGlobalAssetKey.PONSEL_LOGAM
                "PEMADAM" in norm || "APAR" in norm -> CtGlobalAssetKey.TABUNG_PEMADAM
                "KACAMATA" in norm || "KACA_MATA" in norm -> CtGlobalAssetKey.KACA_MATA_LOGAM
                "POMPA" in norm || "INFUS" in norm -> CtGlobalAssetKey.POMPA_INFUS_BIASA
                "BOTOL" in norm || "TERMOS" in norm -> CtGlobalAssetKey.BOTOL_MINUM_LOGAM
                "SANDBAG" in norm || "PASIR" in norm || "VELCRO" in norm -> CtGlobalAssetKey.SANDBAG_NONMAG
                "SELIMUT" in norm || "KATUN" in norm -> CtGlobalAssetKey.SELIMUT_KATUN
                "ALU" in norm || "ALUMINIUM" in norm || "SALINE" in norm -> CtGlobalAssetKey.TABUNG_O2_ALU
                "IMPLAN" in norm || "TITANIUM" in norm || "PELAT" in norm -> CtGlobalAssetKey.IMPLAN_TITANIUM
                "HEADSET" in norm || "PNEUMATIK" in norm || "SHIELD" in norm -> CtGlobalAssetKey.HEADSET_PNEUMATIK
                "MASKER" in norm || "POLIMER" in norm || "KANUL" in norm -> CtGlobalAssetKey.MASKER_OKSIGEN_PLASTIK
                "SKALPEL" in norm || "PISAU" in norm -> CtGlobalAssetKey.PISAU_BEDAH_BAJA
                "PALU" in norm || "REFLEKS" in norm -> CtGlobalAssetKey.PALU_REFLEKS_BESI
                "CINCIN" in norm || "PERHIASAN" in norm -> CtGlobalAssetKey.CINCIN_FEROMAGNETIK
                "LANYARD" in norm || "AKSES" in norm -> CtGlobalAssetKey.KARTU_AKSES_MAGNET
                "MONITOR" in norm || "BOLUS" in norm -> CtGlobalAssetKey.BOLUS_MONITOR
                "TROPHY" in norm || "REKOR" in norm -> CtGlobalAssetKey.TROPHY_MEDAL
                "SHIELD_ALARA" in norm || "PERISAI" in norm || "SHIELD_ALARA" in norm -> CtGlobalAssetKey.SHIELD_ALARA
                "HU_SLICE" in norm || "HU_SLICE" in norm -> CtGlobalAssetKey.HU_SLICE
                "STREAK" in norm || "FIRE" in norm -> CtGlobalAssetKey.FIRE_STREAK
                "STAR" in norm || "BINTANG" in norm -> CtGlobalAssetKey.STAR_BADGE
                "CHECK" in norm || "VERIF" in norm -> CtGlobalAssetKey.CHECK_VERIFIED
                "CREST" in norm || "RADIO" in norm -> CtGlobalAssetKey.RADIOGRAPHER_CREST
                "ID_BADGE" in norm -> CtGlobalAssetKey.CLINICAL_ID_BADGE
                "GANTRY" in norm -> CtGlobalAssetKey.CT_GANTRY
                "METAL_DEFENSE" in norm || "METAL_DEFENSE_ARENA" in norm -> CtGlobalAssetKey.METAL_DEFENSE_ARENA
                "RHYTHM" in norm -> CtGlobalAssetKey.GANTRY_RHYTHM_STAGE
                else -> CtGlobalAssetKey.FALLBACK_CT
            }
    }

    @DrawableRes
    fun drawableResOrNull(key: CtGlobalAssetKey): Int? = when (key) {
        CtGlobalAssetKey.GLOBAL_CT_EMBLEM,
        CtGlobalAssetKey.FALLBACK_CT -> R.drawable.img_mri_global_placeholder
        CtGlobalAssetKey.BOLUS_MONITOR -> R.drawable.img_larmor_monitor
        CtGlobalAssetKey.SCREENING_GATE -> R.drawable.img_arcade_screening_gate
        CtGlobalAssetKey.METAL_DEFENSE_ARENA -> R.drawable.img_arcade_magnet_arena
        CtGlobalAssetKey.GANTRY_RHYTHM_STAGE -> R.drawable.img_arcade_rhythm_stage
        CtGlobalAssetKey.CT_GANTRY -> R.drawable.mri_icon_bersih
        else -> null
    }
}

/**
 * Komponen aset gambar global CT-Learn.
 * Memadukan aset gambar bawaan (img_*) dan ilustrasi vektor datar dark-outline.
 */
@Composable
fun CtGlobalAssetImage(
    assetKey: CtGlobalAssetKey,
    modifier: Modifier = Modifier,
    tintAccent: Color = CyanBright,
    showBadgeFrame: Boolean = false,
    contentDescription: String? = assetKey.contentDescriptionId
) {
    val drawableRes = CtGlobalAssetCatalog.drawableResOrNull(assetKey)
    val frameModifier = if (showBadgeFrame) {
        modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF071322))
            .border(1.3.dp, tintAccent.copy(alpha = 0.55f), RoundedCornerShape(14.dp))
    } else {
        modifier
    }

    Box(
        modifier = frameModifier,
        contentAlignment = Alignment.Center
    ) {
        if (drawableRes != null) {
            Image(
                painter = painterResource(id = drawableRes),
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            val pad = if (showBadgeFrame) 6.dp else 1.dp
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pad)
            ) {
                val w = size.width
                val h = size.height
                val outline = Color(0xFF071322)
                when (assetKey) {
                    CtGlobalAssetKey.TROPHY_MEDAL -> {
                        drawCircle(AmberWarning.copy(alpha = 0.22f), radius = w * 0.44f, center = Offset(w * 0.5f, h * 0.48f))
                        val cup = Path().apply {
                            moveTo(w * 0.28f, h * 0.20f)
                            lineTo(w * 0.72f, h * 0.20f)
                            lineTo(w * 0.65f, h * 0.56f)
                            lineTo(w * 0.50f, h * 0.66f)
                            lineTo(w * 0.35f, h * 0.56f)
                            close()
                        }
                        drawPath(cup, color = AmberWarning)
                        drawPath(cup, color = outline, style = Stroke(width = w * 0.06f, join = StrokeJoin.Round))
                        drawLine(AmberWarning, Offset(w * 0.50f, h * 0.65f), Offset(w * 0.50f, h * 0.80f), w * 0.11f, StrokeCap.Round)
                        drawRoundRect(AmberWarning, Offset(w * 0.30f, h * 0.78f), Size(w * 0.40f, h * 0.11f), CornerRadius(w * 0.04f, w * 0.04f))
                    }
                    CtGlobalAssetKey.SHIELD_ALARA -> {
                        val shield = Path().apply {
                            moveTo(w * 0.50f, h * 0.12f)
                            lineTo(w * 0.84f, h * 0.26f)
                            lineTo(w * 0.80f, h * 0.60f)
                            lineTo(w * 0.50f, h * 0.88f)
                            lineTo(w * 0.20f, h * 0.60f)
                            lineTo(w * 0.16f, h * 0.26f)
                            close()
                        }
                        drawPath(shield, brush = Brush.verticalGradient(listOf(tintAccent, tintAccent.copy(alpha = 0.6f))))
                        drawPath(shield, color = outline, style = Stroke(width = w * 0.06f, join = StrokeJoin.Round))
                        drawCircle(Color.White.copy(alpha = 0.85f), radius = w * 0.10f, center = Offset(w * 0.5f, h * 0.48f))
                    }
                    CtGlobalAssetKey.SPARK_BOLT -> {
                        val bolt = Path().apply {
                            moveTo(w * 0.58f, h * 0.10f)
                            lineTo(w * 0.24f, h * 0.54f)
                            lineTo(w * 0.48f, h * 0.54f)
                            lineTo(w * 0.40f, h * 0.90f)
                            lineTo(w * 0.76f, h * 0.44f)
                            lineTo(w * 0.52f, h * 0.44f)
                            close()
                        }
                        drawPath(bolt, color = tintAccent)
                        drawPath(bolt, color = outline, style = Stroke(width = w * 0.055f, join = StrokeJoin.Round))
                    }
                    CtGlobalAssetKey.GAME_CONTROLLER -> {
                        drawRoundRect(
                            color = CyanPrimary,
                            topLeft = Offset(w * 0.12f, h * 0.28f),
                            size = Size(w * 0.76f, h * 0.44f),
                            cornerRadius = CornerRadius(w * 0.22f, w * 0.22f)
                        )
                        drawRoundRect(
                            color = outline,
                            topLeft = Offset(w * 0.12f, h * 0.28f),
                            size = Size(w * 0.76f, h * 0.44f),
                            cornerRadius = CornerRadius(w * 0.22f, w * 0.22f),
                            style = Stroke(width = w * 0.055f)
                        )
                        drawLine(Color.White, Offset(w * 0.28f, h * 0.50f), Offset(w * 0.42f, h * 0.50f), w * 0.07f, StrokeCap.Round)
                        drawLine(Color.White, Offset(w * 0.35f, h * 0.43f), Offset(w * 0.35f, h * 0.57f), w * 0.07f, StrokeCap.Round)
                        drawCircle(AmberWarning, radius = w * 0.06f, center = Offset(w * 0.64f, h * 0.45f))
                        drawCircle(EmeraldSuccess, radius = w * 0.06f, center = Offset(w * 0.72f, h * 0.55f))
                    }
                    CtGlobalAssetKey.PULSE_WAVE -> {
                        val path = Path()
                        for (i in 0..24) {
                            val t = i / 24f
                            val x = w * 0.10f + t * w * 0.80f
                            val env = 1f - kotlin.math.abs(t - 0.5f) * 1.4f
                            val y = h * 0.50f - sin(t * 4f * PI).toFloat() * h * 0.30f * env.coerceAtLeast(0.2f)
                            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                        }
                        drawPath(path, color = CharActionOrange, style = Stroke(width = w * 0.09f, cap = StrokeCap.Round))
                        drawCircle(CyanBright, radius = w * 0.08f, center = Offset(w * 0.50f, h * 0.24f))
                    }
                    CtGlobalAssetKey.HU_SLICE -> {
                        drawRoundRect(Color(0xFF133352), Offset(w * 0.12f, h * 0.12f), Size(w * 0.76f, h * 0.76f), CornerRadius(w * 0.14f, w * 0.14f))
                        drawOval(CharAccentMint, Offset(w * 0.24f, h * 0.18f), Size(w * 0.52f, h * 0.64f), style = Stroke(width = w * 0.07f))
                        drawLine(CyanBright, Offset(w * 0.50f, h * 0.20f), Offset(w * 0.50f, h * 0.80f), w * 0.05f)
                        drawOval(CyanBright.copy(alpha = 0.75f), Offset(w * 0.34f, h * 0.38f), Size(w * 0.12f, h * 0.22f))
                        drawOval(CyanBright.copy(alpha = 0.75f), Offset(w * 0.54f, h * 0.38f), Size(w * 0.12f, h * 0.22f))
                    }
                    CtGlobalAssetKey.FIRE_STREAK -> {
                        val flame = Path().apply {
                            moveTo(w * 0.50f, h * 0.10f)
                            cubicTo(w * 0.78f, h * 0.32f, w * 0.84f, h * 0.62f, w * 0.68f, h * 0.82f)
                            cubicTo(w * 0.58f, h * 0.92f, w * 0.42f, h * 0.92f, w * 0.32f, h * 0.82f)
                            cubicTo(w * 0.16f, h * 0.62f, w * 0.24f, h * 0.34f, w * 0.50f, h * 0.10f)
                            close()
                        }
                        drawPath(flame, brush = Brush.verticalGradient(listOf(AmberWarning, CharActionOrange)))
                        drawPath(flame, color = outline, style = Stroke(width = w * 0.06f, join = StrokeJoin.Round))
                        drawCircle(Color(0xFFFFF3B0), radius = w * 0.14f, center = Offset(w * 0.50f, h * 0.66f))
                    }
                    CtGlobalAssetKey.CLINICAL_ID_BADGE -> {
                        drawRoundRect(
                            color = Color(0xFF102A43),
                            topLeft = Offset(w * 0.12f, h * 0.18f),
                            size = Size(w * 0.76f, h * 0.64f),
                            cornerRadius = CornerRadius(w * 0.12f, w * 0.12f)
                        )
                        drawRoundRect(
                            color = tintAccent,
                            topLeft = Offset(w * 0.12f, h * 0.18f),
                            size = Size(w * 0.76f, h * 0.64f),
                            cornerRadius = CornerRadius(w * 0.12f, w * 0.12f),
                            style = Stroke(width = w * 0.06f)
                        )
                        drawCircle(CharAccentMint, radius = w * 0.12f, center = Offset(w * 0.34f, h * 0.50f))
                        drawLine(CyanBright, Offset(w * 0.52f, h * 0.40f), Offset(w * 0.76f, h * 0.40f), w * 0.07f, StrokeCap.Round)
                        drawLine(Color.White.copy(alpha = 0.75f), Offset(w * 0.52f, h * 0.58f), Offset(w * 0.72f, h * 0.58f), w * 0.06f, StrokeCap.Round)
                    }
                    CtGlobalAssetKey.STAR_BADGE -> {
                        val star = Path().apply {
                            val cx = w * 0.50f
                            val cy = h * 0.50f
                            val outerR = w * 0.40f
                            val innerR = w * 0.18f
                            for (i in 0 until 10) {
                                val angle = (-PI / 2.0) + i * (PI / 5.0)
                                val r = if (i % 2 == 0) outerR else innerR
                                val px = cx + (cos(angle) * r).toFloat()
                                val py = cy + (sin(angle) * r).toFloat()
                                if (i == 0) moveTo(px, py) else lineTo(px, py)
                            }
                            close()
                        }
                        drawPath(star, color = AmberWarning)
                        drawPath(star, color = outline, style = Stroke(width = w * 0.055f, join = StrokeJoin.Round))
                    }
                    CtGlobalAssetKey.CHECK_VERIFIED -> {
                        drawCircle(EmeraldSuccess.copy(alpha = 0.22f), radius = w * 0.44f, center = Offset(w * 0.50f, h * 0.50f))
                        drawCircle(EmeraldSuccess, radius = w * 0.36f, center = Offset(w * 0.50f, h * 0.50f))
                        drawCircle(outline, radius = w * 0.36f, center = Offset(w * 0.50f, h * 0.50f), style = Stroke(width = w * 0.055f))
                        val check = Path().apply {
                            moveTo(w * 0.33f, h * 0.52f)
                            lineTo(w * 0.45f, h * 0.64f)
                            lineTo(w * 0.68f, h * 0.38f)
                        }
                        drawPath(check, color = Color(0xFF052E16), style = Stroke(width = w * 0.09f, cap = StrokeCap.Round, join = StrokeJoin.Round))
                    }
                    CtGlobalAssetKey.RADIOGRAPHER_CREST -> {
                        drawCircle(tintAccent.copy(alpha = 0.20f), radius = w * 0.44f, center = Offset(w * 0.50f, h * 0.50f))
                        drawCircle(tintAccent, radius = w * 0.34f, center = Offset(w * 0.50f, h * 0.50f), style = Stroke(width = w * 0.07f))
                        drawCircle(CharAccentMint, radius = w * 0.18f, center = Offset(w * 0.50f, h * 0.50f), style = Stroke(width = w * 0.06f))
                        drawCircle(AmberWarning, radius = w * 0.07f, center = Offset(w * 0.50f, h * 0.50f))
                    }
                    else -> {
                        drawCtObjectVector(assetKey = assetKey, accent = tintAccent)
                    }
                }
            }
        }
    }
}

@Composable
fun CtGlobalFallbackImage(
    modifier: Modifier = Modifier,
    contentDescription: String = "Ilustrasi CT-Learn Global"
) {
    CtGlobalAssetImage(
        assetKey = CtGlobalAssetKey.FALLBACK_CT,
        modifier = modifier,
        contentDescription = contentDescription
    )
}
