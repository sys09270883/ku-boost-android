package com.konkuk.boost.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.konkuk.boost.R
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    val container: ConstraintLayout by lazy {
        findViewById(R.id.container)
    }
    var appUpdateManager: AppUpdateManager? = null

    companion object {
        private const val REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkForUpdate()
    }

    private fun checkForUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfo = appUpdateManager?.appUpdateInfo
        appUpdateInfo?.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                requestUpdate(appUpdateManager!!, it)
            }
        }
    }

    private fun requestUpdate(manager: AppUpdateManager, info: AppUpdateInfo) {
        try {
            manager.startUpdateFlowForResult(
                info,
                AppUpdateType.IMMEDIATE,
                this,
                REQUEST_CODE
            )
        } catch (e: Exception) {
            Log.e("ku-boost", "${e.message}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.e("ku-boost", "Update failed. result code: $resultCode")
                appUpdateManager = AppUpdateManagerFactory.create(this)
                val appUpdateInfo = appUpdateManager?.appUpdateInfo
                appUpdateInfo?.addOnSuccessListener {
                    if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                        it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                    ) {
                        requestUpdate(appUpdateManager!!, it)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager?.appUpdateInfo?.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                try {
                    appUpdateManager!!.startUpdateFlowForResult(
                        it,
                        AppUpdateType.IMMEDIATE,
                        this,
                        REQUEST_CODE
                    )
                } catch (e: Exception) {
                    Log.e("ku-boost", "${e.message}")
                }
            }
        }
    }
}