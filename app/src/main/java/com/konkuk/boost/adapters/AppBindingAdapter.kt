package com.konkuk.boost.adapters

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.google.android.material.button.MaterialButton
import com.konkuk.boost.persistence.grade.GradeEntity
import com.konkuk.boost.persistence.simul.GraduationSimulationEntity
import com.konkuk.boost.utils.DateTimeConverter
import com.konkuk.boost.utils.GradeUtils
import com.konkuk.boost.utils.UseCase
import de.hdodenhof.circleimageview.CircleImageView


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

@BindingAdapter("bind_img_res")
fun CircleImageView.bindImageResource(stdNo: Int) {
    val url = "http://kupis.konkuk.ac.kr/ImageFile/schaff/regi/stud/photo/${stdNo}.jpg"
    Glide.with(context).load(url).into(this)
}

@BindingAdapter("update_if_enable")
fun MaterialButton.updateState(isOk: Int) {
    isEnabled = isOk == 0b11
}