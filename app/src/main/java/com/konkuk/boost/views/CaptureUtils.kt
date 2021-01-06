package com.konkuk.boost.views

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import com.konkuk.boost.utils.DateTimeConverter
import java.io.File
import java.io.FileOutputStream

object CaptureUtils {

    fun capture(context: Context, view: View) {
        val filename = "card${DateTimeConverter.currentTime()}.jpg"
        val bitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
            val collection =
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val item = context.contentResolver.insert(collection, values)!!
            context.contentResolver.openAssetFileDescriptor(item, "w", null).use {
                val out = FileOutputStream(it!!.fileDescriptor)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.close()
            }
            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            context.contentResolver.update(item, values, null, null)
        } else {
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                .toString() +
                    File.separator +
                    "boost"
            val file = File(dir)
            if (!file.exists()) {
                file.mkdirs()
            }

            val imgFile = File(file, filename)
            val os = FileOutputStream(imgFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
            val values = ContentValues()
            with(values) {
                put(MediaStore.Images.Media.TITLE, filename)
                put(MediaStore.Images.Media.DATA, imgFile.absolutePath)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            }
            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )
        }
    }

}