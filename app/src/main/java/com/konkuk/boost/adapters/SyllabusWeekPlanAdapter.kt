package com.konkuk.boost.adapters

import android.annotation.SuppressLint
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.konkuk.boost.R
import com.konkuk.boost.data.course.SyllabusWeekPlan

class SyllabusWeekPlanAdapter :
    ListAdapter<SyllabusWeekPlan, SyllabusWeekPlanAdapter.PlanViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<SyllabusWeekPlan>() {
        override fun areItemsTheSame(
            oldItem: SyllabusWeekPlan,
            newItem: SyllabusWeekPlan
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: SyllabusWeekPlan,
            newItem: SyllabusWeekPlan
        ): Boolean {
            return oldItem.lectureContent == newItem.lectureContent
        }
    }

    inner class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weekTextView: TextView = itemView.findViewById(R.id.weekTextView)
        val themeTextView: TextView = itemView.findViewById(R.id.themeTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val roomTextView: TextView = itemView.findViewById(R.id.roomTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val holder = PlanViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.week_plan_item, parent, false)
        )

        holder.contentTextView.movementMethod = ScrollingMovementMethod()

        return holder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val item = currentList[position]
        holder.apply {
            weekTextView.text = "${item.weekNo}주차"
            themeTextView.text = item.theme
            contentTextView.text = item.lectureContent
            roomTextView.text = item.roomName
        }
    }
}