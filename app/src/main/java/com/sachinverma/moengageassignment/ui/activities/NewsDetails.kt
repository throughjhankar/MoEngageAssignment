package com.sachinverma.moengageassignment.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.sachinverma.moengageassignment.R
import com.sachinverma.moengageassignment.utils.Constants
import com.squareup.picasso.Picasso

class NewsDetails : AppCompatActivity() {

    @BindView(R.id.news_web_view)
    lateinit var newsWebView: WebView

    @BindView(R.id.news_image)
    lateinit var newsImage: ImageView

    private var newsUrl = ""
    private var newsImageUrl = ""

    companion object {

        // starter pattern is more strict approach to starting an activity.
        // Main purpose is to improve more readability, while at the same time
        // decrease code complexity, maintenance costs, and coupling of your components.

        // Read more: https://blog.mindorks.com/learn-to-write-good-code-in-android-starter-pattern
        // https://www.programming-books.io/essential/android/starter-pattern-d2db17d348ca46ce8979c8af6504f018

        // Using starter pattern to start this activity
        fun start(
            context: Context,
            newsUrl: String,
            newsImageUrl: String
        ) {
            val intent = Intent(context, NewsDetails::class.java)
            intent.putExtra(Constants.NEWS_URL_KEY, newsUrl)
            intent.putExtra(Constants.NEWS_IMAGE__KEY, newsImageUrl)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        ButterKnife.bind(this)

        getDataFromIntent()

        setData()
    }

    private fun setData() {
        if (newsImageUrl.isNotEmpty()) {
            Picasso.get()
                .load(newsImageUrl)
                .placeholder(R.drawable.ic_poll_black_24dp)
                .error(R.drawable.ic_poll_black_24dp)
                .into(newsImage)
        }
        if (newsUrl.isNotEmpty()) {
            newsWebView.loadUrl(newsUrl)
        }
    }

    private fun getDataFromIntent() {
        this.intent?.let {
            if (it.hasExtra(Constants.NEWS_URL_KEY))
                this.newsUrl = it.getStringExtra(Constants.NEWS_URL_KEY) ?: ""

            if (it.hasExtra(Constants.NEWS_IMAGE__KEY))
                this.newsImageUrl = it.getStringExtra(Constants.NEWS_IMAGE__KEY) ?: ""
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}
