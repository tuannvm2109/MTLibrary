package com.nvmt.android.mtlibrary.base.recyclerview

import androidx.lifecycle.MutableLiveData
import com.nvmt.android.mtlibrary.base.MTConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


abstract class BaseLoadMoreRefreshViewModel() : BaseRefreshViewModel() {

    // current page
    private var currentPage = MutableLiveData<Int>().apply { value = getPreFirstPage() }

    // last page flag
    val isLastPage = MutableLiveData<Boolean>().apply { value = false }

    // scroll listener for recycler view
    val onScrollListener = object : EndlessRecyclerOnScrollListener(getLoadMoreThreshold()) {
        override fun onLoadMore() {
            if (isLoading.value?.peekContent() == true
                || isRefreshing.value == true
                || isLoadMore.value == true
                || isLastPage.value == true
            ) return
            isLoadMore.value = true
            loadMore()
        }
    }

    // item list
    var listItem = MutableLiveData<ArrayList<Any>>()

    // empty list flag
    val isEmptyList: MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * load data
     */
    abstract fun loadData(page: Int)

//    /**
//     * check first time load data
//     */
//    private fun isFirst() = currentPage.value == getPreFirstPage()
//            && listItem.value?.isEmpty() ?: true

    fun clearData() {
        listItem = MutableLiveData<ArrayList<Any>>()
        currentPage = MutableLiveData<Int>().apply { value = getPreFirstPage() }
    }

//    /**
//     * first load
//     */
//    fun firstLoad() {
//        if (isFirst()) {
//            showLoading()
//            loadData(getFirstPage())
//        }
//    }

    /**
     * load first page
     */
    override fun refreshData() {
        loadData(getFirstPage())
    }

    /**
     * load next page
     */
    fun loadMore() {
        loadData(currentPage.value?.plus(1) ?: getFirstPage())
    }

    /**
     * override if first page is not 1
     */
    open fun getFirstPage() = MTConstant.DEFAULT_FIRST_PAGE

    private fun getPreFirstPage() = getFirstPage() - 1

    /**
     * override if need change number visible threshold
     */
    open fun getLoadMoreThreshold() = MTConstant.DEFAULT_NUM_VISIBLE_THRESHOLD

    /**
     * override if need change number item per page
     */
    open fun getNumberItemPerPage() = MTConstant.DEFAULT_ITEM_PER_PAGE

    /**
     * reset load more
     */
    fun resetLoadMore() {
        onScrollListener.resetOnLoadMore()
        isLastPage.value = false
    }

    /**
     * handle load validateSuccess
     */
    fun onLoadSuccess(page: Int, items: List<Any>?) {
        // load validateSuccess then update current page
        currentPage.value = page
        // case load first page then clear data from listItem
        if (currentPage.value == getFirstPage()) listItem.value?.clear()
        // case refresh then reset load more
        if (isRefreshing.value == true) resetLoadMore()

        // add new data to listItem
        val newList = listItem.value ?: ArrayList()
        newList.addAll(items ?: listOf())
        listItem.value = newList

        // check last page
        isLastPage.value = items?.size ?: 0 < getNumberItemPerPage()

        // reset load
//        hideLoading()
        isRefreshing.value = false
        isLoadMore.value = false

        // check empty list
        checkEmptyList()
    }

    /**
     * handle load fail
     */
    override suspend fun onLoadFail(throwable: Throwable) {
        withContext(Dispatchers.Main) {
            super.onLoadFail(throwable)
            // reset load
            isRefreshing.value = false
            isLoadMore.value = false

            // check empty list
            checkEmptyList()
        }
    }

    /**
     * check list is empty
     */
    private fun checkEmptyList() {
        isEmptyList.value = listItem.value?.isEmpty() ?: true
    }
}