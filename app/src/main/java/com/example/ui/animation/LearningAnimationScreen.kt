package com.example.ui.animation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import com.example.ui.components.rememberRestorableScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CardSlate
import com.example.ui.theme.CardSlateBorder
import com.example.ui.theme.CyanBright
import com.example.ui.theme.CyanDark
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.DarkNavySlate
import com.example.ui.theme.DeepObsidian
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.CtUiState
import com.example.viewmodel.CtViewModel
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LearningAnimationScreen(
    state: CtUiState,
    viewModel: CtViewModel,
    modifier: Modifier = Modifier
) {
    val animList = listOf(
        "Rotasi Gantry & Rantai Sinyal CT",
        "Atenuasi Beer-Lambert & Kontras HU",
        "Sinogram & Rekonstruksi FBP",
        "Pitch Helikal & Bow-Tie Filter"
    )

    // Sudut rotasi hanya berjalan ketika state.isAnimationPlaying == true
    // dan menggunakan rememberSaveable agar tahan rotasi layar.
    var rotAngle by rememberSaveable { mutableFloatStateOf(0f) }
    LaunchedEffect(state.isAnimationPlaying) {
        if (!state.isAnimationPlaying) return@LaunchedEffect
        var lastFrameNanos = withFrameNanos { it }
        while (state.isAnimationPlaying) {
            val frameNanos = withFrameNanos { it }
            val deltaSec = ((frameNanos - lastFrameNanos).coerceAtLeast(0L)) / 1_000_000_000f
            lastFrameNanos = frameNanos
            rotAngle = (rotAngle + deltaSec * 120f) % 360f
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepObsidian)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // 1. PINNED TOP HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Text(
                    text = "Animasi Fisika & Akuisisi CT Scan",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = CyanDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary)
            ) {
                Text(
                    text = "Interaktif",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanBright,
                    maxLines = 1,
                    softWrap = false,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. PINNED PILIHAN TOPIK ANIMASI
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            animList.forEachIndexed { index, title ->
                val isSelected = state.selectedAnimationIndex == index
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { viewModel.selectAnimation(index) },
                    color = if (isSelected) CyanPrimary else DarkNavySlate,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) CyanBright else CardSlateBorder)
                ) {
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) DeepObsidian else TextSecondary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 3. SCROLLABLE CONTENT
        val animScrollState = rememberRestorableScrollState("tab_animasi", viewModel)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(animScrollState)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkNavySlate),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardSlateBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(DeepObsidian),
                        contentAlignment = Alignment.Center
                    ) {
                        val rotAngleProvider = { rotAngle }
                        when (state.selectedAnimationIndex) {
                            0 -> GantryChainCanvas(step = state.animationStep, isPlaying = state.isAnimationPlaying, rotAngleProvider = rotAngleProvider)
                            1 -> BeerLambertHuCanvas(step = state.animationStep, isPlaying = state.isAnimationPlaying, rotAngleProvider = rotAngleProvider)
                            2 -> SinogramFbpCanvas(step = state.animationStep, isPlaying = state.isAnimationPlaying, rotAngleProvider = rotAngleProvider)
                            else -> HelicalPitchBowTieCanvas(step = state.animationStep, isPlaying = state.isAnimationPlaying, rotAngleProvider = rotAngleProvider)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Pengatur Langkah Bertahap (Step 1 sampai 4)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { viewModel.setAnimationStep(state.animationStep - 1) },
                                enabled = state.animationStep > 1,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(imageVector = Icons.Default.FastRewind, contentDescription = "Sebelumnya", tint = if (state.animationStep > 1) CyanBright else TextMuted)
                            }

                            Text(
                                text = "Langkah ${state.animationStep} dari 4",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            IconButton(
                                onClick = { viewModel.setAnimationStep(state.animationStep + 1) },
                                enabled = state.animationStep < 4,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(imageVector = Icons.Default.FastForward, contentDescription = "Berikutnya", tint = if (state.animationStep < 4) CyanBright else TextMuted)
                            }
                        }

                        Button(
                            onClick = { viewModel.toggleAnimationPlay() },
                            colors = ButtonDefaults.buttonColors(containerColor = if (state.isAnimationPlaying) CardSlate else CyanPrimary),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (state.isAnimationPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = if (state.isAnimationPlaying) CyanBright else DeepObsidian,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(
                                text = if (state.isAnimationPlaying) "Jeda" else "Putar",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (state.isAnimationPlaying) CyanBright else DeepObsidian
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val (stepTitle, stepExplanation, analogyText) = getStepDescription(
                        animIndex = state.selectedAnimationIndex,
                        step = state.animationStep
                    )

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = DeepObsidian,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardSlateBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = stepTitle,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanBright
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stepExplanation,
                                fontSize = 12.sp,
                                color = TextPrimary,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.Top) {
                                Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(14.dp))
                                Text(
                                    text = "Analogi: $analogyText",
                                    fontSize = 11.sp,
                                    color = AmberWarning,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(84.dp))
        }
    }
}

