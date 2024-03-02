package com.nency.video

import android.os.Parcel
import android.os.Parcelable

data class VideoFolder(val folderName: String,
                       val folderPath: String,
                       var videoCount: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(folderName)
        parcel.writeString(folderPath)
        parcel.writeInt(videoCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VideoFolder> {
        override fun createFromParcel(parcel: Parcel): VideoFolder {
            return VideoFolder(parcel)
        }

        override fun newArray(size: Int): Array<VideoFolder?> {
            return arrayOfNulls(size)
        }
    }
}
