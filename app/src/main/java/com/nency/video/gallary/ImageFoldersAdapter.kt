package com.nency.video.gallary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nency.video.R

class ImageFoldersAdapter : RecyclerView.Adapter<ImageFoldersAdapter.ImageFolderViewHolder>()  {
    private val imageFolders = mutableListOf<ImageFolder>()

    fun setImageFolders(folders: List<ImageFolder>) {
        imageFolders.clear()
        imageFolders.addAll(folders)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFolderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_item, parent, false)
        return ImageFolderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageFolders.size
    }

    override fun onBindViewHolder(holder: ImageFolderViewHolder, position: Int) {
        val folder = imageFolders[position]
        holder.bind(folder)
    }

    inner class ImageFolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val folderNameTextView: TextView = itemView.findViewById(R.id.txtFolder)
        private val coverImageView: ImageView = itemView.findViewById(R.id.imgImage)
        private val imageCountTextView: TextView = itemView.findViewById(R.id.txtStorage)

        fun bind(folder: ImageFolder) {
            folderNameTextView.text = folder.folderName
            imageCountTextView.text = "${folder.imageCount} images"
            folder.coverImagePath?.let { imagePath ->
                Glide.with(itemView.context)
                    .load(imagePath)
                    .into(coverImageView)
            }
        }
    }
}