// -------------------------------------------------------------
// Kanvas Animasi 1: Rotasi Gantry 360° & Rantai Sinyal CT Scan
// -------------------------------------------------------------
@Composable
fun GantryChainCanvas(step: Int, isPlaying: Boolean, rotAngleProvider: () -> Float) {
    var camAzimuth by rememberSaveable { mutableFloatStateOf(0.35f) }
    var camElevation by rememberSaveable { mutableFloatStateOf(0.28f) }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, drag ->
                        change.consume()
                        camAzimuth -= drag.x * 0.011f
                        camElevation = (camElevation + drag.y * 0.011f).coerceIn(-1.2f, 1.2f)
                    }
                }
        ) {
            val rotAngle = rotAngleProvider()
            val cx = size.width / 2f
            val cy = size.height / 2f + 4.dp.toPx()
            val radius = (minOf(size.width, size.height) * 0.37f)

            // Cincin Slip-Ring & Bore Gantry
            drawCircle(
                color = CardSlateBorder.copy(alpha = 0.45f),
                radius = radius * 1.06f,
                center = Offset(cx, cy),
                style = Stroke(3.dp.toPx())
            )
            drawCircle(
                color = CyanPrimary.copy(alpha = 0.25f),
                radius = radius * 0.88f,
                center = Offset(cx, cy),
                style = Stroke(1.5.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f))
            )

            // Penampang tubuh pasien di titik Laser Isocenter
            drawCircle(
                color = Color(0xFF1E293B),
                radius = radius * 0.34f,
                center = Offset(cx, cy)
            )
            drawCircle(
                color = Color(0xFFE2E8F0),
                radius = radius * 0.34f,
                center = Offset(cx, cy),
                style = Stroke(2.dp.toPx())
            )
            // Laser Isocenter Crosshair
            drawLine(color = RoseDanger.copy(alpha = 0.5f), start = Offset(cx - radius * 0.45f, cy), end = Offset(cx + radius * 0.45f, cy), strokeWidth = 1.dp.toPx())
            drawLine(color = RoseDanger.copy(alpha = 0.5f), start = Offset(cx, cy - radius * 0.45f), end = Offset(cx, cy + radius * 0.45f), strokeWidth = 1.dp.toPx())

            val rad = Math.toRadians((rotAngle + camAzimuth * 40f).toDouble()).toFloat()
            val tubePos = Offset(cx + radius * cos(rad), cy + radius * sin(rad))
            val detCenterRad = rad + Math.PI.toFloat()

            // Berkas kipas (Fan Beam) dari Tabung Sinar-X melewati Bow-Tie Filter menuju Busur Detektor
            val fanHalfAngle = 0.42f
            val detLeft = Offset(cx + radius * cos(detCenterRad - fanHalfAngle), cy + radius * sin(detCenterRad - fanHalfAngle))
            val detRight = Offset(cx + radius * cos(detCenterRad + fanHalfAngle), cy + radius * sin(detCenterRad + fanHalfAngle))

            if (step >= 2) {
                val beamPath = Path().apply {
                    moveTo(tubePos.x, tubePos.y)
                    lineTo(detLeft.x, detLeft.y)
                    lineTo(detRight.x, detRight.y)
                    close()
                }
                drawPath(
                    path = beamPath,
                    color = when (step) {
                        2 -> AmberWarning.copy(alpha = 0.22f)
                        3 -> CyanBright.copy(alpha = 0.24f)
                        else -> EmeraldSuccess.copy(alpha = 0.26f)
                    }
                )
                for (rIdx in -3..3) {
                    val rayAng = detCenterRad + (rIdx / 3f) * fanHalfAngle
                    val rayEnd = Offset(cx + radius * cos(rayAng), cy + radius * sin(rayAng))
                    drawLine(
                        color = if (step >= 3) CyanBright.copy(alpha = 0.7f) else AmberWarning.copy(alpha = 0.7f),
                        start = tubePos,
                        end = rayEnd,
                        strokeWidth = 1.4.dp.toPx()
                    )
                }
            }

            // Tabung Sinar-X (Merah/Oranye) & Busur Detektor + DAS (Cyan/Hijau) berhadapan 180°
            drawCircle(color = AmberWarning, radius = 8.dp.toPx(), center = tubePos)
            for (dIdx in -4..4) {
                val dAng = detCenterRad + (dIdx / 4f) * fanHalfAngle
                val dPos = Offset(cx + radius * cos(dAng), cy + radius * sin(dAng))
                drawCircle(
                    color = if (step == 4) EmeraldSuccess else CyanBright,
                    radius = 4.dp.toPx(),
                    center = dPos
                )
            }
        }

        Text(
            text = "Tabung Sinar-X & Busur Detektor berputar sinkron 360° (Rotate-Rotate)",
            fontSize = 10.sp,
            color = TextMuted,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        )
    }
}

