package com.nency.video

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VideoFoldersAdapter(private val folderClickListener: (VideoFolder) -> Unit) : RecyclerView.Adapter<VideoFoldersAdapter.VideoFolderViewHolder>() {

    private val videoFolders = mutableListOf<VideoFolder>()

    fun setVideoFolders(folders: List<VideoFolder>) {
        videoFolders.clear()
        videoFolders.addAll(folders)
        notifyDataSetChanged()
    }

    inner class VideoFolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val folderNameTextView: TextView = itemView.findViewById(R.id.txtName)
        private val videoCountTextView: TextView = itemView.findViewById(R.id.txtfolder)

        fun bind(folder: VideoFolder) {
            folderNameTextView.text = folder.folderName
            videoCountTextView.text = "${folder.videoCount} videos"

            itemView.setOnClickListener {
                folderClickListener.invoke(folder)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideoFoldersAdapter.VideoFolderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_item, parent, false)
        return VideoFolderViewHolder(view)    }

    override fun getItemCount(): Int {
        return videoFolders.size
    }

    override fun onBindViewHolder(
        holder: VideoFoldersAdapter.VideoFolderViewHolder,
        position: Int
    ) {
        val folder = videoFolders[position]
        holder.bind(folder)
    }

}

