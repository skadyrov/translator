package com.translator.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.translator.data.TranslationRepository
import com.translator.data.model.Language
import com.translator.data.model.TranslateRequestBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val translationRepository: TranslationRepository,
) : ViewModel() {

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
            val languages = translationRepository.getLanguages()
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
         return translationRepository.translate(TranslateRequestBody(
            q = text,
            source = source,
            target = target
        )).translatedText
    }
}
