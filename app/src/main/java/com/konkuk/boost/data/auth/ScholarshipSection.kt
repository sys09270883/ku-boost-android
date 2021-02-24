package com.konkuk.boost.data.auth

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.konkuk.boost.R
import com.konkuk.boost.persistence.scholarship.ScholarshipEntity
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import java.text.NumberFormat

class ScholarshipSection : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.scholarship_item)
        .headerResourceId(R.layout.scholarship_header_item)
        .build()
) {

    var items = emptyList<ScholarshipEntity>()

    inner class ScholarshipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scholarshipNameTextView: TextView = itemView.findViewById(R.id.scholarshipNameTextView)
        val yearAndSemesterTextView: TextView = itemView.findViewById(R.id.yearAndSemesterTextView)
        val enterAmountTextView: TextView = itemView.findViewById(R.id.enterAmountTextView)
        val tuitionAmountTextView: TextView = itemView.findViewById(R.id.tuitionAmountTextView)
        val etcAmountTextView: TextView = itemView.findViewById(R.id.etcAmountTextView)
        val paidDateTextView: TextView = itemView.findViewById(R.id.paidDateTextView)
    }

    override fun getContentItemsTotal() = items.size

    override fun getItemViewHolder(view: View) = ScholarshipViewHolder(view)

    @SuppressLint("SetTextI18n")
    override fun onBindItemViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        val holder = viewHolder as ScholarshipViewHolder
        val item = items[position]

        holder.apply {
            scholarshipNameTextView.text = item.scholarshipName
            yearAndSemesterTextView.text = "${item.year}년도 ${item.semester}"
            enterAmountTextView.text =
                NumberFormat.getInstance().format(item.scholarshipEnterAmount)
            tuitionAmountTextView.text =
                NumberFormat.getInstance().format(item.scholarshipTuitionAmount)
            etcAmountTextView.text = NumberFormat.getInstance().format(item.etcAmount)
            paidDateTextView.text = item.date
        }
    }
}