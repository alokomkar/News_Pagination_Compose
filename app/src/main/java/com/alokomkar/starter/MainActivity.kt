package com.alokomkar.starter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.alokomkar.starter.local.NewsEntity
import com.alokomkar.starter.ui.theme.MyApplicationTheme
import com.alokomkar.starter.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewModel.fetchNews()

        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                //val newsUiState = viewModel.newsState.collectAsState()
                Surface(
                    color = MaterialTheme.colors.background
                ) {

                    val newsList = viewModel.newsPager.collectAsLazyPagingItems()
                    NewsListPaginated(newsEntities = newsList)

//                    RequestStateRender(
//                        state = newsUiState,
//                        onLoading = {
//                            LoadingScreen()
//                        },
//                        onError = {
//                            ErrorScreen(it.localizedMessage ?: "Something went wrong")
//                        },
//                        onSuccess = {
//                            NewsList(newsEntities = it)
//                        }
//                    )

                }
            }
        }
    }


}

@Composable
private fun ErrorScreen(error: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = error,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
        )
    }
}

@Composable
private fun NewsListPaginated(
    modifier: Modifier = Modifier,
    newsEntities: LazyPagingItems<NewsEntity>
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        state = rememberLazyListState()
    ) {
        items(items = newsEntities, key = { news -> news.createdAt }) { news ->
            news?.let { NewsCardView(news = it) }
        }

        when (val state = newsEntities.loadState.append) {
            is LoadState.NotLoading -> Unit
            LoadState.Loading -> {
                item {
                    LoadingScreen()
                }
            }
            is LoadState.Error -> {
                item {
                    state.error.printStackTrace()
                    ErrorScreen(error = "Some error occurred")
                }
            }
        }

        when (val state = newsEntities.loadState.refresh) {
            is LoadState.NotLoading -> Unit
            LoadState.Loading -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            is LoadState.Error -> {
                item {
                    state.error.printStackTrace()
                    ErrorScreen(error = "Some error occurred")
                }
            }
        }
    }
}

@Composable
private fun NewsList(modifier: Modifier = Modifier, newsEntities: List<NewsEntity>) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        state = rememberLazyListState()
    ) {
        items(
            items = newsEntities,
            key = { news -> news.createdAt }
        ) {
            NewsCardView(news = it)
        }
    }
}

@Composable
fun NewsCardView(modifier: Modifier = Modifier, news: NewsEntity) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = news.toString(), modifier = Modifier.fillMaxWidth())
        Divider(color = Color.DarkGray)
    }

}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MyApplicationTheme {
//        NewsList()
//    }
//}