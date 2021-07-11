package cz.fit.cvut.and.intents

import android.app.Activity
import android.os.Bundle
import android.webkit.WebView

class WebActivity : Activity() {

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val uri = intent.data ?: return
        val webView = findViewById<WebView>(R.id.web)
        webView.loadUrl(uri.toString())
    }

}
