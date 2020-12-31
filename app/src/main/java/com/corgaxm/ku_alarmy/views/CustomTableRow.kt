package com.corgaxm.ku_alarmy.views

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

class CustomTableRow(context: Context): TableRow(context) {

    fun attach(
        tableLayout: TableLayout,
        classification: String,
        standardValue: Int,
        acquiredValue: Int,
        remainderValue: Int
    ) {
        val row = TableRow(context)
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
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            row.addView(textView)
        }

        tableLayout.addView(row)
    }

    fun attach(
        tableLayout: TableLayout,
        first: String,
        second: String,
        third: String,
        fourth: String
    ) {
        val row = TableRow(context)
        val textList = listOf(first, second, third, fourth)

        for (text in textList) {
            val textView = TextView(context)
            textView.text = text
            textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            textView.typeface = Typeface.DEFAULT_BOLD
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            row.addView(textView)
        }

        tableLayout.addView(row)
    }
}