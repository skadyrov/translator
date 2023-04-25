package com.translator.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.translator.R
import com.translator.ui.mymodel.MyModelViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel by viewModels<MyModelViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.addMyModel("hi")

        val buttonTranslate = findViewById<Button>(R.id.btn_translate)
        val etTextToTranslate = findViewById<EditText>(R.id.et_text_to_translate)
        val tvTranslatedText = findViewById<TextView>(R.id.tv_translated_text)

        buttonTranslate.setOnClickListener {
            lifecycleScope.launch {
                val translatedText = viewModel.translate(etTextToTranslate.text.toString())
                tvTranslatedText.text = translatedText
            }
        }
    }
}
