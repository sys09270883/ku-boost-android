package com.konkuk.boost.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.konkuk.boost.R
import com.konkuk.boost.data.course.RegistrationStatusData

class LikeCourseAdapter :
    ListAdapter<RegistrationStatusData, LikeCourseAdapter.LikeCourseViewHolder>(DiffCallback) {

    lateinit var context: Context
    lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(registrationStatusData: RegistrationStatusData)
    }

    object DiffCallback : DiffUtil.ItemCallback<RegistrationStatusData>() {
        override fun areItemsTheSame(
            oldItem: RegistrationStatusData,
            newItem: RegistrationStatusData
        ): Boolean {
            return oldItem.likeCourseEntity == newItem.likeCourseEntity
        }

        override fun areContentsTheSame(
            oldItem: RegistrationStatusData,
            newItem: RegistrationStatusData
        ): Boolean {
            return oldItem.likeCourseEntity.subjectId == newItem.likeCourseEntity.subjectId
        }
    }

    inner class LikeCourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectIdTextView: TextView = itemView.findViewById(R.id.subjectIdTextView)
        val subjectNameTextView: TextView = itemView.findViewById(R.id.subjectNameTextView)
        val professorTextView: TextView = itemView.findViewById(R.id.professorTextView)
        val results = listOf<TextView>(
            itemView.findViewById(R.id.resultTextView1),
            itemView.findViewById(R.id.resultTextView2),
            itemView.findViewById(R.id.resultTextView3),
            itemView.findViewById(R.id.resultTextView4)
        )
        val baskets = listOf<TextView>(
            itemView.findViewById(R.id.basketTextView1),
            itemView.findViewById(R.id.basketTextView2),
            itemView.findViewById(R.id.basketTextView3),
            itemView.findViewById(R.id.basketTextView4)
        )
        val registrations = listOf<TextView>(
            itemView.findViewById(R.id.registrationTextView1),
            itemView.findViewById(R.id.registrationTextView2),
            itemView.findViewById(R.id.registrationTextView3),
            itemView.findViewById(R.id.registrationTextView4)
        )
        val limited = listOf<TextView>(
            itemView.findViewById(R.id.limitedTextView1),
            itemView.findViewById(R.id.limitedTextView2),
            itemView.findViewById(R.id.limitedTextView3),
            itemView.findViewById(R.id.limitedTextView4)
        )

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
        context = parent.context

        return holder
    }

    override fun onBindViewHolder(holder: LikeCourseViewHolder, position: Int) {
        val item = currentList[position]

        holder.apply {
            subjectIdTextView.text = item.likeCourseEntity.subjectId
            subjectNameTextView.text = item.likeCourseEntity.subjectName
            professorTextView.text = item.likeCourseEntity.professor

            val list = item.registrationStatusList
            for (i in 0..3) {
                baskets[i].text = list[i].classBasketNumber
                registrations[i].text = list[i].registrationNumber
                limited[i].text = list[i].limitedNumber

                val basket = list[i].classBasketNumber.toInt()
                val registration = list[i].registrationNumber.toInt()
                val limited = list[i].limitedNumber.toInt()

                if (basket > limited - registration) {
                    results[i].text = "인원초과"
                    results[i].setTextColor(ContextCompat.getColor(context, R.color.pastelRed))
                } else {
                    results[i].text = "${limited - registration - basket}"
                    results[i].setTextColor(ContextCompat.getColor(context, R.color.pastelBlue))
                }
            }

        }
    }
}