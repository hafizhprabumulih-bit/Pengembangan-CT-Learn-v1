package com.example

import com.example.util.CtArcadeEngine
import com.example.util.CtScanStageKind
import com.example.util.DefenseArtifactCategory
import com.example.util.DefenseProjectile
import com.example.util.GantryBeatNote
import com.example.util.GantryRhythmState
import com.example.util.MetalDefenseState
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CtArcadeEngineTest {

    @Test
    fun `deflecting metal artifact projectile increases score and combo`() {
        val state = MetalDefenseState(
            items = listOf(
                DefenseProjectile(
                    id = 1,
                    label = "Gunting Baja",
                    codeTag = "GUNTING",
                    category = DefenseArtifactCategory.CT_METAL_ARTIFACT,
                    x = 0.2f,
                    y = 0.2f,
                    vx = 0.1f,
                    vy = 0.1f,
                    assetKey = "GUNTING_BAJA"
                )
            )
        )
        val next = CtArcadeEngine.tapDefenseProjectile(state, 1)
        assertTrue(next.items.isEmpty())
        assertEquals(1, next.combo)
        assertEquals(1, next.deflectedCount)
        assertTrue(next.score >= 120)
    }

    @Test
    fun `metal artifact projectile reaching gantry sfov reduces lives`() {
        val state = MetalDefenseState(
            items = listOf(
                DefenseProjectile(
                    id = 1,
                    label = "Tabung O₂",
                    codeTag = "O2",
                    category = DefenseArtifactCategory.CT_METAL_ARTIFACT,
                    x = 0.5f,
                    y = 0.5f,
                    vx = 0f,
                    vy = 0f,
                    assetKey = "TABUNG_O2"
                )
            ),
            lives = 3
        )
        val next = CtArcadeEngine.stepMetalDefense(state, 0.016f)
        assertEquals(2, next.lives)
        assertEquals(0, next.combo)
    }

    @Test
    fun `tapDefenseAtNormalized prioritizes metal artifact when safe item is nearby`() {
        val safeNear = DefenseProjectile(
            id = 1,
            label = "Bantal Busa",
            codeTag = "BANTAL",
            category = DefenseArtifactCategory.CT_RADIOLUCENT_SAFE,
            x = 0.22f,
            y = 0.22f,
            vx = 0f,
            vy = 0f,
            assetKey = "BANTAL_BUSA"
        )
        val unsafeClose = DefenseProjectile(
            id = 2,
            label = "Kunci Pas",
            codeTag = "KUNCI",
            category = DefenseArtifactCategory.CT_METAL_ARTIFACT,
            x = 0.25f,
            y = 0.24f,
            vx = 0f,
            vy = 0f,
            assetKey = "KUNCI_PAS"
        )
        val state = MetalDefenseState(items = listOf(safeNear, unsafeClose), score = 100)
        val next = CtArcadeEngine.tapDefenseAtNormalized(state, 0.23f, 0.23f)
        assertEquals(1, next.items.size)
        assertEquals(1, next.items.first().id)
        assertEquals(1, next.deflectedCount)
        assertTrue(next.lastEventGood)
    }

    @Test
    fun `pickOptimalSpawnAngle separates spawn from existing active projectile`() {
        val existing = listOf(
            DefenseProjectile(
                id = 1,
                label = "Klip Logam",
                codeTag = "KLIP",
                category = DefenseArtifactCategory.CT_METAL_ARTIFACT,
                x = 0.94f,
                y = 0.50f,
                vx = -0.1f,
                vy = 0f,
                assetKey = "KLIP_LOGAM"
            )
        )
        val existingAngle = atan2(0.50f - CtArcadeEngine.GANTRY_ISOCENTER_Y, 0.94f - CtArcadeEngine.GANTRY_ISOCENTER_X)
        val picked = CtArcadeEngine.pickOptimalSpawnAngle(existing, Random(42))
        val rawDiff = abs(picked - existingAngle) % (2f * PI.toFloat())
        val angularGap = minOf(rawDiff, 2f * PI.toFloat() - rawDiff)
        assertTrue("Expected well-separated spawn angle, got gap=$angularGap", angularGap >= PI.toFloat() * 0.5f)
    }

    @Test
    fun `gantry sync rhythm awards perfect hit near target`() {
        val state = GantryRhythmState(
            notes = listOf(GantryBeatNote(id = 7, kind = CtScanStageKind.TUBE_EXPOSURE_KVP, progress = 0.99f))
        )
        val next = CtArcadeEngine.tapGantryRhythmLane(state, CtScanStageKind.TUBE_EXPOSURE_KVP)
        assertTrue(next.notes.isEmpty())
        assertEquals(1, next.combo)
        assertTrue(next.score >= 135)
        assertTrue(next.lastJudgment.startsWith("PERFECT"))
    }

    @Test
    fun `tapping empty rhythm lane does not remove note from another lane`() {
        val note = GantryBeatNote(id = 9, kind = CtScanStageKind.RECON_FBP, progress = 0.96f)
        val state = GantryRhythmState(notes = listOf(note), combo = 3)
        val next = CtArcadeEngine.tapGantryRhythmLane(state, CtScanStageKind.TUBE_EXPOSURE_KVP)
        assertEquals(1, next.notes.size)
        assertEquals(9, next.notes.first().id)
        assertEquals(0, next.combo)
        assertTrue(next.lastJudgment.contains("Salah lajur") || next.lastJudgment.contains("kosong"))
    }

    @Test
    fun `resolveAssetKey returns valid key without CJK fallback characters`() {
        val proj = DefenseProjectile(
            id = 5,
            label = "Pinset Logam",
            codeTag = "PINSET",
            category = DefenseArtifactCategory.CT_METAL_ARTIFACT,
            x = 0.2f,
            y = 0.8f,
            vx = 0f,
            vy = 0f
        )
        val key = CtArcadeEngine.resolveAssetKey(proj)
        assertNotNull(key)
        assertEquals("PINSET", key)
    }

    @Test
    fun `activateMarPowerUp clears up to 2 metal projectiles and starts cooldown`() {
        val items = listOf(
            DefenseProjectile(1, "Klip", "KLIP", DefenseArtifactCategory.CT_METAL_ARTIFACT, 0.45f, 0.50f, 0f, 0f),
            DefenseProjectile(2, "Gunting", "GUNTING", DefenseArtifactCategory.CT_METAL_ARTIFACT, 0.30f, 0.50f, 0f, 0f),
            DefenseProjectile(3, "Ponsel", "PONSEL", DefenseArtifactCategory.CT_METAL_ARTIFACT, 0.15f, 0.50f, 0f, 0f),
            DefenseProjectile(4, "Bantal", "BANTAL", DefenseArtifactCategory.CT_RADIOLUCENT_SAFE, 0.48f, 0.50f, 0f, 0f)
        )
        val state = MetalDefenseState(items = items)
        val after = CtArcadeEngine.activateMarPowerUp(state)
        assertEquals(2, after.items.size)
        assertTrue(after.items.any { it.id == 3 })
        assertTrue(after.items.any { it.id == 4 })
        assertEquals(180, after.score)
        assertTrue(after.marCooldownSec > 0f)
    }
}