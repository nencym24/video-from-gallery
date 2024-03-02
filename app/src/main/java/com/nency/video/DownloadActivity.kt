package com.nency.video

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DownloadActivity : AppCompatActivity() {

    private lateinit var videoFoldersAdapter: VideoFoldersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        val recyclerView = findViewById<RecyclerView>(R.id.rcvVideo)
        recyclerView.layoutManager = LinearLayoutManager(this)
        videoFoldersAdapter = VideoFoldersAdapter { folder ->
            openFolderVideos(folder)
        }
        recyclerView.adapter = videoFoldersAdapter

        if (hasReadStoragePermission()) {
            loadVideoFolders()
        } else {
            requestStoragePermission()
        }
    }
    private fun openFolderVideos(folder: VideoFolder) {
        val intent = Intent(this, VideoPlayerActivity::class.java)
        intent.putExtra(VideoPlayerActivity.EXTRA_FOLDER, folder)
        startActivity(intent)
    }

    private fun loadVideoFolders() {
        val videoFolders = getVideoFolders(this)
        videoFoldersAdapter.setVideoFolders(videoFolders)
    }

    private fun hasReadStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadVideoFolders()
            } else {
                Log.e(TAG, "Permission denied")
            }
        }
    }

    companion object {
        private const val TAG = "VideoFoldersActivity"
        private const val STORAGE_PERMISSION_CODE = 101
    }

    fun getVideoFolders(context: Context): List<VideoFolder> {
        val videoFolders = mutableListOf<VideoFolder>()
        val contentResolver: ContentResolver = context.contentResolver
        val projection = arrayOf(
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME
        )

        val videoUri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val cursor = contentResolver.query(
            videoUri,
            projection,
            null,
            null,
            null
        )

        val folderMap = mutableMapOf<String, VideoFolder>()

        cursor?.use { c ->
            val dataColumn = c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            val folderNameColumn =
                c.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)

            while (c.moveToNext()) {
                val folderName = c.getString(folderNameColumn)
                val videoPath = c.getString(dataColumn)

                val folder = folderMap[folderName]
                if (folder == null) {
                    folderMap[folderName] = VideoFolder(folderName, videoPath, 1)
                } else {
                    folder.videoCount++
                }
            }
        }

        cursor?.close()

        videoFolders.addAll(folderMap.values)

        return videoFolders
    }
}
