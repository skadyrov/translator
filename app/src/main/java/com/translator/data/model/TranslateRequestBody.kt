package com.translator.data.model

import com.google.gson.annotations.SerializedName
import com.translator.data.Constants

data class TranslateRequestBody(
    val q: String,
    val source: String,
    val target: String,
    val format: String = "text",

    @SerializedName("api_key")
    val apiKey: String = Constants.LIBRE_API_KEY
)