package com.translator.data.remote

import com.translator.data.model.Language
import com.translator.data.model.TranslateRequestBody
import com.translator.data.model.TranslateResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TranslatorApi {
    @GET("/languages")
    suspend fun getLanguages() : List<Language>

    @POST("/translate")
    suspend fun translate(@Body body: TranslateRequestBody) : TranslateResponse
}