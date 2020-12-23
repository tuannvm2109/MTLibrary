package com.nvmt.android.mtlibrary.extension

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.nvmt.android.mtlibrary.R
import java.io.File



@BindingAdapter("glideSrc")
fun ImageView.setGlideSrc(@DrawableRes src: Int?) {
    Glide.with(context)
        .load(src)
        .into(this)
}

@BindingAdapter("glidePath")
fun ImageView.setGlidePath(imagePath: String) {
    Glide.with(context)
        .load(File(imagePath))
        .into(this)
}

@BindingAdapter("glideByteArray")
fun ImageView.setGlideByteArray(byteArray: ByteArray?) {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    Glide.with(context)
        .asBitmap()
        .load(byteArray)
        .placeholder(circularProgressDrawable)
        .into(this);
}


@BindingAdapter("loadImageUrlNoAnimation")
fun ImageView.loadImageUrlNoAnimation(image: String?) {
    Glide.with(context)
        .load(image)
        .error(R.drawable.ic_default_placeholder)
        .transition(DrawableTransitionOptions.withCrossFade())
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(this)
}


@BindingAdapter("loadImageUrl")
fun ImageView.loadImageUrl(image: String?) {

    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        circularProgressDrawable.colorFilter =
            BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
    } else {
        circularProgressDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
    }
    circularProgressDrawable.start()

    // Shimmer loading animation sẽ bị lỗi nhỏ màu đen ở góc các ImageView round corner
//    val shimmer = Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
//        .setDuration(900) // how long the shimmering animation takes to do one full sweep
//        .setBaseAlpha(0.8f) //the alpha of the underlying children
////        .setHighlightAlpha(0.6f) // the shimmer alpha amount
//        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
//        .setAutoStart(true)
//        .build()
//
//    val shimmerDrawable = ShimmerDrawable().apply {
//        setShimmer(shimmer)
//    }

    Glide.with(context)
        .load(image)
        .placeholder(circularProgressDrawable)
        .error(R.drawable.ic_default_placeholder)
        .transition(DrawableTransitionOptions.withCrossFade())
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(this)
}
