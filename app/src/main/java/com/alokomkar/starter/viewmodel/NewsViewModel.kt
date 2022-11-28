package com.alokomkar.starter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.alokomkar.core.IoDispatcher
import com.alokomkar.starter.local.NewsEntity
import com.alokomkar.starter.pager.NewsPagerSource
import com.alokomkar.starter.repository.INewsRepository
import com.alokomkar.starter.utils.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: INewsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
)  : ViewModel(){

    val newsPager = Pager(
        PagingConfig(pageSize = 20)
    ) {
        NewsPagerSource(repository)
    }.flow.cachedIn(viewModelScope)


    private val _newsState = MutableStateFlow<RequestState<NewsEntity?, Error>>(RequestState.Idle)
    val newsState = _newsState.asStateFlow()

    fun onNewsItemSelection(newsEntity: NewsEntity) {
        _newsState.value = RequestState.Success(newsEntity)
    }

    fun resetSelection() {
        _newsState.value = RequestState.Idle
    }

//    fun fetchNews(page: Int = 1) {
//        viewModelScope.launch(ioDispatcher) {
//            try {
//                _newsState.value = RequestState.Loading
//                repository.fetchNewsFromPage(
//                    page
//                ).collectLatest {
//                    _newsState.value = RequestState.Success(it)
//                }
//            } catch( e: Exception ) {
//                e.printStackTrace()
//                _newsState.value = RequestState.Error(Error(e.localizedMessage))
//            }
//
//        }
//
//    }

}