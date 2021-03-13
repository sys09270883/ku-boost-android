package com.konkuk.boost.views

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.konkuk.boost.R
import com.konkuk.boost.utils.MessageUtils

class GradeDistributionPieChart : PieChart {

    private val colors: List<Int> by lazy {
        listOf(
            ContextCompat.getColor(context, R.color.pastelRed),
            ContextCompat.getColor(context, R.color.pastelOrange),
            ContextCompat.getColor(context, R.color.pastelYellow),
            ContextCompat.getColor(context, R.color.pastelGreen),
            ContextCompat.getColor(context, R.color.pastelBlue),
            ContextCompat.getColor(context, R.color.pastelIndigo),
            ContextCompat.getColor(context, R.color.pastelPurple),
            ContextCompat.getColor(context, R.color.pastelDeepPurple),
            ContextCompat.getColor(context, R.color.pastelBrown),
            ContextCompat.getColor(context, R.color.pastelLightGray),
        )
    }

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
        setDrawSlicesUnderHole(false)
        holeRadius = 0f
        legend.isEnabled = false
        isDrawHoleEnabled = false
        setEntryLabelColor(ContextCompat.getColor(context, R.color.primaryTextColor))
        animateY(1000, Easing.EaseInOutQuad)
    }

    fun makeChart(characterGrades: List<PieEntry>) {
        clear()

        val dataSet = PieDataSet(characterGrades, null)
        dataSet.colors = colors
        dataSet.setDrawValues(false)
        val pieData = PieData(dataSet)

        data = pieData
    }
}