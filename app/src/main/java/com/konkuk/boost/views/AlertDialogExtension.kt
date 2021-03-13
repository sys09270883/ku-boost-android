package com.konkuk.boost.views

import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.konkuk.boost.R

fun AlertDialog.recolor(): AlertDialog {
    setOnShowListener {
        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
            ContextCompat.getColor(context, R.color.primaryTextColor)
        )
        getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
            ContextCompat.getColor(context, R.color.primaryTextColor)
        )
    }
    return this
}

fun AlertDialog.Builder.setProgressBar(progressBar: ProgressBar): AlertDialog.Builder {
    val container = FrameLayout(context)
    container.addView(progressBar)
    val containerParams = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.MATCH_PARENT
    )
    val marginHorizontal = 48f
    val margin = 48f
    containerParams.leftMargin = marginHorizontal.toInt()
    containerParams.rightMargin = marginHorizontal.toInt()
    containerParams.bottomMargin = margin.toInt()
    container.layoutParams = containerParams
    val superContainer = FrameLayout(context)
    superContainer.addView(container)
    setView(superContainer)
    return this
}