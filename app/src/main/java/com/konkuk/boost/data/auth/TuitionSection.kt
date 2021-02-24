package com.konkuk.boost.data.auth

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.konkuk.boost.R
import com.konkuk.boost.persistence.tuition.TuitionEntity
import com.konkuk.boost.utils.DateTimeConverter
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import java.text.NumberFormat
import java.time.format.DateTimeFormatter

class TuitionSection : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.tuition_item)
        .headerResourceId(R.layout.tuition_header_item)
        .build()
) {

    var items = emptyList<TuitionEntity>()

    inner class TuitionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stateCodeTextView: TextView = itemView.findViewById(R.id.stateCodeTextView)
        val yearAndSemesterTextView: TextView = itemView.findViewById(R.id.yearAndSemesterTextView)
        val enterAmountTextView: TextView = itemView.findViewById(R.id.enterAmountTextView)
        val tuitionAmountTextView: TextView = itemView.findViewById(R.id.tuitionAmountTextView)
        val paidDateTextView: TextView = itemView.findViewById(R.id.paidDateTextView)
    }

    override fun getContentItemsTotal() = items.size

    override fun getItemViewHolder(view: View) = TuitionViewHolder(view)

    @SuppressLint("SetTextI18n")
    override fun onBindItemViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        val holder = viewHolder as TuitionViewHolder
        val item = items[position]

        holder.apply {
            stateCodeTextView.text = item.stateCode
            yearAndSemesterTextView.text = "${item.year}년도 ${item.semester}"
            enterAmountTextView.text = NumberFormat.getInstance().format(item.enterAmount)
            tuitionAmountTextView.text = NumberFormat.getInstance().format(item.tuitionAmount)
            paidDateTextView.text = item.paidDate
        }
    }
}