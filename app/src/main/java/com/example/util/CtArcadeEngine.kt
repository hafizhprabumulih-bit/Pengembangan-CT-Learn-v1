package com.example.util

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.random.Random

enum class DefenseArtifactCategory { CT_METAL_ARTIFACT, CT_RADIOLUCENT_SAFE }

data class DefenseProjectile(
    val id: Int,
    val label: String,
    val codeTag: String,
    val category: DefenseArtifactCategory,
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val assetKey: String = ""
)

data class MetalDefenseState(
    val items: List<DefenseProjectile> = emptyList(),
    val score: Int = 0,
    val combo: Int = 0,
    val lives: Int = 3,
    val wave: Int = 1,
    val elapsedSec: Float = 0f,
    val spawnCooldownSec: Float = 0.25f,
    val nextId: Int = 1,
    val deflectedCount: Int = 0,
    val lastEventMessage: String = "Ketuk benda logam pemicu Metal Streak Artifact (merah) sebelum masuk ke FOV Gantry CT!",
    val lastEventGood: Boolean = true,
    val isGameOver: Boolean = false
) {
    val kvpLabel: String
        get() = when {
            wave >= 3 -> "140 kVp"
            wave == 2 -> "120 kVp"
            else -> "80 kVp"
        }

    val rankTitle: String
        get() = CtArcadeEngine.rankForMetalDefenseScore(score)
}

enum class CtScanStageKind(val label: String) {
    TUBE_EXPOSURE_KVP("Eksposi kVp"),
    DAS_SAMPLING("Sampling DAS"),
    RECON_FBP("Rekon FBP")
}

data class GantryBeatNote(
    val id: Int,
    val kind: CtScanStageKind,
    val progress: Float // 0f = atas, 1f = garis target ketuk
)

data class GantryRhythmState(
    val notes: List<GantryBeatNote> = emptyList(),
    val score: Int = 0,
    val combo: Int = 0,
    val lives: Int = 3,
    val elapsedSec: Float = 0f,
    val spawnTimerSec: Float = 0.25f,
    val nextId: Int = 1,
    val stepIndex: Int = 0,
    val signalAmplitude: Float = 0.5f,
    val lastJudgment: String = "Ketuk tombol sinkronisasi gantry saat indikator tiba di garis target!",
    val isGameOver: Boolean = false
) {
    val isTurboMode: Boolean get() = combo >= 6
    val rankTitle: String get() = CtArcadeEngine.rankForGantryRhythmScore(score)
}

/**
 * Mesin simulasi murni untuk dua game arcade CT-Learn:
 * 1) Metal Streak Defense (spawning anti-tumpang-tindih & prioritas target CT_METAL_ARTIFACT)
 * 2) Gantry Sync Rhythm (spasi catatan adil per lajur akuisisi kVp - DAS - Rekon FBP)
 */
object CtArcadeEngine {

    const val GANTRY_ISOCENTER_X = 0.5f
    const val GANTRY_ISOCENTER_Y = 0.5f
    const val SFOV_TARGET_RADIUS = 0.16f

    private data class DefenseTemplate(
        val label: String,
        val codeTag: String,
        val assetKey: String
    )

