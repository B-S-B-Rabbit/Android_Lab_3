package com.example.androidlab3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidlab3.databinding.NewsItemBinding
import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.text.toSpannable


class NewsAdapter : ListAdapter<Article, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class NewsViewHolder(private val binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.title.text = article.title
            binding.description.text = article.description

            val openNewText = "Open news"

            val spannable: Spannable = openNewText.toSpannable()

            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    article.link?.let { openUrl(it) }
                }
            }

            spannable.setSpan(
                clickableSpan,
                0,
                openNewText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            binding.url.text = spannable
            binding.url.movementMethod = LinkClickMovementMethod()

        }

        private fun openUrl(url: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            binding.root.context.startActivity(intent)
        }

        private class LinkClickMovementMethod : LinkMovementMethod() {
            override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
                try {
                    return super.onTouchEvent(widget, buffer, event)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return false
            }
        }
    }

    private class NewsDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.article_id == newItem.article_id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}


