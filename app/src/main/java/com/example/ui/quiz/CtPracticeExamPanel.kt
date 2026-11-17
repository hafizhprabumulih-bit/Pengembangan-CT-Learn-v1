package com.example.ui.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CtQuizQuestion
import com.example.data.PracticeExamStats
import com.example.ui.theme.AmberDark
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CardSlate
import com.example.ui.theme.CardSlateBorder
import com.example.ui.theme.CyanBright
import com.example.ui.theme.CyanDark
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.DarkNavySlate
import com.example.ui.theme.DeepObsidian
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.RoseDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.CtExamQuestion
import com.example.util.CtPracticeExamEngine
import com.example.util.rememberCtHaptics
import kotlinx.coroutines.delay
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CtPracticeExamPanel(
    allCurriculumQuizzes: List<CtQuizQuestion>,
    attemptModuleOutcomes: List<Pair<String, Boolean>> = emptyList(),
    examStats: PracticeExamStats = PracticeExamStats(),
    onExamCompleted: ((Int, String?, List<com.example.util.CtDomainScore>) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val haptics = rememberCtHaptics()
    var questionCount by rememberSaveable { mutableIntStateOf(20) }
    var useTimer by rememberSaveable { mutableStateOf(true) }
    var useAdaptiveMode by rememberSaveable { mutableStateOf(false) }
    var examSeed by rememberSaveable { mutableIntStateOf(1) }
    var isExamStarted by rememberSaveable { mutableStateOf(false) }
    var isExamSubmitted by rememberSaveable { mutableStateOf(false) }
    var currentIdx by rememberSaveable { mutableIntStateOf(0) }
    var remainingSeconds by rememberSaveable { mutableIntStateOf(20 * 60) }
    var recordedSubmissionSeed by rememberSaveable { mutableIntStateOf(0) }
    val selectedAnswers = rememberSaveable(
        examSeed,
        questionCount,
        saver = mapSaver(
            save = { map -> map.toMap() },
            restore = { restored ->
                mutableStateMapOf<String, Int>().apply {
                    restored.forEach { (k, v) -> (v as? Int)?.let { put(k, it) } }
                }
            }
        )
    ) { mutableStateMapOf<String, Int>() }

    val sessionQuestions: List<CtExamQuestion> = remember(allCurriculumQuizzes, questionCount, examSeed, useAdaptiveMode, attemptModuleOutcomes.size, examStats.completedCount) {
        if (useAdaptiveMode) {
            CtPracticeExamEngine.buildAdaptiveWeakestSession(
                questionCount = questionCount,
                curriculumQuizzes = allCurriculumQuizzes,
                attemptModuleOutcomes = attemptModuleOutcomes,
                tryoutDomainTotals = examStats.domainTotals,
                seed = examSeed
            )
        } else {
            CtPracticeExamEngine.buildExamSession(
                questionCount = questionCount,
                curriculumQuizzes = allCurriculumQuizzes,
                seed = examSeed
            )
        }
    }

    LaunchedEffect(isExamStarted, isExamSubmitted, useTimer, examSeed) {
        if (isExamStarted && !isExamSubmitted && useTimer) {
            while (remainingSeconds > 0 && !isExamSubmitted && isExamStarted) {
                delay(1000L)
                remainingSeconds--
            }
            if (remainingSeconds <= 0 && !isExamSubmitted && isExamStarted) {
                isExamSubmitted = true
            }
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag("ct_tryout_panel"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (!isExamStarted) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkNavySlate),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.2.dp, CyanPrimary.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Try-Out Uji Kompetensi (UKOM) CT Scan",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CyanBright
                    )
                    if (examStats.completedCount > 0) {
                        Text(
                            text = "Riwayat Try-Out: ${examStats.completedCount}x selesai · Skor Terbaik: ${examStats.bestScorePercent}%" +
                                (examStats.weakestDomainName?.let { " · Fokus Perlu Kuat: $it" } ?: ""),
                            fontSize = 11.sp,
                            color = EmeraldSuccess,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    if (examStats.domainTotals.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(CardSlate.copy(alpha = 0.65f))
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = "Profil Akurasi 5 Domain UKOM Anda:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanBright
                            )
                            com.example.util.CtExamDomain.entries.forEach { dom ->
                                val pair = examStats.domainTotals[dom]
                                if (pair != null && pair.second > 0) {
                                    val pct = ((pair.first * 100f) / pair.second).toInt().coerceIn(0, 100)
                                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(dom.shortLabel, fontSize = 10.sp, color = TextPrimary)
                                            Text("$pct% (${pair.first}/${pair.second})", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = if (pct >= 70) EmeraldSuccess else AmberWarning)
                                        }
                                        LinearProgressIndicator(
                                            progress = { pct / 100f },
                                            modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(3.dp)),
                                            color = if (pct >= 70) EmeraldSuccess else AmberWarning,
                                            trackColor = DeepObsidian
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Text(
                        text = "Latihan terpadu acak berimbang dari ${CtPracticeExamEngine.allQuestionPool(allCurriculumQuizzes).size} bank soal (50 materi kurikulum + 25 kasus klinis UKOM CT lanjutan) lintas 5 domain kompetensi.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )

                    Text(
                        text = "Pilih Jumlah Soal Paket Try-Out:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(
                            10 to "10 Soal Cepat",
                            20 to "20 Soal UKOM",
                            30 to "30 Soal Lengkap"
                        ).forEach { (cnt, lbl) ->
                            val isSel = questionCount == cnt
                            FilterChip(
                                selected = isSel,
                                onClick = {
                                    haptics.lightToggle()
                                    questionCount = cnt
                                },
                                label = { Text(lbl, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyanPrimary,
                                    selectedLabelColor = DeepObsidian,
                                    containerColor = CardSlate,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Timer Hitung Mundur (${questionCount} Menit)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Simulasi batas waktu 1 menit/soal seperti ujian sesungguhnya.",
                                fontSize = 10.5.sp,
                                color = TextMuted
                            )
                        }
                        Switch(
                            checked = useTimer,
                            onCheckedChange = { useTimer = it }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Mode Adaptif: Prioritas Domain Terlemah",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Memfokuskan ~60% soal ke domain yang akurasinya paling rendah dari riwayat kuis.",
                                fontSize = 10.5.sp,
                                color = TextMuted
                            )
                        }
                        Switch(
                            checked = useAdaptiveMode,
                            onCheckedChange = { useAdaptiveMode = it }
                        )
                    }

                    Button(
                        onClick = {
                            haptics.lightToggle()
                            selectedAnswers.clear()
                            currentIdx = 0
                            remainingSeconds = CtPracticeExamEngine.recommendedDurationSeconds(questionCount)
                            isExamSubmitted = false
                            isExamStarted = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 48.dp)
                            .testTag("start_ct_tryout_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyanPrimary,
                            contentColor = DeepObsidian
                        )
                    ) {
                        Text(
                            text = "Mulai Try-Out ($questionCount Soal)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        } else if (isExamSubmitted) {
            val eval = remember(sessionQuestions, selectedAnswers.size, isExamSubmitted) {
                CtPracticeExamEngine.evaluateSession(sessionQuestions, selectedAnswers.toMap())
            }
            LaunchedEffect(isExamSubmitted, examSeed) {
                if (isExamSubmitted && recordedSubmissionSeed != examSeed) {
                    recordedSubmissionSeed = examSeed
                    val weakest = eval.domainBreakdown.filter { it.total > 0 }.minByOrNull { it.accuracyPct }
                    onExamCompleted?.invoke(eval.scorePercent, weakest?.domain?.fullLabel, eval.domainBreakdown)
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = DarkNavySlate),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.5.dp, if (eval.passed) EmeraldSuccess else AmberWarning),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = if (eval.passed) EmeraldSuccess else AmberWarning,
                            modifier = Modifier.size(32.dp)
                        )
                        Column {
                            Text(
                                text = "Hasil Try-Out UKOM CT Scan",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${eval.gradeLabel} · ${eval.scorePercent}% (${eval.correctCount}/${eval.totalQuestions} Benar)",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (eval.passed) EmeraldSuccess else AmberWarning
                            )
                        }
                    }

                    Text(
                        text = "Rincian Penguasaan per Domain CT:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanBright
                    )
                    eval.domainBreakdown.forEach { dScore ->
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(dScore.domain.fullLabel, fontSize = 11.sp, color = TextPrimary)
                                Text(
                                    text = "${dScore.correct}/${dScore.total} (${dScore.accuracyPct}%)",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = if (dScore.accuracyPct >= 70) EmeraldSuccess else AmberWarning
                                )
                            }
                            LinearProgressIndicator(
                                progress = { dScore.accuracyPct / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = if (dScore.accuracyPct >= 70) EmeraldSuccess else AmberWarning,
                                trackColor = CardSlate
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                isExamStarted = false
                                isExamSubmitted = false
                            },
                            modifier = Modifier
                                .weight(1f)
                                .defaultMinSize(minHeight = 48.dp)
                        ) {
                            Text("Ubah Paket", fontSize = 11.5.sp, color = TextPrimary)
                        }
                        Button(
                            onClick = {
                                examSeed++
                                selectedAnswers.clear()
                                currentIdx = 0
                                remainingSeconds = CtPracticeExamEngine.recommendedDurationSeconds(questionCount)
                                isExamSubmitted = false
                            },
                            modifier = Modifier
                                .weight(1f)
                                .defaultMinSize(minHeight = 48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = DeepObsidian)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(5.dp))
                            Text("Paket Acak Baru", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Pembahasan soal yang keliru atau belum dijawab
            val missedQuestions = sessionQuestions.filter { selectedAnswers[it.id] != it.correctIndex }
            if (missedQuestions.isNotEmpty()) {
                Text(
                    text = "Pembahasan Soal yang Perlu Penguatan (${missedQuestions.size} Soal):",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberWarning
                )
                missedQuestions.forEach { q ->
                    val chosen = selectedAnswers[q.id]
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkNavySlate),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, RoseDanger.copy(alpha = 0.55f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "${q.domain.shortLabel} · ${q.topicTag}",
                                fontSize = 10.sp,
                                color = CyanBright,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(q.question, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(
                                text = if (chosen != null) "Jawaban Anda: ${q.options[chosen]}" else "Tidak dijawab",
                                fontSize = 11.sp,
                                color = RoseDanger
                            )
                            Text(
                                text = "Kunci Benar: ${q.options[q.correctIndex]}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSuccess
                            )
                            Text(
                                text = "Pembahasan: ${q.explanation}",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        } else {
            val activeQ = sessionQuestions.getOrNull(currentIdx)
            if (activeQ != null) {
                val mins = remainingSeconds / 60
                val secs = remainingSeconds % 60
                val timeStr = String.format(Locale.US, "%02d:%02d", mins, secs)

                // Top status row + Submit button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = DarkNavySlate,
                        border = BorderStroke(1.dp, CardSlateBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            if (useTimer) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = "Sisa waktu ujian",
                                    tint = if (remainingSeconds <= 60) RoseDanger else CyanBright,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "$timeStr  |",
                                    fontSize = 11.5.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = if (remainingSeconds <= 60) RoseDanger else CyanBright
                                )
                            }
                            Text(
                                text = "Terjawab ${selectedAnswers.size}/${sessionQuestions.size}",
                                fontSize = 11.5.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Button(
                        onClick = {
                            haptics.lightToggle()
                            isExamSubmitted = true
                        },
                        modifier = Modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess, contentColor = DeepObsidian)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Selesai", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Quick Question Navigator
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    sessionQuestions.forEachIndexed { idx, qItem ->
                        val isCur = idx == currentIdx
                        val isAns = selectedAnswers.containsKey(qItem.id)
                        Surface(
                            modifier = Modifier
                                .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                                .clickable { currentIdx = idx },
                            shape = RoundedCornerShape(8.dp),
                            color = when {
                                isCur -> CyanPrimary
                                isAns -> EmeraldDark
                                else -> CardSlate
                            },
                            border = BorderStroke(
                                1.dp,
                                when {
                                    isCur -> CyanBright
                                    isAns -> EmeraldSuccess
                                    else -> CardSlateBorder
                                }
                            )
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 8.dp)) {
                                Text(
                                    text = "${idx + 1}",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCur) DeepObsidian else TextPrimary
                                )
                            }
                        }
                    }
                }

                // Question Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkNavySlate),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, CardSlateBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Soal ${currentIdx + 1} · ${activeQ.domain.shortLabel}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanBright
                            )
                            Text(
                                text = activeQ.topicTag,
                                fontSize = 10.sp,
                                color = TextMuted,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Text(
                            text = activeQ.question,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            lineHeight = 19.sp
                        )

                        activeQ.options.forEachIndexed { oIdx, optText ->
                            val isSel = selectedAnswers[activeQ.id] == oIdx
                            val letter = ('A' + oIdx)
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 48.dp)
                                    .clickable {
                                        haptics.lightToggle()
                                        selectedAnswers[activeQ.id] = oIdx
                                    },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) CyanDark else DeepObsidian,
                                border = BorderStroke(1.2.dp, if (isSel) CyanBright else CardSlateBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = "$letter.",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isSel) CyanBright else TextMuted
                                    )
                                    Text(
                                        text = optText,
                                        fontSize = 12.sp,
                                        color = TextPrimary,
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(
                                onClick = { if (currentIdx > 0) currentIdx-- },
                                enabled = currentIdx > 0,
                                modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Sebelumnya", fontSize = 11.5.sp)
                            }
                            if (currentIdx < sessionQuestions.lastIndex) {
                                Button(
                                    onClick = { currentIdx++ },
                                    modifier = Modifier.defaultMinSize(minHeight = 48.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = DeepObsidian)
                                ) {
                                    Text("Berikutnya", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            } else {
                                Button(
                                    onClick = { isExamSubmitted = true },
                                    modifier = Modifier.defaultMinSize(minHeight = 48.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess, contentColor = DeepObsidian)
                                ) {
                                    Text("Kumpulkan Ujian", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}
