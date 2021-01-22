package com.konkuk.boost.adapters

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.konkuk.boost.persistence.GradeEntity
import com.konkuk.boost.persistence.GraduationSimulationEntity
import com.konkuk.boost.utils.DateTimeConverter
import com.konkuk.boost.utils.GradeUtils
import com.konkuk.boost.utils.UseCase


@BindingAdapter("bind_visibility")
fun ProgressBar.bindVisibility(loading: Boolean) {
    visibility = when (loading) {
        true -> View.VISIBLE
        else -> View.GONE
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("year", "semester", requireAll = true)
fun TextView.bindYearAndSemester(year: Int, semester: String?) {
    semester?.let {
        text = "${year}년도 ${semester}학기"
    }
}

@BindingAdapter("bind_visibility")
fun TextView.bindGradesVisibility(grades: UseCase<List<GradeEntity>>?) {
    visibility = if (grades == null)
        View.VISIBLE
    else {
        val data = grades.data
        if (data.isNullOrEmpty()) View.VISIBLE else View.GONE
    }
}

@BindingAdapter("bind_visibility")
fun PieChart.bindGradesVisibility(grades: UseCase<List<GradeEntity>>?) {
    visibility = if (grades == null)
        View.GONE
    else {
        val data = grades.data
        if (data.isNullOrEmpty()) View.GONE else View.VISIBLE
    }
}

@BindingAdapter("bind_visibility")
fun LineChart.bindGradesVisibility(grades: UseCase<List<GradeEntity>>?) {
    visibility = if (grades == null)
        View.GONE
    else {
        val data = grades.data
        if (data.isNullOrEmpty()) View.GONE else View.VISIBLE
    }
}

@BindingAdapter("bind_grade_visibility")
fun ImageView.bindGradesVisibility(grades: UseCase<List<GradeEntity>>?) {
    visibility = if (grades == null)
        View.GONE
    else {
        val data = grades.data
        if (data.isNullOrEmpty()) View.GONE else View.VISIBLE
    }
}

@BindingAdapter("bind_simul_visibility")
fun ImageView.bindSimulationVisibility(simulations: UseCase<List<GraduationSimulationEntity>>?) {
    visibility = if (simulations == null)
        View.GONE
    else {
        val data = simulations.data
        if (data.isNullOrEmpty()) View.GONE else View.VISIBLE
    }
}

@BindingAdapter("bind_visibility")
fun RecyclerView.bindGradesVisibility(grades: UseCase<List<GradeEntity>>?) {
    visibility = if (grades == null)
        View.GONE
    else {
        val data = grades.data
        if (data.isNullOrEmpty()) View.GONE else View.VISIBLE
    }
}

@BindingAdapter("bind_syllabus_visibility")
fun RecyclerView.bindSyllabusVisibility(loading: Boolean) {
    visibility = if (loading) View.GONE else View.VISIBLE
}

@BindingAdapter("bind_syllabus_search_view_visibility")
fun androidx.appcompat.widget.SearchView.bindSyllabusVisibility(loading: Boolean) {
    visibility = if (loading) View.GONE else View.VISIBLE
}

@SuppressLint("SetTextI18n")
@BindingAdapter("bind_date_text")
fun TextView.bindDateText(semester: Int) {
    val year = DateTimeConverter.currentYear()
    val sem = GradeUtils.translate(semester)
    text = "${year}년 ${sem}학기"
}