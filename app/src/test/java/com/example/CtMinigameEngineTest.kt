package com.example

import com.example.ui.components.CtGlobalAssetCatalog
import com.example.ui.components.CtGlobalAssetKey
import com.example.util.CtMinigameEngine
import com.example.util.TapAccuracy
import kotlin.math.abs
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CtMinigameEngineTest {

    @Test
    fun `screening bank has balanced safe and unsafe items with asset keys`() {
        val safeCount = CtMinigameEngine.screeningBank.count { it.allowIntoGantry }
        val unsafeCount = CtMinigameEngine.screeningBank.count { !it.allowIntoGantry }
        assertTrue(safeCount >= 6)
        assertTrue(unsafeCount >= 6)
        assertTrue(CtMinigameEngine.screeningBank.all { it.reason.isNotBlank() && it.assetKey.isNotBlank() })
        CtMinigameEngine.screeningBank.forEach { item ->
            val resolved = CtGlobalAssetCatalog.resolveKey(item.assetKey)
            assertNotEquals("Item ${item.id} should resolve dedicated asset", CtGlobalAssetKey.FALLBACK_CT, resolved)
        }
    }

    @Test
    fun `bolus trigger tap evaluates perfect good and miss thresholds`() {
        assertEquals(TapAccuracy.PERFECT, CtMinigameEngine.evaluateBolusTriggerTap(0.55f, 0.55f))
        assertEquals(TapAccuracy.PERFECT, CtMinigameEngine.evaluateBolusTriggerTap(0.60f, 0.55f))
        assertEquals(TapAccuracy.GOOD, CtMinigameEngine.evaluateBolusTriggerTap(0.66f, 0.55f))
        assertEquals(TapAccuracy.MISS, CtMinigameEngine.evaluateBolusTriggerTap(0.80f, 0.55f))
    }

    @Test
    fun `bolus phase and needle remain bounded and continuous when combo rises`() {
        var phase = 0.65f
        val needleBefore = CtMinigameEngine.needleFromPhase(phase)
        phase = CtMinigameEngine.advanceBolusPhase(phase, deltaSec = 0.016f, speedMultiplier = 1.2f, combo = 5)
        val needleAfter = CtMinigameEngine.needleFromPhase(phase)

        assertTrue(needleBefore in 0.06f..0.94f)
        assertTrue(needleAfter in 0.06f..0.94f)
        assertTrue("Needle should move smoothly without sudden jump on combo increase", abs(needleAfter - needleBefore) < 0.08f)
    }

    @Test
    fun `buildNonRepeatingDeck avoids repeating previousLast at index zero`() {
        val base = listOf("A", "B", "C", "D", "E")
        repeat(10) { seed ->
            val deck = CtMinigameEngine.buildNonRepeatingDeck(base, previousLast = "C", random = Random(seed))
            assertEquals(base.size, deck.size)
            assertNotEquals("C", deck.first())
        }
    }

    @Test
    fun `window artifact bank has valid correctIndex bounds`() {
        assertTrue(CtMinigameEngine.windowArtifactBank.size >= 12)
        CtMinigameEngine.windowArtifactBank.forEach { q ->
            assertTrue(q.correctIndex in q.options.indices)
            assertTrue(q.explanation.isNotBlank())
        }
    }

    @Test
    fun `combo points scale and cap cleanly`() {
        assertEquals(0, CtMinigameEngine.computePoints(0, 5))
        assertEquals(140, CtMinigameEngine.computePoints(100, 2))
        assertEquals(220, CtMinigameEngine.computePoints(100, 10))
    }
}
