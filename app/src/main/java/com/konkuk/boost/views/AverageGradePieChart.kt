package com.konkuk.boost.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.konkuk.boost.R
import com.konkuk.boost.utils.MessageUtils

class AverageGradePieChart : PieChart {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet? = null) : super(context, attributeSet)
    constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : super(context, attributeSet, defStyleAttr)

    init {
        setNoDataText(MessageUtils.PENDING_DATA)
        description = null
        setTouchEnabled(false)
        isDrawHoleEnabled = true
        setHoleColor(Color.WHITE)
        setTransparentCircleColor(Color.WHITE)
        setTransparentCircleAlpha(0)
        holeRadius = 90f
        transparentCircleRadius = 92f
        setDrawCenterText(true)
        setCenterTextSize(18f)
        legend.setDrawInside(true)
        legend.isEnabled = false
        setDrawEntryLabels(false)
        animateY(1000, Easing.EaseInOutQuad)
    }

    fun makeChart(classification: String, avr: String) {
        clear()

        centerText = "${classification}학점\n$avr"

        val mainColor = ContextCompat.getColor(context, R.color.pastelRed)
        val subColor = ContextCompat.getColor(context, R.color.pastelLightGray)

        val grades = mutableListOf<PieEntry>()
        grades.add(PieEntry(avr.toFloat(), "grade"))
        grades.add(PieEntry(4.5f - avr.toFloat(), "total"))
        val pieDataSet = PieDataSet(grades, null)
        val colors = listOf(mainColor, subColor)
        pieDataSet.colors = colors
        pieDataSet.setDrawValues(false)
        val pieData = PieData(pieDataSet)

        data = pieData
    }
}