// -------------------------------------------------------------
// Kanvas Animasi 2: Kurva Atenuasi Beer-Lambert & Kontras HU
// -------------------------------------------------------------
@Composable
fun BeerLambertHuCanvas(step: Int, isPlaying: Boolean, rotAngleProvider: () -> Float = { 0f }) {
    Spacer(modifier = Modifier.fillMaxSize().drawWithCache {
        val cx = 40f
        val cy = size.height - 40f
        val w = size.width - 80f
        val h = size.height - 70f

        // Kurva 1 (Cyan): Transmisi Beer-Lambert Jaringan Lunak I(x) = I0 * exp(-mu_soft * x)
        val pathSoftAtten = Path().apply {
            moveTo(cx, cy - h * 0.9f)
            for (i in 0..100) {
                val t = i / 100f
                lineTo(cx + t * w, cy - (Math.exp(-t * 1.8).toFloat()) * (h * 0.9f))
            }
        }
        // Kurva 2 (Rose/Merah): Atenuasi Kuat Tulang Kortikal / Kontras Iodin (Z tinggi -> Fotolistrik kuat)
        val pathBoneIodine = Path().apply {
            moveTo(cx, cy - h * 0.9f)
            for (i in 0..100) {
                val t = i / 100f
                lineTo(cx + t * w, cy - (Math.exp(-t * 4.6).toFloat()) * (h * 0.9f))
            }
        }
        val axisStroke = 2.dp.toPx()
        val curveStroke = Stroke(width = 3.dp.toPx())
        val markerStroke = 1.5.dp.toPx()
        val markerRadius = 5.dp.toPx()
        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)

        onDrawBehind {
            val rotAngle = rotAngleProvider()
            drawLine(color = CardSlateBorder, start = Offset(cx, cy), end = Offset(cx + w, cy), strokeWidth = axisStroke)
            drawLine(color = CardSlateBorder, start = Offset(cx, cy), end = Offset(cx, cy - h), strokeWidth = axisStroke)

            drawPath(path = pathSoftAtten, color = CyanBright, style = curveStroke)
            drawPath(path = pathBoneIodine, color = RoseDanger, style = curveStroke)

            val baseProgress = ((step - 1) * 0.22f + (rotAngle / 360f) * 0.25f).coerceIn(0.05f, 0.98f)
            val markerX = cx + baseProgress * w
            val markerSoftY = cy - (Math.exp(-baseProgress * 1.8).toFloat()) * (h * 0.9f)
            val markerBoneY = cy - (Math.exp(-baseProgress * 4.6).toFloat()) * (h * 0.9f)

            drawLine(
                color = AmberWarning.copy(alpha = 0.6f),
                start = Offset(markerX, cy),
                end = Offset(markerX, cy - h * 0.92f),
                strokeWidth = markerStroke,
                pathEffect = dashEffect
            )
            drawCircle(color = CyanBright, radius = markerRadius, center = Offset(markerX, markerSoftY))
            drawCircle(color = RoseDanger, radius = markerRadius, center = Offset(markerX, markerBoneY))
        }
    })
}

