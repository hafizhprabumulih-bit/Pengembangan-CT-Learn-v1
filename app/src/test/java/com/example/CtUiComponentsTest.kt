package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.data.CharacterCustomizationState
import com.example.data.CtQuizQuestion
import com.example.data.PracticeExamStats
import com.example.ui.arcade.MetalDefenseScreen
import com.example.ui.quiz.CtPracticeExamPanel
import com.example.ui.simulators.CtHuWindowingAndBeerLambertSimulator
import com.example.ui.theme.CtLearnTheme
import com.example.util.CtExamDomain
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CtUiComponentsTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun simulator2_presetsAndKvpChipsUpdateHuReadout() {
        composeRule.setContent {
            CtLearnTheme {
                CtHuWindowingAndBeerLambertSimulator(state = null, viewModel = null)
            }
        }

        composeRule.onNodeWithTag("slider_ct_ww").assertExists()
        composeRule.onNodeWithTag("slider_ct_wl").assertExists()
        composeRule.onNodeWithText("ROI @120kVp:", substring = true).assertIsDisplayed()

        composeRule.onNodeWithText("Lung (1500/-600)").performClick()
        composeRule.onNodeWithText("WW: 1500 HU | WL: -600 HU", substring = true).assertIsDisplayed()

        composeRule.onNodeWithText("80 kVp").performClick()
        composeRule.onNodeWithText("ROI @80kVp:", substring = true).assertIsDisplayed()
    }

    @Test
    fun practiceExamPanel_rendersDomainBarsAndCompletesTryOut() {
        val sampleQuizzes = listOf(
            CtQuizQuestion(
                id = "q_ui_1",
                moduleId = "m01",
                nodeCode = "1.1",
                question = "Nilai kalibrasi air murni pada skala Hounsfield Unit adalah?",
                options = listOf("0 HU", "-1000 HU", "+400 HU", "+1000 HU"),
                correctIndex = 0,
                explanation = "Air murni dikalibrasi tepat pada 0 HU."
            ),
            CtQuizQuestion(
                id = "q_ui_2",
                moduleId = "m09",
                nodeCode = "9.1",
                question = "Teknik utama mereduksi streak artifact dari implan logam?",
                options = listOf("Metal Artifact Reduction (MAR)", "Turunkan WW menjadi 10", "Matikan detektor", "Naikkan pitch ke 3.0"),
                correctIndex = 0,
                explanation = "MAR mereduksi distorsi atenuasi ekstrem pada logam."
            )
        )
        var examCallbackInvoked = false

        composeRule.setContent {
            CtLearnTheme {
                CtPracticeExamPanel(
                    allCurriculumQuizzes = sampleQuizzes,
                    attemptModuleOutcomes = listOf("m01" to true, "m09" to false),
                    examStats = PracticeExamStats(
                        completedCount = 1,
                        bestScorePercent = 80,
                        lastScorePercent = 80,
                        weakestDomainName = "Artefak & QA",
                        domainTotals = mapOf(
                            CtExamDomain.PHYSICS_HU to (4 to 5),
                            CtExamDomain.ARTIFACT_QA to (1 to 4)
                        )
                    ),
                    onExamCompleted = { _, _, _ -> examCallbackInvoked = true }
                )
            }
        }

        composeRule.onNodeWithTag("ct_tryout_panel").assertIsDisplayed()
        composeRule.onNodeWithText("Akurasi Per Domain UKOM", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("Mode Adaptif: Prioritas Domain Terlemah", substring = true).assertIsDisplayed()

        composeRule.onNodeWithTag("start_ct_tryout_button").performClick()
        composeRule.onNodeWithText("Selesai").assertIsDisplayed()
        composeRule.onNodeWithText("Selesai").performClick()

        composeRule.onNodeWithText("Hasil Try-Out UKOM CT Scan", substring = true).assertIsDisplayed()
        assertTrue("Callback onExamCompleted harus terpanggil setelah klik Selesai", examCallbackInvoked)
    }

    @Test
    fun metalDefenseScreen_rendersHudAndActivatesMarPowerUp() {
        composeRule.setContent {
            CtLearnTheme {
                MetalDefenseScreen(
                    characterCustomization = CharacterCustomizationState(),
                    bestScore = 150,
                    onGameFinished = { _, _ -> },
                    onBack = {}
                )
            }
        }

        composeRule.onNodeWithText("CT Metal Streak Defense").assertIsDisplayed()
        composeRule.onNodeWithText("SKOR").assertIsDisplayed()
        composeRule.onNodeWithText("COMBO").assertIsDisplayed()
        composeRule.onNodeWithText("MAR").assertIsDisplayed()

        composeRule.onNodeWithText("MAR").performClick()
        composeRule.onNodeWithText("MAR").assertDoesNotExist()
    }
}
