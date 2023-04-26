package com.translator.data

import com.translator.data.model.Language
import com.translator.data.model.TranslateRequestBody
import com.translator.data.model.TranslateResponse
import com.translator.data.remote.TranslatorApi
import retrofit2.http.Body

interface TranslationRepository {
    suspend fun getLanguages() : List<Language>
    suspend fun translate(@Body body: TranslateRequestBody) : TranslateResponse
}

class DefaultTranslationRepository constructor(
    private val translatorApi: TranslatorApi
) : TranslationRepository {
    override suspend fun getLanguages(): List<Language> {
        return translatorApi.getLanguages()
    }

    override suspend fun translate(body: TranslateRequestBody): TranslateResponse {
        return translatorApi.translate(body)
    }
}
