package com.konkuk.boost.data.auth

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.konkuk.boost.R
import com.konkuk.boost.persistence.dept.DeptTransferEntity
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters

class DeptTransferSection : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.dept_transfer_item)
        .headerResourceId(R.layout.dept_transfer_header_item)
        .build()
) {

    var items = emptyList<DeptTransferEntity>()

    inner class DeptTransferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val changedCodeTextView: TextView = itemView.findViewById(R.id.changedCodeTextView)
        val yearAndSemesterTextView: TextView = itemView.findViewById(R.id.yearAndSemesterTextView)
        val beforeTextView: TextView = itemView.findViewById(R.id.beforeTextView)
        val afterTextView: TextView = itemView.findViewById(R.id.afterTextView)
    }

    override fun getContentItemsTotal() = items.size

    override fun getItemViewHolder(view: View) = DeptTransferViewHolder(view)

    @SuppressLint("SetTextI18n")
    override fun onBindItemViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        val holder = viewHolder as DeptTransferViewHolder
        val item = items[position]

        holder.apply {
            changedCodeTextView.text = item.changedCode
            yearAndSemesterTextView.text = "${item.changedYear}년도 ${item.changedSemester}"
            beforeTextView.text = "${item.beforeDept} ${item.beforeSust} ${item.beforeMajor}".trim()
            afterTextView.text = "${item.dept} ${item.sust} ${item.major}".trim()
        }
    }
}