package com.nvmt.android.mtlibrary.base.ratestar

import android.content.Context
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.nvmt.android.mtlibrary.R
import com.nvmt.android.mtlibrary.extension.setGlideSrc

class RateStarAdapter(
    val context: Context,
    var star: Int,
    val listener: (star : Int) -> Unit
) : RecyclerView.Adapter<RateStarAdapter.ViewHolder>() {
    var isClickAble: Boolean? = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rate_star, parent, false)
        val holder = ViewHolder(view)
        view.setOnClickListener {
            if (isClickAble == false) return@setOnClickListener
            star = holder.layoutPosition + 1
            listener.invoke(star)
            notifyDataSetChanged()
        }
        return holder
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position <= star - 1) holder.setStar(true)
        else holder.setStar(false)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setStar(rate: Boolean) {
            val img = itemView.findViewById<ImageView>(R.id.imgStar)
            if (rate) img.setGlideSrc(R.drawable.ic_star_rate)
            else img.setGlideSrc(R.drawable.ic_star_not_rate)
        }
    }
}