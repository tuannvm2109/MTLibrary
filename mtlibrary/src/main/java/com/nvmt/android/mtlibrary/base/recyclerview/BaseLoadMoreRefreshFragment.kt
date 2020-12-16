package com.nvmt.android.mtlibrary.base.recyclerview

import androidx.databinding.ViewDataBinding
import com.nvmt.android.mtlibrary.R
import com.nvmt.android.mtlibrary.base.common.BaseFragment

abstract class BaseLoadMoreRefreshFragment<ViewBinding : ViewDataBinding, ViewModel : BaseLoadMoreRefreshViewModel> :
    BaseFragment<ViewBinding, ViewModel>() {

    override val layoutId: Int = R.layout.fragment_loadmore_refresh
}