package com.nvmt.android.mtlibrary.base.recyclerview

import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nvmt.android.mtlibrary.base.common.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


abstract class BaseRefreshViewModel : BaseViewModel() {

    open val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        if (isLoading.value?.peekContent() == true
            || isRefreshing.value == true
        ) return@OnRefreshListener
        isRefreshing.value = true
        refreshData()
    }

    val isRefreshing = MutableLiveData<Boolean>(false)

    val isLoadMore = MutableLiveData<Boolean>().apply { value = false }

    /**
     * handle throwable when load fail
     */
    override suspend fun onLoadFail(throwable: Throwable) {
        super.onLoadFail(throwable)
        withContext(Dispatchers.Main) {
            super.onLoadFail(throwable)
            // reset load
            isRefreshing.value = false
            isLoadMore.value = false
        }
    }

    open fun refreshData() {
//        hideLoading()
        isRefreshing.value = false
    }
}