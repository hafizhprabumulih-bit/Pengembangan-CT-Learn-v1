package com.example.util

import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Kalkulator fisika medis & dosimetri CT Scan murni (JVM-testable).
 * Menjadi sumber kebenaran tunggal untuk [com.example.viewmodel.CtUiState] dan Simulator CT.
 */
object CtPhysicsCalculator {

    const val MU_WATER_120_KVP = 0.200f

    fun effectiveMas(tubeMa: Float, rotationTimeSec: Float, helicalPitch: Float): Float {
        val mAsPerRot = tubeMa.coerceAtLeast(10f) * rotationTimeSec.coerceAtLeast(0.1f)
        return mAsPerRot / helicalPitch.coerceAtLeast(0.2f)
    }

    fun pixelSizeMm(displayFovMm: Float, matrixSize: Int): Float =
        displayFovMm.coerceAtLeast(50f) / matrixSize.coerceAtLeast(128)

    fun ctdiVolMgy(tubeKvp: Int, effectiveMas: Float, isHeadPhantom: Boolean): Float {
        val kvpRatio = tubeKvp.coerceIn(70, 140) / 120f
        val kvpFactor = kvpRatio.pow(2.2f)
        val basePer100Mas = if (isHeadPhantom) 20.0f else 8.5f
        return basePer100Mas * kvpFactor * (effectiveMas.coerceAtLeast(1f) / 100f)
    }

    fun ssdeConversionFactor(isHeadPhantom: Boolean, patientDiameterCm: Float = 26f): Float {
        if (isHeadPhantom) return 1.0f
        // Estimasi faktor konversi SSDE AAPM TG-204 untuk fantom 32 cm
        return (2.15f * exp(-0.019f * patientDiameterCm.coerceIn(10f, 45f))).coerceIn(0.85f, 2.2f)
    }

    fun dlpMgyCm(ctdiVolMgy: Float, scanLengthCm: Float): Float =
        ctdiVolMgy.coerceAtLeast(0f) * scanLengthCm.coerceAtLeast(1f)

    fun effectiveDoseMsv(dlpMgyCm: Float, isHeadPhantom: Boolean): Float {
        val kFactor = if (isHeadPhantom) 0.0021f else 0.015f
        return dlpMgyCm.coerceAtLeast(0f) * kFactor
    }

    fun relativeNoiseSdHu(
        tubeKvp: Int,
        effectiveMas: Float,
        isIterativeReconActive: Boolean = false
    ): Float {
        val kvpRatio = tubeKvp.coerceIn(70, 140) / 120f
        val kvpFactor = kvpRatio.pow(2.2f)
        val baseNoise = 12.0f / sqrt(((effectiveMas.coerceAtLeast(1f) / 100f) * kvpFactor).coerceAtLeast(0.12f))
        val reconFactor = if (isIterativeReconActive) 0.65f else 1.0f
        return (baseNoise * reconFactor).coerceIn(1.5f, 45f)
    }

    fun beerLambertTransmission(muPerCm: Float, thicknessCm: Float): Float =
        exp(-muPerCm.coerceAtLeast(0f) * thicknessCm.coerceAtLeast(0f))

    fun halfValueLayerCm(muPerCm: Float): Float =
        0.693147f / muPerCm.coerceAtLeast(0.001f)

    fun sinogramRaySum(transmittedRatio: Float): Float =
        ln(1f / transmittedRatio.coerceIn(1e-6f, 1f))

    fun calculateHu(muPerCm: Float, muWater: Float = MU_WATER_120_KVP): Int {
        val water = muWater.coerceAtLeast(0.01f)
        return kotlin.math.round(1000f * (muPerCm - water) / water).toInt()
    }

    /**
     * Menghitung perubahan koefisien atenuasi linier terhadap tegangan tabung kVp.
     * Jaringan dengan nomor atom efektif tinggi (Tulang/Ca, Kontras Iodin Z=53)
     * menunjukkan lonjakan serapan fotolistrik (Z^3/E^3) saat kVp diturunkan ke 80-100 kVp,
     * sedangkan air murni (effectiveZRatio = 1.0f) dan udara tetap terkalibrasi konstan.
     */
    fun muAtKvp(
        mu120: Float,
        effectiveZRatio: Float = 1.0f,
        tubeKvp: Int = 120
    ): Float {
        if (mu120 <= 0.001f) return 0f
        if (kotlin.math.abs(mu120 - MU_WATER_120_KVP) < 0.0005f || effectiveZRatio <= 1.02f) {
            return mu120
        }
        val energyFactor = (120f / tubeKvp.coerceIn(70, 140).toFloat()).pow(1.15f)
        val deltaFromWater = mu120 - MU_WATER_120_KVP
        val scaledDelta = if (deltaFromWater > 0f) {
            deltaFromWater * (1f + (effectiveZRatio - 1f) * (energyFactor - 1f) * 0.45f).coerceIn(0.65f, 2.4f)
        } else {
            deltaFromWater
        }
        return (MU_WATER_120_KVP + scaledDelta).coerceAtLeast(0f)
    }

