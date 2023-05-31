package com.bookmaker.onexbetapp.view.utils

import android.content.Context
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.bookmaker.onexbetapp.R

class UnitSpan(private val context: Context, private val unit: () -> Unit): ClickableSpan() {
    override fun onClick(widget: View) {
        unit()
    }

    override fun updateDrawState(ds: TextPaint) {
        ds.underlineColor = context.getColor(R.color.icon)
        ds.underlineThickness = 3f
    }
}