    private val unsafeTemplates = listOf(
        DefenseTemplate("Gunting Baja", "GUNTING", "GUNTING_BAJA"),
        DefenseTemplate("Tabung O₂", "O2", "TABUNG_O2"),
        DefenseTemplate("Kunci Pas", "KUNCI", "KUNCI_PAS"),
        DefenseTemplate("Klip Logam", "KLIP", "KLIP_LOGAM"),
        DefenseTemplate("Besi Asing Bore", "BORE", "KURSI_RODA_BESI"),
        DefenseTemplate("Lead Kabel Logam", "LEAD", "STETOSKOP"),
        DefenseTemplate("Jam Mekanik", "JAM", "JAM_MEKANIK"),
        DefenseTemplate("Pinset Logam", "PINSET", "PINSET"),
        DefenseTemplate("Ponsel Logam", "PONSEL", "PONSEL_LOGAM"),
        DefenseTemplate("Tabung Berat", "TABUNG", "TABUNG_PEMADAM"),
        DefenseTemplate("Kacamata Logam", "KACAMATA", "KACA_MATA_LOGAM"),
        DefenseTemplate("Pompa Tanpa Jalur", "POMPA", "POMPA_INFUS_BIASA"),
        DefenseTemplate("Botol Baja", "BOTOL", "BOTOL_MINUM_LOGAM"),
        DefenseTemplate("Skalpel Baja", "SKALPEL", "PISAU_BEDAH_BAJA"),
        DefenseTemplate("Palu Refleks", "PALU", "PALU_REFLEKS_BESI"),
        DefenseTemplate("Cincin Logam", "CINCIN", "CINCIN_FEROMAGNETIK"),
        DefenseTemplate("Lanyard Klip", "LANYARD", "KARTU_AKSES_MAGNET")
    )

    private val safeTemplates = listOf(
        DefenseTemplate("Bantal Busa", "BANTAL", "BANTAL_BUSA"),
        DefenseTemplate("Vial Kontras Iodin", "VIAL", "VIAL_KONTRAS"),
        DefenseTemplate("Gaun Katun", "GAUN", "GAUN_KATUN"),
        DefenseTemplate("Spons Fiksasi", "SPONS", "EARPLUG"),
        DefenseTemplate("Fantom Air QA", "PHANTOM", "PHANTOM_ACR"),
        DefenseTemplate("Sabuk Velcro", "VELCRO", "SANDBAG_NONMAG"),
        DefenseTemplate("Selimut Katun", "SELIMUT", "SELIMUT_KATUN"),
        DefenseTemplate("Shield Luar FOV", "SHIELD", "HEADSET_PNEUMATIK"),
        DefenseTemplate("Kanul Polimer", "MASKER", "MASKER_OKSIGEN_PLASTIK")
    )

    // Variasi pola akuisisi gantry helikal: Eksposi Tabung -> Sampling DAS -> Rekon FBP/IR
    private val rhythmSequencePatterns = listOf(
        CtScanStageKind.TUBE_EXPOSURE_KVP, CtScanStageKind.DAS_SAMPLING, CtScanStageKind.RECON_FBP,
        CtScanStageKind.TUBE_EXPOSURE_KVP, CtScanStageKind.DAS_SAMPLING, CtScanStageKind.RECON_FBP,
        CtScanStageKind.DAS_SAMPLING, CtScanStageKind.RECON_FBP,
        CtScanStageKind.DAS_SAMPLING, CtScanStageKind.TUBE_EXPOSURE_KVP, CtScanStageKind.DAS_SAMPLING, CtScanStageKind.RECON_FBP
    )

