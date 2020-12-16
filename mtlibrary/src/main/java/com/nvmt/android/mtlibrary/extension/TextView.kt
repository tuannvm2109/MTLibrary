package com.nvmt.android.mtlibrary.extension

import android.os.Build
import android.text.Html
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.nvmt.android.mtlibrary.util.html.URLImageParser

@BindingAdapter("text_html")
fun TextView.setTextHtml(source: String?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        text = Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT)
    } else {
        text = Html.fromHtml(source)
    }
}

// có hình thì nên dùng webview đi
@BindingAdapter("text_html_with_image")
fun TextView.setTextHtmlWithImage(source: String?) {
    if (!text.isNullOrEmpty() || source == null) return
    val p = URLImageParser(this, context);
    val htmlSpan = Html.fromHtml(source, p, null);
    text = htmlSpan
}
