package com.translator.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.translator.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel by viewModels<MainViewModel>()
    private lateinit var adapterFrom: LanguageAdapter
    private lateinit var adapterTo: LanguageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonTranslate = findViewById<Button>(R.id.btn_translate)
        val etTextToTranslate = findViewById<EditText>(R.id.et_text_to_translate)
        val tvTranslatedText = findViewById<TextView>(R.id.tv_translated_text)

        buttonTranslate.setOnClickListener {
            lifecycleScope.launch {
                val translatedText = viewModel.translate(etTextToTranslate.text.toString())
                tvTranslatedText.text = translatedText
            }
        }

        lifecycleScope.launch {
            viewModel.fromLanguages.collect { languages ->
                bindFromLanguages(languages)
            }
        }

        lifecycleScope.launch {
            viewModel.toLanguages.collect { languages ->
                bindToLanguages(languages)
            }
        }
    }

    private fun bindFromLanguages(languages: List<String>) {
        val spinnerFrom = findViewById<Spinner>(R.id.spinner_from_language)
        adapterFrom = LanguageAdapter(this, android.R.layout.simple_spinner_item, languages)
        spinnerFrom.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.setFromLanguage(languages[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
        spinnerFrom.adapter = adapterFrom
    }
    private fun bindToLanguages(languages: List<String>) {
        val spinnerTo = findViewById<Spinner>(R.id.spinner_to_language)
        adapterTo = LanguageAdapter(this, android.R.layout.simple_spinner_item, languages)
        spinnerTo.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.setToLanguage(languages[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
        spinnerTo.adapter = adapterTo
    }
}
