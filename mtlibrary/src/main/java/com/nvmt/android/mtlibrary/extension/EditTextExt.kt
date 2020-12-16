package com.nvmt.android.mtlibrary.extension

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