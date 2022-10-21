package com.sachinverma.moengageassignment.ui.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sachinverma.moengageassignment.R
import com.sachinverma.moengageassignment.model.News
import com.squareup.picasso.Picasso

class NewsListViewHolder(itemView: View, private val listener: OnNewsItemActionListener) :
    RecyclerView.ViewHolder(itemView) {

    private var newImage: ImageView = itemView.findViewById(R.id.news_image)
    private var newsTitle: TextView = itemView.findViewById(R.id.news_title)
    private var newsDescription: TextView = itemView.findViewById(R.id.news_description)
    private var newsAuthor: TextView = itemView.findViewById(R.id.news_author)
    private var rowNews: ConstraintLayout = itemView.findViewById(R.id.cl_news_item_row)

    fun bind(news: News) {

        newsTitle.text = news.title
        newsDescription.text = news.description
        newsAuthor.text = news.author

        Picasso.get()
            .load(news.urlToImage)
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
            .resize(250, 250)
            .centerCrop()
            .into(newImage)

        rowNews.setOnClickListener {
            listener.onItemClicked(news)
        }
    }
}

interface OnNewsItemActionListener {
    fun onItemClicked(news: News)
}
