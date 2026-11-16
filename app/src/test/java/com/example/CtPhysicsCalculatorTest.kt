package com.example

import com.example.util.CtPhysicsCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CtPhysicsCalculatorTest {

    @Test
    fun `effective mAs scales with mA rotation time and inverse pitch`() {
        assertEquals(125f, CtPhysicsCalculator.effectiveMas(250f, 0.5f, 1.0f), 0.01f)
        assertEquals(100f, CtPhysicsCalculator.effectiveMas(250f, 0.5f, 1.25f), 0.01f)
    }

    @Test
    fun `beer lambert transmission matches half value layer attenuation`() {
        val mu = 0.200f
        val hvl = CtPhysicsCalculator.halfValueLayerCm(mu)
        val transmissionAtHvl = CtPhysicsCalculator.beerLambertTransmission(mu, hvl)
        assertEquals(0.5f, transmissionAtHvl, 0.005f)
        assertEquals(0, CtPhysicsCalculator.calculateHu(mu))
        assertEquals(-1000, CtPhysicsCalculator.calculateHu(0f))
    }

    @Test
    fun `dosimetry and iterative recon noise respond predictably`() {
        val ctdi = CtPhysicsCalculator.ctdiVolMgy(120, 100f, isHeadPhantom = false)
        assertEquals(8.5f, ctdi, 0.05f)

        val dlp = CtPhysicsCalculator.dlpMgyCm(ctdi, 30f)
        assertEquals(255f, dlp, 0.5f)
        assertEquals(3.825f, CtPhysicsCalculator.effectiveDoseMsv(dlp, isHeadPhantom = false), 0.02f)

        val fbpNoise = CtPhysicsCalculator.relativeNoiseSdHu(120, 100f, isIterativeReconActive = false)
        val irNoise = CtPhysicsCalculator.relativeNoiseSdHu(120, 100f, isIterativeReconActive = true)
        assertTrue(irNoise < fbpNoise)

        val pedSsde = CtPhysicsCalculator.ssdeConversionFactor(isHeadPhantom = false, patientDiameterCm = 18f)
        assertTrue("Small 18cm patient must have SSDE factor > 1.3", pedSsde > 1.3f)
    }
}
