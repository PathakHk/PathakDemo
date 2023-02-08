package com.navkar.myapp.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.navkar.myapp.R
import com.navkar.myapp.databinding.ActivityPdfBinding
import io.paperdb.Paper


class PDFActivity : AppCompatActivity(), ViewTreeObserver.OnScrollChangedListener {

    lateinit var mBinder: ActivityPdfBinding
    private var mContext: Context? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_pdf)
        mContext = this

        mBinder.webview.getSettings().setJavaScriptEnabled(true)
        mBinder.webview.getSettings().setDomStorageEnabled(true)
        mBinder.webview.getSettings().setBuiltInZoomControls(true)
        mBinder.webview.getSettings().setDisplayZoomControls(false)
        mBinder.webview.getSettings().setUseWideViewPort(true)
        mBinder.webview.getSettings().setLoadWithOverviewMode(true)
        //mBinder.webview.loadUrl("https://onlinedocumentviewer.com/blog/online-ppt-pptx-powerpoint-viewer.aspx")
        mBinder.webview.loadUrl("http://www.tutorialspoint.com")

        mBinder.webview.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url);
                return true;
            }

            override fun onLoadResource(view: WebView, url: String) {}
            override fun onPageFinished(view: WebView, url: String) {
              //  var a = Paper.book().read("wv_pos", 0)
            //    mBinder.scrollView.scrollTo(0, a)
            }
        })

        mBinder.scrollView.getViewTreeObserver().addOnScrollChangedListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        val a = mBinder.scrollView.scrollY
       // Paper.book().write("wv_pos", a)
    }

    override fun onScrollChanged() {
        val view: View = mBinder.scrollView.getChildAt(mBinder.scrollView.getChildCount() - 1)
        val topDetector: Int = mBinder.scrollView.getScrollY()
        val bottomDetector: Int = view.getBottom() - (mBinder.scrollView.getHeight() + mBinder.scrollView.getScrollY())
        if (bottomDetector == 0) {
            Toast.makeText(baseContext, "Scroll View bottom reached", Toast.LENGTH_SHORT).show()
        }
        if (topDetector <= 0) {
            Toast.makeText(baseContext, "Scroll View top reached", Toast.LENGTH_SHORT).show()
        }
    }


}