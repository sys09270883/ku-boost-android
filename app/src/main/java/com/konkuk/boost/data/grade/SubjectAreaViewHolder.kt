package com.konkuk.boost.data.grade

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.konkuk.boost.R

class SubjectAreaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val subjectAreaTextView: TextView = itemView.findViewById(R.id.subjectAreaTextView)
    val countTextView: TextView = itemView.findViewById(R.id.countTextView)
}