// -------------------------------------------------------------
// Kanvas Animasi 3: Pengisian Sinogram & Rekonstruksi FBP
// -------------------------------------------------------------
@Composable
fun SinogramFbpCanvas(step: Int, isPlaying: Boolean, rotAngleProvider: () -> Float = { 0f }) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val rotAngle = rotAngleProvider()
        val cx = size.width / 2f
        val cy = size.height / 2f
        val matrixSize = 140f

        // Baris-baris sudut proyeksi Sinogram P(t, theta) dari 0° s.d. 360°
        for (i in -4..4) {
            val y = cy + i * (matrixSize / 9f)
            val isFilled = (i + 4) <= (step * 2)
            drawLine(
                color = if (isFilled) CyanBright.copy(alpha = 0.55f) else CardSlateBorder.copy(alpha = 0.35f),
                start = Offset(cx - matrixSize / 2f, y),
                end = Offset(cx + matrixSize / 2f, y),
                strokeWidth = if (isFilled) 2.2.dp.toPx() else 1.dp.toPx()
            )
        }

        // Gelombang Sinusoidal jejak organ pada Sinogram
        val sinePath = Path()
        for (k in 0..60) {
            val frac = k / 60f
            val y = (cy - matrixSize / 2f) + frac * matrixSize
            val x = cx + (matrixSize * 0.32f) * sin(frac * 2f * Math.PI.toFloat())
            if (k == 0) sinePath.moveTo(x, y) else sinePath.lineTo(x, y)
        }
        drawPath(path = sinePath, color = EmeraldSuccess, style = Stroke(width = 2.5.dp.toPx()))

        // Titik akuisisi DAS yang menyapu baris kanal detektor saat Gantry berputar
        val currY = cy + (-4 + (step * 2)) * (matrixSize / 9f)
        val scanProgress = (rotAngle / 360f).coerceIn(0f, 1f)
        val penX = (cx - matrixSize / 2f) + scanProgress * matrixSize
        drawCircle(color = AmberWarning, radius = 5.dp.toPx(), center = Offset(penX, currY))
    }
}

@Composable
fun HelicalPitchBowTieCanvas(step: Int, isPlaying: Boolean, rotAngleProvider: () -> Float = { 0f }) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val rotAngle = rotAngleProvider()
        val cy = h * 0.50f
        val pitchOffset = ((rotAngle / 360f) * w * 0.16f)

        // Sumbu z meja pasien horizontal + lintasan spiral helikal (Pitch = d / (N * T))
        drawLine(Color(0xFF475569), Offset(w * 0.08f, cy), Offset(w * 0.92f, cy), strokeWidth = 2.dp.toPx())
        val spiral = Path()
        val stepsCount = 60
        for (i in 0..stepsCount) {
            val t = i / stepsCount.toFloat()
            val x = w * 0.10f + t * w * 0.80f
            val freq = when (step) {
                1 -> 18f // Pitch rendah ~0.5 overlap rapat
                2 -> 12f // Pitch 1.0 kontigu
                else -> 8f // Pitch > 1.0 renggang cepat
            }
            val y = cy + sin(t * freq + Math.toRadians(rotAngle.toDouble()).toFloat() + pitchOffset * 0.01f) * (h * 0.28f)
            if (i == 0) spiral.moveTo(x, y) else spiral.lineTo(x, y)
        }
        drawPath(
            path = spiral,
            color = if (step == 3) AmberWarning else CyanBright,
            style = Stroke(width = 3.dp.toPx())
        )

        // Bow-Tie Filter di bagian atas gantry (tebal di tepi, tipis di pusat isocenter)
        val btLeft = Path().apply {
            moveTo(w * 0.34f, h * 0.10f)
            lineTo(w * 0.48f, h * 0.17f)
            lineTo(w * 0.34f, h * 0.22f)
            close()
        }
        val btRight = Path().apply {
            moveTo(w * 0.66f, h * 0.10f)
            lineTo(w * 0.52f, h * 0.17f)
            lineTo(w * 0.66f, h * 0.22f)
            close()
        }
        drawPath(btLeft, color = EmeraldSuccess.copy(alpha = 0.78f))
        drawPath(btRight, color = EmeraldSuccess.copy(alpha = 0.78f))
    }
}

