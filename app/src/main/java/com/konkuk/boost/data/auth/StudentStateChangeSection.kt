package com.konkuk.boost.data.auth

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.konkuk.boost.R
import com.konkuk.boost.persistence.stdstate.StudentStateChangeEntity
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters

class StudentStateChangeSection : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.student_state_change_item)
        .headerResourceId(R.layout.student_state_change_header_item)
        .build()
) {

    var items = emptyList<StudentStateChangeEntity>()

    inner class StudentStateChangeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stateChangeCodeTextView: TextView = itemView.findViewById(R.id.stateChangeCodeTextView)
        val applyDateTextView: TextView = itemView.findViewById(R.id.applyDateTextView)
        val changedDateTextView: TextView = itemView.findViewById(R.id.changedDateTextView)
    }

    override fun getContentItemsTotal() = items.size

    override fun getItemViewHolder(view: View) = StudentStateChangeViewHolder(view)

    @SuppressLint("SetTextI18n")
    override fun onBindItemViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        val holder = viewHolder as StudentStateChangeViewHolder
        val item = items[position]

        holder.apply {
            stateChangeCodeTextView.text = item.changedCode

            if (item.changedReason.isNotBlank())
                stateChangeCodeTextView.text =
                    "${stateChangeCodeTextView.text}(${item.changedReason})"

            applyDateTextView.text = item.applyDate
            changedDateTextView.text = item.changedDate
        }
    }
}