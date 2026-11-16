package com.example.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.data.CharacterCustomizationState
import com.example.data.CtFlashcard
import com.example.data.CtGlossaryItem
import com.example.data.CtModule
import com.example.data.CtNode
import com.example.data.CtQuizQuestion
import com.example.data.CtRepository
import com.example.data.CtTissueAttenuation
import com.example.data.LegacyNodeCodes
import com.example.data.PracticeExamStats
import com.example.data.StudyNote
import com.example.data.UserDataRepository
import com.example.util.CtPhysicsCalculator
import com.example.data.local.QuizAttemptEntity
import com.example.data.local.StudyDayEntity
import com.example.data.local.StudyNoteEntity
import com.example.data.toDomainModel
import com.example.util.QuizShuffle
import com.example.util.ResumeNodeResolver
import com.example.util.StreakCalculator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import kotlin.math.roundToInt
import kotlin.math.sqrt

enum class MainTab {
    BERANDA,
    ANIMASI,
    SIMULASI,
    TERSIMPAN,
    CATATAN,
    GLOSARIUM,
    KUIS,
    KARAKTER
}

enum class CtSimType {
    GANTRY_RANTAI_SINYAL,
    WINDOWING_HU_ATENUASI,
    MSCT_PITCH_REKON,
    DOSIMETRI_BOLUS_TRACKING,
    ARTEFAK_KALIBRASI_QA
}

data class CtUiState(
    val currentTab: MainTab = MainTab.BERANDA,
    val modulesList: List<CtModule> = CtRepository.modules,
    val quizzesList: List<CtQuizQuestion> = CtRepository.quizzes,
    val selectedModule: CtModule? = null,
    val selectedSimType: CtSimType = CtSimType.GANTRY_RANTAI_SINYAL,
    val selectedCtActivityId: String = "ct_m02_anatomi_pesawat",
    val completedNodeCodes: Set<String> = emptySet(),
    val lastVisitedNodeCode: String? = null,
    val isResumeRestored: Boolean = false,
    // Streak Hari Belajar
    val studyStreakDays: Int = 0,
    val showStreakDialog: Boolean = false,
    // Avatar Pengguna & Ruang Karakter
    val userAvatarId: String = "radiografer_ct",
    val userName: String = "",
    val showAvatarDialog: Boolean = false,
    val characterCustomization: CharacterCustomizationState = CharacterCustomizationState(),
    // Catatan Belajar (Notes)
    val notesList: List<StudyNote> = emptyList(),
    val selectedNoteCategory: String = "SEMUA",
    val showNoteDialog: Boolean = false,
    val editingNote: StudyNote? = null,
    // Riwayat Kuis
    val quizAttempts: List<QuizAttemptEntity> = emptyList(),
    // Animasi Belajar
    val selectedAnimationIndex: Int = 0,
    val isAnimationPlaying: Boolean = true,
    val animationStep: Int = 1, // 1 to 4
    // Parameter Konsol & Fisika CT Scan
    val selectedTissue: CtTissueAttenuation = CtRepository.tissues[0],
    val tubeKvp: Int = 120,
    val tubeMa: Float = 240f,
    val rotationTimeSec: Float = 0.5f,
    val helicalPitch: Float = 1.0f,
    val scanLengthCm: Float = 30f,
    val isHeadPhantom: Boolean = false,
    val windowWidthHu: Float = 80f,
    val windowLevelHu: Float = 40f,
    val reconstructionMatrix: Int = 512,
    val displayFovMm: Float = 240f,
    val isIterativeReconActive: Boolean = true,
    val bolusThresholdHu: Int = 120,
    // Kuis States
    val quizModuleFilter: String = "SEMUA",
    val quizLevelFilter: Int = 0, // 0 = Semua, 1 = Tingkat 1, 2 = Tingkat 2, 3 = Tingkat 3
    val currentQuizIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val quizAnswers: Map<String, Int> = emptyMap(),
    val answeredQuizMap: Map<Int, Int> = emptyMap(),
    val quizScore: Int = 0,
    val isQuizFinished: Boolean = false,
    // Glosarium States
    val glossarySearchQuery: String = "",
    val glossaryModuleFilter: String = "SEMUA",
    val selectedGlossaryDetail: CtGlossaryItem? = null,
    // Level & Mode filter for Module Detail (-1 = Semua, 0 = Tujuan, 1 = T1, 2 = T2, 3 = T3)
    val nodeLevelFilter: Int = 0,
    val globalNodeMode: String = "s",
    val nodeModeMap: Map<String, String> = emptyMap(),
    // Bookmark & Flashcards States (dipisahkan per mode Modul vs Glosarium)
    val bookmarkedNodeCodes: Set<String> = emptySet(),
    val bookmarkedGlossaryTerms: Set<String> = emptySet(),
    val masteredFlashcardIds: Set<String> = emptySet(),
    val onlyBookmarkedNodes: Boolean = false,
    val onlyBookmarkedGlossary: Boolean = false,
    val isModuleFlashcardMode: Boolean = false,
    val isGlossaryFlashcardMode: Boolean = false,
    val moduleFlashcardIndex: Int = 0,
    val isModuleFlashcardFlipped: Boolean = false,
    val moduleFlashcardShuffleSeed: Int = 0,
    val glossaryFlashcardIndex: Int = 0,
    val isGlossaryFlashcardFlipped: Boolean = false,
    val glossaryFlashcardShuffleSeed: Int = 0,
    val currentFlashcardIndex: Int = 0,
    val isFlashcardFlipped: Boolean = false,
    val flashcardShuffleSeed: Int = 0,
    // Dialog & Loading States
    val showInfoDialog: Boolean = false,
    val isLoading: Boolean = false,
    val loadingMessage: String? = null,
    val canNavigateBack: Boolean = false,
    val instantTransitions: Boolean = false,
    val enforceCharacterLocks: Boolean = false,
    val practiceExamStats: PracticeExamStats = PracticeExamStats(),
    val completedConsoleCases: Set<String> = emptySet()
) {
    // Evaluasi malas (lazy) agar penyalinan state saat geser slider tidak mengalokasikan HashSet baru
    val validNodeCodes: Set<String> by lazy(LazyThreadSafetyMode.NONE) {
        modulesList.flatMapTo(HashSet()) { m -> m.nodes.map { it.code } }
    }
    val totalNodesCount: Int by lazy(LazyThreadSafetyMode.NONE) { validNodeCodes.size }
    val completedCount: Int by lazy(LazyThreadSafetyMode.NONE) {
        completedNodeCodes.count { it in validNodeCodes }
    }

    val isFlashcardMode: Boolean
        get() = if (selectedModule != null) isModuleFlashcardMode else isGlossaryFlashcardMode

    fun isFlashcardBookmarked(card: CtFlashcard): Boolean =
        if (card.isGlossaryCard) {
            bookmarkedGlossaryTerms.contains(card.glossaryTermKey) ||
                bookmarkedGlossaryTerms.contains(card.frontTitle)
        } else {
            bookmarkedNodeCodes.contains(card.nodeCode)
        }

    val lastVisitedNode: CtNode
        get() = ResumeNodeResolver.findNode(modulesList, lastVisitedNodeCode)
            ?: modulesList.firstOrNull()?.nodes?.firstOrNull { it.level >= 1 }
            ?: modulesList.firstOrNull()?.nodes?.firstOrNull()
            ?: CtRepository.modules[0].nodes[1]

    val filteredQuizzes: List<CtQuizQuestion>
        get() = quizzesList.filter { q ->
            val filter = quizModuleFilter
            val matchesMod = filter == "SEMUA" ||
                q.moduleId == filter ||
                (filter.length <= 3 && q.moduleId.startsWith(filter)) ||
                (filter.length <= 3 && q.nodeCode.startsWith(filter)) ||
                q.nodeCode.startsWith(filter)
            val matchesLevel = quizLevelFilter == 0 || q.level == quizLevelFilter
            matchesMod && matchesLevel
        }.map { QuizShuffle.shuffled(it) }

    // Parameter Turunan Klinis CT Scan (sumber tunggal: CtPhysicsCalculator)
    val effectiveMas: Float
        get() = CtPhysicsCalculator.effectiveMas(tubeMa, rotationTimeSec, helicalPitch)

    val pixelSizeMm: Float
        get() = CtPhysicsCalculator.pixelSizeMm(displayFovMm, reconstructionMatrix)

    val ctdiVolMgy: Float
        get() = CtPhysicsCalculator.ctdiVolMgy(tubeKvp, effectiveMas, isHeadPhantom)

    val dlpMgyCm: Float
        get() = CtPhysicsCalculator.dlpMgyCm(ctdiVolMgy, scanLengthCm)

    val effectiveDoseMsv: Float
        get() = CtPhysicsCalculator.effectiveDoseMsv(dlpMgyCm, isHeadPhantom)

    val isDrlExceeded: Boolean
        get() {
            val drlLimit = if (isHeadPhantom) CtRepository.DRL_HEAD_CTDI_MGY else CtRepository.DRL_BODY_CTDI_MGY
            return ctdiVolMgy > drlLimit
        }

    val estimatedNoiseSdHu: Float
        get() = CtPhysicsCalculator.relativeNoiseSdHu(tubeKvp, effectiveMas, isIterativeReconActive)

    val calculatedHu: Int
        get() = CtPhysicsCalculator.calculateHu(selectedTissue.muPerCmAt120kVp)

    val windowRangeSummary: String
        get() {
            val low = (windowLevelHu - windowWidthHu / 2f).roundToInt()
            val high = (windowLevelHu + windowWidthHu / 2f).roundToInt()
            return "$low s.d. $high HU"
        }
}

class CtViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    // Konstruktor sekunder untuk kompatibilitas tes unit yang memanggil CtViewModel(application)
    constructor(application: Application) : this(application, SavedStateHandle())

    private val repository = UserDataRepository(application)

    private val initialTab: MainTab = savedStateHandle.get<String>(KEY_CURRENT_TAB)
        ?.let { runCatching { MainTab.valueOf(it) }.getOrNull() }
        ?: MainTab.BERANDA

    private val initialSimType: CtSimType = savedStateHandle.get<String>(KEY_SELECTED_SIM_TYPE)
        ?.let { runCatching { CtSimType.valueOf(it) }.getOrNull() }
        ?: CtSimType.GANTRY_RANTAI_SINYAL

    private val initialModule: CtModule? = savedStateHandle.get<String>(KEY_SELECTED_MODULE_ID)
        ?.let { modId -> CtRepository.modules.find { it.id == modId } }

    private val initialQuizModFilter: String = savedStateHandle.get<String>(KEY_QUIZ_MOD_FILTER) ?: "SEMUA"
    private val initialQuizLevelFilter: Int = savedStateHandle.get<Int>(KEY_QUIZ_LEVEL_FILTER) ?: 0
    private val initialNoteCategory: String = savedStateHandle.get<String>(KEY_NOTE_CATEGORY) ?: "SEMUA"

    private val _uiState = MutableStateFlow(
        CtUiState(
            currentTab = initialTab,
            selectedModule = initialModule,
            selectedSimType = initialSimType,
            quizModuleFilter = initialQuizModFilter,
            quizLevelFilter = initialQuizLevelFilter,
            selectedNoteCategory = initialNoteCategory
        )
    )
    val uiState: StateFlow<CtUiState> = _uiState.asStateFlow()

    private var latestStudyDays: List<StudyDayEntity> = emptyList()

    private data class NavSession(
        val tab: MainTab,
        val moduleId: String?,
        val simType: CtSimType,
        val nodeLevelFilter: Int,
        val onlyBookmarkedNodes: Boolean,
        val quizModuleFilter: String,
        val quizLevelFilter: Int,
        val currentQuizIndex: Int,
        val selectedCtActivityId: String,
        val screenKey: String,
        val scrollOffset: Int
    )

    private val navigationHistory = ArrayDeque<NavSession>()
    private val screenScrollOffsets = mutableMapOf<String, Int>()

    private fun resolveScreenKey(state: CtUiState): String {
        val mod = state.selectedModule
        return when {
            mod != null -> "module_${mod.id}"
            state.currentTab == MainTab.SIMULASI -> "sim_${state.selectedSimType.name}"
            else -> "tab_${state.currentTab.name.lowercase()}"
        }
    }

    fun getSavedScrollOffset(screenKey: String): Int = screenScrollOffsets[screenKey] ?: 0

    fun updateScrollOffset(screenKey: String, offset: Int) {
        screenScrollOffsets[screenKey] = offset
    }

    private fun pushCurrentSession() {
        val current = _uiState.value
        val key = resolveScreenKey(current)
        val snap = NavSession(
            tab = current.currentTab,
            moduleId = current.selectedModule?.id,
            simType = current.selectedSimType,
            nodeLevelFilter = current.nodeLevelFilter,
            onlyBookmarkedNodes = current.onlyBookmarkedNodes,
            quizModuleFilter = current.quizModuleFilter,
            quizLevelFilter = current.quizLevelFilter,
            currentQuizIndex = current.currentQuizIndex,
            selectedCtActivityId = current.selectedCtActivityId,
            screenKey = key,
            scrollOffset = screenScrollOffsets[key] ?: 0
        )
        if (navigationHistory.lastOrNull() != snap) {
            navigationHistory.addLast(snap)
            if (navigationHistory.size > 24) {
                navigationHistory.removeFirst()
            }
        }
    }

    companion object {
        private const val KEY_CURRENT_TAB = "saved_current_tab"
        private const val KEY_SELECTED_MODULE_ID = "saved_selected_module_id"
        private const val KEY_SELECTED_SIM_TYPE = "saved_selected_sim_type"
        private const val KEY_QUIZ_MOD_FILTER = "saved_quiz_mod_filter"
        private const val KEY_QUIZ_LEVEL_FILTER = "saved_quiz_level_filter"
        private const val KEY_NOTE_CATEGORY = "saved_note_category"
        const val MAX_USER_NAME_LENGTH = 40
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.seedCurriculumIfNeeded()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("CtViewModel", "gagal seed kurikulum", e)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.normalizeLegacyNodeCodesIfNeeded()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("CtViewModel", "gagal menormalkan kode lama", e)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.seededNotesFlow.collect { seeded ->
                if (!seeded) {
                    val count = repository.getNotesCount()
                    if (count == 0) {
                        CtRepository.defaultNotes.forEach { defaultNote ->
                            repository.insertNote(
                                StudyNoteEntity(
                                    id = defaultNote.id,
                                    title = defaultNote.title,
                                    content = defaultNote.content,
                                    category = defaultNote.category,
                                    createdAt = System.currentTimeMillis(),
                                    updatedAt = System.currentTimeMillis()
                                )
                            )
                        }
                    }
                    repository.setSeededNotes(true)
                }
            }
        }

        viewModelScope.launch {
            repository.observeCurriculumNodes()
                .map { nodeEntities ->
                    if (nodeEntities.isEmpty()) null else {
                        val groupedByModule = nodeEntities.groupBy { it.moduleId }
                        CtRepository.modules.map { baseMod ->
                            val orderMap = baseMod.nodes.mapIndexed { idx, n -> n.code to idx }.toMap()
                            val dbNodes = groupedByModule[baseMod.id]
                                ?.map { it.toDomainModel() }
                                ?.sortedBy { orderMap[it.code] ?: Int.MAX_VALUE }
                            if (!dbNodes.isNullOrEmpty()) {
                                baseMod.copy(nodes = dbNodes)
                            } else {
                                baseMod
                            }
                        }
                    }
                }
                .flowOn(Dispatchers.Default)
                .collect { updatedModules ->
                    if (updatedModules != null) {
                        _uiState.update { current ->
                            val updatedSelected = current.selectedModule?.let { sel ->
                                updatedModules.find { it.id == sel.id } ?: sel
                            }
                            current.copy(
                                modulesList = updatedModules,
                                selectedModule = updatedSelected
                            )
                        }
                    }
                }
        }

        viewModelScope.launch {
            repository.observeCurriculumQuizzes()
                .map { quizEntities ->
                    if (quizEntities.isEmpty()) null else {
                        val mappedQuizzes = quizEntities.map { it.toDomainModel() }
                        val orderMap = CtRepository.quizzes.mapIndexed { index, q -> q.id to index }.toMap()
                        mappedQuizzes.sortedBy { orderMap[it.id] ?: Int.MAX_VALUE }
                    }
                }
                .flowOn(Dispatchers.Default)
                .collect { sortedQuizzes ->
                    if (sortedQuizzes != null) {
                        _uiState.update { it.copy(quizzesList = sortedQuizzes) }
                    }
                }
        }

        viewModelScope.launch {
            repository.observeCompletedNodes().collect { nodes ->
                val codes = nodes.map { it.nodeCode }.toSet()
                _uiState.update { it.copy(completedNodeCodes = codes) }
            }
        }

        viewModelScope.launch {
            repository.observeNotes().collect { noteEntities ->
                val notes = noteEntities.map { entity ->
                    StudyNote(
                        id = entity.id,
                        title = entity.title,
                        content = entity.content,
                        category = entity.category,
                        dateString = android.text.format.DateFormat.format("dd MMM yyyy", entity.updatedAt).toString()
                    )
                }
                _uiState.update { it.copy(notesList = notes) }
            }
        }

        viewModelScope.launch {
            repository.observeQuizAttempts().collect { attempts ->
                _uiState.update { it.copy(quizAttempts = attempts) }
            }
        }

        viewModelScope.launch {
            repository.observeStudyDays().collect { days ->
                latestStudyDays = days
                recomputeStreak(days)
            }
        }

        viewModelScope.launch {
            repository.userNameFlow.collect { name ->
                _uiState.update { it.copy(userName = name) }
            }
        }

        viewModelScope.launch {
            repository.userAvatarIdFlow.collect { avatar ->
                _uiState.update { it.copy(userAvatarId = avatar) }
            }
        }

        viewModelScope.launch {
            repository.bookmarkedNodesFlow.collect { bookmarks ->
                _uiState.update { it.copy(bookmarkedNodeCodes = bookmarks) }
            }
        }

        viewModelScope.launch {
            repository.bookmarkedGlossaryFlow.collect { bookmarks ->
                _uiState.update { it.copy(bookmarkedGlossaryTerms = bookmarks) }
            }
        }

        viewModelScope.launch {
            repository.masteredFlashcardsFlow.collect { mastered ->
                _uiState.update { it.copy(masteredFlashcardIds = mastered) }
            }
        }

        viewModelScope.launch {
            repository.lastVisitedNodeCodeFlow.collect { code ->
                _uiState.update { it.copy(lastVisitedNodeCode = code, isResumeRestored = true) }
            }
        }

        viewModelScope.launch {
            repository.characterCustomizationFlow.collect { charState ->
                _uiState.update { it.copy(characterCustomization = charState, userAvatarId = charState.avatarId) }
            }
        }

        viewModelScope.launch {
            repository.instantTransitionsFlow.collect { instant ->
                _uiState.update { it.copy(instantTransitions = instant) }
            }
        }

        viewModelScope.launch {
            repository.enforceCharacterLocksFlow.collect { locked ->
                _uiState.update { it.copy(enforceCharacterLocks = locked) }
            }
        }

        viewModelScope.launch {
            repository.practiceExamStatsFlow.collect { stats ->
                _uiState.update { it.copy(practiceExamStats = stats) }
            }
        }

        viewModelScope.launch {
            repository.completedConsoleCasesFlow.collect { cases ->
                _uiState.update { it.copy(completedConsoleCases = cases) }
            }
        }
    }

    private fun recomputeStreak(days: List<StudyDayEntity> = latestStudyDays, today: LocalDate = LocalDate.now()) {
        val studyDaysSet = days.mapNotNull { day ->
            runCatching { LocalDate.parse(day.dateIso) }.getOrNull()
        }.toSet()
        val streak = StreakCalculator.compute(studyDaysSet, today)
        _uiState.update { it.copy(studyStreakDays = streak) }
    }

    fun refreshStreak(today: LocalDate = LocalDate.now()) {
        recomputeStreak(latestStudyDays, today)
    }

    private fun rememberLastVisited(code: String) {
        if (code.isBlank() || !ResumeNodeResolver.isKnownNode(_uiState.value.modulesList, code)) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.setLastVisitedNodeCode(code)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("CtViewModel", "gagal menyimpan posisi terakhir", e)
            }
        }
    }

    private var transitionJob: Job? = null

    fun triggerSectionTransition(destinationName: String? = null, onStart: () -> Unit = {}) {
        transitionJob?.cancel()
        onStart()
        if (_uiState.value.instantTransitions) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    loadingMessage = null
                )
            }
            return
        }
        _uiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = destinationName
            )
        }
        transitionJob = viewModelScope.launch {
            delay(5100L)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    loadingMessage = null
                )
            }
        }
    }

    fun finishLoading() {
        transitionJob?.cancel()
        _uiState.update {
            it.copy(
                isLoading = false,
                loadingMessage = null
            )
        }
    }

    fun selectTab(tab: MainTab) {
        if (_uiState.value.currentTab == tab && _uiState.value.selectedModule == null && !_uiState.value.isLoading) return
        pushCurrentSession()
        val tabName = when (tab) {
            MainTab.BERANDA -> "Beranda"
            MainTab.ANIMASI -> "Animasi Belajar"
            MainTab.SIMULASI -> "Laboratorium Simulasi CT"
            MainTab.TERSIMPAN -> "Materi & Glosarium Tersimpan"
            MainTab.CATATAN -> "Catatan Klinis"
            MainTab.GLOSARIUM -> "Glosarium Radiologi CT"
            MainTab.KUIS -> "Uji Kompetensi"
            MainTab.KARAKTER -> "Ruang Karakter"
        }
        savedStateHandle[KEY_CURRENT_TAB] = tab.name
        savedStateHandle[KEY_SELECTED_MODULE_ID] = null
        triggerSectionTransition(tabName) {
            _uiState.update {
                it.copy(
                    currentTab = tab,
                    selectedModule = null,
                    canNavigateBack = navigationHistory.isNotEmpty(),
                    isModuleFlashcardMode = false,
                    currentFlashcardIndex = it.glossaryFlashcardIndex,
                    isFlashcardFlipped = it.isGlossaryFlashcardFlipped,
                    flashcardShuffleSeed = it.glossaryFlashcardShuffleSeed
                )
            }
        }
    }

    fun updateCharacterCustomization(state: CharacterCustomizationState) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateCharacterCustomization(state)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("CtViewModel", "gagal memperbarui kustomisasi karakter", e)
            }
        }
    }

    fun resetCharacterCustomization() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val defaultState = CharacterCustomizationState()
                repository.updateCharacterCustomization(defaultState)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("CtViewModel", "gagal mereset kustomisasi karakter", e)
            }
        }
    }

    // --- Streak Belajar ---
    fun setStreakDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showStreakDialog = visible) }
    }

    // --- Avatar Pengguna ---
    fun setAvatarDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showAvatarDialog = visible) }
    }

    fun updateAvatar(avatarId: String, name: String) {
        val normalizedName = name.trim().take(MAX_USER_NAME_LENGTH)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateUserAvatarId(avatarId)
                repository.updateUserName(normalizedName)
                _uiState.update { it.copy(showAvatarDialog = false) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("CtViewModel", "gagal memperbarui avatar", e)
            }
        }
    }

    // --- Catatan Belajar (Notes) ---
    fun setNoteCategory(category: String) {
        savedStateHandle[KEY_NOTE_CATEGORY] = category
        _uiState.update { it.copy(selectedNoteCategory = category) }
    }

    fun openNoteDialog(note: StudyNote? = null) {
        _uiState.update { it.copy(showNoteDialog = true, editingNote = note) }
    }

    fun closeNoteDialog() {
        _uiState.update { it.copy(showNoteDialog = false, editingNote = null) }
    }

    fun saveNote(
        id: String?,
        title: String,
        content: String,
        category: String,
        nodeCode: String? = null,
        moduleId: String? = null
    ) {
        val cleanTitle = title.trim()
        val cleanContent = content.trim()
        if (cleanTitle.isEmpty() && cleanContent.isEmpty()) {
            closeNoteDialog()
            return
        }
        val finalTitle = cleanTitle.ifEmpty { "Catatan Tanpa Judul" }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val noteId = id ?: UUID.randomUUID().toString()
                val now = System.currentTimeMillis()
                val existingNote = if (id != null) repository.getNoteById(id) else null
                val preservedCreatedAt = existingNote?.createdAt ?: now
                val resolvedNodeCode = nodeCode ?: existingNote?.nodeCode
                val resolvedMod = moduleId
                    ?: existingNote?.moduleId
                    ?: if (resolvedNodeCode?.contains(".") == true) resolvedNodeCode.substringBefore(".") else null
                val entity = StudyNoteEntity(
                    id = noteId,
                    title = finalTitle,
                    content = cleanContent,
                    category = category,
                    createdAt = preservedCreatedAt,
                    updatedAt = now,
                    nodeCode = resolvedNodeCode,
                    moduleId = resolvedMod
                )
                repository.insertNote(entity)
                closeNoteDialog()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("CtViewModel", "gagal menyimpan catatan", e)
            }
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteNote(id)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("CtViewModel", "gagal menghapus catatan", e)
            }
        }
    }

    fun exportNotesToJson(): String {
        val currentState = _uiState.value
        val root = org.json.JSONObject()
        root.put("app", "CT-Learn")
        root.put("exportedAt", LocalDate.now().toString())
        root.put("userName", currentState.userName.ifBlank { "Radiografer CT" })
        root.put("totalNotes", currentState.notesList.size)
        val notesArray = org.json.JSONArray()
        currentState.notesList.forEach { note ->
            val item = org.json.JSONObject()
            item.put("id", note.id)
            item.put("title", note.title)
            item.put("category", note.category)
            item.put("content", note.content)
            item.put("dateString", note.dateString)
            notesArray.put(item)
        }
        root.put("notes", notesArray)
        return root.toString(2)
    }

    // --- Simpul Kurikulum & Progress ---
    fun openModuleDetail(module: CtModule) {
        val resolvedModule = _uiState.value.modulesList.find { it.id == module.id } ?: module
        if (_uiState.value.selectedModule?.id != resolvedModule.id) {
            pushCurrentSession()
        }
        savedStateHandle[KEY_SELECTED_MODULE_ID] = resolvedModule.id
        triggerSectionTransition("Modul ${resolvedModule.code}: ${resolvedModule.title}") {
            _uiState.update {
                it.copy(
                    selectedModule = resolvedModule,
                    canNavigateBack = navigationHistory.isNotEmpty(),
                    nodeLevelFilter = 0,
                    onlyBookmarkedNodes = false,
                    isModuleFlashcardMode = false,
                    moduleFlashcardIndex = 0,
                    isModuleFlashcardFlipped = false,
                    moduleFlashcardShuffleSeed = 0,
                    currentFlashcardIndex = 0,
                    isFlashcardFlipped = false,
                    flashcardShuffleSeed = 0
                )
            }
        }
        ResumeNodeResolver.codeToRememberOnOpen(resolvedModule, _uiState.value.lastVisitedNodeCode)?.let {
            rememberLastVisited(it)
        }
    }

    fun navigateBack() {
        if (navigationHistory.isNotEmpty()) {
            val prev = navigationHistory.removeLast()
            val prevModule = prev.moduleId?.let { id ->
                _uiState.value.modulesList.find { it.id == id }
                    ?: CtRepository.modules.find { it.id == id }
            }
            savedStateHandle[KEY_CURRENT_TAB] = prev.tab.name
            savedStateHandle[KEY_SELECTED_MODULE_ID] = prevModule?.id
            savedStateHandle[KEY_SELECTED_SIM_TYPE] = prev.simType.name
            savedStateHandle[KEY_QUIZ_MOD_FILTER] = prev.quizModuleFilter
            savedStateHandle[KEY_QUIZ_LEVEL_FILTER] = prev.quizLevelFilter
            screenScrollOffsets[prev.screenKey] = prev.scrollOffset
            val destLabel = when {
                prevModule != null -> "Modul ${prevModule.code}: ${prevModule.title}"
                prev.tab == MainTab.BERANDA -> "Menu Beranda"
                prev.tab == MainTab.SIMULASI -> "Laboratorium Simulasi CT"
                prev.tab == MainTab.CATATAN -> "Catatan Klinis"
                prev.tab == MainTab.KUIS -> "Uji Kompetensi"
                prev.tab == MainTab.KARAKTER -> "Ruang Karakter"
                prev.tab == MainTab.ANIMASI -> "Animasi Belajar"
                prev.tab == MainTab.TERSIMPAN -> "Materi & Glosarium Tersimpan"
                prev.tab == MainTab.GLOSARIUM -> "Glosarium Radiologi CT"
                else -> "Sesi Sebelumnya"
            }
            triggerSectionTransition(destLabel) {
                _uiState.update {
                    it.copy(
                        currentTab = prev.tab,
                        selectedModule = prevModule,
                        selectedSimType = prev.simType,
                        nodeLevelFilter = prev.nodeLevelFilter,
                        onlyBookmarkedNodes = prev.onlyBookmarkedNodes,
                        quizModuleFilter = prev.quizModuleFilter,
                        quizLevelFilter = prev.quizLevelFilter,
                        currentQuizIndex = prev.currentQuizIndex,
                        selectedCtActivityId = prev.selectedCtActivityId,
                        canNavigateBack = navigationHistory.isNotEmpty(),
                        isModuleFlashcardMode = false,
                        currentFlashcardIndex = it.glossaryFlashcardIndex,
                        isFlashcardFlipped = it.isGlossaryFlashcardFlipped,
                        flashcardShuffleSeed = it.glossaryFlashcardShuffleSeed
                    )
                }
            }
        } else if (_uiState.value.selectedModule != null) {
            savedStateHandle[KEY_SELECTED_MODULE_ID] = null
            triggerSectionTransition("Menu Beranda") {
                _uiState.update {
                    it.copy(
                        selectedModule = null,
                        canNavigateBack = false,
                        isModuleFlashcardMode = false,
                        currentFlashcardIndex = it.glossaryFlashcardIndex,
                        isFlashcardFlipped = it.isGlossaryFlashcardFlipped,
                        flashcardShuffleSeed = it.glossaryFlashcardShuffleSeed
                    )
                }
            }
        } else if (_uiState.value.currentTab != MainTab.BERANDA) {
            savedStateHandle[KEY_CURRENT_TAB] = MainTab.BERANDA.name
            savedStateHandle[KEY_SELECTED_MODULE_ID] = null
            triggerSectionTransition("Menu Beranda") {
                _uiState.update {
                    it.copy(
                        currentTab = MainTab.BERANDA,
                        selectedModule = null,
                        canNavigateBack = false,
                        isModuleFlashcardMode = false
                    )
                }
            }
        }
    }

    fun closeModuleDetail() {
        navigateBack()
    }

    fun resumeStudy() {
        val targetNode = _uiState.value.lastVisitedNode
        val modId = ResumeNodeResolver.moduleIdOf(_uiState.value.modulesList, targetNode.code)
            ?: targetNode.code.substringBefore(".").ifBlank { "M01" }
        val targetModule = _uiState.value.modulesList.find { it.id == modId }
            ?: _uiState.value.modulesList.find { it.id == targetNode.code.substringBefore(".") }
            ?: _uiState.value.modulesList.first()
        openModuleDetail(targetModule)
    }

    fun setNodeLevelFilter(level: Int) {
        _uiState.update {
            it.copy(
                nodeLevelFilter = level,
                moduleFlashcardIndex = 0,
                isModuleFlashcardFlipped = false,
                currentFlashcardIndex = if (it.selectedModule != null) 0 else it.currentFlashcardIndex,
                isFlashcardFlipped = if (it.selectedModule != null) false else it.isFlashcardFlipped
            )
        }
    }

    fun setNodeMode(nodeCode: String, mode: String) {
        _uiState.update {
            it.copy(nodeModeMap = it.nodeModeMap + (nodeCode to mode))
        }
    }

    fun setGlobalNodeMode(module: CtModule, mode: String) {
        _uiState.update { current ->
            val updatedMap = current.nodeModeMap.toMutableMap()
            module.nodes.forEach { node ->
                updatedMap[node.code] = mode
            }
            current.copy(
                globalNodeMode = mode,
                nodeModeMap = updatedMap
            )
        }
    }

    fun openModuleQuiz(moduleId: String, level: Int = 0) {
        pushCurrentSession()
        savedStateHandle[KEY_SELECTED_MODULE_ID] = null
        savedStateHandle[KEY_CURRENT_TAB] = MainTab.KUIS.name
        savedStateHandle[KEY_QUIZ_MOD_FILTER] = moduleId
        savedStateHandle[KEY_QUIZ_LEVEL_FILTER] = level
        _uiState.update {
            it.copy(
                selectedModule = null,
                currentTab = MainTab.KUIS,
                canNavigateBack = navigationHistory.isNotEmpty(),
                quizModuleFilter = moduleId,
                quizLevelFilter = level,
                currentQuizIndex = 0,
                selectedOptionIndex = null,
                isAnswerSubmitted = false,
                quizAnswers = emptyMap(),
                answeredQuizMap = emptyMap(),
                quizScore = 0,
                isQuizFinished = false
            )
        }
    }

    fun toggleNodeCompletion(nodeCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val normalizedCode = LegacyNodeCodes.normalize(nodeCode)
                val resolvedModuleId = when {
                    normalizedCode.startsWith("M03A.") -> "M03A"
                    normalizedCode.startsWith("M03B.") -> "M03B"
                    normalizedCode.startsWith("M03C.") -> "M03C"
                    normalizedCode.contains(".") -> normalizedCode.substringBefore(".")
                    else -> ""
                }
                val todayIso = LocalDate.now().toString()
                val nowCompleted = repository.toggleNodeCompletionTransactional(
                    normalizedCode = normalizedCode,
                    resolvedModuleId = resolvedModuleId,
                    dateIso = todayIso
                )
                if (nowCompleted) {
                    rememberLastVisited(normalizedCode)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("CtViewModel", "gagal mengubah status simpul: $nodeCode", e)
            }
        }
    }

    private val pendingNodeCompletions = java.util.Collections.synchronizedSet(HashSet<String>())

    fun markNodeCompleted(nodeCode: String) {
        val normalizedCode = LegacyNodeCodes.normalize(nodeCode.trim())
        if (normalizedCode.isBlank()) return
        if (_uiState.value.completedNodeCodes.contains(normalizedCode)) return
        if (!pendingNodeCompletions.add(normalizedCode)) return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newlyMarked = repository.markNodeCompletedIfNotYet(normalizedCode)
                if (newlyMarked) {
                    rememberLastVisited(normalizedCode)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("CtViewModel", "gagal menandai simpul selesai: $normalizedCode", e)
            } finally {
                pendingNodeCompletions.remove(normalizedCode)
            }
        }
    }

    // --- Bookmark Materi & Glosarium ---
    fun toggleNodeBookmark(nodeCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.toggleNodeBookmark(nodeCode)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("CtViewModel", "gagal mengubah markah simpul: $nodeCode", e)
            }
        }
    }

    fun removeNodeBookmark(nodeCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.removeNodeBookmark(nodeCode)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("CtViewModel", "gagal menghapus markah simpul: $nodeCode", e)
            }
        }
    }

    fun toggleGlossaryBookmark(term: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.toggleGlossaryBookmark(term)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("CtViewModel", "gagal mengubah markah glosarium: $term", e)
            }
        }
    }

    fun removeGlossaryBookmark(term: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.removeGlossaryBookmark(term)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("CtViewModel", "gagal menghapus markah glosarium: $term", e)
            }
        }
    }

    fun clearAllBookmarks() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.clearAllBookmarks()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("CtViewModel", "gagal mengosongkan semua markah", e)
            }
        }
    }

    fun toggleFlashcardBookmark(card: CtFlashcard) {
        if (card.isGlossaryCard) {
            toggleGlossaryBookmark(card.glossaryTermKey)
        } else {
            toggleNodeBookmark(card.nodeCode)
        }
    }

    fun setOnlyBookmarkedNodes(onlyBookmarked: Boolean) {
        _uiState.update {
            it.copy(
                onlyBookmarkedNodes = onlyBookmarked,
                moduleFlashcardIndex = 0,
                isModuleFlashcardFlipped = false,
                currentFlashcardIndex = if (it.selectedModule != null) 0 else it.currentFlashcardIndex,
                isFlashcardFlipped = if (it.selectedModule != null) false else it.isFlashcardFlipped
            )
        }
    }

    fun setOnlyBookmarkedGlossary(onlyBookmarked: Boolean) {
        _uiState.update {
            it.copy(
                onlyBookmarkedGlossary = onlyBookmarked,
                glossaryFlashcardIndex = 0,
                isGlossaryFlashcardFlipped = false,
                currentFlashcardIndex = if (it.selectedModule == null) 0 else it.currentFlashcardIndex,
                isFlashcardFlipped = if (it.selectedModule == null) false else it.isFlashcardFlipped
            )
        }
    }

    // --- Flashcards Hafalan Cepat ---
    fun setFlashcardMode(enabled: Boolean) {
        if (_uiState.value.selectedModule != null) {
            setModuleFlashcardMode(enabled)
        } else {
            setGlossaryFlashcardMode(enabled)
        }
    }

    fun setModuleFlashcardMode(enabled: Boolean) {
        _uiState.update {
            it.copy(
                isModuleFlashcardMode = enabled,
                currentFlashcardIndex = it.moduleFlashcardIndex,
                isFlashcardFlipped = it.isModuleFlashcardFlipped,
                flashcardShuffleSeed = it.moduleFlashcardShuffleSeed
            )
        }
    }

    fun setGlossaryFlashcardMode(enabled: Boolean) {
        _uiState.update {
            it.copy(
                isGlossaryFlashcardMode = enabled,
                currentFlashcardIndex = it.glossaryFlashcardIndex,
                isFlashcardFlipped = it.isGlossaryFlashcardFlipped,
                flashcardShuffleSeed = it.glossaryFlashcardShuffleSeed
            )
        }
    }

    private fun isModuleDeckActive(state: CtUiState): Boolean =
        state.selectedModule != null && state.isModuleFlashcardMode

    fun flipFlashcard() {
        _uiState.update { state ->
            if (isModuleDeckActive(state)) {
                val newFlipped = !state.isModuleFlashcardFlipped
                state.copy(
                    isModuleFlashcardFlipped = newFlipped,
                    isFlashcardFlipped = newFlipped
                )
            } else {
                val newFlipped = !state.isGlossaryFlashcardFlipped
                state.copy(
                    isGlossaryFlashcardFlipped = newFlipped,
                    isFlashcardFlipped = newFlipped
                )
            }
        }
    }

    fun nextFlashcard(deckSize: Int) {
        if (deckSize <= 0) return
        _uiState.update { state ->
            if (isModuleDeckActive(state)) {
                val next = (state.moduleFlashcardIndex + 1) % deckSize
                state.copy(
                    moduleFlashcardIndex = next,
                    isModuleFlashcardFlipped = false,
                    currentFlashcardIndex = next,
                    isFlashcardFlipped = false
                )
            } else {
                val next = (state.glossaryFlashcardIndex + 1) % deckSize
                state.copy(
                    glossaryFlashcardIndex = next,
                    isGlossaryFlashcardFlipped = false,
                    currentFlashcardIndex = next,
                    isFlashcardFlipped = false
                )
            }
        }
    }

    fun prevFlashcard(deckSize: Int) {
        if (deckSize <= 0) return
        _uiState.update { state ->
            if (isModuleDeckActive(state)) {
                val prev = if (state.moduleFlashcardIndex - 1 < 0) deckSize - 1 else state.moduleFlashcardIndex - 1
                state.copy(
                    moduleFlashcardIndex = prev,
                    isModuleFlashcardFlipped = false,
                    currentFlashcardIndex = prev,
                    isFlashcardFlipped = false
                )
            } else {
                val prev = if (state.glossaryFlashcardIndex - 1 < 0) deckSize - 1 else state.glossaryFlashcardIndex - 1
                state.copy(
                    glossaryFlashcardIndex = prev,
                    isGlossaryFlashcardFlipped = false,
                    currentFlashcardIndex = prev,
                    isFlashcardFlipped = false
                )
            }
        }
    }

    fun shuffleFlashcards() {
        _uiState.update { state ->
            if (isModuleDeckActive(state)) {
                val nextSeed = state.moduleFlashcardShuffleSeed + 1
                state.copy(
                    moduleFlashcardShuffleSeed = nextSeed,
                    moduleFlashcardIndex = 0,
                    isModuleFlashcardFlipped = false,
                    flashcardShuffleSeed = nextSeed,
                    currentFlashcardIndex = 0,
                    isFlashcardFlipped = false
                )
            } else {
                val nextSeed = state.glossaryFlashcardShuffleSeed + 1
                state.copy(
                    glossaryFlashcardShuffleSeed = nextSeed,
                    glossaryFlashcardIndex = 0,
                    isGlossaryFlashcardFlipped = false,
                    flashcardShuffleSeed = nextSeed,
                    currentFlashcardIndex = 0,
                    isFlashcardFlipped = false
                )
            }
        }
    }

    fun toggleFlashcardMastered(cardId: String) {
        val isNowMastered = !_uiState.value.masteredFlashcardIds.contains(cardId)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.toggleFlashcardMastered(cardId)
                if (isNowMastered) {
                    val todayIso = LocalDate.now().toString()
                    repository.recordStudyActivity(
                        dateIso = todayIso,
                        flashcardActivity = true
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("CtViewModel", "gagal mengubah status flashcard: $cardId", e)
            }
        }
    }

    // --- Animasi Belajar ---
    fun selectAnimation(index: Int) {
        _uiState.update { it.copy(selectedAnimationIndex = index, animationStep = 1) }
    }

    fun toggleAnimationPlay() {
        _uiState.update { it.copy(isAnimationPlaying = !it.isAnimationPlaying) }
    }

    fun setAnimationStep(step: Int) {
        _uiState.update { it.copy(animationStep = step.coerceIn(1, 4)) }
    }

    // --- Simulator CT Scan ---
    fun selectSimType(simType: CtSimType, recordHistory: Boolean = true) {
        if (recordHistory) {
            pushCurrentSession()
        }
        val simName = when (simType) {
            CtSimType.GANTRY_RANTAI_SINYAL -> "Simulator Anatomi Gantry & Rantai Sinyal CT"
            CtSimType.WINDOWING_HU_ATENUASI -> "Simulator Windowing HU & Atenuasi Sinar-X"
            CtSimType.MSCT_PITCH_REKON -> "Simulator Geometri MSCT, Pitch & Rekonstruksi"
            CtSimType.DOSIMETRI_BOLUS_TRACKING -> "Simulator Dosimetri CTDIvol/DLP & Bolus Tracking"
            CtSimType.ARTEFAK_KALIBRASI_QA -> "Laboratorium Artefak, Kalibrasi & QA CT"
        }
        savedStateHandle[KEY_CURRENT_TAB] = MainTab.SIMULASI.name
        savedStateHandle[KEY_SELECTED_MODULE_ID] = null
        savedStateHandle[KEY_SELECTED_SIM_TYPE] = simType.name
        triggerSectionTransition(simName) {
            _uiState.update {
                it.copy(
                    currentTab = MainTab.SIMULASI,
                    selectedModule = null,
                    selectedSimType = simType,
                    canNavigateBack = navigationHistory.isNotEmpty()
                )
            }
        }
    }

    fun launchLab(labType: String) {
        val targetSim = when (labType) {
            "ct_m02_anatomi_pesawat", "ct_m03_instrumentasi_rantai" -> CtSimType.GANTRY_RANTAI_SINYAL
            "ct_m01_atenuasi", "ct_m06_hu_windowing" -> CtSimType.WINDOWING_HU_ATENUASI
            "ct_m04_generasi_pitch", "ct_m05_rekonstruksi" -> CtSimType.MSCT_PITCH_REKON
            "ct_m07_dosimetri_kualitas", "ct_m08_kontras_3d" -> CtSimType.DOSIMETRI_BOLUS_TRACKING
            "ct_m09_artefak_qa" -> CtSimType.ARTEFAK_KALIBRASI_QA
            else -> CtSimType.WINDOWING_HU_ATENUASI
        }
        pushCurrentSession()
        _uiState.update {
            val base = it.copy(selectedCtActivityId = labType)
            when (labType) {
                "ct_m06_hu_windowing" -> base.copy(windowWidthHu = 80f, windowLevelHu = 40f, isHeadPhantom = true)
                "ct_m08_kontras_3d" -> base.copy(tubeKvp = 100, helicalPitch = 1.2f, isHeadPhantom = false)
                else -> base
            }
        }
        selectSimType(targetSim, recordHistory = false)
    }

    fun setInstantTransitions(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { repository.setInstantTransitions(enabled) }
        }
    }

    fun setEnforceCharacterLocks(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { repository.setEnforceCharacterLocks(enabled) }
        }
    }

    fun recordPracticeExamCompleted(
        scorePercent: Int,
        weakestDomainName: String?,
        domainBreakdown: List<com.example.util.CtDomainScore> = emptyList()
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.recordPracticeExamResult(scorePercent, weakestDomainName, domainBreakdown)
                val todayIso = LocalDate.now().toString()
                repository.recordStudyActivity(dateIso = todayIso, lastNodeCode = "TRYOUT_UKOM", lastModuleId = "UKOM")
                repository.incrementDailyShift(todayKey = todayIso, quizzesDelta = 1)
            }
        }
    }

    fun recordConsoleLabCompleted(caseId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.recordCompletedConsoleCase(caseId)
                val todayIso = LocalDate.now().toString()
                repository.recordStudyActivity(dateIso = todayIso, lastNodeCode = "SIM_${caseId.uppercase()}", lastModuleId = "M07")
                repository.incrementDailyShift(todayKey = todayIso, labsDelta = 1)
            }
        }
    }

    // --- Pengaturan Parameter Konsol CT ---
    fun selectTissue(tissue: CtTissueAttenuation) {
        _uiState.update { it.copy(selectedTissue = tissue) }
    }

    fun setTubeKvp(kvp: Int) {
        _uiState.update { it.copy(tubeKvp = kvp.coerceIn(70, 140)) }
    }

    fun setTubeMa(ma: Float) {
        _uiState.update { it.copy(tubeMa = ma.coerceIn(20f, 800f)) }
    }

    fun setRotationTime(sec: Float) {
        _uiState.update { it.copy(rotationTimeSec = sec.coerceIn(0.25f, 2.0f)) }
    }

    fun setHelicalPitch(pitch: Float) {
        _uiState.update { it.copy(helicalPitch = pitch.coerceIn(0.3f, 1.75f)) }
    }

    fun setScanLength(cm: Float) {
        _uiState.update { it.copy(scanLengthCm = cm.coerceIn(5f, 120f)) }
    }

    fun setHeadPhantom(isHead: Boolean) {
        _uiState.update { it.copy(isHeadPhantom = isHead) }
    }

    fun setWindowing(wwHu: Float, wlHu: Float) {
        _uiState.update {
            it.copy(
                windowWidthHu = wwHu.coerceIn(20f, 4000f),
                windowLevelHu = wlHu.coerceIn(-1000f, 2000f)
            )
        }
    }

    fun setDisplayFov(mm: Float) {
        _uiState.update { it.copy(displayFovMm = mm.coerceIn(80f, 500f)) }
    }

    fun setReconstructionMatrix(matrix: Int) {
        _uiState.update { it.copy(reconstructionMatrix = matrix.coerceIn(256, 1024)) }
    }

    fun setIterativeRecon(enabled: Boolean) {
        _uiState.update { it.copy(isIterativeReconActive = enabled) }
    }

    fun applyBrainWindowPreset() {
        _uiState.update { it.copy(windowWidthHu = 80f, windowLevelHu = 40f, isHeadPhantom = true) }
    }

    fun applyStrokeWindowPreset() {
        _uiState.update { it.copy(windowWidthHu = 40f, windowLevelHu = 40f, isHeadPhantom = true) }
    }

    fun applyLungWindowPreset() {
        _uiState.update { it.copy(windowWidthHu = 1500f, windowLevelHu = -600f, isHeadPhantom = false) }
    }

    fun applyBoneWindowPreset() {
        _uiState.update { it.copy(windowWidthHu = 2000f, windowLevelHu = 500f) }
    }

    // --- Kuis Kompetensi ---
    fun setQuizModuleFilter(moduleId: String) {
        savedStateHandle[KEY_QUIZ_MOD_FILTER] = moduleId
        _uiState.update {
            it.copy(
                quizModuleFilter = moduleId,
                currentQuizIndex = 0,
                selectedOptionIndex = null,
                isAnswerSubmitted = false,
                quizAnswers = emptyMap(),
                answeredQuizMap = emptyMap(),
                quizScore = 0,
                isQuizFinished = false
            )
        }
    }

    fun setQuizLevelFilter(level: Int) {
        savedStateHandle[KEY_QUIZ_LEVEL_FILTER] = level
        _uiState.update {
            it.copy(
                quizLevelFilter = level,
                currentQuizIndex = 0,
                selectedOptionIndex = null,
                isAnswerSubmitted = false,
                quizAnswers = emptyMap(),
                answeredQuizMap = emptyMap(),
                quizScore = 0,
                isQuizFinished = false
            )
        }
    }

    fun selectQuizOption(optionIndex: Int) {
        _uiState.update { state ->
            val currentQuiz = state.filteredQuizzes.getOrNull(state.currentQuizIndex)
            val alreadyAnswered = (currentQuiz != null && state.quizAnswers.containsKey(currentQuiz.id)) ||
                state.answeredQuizMap.containsKey(state.currentQuizIndex)
            if (state.isAnswerSubmitted || alreadyAnswered) {
                state
            } else {
                state.copy(selectedOptionIndex = optionIndex)
            }
        }
    }

    fun submitQuizAnswer() {
        val snapshot = _uiState.value
        val selected = snapshot.selectedOptionIndex ?: return
        val quizzes = snapshot.filteredQuizzes
        val qIdx = snapshot.currentQuizIndex
        val quiz = quizzes.getOrNull(qIdx) ?: return
        if (snapshot.isAnswerSubmitted || snapshot.quizAnswers.containsKey(quiz.id) || snapshot.answeredQuizMap.containsKey(qIdx)) {
            return
        }

        val wasCorrect = (selected == quiz.correctIndex)

        _uiState.update { state ->
            if (state.isAnswerSubmitted || state.quizAnswers.containsKey(quiz.id) || state.answeredQuizMap.containsKey(qIdx)) {
                return@update state
            }
            val updatedAnswers = state.quizAnswers + (quiz.id to selected)
            val updatedIndexMap = state.answeredQuizMap + (qIdx to selected)
            val newScore = quizzes.count { q -> updatedAnswers[q.id] == q.correctIndex }
            state.copy(
                isAnswerSubmitted = true,
                quizAnswers = updatedAnswers,
                answeredQuizMap = updatedIndexMap,
                quizScore = newScore
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertQuizAttempt(
                    QuizAttemptEntity(
                        quizId = quiz.id,
                        nodeCode = quiz.nodeCode,
                        moduleId = quiz.moduleId,
                        selectedIndex = selected,
                        isCorrect = wasCorrect,
                        answeredAt = System.currentTimeMillis()
                    )
                )
                val todayIso = LocalDate.now().toString()
                repository.recordStudyActivity(
                    dateIso = todayIso,
                    lastNodeCode = quiz.nodeCode,
                    lastModuleId = quiz.moduleId
                )
                rememberLastVisited(quiz.nodeCode)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("CtViewModel", "gagal menyimpan hasil kuis", e)
            }
        }
    }

    fun prevQuizQuestion() {
        _uiState.update { state ->
            val prevIndex = state.currentQuizIndex - 1
            if (prevIndex < 0) return@update state
            val prevQuiz = state.filteredQuizzes.getOrNull(prevIndex)
            val savedSelection = prevQuiz?.let { state.quizAnswers[it.id] } ?: state.answeredQuizMap[prevIndex]
            state.copy(
                currentQuizIndex = prevIndex,
                selectedOptionIndex = savedSelection,
                isAnswerSubmitted = savedSelection != null
            )
        }
    }

    fun nextQuizQuestion() {
        _uiState.update { state ->
            val quizzes = state.filteredQuizzes
            val nextIndex = state.currentQuizIndex + 1
            if (nextIndex < quizzes.size) {
                val nextQuiz = quizzes.getOrNull(nextIndex)
                val savedSelection = nextQuiz?.let { state.quizAnswers[it.id] } ?: state.answeredQuizMap[nextIndex]
                state.copy(
                    currentQuizIndex = nextIndex,
                    selectedOptionIndex = savedSelection,
                    isAnswerSubmitted = savedSelection != null
                )
            } else {
                state.copy(isQuizFinished = true)
            }
        }
    }

    fun restartQuiz() {
        _uiState.update {
            it.copy(
                currentQuizIndex = 0,
                selectedOptionIndex = null,
                isAnswerSubmitted = false,
                quizAnswers = emptyMap(),
                answeredQuizMap = emptyMap(),
                quizScore = 0,
                isQuizFinished = false
            )
        }
    }

    // --- Glosarium ---
    fun setGlossarySearch(query: String) {
        _uiState.update {
            it.copy(
                glossarySearchQuery = query,
                glossaryFlashcardIndex = 0,
                isGlossaryFlashcardFlipped = false,
                currentFlashcardIndex = if (it.selectedModule == null) 0 else it.currentFlashcardIndex,
                isFlashcardFlipped = if (it.selectedModule == null) false else it.isFlashcardFlipped
            )
        }
    }

    fun setGlossaryModuleFilter(modId: String) {
        _uiState.update {
            it.copy(
                glossaryModuleFilter = modId,
                glossaryFlashcardIndex = 0,
                isGlossaryFlashcardFlipped = false,
                currentFlashcardIndex = if (it.selectedModule == null) 0 else it.currentFlashcardIndex,
                isFlashcardFlipped = if (it.selectedModule == null) false else it.isFlashcardFlipped
            )
        }
    }

    fun setInfoDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showInfoDialog = visible) }
    }
}
