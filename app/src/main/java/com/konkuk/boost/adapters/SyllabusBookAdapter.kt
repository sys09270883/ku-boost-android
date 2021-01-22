package com.konkuk.boost.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.konkuk.boost.R
import com.konkuk.boost.data.course.SyllabusBook

class SyllabusBookAdapter :
    ListAdapter<SyllabusBook, SyllabusBookAdapter.BookViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<SyllabusBook>() {
        override fun areItemsTheSame(oldItem: SyllabusBook, newItem: SyllabusBook): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SyllabusBook, newItem: SyllabusBook): Boolean {
            return oldItem.bookName == newItem.bookName
        }
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookDivTextView: TextView = itemView.findViewById(R.id.bookDivTextView)
        val bookNameTextView: TextView = itemView.findViewById(R.id.bookNameTextView)
        val bookAuthorTextView: TextView = itemView.findViewById(R.id.bookAuthorTextView)
        val bookCompanyTextView: TextView = itemView.findViewById(R.id.bookCompanyTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val holder = BookViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        )

        holder.bookNameTextView.isSelected = true

        return holder
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = currentList[position]
        holder.apply {
            bookNameTextView.text = book.bookName
            bookDivTextView.text = when (book.bookDiv ?: "") {
                "B23231" -> "주교재"
                "B23232" -> "부교재"
                "B23233" -> "참고문헌"
                else -> ""
            }
            bookAuthorTextView.text = book.author
            bookCompanyTextView.text = book.publishedCompany
        }
    }
}