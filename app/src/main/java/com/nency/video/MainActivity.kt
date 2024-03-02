package com.nency.video

import android.Manifest
import android.app.DownloadManager
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.util.Objects


class MainActivity : AppCompatActivity() {

     lateinit var urlEditText: EditText
     lateinit var download : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        urlEditText = findViewById(R.id.urlEditText)
        download = findViewById(R.id.btnDownload)

        download.setOnClickListener {
            downloadVideo()
//            downloadFile()
//            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//            dir.mkdirs()
//            val tsLong = System.currentTimeMillis() / 1000
//            val ts = tsLong.toString()
//     val file =        File(dir, "$ts.mp4")
//        downloadFile(urlEditText.text.toString(),file)
        }
    }

    fun downloadVideo() {
        val url = urlEditText.text.toString()
        Log.e(  "downloadVideo: ",url )
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    saveVideoLocally(responseBody?.bytes())
                } else {
                }
            }
        })
    }
     fun saveVideoLocally(videoData: ByteArray?) {
        if (videoData == null) return

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            dir.mkdirs()
            val tsLong = System.currentTimeMillis() / 1000
            val ts = tsLong.toString()

            val file = File(dir, "$ts.mp4")

            try {
                val fos = FileOutputStream(file)
                fos.write(videoData)
                fos.close()

                val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
//                downloadManager.enqueue()
                downloadManager.addCompletedDownload(
                    "Video Download",
                    "Download complete",
                    true,
                    "video/mp4",
                    file.absolutePath,
                    file.length(),
                    true
                )

                MediaScannerConnection.scanFile(this, arrayOf(file.path), arrayOf("video/*")) { str, uri ->
                    Log.e(  "saveVideoLocally:ss ",str )
                    Log.e(  "saveVideoLocally:uri ",uri.path.toString() )

                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    }
    private fun downloadFile(url: String, outputFile: File) {
        try {
            val u = URL(url)
            val conn = u.openConnection()
            val contentLength = conn.contentLength
            val stream = DataInputStream(u.openStream())
            val buffer = ByteArray(contentLength)
            stream.readFully(buffer)
            stream.close()
            val fos = DataOutputStream(FileOutputStream(outputFile))
            fos.write(buffer)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            return  // swallow a 404
        } catch (e: IOException) {
            return  // swallow a 404
        }
    }

}