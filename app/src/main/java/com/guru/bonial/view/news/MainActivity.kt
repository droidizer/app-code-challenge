package com.guru.bonial.view.news

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guru.bonial.R
import com.guru.bonial.view.news.utils.LoadingState
import com.guru.bonial.view.news.adapter.NewsAdapter
import com.guru.bonial.view.news.viewmodel.NewsViewModelFactory
import com.guru.bonial.view.news.model.NewsDataModel
import com.guru.bonial.view.news.viewmodel.MainViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var newsFactory: NewsViewModelFactory

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, newsFactory)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3

        val gridLayoutManager = GridLayoutManager(this, spanCount)
        newsList.layoutManager = gridLayoutManager

        val adapter = NewsAdapter {
            launchNewsArticle(it)
        }

        newsList.adapter = adapter

        viewModel.news.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.loadingState.observe(this, Observer {

            if (it == LoadingState.ERROR) {
                val error = findViewById<TextView>(R.id.error)
                newsList.toVisibility(false)
                error.toVisibility(true)
            } else {
                adapter.latestLoadingState(it)
            }
        })

        newsList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            val margin = resources.getDimension(R.dimen.margin_8).toInt()

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val position = parent.getChildAdapterPosition(view)
                if (position == 0) {
                    outRect.set(
                        margin,
                        margin,
                        margin,
                        0
                    )
                } else {
                    outRect.set(
                        margin / 2,
                        margin,
                        margin / 2,
                        margin
                    )
                }
            }
        })


        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val hasExtraRow = adapter.hasExtraRow()
                val count = adapter.itemCount
                return if (position % 7 == 0 || (hasExtraRow && position == count - 1)) {
                    spanCount
                } else {
                    1
                }
            }
        }
    }

    private fun launchNewsArticle(newsData: NewsDataModel) {
        newsData.url ?: return
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(newsData.url)))
        } catch (e: Exception) {
            Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
        }
    }
}

fun View.toVisibility(visible: Boolean) {
    visibility = when (visible) {
        true -> View.VISIBLE
        false -> View.GONE
    }
}
