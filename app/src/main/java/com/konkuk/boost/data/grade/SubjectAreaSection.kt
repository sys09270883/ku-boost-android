package com.konkuk.boost.data.grade

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.konkuk.boost.R
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters

class SubjectAreaSection(flag: Int) :
    Section(
        SectionParameters.builder()
            .itemResourceId(R.layout.elective_item)
            .headerResourceId(
                when (flag) {
                    1 -> R.layout.basic_section_header_item
                    2 -> R.layout.core_section_header_item
                    3 -> R.layout.advanced_section_header_item
                    else -> throw Exception("Invalid flag in SubjectAreaSection.")
                }
            )
            .build()
    ) {

    var itemList = emptyList<SubjectAreaCount>()

    override fun getContentItemsTotal() = itemList.size

    override fun getItemViewHolder(view: View) = SubjectAreaViewHolder(view)

    override fun onBindItemViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as SubjectAreaViewHolder
        val item = itemList[position]

        holder.subjectAreaTextView.text = item.area.subjectAreaName
        holder.countTextView.text = item.count.toString()
    }
}