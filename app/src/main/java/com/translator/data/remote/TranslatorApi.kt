package com.translator.data.remote

import com.translator.data.model.Language
import retrofit2.http.GET

interface TranslatorApi {
    @GET("/languages")
    suspend fun getLanguages() : List<Language>
}