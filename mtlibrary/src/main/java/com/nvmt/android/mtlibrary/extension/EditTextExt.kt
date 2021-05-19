package com.nvmt.android.mtlibrary.extension

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.nvmt.android.mtlibrary.R
import com.nvmt.android.mtlibrary.base.MTConstant.DESTINATION_CHARACTERS
import com.nvmt.android.mtlibrary.base.MTConstant.SOURCE_CHARACTERS
import kotlinx.coroutines.*
import java.util.*

fun EditText.setOnlyClickListener(listener: () -> Unit, inputLayout: TextInputLayout? = null) {
    this.movementMethod = null
    this.keyListener = null
    this.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) this.performClick() }
    if (inputLayout != null) this.doAfterTextChanged { inputLayout.error = null }
    this.setOnClickListener {
        listener.invoke()
    }
}

fun EditText.setOnSearchDelayListener(listener: (String) -> Unit, delay: Long = 1500L) {
    val scope = CoroutineScope(context = Dispatchers.Main)
    var debounceJob: Job? = null

    var lastSearch: String? = null
    addTextChangedListener {
        if (it.isNullOrBlank() || it.toString() == lastSearch) return@addTextChangedListener
        lastSearch = it.toString()
        debounceJob?.cancel()
        debounceJob = scope.launch {
            delay(delay)
            listener.invoke(it.toString())
        }

    }
}

fun EditText.setOnSearchListener(listener: () -> Unit) {
    imeOptions = EditorInfo.IME_ACTION_SEARCH
    isSingleLine = true
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            context.hideKeyboard(this)
            listener.invoke()
            true
        }
        false
    }
}

fun EditText.setForceOnlyUpperCaseAlphaBet() {
    this.addTextChangedListener {
        val str = it.toString()
        val tag = this.getTag(R.string.unique_tag)
        if (str == tag) return@addTextChangedListener
        val newStr = removeAccent(str)
        this.setTag(R.string.unique_tag, newStr)
        this.setText(newStr)
        this.setSelection(newStr.length)
    }
}
