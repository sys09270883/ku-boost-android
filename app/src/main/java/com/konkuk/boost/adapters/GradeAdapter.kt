package com.konkuk.boost.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.konkuk.boost.R
import com.konkuk.boost.persistence.GradeEntity

class GradeAdapter : ListAdapter<GradeEntity, GradeAdapter.GradeViewHolder>(DiffCallback) {

    lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(gradeEntity: GradeEntity)
    }

    object DiffCallback : DiffUtil.ItemCallback<GradeEntity>() {
        override fun areItemsTheSame(oldItem: GradeEntity, newItem: GradeEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: GradeEntity, newItem: GradeEntity): Boolean {
            return oldItem.subjectName == newItem.subjectName
        }
    }

    inner class GradeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectNameTextView: TextView = itemView.findViewById(R.id.subjectNameTextView)
        val professorTextView: TextView = itemView.findViewById(R.id.professorTextView)
        val subjectPointTextView: TextView = itemView.findViewById(R.id.subjectPointTextView)
        val classificationTextView: TextView = itemView.findViewById(R.id.classificationTextView)
        val characterGradeTextView: TextView = itemView.findViewById(R.id.characterGradeButton)

        init {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(currentList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeViewHolder {
        val holder = GradeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.grade_item, parent, false)
        )

        holder.subjectNameTextView.isSelected = true
        holder.professorTextView.isSelected = true
        holder.subjectPointTextView.isSelected = true
        holder.classificationTextView.isSelected = true
        holder.characterGradeTextView.isSelected = true

        return holder
    }

    override fun onBindViewHolder(holder: GradeViewHolder, position: Int) {
        val grade = currentList[position]
        holder.apply {
            subjectNameTextView.text = grade.subjectName
            professorTextView.text = grade.professor
            subjectPointTextView.text = "${grade.subjectPoint}"
            classificationTextView.text = grade.classification
            characterGradeTextView.text = grade.characterGrade
        }
    }

    override fun submitList(list: MutableList<GradeEntity>?) {
        super.submitList(list?.let { ArrayList(it) })
    }
}