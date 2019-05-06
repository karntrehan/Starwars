package com.karntrehan.starwars.extensions

import android.text.Html
import android.view.View
import android.widget.TextView

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun String?.isValid(): Boolean {
    return !this.isNullOrEmpty()
}

@Suppress("DEPRECATION")
fun TextView.html(html: String?) {
    if (!html.isValid()) return
    if (android.os.Build.VERSION.SDK_INT >= 24)
        this.text = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    else
        this.text = Html.fromHtml(html)
}
