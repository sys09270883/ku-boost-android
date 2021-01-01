package com.corgaxm.ku_alarmy.adapters

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("bind_visibility")
fun ProgressBar.bindVisibility(loading: Boolean) {
    visibility = when (loading) {
        true -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("bind_visibility")
fun ImageView.bindVisibility(loading: Boolean) {
    visibility = when (loading) {
        true -> View.GONE
        else -> View.VISIBLE
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("year", "semester", requireAll = true)
fun TextView.bindYearAndSemester(year: Int, semester: String) {
    text = "${year}년 ${semester}학기"
}

