package com.nvmt.android.mtlibrary.base.ratestar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nvmt.android.mtlibrary.R

class RateStarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    var isRateAble: Boolean? = false
    var currentStar: Int? = null
        set(value) {
            field = value
            updateView(this)
        }


    init {
        this.orientation = HORIZONTAL
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.custom_rate_star_view, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.RateStarView)
        try {
            isRateAble =
                ta.getBoolean(R.styleable.RateStarView_isRateAble, false)

        } finally {
            ta.recycle()
        }
    }

    private fun updateView(view: View) {
        val adapter = RateStarAdapter(
            context, currentStar ?: 0
        ) { star ->
            currentStar = star
        }

        adapter.setHasStableIds(true)
        adapter.isClickAble = true

        view.findViewById<RecyclerView>(R.id.rvRate).apply {
            itemAnimator = null
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            this.adapter = adapter
            if (isRateAble != true) suppressLayout(true)
        }
    }
}