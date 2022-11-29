package com.alokomkar.starter.ui.details

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.MutableState

class NewsDetailsWebComponent(private val isLoading: MutableState<Boolean>) {

    private var webView: WebView? = null

    private val newsDetailsWebViewClient by lazy {
        object: WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                isLoading.value = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                isLoading.value = false
            }
        }
    }

    fun init(webView: WebView) {
        this.webView = webView
        this.webView?.apply {
            webViewClient = newsDetailsWebViewClient
        }
    }

    fun canGoBack(): Boolean {
        if(webView?.canGoBack() == true) {
            webView?.goBack()
            return true
        }
        return false
    }
}