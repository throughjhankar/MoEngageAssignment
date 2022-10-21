package com.sachinverma.moengageassignment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.sachinverma.moengageassignment.R
import com.sachinverma.moengageassignment.model.News
import com.sachinverma.moengageassignment.ui.adapter.viewholder.NewsListViewHolder
import com.sachinverma.moengageassignment.ui.adapter.viewholder.OnNewsItemActionListener

/**
 * This class is responsible for drawing the list items on screen. We are using ListAdapter instead
 * of traditional RecyclerView because it comes with some benefits. To update the list we need not
 * to re-create a whole new adapter and set it recycler view which is not the case with recycler view
 * adapter.
 * ListAdapter doesn't update whole list on view but it takes advantage of DiffUtil to identify which
 * item got changed and only updates that particular item on the view.
 * One problem with ListAdapter us that to refresh the list it requires whole new reference of the list
 * so that it can compare it with the previous list.
 */
class NewsListAdapter(private val listener: OnNewsItemActionListener) :
    ListAdapter<News, NewsListViewHolder>(NewsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsListViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.row_news_item, parent, false), listener
    )

    override fun onBindViewHolder(holder: NewsListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NewsDiffUtil : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }
}
