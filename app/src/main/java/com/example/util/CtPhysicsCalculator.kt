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
