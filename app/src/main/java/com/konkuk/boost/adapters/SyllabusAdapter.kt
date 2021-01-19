package com.konkuk.boost.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.konkuk.boost.R
import com.konkuk.boost.data.course.LectureInfo


class SyllabusAdapter : ListAdapter<LectureInfo, SyllabusAdapter.SyllabusViewHolder>(DiffCallback) {

    lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(lectureInfo: LectureInfo)
    }

    object DiffCallback : DiffUtil.ItemCallback<LectureInfo>() {
        override fun areItemsTheSame(oldItem: LectureInfo, newItem: LectureInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LectureInfo, newItem: LectureInfo): Boolean {
            return oldItem.subjectName == newItem.subjectName
        }
    }

    inner class SyllabusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectIdTextView: TextView = itemView.findViewById(R.id.subjectIdTextView)
        val subjectNameTextView: TextView = itemView.findViewById(R.id.subjectNameTextView)
        val professorTextView: TextView = itemView.findViewById(R.id.professorTextView)
        val lectureDayTimeTextView: TextView = itemView.findViewById(R.id.lectureDayTimeTextView)

        init {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(currentList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyllabusViewHolder {
        val holder = SyllabusViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.lecture_item, parent, false)
        )

        holder.subjectIdTextView.isSelected = true
        holder.subjectNameTextView.isSelected = true
        holder.professorTextView.isSelected = true
        holder.lectureDayTimeTextView.isSelected = true

        return holder
    }

    override fun onBindViewHolder(holder: SyllabusViewHolder, position: Int) {
        val lectureInfo = currentList[position]
        holder.apply {
            subjectIdTextView.text = lectureInfo.subjectId
            subjectNameTextView.text = lectureInfo.subjectName
            var professor = lectureInfo.professor
            if (professor.isNullOrBlank()) professor = "미배정"
            professorTextView.text = professor
            lectureDayTimeTextView.text = lectureInfo.lectureDayTime
        }
    }

    override fun submitList(list: MutableList<LectureInfo>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

}