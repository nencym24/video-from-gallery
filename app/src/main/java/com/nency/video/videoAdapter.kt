package com.nency.video

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView

class videoAdapter (private val videoPaths: List<String>) : RecyclerView.Adapter<videoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val videoPath = videoPaths[position]
        holder.bind(videoPath)
    }

    override fun getItemCount(): Int {
        return videoPaths.size
    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val videoView: VideoView = itemView.findViewById(R.id.imgList)

        fun bind(videoPath: String) {
            videoView.setVideoURI(Uri.parse(videoPath))
            videoView.start()
        }
    }

}