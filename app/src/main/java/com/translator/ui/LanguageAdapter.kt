package com.translator.ui

import android.content.Context
import android.widget.ArrayAdapter

class LanguageAdapter(context: Context, resource: Int, languages: List<String>) : ArrayAdapter<String>(context, resource, languages)