// Helper teks penjelasan fisika medis langkah animasi CT Scan
fun getStepDescription(animIndex: Int, step: Int): Triple<String, String, String> {
    return when (animIndex) {
        0 -> when (step) {
            1 -> Triple(
                "Langkah 1: Suplai Daya Kontinu Slip-Ring & HF HV Generator",
                "Cincin konduktor Low-Voltage Slip-Ring menyalurkan daya listrik menuju High-Frequency Inverter HV Generator (80–140 kVp) yang terpasang langsung di atas Rotating Frame Gantry, memungkinkan putaran 360° terus-menerus tanpa kabel terbelit.",
                "Seperti rel listrik melingkar yang menyuplai daya ke komidi putar berkecepatan tinggi tanpa satu pun kabel yang kusut."
            )
            2 -> Triple(
                "Langkah 2: Pancaran Berkas Kipas Sinar-X & Bow-Tie Filter",
                "Tabung Sinar-X CT berkapasitas 5–8+ MHU memancarkan foton Bremsstrahlung dan Karakteristik melewati Bow-Tie Filter (tipis di tengah, tebal di tepi) serta Pre-Patient Collimator untuk menyeragamkan fluks foton dan memangkas dosis kulit tepi.",
                "Lampu sorot yang dipasangi lensa dasi kupu-kupu agar bagian pinggir tubuh yang tipis tidak kelebihan cahaya."
            )
            3 -> Triple(
                "Langkah 3: Atenuasi Pasien di Isocenter & Penolakan Scatter",
                "Berkas sinar-X menembus tubuh pasien yang diposisikan tepat di titik Laser Isocenter. Foton yang terhambur miring (Compton scatter) diserap oleh sekat Tungsten Post-Patient Anti-Scatter Septa di depan permukaan detektor.",
                "Sekat kacamata kuda di depan tiap sensor detektor yang memblokir pantulan cahaya liar dari samping."
            )
            4 -> Triple(
                "Langkah 4: Konversi Detektor Solid-State (Gd₂O₂S) & Digitalisasi DAS",
                "Kristal sintilator Solid-State mengubah sisa foton sinar-X menjadi kilatan cahaya tampak, diubah oleh Silicon Photodiode menjadi arus listrik lemah (pA–nA), lalu diintegrasikan dan dikonversi secara logaritmik ln(I₀/I) oleh ADC di dalam modul DAS.",
                "Tongkat estafet terakhir yang menerjemahkan kedipan cahaya kristal menjadi deretan angka biner Sinogram berkecepatan gigabit."
            )
            else -> Triple("", "", "")
        }
        3 -> when (step) {
            1 -> Triple(
                "Langkah 1: Pitch Helikal Rendah (< 1,0 - Overlapping)",
                "Meja pasien bergerak lebih pendek daripada total lebar kolimasi berkas (Pitch = d / (N x T) < 1). Irisan saling tumpang tindih sehingga resolusi sumbu-Z tinggi dan noise menurun, ideal untuk organ kecil atau CT Kepala.",
                "Mengecat dinding dengan sapuan kuas yang tumpang tindih agar hasilnya ekstra rata dan padat."
            )
            2 -> Triple(
                "Langkah 2: Pitch 1,0 (Akuisisi Kontigu Seimbang)",
                "Pergerakan meja per rotasi sama persis dengan total lebar berkas (d = N x T). Tidak ada celah maupun tumpang tindih geometris, menyeimbangkan kecepatan akuisisi dan dosis radiasi rutin.",
                "Berjalan menyusun ubin lantai tepat berdampingan tanpa celah dan tanpa menumpuk."
            )
            3 -> Triple(
                "Langkah 3: Pitch Tinggi (> 1,0 - Scan Cepat Toraks/CTPA)",
                "Meja melaju cepat melewati celah antar-lingkaran heliks sehingga waktu tahan napas pasien jauh lebih singkat. Pada single-source MSCT, perangkat menaikkan mA agar noise tetap terjaga (Effective mAs konstan).",
                "Meregangkan per/pegas memanjang agar seluruh panjang pasien selesai dipindai dalam beberapa detik."
            )
            else -> Triple(
                "Langkah 4: Bow-Tie Shaping Filter & Penyesuaian Isocenter",
                "Filter berbentuk dasi kupu-kupu lebih tebal di tepi berkas sinar-X untuk menyerap foton lunak pada bagian pinggir tubuh yang lebih tipis, memangkas dosis kulit hingga 40-50% asalkan pasien ditempatkan tepat di Isocenter.",
                "Lensa kacamata hitam tergradasi di sisi kiri-kanan agar bagian tepi yang tipis tidak tersilaukan oleh sinar."
            )
        }
        1 -> when (step) {
            1 -> Triple(
                "Langkah 1: Penurunan Eksponensial Hukum Beer-Lambert",
                "Saat menembus jaringan setebal x dengan koefisien atenuasi linier μ, intensitas berkas sinar-X menurun secara eksponensial sesuai I(x) = I₀ · e^(−μx). Setiap ketebalan 1 Half-Value Layer (HVL = 0,693/μ) memangkas intensitas foton menjadi separuhnya (50%).",
                "Melewati berlapis-lapis kaca film di mana tiap lapis kaca mengurangi separuh cahaya yang lewat."
            )
            2 -> Triple(
                "Langkah 2: Kontras Fotolistrik (Z³/E³) pada Tulang & Iodin",
                "Kurva merah meluruh jauh lebih tajam daripada kurva biru (jaringan lunak) karena tulang kortikal (Ca, Z=20) dan media kontras Iodin (Z=53, K-edge 33,2 keV) menyerap foton sinar-X secara kuat melalui Efek Fotolistrik.",
                "Tulang padat dan kontras Iodin bekerja seperti perisai tebal yang menghentikan foton sinar-X sehingga tampak putih terang (hiperdens)."
            )
            3 -> Triple(
                "Langkah 3: Pengerasan Berkas (Beam Hardening) Polikromatik",
                "Karena spektrum sinar-X tabung bersifat polikromatik, foton berenergi rendah terserap lebih dulu di lapisan dangkal sehingga energi rata-rata berkas yang lolos meningkat. Koreksi filtrasi dan kalibrasi mencegah munculnya cupping artifact.",
                "Pelari berstamina lemah gugur di tanjakan awal sehingga yang mencapai garis finis hanyalah pelari berenergi tinggi."
            )
            4 -> Triple(
                "Langkah 4: Standarisasi Skala Hounsfield Unit (HU) & Windowing",
                "Koefisien atenuasi μ setiap voksel dinormalisasi terhadap air murni menjadi skala kuantitatif HU = 1000 × (μ − μ_air) / μ_air (Air = 0 HU, Udara = −1000 HU), lalu dipetakan ke layar monitor melalui Window Width (WW) dan Window Level (WL).",
                "Mengubah angka serapan fisik menjadi skala derajat standar seperti termometer Celcius yang berpatokan pada titik nol air."
            )
            else -> Triple("", "", "")
        }
        else -> when (step) {
            1 -> Triple(
                "Langkah 1: Pencuplikan Proyeksi Multi-Sudut (1.000–2.400 Views)",
                "Selama Gantry berputar 360°, modul DAS membaca nilai integral garis atenuasi P(t, θ) = ln(I₀/I) dari seluruh kanal detektor (t) pada ribuan sudut proyeksi (θ) secara berurutan.",
                "Memotret siluet bayangan objek dari ribuan sudut melingkar tanpa henti."
            )
            2 -> Triple(
                "Langkah 2: Pembentukan Gelombang Sinus pada Matriks Sinogram",
                "Ketika profil proyeksi dari sudut 0° hingga 360° ditumpuk baris demi baris, setiap struktur anatomi di luar pusat Isocenter membentuk jejak kurva sinusoidal yang mulus (Transformasi Radon / Raw Data Sinogram).",
                "Jejak posisi mercusuar yang tampak meliuk ke kiri dan kanan membentuk gelombang sinus saat dipotret dari kapal yang berputar mengelilingi pulau."
            )
            3 -> Triple(
                "Langkah 3: Konvolusi Filter Frekuensi (Ramp Filter & Kernel)",
                "Sebelum diproyeksikan balik, setiap baris Sinogram dikonvolusikan dengan filter matematis (Ramp Filter) untuk menghilangkan kabur bintang 1/r, serta disesuaikan dengan pilihan Soft Kernel (halus, rendah noise) atau Sharp/Bone Kernel (tepi tajam).",
                "Memasang cetakan penajam tepi pada setiap sorotan proyektor agar bayangan yang bertumpuk tidak saling mengaburkan."
            )
            4 -> Triple(
                "Langkah 4: Back-Projection & Rekonstruksi Iteratif / AI DLIR",
                "Profil yang telah difilter diproyeksikan balik dari 360° ke atas matriks piksel 512×512 (atau diperbarui secara berulang lewat algoritma Iterative Reconstruction & AI DLIR) untuk menghasilkan citra penampang aksial CT yang tajam dan rendah dosis.",
                "Menyatukan ribuan goresan sudut pandang menjadi satu lukisan anatomi penampang melintang yang jernih."
            )
            else -> Triple("", "", "")
        }
    }
}
