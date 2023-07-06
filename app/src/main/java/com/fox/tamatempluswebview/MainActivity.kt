package com.fox.tamatempluswebview

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.fox.tamatempluswebview.databinding.ActivityMainBinding
import com.fox.tamatempluswebview.databinding.LayoutWebviewBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mBinding = ActivityMainBinding.inflate(
            layoutInflater
        )

        setContentView(mBinding.root)

        /**
         * setting click listener for Open Browser Button
         */
        mBinding.btnOpenBrowser.setOnClickListener { launchWebViewModal() }
    }

    /**
     * Creates and launches a Dialog that has a webView
     */
    private fun launchWebViewModal() {
        val webViewBinding = LayoutWebviewBinding.inflate(layoutInflater)

        /**
         * creating dialog and setting a custom theme
         */
        val dialog = Dialog(this, R.style.DialogTheme)
        dialog.setContentView(webViewBinding.root)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        val webView = webViewBinding.webView

        setUpWebView(webView)

        /**
         * using setPictureListener instead on onPageFinished to enable/disable navigation buttons.
         *
         * because onPageFinished does not get triggered at first interaction
         */
        webView.setPictureListener { view, p1 ->
            //Make Enable or Disable buttons
            webViewBinding.btnBack.isEnabled = view.canGoBack()
            webViewBinding.btnForward.isEnabled = view.canGoForward()

            webViewBinding.progressBar.visibility = View.GONE
        }

        /**
         * setting webView Client
         */
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                webViewBinding.progressBar.visibility = View.VISIBLE
            }
        }

        /**
         * loading url
         */
        webView.loadUrl("https://tamatemplus.com")

        /**
         * showing the dialog
         */
        dialog.show()

        /**
         * handle controls click listeners
         */
        webViewBinding.btnClose.setOnClickListener {//dismiss dialog
            dialog.dismiss()
        }

        webViewBinding.btnRefresh.setOnClickListener { //reload current webpage
            webView.reload()
        }

        webViewBinding.btnBack.setOnClickListener {//move backwards
            if (webView.canGoBack()) {
                webView.goBack()
            }
        }

        webViewBinding.btnForward.setOnClickListener {//move forward
            if (webView.canGoForward()) {
                webView.goForward()
            }
        }
    }

    /**
     * setUp web View webSettings
     *
     * @param webView WebView
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView(webView: WebView) {
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.pluginState = WebSettings.PluginState.ON
    }
}