    fun calculateHuAtKvp(
        mu120: Float,
        effectiveZRatio: Float = 1.0f,
        tubeKvp: Int = 120
    ): Int = calculateHu(muAtKvp(mu120, effectiveZRatio, tubeKvp), MU_WATER_120_KVP)

    /**
     * Memetakan koordinat ter-normalisasi (nx, ny in 0..1) pada fantom multi-potongan Simulator 2
     * menjadi nilai HU anatomis yang responsif terhadap tegangan tabung kVp.
     */
    fun pixelHuAtNormalized(
        sliceIndex: Int,
        nx: Float,
        ny: Float,
        tubeKvp: Int = 120
    ): Float {
        val dx = nx - 0.5f
        val dy = ny - 0.5f
        val dist = kotlin.math.hypot(dx, dy)
        val boneScale = (120f / tubeKvp.coerceIn(70, 140)).pow(0.45f)
        val iodineScale = (120f / tubeKvp.coerceIn(70, 140)).pow(0.85f)
        return when (sliceIndex) {
            0 -> when {
                dist > 0.44f -> -1000f
                dist > 0.36f -> 1400f * boneScale // Tulang Kranium
                kotlin.math.hypot(nx - 0.34f, ny - 0.50f) <= 0.10f -> 75f // Hematoma Akut
                kotlin.math.hypot(nx - 0.66f, ny - 0.50f) <= 0.09f -> 18f // Infark Subakut
                kotlin.math.hypot(nx - 0.44f, ny - 0.46f) <= 0.05f ||
                    kotlin.math.hypot(nx - 0.56f, ny - 0.46f) <= 0.05f -> 10f // CSF Ventrikel
                dist > 0.30f -> 40f // Korteks Grisea
                else -> 28f // Substansia Alba
            }
            1 -> when {
                dist > 0.44f -> -1000f
                kotlin.math.hypot(nx - 0.50f, ny - 0.72f) <= 0.08f -> 1200f * boneScale // Vertebra
                kotlin.math.hypot(nx - 0.53f, ny - 0.48f) <= 0.06f -> 320f * iodineScale // Aorta Kontras
                kotlin.math.hypot(nx - 0.34f, ny - 0.45f) <= 0.04f -> 35f // Nodul Paru
                kotlin.math.hypot(nx - 0.50f, ny - 0.44f) <= 0.08f -> 45f // Mediastinum
                dist < 0.35f -> -750f // Parenkim Paru
                else -> 40f // Dinding Toraks
            }
            else -> when {
                dist > 0.44f -> -1000f
                kotlin.math.hypot(nx - 0.50f, ny - 0.72f) <= 0.09f -> 1100f * boneScale // Vertebra
                kotlin.math.hypot(nx - 0.53f, ny - 0.55f) <= 0.06f -> 260f * iodineScale // Aorta Abdominalis
                kotlin.math.hypot(nx - 0.30f, ny - 0.44f) <= 0.07f -> 8f // Kista Hepar
                kotlin.math.hypot(nx - 0.68f, ny - 0.39f) <= 0.11f -> -980f // Gas Lambung
                nx < 0.48f && dist < 0.34f -> 58f // Parenkim Hepar
                dist > 0.37f -> -90f // Lemak Subkutan
                else -> 45f // Limpa / Organ Lunak
            }
        }
    }

    fun bolusPeakArterialHu(tubeKvp: Int, flowRateMlSec: Float, useSalineChaser: Boolean): Float =
        160f + flowRateMlSec.coerceIn(1f, 8f) * 48f +
            (if (useSalineChaser) 35f else 0f) +
            (if (tubeKvp <= 100) 45f else 0f)

    fun bolusTimeToPeakSec(flowRateMlSec: Float): Float =
        (24f - flowRateMlSec.coerceIn(1f, 8f) * 1.6f).coerceIn(12f, 22f)

    fun bolusTriggerTimeSec(timeToPeakSec: Float, peakArterialHu: Float, triggerHu: Float): Float {
        val denom = peakArterialHu.coerceAtLeast(triggerHu + 10f)
        return (timeToPeakSec * (triggerHu.coerceAtLeast(50f) / denom) + 5f).coerceIn(8f, 24f)
    }
}
