package com.nvmt.android.mtlibrary.testkeeprecyclerview

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.nvmt.android.mtlibrary.R
import com.nvmt.android.mtlibrary.base.recyclerview.BaseRecyclerAdapter
import com.nvmt.android.mtlibrary.databinding.ItemNotificationBinding
import com.nvmt.android.mtlibrary.util.TimeUtil

class NotificationAdapter(
    val itemClickListener: (NotifyCas) -> Unit = {},
    val context: Context?
) : BaseRecyclerAdapter<NotifyCas, ItemNotificationBinding>(object :
    DiffUtil.ItemCallback<NotifyCas>() {
    override fun areItemsTheSame(oldItem: NotifyCas, newItem: NotifyCas): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NotifyCas, newItem: NotifyCas): Boolean {
        return oldItem == newItem
    }
}) {

    override fun getLayoutRes(viewType: Int): Int {
        return R.layout.item_notification
    }

    override fun bindFirstTime(binding: ItemNotificationBinding) {
        binding.apply {
        }
    }
}