package com.nvmt.android.mtlibrary.base.iconbadge

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.nvmt.android.mtlibrary.R

class IconBadgeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var mColorFilter: ColorFilter? = null
    private var mDrawable: Drawable? = null

    var badgeNumb: Int? = null
        set(value) {
            field = value
            updateView(this)
        }

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.custom_icon_badge_view, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.IconBadgeView)
        try {
            val color = ta.getColor(R.styleable.IconBadgeView_icon_tint, -210996)
            if (color != -210996) {
                mColorFilter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    BlendModeColorFilter(color, BlendMode.SRC_ATOP)
                } else {
                    PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
                }
            }
            mDrawable = ta.getDrawable(R.styleable.IconBadgeView_icon_src)

        } finally {
            ta.recycle()
        }
    }

    private fun updateView(view: View) {
        val imgIcon = view.findViewById<ImageView>(R.id.imgIcon)
        val tvNumBadge = view.findViewById<TextView>(R.id.tvBadge)
        if (mColorFilter != null) imgIcon.colorFilter = mColorFilter
        if (mDrawable != null) imgIcon.setImageDrawable(mDrawable)
        if ((badgeNumb ?: 0) <= 0) tvNumBadge.visibility = View.GONE
        else {
            tvNumBadge.visibility = View.VISIBLE
            tvNumBadge.text = if ((badgeNumb ?: 0) > 99) "99+" else badgeNumb.toString()
        }
    }
}