package com.nency.video

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class VideoPlayerActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_FOLDER = "folder"
    }

    private lateinit var folder: VideoFolder
    private lateinit var videoRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        folder = intent.getParcelableExtra(EXTRA_FOLDER) ?: return

        videoRecyclerView = findViewById(R.id.videoListRecyclerView)
        videoRecyclerView.layoutManager = LinearLayoutManager(this)

        val videoPaths = getVideosInFolder(this,folder.folderPath)
        val videoAdapter = videoAdapter(videoPaths)
        videoRecyclerView.adapter = videoAdapter
    }

    fun getVideosInFolder(context: Context, folderPath: String): List<String> {
        val videoPaths = mutableListOf<String>()
        val contentResolver: ContentResolver = context.contentResolver

        val projection = arrayOf(
            MediaStore.Video.Media.DATA
        )

        val selection = "${MediaStore.Video.Media.DATA} like ?"
        val selectionArgs = arrayOf("$folderPath%") // Filter videos in the specified folder

        val videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor? = contentResolver.query(
            videoUri,
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use { c ->
            val dataColumn = c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)

            while (c.moveToNext()) {
                val videoPath = c.getString(dataColumn)
                videoPaths.add(videoPath)
            }
        }

        cursor?.close()

        return videoPaths
    }
}