package com.corgaxm.ku_alarmy.adapters
import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter


@BindingAdapter("bind_visibility")
fun ProgressBar.bindVisibility(loading: Boolean) {
    visibility = when (loading) {
        true -> View.VISIBLE
        else -> View.GONE
    }
}