    fun resolveAssetKey(projectile: DefenseProjectile): String {
        if (projectile.assetKey.isNotBlank()) return projectile.assetKey
        val labelLower = projectile.label.lowercase()
        return when {
            "gunting" in labelLower -> "GUNTING_BAJA"
            "tabung" in labelLower || "o₂" in labelLower || "o2" in labelLower -> "TABUNG_O2"
            "kunci" in labelLower -> "KUNCI_PAS"
            "klip" in labelLower -> "KLIP_LOGAM"
            "kursi" in labelLower || "bore" in labelLower -> "KURSI_RODA_BESI"
            "stetoskop" in labelLower || "lead" in labelLower -> "STETOSKOP"
            "jam" in labelLower -> "JAM_MEKANIK"
            "pinset" in labelLower -> "PINSET"
            "bantal" in labelLower -> "BANTAL_BUSA"
            "vial" in labelLower || "kontras" in labelLower -> "VIAL_KONTRAS"
            "gaun" in labelLower -> "GAUN_KATUN"
            "spons" in labelLower || "earplug" in labelLower -> "EARPLUG"
            "fantom" in labelLower || "phantom" in labelLower -> "PHANTOM_ACR"
            "ponsel" in labelLower || "hp" in labelLower -> "PONSEL_LOGAM"
            "apar" in labelLower || "pemadam" in labelLower -> "TABUNG_PEMADAM"
            "kacamata" in labelLower -> "KACA_MATA_LOGAM"
            "pompa" in labelLower || "infus" in labelLower -> "POMPA_INFUS_BIASA"
            "botol" in labelLower || "termos" in labelLower -> "BOTOL_MINUM_LOGAM"
            "velcro" in labelLower || "sandbag" in labelLower -> "SANDBAG_NONMAG"
            "selimut" in labelLower -> "SELIMUT_KATUN"
            "saline" in labelLower || "alu" in labelLower -> "TABUNG_O2_ALU"
            "titanium" in labelLower || "implan" in labelLower -> "IMPLAN_TITANIUM"
            "shield" in labelLower || "headset" in labelLower -> "HEADSET_PNEUMATIK"
            "kanul" in labelLower || "masker" in labelLower -> "MASKER_OKSIGEN_PLASTIK"
            "skalpel" in labelLower || "pisau" in labelLower -> "PISAU_BEDAH_BAJA"
            "palu" in labelLower || "refleks" in labelLower -> "PALU_REFLEKS_BESI"
            "cincin" in labelLower -> "CINCIN_FEROMAGNETIK"
            "lanyard" in labelLower || "akses" in labelLower -> "KARTU_AKSES_MAGNET"
            else -> "FALLBACK_CT"
        }
    }

    /**
     * Memilih sudut spawn optimal yang memaksimalkan jarak sudut minimum terhadap
     * proyektil aktif sehingga item tidak bertumpukan di lintasan berkas yang sama.
     */
    fun pickOptimalSpawnAngle(existingItems: List<DefenseProjectile>, random: Random): Float {
        if (existingItems.isEmpty()) return random.nextFloat() * (2f * PI.toFloat())
        val existingAngles = existingItems.map { item ->
            atan2(item.y - GANTRY_ISOCENTER_Y, item.x - GANTRY_ISOCENTER_X)
        }
        val baseOffset = random.nextFloat() * (2f * PI.toFloat())
        var bestAngle = baseOffset
        var bestMinAngularGap = -1f
        val candidates = 10
        for (i in 0 until candidates) {
            val candidate = baseOffset + (i.toFloat() / candidates) * (2f * PI.toFloat())
            val minGap = existingAngles.minOf { angle ->
                val raw = abs(candidate - angle) % (2f * PI.toFloat())
                minOf(raw, 2f * PI.toFloat() - raw)
            }
            if (minGap > bestMinAngularGap) {
                bestMinAngularGap = minGap
                bestAngle = candidate
            }
        }
        return bestAngle
    }

