package com.alokomkar.starter

import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.alokomkar.starter.local.NewsEntity
import com.alokomkar.starter.ui.details.NewsDetailsWebComponent
import com.alokomkar.starter.ui.theme.MyApplicationTheme
import com.alokomkar.starter.utils.RequestStateRender
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
                val newsUiState = viewModel.newsState.collectAsState()
                Surface(
                    color = MaterialTheme.colors.background
                ) {

                    val newsList = viewModel.newsPager.collectAsLazyPagingItems()
                    NewsListPaginated(newsEntities = newsList)

                    RequestStateRender(
                        state = newsUiState,
                        onIdle = { /* Do nothing */ },
                        onSuccess = {
                            it?.let {
                                NewsDetailsScreen(newsEntity = it)
                            }
                        }
                    )

                }
            }
        }
    }

    @Composable
    private fun NewsListPaginated(
        modifier: Modifier = Modifier,
        newsEntities: LazyPagingItems<NewsEntity>
    ) {
        val context = LocalContext.current
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
            state = rememberLazyListState()
        ) {
            items(items = newsEntities, key = { news -> news.createdAt }) { news ->
                news?.let { NewsCardView(news = it) { newsEntity ->
                    val webUrl = newsEntity.webUrl
                    if(webUrl.isEmpty()) {
                        Toast.makeText(context, "No webpage available", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        viewModel.onNewsItemSelection(newsEntity)
                    }
                }}
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
                        ErrorScreen(error = "Some error occurred while loading data")
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
    private fun NewsDetailsScreen(modifier: Modifier = Modifier, newsEntity: NewsEntity) {

        val isLoading = remember { mutableStateOf(false) }
        val newsDetailsWebComponent = remember {
            NewsDetailsWebComponent(isLoading)
        }

        BackHandler(onBack = {
            if(!newsDetailsWebComponent.canGoBack()) {
                viewModel.resetSelection()
            }
        })

        Box(modifier = modifier.fillMaxSize()) {
            AndroidView(
                modifier = modifier.fillMaxSize(),
                factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        newsDetailsWebComponent.init(this)
                        loadUrl(newsEntity.webUrl)
                    }
                }, update = {
                    /* Do nothing */
                })

            if(isLoading.value) {
                LoadingScreen()
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
private fun NewsCardView(
    modifier: Modifier = Modifier,
    news: NewsEntity,
    onClick: (news: NewsEntity) -> Unit
) {
    Card(
        modifier = modifier.clickable {
           onClick.invoke(news)
        },
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(8.dp)),
        elevation = 4.dp
    ) {
        Column(modifier = modifier
            .padding(all = 8.dp)
            .fillMaxWidth()) {
            Text(
                text = news.title,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body1
            )
            Text(
                text = news.author,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.body2
            )
            Text(
                text = news.createdAt.split("T")[0],
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.subtitle2
            )
        }
    }


}

@Composable
fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    // Safely update the current `onBack` lambda when a new one is provided
    val currentOnBack by rememberUpdatedState(onBack)
    // Remember in Composition a back callback that calls the `onBack` lambda
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }
    // On every successful composition, update the callback with the `enabled` value
    SideEffect {
        backCallback.isEnabled = enabled
    }
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        // Add callback to the backDispatcher
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        // When the effect leaves the Composition, remove the callback
        onDispose {
            backCallback.remove()
        }
    }
}

//@Composable
//private fun NewsList(modifier: Modifier = Modifier, newsEntities: List<NewsEntity>) {
//    LazyColumn(
//        modifier = modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        contentPadding = PaddingValues(16.dp),
//        state = rememberLazyListState()
//    ) {
//        items(
//            items = newsEntities,
//            key = { news -> news.createdAt }
//        ) {
//            NewsCardView(news = it)
//        }
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MyApplicationTheme {
//        NewsList()
//    }
//}