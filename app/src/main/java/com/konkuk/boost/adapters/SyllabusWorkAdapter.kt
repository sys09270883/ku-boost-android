package com.konkuk.boost.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.konkuk.boost.R
import com.konkuk.boost.data.course.SyllabusWork

class SyllabusWorkAdapter :
    ListAdapter<SyllabusWork, SyllabusWorkAdapter.WorkViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<SyllabusWork>() {
        override fun areItemsTheSame(oldItem: SyllabusWork, newItem: SyllabusWork): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SyllabusWork, newItem: SyllabusWork): Boolean {
            return oldItem.homeworkName == newItem.homeworkName
        }
    }

    inner class WorkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val homeworkDivTextView: TextView = itemView.findViewById(R.id.homeworkDivTextView)
        val homeworkContentTextView: TextView = itemView.findViewById(R.id.homeworkContentTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkViewHolder {
        return WorkViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.work_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WorkViewHolder, position: Int) {
        val item = currentList[position]
        holder.apply {
            homeworkDivTextView.text = when (item.homeworkDiv ?: "") {
                "B51001" -> "과제"
                "B51002" -> "퀴즈"
                "B51003" -> "발표"
                "B51004" -> "프로젝트"
                "B51005" -> "토론"
                else -> ""
            }
            homeworkContentTextView.text = item.homeworkName
        }
    }
}