    fun stepMetalDefense(
        state: MetalDefenseState,
        deltaSec: Float,
        random: Random = Random.Default
    ): MetalDefenseState {
        if (state.isGameOver || deltaSec <= 0f) return state
        val dt = deltaSec.coerceAtMost(0.05f)
        val newElapsed = state.elapsedSec + dt
        val newWave = (1 + (newElapsed / 15f).toInt()).coerceAtMost(5)
        val speedFactor = 0.135f + (newWave - 1) * 0.030f
        val maxActiveItems = (4 + newWave).coerceAtMost(7)

        var lives = state.lives
        var combo = state.combo
        var score = state.score
        var lastMsg = state.lastEventMessage
        var lastGood = state.lastEventGood

        val surviving = mutableListOf<DefenseProjectile>()
        for (item in state.items) {
            val dxCenter = GANTRY_ISOCENTER_X - item.x
            val dyCenter = GANTRY_ISOCENTER_Y - item.y
            val distNow = hypot(dxCenter, dyCenter).coerceAtLeast(0.001f)
            // Laju tarikan konveyor ke arah pusat gantry + efek rotasi pada gelombang tinggi
            val pullBoost = 1f + ((0.44f - distNow).coerceAtLeast(0f) * 0.82f)
            val swirl = if (newWave >= 3 && item.category == DefenseArtifactCategory.CT_METAL_ARTIFACT) {
                0.036f * if (item.id % 2 == 0) 1f else -1f
            } else 0f
            val tx = -dyCenter / distNow
            val ty = dxCenter / distNow

            val nx = item.x + (item.vx * pullBoost + tx * swirl) * dt
            val ny = item.y + (item.vy * pullBoost + ty * swirl) * dt
            val dist = hypot(nx - GANTRY_ISOCENTER_X, ny - GANTRY_ISOCENTER_Y)
            if (dist <= SFOV_TARGET_RADIUS) {
                if (item.category == DefenseArtifactCategory.CT_METAL_ARTIFACT) {
                    lives = (lives - 1).coerceAtLeast(0)
                    combo = 0
                    lastMsg = "Metal Streak Artifact! ${item.label} masuk ke FOV berkas sinar-X!"
                    lastGood = false
                } else {
                    score += 45
                    lastMsg = "${item.label} (Radiolusen Aman) lolos ke bore CT (+45)"
                    lastGood = true
                }
            } else {
                surviving.add(item.copy(x = nx, y = ny))
            }
        }

        var cooldown = state.spawnCooldownSec - dt
        var nextId = state.nextId
        if (cooldown <= 0f && lives > 0 && surviving.size < maxActiveItems) {
            val wantDouble = newWave >= 4 && random.nextFloat() < 0.28f && surviving.size <= maxActiveItems - 2
            val spawnCount = if (wantDouble) 2 else 1
            repeat(spawnCount) {
                val angle = pickOptimalSpawnAngle(surviving, random)
                val startX = GANTRY_ISOCENTER_X + cos(angle) * 0.44f
                val startY = GANTRY_ISOCENTER_Y + sin(angle) * 0.44f
                val dx = GANTRY_ISOCENTER_X - startX
                val dy = GANTRY_ISOCENTER_Y - startY
                val len = hypot(dx, dy).coerceAtLeast(0.001f)

                val activeUnsafe = surviving.count { it.category == DefenseArtifactCategory.CT_METAL_ARTIFACT }
                val unsafeChance = if (activeUnsafe == 0) 0.90f else 0.75f
                val isUnsafe = random.nextFloat() < unsafeChance
                val template = if (isUnsafe) {
                    unsafeTemplates[random.nextInt(unsafeTemplates.size)]
                } else {
                    safeTemplates[random.nextInt(safeTemplates.size)]
                }
                val speedVar = speedFactor * (0.92f + random.nextFloat() * 0.24f)
                surviving.add(
                    DefenseProjectile(
                        id = nextId++,
                        label = template.label,
                        codeTag = template.codeTag,
                        category = if (isUnsafe) DefenseArtifactCategory.CT_METAL_ARTIFACT else DefenseArtifactCategory.CT_RADIOLUCENT_SAFE,
                        x = startX,
                        y = startY,
                        vx = (dx / len) * speedVar,
                        vy = (dy / len) * speedVar,
                        assetKey = template.assetKey
                    )
                )
            }
            cooldown = (1.16f - (newWave - 1) * 0.14f).coerceAtLeast(0.50f)
        }

        return state.copy(
            items = surviving,
            score = score,
            combo = combo,
            lives = lives,
            wave = newWave,
            elapsedSec = newElapsed,
            spawnCooldownSec = cooldown,
            nextId = nextId,
            lastEventMessage = lastMsg,
            lastEventGood = lastGood,
            isGameOver = lives <= 0
        )
    }

