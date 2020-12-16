package com.nvmt.android.mtlibrary.base.recyclerview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nvmt.android.mtlibrary.R
import java.util.concurrent.Executors

/**
 * base recycler view adapter
 */
abstract class BaseRecyclerAdapter<Item, ViewBinding : ViewDataBinding>(
    callBack: DiffUtil.ItemCallback<Item>
) : ListAdapter<Item, BaseViewHolder<ViewBinding>>(
    AsyncDifferConfig.Builder<Item>(callBack)
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
        .build()
) {
    private var isHasAnimFadeIn: Boolean = true

    private var isFlicker: Boolean = false
    private var startColor: Int? = null
    private var endColor: Int? = null

    /**
     * override this with new list to pass check "if (newList == mList)" in AsyncListDiffer
     */
    override fun submitList(list: List<Item>?) {
        super.submitList(ArrayList<Item>(list ?: listOf()))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(DataBindingUtil.inflate<ViewBinding>(
            LayoutInflater.from(parent.context),
            getLayoutRes(viewType),
            parent, false
        ).apply {
            bindFirstTime(this)
        })
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding>, position: Int) {
        val item: Item? = currentList.getOrNull(position)
        holder.binding.setVariable(BR.item, item)
        if (item != null) {
            bindView(holder.binding, item, position)
        }
        holder.binding.executePendingBindings()


        if (isHasAnimFadeIn) {
            holder.itemView.startAnimation(
                AnimationUtils.loadAnimation(
                    holder.itemView.context,
                    R.anim.fade_in
                )
            )
        }

        if (isFlicker) {
            if (holder.adapterPosition % 2 == 0) {
                holder.itemView.setBackgroundColor(startColor ?: Color.GRAY)
            } else {
                holder.itemView.setBackgroundColor(endColor ?: Color.WHITE)
            }
        }
    }

    fun setHasAnimFadein(isUseAnimFadeIn: Boolean) {
        this.isHasAnimFadeIn = isUseAnimFadeIn
    }

    fun setHasFlicker(isFlicker: Boolean, startColor: Int, endColor: Int) {
        this.isFlicker = isFlicker
        this.startColor = startColor
        this.endColor = endColor
    }

    /**
     * get layout res based on view type
     */
    protected abstract fun getLayoutRes(viewType: Int): Int

    /**
     * bind first time
     * use for set item onClickListener, something only set one time
     */
    protected open fun bindFirstTime(binding: ViewBinding) {}

    /**
     * bind view
     */
    protected open fun bindView(binding: ViewBinding, item: Item, position: Int) {}

}

open class BaseViewHolder<ViewBinding : ViewDataBinding>(
    val binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root)