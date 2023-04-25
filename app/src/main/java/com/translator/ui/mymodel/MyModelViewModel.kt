package com.translator.ui.mymodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.translator.data.MyModelRepository
import com.translator.data.remote.TranslatorApi
import com.translator.ui.mymodel.MyModelUiState.Error
import com.translator.ui.mymodel.MyModelUiState.Loading
import com.translator.ui.mymodel.MyModelUiState.Success
import android.util.Log
import com.translator.data.model.TranslateRequestBody
import javax.inject.Inject

@HiltViewModel
class MyModelViewModel @Inject constructor(
    private val myModelRepository: MyModelRepository,
    private val translatorApi: TranslatorApi
) : ViewModel() {

    val uiState: StateFlow<MyModelUiState> = myModelRepository
        .myModels.map<List<String>, MyModelUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    init {
        viewModelScope.launch {
            Log.d("Languages: ", translatorApi.getLanguages().toString())
        }
    }

    fun addMyModel(name: String) {
        viewModelScope.launch {
            myModelRepository.add(name)
        }
    }

    suspend fun translate(text: String): String {
         return translatorApi.translate(TranslateRequestBody(
            q = text,
            source = "en",
            target = "ru"
        )).translatedText
    }
}

sealed interface MyModelUiState {
    object Loading : MyModelUiState
    data class Error(val throwable: Throwable) : MyModelUiState
    data class Success(val data: List<String>) : MyModelUiState
}
