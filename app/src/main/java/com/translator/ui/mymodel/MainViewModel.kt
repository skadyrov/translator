package com.translator.ui.mymodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.translator.data.MyModelRepository
import com.translator.data.model.Language
import com.translator.data.model.TranslateRequestBody
import com.translator.data.remote.TranslatorApi
import com.translator.ui.mymodel.MyModelUiState.Error
import com.translator.ui.mymodel.MyModelUiState.Loading
import com.translator.ui.mymodel.MyModelUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val myModelRepository: MyModelRepository,
    private val translatorApi: TranslatorApi
) : ViewModel() {

    val uiState: StateFlow<MyModelUiState> = myModelRepository
        .myModels.map<List<String>, MyModelUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    private var languagesMapByName: Map<String, Language> = emptyMap()
    private var languagesMapByCode: Map<String, Language> = emptyMap()

    private val _fromLanguages: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val fromLanguages: StateFlow<List<String>> = _fromLanguages

    private val _toLanguages: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val toLanguages: StateFlow<List<String>> = _toLanguages

    private var source = "en"
    private var target = "ar"

    init {
        viewModelScope.launch {
            val languages = translatorApi.getLanguages()
            languagesMapByName = languages.associateBy {
                it.name
            }
            languagesMapByCode = languages.associateBy {
                it.code
            }
            val languagesToDisplay = languages.map { it.name }
            _fromLanguages.emit(languagesToDisplay)
            _toLanguages.emit(languagesToDisplay)
        }
    }

    fun addMyModel(name: String) {
        viewModelScope.launch {
            myModelRepository.add(name)
        }
    }

    fun setFromLanguage(languageName: String) {
        viewModelScope.launch {
            languagesMapByName[languageName]?.let { language ->
                source = language.code
                val languagesWeCanTranslateTo = language.targets.map {  target ->
                    languagesMapByCode[target]!!.name
                }
                _toLanguages.emit(languagesWeCanTranslateTo)
            }
        }
    }
    fun setToLanguage(languageName: String) {
        target = languagesMapByName[languageName]!!.code
    }

    suspend fun translate(text: String): String {
         return translatorApi.translate(TranslateRequestBody(
            q = text,
            source = source,
            target = target
        )).translatedText
    }
}

sealed interface MyModelUiState {
    object Loading : MyModelUiState
    data class Error(val throwable: Throwable) : MyModelUiState
    data class Success(val data: List<String>) : MyModelUiState
}
