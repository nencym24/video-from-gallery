package com.nency.video.gallary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nency.video.R

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private var imageList = mutableListOf<String>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageAdapter.ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageAdapter.ImageViewHolder, position: Int) {
        val imagePath = imageList[position]
        holder.bind(imagePath)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
    fun setImageList(images: List<String>) {
        imageList.clear()
        imageList.addAll(images)
        notifyDataSetChanged()
    }



    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageview)

        fun bind(imagePath: String) {
            Glide.with(itemView.context)
                .load(imagePath)
                .into(imageView)
        }
    }
}