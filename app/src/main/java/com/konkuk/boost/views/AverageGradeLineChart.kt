package com.konkuk.boost.views

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.konkuk.boost.R
import com.konkuk.boost.utils.CustomValueFormatter
import com.konkuk.boost.utils.MessageUtils

class AverageGradeLineChart : LineChart {

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
        isDragEnabled = false
        setScaleEnabled(false)
        setDrawGridBackground(false)
        isHighlightPerDragEnabled = false
        isHighlightPerTapEnabled = false
        axisRight.isEnabled = false
        axisLeft.isEnabled = false
        legend.isEnabled = false
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(false)
        animateY(1000, Easing.EaseInOutQuad)
    }

    fun makeChart(entries: List<Entry>) {
        clear()

        val dataSet = LineDataSet(entries, null)
        val color = ContextCompat.getColor(context, R.color.pastelRed)
        dataSet.setCircleColor(color)
        dataSet.color = color
        dataSet.circleHoleRadius = 4f
        dataSet.circleRadius = 6f
        dataSet.setDrawCircleHole(true)
        dataSet.lineWidth = 2f
        dataSet.valueFormatter = CustomValueFormatter()

        val lineData = LineData()
        lineData.addDataSet(dataSet)
        lineData.setValueTextColor(
            ContextCompat.getColor(
                context, R.color.secondaryTextColor
            )
        )
        lineData.setValueTextSize(12f)

        data = lineData
    }
}