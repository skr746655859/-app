package com.salhe.antibigdata.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils.SimpleStringSplitter
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.salhe.antibigdata.R
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    val REQUEST_READ_PHONE_STATE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                REQUEST_READ_PHONE_STATE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_READ_PHONE_STATE -> if (!(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "权限已被用户拒绝", Toast.LENGTH_SHORT).show()
            }
        }
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

    override fun onResume() {
        super.onResume()
        if (!isAccessibilitySettingsOn(this)) {
            Snackbar.make(container, "AccessibilityService 未开启，请手动开启", Snackbar.LENGTH_INDEFINITE)
                .apply {
                    setAction("去设置") {
                        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                        dismiss()
                    }
                }
                .show()
        }
    }

    // To check if service is enabled
    // https://blog.csdn.net/mp624183768/article/details/77448014
    private fun isAccessibilitySettingsOn(mContext: Context): Boolean {
        var accessibilityEnabled = 0
        val service =
            "com.salhe.antibigdata/com.salhe.antibigdata.service.CollectProductAccessibilityService"
        val accessibilityFound = false
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                mContext.getApplicationContext().getContentResolver(),
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
            // Log.v(FragmentActivity.TAG, "accessibilityEnabled = $accessibilityEnabled")
        } catch (e: SettingNotFoundException) {
            // Log.e(
            //     FragmentActivity.TAG, "Error finding setting, default accessibility to not found: "
            //             + e.message
            // )
        }
        val mStringColonSplitter = SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            // Log.v(FragmentActivity.TAG, "***ACCESSIBILIY IS ENABLED*** -----------------")
            val settingValue = Settings.Secure.getString(
                mContext.getApplicationContext().getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessabilityService = mStringColonSplitter.next()
                    // Log.v(
                    //     FragmentActivity.TAG,
                    //     "-------------- > accessabilityService :: $accessabilityService"
                    // )
                    if (accessabilityService.equals(service, ignoreCase = true)) {
                        // Log.v(
                        //     FragmentActivity.TAG,
                        //     "We've found the correct setting - accessibility is switched on!"
                        // )
                        return true
                    }
                }
            }
        } else {
            // Log.v(FragmentActivity.TAG, "***ACCESSIBILIY IS DISABLED***")
        }
        return accessibilityFound
    }
}