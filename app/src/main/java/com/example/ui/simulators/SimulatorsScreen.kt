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
import com.example.viewmodel.MriUiState
import com.example.viewmodel.MriViewModel
import com.example.viewmodel.SimType
import java.util.Locale

internal val ID_LOCALE: Locale = Locale.forLanguageTag("id-ID")

@Composable
fun SimulatorsScreen(
    state: MriUiState,
    viewModel: MriViewModel
) {
    val activeTabIndex = when (state.selectedSimType) {
        SimType.PESAWAT_MAGNET_M05, SimType.SPIN_LARMOR -> 0
        SimType.KONTRAS_TR_TE, SimType.FISIKA_DASAR_M01 -> 1
        SimType.RUANG_K, SimType.SEKUENS_KONTRAS_M03, SimType.GRADIEN_RF_M06 -> 2
        SimType.SAR_SAFETY, SimType.PROTOKOL_TA_SNR, SimType.KONSOL_VENDOR -> 3
        SimType.ARTEFAK_QA_M08 -> 4
    }

    SimulatorsScreen(
        selectedTabIndex = activeTabIndex,
        onSelectTab = { index ->
            val targetSimType = when (index) {
                0 -> SimType.PESAWAT_MAGNET_M05
                1 -> SimType.KONTRAS_TR_TE
                2 -> SimType.RUANG_K
                3 -> SimType.SAR_SAFETY
                else -> SimType.ARTEFAK_QA_M08
            }
            viewModel.selectSimType(targetSimType)
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SimulatorsScreen(
    selectedTabIndex: Int = 0,
    onSelectTab: (Int) -> Unit = {}
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

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTabIndex.coerceIn(0, 4)) {
            0 -> CtGantryAnatomyAndChainSimulator()
            1 -> CtHuWindowingAndBeerLambertSimulator()
            2 -> CtMsctPitchAndReconstructionSimulator()
            3 -> CtDosimetryAndBolusTrackingSimulator()
            4 -> CtArtifactAndDailyQaSimulator()
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
