package com.example.petpulse

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object FileUtil {
    fun from(context: Context, uri: Uri): File {
        val input = context.contentResolver.openInputStream(uri)!!
        val file = File(context.cacheDir, "upload_file")
        FileOutputStream(file).use { output ->
            input.copyTo(output)
        }
        return file
    }
}
