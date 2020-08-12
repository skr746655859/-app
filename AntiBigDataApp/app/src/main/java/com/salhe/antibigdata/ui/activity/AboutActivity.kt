package com.salhe.antibigdata.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.salhe.antibigdata.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        webView.loadUrl("file:///android_asset/about.html")
    }


}