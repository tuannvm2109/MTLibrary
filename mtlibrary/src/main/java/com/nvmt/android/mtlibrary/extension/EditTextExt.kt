package com.nvmt.android.mtlibrary.extension

import android.text.Html
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import com.nvmt.android.mtlibrary.util.html.URLImageParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> throttleLatest(
    intervalMs: Long = 300L,
    coroutineScope: CoroutineScope,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var throttleJob: Job? = null
    var latestParam: T
    return { param: T ->
        latestParam = param
        if (throttleJob?.isCompleted != false) {
            throttleJob = coroutineScope.launch {
                delay(intervalMs)
                latestParam.let(destinationFunction)
            }
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

//var debounceJob: Job? = null
//var lastestStr: String? = null
//viewModel.agentName.observe(viewLifecycleOwner, Observer {
//    lastestStr = it
//    debounceJob?.cancel()
//    debounceJob = viewLifecycleOwner.lifecycleScope.launch {
//        delay(1500L)
//        lastestStr.let {
//            viewModel.findAgency()
//        }
//    }
//})