    fun tapDefenseProjectile(state: MetalDefenseState, itemId: Int): MetalDefenseState {
        if (state.isGameOver) return state
        val target = state.items.firstOrNull { it.id == itemId } ?: return state
        val remaining = state.items.filterNot { it.id == itemId }

        return if (target.category == DefenseArtifactCategory.CT_METAL_ARTIFACT) {
            val newCombo = state.combo + 1
            val distFromBore = hypot(target.x - GANTRY_ISOCENTER_X, target.y - GANTRY_ISOCENTER_Y)
            val closeCallBonus = if (distFromBore <= SFOV_TARGET_RADIUS + 0.08f) 35 else 0
            val points = 100 + (newCombo * 20).coerceAtMost(140) + closeCallBonus
            val closeTag = if (closeCallBonus > 0) " [Tangkisan Kritis +35!]" else ""
            state.copy(
                items = remaining,
                score = state.score + points,
                combo = newCombo,
                deflectedCount = state.deflectedCount + 1,
                lastEventMessage = "Singkirkan ${target.label}! +$points (Combo x$newCombo)$closeTag",
                lastEventGood = true
            )
        } else {
            state.copy(
                items = remaining,
                combo = 0,
                score = (state.score - 30).coerceAtLeast(0),
                lastEventMessage = "${target.label} itu Radiolusen Aman, biarkan masuk! (-30)",
                lastEventGood = false
            )
        }
    }

    /**
     * Memprioritaskan target CT_METAL_ARTIFACT di dalam radius ketuk agar pemain tidak
     * terkena penalti tidak adil saat item aman berdekatan dengan proyektil logam bahaya.
     */
    fun tapDefenseAtNormalized(
        state: MetalDefenseState,
        normX: Float,
        normY: Float,
        hitRadius: Float = 0.14f
    ): MetalDefenseState {
        if (state.isGameOver || state.items.isEmpty()) return state
        val candidates = state.items.filter { hypot(it.x - normX, it.y - normY) <= hitRadius }
        if (candidates.isEmpty()) return state
        val preferredTarget = candidates
            .filter { it.category == DefenseArtifactCategory.CT_METAL_ARTIFACT }
            .minByOrNull { hypot(it.x - normX, it.y - normY) }
            ?: candidates.minByOrNull { hypot(it.x - normX, it.y - normY) }
            ?: return state
        return tapDefenseProjectile(state, preferredTarget.id)
    }

    fun stepGantryRhythm(state: GantryRhythmState, deltaSec: Float): GantryRhythmState {
        if (state.isGameOver || deltaSec <= 0f) return state
        val dt = deltaSec.coerceAtMost(0.05f)
        val newElapsed = state.elapsedSec + dt
        val dynamicBoost = (newElapsed / 48f).coerceAtMost(0.18f)
        val speed = (if (state.isTurboMode) 0.52f else 0.38f) + dynamicBoost

        var lives = state.lives
        var combo = state.combo
        var judgment = state.lastJudgment
        var amp = (state.signalAmplitude - dt * 0.15f).coerceAtLeast(0.2f)

        val kept = mutableListOf<GantryBeatNote>()
        for (note in state.notes) {
            val nextProg = note.progress + speed * dt
            if (nextProg > 1.22f) {
                lives = (lives - 1).coerceAtLeast(0)
                combo = 0
                amp = 0.2f
                judgment = "Terlewat ${note.kind.label}! Proyeksi Sinogram DAS kehilangan sinkronisasi"
            } else {
                kept.add(note.copy(progress = nextProg))
            }
        }

        var spawnTimer = state.spawnTimerSec - dt
        var nextId = state.nextId
        var stepIdx = state.stepIndex
        val newestProgress = kept.minOfOrNull { it.progress } ?: 1f
        if (spawnTimer <= 0f && lives > 0 && newestProgress >= 0.22f) {
            val kind = rhythmSequencePatterns[stepIdx % rhythmSequencePatterns.size]
            val sameLaneTooClose = kept.any { it.kind == kind && it.progress < 0.30f }
            if (!sameLaneTooClose) {
                kept.add(GantryBeatNote(id = nextId++, kind = kind, progress = 0f))
                stepIdx++
                val baseInterval = if (state.isTurboMode) 0.78f else 1.00f
                spawnTimer = when (kind) {
                    CtScanStageKind.DAS_SAMPLING -> baseInterval * 0.86f
                    CtScanStageKind.RECON_FBP -> baseInterval * 1.06f
                    CtScanStageKind.TUBE_EXPOSURE_KVP -> baseInterval
                }
            }
        }

        return state.copy(
            notes = kept,
            lives = lives,
            combo = combo,
            elapsedSec = newElapsed,
            spawnTimerSec = spawnTimer,
            nextId = nextId,
            stepIndex = stepIdx,
            signalAmplitude = amp,
            lastJudgment = judgment,
            isGameOver = lives <= 0
        )
    }

