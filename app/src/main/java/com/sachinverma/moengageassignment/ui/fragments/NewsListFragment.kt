package com.sachinverma.moengageassignment.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.sachinverma.moengageassignment.R
import com.sachinverma.moengageassignment.model.News
import com.sachinverma.moengageassignment.ui.adapter.NewsListAdapter
import com.sachinverma.moengageassignment.ui.adapter.viewholder.OnNewsItemActionListener
import com.sachinverma.moengageassignment.ui.viewmodel.NewsListScreenState
import com.sachinverma.moengageassignment.ui.viewmodel.NewsListVM
import com.sachinverma.moengageassignment.ui.viewmodel.Sort
import com.sachinverma.moengageassignment.utils.NetworkManager
import com.sachinverma.moengageassignment.utils.hide
import com.sachinverma.moengageassignment.utils.show

class NewsListFragment : Fragment(R.layout.fragment_news_list), OnNewsItemActionListener,
    NetworkManager.NetworkStateListener {

    private val viewModel: NewsListVM by lazy {
        ViewModelProvider.NewInstanceFactory().create(NewsListVM::class.java)
    }

    private lateinit var newsListRV: RecyclerView
    private lateinit var noDataTV: TextView
    private lateinit var loadingPB: ProgressBar
    private lateinit var retryButton: Button
    private lateinit var adapter: NewsListAdapter
    private lateinit var newsList: List<News>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        initViews()
        initAdapter()
        viewModel.currentState.observe(viewLifecycleOwner, Observer(::reactToState))
        viewModel.onViewCreated(this)
    }

    /**
     * Setting up menus for user to sort the list based on Title or Date.
     * Since we are using ListAdapter, it maintains the position on current top visible item.
     * For example if you are at 10th item and sorts the list based on any criteria then it will
     * sort the list but it will not take you to the top of the list.
     */
    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.sort_by_title -> {
                        viewModel.onMenuItemTapped(Sort.TITLE, newsList)
                        true
                    }
                    R.id.sort_by_date -> {
                        viewModel.onMenuItemTapped(Sort.DATE, newsList)
                        true
                    }
                    else -> false
                }
            }
        })
    }

    private fun initViews() {
        view?.apply {
            newsListRV = findViewById(R.id.rv_news)
            noDataTV = findViewById(R.id.no_news_tv)
            loadingPB = findViewById(R.id.loading_pb)
            retryButton = findViewById(R.id.retry_button)
        }

        retryButton.setOnClickListener {
            viewModel.onRetry(this)
        }
    }

    /**
     * This method is responsible for reacting to the Sate of the View
     */
    private fun reactToState(state: NewsListScreenState) {
        when (state) {
            is NewsListScreenState.Data -> {
                handleDataState(state)
            }
            is NewsListScreenState.Error -> {
                handleErrorState(state)
            }
            is NewsListScreenState.Loading -> {
                handleLoadingState()
            }
        }
    }

    /**
     * This method is responsible for showing views based on which state the Fragment is.
     */
    private fun showViewsForState(view: View) {
        when (view) {
            noDataTV -> {
                noDataTV.show()
                retryButton.show()
                loadingPB.hide()
                newsListRV.hide()
            }
            loadingPB -> {
                loadingPB.show()
                noDataTV.hide()
                newsListRV.hide()
                retryButton.hide()
            }
            newsListRV -> {
                newsListRV.show()
                noDataTV.hide()
                loadingPB.hide()
                retryButton.hide()
            }
        }
    }

    private fun handleErrorState(state: NewsListScreenState.Error) {
        noDataTV.text = state.errorMessage
        showViewsForState(noDataTV)
    }

    private fun handleLoadingState() {
        showViewsForState(loadingPB)
    }

    private fun handleDataState(state: NewsListScreenState.Data) {
        newsList = state.newsList
        showViewsForState(newsListRV)
        setDataToAdapter(state.newsList)
    }

    private fun initAdapter() {
        adapter = NewsListAdapter(this)
        newsListRV.adapter = adapter
    }

    private fun setDataToAdapter(list: List<News>) {
        adapter.submitList(list)
    }

    /**
     * Basic idea of MVVM pattern - Business logic will be handled by View Model
     * So View is only informing View Model about what has happened, and View Model will decide
     * what to do.
     */
    override fun onItemClicked(news: News) {
        viewModel.onNewsItemTapped(requireContext(), news)
    }

    /**
     * Basic idea of MVVM pattern - Business logic will be handled by View Model
     * So View is only informing View Model about what has happened, and View Model will decide
     * what to do.
     */
    override fun onAvailable() {
        viewModel.onNetworkAvailable()
    }

    /**
     * Basic idea of MVVM pattern - Business logic will be handled by View Model
     * So View is only informing View Model about what has happened, and View Model will decide
     * what to do.
     */
    override fun onLost() {
        viewModel.onNetworkLost()
    }
}
