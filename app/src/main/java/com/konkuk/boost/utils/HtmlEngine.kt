package com.konkuk.boost.utils

import android.content.res.AssetManager
import android.util.Log

object HtmlEngine {

    const val CERTIFICATE_OF_ACQUIRED = "certificate_of_acquired.html"

    fun getCertificateOfAcquired(am: AssetManager, stdNo: Int): String {
        var buffer: ByteArray? = null
        try {
            val inputStream = am.open(CERTIFICATE_OF_ACQUIRED)
            val size = inputStream.available()
            buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
        } catch (e: Exception) {
            Log.e("ku-boost", "${e.message}")
        }

        val builder = StringBuilder(String(buffer ?: throw Exception()))
        val target = "ast_std_no="
        val idx = builder.indexOf(target)
        builder.insert(idx + target.length, stdNo)

        return builder.toString()
    }

}