    fun tapGantryRhythmLane(
        state: GantryRhythmState,
        preferredKind: CtScanStageKind? = null
    ): GantryRhythmState {
        if (state.isGameOver || state.notes.isEmpty()) return state

        if (preferredKind != null) {
            val laneCandidate = state.notes
                .filter { it.kind == preferredKind }
                .minByOrNull { abs(it.progress - 1.0f) }

            if (laneCandidate != null) {
                val dist = abs(laneCandidate.progress - 1.0f)
                return if (dist <= 0.28f) {
                    awardRhythmHit(state, laneCandidate, dist)
                } else {
                    state.copy(
                        combo = 0,
                        signalAmplitude = 0.30f,
                        lastJudgment = "Tunggu sinyal ${preferredKind.label} menyentuh garis target!"
                    )
                }
            } else {
                // Lajur yang diketuk sedang kosong — beri umpan balik tanpa menghapus not lajur lain
                val activeNearTarget = state.notes.minByOrNull { abs(it.progress - 1.0f) }
                val hint = if (activeNearTarget != null && abs(activeNearTarget.progress - 1.0f) <= 0.32f) {
                    "Salah lajur! Target terdekat ada di ${activeNearTarget.kind.label}"
                } else {
                    "Lajur ${preferredKind.label} kosong — tunggu not turun!"
                }
                return state.copy(
                    combo = 0,
                    signalAmplitude = 0.25f,
                    lastJudgment = hint
                )
            }
        }

        val candidate = state.notes.minByOrNull { abs(it.progress - 1.0f) } ?: return state
        val dist = abs(candidate.progress - 1.0f)
        return if (dist <= 0.28f) {
            awardRhythmHit(state, candidate, dist)
        } else {
            state.copy(
                combo = 0,
                signalAmplitude = 0.30f,
                lastJudgment = "Tunggu hingga sinyal menyentuh garis target!"
            )
        }
    }

    private fun awardRhythmHit(
        state: GantryRhythmState,
        candidate: GantryBeatNote,
        dist: Float
    ): GantryRhythmState {
        val isPerfect = dist <= 0.11f
        val newCombo = state.combo + 1
        val mult = if (newCombo >= 6) 2 else 1
        val base = if (isPerfect) 120 else 80
        val gained = (base + (newCombo * 15).coerceAtMost(120)) * mult
        return state.copy(
            notes = state.notes.filterNot { it.id == candidate.id },
            score = state.score + gained,
            combo = newCombo,
            signalAmplitude = if (isPerfect) 1.0f else 0.82f,
            lastJudgment = buildString {
                append(if (isPerfect) "PERFECT" else "GOOD")
                append(" ${candidate.kind.label} +$gained")
                if (newCombo >= 6) append(" (Helikal 2x!)")
            }
        )
    }

    fun rankForMetalDefenseScore(score: Int): String = when {
        score >= 2200 -> "Master Konsol CT"
        score >= 1200 -> "Radiografer CT Senior"
        score >= 500 -> "Operator Siaga ALARA"
        else -> "Magang Konsol CT"
    }

    fun rankForGantryRhythmScore(score: Int): String = when {
        score >= 2400 -> "Master Konsol CT"
        score >= 1300 -> "Radiografer CT Senior"
        score >= 550 -> "Operator Siaga ALARA"
        else -> "Magang Konsol CT"
    }
}
