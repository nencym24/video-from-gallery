package com.nency.video.gallary

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nency.video.R

class ImageActivity : AppCompatActivity() {

    private lateinit var imageFoldersAdapter: ImageFoldersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val recyclerView = findViewById<RecyclerView>(R.id.rcvImages)
        recyclerView.layoutManager = GridLayoutManager(this,3, GridLayoutManager.VERTICAL,false)
        imageFoldersAdapter = ImageFoldersAdapter()
        recyclerView.adapter = imageFoldersAdapter

        if (hasReadStoragePermission()) {
            loadImageFoldersWithCovers()
        } else {
            requestStoragePermission()
        }
//        val selectedFolderPath = "folderName"
//        val intent = Intent(this, PhotoViewerActivity::class.java)
//        intent.putExtra("folderPath", selectedFolderPath)
//        startActivity(intent)
    }

    private fun loadImageFoldersWithCovers() {
        val imageFolders = getImageFoldersWithCoversAndCounts(this)
        imageFoldersAdapter.setImageFolders(imageFolders)
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
                loadImageFoldersWithCovers()
            } else {
                Log.e(TAG, "Permission denied")
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val STORAGE_PERMISSION_CODE = 101
    }

    fun getImageFoldersWithCoversAndCounts(context: Context): List<ImageFolder> {
        val imageFolders = mutableListOf<ImageFolder>()
        val contentResolver: ContentResolver = context.contentResolver
        val projection = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )

        val imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor = contentResolver.query(
            imageUri,
            projection,
            null,
            null,
            null
        )

        cursor?.use { c ->
            val dataColumn = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val folderNameColumn = c.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

            val folderMap = mutableMapOf<String, ImageFolder>()

            while (c.moveToNext()) {
                val folderName = c.getString(folderNameColumn)
                val imagePath = c.getString(dataColumn)

                val folder = folderMap[folderName]
                if (folder == null) {
                    folderMap[folderName] = ImageFolder(folderName, "", imagePath, 1)
                } else {
                    folder.imageCount++
                }
            }

            imageFolders.addAll(folderMap.values)
        }

        cursor?.close()

        return imageFolders
    }

}

