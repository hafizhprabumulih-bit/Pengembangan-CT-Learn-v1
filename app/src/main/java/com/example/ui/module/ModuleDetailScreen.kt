package com.example.ui.module

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CtModule
import com.example.data.CtNode
import com.example.data.CtRepository
import com.example.ui.components.AnimatedBookmarkPill
import com.example.ui.components.FlashcardDeckSection
import com.example.ui.components.StudyModeToggleBar
import com.example.ui.components.rememberRestorableScrollState
import com.example.ui.simulators.*
import com.example.ui.theme.AmberAccent
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
import com.example.util.NodeDisplay
import com.example.util.NodeStatus
import com.example.util.PATH_STRICT_LOCKS
import com.example.util.PathStateResolver
import com.example.viewmodel.MainTab
import com.example.viewmodel.CtUiState
import com.example.viewmodel.CtViewModel
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

enum class ReaderMode {
    FOCUS_NODE,
    ALL_NODES
}

@Composable
fun ModuleDetailScreen(
    module: CtModule,
    state: CtUiState,
    viewModel: CtViewModel,
    modifier: Modifier = Modifier,
    focusedNodeCode: String? = null,
    readerMode: ReaderMode = if (!focusedNodeCode.isNullOrBlank()) ReaderMode.FOCUS_NODE else ReaderMode.ALL_NODES,
    skippedNodeCodes: Set<String> = emptySet(),
    onClearFocusedNode: (() -> Unit)? = null,
    onSelectFocusedNode: ((String) -> Unit)? = null,
    onReturnToPath: (() -> Unit)? = null,
    onBackOverride: (() -> Unit)? = null,
    onNodeJustCompleted: ((completedCode: String, completedBefore: Set<String>, skippedBefore: Set<String>) -> Unit)? = null,
    onMarkSkippedNodeAsRead: ((String) -> Unit)? = null
) {
    BackHandler {
        if (onBackOverride != null) {
            onBackOverride()
        } else {
            viewModel.navigateBack()
        }
    }

    if (readerMode == ReaderMode.FOCUS_NODE && !focusedNodeCode.isNullOrBlank()) {
        val normTarget = com.example.data.LegacyNodeCodes.normalize(focusedNodeCode)
        val focusNode = module.nodes.find {
            com.example.data.LegacyNodeCodes.normalize(it.code) == normTarget
        }
        if (focusNode != null) {
            FocusNodeReader(
                module = module,
                node = focusNode,
                state = state,
                viewModel = viewModel,
                skippedNodeCodes = skippedNodeCodes,
                onBack = {
                    when {
                        onBackOverride != null -> onBackOverride()
                        onReturnToPath != null -> onReturnToPath()
                        else -> viewModel.navigateBack()
                    }
                },
                onShowAllNodes = { onClearFocusedNode?.invoke() },
                onSelectFocusedNode = onSelectFocusedNode,
                onReturnToPath = onReturnToPath,
                onNodeJustCompleted = onNodeJustCompleted,
                onMarkSkippedNodeAsRead = onMarkSkippedNodeAsRead,
                modifier = modifier
            )
            return
        }
    }

    val moduleBookmarkedCount = module.nodes.count { state.bookmarkedNodeCodes.contains(it.code) }
    val moduleCompletedCount = module.nodes.count { state.completedNodeCodes.contains(it.code) }
    val totalModuleNodes = module.nodes.size
    val isModuleComplete = moduleCompletedCount == totalModuleNodes && totalModuleNodes > 0
    val moduleProgress = if (totalModuleNodes > 0) {
        (moduleCompletedCount.toFloat() / totalModuleNodes.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    val detailScrollState = rememberRestorableScrollState("module_${module.id}", viewModel)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepObsidian)
            .verticalScroll(detailScrollState)
            .padding(16.dp)
    ) {
        if (onReturnToPath != null || focusedNodeCode != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onReturnToPath != null) {
                    OutlinedButton(
                        onClick = onReturnToPath,
                        modifier = Modifier
                            .defaultMinSize(minHeight = 48.dp)
                            .testTag("reader_return_to_path"),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.7f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = CyanBright,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Peta ${module.code}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CyanBright
                        )
                    }
                }
                if (focusedNodeCode != null && onClearFocusedNode != null) {
                    OutlinedButton(
                        onClick = onClearFocusedNode,
                        modifier = Modifier
                            .defaultMinSize(minHeight = 48.dp)
                            .testTag("reader_show_all_nodes"),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardSlateBorder)
                    ) {
                        Text(
                            text = "Semua Simpul (${module.nodes.size})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Header Modul dengan Indikator Lingkaran Progres Belajar di Dalam Kartu Penjelasan
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkNavySlate),
            border = androidx.compose.foundation.BorderStroke(
                1.5.dp,
                if (isModuleComplete) EmeraldSuccess.copy(alpha = 0.8f) else CyanPrimary.copy(alpha = 0.7f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = CyanDark,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary)
                        ) {
                            Text(
                                text = module.code,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanBright,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Text(
                            text = module.levelBadge,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = module.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = module.description,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Lingkaran Progres Belajar di Dalam Kartu Penjelasan Modul
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(58.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { moduleProgress },
                            modifier = Modifier.fillMaxSize(),
                            color = if (isModuleComplete) EmeraldSuccess else CyanBright,
                            trackColor = CardSlate,
                            strokeWidth = 5.dp
                        )
                        Text(
                            text = "$moduleCompletedCount/$totalModuleNodes",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isModuleComplete) EmeraldSuccess else TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Selesai",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isModuleComplete) EmeraldSuccess else CyanBright
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Mode Toggle Bar: Materi Modul vs Flashcards Hafalan
        StudyModeToggleBar(
            primaryLabel = "Materi Modul (${module.nodes.size})",
            flashcardLabel = "Flashcards Hafalan",
            isFlashcardMode = state.isModuleFlashcardMode,
            onSelectMode = { viewModel.setModuleFlashcardMode(it) }
        )

        Spacer(modifier = Modifier.height(12.dp))



        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = state.onlyBookmarkedNodes,
                onClick = { viewModel.setOnlyBookmarkedNodes(!state.onlyBookmarkedNodes) },
                modifier = Modifier.testTag("module_bookmark_filter_chip"),
                label = {
                    Text(
                        text = "★ Tersimpan ($moduleBookmarkedCount)",
                        fontSize = 11.sp,
                        fontWeight = if (state.onlyBookmarkedNodes) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AmberAccent,
                    selectedLabelColor = DeepObsidian,
                    containerColor = DarkNavySlate,
                    labelColor = AmberAccent
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = state.onlyBookmarkedNodes,
                    borderColor = if (state.onlyBookmarkedNodes) AmberAccent else CardSlateBorder
                )
            )
            val availableLevels = remember(module.nodes) {
                module.nodes.map { if (it.level == 0) 1 else it.level }.toSet()
            }
            val levels = buildList {
                add(0 to "Semua Tingkat")
                if (1 in availableLevels) add(1 to "T1 (Fundamental)")
                if (2 in availableLevels) add(2 to "T2 (Klinis Menengah)")
                if (3 in availableLevels) add(3 to "T3 (Fisika Lanjut)")
            }
            levels.forEach { (lvl, label) ->
                FilterChipLevel(label = label, isSelected = state.nodeLevelFilter == lvl) {
                    viewModel.setNodeLevelFilter(lvl)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (state.isModuleFlashcardMode) {
            val moduleCards = CtRepository.flashcards.filter { card ->
                val matchesModule = card.moduleId == module.id
                val rawNodeLevel = module.nodes.find { it.code == card.nodeCode }?.level ?: 1
                val nodeLevel = if (rawNodeLevel == 0) 1 else rawNodeLevel
                val matchesLevel = state.nodeLevelFilter == 0 || nodeLevel == state.nodeLevelFilter
                val matchesBookmark = !state.onlyBookmarkedNodes || state.isFlashcardBookmarked(card)
                matchesModule && matchesLevel && matchesBookmark
            }

            FlashcardDeckSection(
                cards = moduleCards,
                currentIndex = state.moduleFlashcardIndex,
                isFlipped = state.isModuleFlashcardFlipped,
                shuffleSeed = state.moduleFlashcardShuffleSeed,
                masteredCardIds = state.masteredFlashcardIds,
                isCardBookmarked = { card -> state.isFlashcardBookmarked(card) },
                onToggleBookmark = { card -> viewModel.toggleFlashcardBookmark(card) },
                onFlip = { viewModel.flipFlashcard() },
                onPrev = { size -> viewModel.prevFlashcard(size) },
                onNext = { size -> viewModel.nextFlashcard(size) },
                onShuffle = { viewModel.shuffleFlashcards() },
                onToggleMastered = { id -> viewModel.toggleFlashcardMastered(id) },
                emptyMessage = if (state.onlyBookmarkedNodes) {
                    "Belum ada simpul materi yang disimpan (Bookmark) pada filter ini."
                } else {
                    "Tidak ada kartu hafalan pada tingkatan ini."
                }
            )
        } else {
            val filteredNodes = if (!focusedNodeCode.isNullOrBlank()) {
                val targetList = module.nodes.filter { it.code.equals(focusedNodeCode, ignoreCase = true) }
                if (targetList.isNotEmpty()) targetList else module.nodes
            } else {
                module.nodes.filter { node ->
                    val effectiveLevel = if (node.level == 0) 1 else node.level
                    val matchesLevel = state.nodeLevelFilter == 0 || effectiveLevel == state.nodeLevelFilter
                    val matchesBookmark = !state.onlyBookmarkedNodes || state.bookmarkedNodeCodes.contains(node.code)
                    matchesLevel && matchesBookmark
                }
            }

            if (filteredNodes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (state.onlyBookmarkedNodes) {
                            "Belum ada simpul materi yang disimpan (Bookmark) pada filter ini."
                        } else {
                            "Tidak ada simpul materi pada tingkatan ini."
                        },
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            } else {
                val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    filteredNodes.forEach { node ->
                        val normalizedCode = com.example.data.LegacyNodeCodes.normalize(node.code)
                        val isCompleted = state.completedNodeCodes.contains(node.code) ||
                            state.completedNodeCodes.contains(normalizedCode)
                        val isSkipped = normalizedCode in skippedNodeCodes || node.code in skippedNodeCodes
                        val isBookmarked = state.bookmarkedNodeCodes.contains(node.code)
                        val activeMode = state.nodeModeMap[node.code] ?: state.globalNodeMode
                        NodeCard(
                            node = node,
                            isCompleted = isCompleted,
                            isBookmarked = isBookmarked,
                            activeMode = activeMode,
                            onModeSelected = { mode ->
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                viewModel.setNodeMode(node.code, mode)
                            },
                            onToggleCompleted = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                val snapshotCompleted = state.completedNodeCodes
                                val snapshotSkipped = skippedNodeCodes
                                if (isSkipped) {
                                    onMarkSkippedNodeAsRead?.invoke(node.code)
                                    onNodeJustCompleted?.invoke(node.code, snapshotCompleted, snapshotSkipped)
                                } else {
                                    viewModel.toggleNodeCompletion(node.code)
                                    if (!isCompleted) {
                                        onNodeJustCompleted?.invoke(node.code, snapshotCompleted, snapshotSkipped)
                                    }
                                }
                            },
                            onMarkNodeCompleted = { code ->
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                val target = code.ifBlank { node.code }
                                val normTarget = com.example.data.LegacyNodeCodes.normalize(target)
                                val snapshotCompleted = state.completedNodeCodes
                                val snapshotSkipped = skippedNodeCodes
                                val wasDoneAsRead = (snapshotCompleted.contains(target) || snapshotCompleted.contains(normTarget)) &&
                                    normTarget !in snapshotSkipped && target !in snapshotSkipped
                                if (normTarget in snapshotSkipped || target in snapshotSkipped) {
                                    onMarkSkippedNodeAsRead?.invoke(target)
                                } else {
                                    viewModel.markNodeCompleted(target)
                                }
                                if (!wasDoneAsRead) {
                                    onNodeJustCompleted?.invoke(target, snapshotCompleted, snapshotSkipped)
                                }
                            },
                            onToggleBookmark = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                viewModel.toggleNodeBookmark(node.code)
                            },
                            onLaunchLab = viewModel::launchLab,
                            isSkippedByTest = isSkipped
                        )
                    }
                    if (!focusedNodeCode.isNullOrBlank() && onSelectFocusedNode != null) {
                        val orderedNodes = PathStateResolver.orderNodesPublic(module.nodes)
                        val normFocused = com.example.data.LegacyNodeCodes.normalize(focusedNodeCode)
                        val currentIndex = orderedNodes.indexOfFirst {
                            com.example.data.LegacyNodeCodes.normalize(it.code) == normFocused
                        }
                        val nextNode = if (currentIndex >= 0 && currentIndex + 1 < orderedNodes.size) {
                            orderedNodes[currentIndex + 1]
                        } else {
                            null
                        }
                        if (nextNode != null) {
                            OutlinedButton(
                                onClick = { onSelectFocusedNode(nextNode.code) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 48.dp)
                                    .testTag("reader_next_node"),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.7f))
                            ) {
                                Text(
                                    text = "Simpul Berikutnya: ${NodeDisplay.displayTitle(nextNode)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = CyanBright
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = CyanBright,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Card Pintasan Evaluasi Kuis Modul
        val moduleQuizzes = state.quizzesList.filter { it.moduleId == module.id }
        val moduleQuizCount = moduleQuizzes.size
        if (moduleQuizCount > 0) {
            Spacer(modifier = Modifier.height(18.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkNavySlate),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Evaluasi Pemahaman ${module.code}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tersedia $moduleQuizCount soal kuis interaktif (3 tingkatan) untuk menguji penguasaan materi ${module.title}.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val t1Count = moduleQuizzes.count { it.level == 1 }
                    val t2Count = moduleQuizzes.count { it.level == 2 }
                    val t3Count = moduleQuizzes.count { it.level == 3 }
                    if (t1Count > 0 || t2Count > 0 || t3Count > 0) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (t1Count > 0) {
                                OutlinedButton(
                                    onClick = { viewModel.openModuleQuiz(module.id, 1) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.6f))
                                ) {
                                    Text("T1 ($t1Count)", fontSize = 10.sp, color = EmeraldSuccess, fontWeight = FontWeight.Bold)
                                }
                            }
                            if (t2Count > 0) {
                                OutlinedButton(
                                    onClick = { viewModel.openModuleQuiz(module.id, 2) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CyanBright.copy(alpha = 0.6f))
                                ) {
                                    Text("T2 ($t2Count)", fontSize = 10.sp, color = CyanBright, fontWeight = FontWeight.Bold)
                                }
                            }
                            if (t3Count > 0) {
                                OutlinedButton(
                                    onClick = { viewModel.openModuleQuiz(module.id, 3) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, AmberWarning.copy(alpha = 0.6f))
                                ) {
                                    Text("T3 ($t3Count)", fontSize = 10.sp, color = AmberWarning, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Button(
                        onClick = { viewModel.openModuleQuiz(module.id, 0) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("start_module_quiz_${module.id}"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = null,
                            tint = DeepObsidian,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Mulai Kuis ${module.code} ($moduleQuizCount Soal)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepObsidian
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun FilterChipLevel(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = CyanPrimary,
            selectedLabelColor = DeepObsidian,
            containerColor = DarkNavySlate,
            labelColor = TextSecondary
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = if (isSelected) CyanBright else CardSlateBorder
        )
    )
}

@Composable
private fun FocusNodeReader(
    module: CtModule,
    node: CtNode,
    state: CtUiState,
    viewModel: CtViewModel,
    skippedNodeCodes: Set<String>,
    onBack: () -> Unit,
    onShowAllNodes: () -> Unit,
    onSelectFocusedNode: ((String) -> Unit)?,
    onReturnToPath: (() -> Unit)?,
    onNodeJustCompleted: ((completedCode: String, completedBefore: Set<String>, skippedBefore: Set<String>) -> Unit)?,
    onMarkSkippedNodeAsRead: ((String) -> Unit)?,
    modifier: Modifier = Modifier
) {
    val normalizedCode = com.example.data.LegacyNodeCodes.normalize(node.code)
    val isCompleted = state.completedNodeCodes.contains(node.code) ||
        state.completedNodeCodes.contains(normalizedCode)
    val isSkipped = normalizedCode in skippedNodeCodes || node.code in skippedNodeCodes
    val isDoneAsRead = isCompleted && !isSkipped
    val isBookmarked = state.bookmarkedNodeCodes.contains(node.code)
    val activeMode = state.nodeModeMap[node.code] ?: state.globalNodeMode

    val pathState = remember(module, state.modulesList, state.completedNodeCodes, skippedNodeCodes) {
        PathStateResolver.resolveModule(
            module = module,
            allModules = state.modulesList,
            completedCodes = state.completedNodeCodes,
            strictLocks = PATH_STRICT_LOCKS,
            skippedCodes = skippedNodeCodes
        )
    }
    val (position, totalNodes) = remember(pathState, node.code) {
        PathStateResolver.positionOf(pathState, node.code)
    }
    val nextPathNode = remember(pathState, node.code) {
        PathStateResolver.nextNodeOf(pathState, node.code)
    }
    val nodePathState = remember(pathState, normalizedCode) {
        pathState.nodes.find { com.example.data.LegacyNodeCodes.normalize(it.node.code) == normalizedCode }
    }
    val nodeQuizzes = remember(state.quizzesList, module.id, normalizedCode) {
        state.quizzesList.filter {
            it.moduleId == module.id && com.example.data.LegacyNodeCodes.normalize(it.nodeCode) == normalizedCode
        }
    }
    val preferredQuizLevel = remember(nodeQuizzes, node.level) {
        if (nodeQuizzes.any { it.level == node.level }) {
            node.level.coerceIn(1, 3)
        } else {
            nodeQuizzes.firstOrNull()?.level?.coerceIn(1, 3) ?: node.level.coerceIn(1, 3)
        }
    }
    val readMinutes = remember(node.detailFull) {
        val wordCount = node.detailFull.trim().split(Regex("\\s+")).size
        (wordCount / 130).coerceIn(2, 8)
    }

    val focusScrollState = rememberRestorableScrollState("focus_${module.id}_${node.code}", viewModel)
    val pathHolder = com.example.ui.path.rememberModulePathStateHolder()
    val scope = rememberCoroutineScope()
    val todayKey = remember {
        java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())
    }
    var showQuickReadGateDialog by rememberSaveable(node.code) { mutableStateOf(false) }
    var quickReadSelectedOption by rememberSaveable(node.code) { mutableStateOf<Int?>(null) }

    val completeQuickReadNode = {
        val snapshotCompleted = state.completedNodeCodes
        val snapshotSkipped = skippedNodeCodes
        if (isSkipped) {
            onMarkSkippedNodeAsRead?.invoke(node.code)
        } else {
            viewModel.markNodeCompleted(node.code)
        }
        scope.launch { pathHolder.recordDailyShift(todayKey, nodesDelta = 1) }
        if (onNodeJustCompleted != null) {
            onNodeJustCompleted(node.code, snapshotCompleted, snapshotSkipped)
        } else if (nextPathNode != null && onSelectFocusedNode != null) {
            onSelectFocusedNode(nextPathNode.node.code)
        } else {
            onReturnToPath?.invoke()
        }
    }

    if (showQuickReadGateDialog) {
        val gateQuiz = nodeQuizzes.firstOrNull()
        androidx.compose.material3.AlertDialog(
            onDismissRequest = {
                showQuickReadGateDialog = false
            },
            containerColor = DarkNavySlate,
            title = {
                Text(
                    text = if (gateQuiz != null) "Cek Singkat · ${NodeDisplay.shortCode(node.code)}" else "Tandai simpul ini selesai?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
            },
            text = {
                if (gateQuiz != null) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = gateQuiz.question,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            lineHeight = 20.sp
                        )
                        gateQuiz.options.forEachIndexed { idx, opt ->
                            val picked = quickReadSelectedOption == idx
                            val isCorrect = idx == gateQuiz.correctIndex
                            val borderCol = when {
                                quickReadSelectedOption != null && isCorrect -> EmeraldSuccess
                                picked && !isCorrect -> AmberWarning
                                else -> CardSlateBorder
                            }
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable(enabled = quickReadSelectedOption == null) {
                                        quickReadSelectedOption = idx
                                        pathHolder.recordLessonQuizAttemptAsync(
                                            quizId = gateQuiz.id,
                                            nodeCode = normalizedCode,
                                            moduleId = module.id,
                                            selectedIndex = idx,
                                            isCorrect = isCorrect
                                        )
                                    }
                                    .testTag("quick_read_quiz_option_$idx"),
                                shape = RoundedCornerShape(12.dp),
                                color = if (picked) CyanPrimary.copy(alpha = 0.14f) else DeepObsidian,
                                border = androidx.compose.foundation.BorderStroke(1.2.dp, borderCol)
                            ) {
                                Text(
                                    text = opt,
                                    fontSize = 13.sp,
                                    color = TextPrimary,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                                )
                            }
                        }
                        if (quickReadSelectedOption != null && gateQuiz.explanation.isNotBlank()) {
                            Text(
                                text = gateQuiz.explanation,
                                fontSize = 12.5.sp,
                                color = if (quickReadSelectedOption == gateQuiz.correctIndex) EmeraldSuccess else AmberWarning,
                                lineHeight = 18.sp
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Pastikan kamu sudah memahami poin utama simpul ${NodeDisplay.displayTitle(node)} sebelum melanjutkan.",
                        fontSize = 13.5.sp,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showQuickReadGateDialog = false
                        quickReadSelectedOption = null
                        completeQuickReadNode()
                    },
                    enabled = gateQuiz == null || quickReadSelectedOption != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyanPrimary,
                        contentColor = DeepObsidian
                    ),
                    modifier = Modifier.testTag("quick_read_confirm_complete")
                ) {
                    Text("Selesaikan & Lanjut", fontWeight = FontWeight.ExtraBold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showQuickReadGateDialog = false
                    }
                ) {
                    Text("Batal", color = TextSecondary)
                }
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DeepObsidian,
        topBar = {
            Surface(color = DarkNavySlate) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("reader_return_to_path")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali ke Peta ${module.code}",
                                tint = TextPrimary
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            val levelLabel = if (node.level == 0) "Pendahuluan" else "Tingkat ${node.level}"
                            Text(
                                text = "Peta ${module.code} · $levelLabel",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CyanBright,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Simpul $position dari $totalNodes",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                        OutlinedButton(
                            onClick = onShowAllNodes,
                            modifier = Modifier
                                .defaultMinSize(minHeight = 40.dp)
                                .testTag("reader_show_all_nodes"),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardSlateBorder)
                        ) {
                            Text(
                                text = "Semua simpul",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }
                    }
                    LinearProgressIndicator(
                        progress = {
                            if (totalNodes > 0) (position.toFloat() / totalNodes.toFloat()).coerceIn(0f, 1f) else 0f
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp),
                        color = CyanPrimary,
                        trackColor = CardSlate
                    )
                }
            }
        },
        bottomBar = {
            Surface(
                color = DarkNavySlate,
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardSlateBorder)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (isDoneAsRead) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = { viewModel.toggleNodeCompletion(node.code) },
                                modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                            ) {
                                Text(
                                    text = "Batalkan selesai",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.toggleNodeBookmark(node.code) }
                                .testTag("bookmark_node_${node.code}"),
                            shape = RoundedCornerShape(12.dp),
                            color = DeepObsidian,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isBookmarked) AmberAccent else CardSlateBorder
                            )
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = if (isBookmarked) "Hapus dari tersimpan" else "Simpan simpul",
                                    tint = if (isBookmarked) AmberAccent else TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    viewModel.selectTab(MainTab.CATATAN)
                                    viewModel.openNoteDialog(null)
                                }
                                .testTag("focus_open_notes_${node.code}"),
                            shape = RoundedCornerShape(12.dp),
                            color = DeepObsidian,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardSlateBorder)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.EditNote,
                                    contentDescription = "Buka catatan",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Button(
                            onClick = {
                                if (isDoneAsRead) {
                                    if (nextPathNode != null && onSelectFocusedNode != null) {
                                        onSelectFocusedNode(nextPathNode.node.code)
                                    } else {
                                        onReturnToPath?.invoke()
                                    }
                                } else if (com.example.util.lesson.LESSON_FLOW_ENABLED && nodeQuizzes.isNotEmpty()) {
                                    showQuickReadGateDialog = true
                                } else {
                                    completeQuickReadNode()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .defaultMinSize(minHeight = 52.dp)
                                .testTag("focus_complete_and_next"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDoneAsRead) EmeraldSuccess else CyanPrimary,
                                contentColor = DeepObsidian
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (isDoneAsRead) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = DeepObsidian,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = if (isDoneAsRead) "Lanjut" else "Tandai selesai & lanjut",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepObsidian
                                    )
                                    Text(
                                        text = nextPathNode?.let {
                                            "Berikutnya: ${NodeDisplay.displayTitle(it.node)}"
                                        } ?: "Kembali ke Peta ${module.code}",
                                        fontSize = 10.sp,
                                        color = DeepObsidian.copy(alpha = 0.8f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(focusScrollState)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = CyanDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.6f))
                ) {
                    Text(
                        text = NodeDisplay.shortCode(node.code),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = CyanBright,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                val metaParts = buildList {
                    add("$readMinutes mnt")
                    if (nodeQuizzes.isNotEmpty()) add("${nodeQuizzes.size} kuis")
                    if (!isDoneAsRead) add("+10 XP")
                }.joinToString(" · ")
                Text(
                    text = metaParts,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = NodeDisplay.displayTitle(node),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            NodeContentAndInlineLab(
                node = node,
                activeMode = activeMode,
                onModeSelected = { mode -> viewModel.setNodeMode(node.code, mode) },
                onMarkNodeCompleted = { targetCode ->
                    val target = targetCode.ifBlank { node.code }
                    val normTarget = com.example.data.LegacyNodeCodes.normalize(target)
                    val wasDoneAsRead = (state.completedNodeCodes.contains(target) ||
                        state.completedNodeCodes.contains(normTarget)) &&
                        normTarget !in skippedNodeCodes && target !in skippedNodeCodes
                    if (normTarget in skippedNodeCodes || target in skippedNodeCodes) {
                        onMarkSkippedNodeAsRead?.invoke(target)
                    } else {
                        viewModel.markNodeCompleted(target)
                    }
                    if (!wasDoneAsRead) {
                        onNodeJustCompleted?.invoke(target, state.completedNodeCodes, skippedNodeCodes)
                    }
                }
            )

            if (node.labType != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable {
                            scope.launch {
                                pathHolder.markLabTried(node.code)
                                pathHolder.recordDailyShift(todayKey, labsDelta = 1)
                            }
                            viewModel.launchLab(node.labType)
                        }
                        .testTag("launch_lab_${node.code}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkNavySlate),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Science,
                            contentDescription = null,
                            tint = CyanBright,
                            modifier = Modifier.size(22.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Praktik Simulasi Terintegrasi",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Opsional · tidak mengunci simpul berikutnya",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = CyanBright
                        )
                    }
                }
            }

            if (nodeQuizzes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { viewModel.openModuleQuiz(module.id, preferredQuizLevel) }
                        .testTag("focus_quiz_card_${node.code}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkNavySlate),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardSlateBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Cek pemahaman · ${nodeQuizzes.size} soal",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Jawaban benar menambah bintang penguasaan",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    }
                }
            }

            val softHint = nodePathState?.softPrerequisiteHint
            if (!softHint.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AmberWarning.copy(alpha = 0.14f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AmberWarning.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Flag,
                            contentDescription = null,
                            tint = AmberWarning,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "$softHint (prasyarat lembut, tidak mengunci)",
                            fontSize = 12.sp,
                            color = TextPrimary,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun NodeContentAndInlineLab(
    node: CtNode,
    activeMode: String,
    onModeSelected: (String) -> Unit,
    onMarkNodeCompleted: (String) -> Unit
) {
    // 3-Mode Segmented Control: [Singkat | Lengkap | Analogi]
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(DeepObsidian)
            .border(1.dp, CardSlateBorder, RoundedCornerShape(10.dp))
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ModeButton(
            label = "Ringkasan",
            isSelected = activeMode == "s",
            modifier = Modifier
                .weight(1f)
                .testTag("mode_singkat_${node.code}")
        ) {
            onModeSelected("s")
        }
        ModeButton(
            label = "Materi Lengkap",
            isSelected = activeMode == "l",
            modifier = Modifier
                .weight(1f)
                .testTag("mode_lengkap_${node.code}")
        ) {
            onModeSelected("l")
        }
        ModeButton(
            label = "Analogi",
            isSelected = activeMode == "a",
            modifier = Modifier
                .weight(1f)
                .testTag("mode_analogi_${node.code}")
        ) {
            onModeSelected("a")
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Dynamic Mode Content
    when (activeMode) {
        "s" -> {
            Text(
                text = node.summaryShort,
                fontSize = 12.sp,
                color = TextPrimary,
                lineHeight = 19.sp
            )
        }
        "l" -> {
            val paragraphs = node.detailFull.split("\n\n")
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                paragraphs.forEach { block ->
                    if (block.trim().startsWith("Rumus:")) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = CardSlate,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary)
                        ) {
                            Text(
                                text = block.trim(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = CyanBright,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    } else {
                        Text(
                            text = block,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 19.sp
                        )
                    }
                }
            }
        }
        "a" -> {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = CyanDark.copy(alpha = 0.4f),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = CyanBright,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = node.analogi,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = CyanBright,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }

    if (!node.crossLinks.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = node.crossLinks,
            fontSize = 11.sp,
            color = TextMuted
        )
    }



}

@Composable
fun NodeCard(
    node: CtNode,
    isCompleted: Boolean,
    isBookmarked: Boolean,
    activeMode: String,
    onModeSelected: (String) -> Unit,
    onToggleCompleted: () -> Unit,
    onToggleBookmark: () -> Unit,
    onLaunchLab: (String) -> Unit,
    onMarkNodeCompleted: (String) -> Unit = { if (!isCompleted) onToggleCompleted() },
    isSkippedByTest: Boolean = false
) {
    var isCompleteAnimTriggered by remember { mutableStateOf(false) }
    LaunchedEffect(isCompleteAnimTriggered) {
        if (isCompleteAnimTriggered) {
            delay(340)
            isCompleteAnimTriggered = false
        }
    }

    val cardContainerColor by animateColorAsState(
        targetValue = if (isCompleted && !isCompleteAnimTriggered) {
            Color(0xFF0A2E25)
        } else {
            DarkNavySlate
        },
        animationSpec = tween(durationMillis = 320),
        label = "node_card_container_color"
    )
    val cardBorderColor by animateColorAsState(
        targetValue = when {
            isCompleted && !isCompleteAnimTriggered -> EmeraldSuccess.copy(alpha = 0.78f)
            isBookmarked -> AmberAccent.copy(alpha = 0.75f)
            else -> CardSlateBorder
        },
        animationSpec = tween(durationMillis = 280),
        label = "node_card_border_color"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("node_card_${node.code}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardContainerColor),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, cardBorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Kode Simpul, Tag Level, Bookmark, dan Tombol Selesai
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = NodeDisplay.shortCode(node.code),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanBright,
                        fontFamily = FontFamily.Monospace
                    )
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = DeepObsidian
                    ) {
                        Text(
                            text = if (node.level == 0) "Pendahuluan" else "Tingkat ${node.level}",
                            fontSize = 9.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AnimatedBookmarkPill(
                        isBookmarked = isBookmarked,
                        onToggleBookmark = onToggleBookmark,
                        testTag = "bookmark_node_${node.code}",
                        savedContentDescription = "Hapus Bookmark Materi",
                        unsavedContentDescription = "Simpan Bookmark Materi"
                    )

                    val completeScale by animateFloatAsState(
                        targetValue = when {
                            isCompleteAnimTriggered -> 1.24f
                            isCompleted -> 1.04f
                            else -> 1f
                        },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "complete_pill_scale"
                    )
                    val completeRotation by animateFloatAsState(
                        targetValue = if (isCompleteAnimTriggered) -8f else 0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "complete_pill_rotation"
                    )
                    val completeBgColor by animateColorAsState(
                        targetValue = if (isCompleted) EmeraldDark else CardSlate,
                        animationSpec = tween(durationMillis = 240),
                        label = "complete_pill_bg"
                    )
                    val completeBorderColor by animateColorAsState(
                        targetValue = if (isCompleted) EmeraldSuccess else CardSlateBorder,
                        animationSpec = tween(durationMillis = 240),
                        label = "complete_pill_border"
                    )

                    Surface(
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = completeScale
                                scaleY = completeScale
                                rotationZ = completeRotation
                            }
                            .clip(CircleShape)
                            .clickable {
                                isCompleteAnimTriggered = true
                                onToggleCompleted()
                            }
                            .testTag("complete_node_${node.code}"),
                        shape = CircleShape,
                        color = completeBgColor,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            completeBorderColor
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Circle,
                                contentDescription = null,
                                tint = if (isCompleted) EmeraldSuccess else TextMuted,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = when {
                                    isSkippedByTest -> "Baca (+XP)"
                                    isCompleted -> "Selesai"
                                    else -> "Tandai selesai"
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = when {
                                    isSkippedByTest -> CyanBright
                                    isCompleted -> EmeraldSuccess
                                    else -> TextSecondary
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = NodeDisplay.displayTitle(node),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            if (!node.prerequisite.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⚑ Prasyarat: ${node.prerequisite}",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }
            if (!node.learningGoal.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tujuan belajar: ${node.learningGoal}",
                    fontSize = 11.sp,
                    color = CyanBright,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            NodeContentAndInlineLab(
                node = node,
                activeMode = activeMode,
                onModeSelected = onModeSelected,
                onMarkNodeCompleted = onMarkNodeCompleted
            )

            // Integrated Activity Badge (🧪)
            if (node.labType != null) {
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedButton(
                    onClick = { onLaunchLab(node.labType) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("launch_lab_${node.code}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = DeepObsidian,
                        contentColor = CyanBright
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Science,
                        contentDescription = null,
                        tint = CyanBright,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (node.labType) {
                            "ct_m01_atenuasi" -> "🧪 Buka Simulator 2: Atenuasi Beer-Lambert & Skala HU"
                            "ct_m02_anatomi_pesawat" -> "🧪 Buka Simulator 1: Bedah Anatomi Gantry & Laser Isocenter"
                            "ct_m03_instrumentasi_rantai" -> "🧪 Buka Simulator 1: Rantai Sinyal Tabung, Slip-Ring & DAS"
                            "ct_m04_generasi_pitch" -> "🧪 Buka Simulator 3: Geometri MSCT & Helikal Pitch"
                            "ct_m05_rekonstruksi" -> "🧪 Buka Simulator 3: Sinogram, FBP, Kernel & Iterative Recon"
                            "ct_m06_hu_windowing" -> "🧪 Buka Simulator 2: Preset Windowing HU (WW/WL)"
                            "ct_m07_dosimetri_kualitas" -> "🧪 Buka Simulator 4: Konsol Eksposi & Dosimetri CTDIvol/DLP"
                            "ct_m08_kontras_3d" -> "🧪 Buka Simulator 4: Bolus Tracking Kontras IV & 3D"
                            "ct_m09_artefak_qa" -> "🧪 Buka Simulator 5: Artefak CT, Air Calibration & QA Fantom"
                            else -> "🧪 Praktik Simulasi Terintegrasi"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@Composable
private fun ModeButton(
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) CardSlate else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) CyanBright else TextSecondary
        )
    }
}



