package com.guru.bonial.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import com.guru.bonial.domain.INewsRepository
import com.guru.bonial.view.news.utils.LoadingState
import com.guru.bonial.view.news.viewmodel.MainViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class TestMainViewMode {

    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var newsRepository: INewsRepository

    private lateinit var mainViewModel: MainViewModel

    val lifecycleOwner = TestLifecycleOwner()

    @Before
    fun setup() {
        mainViewModel = Mockito.spy(MainViewModel(newsRepository))
        mainViewModel!!.pageSize.testObserver()
    }

    @Test
    fun testNull() {
        Assert.assertNotNull(mainViewModel!!.pageSize)
        Assert.assertTrue(mainViewModel!!.pageSize.hasObservers())
    }

    @Test
    fun testPageSizeValue() {
        Assert.assertNotNull(mainViewModel.pageSize.hasObservers())
        Assert.assertEquals(mainViewModel.pageSize.value, 21)
    }

    @Test
    fun testLoadingState() {
        mainViewModel.newsTransformer.observe(lifecycleOwner, Observer {
            Assert.assertEquals(it!!.loadingState, LoadingState.LOADING)
        })
    }

    @Test
    fun testTransformations() {
        //Todo transformations

    }

    fun <T> LiveData<T>.testObserver() = TestObserver<T>().also {
        observeForever(it)
    }

    class TestObserver<T> : Observer<T> {
        val observedValues = mutableListOf<T?>()

        override fun onChanged(value: T?) {
            observedValues.add(value)
        }
    }

    inner class TestLifecycleOwner : LifecycleOwner {
        private val mLifecycle: LifecycleRegistry

        init {
            mLifecycle = LifecycleRegistry(this)
        }

        override fun getLifecycle(): Lifecycle {
            return mLifecycle
        }

        fun handleEvent(event: Lifecycle.Event) {
            mLifecycle.handleLifecycleEvent(event)
        }
    }

    /* private fun getNews(): PagedListWrapper<News> {
         val newsResponse = JsonFileReader.read(
             javaClass.classLoader.getResourceAsStream("news.json"),
             Gson(), PaginatedResponse::class.java
         ).blockingFirst()

         val modelList = mutableListOf<NewsDataModel>()
         val newsList = newsResponse.articles.fold(modelList)
         { acc, news ->
             acc.add(news.toModel())
             acc
         }

         val pagedListWrapper = PagedListWrapper.pagedList
     } */
}
