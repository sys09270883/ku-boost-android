package com.konkuk.boost.views

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.util.TypedValue
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.konkuk.boost.R

class CustomTableRow(context: Context) : TableRow(context) {

    fun attach(
        tableLayout: TableLayout,
        classification: String,
        standardValue: Int,
        acquiredValue: Int,
        remainderValue: Int
    ) {
        try {
            val row = TableRow(context)
            val params = TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(12, 12, 12, 12)
            row.layoutParams = params

            val textList = listOf(
                classification,
                "$standardValue",
                "$acquiredValue",
                "$remainderValue"
            )

            for (text in textList) {
                val textView = TextView(context)
                textView.text = text
                textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                textView.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor))
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                row.addView(textView)
            }

            tableLayout.addView(row)
        } catch (exception: Exception) {
            Log.e("yoonseop", "${exception.message}")
        }
    }

    fun attach(
        tableLayout: TableLayout,
        first: String,
        second: String,
        third: String,
        fourth: String
    ) {
        try {
            val row = TableRow(context)
            val params = TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(12, 12, 12, 12)
            row.layoutParams = params

            val textList = listOf(first, second, third, fourth)

            for (text in textList) {
                val textView = TextView(context)
                textView.text = text
                textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                textView.typeface = Typeface.DEFAULT_BOLD
                textView.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor))
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                row.addView(textView)
            }

            tableLayout.addView(row)
        } catch (exception: Exception) {
            Log.e("yoonseop", "${exception.message}")
        }
    }
}