package com.konkuk.boost.views

import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.konkuk.boost.R

object DialogUtils {
    fun AlertDialog.Builder.setSpinner(spinner: Spinner): AlertDialog.Builder {
        val context = spinner.context
        val container = FrameLayout(context)
        container.addView(spinner)
        val containerParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        val marginHorizontal = 200f
        val marginTop = 16f
        containerParams.topMargin = (marginTop / 2).toInt()
        containerParams.leftMargin = marginHorizontal.toInt()
        containerParams.rightMargin = marginHorizontal.toInt()
        container.layoutParams = containerParams
        val superContainer = FrameLayout(context)
        superContainer.addView(container)
        setView(superContainer)
        return this
    }

    fun recolor(dialog: AlertDialog) = dialog.let {
        val context = it.context
        it.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                ContextCompat.getColor(context, R.color.primaryTextColor)
            )
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                ContextCompat.getColor(context, R.color.primaryTextColor)
            )
        }
        it
    }

    fun AlertDialog.Builder.setProgressBar(progressBar: ProgressBar): AlertDialog.Builder {
        val context = progressBar.context
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
}