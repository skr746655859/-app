package com.salhe.antibigdata.ui.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.salhe.antibigdata.R

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    fun viewProducts(v: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun viewUserPolicy(v: View) {
        startActivity(Intent(this, AboutActivity::class.java))
    }

    fun openAccessibilityService(v: View) {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }
}