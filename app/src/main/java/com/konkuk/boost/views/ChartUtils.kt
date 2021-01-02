package com.konkuk.boost.views

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.konkuk.boost.R

object ChartUtils {

    fun setGradeConfigWith(
        pieChart: PieChart,
        classification: String,
        hasAnimation: Boolean = false,
        noDataText: String = "데이터 가져오는 중…"
    ) {
        pieChart.apply {
            setNoDataText(noDataText)
            description = null
            setTouchEnabled(false)
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(0)
            holeRadius = 90f // 가운데 반지름
            transparentCircleRadius = 92f // 그래프 반지름
            setDrawCenterText(true)
            centerText = "${classification}평점\n4.5"
            setCenterTextSize(18f)
            legend.setDrawInside(true)
            legend.isEnabled = false
            setDrawEntryLabels(false)
            if (hasAnimation) animateY(1400, Easing.EaseInOutQuad)
        }
    }

    fun setSummaryConfig(
        pieChart: PieChart,
        hasAnimation: Boolean = false,
        noDataText: String = "데이터 가져오는 중…"
    ) {
        pieChart.apply {
            setNoDataText(noDataText)
            description = null
            setTouchEnabled(false)
            setDrawSlicesUnderHole(false)
            holeRadius = 0f
            legend.isEnabled = false
            isDrawHoleEnabled = false
            setEntryLabelColor(ContextCompat.getColor(context, R.color.primaryTextColor))
            if (hasAnimation) animateY(1400, Easing.EaseInOutQuad)
        }
    }

    fun setLineChartConfig(
        lineChart: LineChart,
        hasAnimation: Boolean = false,
        noDataText: String = "데이터 가져오는 중…"
    ) {
        lineChart.apply {
            setNoDataText(noDataText)
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
            if (hasAnimation) animateY(1400, Easing.EaseInOutQuad)
        }
    }

    fun makeGradeChart(pieChart: PieChart, classification: String, avr: String, mainColor: Int, subColor: Int) {
        pieChart.clear()
        pieChart.centerText = "${classification}학점\n$avr"
        val grades = mutableListOf<PieEntry>()
        grades.add(PieEntry(avr.toFloat(), "grade")) // 전체평점
        grades.add(PieEntry(4.5f - avr.toFloat(), "total")) // 기준평점-전체평점
        val pieDataSet = PieDataSet(grades, null)
        val colors = listOf(mainColor, subColor)
        pieDataSet.colors = colors
        pieDataSet.setDrawValues(false)
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
    }

    fun makeSummaryChart(pieChart: PieChart, colors: List<Int>, characterGrades: List<PieEntry>) {
        pieChart.clear()
        val pieDataSet = PieDataSet(characterGrades, null)
        pieDataSet.colors = colors
        pieDataSet.setDrawValues(false)
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
    }
}