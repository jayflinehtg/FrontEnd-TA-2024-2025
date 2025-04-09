package com.example.compose_ta09.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ToolsUtil {
    // Fungsi untuk mengubah timestamp menjadi format tanggal
    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return formatter.format(Date(millis))
    }

    // Fungsi untuk mengubah string menjadi RequestBody (untuk multipart request)
    fun createPartFromString(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    // Fungsi untuk mengambil file dari Uri dan menyimpannya di cache directory
    fun getFileFromUri(context: Context, uri: Uri): File? {
        val contentResolver = context.contentResolver
        val fileName = getFileName(context, uri) ?: "temp_file_${System.currentTimeMillis()}"
        val file = File(context.cacheDir, fileName) // Simpan ke cacheDir

        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Fungsi untuk mendapatkan nama file dari Uri
    fun getFileName(context: Context, uri: Uri): String? {
        var name: String? = null
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    name = it.getString(index)
                }
            }
        }
        return name
    }

    // Fungsi untuk mendapatkan MIME type dari file berdasarkan Uri
    fun getMimeType(context: Context, uri: Uri): String {
        return context.contentResolver.getType(uri) ?: "application/octet-stream"
    }

    // Fungsi untuk mengekstrak nama file dari Content-Disposition header
    fun extractFileName(contentDisposition: String): String? {
        val regex = "filename=\"([^\"]+)\"".toRegex()
        return regex.find(contentDisposition)?.groupValues?.get(1)
    }

    // Fungsi untuk menemukan file yang sudah ada berdasarkan cid di direktori download
    fun findExistingFile(context: Context, cid: String): File? {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: return null
        val files = dir.listFiles { _, name -> name.startsWith("file$cid.") }
        return files?.firstOrNull() // Mengambil file pertama yang ditemukan
    }
}