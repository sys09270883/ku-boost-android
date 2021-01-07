package com.konkuk.boost.views

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class CustomValueFormatter : ValueFormatter() {
    private val format = DecimalFormat("###,###,##0.00")

    override fun getFormattedValue(value: Float): String {
        return format.format(value).toString()
    }
}