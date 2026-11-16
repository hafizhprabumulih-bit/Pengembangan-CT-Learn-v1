package com.example.ui.simulators

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.geometry.CornerRadius
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
import kotlin.math.ln
import kotlin.math.roundToInt
import kotlin.math.sin

data class GantryComponentSpec(
    val id: Int,
    val shortLabel: String,
    val title: String,
    val locationDesc: String,
    val technicalSpec: String,
    val clinicalRole: String,
    val badgeColor: Color
)

private val gantryComponents = listOf(
    GantryComponentSpec(
        id = 0,
        shortLabel = "1. Tabung Sinar-X",
        title = "X-Ray Tube Assembly & Rotating Anode",
        locationDesc = "Posisi Puncak Rangka Putar (Berhadapan 180° dengan Busur Detektor)",
        technicalSpec = "Anoda Putar W-Re-Mo-Graphite 5–8+ MHU | Dual Focal Spot (0,6 / 1,2 mm) & z-Flying Focal Spot | Pendingin Oli Heat Exchanger.",
        clinicalRole = "Menghasilkan berkas foton sinar-X intensitas tinggi secara kontinu selama pemindaian helikal tanpa mengalami panas berlebih (overheating).",
        badgeColor = Color(0xFFEF4444)
    ),
    GantryComponentSpec(
        id = 1,
        shortLabel = "2. HV Generator",
        title = "High-Frequency Inverter HV Generator",
        locationDesc = "Terpasang di Rangka Putar Gantry (Posisi Jam 9–10 sebagai Penyeimbang)",
        technicalSpec = "Inverter Frekuensi Tinggi 10–100 kHz | Output Stabil 80, 100, 120, 140 kVp | Daya 60–120 kW | Ripple Tegangan < 1%.",
        clinicalRole = "Mengubah tegangan AC dari Slip-Ring menjadi tegangan DC tinggi (kVp) langsung di atas drum putar sehingga tidak membutuhkan kabel tegangan tinggi.",
        badgeColor = Color(0xFFF59E0B)
    ),
    GantryComponentSpec(
        id = 2,
        shortLabel = "3. Bow-Tie & Kolimator",
        title = "Bow-Tie Filter & Pre-Patient Collimator",
        locationDesc = "Di Antara Jendela Tabung Sinar-X dan Lubang Bore Pasien",
        technicalSpec = "Filter bentuk dasi kupu-kupu (Teflon/Al/Cu: tipis di pusat, tebal di tepi) + Bilah Tungsten bermotor pembatas lebar berkas z-axis (W).",
        clinicalRole = "Menyerap foton energi rendah di tepi tubuh yang tipis, mengurangi dosis kulit perifer hingga 50%, dan mencegah over-beaming.",
        badgeColor = Color(0xFF10B981)
    ),
    GantryComponentSpec(
        id = 3,
        shortLabel = "4. Laser Isocenter & Meja",
        title = "Laser Positioning 3-Sumbu & Meja Serat Karbon",
        locationDesc = "Pusat Bore Gantry (Aperture 70–85 cm) & Patient Couch",
        technicalSpec = "Laser Aksial, Koronal (ketinggian vertikal), & Sagital | Meja Carbon-Fiber atenuasi rendah | Akurasi motor z-axis ±0,25 mm, beban maks 200–300 kg.",
        clinicalRole = "Menempatkan pusat organ target tepat pada Isocenter Gantry agar sejajar dengan bagian tengah Bow-Tie Filter dan kalkulasi mA pada AEC akurat.",
        badgeColor = Color(0xFFEC4899)
    ),
    GantryComponentSpec(
        id = 4,
        shortLabel = "5. Slip-Ring",
        title = "Cincin Luncur Elektromekanis & Link Optik (Slip-Ring)",
        locationDesc = "Lingkar Luar Antarmuka Rangka Statis dan Rangka Putar Gantry",
        technicalSpec = "Cincin konduktif paduan perak-grafit + sikat penyapu (brush) / induktif nirkabel + Transmisi data optik laser multi-Gbps.",
        clinicalRole = "Menyalurkan daya listrik menuju HV Generator & Tabung serta mengirimkan data sinogram DAS keluar dari gantry sambil berputar 360° tanpa henti.",
        badgeColor = Color(0xFF8B5CF6)
    ),
    GantryComponentSpec(
        id = 5,
        shortLabel = "6. Busur Detektor",
        title = "Solid-State Scintillation Detector Array (MSCT)",
        locationDesc = "Posisi Bawah Rangka Putar (Berhadapan 180° dengan Tabung Sinar-X)",
        technicalSpec = "800–900 elemen/baris × 64–320 baris (z-axis) | Kristal Sintilator Keramik Ultra-Fast (Gd2O2S / GOS) + Fotodioda Silikon + Septa Tungsten Anti-Scatter.",
        clinicalRole = "Menangkap foton sinar-X transmisi (DQE ~98%) dan mengubahnya menjadi cahaya tampak lalu menjadi arus listrik proporsional dengan afterglow < 10 mikrodetik.",
        badgeColor = Color(0xFF06B6D4)
    ),
    GantryComponentSpec(
        id = 6,
        shortLabel = "7. Modul DAS",
        title = "Data Acquisition System (DAS) & Logarithmic ADC",
        locationDesc = "Tepat di Belakang Busur Detektor pada Rangka Putar",
        technicalSpec = "Integrator Arus Rendah Derau + Penguat Logaritmik ln(I0/I) + ADC 16–24 bit | Kecepatan sampling 1.000–2.500 proyeksi (views) per rotasi.",
        clinicalRole = "Mendigitalkan sinyal ribuan kanal detektor dan menghitung integral atenuasi garis ln(I0/I) sebagai baris data mentah Sinogram.",
        badgeColor = Color(0xFF3B82F6)
    )
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CtGantryAnatomyAndChainSimulator() {
    var coverOpened by rememberSaveable { mutableStateOf(true) }
    var gantryRotating by rememberSaveable { mutableStateOf(true) }
    var selectedComponentId by rememberSaveable { mutableIntStateOf(0) }
    var tableOffsetCm by rememberSaveable { mutableFloatStateOf(0f) } // -6 cm to +6 cm vertical off-centering

    val infiniteTransition = rememberInfiniteTransition(label = "gantry_rot")
    val animAngleDeg by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gantry_angle"
    )
    val effectiveAngleDeg = if (gantryRotating) animAngleDeg else 0f

    val absOffset = abs(tableOffsetCm)
    val noisePenaltyPct = (absOffset * absOffset * 1.4f + absOffset * 2.5f).roundToInt()
    val aecDoseErrorPct = (tableOffsetCm * 6.5f).roundToInt()
    val selectedSpec = gantryComponents[selectedComponentId]

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Simulator 1: Bedah Anatomi Gantry & Rantai Instrumentasi CT",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Buka penutup depan (front cover) Gantry untuk membedah komponen internal rangka putar, alur konversi sinyal, dan efek ketinggian meja terhadap Isocenter.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = coverOpened,
                            onCheckedChange = { coverOpened = it },
                            modifier = Modifier.testTag("toggle_gantry_cover")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (coverOpened) "Cover Gantry Terbuka (Internal)" else "Cover Gantry Tertutup (Eksternal)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    FilterChip(
                        selected = gantryRotating,
                        onClick = { gantryRotating = !gantryRotating },
                        label = { Text(if (gantryRotating) "Rotasi: ON" else "Rotasi: PAUSE", fontSize = 11.sp) }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Interactive Gantry Cross-Section Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0F172A))
                        .padding(8.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxWidth().height(244.dp)) {
                        val cx = size.width / 2f
                        val cy = size.height / 2f
                        val outerR = size.minDimension * 0.45f
                        val slipRingR = outerR * 0.86f
                        val boreR = outerR * 0.42f

                        // Outer Gantry Housing
                        drawCircle(
                            color = Color(0xFF1E293B),
                            radius = outerR,
                            center = Offset(cx, cy)
                        )
                        drawCircle(
                            color = Color(0xFF475569),
                            radius = outerR,
                            center = Offset(cx, cy),
                            style = Stroke(width = 4f)
                        )

                        if (coverOpened) {
                            // Slip-Ring conductive tracks
                            drawCircle(
                                color = if (selectedComponentId == 4) Color(0xFFA855F7) else Color(0xFF64748B),
                                radius = slipRingR,
                                center = Offset(cx, cy),
                                style = Stroke(
                                    width = if (selectedComponentId == 4) 6f else 3f,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
                                )
                            )

                            val rad = (effectiveAngleDeg - 90f) * (PI.toFloat() / 180f)
                            val tubeDist = (slipRingR + boreR) * 0.54f
                            val tubePos = Offset(
                                cx + tubeDist * cos(rad),
                                cy + tubeDist * sin(rad)
                            )

                            // HV Generator at -90 deg offset on rotating frame
                            val hvRad = rad - (PI.toFloat() * 0.55f)
                            val hvPos = Offset(
                                cx + tubeDist * cos(hvRad),
                                cy + tubeDist * sin(hvRad)
                            )
                            drawCircle(
                                color = if (selectedComponentId == 1) Color(0xFFF59E0B) else Color(0xFFD97706).copy(alpha = 0.75f),
                                radius = 18f,
                                center = hvPos
                            )

                            // Heat Exchanger at +90 deg offset on rotating frame
                            val coolRad = rad + (PI.toFloat() * 0.55f)
                            val coolPos = Offset(
                                cx + tubeDist * cos(coolRad),
                                cy + tubeDist * sin(coolRad)
                            )
                            drawCircle(
                                color = Color(0xFF38BDF8).copy(alpha = 0.65f),
                                radius = 15f,
                                center = coolPos
                            )

                            // Bow-Tie Filter & Pre-Patient Collimator near tube toward center
                            val bowTiePos = Offset(
                                cx + (boreR * 1.12f) * cos(rad),
                                cy + (boreR * 1.12f) * sin(rad)
                            )
                            drawCircle(
                                color = if (selectedComponentId == 2) Color(0xFF10B981) else Color(0xFF059669),
                                radius = 11f,
                                center = bowTiePos
                            )

                            // X-Ray Fan Beam from Tube to Detector Arc
                            val detArcCenterAngle = effectiveAngleDeg + 90f
                            val fanHalfAngle = 28f
                            val detLeftRad = (detArcCenterAngle - fanHalfAngle - 90f) * (PI.toFloat() / 180f)
                            val detRightRad = (detArcCenterAngle + fanHalfAngle - 90f) * (PI.toFloat() / 180f)
                            val detR = tubeDist * 0.96f
                            val fanPath = Path().apply {
                                moveTo(tubePos.x, tubePos.y)
                                lineTo(cx + detR * cos(detLeftRad), cy + detR * sin(detLeftRad))
                                lineTo(cx + detR * cos(detRightRad), cy + detR * sin(detRightRad))
                                close()
                            }
                            drawPath(
                                path = fanPath,
                                color = Color(0xFF38BDF8).copy(alpha = 0.18f)
                            )

                            // X-Ray Tube Assembly
                            drawCircle(
                                color = if (selectedComponentId == 0) Color(0xFFEF4444) else Color(0xFFDC2626),
                                radius = 20f,
                                center = tubePos
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 5f,
                                center = tubePos
                            )

                            // Detector Array Arc (opposite 180 deg)
                            drawArc(
                                color = if (selectedComponentId == 5) Color(0xFF06B6D4) else Color(0xFF0891B2),
                                startAngle = detArcCenterAngle - fanHalfAngle - 90f,
                                sweepAngle = fanHalfAngle * 2f,
                                useCenter = false,
                                topLeft = Offset(cx - detR, cy - detR),
                                size = Size(detR * 2f, detR * 2f),
                                style = Stroke(width = 10f, cap = StrokeCap.Round)
                            )

                            // DAS behind Detector Arc
                            val dasR = detR + 12f
                            drawArc(
                                color = if (selectedComponentId == 6) Color(0xFF3B82F6) else Color(0xFF1D4ED8),
                                startAngle = detArcCenterAngle - (fanHalfAngle * 0.85f) - 90f,
                                sweepAngle = fanHalfAngle * 1.7f,
                                useCenter = false,
                                topLeft = Offset(cx - dasR, cy - dasR),
                                size = Size(dasR * 2f, dasR * 2f),
                                style = Stroke(width = 6f, cap = StrokeCap.Round)
                            )
                        } else {
                            // Closed External Cover aesthetic rings & control panels
                            drawCircle(
                                color = Color(0xFF334155),
                                radius = outerR * 0.92f,
                                center = Offset(cx, cy)
                            )
                            drawRoundRect(
                                color = Color(0xFF0EA5E9),
                                topLeft = Offset(cx - outerR * 0.72f, cy - 22f),
                                size = Size(24f, 44f),
                                cornerRadius = CornerRadius(6f, 6f)
                            )
                            drawRoundRect(
                                color = Color(0xFF0EA5E9),
                                topLeft = Offset(cx + outerR * 0.72f - 24f, cy - 22f),
                                size = Size(24f, 44f),
                                cornerRadius = CornerRadius(6f, 6f)
                            )
                        }

                        // Central Bore Aperture (70-85 cm)
                        drawCircle(
                            color = Color(0xFF090D16),
                            radius = boreR,
                            center = Offset(cx, cy)
                        )
                        drawCircle(
                            color = Color(0xFF64748B),
                            radius = boreR,
                            center = Offset(cx, cy),
                            style = Stroke(width = 2.5f)
                        )

                        // Patient Couch & Phantom shifted by tableOffsetCm
                        val shiftPx = (tableOffsetCm / 6f) * (boreR * 0.42f)
                        val patientY = cy - shiftPx

                        // Carbon Fiber Table Cradle
                        drawRoundRect(
                            color = Color(0xFF475569),
                            topLeft = Offset(cx - boreR * 0.55f, patientY + boreR * 0.22f),
                            size = Size(boreR * 1.1f, 8f),
                            cornerRadius = CornerRadius(4f, 4f)
                        )
                        // Patient Body Ellipse
                        drawOval(
                            color = Color(0xFF94A3B8).copy(alpha = 0.85f),
                            topLeft = Offset(cx - boreR * 0.48f, patientY - boreR * 0.24f),
                            size = Size(boreR * 0.96f, boreR * 0.48f)
                        )

                        // Laser Isocenter Crosshair (Red Sagittal & Coronal lines intersecting at true Isocenter)
                        val laserColor = if (absOffset < 0.3f) Color(0xFF22C55E) else Color(0xFFEF4444)
                        drawLine(
                            color = laserColor,
                            start = Offset(cx - boreR, cy),
                            end = Offset(cx + boreR, cy),
                            strokeWidth = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                        )
                        drawLine(
                            color = laserColor,
                            start = Offset(cx, cy - boreR),
                            end = Offset(cx, cy + boreR),
                            strokeWidth = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                        )
                    }

                    Text(
                        text = if (absOffset < 0.3f) "ISOCENTER TEPAT (0,0 cm)" else "OFF-CENTER: ${String.format(Locale.US, "%+.1f", tableOffsetCm)} cm",
                        color = if (absOffset < 0.3f) Color(0xFF4ADE80) else Color(0xFFF87171),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.align(Alignment.TopStart).padding(6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Klik Komponen Gantry untuk Membedah Cara Kerja:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    gantryComponents.forEach { comp ->
                        FilterChip(
                            selected = selectedComponentId == comp.id,
                            onClick = { selectedComponentId = comp.id },
                            label = { Text(comp.shortLabel, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = comp.badgeColor.copy(alpha = 0.2f),
                                selectedLabelColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Selected Component Detail Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                        .border(1.dp, selectedSpec.badgeColor.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = selectedSpec.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = selectedSpec.badgeColor
                    )
                    Text(
                        text = "Lokasi: ${selectedSpec.locationDesc}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Spesifikasi: ${selectedSpec.technicalSpec}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Peran Instrumentasi: ${selectedSpec.clinicalRole}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Rantai Konversi Sinyal (Signal Chain)
                Text(
                    text = "Rantai Konversi Energi & Sinyal Akuisisi CT:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Listrik (Slip-Ring & HV Gen) → Foton Sinar-X (Tabung) → Cahaya Tampak (Sintilator GOS) → Arus Listrik (Fotodioda) → Data Digital ln(I0/I) (DAS) → Sinogram",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f))
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Table Vertical Height Off-Centering Control
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Ketinggian Vertikal Meja (Laser Isocenter):",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${String.format(Locale.US, "%+.1f", tableOffsetCm)} cm",
                        style = MaterialTheme.typography.labelMedium,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
                Slider(
                    value = tableOffsetCm,
                    onValueChange = { tableOffsetCm = it },
                    valueRange = -6f..6f,
                    modifier = Modifier.testTag("slider_table_isocenter")
                )
                Text(
                    text = if (absOffset < 0.5f) {
                        "Status Isocenter Optimal: Tubuh pasien tepat di pusat Bow-Tie Filter. Noise tambahan +0%, akurasi modulasi mA AEC optimal."
                    } else {
                        "Peringatan Off-Centering (${String.format(Locale.US, "%+.1f", tableOffsetCm)} cm): Bagian tebal pasien meleset dari tengah Bow-Tie Filter! Penalti Quantum Noise +$noisePenaltyPct% & deviasi estimasi mA AEC ${String.format(Locale.US, "%+d", aecDoseErrorPct)}%."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = if (absOffset < 0.5f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private data class WindowPreset(
    val name: String,
    val ww: Float,
    val wl: Float,
    val clinicalNote: String
)

private val clinicalWindowPresets = listOf(
    WindowPreset("Brain (80/40)", 80f, 40f, "WW sempit untuk memisahkan substansia grisea (+40 HU) & alba (+28 HU) serta deteksi perdarahan akut (+75 HU)."),
    WindowPreset("Stroke (40/40)", 40f, 40f, "Kontras ultra-tinggi (+20 s.d. +60 HU) untuk menonjolkan hipodensitas infark iskemik hiperakut (+18 HU)."),
    WindowPreset("Abdomen (350/40)", 350f, 40f, "Rentang sedang (-135 s.d. +215 HU) untuk parenkim organ lunak, otot, dan pembuluh darah berkontras."),
    WindowPreset("Lung (1500/-600)", 1500f, -600f, "Rentang lebar berpusat di densitas paru (-1350 s.d. +150 HU) untuk melihat parenkim dan vaskular paru."),
    WindowPreset("Bone (2000/500)", 2000f, 500f, "Window sangat lebar (-500 s.d. +1500 HU) untuk mengevaluasi korteks (+1400 HU) dan trabekula tulang.")
)

private fun huToGrayscaleColor(hu: Float, ww: Float, wl: Float): Color {
    val minHu = wl - (ww / 2f)
    val maxHu = wl + (ww / 2f)
    val norm = ((hu - minHu) / (maxHu - minHu)).coerceIn(0f, 1f)
    return Color(norm, norm, norm)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CtHuWindowingAndBeerLambertSimulator(
    state: CtUiState? = null,
    viewModel: CtViewModel? = null
) {
    var ww by rememberSaveable { mutableFloatStateOf(state?.windowWidthHu ?: 80f) }
    var wl by rememberSaveable { mutableFloatStateOf(state?.windowLevelHu ?: 40f) }
    var selectedPresetName by rememberSaveable { mutableStateOf("Brain (80/40)") }
    var sliceIndex by rememberSaveable { mutableIntStateOf(0) } // 0: Kepala, 1: Toraks, 2: Abdomen

    LaunchedEffect(state?.windowWidthHu, state?.windowLevelHu) {
        if (state != null) {
            ww = state.windowWidthHu
            wl = state.windowLevelHu
            val matched = clinicalWindowPresets.firstOrNull {
                kotlin.math.abs(it.ww - state.windowWidthHu) < 2f && kotlin.math.abs(it.wl - state.windowLevelHu) < 2f
            }
            if (matched != null) selectedPresetName = matched.name
        }
    }

    // Beer-Lambert & HU Interactive Calculator state
    var muTissue by rememberSaveable { mutableFloatStateOf(0.210f) } // cm^-1 (Water = 0.200 cm^-1)
    var thicknessCm by rememberSaveable { mutableFloatStateOf(10f) }

    val calculatedHu = CtPhysicsCalculator.calculateHu(muTissue).toFloat()
    val transmittedRatio = CtPhysicsCalculator.beerLambertTransmission(muTissue, thicknessCm)
    val transmittedPct = transmittedRatio * 100f
    val hvlCm = CtPhysicsCalculator.halfValueLayerCm(muTissue)
    val sinogramRaySum = CtPhysicsCalculator.sinogramRaySum(transmittedRatio)

    val minDisplayedHu = (wl - ww / 2f).roundToInt()
    val maxDisplayedHu = (wl + ww / 2f).roundToInt()

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Simulator 2: Windowing HU (WW/WL) & Atenuasi Beer-Lambert",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Uji bagaimana pengaturan Window Width (WW) dan Window Level (WL) mengubah tampilan struktur anatomi pada fantom CT multi-jaringan.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    clinicalWindowPresets.forEach { preset ->
                        FilterChip(
                            selected = selectedPresetName == preset.name,
                            onClick = {
                                selectedPresetName = preset.name
                                ww = preset.ww
                                wl = preset.wl
                                viewModel?.setWindowing(preset.ww, preset.wl)
                            },
                            label = { Text(preset.name, fontSize = 11.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Spacer(modifier = Modifier.height(6.dp))

                // Multi-Slice Anatomy Level Selector
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        0 to "Potongan 1: Kepala (Stroke/Hematoma)",
                        1 to "Potongan 2: Toraks (Paru/Aorta)",
                        2 to "Potongan 3: Abdomen (Hepar/Kista)"
                    ).forEach { (idx, label) ->
                        FilterChip(
                            selected = sliceIndex == idx,
                            onClick = { sliceIndex = idx },
                            label = { Text(label, fontSize = 10.5.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Multi-Tissue CT Phantom Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black)
                        .padding(8.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxWidth().height(214.dp)) {
                        val cx = size.width / 2f
                        val cy = size.height / 2f
                        val rMax = size.minDimension * 0.44f

                        // Air background (-1000 HU)
                        drawRect(color = huToGrayscaleColor(-1000f, ww, wl))

                        if (sliceIndex == 0) {
                        // Subcutaneous Fat Rim (-80 HU)
                        drawCircle(
                            color = huToGrayscaleColor(-80f, ww, wl),
                            radius = rMax,
                            center = Offset(cx, cy)
                        )

                        // Cranial Cortical Bone Skull Ring (+1400 HU)
                        drawCircle(
                            color = huToGrayscaleColor(1400f, ww, wl),
                            radius = rMax * 0.92f,
                            center = Offset(cx, cy)
                        )

                        // Diploe / Trabecular Bone (+350 HU)
                        drawCircle(
                            color = huToGrayscaleColor(350f, ww, wl),
                            radius = rMax * 0.84f,
                            center = Offset(cx, cy),
                            style = Stroke(width = 5f)
                        )

                        // Cerebral Gray Matter Cortex (+40 HU)
                        drawCircle(
                            color = huToGrayscaleColor(40f, ww, wl),
                            radius = rMax * 0.78f,
                            center = Offset(cx, cy)
                        )

                        // Cerebral White Matter (+28 HU)
                        drawCircle(
                            color = huToGrayscaleColor(28f, ww, wl),
                            radius = rMax * 0.58f,
                            center = Offset(cx, cy)
                        )

                        // Frontal Air Sinus (-950 HU) & Lung-like aerated cell (-780 HU)
                        drawCircle(
                            color = huToGrayscaleColor(-950f, ww, wl),
                            radius = rMax * 0.14f,
                            center = Offset(cx, cy - rMax * 0.72f)
                        )

                        // Lateral Ventricles CSF (+10 HU)
                        drawOval(
                            color = huToGrayscaleColor(10f, ww, wl),
                            topLeft = Offset(cx - rMax * 0.24f, cy - rMax * 0.18f),
                            size = Size(rMax * 0.18f, rMax * 0.36f)
                        )
                        drawOval(
                            color = huToGrayscaleColor(10f, ww, wl),
                            topLeft = Offset(cx + rMax * 0.06f, cy - rMax * 0.18f),
                            size = Size(rMax * 0.18f, rMax * 0.36f)
                        )

                        // Acute Epidural/Parenchymal Hematoma (+75 HU) on Left
                        drawCircle(
                            color = huToGrayscaleColor(75f, ww, wl),
                            radius = rMax * 0.17f,
                            center = Offset(cx - rMax * 0.42f, cy + rMax * 0.12f)
                        )

                        // Hyperacute Ischemic Stroke Lesion (+18 HU) on Right
                        drawCircle(
                            color = huToGrayscaleColor(18f, ww, wl),
                            radius = rMax * 0.19f,
                            center = Offset(cx + rMax * 0.40f, cy + rMax * 0.10f)
                        )
                        } else if (sliceIndex == 1) {
                            // Slice 1: Toraks (Paru, Nodul, Aorta, Trakea, Vertebra)
                            drawOval(color = huToGrayscaleColor(-85f, ww, wl), topLeft = Offset(cx - rMax * 1.15f, cy - rMax * 0.85f), size = Size(rMax * 2.3f, rMax * 1.7f))
                            drawOval(color = huToGrayscaleColor(45f, ww, wl), topLeft = Offset(cx - rMax * 1.05f, cy - rMax * 0.76f), size = Size(rMax * 2.1f, rMax * 1.52f))
                            drawOval(color = huToGrayscaleColor(-750f, ww, wl), topLeft = Offset(cx - rMax * 0.92f, cy - rMax * 0.58f), size = Size(rMax * 0.76f, rMax * 1.16f))
                            drawOval(color = huToGrayscaleColor(-750f, ww, wl), topLeft = Offset(cx + rMax * 0.16f, cy - rMax * 0.58f), size = Size(rMax * 0.76f, rMax * 1.16f))
                            drawCircle(color = huToGrayscaleColor(35f, ww, wl), radius = rMax * 0.13f, center = Offset(cx + rMax * 0.54f, cy - rMax * 0.12f))
                            drawCircle(color = huToGrayscaleColor(320f, ww, wl), radius = rMax * 0.16f, center = Offset(cx + rMax * 0.05f, cy + rMax * 0.15f))
                            drawCircle(color = huToGrayscaleColor(-1000f, ww, wl), radius = rMax * 0.11f, center = Offset(cx, cy - rMax * 0.18f))
                            drawCircle(color = huToGrayscaleColor(1200f, ww, wl), radius = rMax * 0.20f, center = Offset(cx, cy + rMax * 0.52f))
                        } else {
                            // Slice 2: Abdomen (Hepar, Kista, Gas Lambung, Aorta, Vertebra)
                            drawOval(color = huToGrayscaleColor(-90f, ww, wl), topLeft = Offset(cx - rMax * 1.15f, cy - rMax * 0.85f), size = Size(rMax * 2.3f, rMax * 1.7f))
                            drawOval(color = huToGrayscaleColor(42f, ww, wl), topLeft = Offset(cx - rMax * 1.02f, cy - rMax * 0.74f), size = Size(rMax * 2.04f, rMax * 1.48f))
                            drawOval(color = huToGrayscaleColor(58f, ww, wl), topLeft = Offset(cx - rMax * 0.90f, cy - rMax * 0.52f), size = Size(rMax * 0.88f, rMax * 0.85f))
                            drawCircle(color = huToGrayscaleColor(8f, ww, wl), radius = rMax * 0.15f, center = Offset(cx - rMax * 0.50f, cy - rMax * 0.12f))
                            drawCircle(color = huToGrayscaleColor(-980f, ww, wl), radius = rMax * 0.24f, center = Offset(cx + rMax * 0.40f, cy - rMax * 0.24f))
                            drawCircle(color = huToGrayscaleColor(52f, ww, wl), radius = rMax * 0.22f, center = Offset(cx + rMax * 0.68f, cy + rMax * 0.18f))
                            drawCircle(color = huToGrayscaleColor(260f, ww, wl), radius = rMax * 0.13f, center = Offset(cx + rMax * 0.06f, cy + rMax * 0.12f))
                            drawCircle(color = huToGrayscaleColor(1100f, ww, wl), radius = rMax * 0.20f, center = Offset(cx, cy + rMax * 0.48f))
                        }
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp)
                    ) {
                        Text(
                            text = "WW: ${ww.roundToInt()} HU | WL: ${wl.roundToInt()} HU",
                            color = Color(0xFF38BDF8),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Rentang Abu-Abu: [$minDisplayedHu s.d. $maxDisplayedHu HU]",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = when (sliceIndex) {
                        0 -> "Legenda Kepala: Tulang (+1400 HU) • Hematoma Kiri (+75 HU) • Korteks (+40 HU) • Alba (+28 HU) • Infark Kanan (+18 HU) • CSF (+10 HU) • Sinus (-950 HU)"
                        1 -> "Legenda Toraks: Vertebra (+1200 HU) • Aorta Kontras (+320 HU) • Mediastinum (+45 HU) • Nodul Paru (+35 HU) • Paru (-750 HU) • Trakea (-1000 HU)"
                        else -> "Legenda Abdomen: Vertebra (+1100 HU) • Aorta (+260 HU) • Hepar (+58 HU) • Kista (+8 HU) • Lemak (-90 HU) • Gas Lambung (-980 HU)"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Sliders for WW & WL
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Window Width (WW - Kontras):", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                    Text("${ww.roundToInt()} HU", style = MaterialTheme.typography.labelMedium, fontFamily = FontFamily.Monospace)
                }
                Slider(
                    value = ww,
                    onValueChange = {
                        ww = it
                        selectedPresetName = "Kustom"
                        viewModel?.setWindowing(ww, wl)
                    },
                    valueRange = 20f..2500f,
                    modifier = Modifier.testTag("slider_ct_ww")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Window Level (WL - Kecerahan Pusat):", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                    Text("${wl.roundToInt()} HU", style = MaterialTheme.typography.labelMedium, fontFamily = FontFamily.Monospace)
                }
                Slider(
                    value = wl,
                    onValueChange = {
                        wl = it
                        selectedPresetName = "Kustom"
                        viewModel?.setWindowing(ww, wl)
                    },
                    valueRange = -800f..800f,
                    modifier = Modifier.testTag("slider_ct_wl")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Beer-Lambert & HU Calculator Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Kalkulator Fisika Atenuasi Beer-Lambert & Konversi HU (mu_air = 0,200 cm⁻¹)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Koefisien Atenuasi Jaringan (mu):", style = MaterialTheme.typography.bodySmall)
                        Text(
                            "${String.format(Locale.US, "%.3f", muTissue)} cm⁻¹  →  ${calculatedHu.roundToInt()} HU",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Slider(
                        value = muTissue,
                        onValueChange = { muTissue = it },
                        valueRange = 0.020f..0.500f,
                        colors = SliderDefaults.colors(thumbColor = MaterialTheme.colorScheme.secondary)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Ketebalan Lintasan Jaringan (x):", style = MaterialTheme.typography.bodySmall)
                        Text("${String.format(Locale.US, "%.1f", thicknessCm)} cm", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                    }
                    Slider(
                        value = thicknessCm,
                        onValueChange = { thicknessCm = it },
                        valueRange = 1f..25f
                    )

                    Text(
                        text = "Transmisi I/I0 = ${String.format(Locale.US, "%.1f", transmittedPct)}% | HVL = ${String.format(Locale.US, "%.2f", hvlCm)} cm | Sinyal DAS ln(I0/I) = ${String.format(Locale.US, "%.2f", sinogramRaySum)}",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
