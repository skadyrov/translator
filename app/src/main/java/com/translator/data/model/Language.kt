package com.translator.data.model

data class Language(
    val code: String,
    val name: String,
    val targets: List<String>
)