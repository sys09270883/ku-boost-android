package com.konkuk.boost.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.konkuk.boost.R
import com.konkuk.boost.persistence.LikeCourseEntity

class LikeCourseAdapter :
    ListAdapter<LikeCourseEntity, LikeCourseAdapter.LikeCourseViewHolder>(DiffCallback) {

    lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(likeCourse: LikeCourseEntity)
    }

    object DiffCallback : DiffUtil.ItemCallback<LikeCourseEntity>() {
        override fun areItemsTheSame(
            oldItem: LikeCourseEntity,
            newItem: LikeCourseEntity
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: LikeCourseEntity,
            newItem: LikeCourseEntity
        ): Boolean {
            return oldItem.subjectId == newItem.subjectId
        }
    }

    inner class LikeCourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectIdTextView: TextView = itemView.findViewById(R.id.subjectIdTextView)
        val subjectNameTextView: TextView = itemView.findViewById(R.id.subjectNameTextView)
        val professorTextView: TextView = itemView.findViewById(R.id.professorTextView)

        init {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(currentList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeCourseViewHolder {
        val holder = LikeCourseViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.like_course_item, parent, false)
        )

        holder.subjectNameTextView.isSelected = true
        holder.professorTextView.isSelected = true

        return holder
    }

    override fun onBindViewHolder(holder: LikeCourseViewHolder, position: Int) {
        val item = currentList[position]
        holder.apply {
            subjectIdTextView.text = item.subjectId
            subjectNameTextView.text = item.subjectName
            professorTextView.text = item.professor
        }
    }
}