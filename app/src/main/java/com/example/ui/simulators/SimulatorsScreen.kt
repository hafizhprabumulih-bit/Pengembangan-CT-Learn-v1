package com.example.ui.simulators

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.CtSimType
import com.example.viewmodel.CtUiState
import com.example.viewmodel.CtViewModel
import java.util.Locale

internal val ID_LOCALE: Locale = Locale.forLanguageTag("id-ID")

@Composable
fun SimulatorsScreen(
    state: CtUiState,
    viewModel: CtViewModel
) {
    val activeTabIndex = when (state.selectedSimType) {
        CtSimType.GANTRY_RANTAI_SINYAL -> 0
        CtSimType.WINDOWING_HU_ATENUASI -> 1
        CtSimType.MSCT_PITCH_REKON -> 2
        CtSimType.DOSIMETRI_BOLUS_TRACKING -> 3
        CtSimType.ARTEFAK_KALIBRASI_QA -> 4
    }

    SimulatorsScreen(
        selectedTabIndex = activeTabIndex,
        onSelectTab = { index ->
            val targetSimType = when (index) {
                0 -> CtSimType.GANTRY_RANTAI_SINYAL
                1 -> CtSimType.WINDOWING_HU_ATENUASI
                2 -> CtSimType.MSCT_PITCH_REKON
                3 -> CtSimType.DOSIMETRI_BOLUS_TRACKING
                else -> CtSimType.ARTEFAK_KALIBRASI_QA
            }
            viewModel.selectSimType(targetSimType)
        },
        state = state,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SimulatorsScreen(
    selectedTabIndex: Int = 0,
    onSelectTab: (Int) -> Unit = {},
    state: CtUiState? = null,
    viewModel: CtViewModel? = null
) {
    val scrollState = rememberScrollState()
    val tabs = listOf(
        0 to "1. Gantry & Rantai Sinyal",
        1 to "2. Windowing HU & Atenuasi",
        2 to "3. MSCT, Pitch & Rekon",
        3 to "4. Dosimetri & Bolus Tracking",
        4 to "5. Artefak, Kalibrasi & QA"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("simulators_screen")
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Laboratorium Konsol & Instrumentasi Pesawat CT Scan",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Pilih satu dari 5 simulator interaktif di bawah ini untuk membedah cara kerja Gantry, skala Hounsfield Unit, geometri MSCT Helikal, dosimetri CTDIvol/DLP, hingga kalibrasi udara & uji QA.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    tabs.forEach { (index, title) ->
                        FilterChip(
                            selected = selectedTabIndex == index,
                            onClick = { onSelectTab(index) },
                            label = {
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.testTag("sim_tab_$index")
                        )
                    }
                }
            }
        }

        val focusBanner = when (state?.selectedCtActivityId) {
            "ct_m01_atenuasi" -> "Fokus Lab M01: Uji koefisien atenuasi Beer-Lambert, HVL & nilai HU."
            "ct_m02_anatomi_pesawat" -> "Fokus Lab M02: Eksplorasi Gantry Gen-3, Slip-Ring & Laser Isocenter."
            "ct_m03_instrumentasi_rantai" -> "Fokus Lab M03: Rantai sinyal Tabung Sinar-X, GOS Detektor & DAS."
            "ct_m04_generasi_pitch" -> "Fokus Lab M04: Simulasi Geometri MSCT Helikal & kurva SSP Pitch."
            "ct_m05_rekonstruksi" -> "Fokus Lab M05: Perbandingan Simple BP, FBP Ramp Filter & Iterative/DLIR."
            "ct_m06_hu_windowing" -> "Fokus Lab M06: Latihan preset Windowing (Brain/Stroke/Lung/Bone) & ROI."
            "ct_m07_dosimetri_kualitas" -> "Fokus Lab M07: Keseimbangan CTDIvol, SSDE, DLP & Quantum Noise SD."
            "ct_m08_kontras_3d" -> "Fokus Lab M08: Kurva Bolus Tracking Iodin & post-processing MPR/MIP/MinIP/VRT."
            "ct_m09_artefak_qa" -> "Fokus Lab M09: Mitigasi Ring/Beam Hardening/Metal Streak & Uji QA 5-ROI."
            else -> null
        }
        if (focusBanner != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.65f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = focusBanner,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTabIndex.coerceIn(0, 4)) {
            0 -> CtGantryAnatomyAndChainSimulator()
            1 -> CtHuWindowingAndBeerLambertSimulator(state = state, viewModel = viewModel)
            2 -> CtMsctPitchAndReconstructionSimulator(state = state, viewModel = viewModel)
            3 -> CtDosimetryAndBolusTrackingSimulator(state = state, viewModel = viewModel)
            4 -> CtArtifactAndDailyQaSimulator()
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
