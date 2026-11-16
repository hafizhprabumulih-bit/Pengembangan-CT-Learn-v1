package com.example.ui.simulators

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import com.example.util.CtConsoleCaseEvaluator
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.util.CtPhysicsCalculator
import com.example.viewmodel.CtUiState
import com.example.viewmodel.CtViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CtMsctPitchAndReconstructionSimulator() {
    var detectorRows by rememberSaveable { mutableIntStateOf(64) } // 16, 64, 128, 320
    var pitch by rememberSaveable { mutableFloatStateOf(1.0f) } // 0.5 to 1.5
    var reconAlgo by rememberSaveable { mutableIntStateOf(1) } // 0: Simple BP, 1: FBP, 2: Iterative / DLIR
    var isSharpKernel by rememberSaveable { mutableStateOf(false) } // false: Soft/Standard, true: Sharp/Bone

    val singleRowWidthMm = if (detectorRows == 320) 0.5f else 0.625f
    val totalCollimationMm = detectorRows * singleRowWidthMm
    val tableFeedPerRotMm = pitch * totalCollimationMm
    val rotTimeSec = 0.5f
    val tableSpeedMmPerSec = tableFeedPerRotMm / rotTimeSec
    val effectiveSliceMm = singleRowWidthMm * (1f + (pitch - 0.5f).coerceAtLeast(0f) * 0.35f)
    val scanTimeFor30Cm = 300f / tableSpeedMmPerSec

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Simulator 3: Geometri MSCT, Helikal Pitch & Rekonstruksi",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Atur jumlah baris detektor MSCT, nilai Pitch helikal, algoritma rekonstruksi, dan Kernel konvolusi.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text("Konfigurasi Baris Detektor MSCT (N × T):", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(16, 64, 128, 320).forEach { rows ->
                        FilterChip(
                            selected = detectorRows == rows,
                            onClick = { detectorRows = rows },
                            label = { Text("$rows-Slice", fontSize = 11.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Helical Trajectory & SSP Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(195.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0F172A))
                        .padding(10.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxWidth().height(175.dp)) {
                        val w = size.width
                        val h = size.height
                        val midY = h * 0.42f

                        // Patient cylinder body axis
                        drawRoundRect(
                            color = Color(0xFF1E293B),
                            topLeft = Offset(w * 0.06f, midY - 36f),
                            size = Size(w * 0.88f, 72f),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(18f, 18f)
                        )

                        // Helical spiral path based on Pitch
                        val spiralPath = Path()
                        val cycles = (7.5f / pitch).coerceIn(3f, 14f)
                        val steps = 220
                        val startX = w * 0.08f
                        val spanX = w * 0.84f
                        for (i in 0..steps) {
                            val t = i.toFloat() / steps
                            val x = startX + t * spanX
                            val y = midY + 32f * sin(t * cycles * 2f * PI.toFloat())
                            if (i == 0) spiralPath.moveTo(x, y) else spiralPath.lineTo(x, y)
                        }
                        val beamStrokeWidth = (totalCollimationMm / 160f * 18f).coerceIn(3f, 14f)
                        drawPath(
                            path = spiralPath,
                            color = when {
                                pitch < 0.9f -> Color(0xFFF59E0B) // Overlapping
                                pitch <= 1.1f -> Color(0xFF22C55E) // Contiguous
                                else -> Color(0xFF38BDF8) // Extended / Gaps
                            },
                            style = Stroke(width = beamStrokeWidth, cap = StrokeCap.Round)
                        )

                        // Slice Sensitivity Profile (SSP) curve in bottom area
                        val sspBaseY = h * 0.92f
                        val sspCenterX = w * 0.5f
                        val sspPath = Path()
                        val sigma = 18f * pitch
                        for (dx in -90..90 step 3) {
                            val x = sspCenterX + dx
                            val gauss = exp(-(dx * dx).toFloat() / (2f * sigma * sigma))
                            val y = sspBaseY - gauss * 36f
                            if (dx == -90) sspPath.moveTo(x, y) else sspPath.lineTo(x, y)
                        }
                        drawPath(
                            path = sspPath,
                            color = if (isSharpKernel) Color(0xFFF43F5E) else Color(0xFFA855F7),
                            style = Stroke(width = 3f)
                        )
                    }

                    Text(
                        text = when {
                            pitch < 0.95f -> "Pitch ${String.format(Locale.US, "%.2f", pitch)}: Oversampling / Tumpang-Tindih (Resolusi z Tinggi)"
                            pitch <= 1.05f -> "Pitch ${String.format(Locale.US, "%.2f", pitch)}: Contiguous Spiral (Keseimbangan Ideal)"
                            else -> "Pitch ${String.format(Locale.US, "%.2f", pitch)}: Extended Spiral / Celah Helikal (Akuisisi Cepat, SSP Melebar)"
                        },
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.TopStart)
                    )
                    Text(
                        text = "Kurva SSP (Effective Slice: ${String.format(Locale.US, "%.2f", effectiveSliceMm)} mm)",
                        color = Color(0xFFE2E8F0),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.align(Alignment.BottomStart)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Helical Beam Pitch (d / W):", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                    Text(String.format(Locale.US, "%.2f", pitch), style = MaterialTheme.typography.labelMedium, fontFamily = FontFamily.Monospace)
                }
                Slider(
                    value = pitch,
                    onValueChange = { pitch = it },
                    valueRange = 0.5f..1.5f,
                    modifier = Modifier.testTag("slider_ct_pitch")
                )

                Text(
                    text = "Kolimasi W = ${String.format(Locale.US, "%.1f", totalCollimationMm)} mm | Table Feed d = ${String.format(Locale.US, "%.1f", tableFeedPerRotMm)} mm/rot | Speed = ${String.format(Locale.US, "%.1f", tableSpeedMmPerSec)} mm/dtk | Waktu Scan 30 cm = ${String.format(Locale.US, "%.1f", scanTimeFor30Cm)} dtk",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text("Algoritma Rekonstruksi & Konvolusi Kernel:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = reconAlgo == 0,
                        onClick = { reconAlgo = 0 },
                        label = { Text("Simple BP (Kabur 1/r)", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = reconAlgo == 1,
                        onClick = { reconAlgo = 1 },
                        label = { Text("FBP (Ramp Filter)", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = reconAlgo == 2,
                        onClick = { reconAlgo = 2 },
                        label = { Text("Iterative / DLIR", fontSize = 11.sp) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isSharpKernel) "Kernel Aktif: Sharp / Bone (MTF Tinggi, Tepi Tajam)" else "Kernel Aktif: Soft / Standard (Noise Rendah, Halus)",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Switch(
                        checked = isSharpKernel,
                        onCheckedChange = { isSharpKernel = it }
                    )
                }

                val reconSummary = when (reconAlgo) {
                    0 -> "Simple Back-Projection menumpuk garis proyeksi tanpa filter konvolusi sehingga menghasilkan halo pengaburan bintang (1/r blurring)."
                    1 -> if (isSharpKernel) {
                        "FBP + Sharp/Bone Kernel memperkuat frekuensi spasial tinggi untuk menajamkan korteks tulang/paru, namun meningkatkan quantum noise."
                    } else {
                        "FBP + Soft/Standard Kernel meredam derau frekuensi tinggi untuk membedakan jaringan lunak otak dan abdomen."
                    }
                    else -> "Iterative Reconstruction (IR / DLIR) memodelkan statistik foton sehingga mampu menekan quantum noise hingga 40–60% pada protokol Low-Dose CT."
                }
                Text(
                    text = reconSummary,
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Interactive Visual Comparison Canvas: Sinogram (Left) vs Reconstructed Image (Right)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(165.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF090D16))
                        .padding(10.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxWidth().height(145.dp)) {
                        val w = size.width
                        val h = size.height
                        val sinoW = w * 0.34f

                        // Left: Raw Data Sinogram
                        drawRect(color = Color(0xFF1E293B), topLeft = Offset.Zero, size = Size(sinoW, h))
                        for (sIdx in 0..3) {
                            val p = Path()
                            val amp = sinoW * (0.14f + sIdx * 0.06f)
                            val ph = sIdx * 1.1f
                            for (yStep in 0..60) {
                                val yf = (yStep / 60f) * h
                                val xf = sinoW * 0.5f + amp * sin((yStep / 60f) * 2f * PI.toFloat() + ph)
                                if (yStep == 0) p.moveTo(xf, yf) else p.lineTo(xf, yf)
                            }
                            drawPath(p, color = Color(0xFF38BDF8).copy(alpha = 0.75f), style = Stroke(width = 2f))
                        }

                        // Right: Reconstructed Axial Phantom Preview
                        val cx = w * 0.68f
                        val cy = h * 0.50f
                        val r = minOf(w * 0.26f, h * 0.42f)

                        if (reconAlgo == 0) {
                            // Simple Back-Projection 1/r Star-Artifact Halo
                            for (ray in 0 until 8) {
                                val ang = ray * (PI.toFloat() / 8f)
                                drawLine(
                                    color = Color(0xFF94A3B8).copy(alpha = 0.38f),
                                    start = Offset(cx - r * 1.25f * cos(ang), cy - r * 1.25f * sin(ang)),
                                    end = Offset(cx + r * 1.25f * cos(ang), cy + r * 1.25f * sin(ang)),
                                    strokeWidth = 14f,
                                    cap = StrokeCap.Round
                                )
                            }
                            drawCircle(color = Color(0xFFCBD5E1).copy(alpha = 0.45f), radius = r * 1.12f, center = Offset(cx, cy))
                        }

                        // Cortical Bone Ring & Soft Tissue Brain/Abdomen
                        drawCircle(
                            color = if (isSharpKernel) Color.White else Color(0xFFCBD5E1),
                            radius = r,
                            center = Offset(cx, cy)
                        )
                        drawCircle(
                            color = Color(0xFF475569),
                            radius = if (isSharpKernel) r * 0.88f else r * 0.83f,
                            center = Offset(cx, cy)
                        )
                        // Low-contrast lesion (+25 HU) & high-contrast calcification (+800 HU)
                        drawCircle(
                            color = if (reconAlgo == 0) Color(0xFF64748B) else Color(0xFF1E293B),
                            radius = r * 0.24f,
                            center = Offset(cx - r * 0.32f, cy + r * 0.10f)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = if (isSharpKernel) r * 0.13f else r * 0.16f,
                            center = Offset(cx + r * 0.34f, cy - r * 0.16f)
                        )

                        // Quantum Mottle Noise Speckles
                        val noiseDots = when {
                            reconAlgo == 2 -> if (isSharpKernel) 8 else 3
                            isSharpKernel -> 34
                            else -> 12
                        }
                        for (n in 0 until noiseDots) {
                            val ang = (n * 137.5f) * (PI.toFloat() / 180f)
                            val dist = r * 0.72f * ((n % 7 + 1) / 8f)
                            drawCircle(
                                color = if (n % 2 == 0) Color.White.copy(alpha = 0.55f) else Color.Black.copy(alpha = 0.65f),
                                radius = if (isSharpKernel) 2.2f else 1.4f,
                                center = Offset(cx + dist * cos(ang), cy + dist * sin(ang))
                            )
                        }
                    }

                    Text(
                        text = "Raw Sinogram P(t,θ)",
                        color = Color(0xFF38BDF8),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.align(Alignment.BottomStart)
                    )
                    Text(
                        text = when (reconAlgo) {
                            0 -> "Rekon: Simple BP (Kabur Bintang 1/r)"
                            1 -> if (isSharpKernel) "Rekon: FBP + Sharp Kernel (Tepi Tajam, Noise Tinggi)" else "Rekon: FBP + Soft Kernel (Halus, Kontras Lunak)"
                            else -> if (isSharpKernel) "Rekon: DLIR + Sharp (Tajam & Rendah Noise)" else "Rekon: Iterative / DLIR (Kontras Tinggi, Hemat Dosis)"
                        },
                        color = Color.White,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                }
                if (false) Text(
                    text = reconSummary,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CtDosimetryAndBolusTrackingSimulator(
    state: CtUiState? = null,
    viewModel: CtViewModel? = null
) {
    var localKvp by rememberSaveable { mutableIntStateOf(120) }
    var localMa by rememberSaveable { mutableFloatStateOf(240f) }
    var localRotTime by rememberSaveable { mutableFloatStateOf(0.5f) }
    var localPitch by rememberSaveable { mutableFloatStateOf(1.0f) }
    var localScanLen by rememberSaveable { mutableFloatStateOf(30f) }
    var localHead by rememberSaveable { mutableStateOf(false) }

    val kvp = state?.tubeKvp ?: localKvp
    val tubeMa = state?.tubeMa ?: localMa
    val rotTime = state?.rotationTimeSec ?: localRotTime
    val pitch = state?.helicalPitch ?: localPitch
    val scanLengthCm = state?.scanLengthCm ?: localScanLen
    val isHeadPhantom = state?.isHeadPhantom ?: localHead

    // Bolus tracking controls
    var flowRateMlSec by rememberSaveable { mutableFloatStateOf(4.0f) } // 2.0 to 6.0 mL/s
    var triggerHu by rememberSaveable { mutableFloatStateOf(120f) } // 80 to 180 HU
    var useSalineChaser by rememberSaveable { mutableStateOf(true) }
    var postProcMode by rememberSaveable { mutableIntStateOf(0) } // 0: MPR, 1: MIP (CTA), 2: MinIP (Airway), 3: 3D VRT
    var selectedCaseId by rememberSaveable { mutableStateOf<String?>("case_stroke") }
    var recordedCasesNote by rememberSaveable { mutableStateOf<String?>(null) }
    val ssdeFactor = CtPhysicsCalculator.ssdeConversionFactor(isHeadPhantom = isHeadPhantom)

    val effectiveMas = CtPhysicsCalculator.effectiveMas(tubeMa, rotTime, pitch)
    val ctdiVol = CtPhysicsCalculator.ctdiVolMgy(kvp, effectiveMas, isHeadPhantom)
    val dlp = CtPhysicsCalculator.dlpMgyCm(ctdiVol, scanLengthCm)
    val effectiveDoseMsv = CtPhysicsCalculator.effectiveDoseMsv(dlp, isHeadPhantom)
    val relativeNoiseSd = CtPhysicsCalculator.relativeNoiseSdHu(kvp, effectiveMas, state?.isIterativeReconActive ?: false)

    // Bolus tracking peak calculation
    val peakArterialHu = CtPhysicsCalculator.bolusPeakArterialHu(kvp, flowRateMlSec, useSalineChaser)
    val timeToPeakSec = CtPhysicsCalculator.bolusTimeToPeakSec(flowRateMlSec)
    val triggerTimeSec = CtPhysicsCalculator.bolusTriggerTimeSec(timeToPeakSec, peakArterialHu, triggerHu)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Simulator 4: Konsol Eksposi, Dosimetri (CTDIvol/DLP) & Bolus Tracking",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Simulasikan pengaturan kVp, mA, waktu rotasi, dan panjang scan terhadap indeks dosis CTDIvol, DLP, Quantum Noise, serta kurva Bolus Tracking.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Readout Dashboard (Grid 2x2 responsif untuk layar < 360dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0F172A))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Eff. mAs", color = Color(0xFF94A3B8), fontSize = 10.sp)
                            Text("${effectiveMas.roundToInt()} mAs", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("CTDIvol / SSDE", color = Color(0xFF94A3B8), fontSize = 10.sp)
                            Text("${String.format(Locale.US, "%.1f", ctdiVol)} / ${String.format(Locale.US, "%.1f", ctdiVol * ssdeFactor)} mGy", color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, fontSize = 11.5.sp)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("DLP (Dose-Length)", color = Color(0xFF94A3B8), fontSize = 10.sp)
                            Text("${dlp.roundToInt()} mGy·cm", color = Color(0xFFF59E0B), fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Dosis E / Noise", color = Color(0xFF94A3B8), fontSize = 10.sp)
                            Text("${String.format(Locale.US, "%.1f", effectiveDoseMsv)} mSv | ±${String.format(Locale.US, "%.1f", relativeNoiseSd)} HU", color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, fontSize = 11.5.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(80, 100, 120, 140).forEach { v ->
                            FilterChip(
                                selected = kvp == v,
                                onClick = { if (viewModel != null) viewModel.setTubeKvp(v) else localKvp = v },
                                label = { Text("$v kVp", fontSize = 11.sp) }
                            )
                        }
                    }
                    FilterChip(
                        selected = isHeadPhantom,
                        onClick = { val next = !isHeadPhantom; if (viewModel != null) viewModel.setHeadPhantom(next) else localHead = next },
                        label = { Text(if (isHeadPhantom) "Fantom 16cm (Kepala)" else "Fantom 32cm (Tubuh)", fontSize = 11.sp) }
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Arus Tabung (mA): ${tubeMa.roundToInt()} mA", style = MaterialTheme.typography.labelSmall)
                    Text("Rotasi: ${String.format(Locale.US, "%.2f", rotTime)} dtk | Pitch: ${String.format(Locale.US, "%.2f", pitch)}", style = MaterialTheme.typography.labelSmall)
                }
                Slider(
                    value = tubeMa,
                    onValueChange = { if (viewModel != null) viewModel.setTubeMa(it) else localMa = it },
                    valueRange = 50f..500f,
                    modifier = Modifier.testTag("slider_ct_ma")
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Slider(
                        value = rotTime,
                        onValueChange = { if (viewModel != null) viewModel.setRotationTime(it) else localRotTime = it },
                        valueRange = 0.28f..1.0f,
                        modifier = Modifier.weight(1f).testTag("slider_ct_rottime")
                    )
                    Slider(
                        value = pitch,
                        onValueChange = { if (viewModel != null) viewModel.setHelicalPitch(it) else localPitch = it },
                        valueRange = 0.5f..1.5f,
                        modifier = Modifier.weight(1f).testTag("slider_ct_dosimetry_pitch")
                    )
                }
                Spacer(modifier = Modifier.height(2.dp)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Panjang Rentang Scan (L):", style = MaterialTheme.typography.labelSmall)
                    Text("${scanLengthCm.roundToInt()} cm", style = MaterialTheme.typography.labelSmall, fontFamily = FontFamily.Monospace)
                }
                Slider(
                    value = scanLengthCm,
                    onValueChange = { if (viewModel != null) viewModel.setScanLength(it) else localScanLen = it },
                    valueRange = 10f..60f,
                    modifier = Modifier.testTag("slider_ct_scan_length")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Bolus Tracking Time-Attenuation Curve (TAC) Canvas
                Text(
                    text = "Kurva Sinkronisasi Bolus Tracking (Time-Attenuation Curve ROI):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0F172A))
                        .padding(10.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                        val w = size.width
                        val h = size.height

                        // Threshold horizontal dashed line
                        val threshY = h - (triggerHu / 450f * h).coerceIn(15f, h - 15f)
                        drawLine(
                            color = Color(0xFFF59E0B),
                            start = Offset(0f, threshY),
                            end = Offset(w, threshY),
                            strokeWidth = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)
                        )

                        // Aorta Arterial Curve (Red) & Portal Venous Parenchymal Curve (Cyan)
                        val artPath = Path()
                        val pvPath = Path()
                        val maxT = 75f
                        for (tInt in 0..75) {
                            val t = tInt.toFloat()
                            val x = (t / maxT) * w
                            val artHu = 40f + if (t > 6f) {
                                val dt = (t - timeToPeakSec) / 6.5f
                                (peakArterialHu - 40f) * exp(-dt * dt)
                            } else 0f
                            val pvHu = 50f + if (t > 18f) {
                                val dt = (t - 58f) / 18f
                                95f * exp(-dt * dt)
                            } else 0f

                            val yArt = h - (artHu / 450f * h).coerceIn(4f, h - 4f)
                            val yPv = h - (pvHu / 450f * h).coerceIn(4f, h - 4f)
                            if (tInt == 0) {
                                artPath.moveTo(x, yArt)
                                pvPath.moveTo(x, yPv)
                            } else {
                                artPath.lineTo(x, yArt)
                                pvPath.lineTo(x, yPv)
                            }
                        }
                        drawPath(artPath, color = Color(0xFFEF4444), style = Stroke(width = 3.5f))
                        drawPath(pvPath, color = Color(0xFF06B6D4), style = Stroke(width = 2.5f))

                        // Trigger vertical indicator
                        val trigX = (triggerTimeSec / maxT) * w
                        drawLine(
                            color = Color(0xFF4ADE80),
                            start = Offset(trigX, 0f),
                            end = Offset(trigX, h),
                            strokeWidth = 2f
                        )
                    }

                    Text(
                        text = "Trigger ROI: +${triggerHu.roundToInt()} HU tercapai @ ${String.format(Locale.US, "%.1f", triggerTimeSec)} dtk | Puncak Aorta: +${peakArterialHu.roundToInt()} HU",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.align(Alignment.TopStart)
                    )
                    Text(
                        text = "Merah: Aorta (CTA) | Biru: Vena Porta Hepar (60-70 dtk)",
                        color = Color(0xFFCBD5E1),
                        fontSize = 10.sp,
                        modifier = Modifier.align(Alignment.BottomStart)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Flow Rate Injektor: ${String.format(Locale.US, "%.1f", flowRateMlSec)} mL/dtk", style = MaterialTheme.typography.labelSmall)
                    Text("Ambang Trigger ROI: +${triggerHu.roundToInt()} HU", style = MaterialTheme.typography.labelSmall)
                }
                Slider(
                    value = flowRateMlSec,
                    onValueChange = { flowRateMlSec = it },
                    valueRange = 2.0f..6.0f
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (useSalineChaser) "Saline Chaser NaCl 0,9% (40 mL): AKTIF (Bolus Padat)" else "Saline Chaser: NONAKTIF",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Switch(checked = useSalineChaser, onCheckedChange = { useSalineChaser = it })
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Mode Rekonstruksi Post-Processing 3D:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(
                        0 to "MPR (Koronal/Sagital)",
                        1 to "MIP (Angiografi/Nodul)",
                        2 to "MinIP (Jalan Napas/Emfisema)",
                        3 to "3D VRT (Bedah/Anatomi)"
                    ).forEach { (idx, label) ->
                        FilterChip(
                            selected = postProcMode == idx,
                            onClick = { postProcMode = idx },
                            label = { Text(label, fontSize = 11.sp) }
                        )
                    }
                }
                Text(
                    text = when (postProcMode) {
                        0 -> "MPR (Multiplanar Reformation): Menyusun ulang tumpukan voxel isotropik menjadi potongan Koronal, Sagital, atau Curved MPR tanpa kehilangan resolusi."
                        1 -> "MIP (Maximum Intensity Projection): Menampilkan voxel dengan nilai HU tertinggi di sepanjang slab; ideal untuk CT Angiografi (+300 HU) dan nodul paru."
                        2 -> "MinIP (Minimum Intensity Projection): Menampilkan voxel dengan nilai HU terendah (-900 s.d. -1000 HU); ideal untuk trakeobronkial dan air-trapping."
                        else -> "3D VRT (Volume Rendering Technique): Memetakan rentang HU ke opasitas dan warna 3D berbayang untuk perencanaan bedah ortopedi dan aneurisma."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (selectedCaseId != null) {
                    val activeCase = CtConsoleCaseEvaluator.clinicalCases.first { it.id == selectedCaseId }
                    val windowTag = state?.let { "${it.windowWidthHu.roundToInt()}/${it.windowLevelHu.roundToInt()}" }
                    val caseEval = CtConsoleCaseEvaluator.evaluateCase(
                        caseId = activeCase.id,
                        kvp = kvp,
                        effectiveMas = effectiveMas,
                        pitch = pitch,
                        ctdiVol = ctdiVol,
                        isHeadPhantom = isHeadPhantom,
                        noiseSdHu = relativeNoiseSd,
                        ssdeMgy = ctdiVol * ssdeFactor,
                        selectedWindowLabel = windowTag
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f))
                            .border(
                                1.dp,
                                if (caseEval.passed) Color(0xFF059669) else Color(0xFFF59E0B),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Tantangan Kasus Konsol Klinis Terpadu:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            CtConsoleCaseEvaluator.clinicalCases.forEachIndexed { cIdx, cItem ->
                                FilterChip(
                                    selected = selectedCaseId == cItem.id,
                                    onClick = { selectedCaseId = cItem.id },
                                    label = { Text("Kasus ${cIdx + 1}", fontSize = 11.sp) }
                                )
                            }
                        }
                        Text(
                            text = activeCase.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = activeCase.clinicalScenario,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = caseEval.summary,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (caseEval.passed) Color(0xFF059669) else Color(0xFFD97706)
                        )
                        caseEval.passedChecks.forEach { okMsg ->
                            Text("✓ $okMsg", fontSize = 11.sp, color = Color(0xFF059669))
                        }
                        caseEval.warnings.forEach { warnMsg ->
                            Text("⚠ $warnMsg", fontSize = 11.sp, color = MaterialTheme.colorScheme.error)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Kunci Klinis: ${activeCase.keyTakeaway}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (caseEval.passed && viewModel != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Button(
                                onClick = {
                                    viewModel.recordConsoleLabCompleted(activeCase.id)
                                    recordedCasesNote = "Praktik ${activeCase.title.substringBefore(":")} tercatat ke Shift Lab Harian!"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
                            ) {
                                Text("Catat Lulus Praktik Kasus (+1 Shift Lab)", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        recordedCasesNote?.let { note ->
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = note, fontSize = 11.sp, color = Color(0xFF059669), fontWeight = FontWeight.SemiBold)
                        }
                    }
                    // close block safely
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CtArtifactAndDailyQaSimulator() {
    var selectedMode by rememberSaveable { mutableIntStateOf(0) } // 0: Ring Artifact, 1: Beam Hardening, 2: Metal Streak, 3: Daily Water QA
    var airCalibrated by rememberSaveable { mutableStateOf(false) }
    var tubeWarmedUp by rememberSaveable { mutableStateOf(true) }
    var bhCorrectionActive by rememberSaveable { mutableStateOf(false) }
    var marActive by rememberSaveable { mutableStateOf(false) }
    var selectedRoiIdx by rememberSaveable { mutableIntStateOf(0) } // 0=Pusat, 1=Jam 12, 2=Jam 3, 3=Jam 6, 4=Jam 9

    // QA Water Phantom values depending on calibration & BHC state
    val centerHu = if (airCalibrated) 0.4f else 6.2f
    val periphHu12 = if (bhCorrectionActive) 1.2f else 8.8f
    val periphHu3 = if (bhCorrectionActive) -0.8f else 7.9f
    val periphHu6 = if (bhCorrectionActive) 1.0f else 8.5f
    val periphHu9 = if (bhCorrectionActive) -0.5f else 8.1f
    val maxUniformityDiff = listOf(periphHu12, periphHu3, periphHu6, periphHu9).maxOf { abs(it - centerHu) }
    val noiseSd = if (tubeWarmedUp && airCalibrated) 3.2f else 5.8f
    val qaPassed = abs(centerHu) <= 4.0f && maxUniformityDiff <= 5.0f && airCalibrated

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Simulator 5: Laboratorium Artefak, Air Calibration & QA Fantom Air",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Identifikasi penyebab fisik artefak CT, jalankan prosedur kalibrasi udara detektor, dan evaluasi uji QA Fantom Air 5-ROI harian.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        0 to "1. Ring Artifact",
                        1 to "2. Beam Hardening",
                        2 to "3. Metal Streak (MAR)",
                        3 to "4. Uji QA Fantom Air 5-ROI"
                    ).forEach { (idx, label) ->
                        FilterChip(
                            selected = selectedMode == idx,
                            onClick = { selectedMode = idx },
                            label = { Text(label, fontSize = 11.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Dual View Canvas: Left = Sinogram / Raw Detector Channels, Right = Reconstructed Axial CT Phantom
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF090D16))
                        .padding(10.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                        val w = size.width
                        val h = size.height

                        // Left pane: Sinogram representation (32% width)
                        val sinoW = w * 0.32f
                        drawRect(
                            color = Color(0xFF1E293B),
                            topLeft = Offset(0f, 0f),
                            size = Size(sinoW, h)
                        )
                        // Sinusoidal traces in Sinogram
                        for (k in 0..4) {
                            val p = Path()
                            val amp = sinoW * (0.15f + k * 0.05f)
                            val phase = k * 0.9f
                            for (yStep in 0..80) {
                                val yf = (yStep / 80f) * h
                                val xf = sinoW * 0.5f + amp * sin((yStep / 80f) * 2f * PI.toFloat() + phase)
                                if (yStep == 0) p.moveTo(xf, yf) else p.lineTo(xf, yf)
                            }
                            drawPath(p, color = Color(0xFF64748B), style = Stroke(width = 2f))
                        }

                        // Defective detector vertical line on sinogram if Ring Artifact not calibrated
                        if (!airCalibrated) {
                            drawLine(
                                color = Color(0xFFEF4444),
                                start = Offset(sinoW * 0.64f, 0f),
                                end = Offset(sinoW * 0.64f, h),
                                strokeWidth = 3f
                            )
                            drawLine(
                                color = Color(0xFFF59E0B),
                                start = Offset(sinoW * 0.38f, 0f),
                                end = Offset(sinoW * 0.38f, h),
                                strokeWidth = 2f
                            )
                        }

                        // Right pane: Reconstructed Axial CT Image
                        val imgCx = w * 0.67f
                        val imgCy = h * 0.5f
                        val r = minOf(w * 0.28f, h * 0.44f)

                        // Outer skull/phantom ring
                        drawCircle(
                            color = Color(0xFFE2E8F0),
                            radius = r,
                            center = Offset(imgCx, imgCy)
                        )
                        // Inner phantom water / parenchyma
                        drawCircle(
                            color = Color(0xFF475569),
                            radius = r * 0.88f,
                            center = Offset(imgCx, imgCy)
                        )

                        when (selectedMode) {
                            0 -> {
                                // Ring Artifact mode
                                if (!airCalibrated) {
                                    drawCircle(
                                        color = Color(0xFF0F172A),
                                        radius = r * 0.52f,
                                        center = Offset(imgCx, imgCy),
                                        style = Stroke(width = 4.5f)
                                    )
                                    drawCircle(
                                        color = Color(0xFFCBD5E1),
                                        radius = r * 0.28f,
                                        center = Offset(imgCx, imgCy),
                                        style = Stroke(width = 3f)
                                    )
                                }
                            }
                            1 -> {
                                // Beam Hardening (Petrous Bones + Dark Bands & Cupping)
                                drawCircle(
                                    color = Color.White,
                                    radius = r * 0.16f,
                                    center = Offset(imgCx - r * 0.52f, imgCy)
                                )
                                drawCircle(
                                    color = Color.White,
                                    radius = r * 0.16f,
                                    center = Offset(imgCx + r * 0.52f, imgCy)
                                )
                                if (!bhCorrectionActive) {
                                    // Dark cupping in center & Hounsfield dark band between dense bones
                                    drawCircle(
                                        color = Color(0xFF1E293B),
                                        radius = r * 0.46f,
                                        center = Offset(imgCx, imgCy)
                                    )
                                    drawRect(
                                        color = Color(0xFF090D16).copy(alpha = 0.85f),
                                        topLeft = Offset(imgCx - r * 0.36f, imgCy - 12f),
                                        size = Size(r * 0.72f, 24f)
                                    )
                                }
                            }
                            2 -> {
                                // Metal Implant with Starburst Streaks
                                val metalPos = Offset(imgCx - r * 0.25f, imgCy - r * 0.15f)
                                if (!marActive) {
                                    for (a in 0 until 12) {
                                        val ang = a * (PI.toFloat() / 6f)
                                        drawLine(
                                            color = if (a % 2 == 0) Color.White.copy(alpha = 0.75f) else Color.Black.copy(alpha = 0.85f),
                                            start = Offset(metalPos.x - r * 0.75f * cos(ang), metalPos.y - r * 0.75f * sin(ang)),
                                            end = Offset(metalPos.x + r * 0.75f * cos(ang), metalPos.y + r * 0.75f * sin(ang)),
                                            strokeWidth = 3.5f
                                        )
                                    }
                                }
                                drawCircle(
                                    color = Color(0xFF38BDF8),
                                    radius = 10f,
                                    center = metalPos
                                )
                            }
                            3 -> {
                                // 5-ROI Water Phantom QA Overlay (Center + 12, 3, 6, 9 o'clock)
                                if (!bhCorrectionActive) {
                                    drawCircle(
                                        color = Color(0xFF334155),
                                        radius = r * 0.5f,
                                        center = Offset(imgCx, imgCy)
                                    )
                                }
                                if (!airCalibrated) {
                                    drawCircle(
                                        color = Color(0xFF1E293B),
                                        radius = r * 0.42f,
                                        center = Offset(imgCx, imgCy),
                                        style = Stroke(width = 3f)
                                    )
                                }
                                val baseRoiColor = if (qaPassed) Color(0xFF4ADE80) else Color(0xFFF59E0B)
                                val roiPositions = listOf(
                                    Offset(imgCx, imgCy),
                                    Offset(imgCx, imgCy - r * 0.62f),
                                    Offset(imgCx + r * 0.62f, imgCy),
                                    Offset(imgCx, imgCy + r * 0.62f),
                                    Offset(imgCx - r * 0.62f, imgCy)
                                )
                                roiPositions.forEachIndexed { rIdx, pos ->
                                    val isSelRoi = selectedRoiIdx == rIdx
                                    drawCircle(
                                        color = if (isSelRoi) Color(0xFF38BDF8) else baseRoiColor,
                                        radius = if (isSelRoi) 15f else 12f,
                                        center = pos,
                                        style = Stroke(width = if (isSelRoi) 4f else 2.5f)
                                    )
                                }
                            }
                        }
                    }

                    Text(
                        text = if (airCalibrated) "Sinogram: Bersih" else "Sinogram: Garis Kanal Drift",
                        color = if (airCalibrated) Color(0xFF4ADE80) else Color(0xFFF87171),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.align(Alignment.BottomStart)
                    )
                    Text(
                        text = "Citra Aksial CT",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Interactive Action Controls
                Button(
                    onClick = {
                        tubeWarmedUp = true
                        airCalibrated = !airCalibrated
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (airCalibrated) Color(0xFF059669) else MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("btn_air_calibration")
                ) {
                    Text(
                        text = if (airCalibrated) {
                            "Air Calibration Aktif: Tabel Gain/Offset Detektor Seragam (Klik untuk Simulasi Drift)"
                        } else {
                            "Lakukan Fast Air Calibration & Koreksi Kanal Detektor"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Tube Warm-Up Pagi Hari (Anoda Siap):", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    Switch(checked = tubeWarmedUp, onCheckedChange = { tubeWarmedUp = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Koreksi Beam Hardening & Bow-Tie Filter:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    Switch(checked = bhCorrectionActive, onCheckedChange = { bhCorrectionActive = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Algoritma MAR (Metal Artifact Reduction):", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    Switch(checked = marActive, onCheckedChange = { marActive = it })
                }

                Spacer(modifier = Modifier.height(8.dp))

                // QA 5-ROI Report Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Hasil Uji Quality Assurance (QA) Fantom Air 5-ROI Harian:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val roiItems = listOf(
                        Triple(0, "ROI Pusat", centerHu),
                        Triple(1, "Jam 12", periphHu12),
                        Triple(2, "Jam 3", periphHu3),
                        Triple(3, "Jam 6", periphHu6),
                        Triple(4, "Jam 9", periphHu9)
                    )
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        roiItems.forEach { (rIdx, rLabel, rHu) ->
                            FilterChip(
                                selected = selectedRoiIdx == rIdx,
                                onClick = {
                                    selectedMode = 3
                                    selectedRoiIdx = rIdx
                                },
                                label = { Text("$rLabel (${String.format(Locale.US, "%+.1f", rHu)})", fontSize = 10.sp) }
                            )
                        }
                    }
                    val selEntry = roiItems.first { it.first == selectedRoiIdx }
                    Text(
                        text = "Inspektor ${selEntry.second}: Mean = ${String.format(Locale.US, "%+.1f", selEntry.third)} HU | SD = ±${String.format(Locale.US, "%.1f", noiseSd + selectedRoiIdx * 0.1f)} HU | Selisih vs Pusat = ${String.format(Locale.US, "%.1f", abs(selEntry.third - centerHu))} HU",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• ROI Pusat (Akurasi Air, Syarat 0 ± 4 HU): ${String.format(Locale.US, "%+.1f", centerHu)} HU\n" +
                            "• Selisih Maksimal ROI Perifer vs Pusat (Uniformity, Syarat ≤ 5 HU): ${String.format(Locale.US, "%.1f", maxUniformityDiff)} HU\n" +
                            "• Quantum Noise (SD Pusat): ±${String.format(Locale.US, "%.1f", noiseSd)} HU\n" +
                            "• Status Kelayakan Klinis: ${if (qaPassed) "LULUS UJI QA HARIAN (Siap Pelayanan Pasien)" else "TIDAK LULUS QA (Wajib Jalankan Air Calibration & Aktifkan BHC!)"}",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace,
                        color = if (qaPassed) Color(0xFF059669) else MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
