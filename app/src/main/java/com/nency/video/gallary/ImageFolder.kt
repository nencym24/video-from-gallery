package com.nency.video.gallary

data class  ImageFolder(val folderName: String,
                       val folderPath: String,
                       val coverImagePath: String? = null,
                       var imageCount: Int = 0)
