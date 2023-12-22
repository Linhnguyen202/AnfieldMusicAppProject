package com.example.anfieldmusicapp.model

import android.os.Parcel
import android.os.Parcelable

data class Music(
    val __v: Int,
    val _id: String?,
    val category: String?,
    val createdAt: String?,
    val favorite: Double,
    val account_favorite: List<AccountFavor>,
    val id_account: String?,
    val image_music: String?,
    val link_mv: String?,
    val name_music: String?,
    val name_singer: String?,
    val seconds: Double,
    val slug_category: String?,
    val slug_name_music: String?,
    val slug_name_singer: String?,
    val slug_subscribe: String?,
    val src_music: String?,
    val subscribe: String?,
    val sum_comment: String?,
    val time_format: String?,
    val updatedAt: String?,
    val view: Int
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        arrayListOf<AccountFavor>().apply {
            parcel.readList(this, AccountFavor::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(__v)
        parcel.writeString(_id)
        parcel.writeString(category)
        parcel.writeString(createdAt)
        parcel.writeDouble(favorite)
        arrayListOf<AccountFavor>().apply {
            parcel.writeList(this)
        }
        parcel.writeString(id_account)
        parcel.writeString(image_music)
        parcel.writeString(link_mv)
        parcel.writeString(name_music)
        parcel.writeString(name_singer)
        parcel.writeDouble(seconds)
        parcel.writeString(slug_category)
        parcel.writeString(slug_name_music)
        parcel.writeString(slug_name_singer)
        parcel.writeString(slug_subscribe)
        parcel.writeString(src_music)
        parcel.writeString(subscribe)
        parcel.writeString(sum_comment)
        parcel.writeString(time_format)
        parcel.writeString(updatedAt)
        parcel.writeInt(view)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Music> {
        override fun createFromParcel(parcel: Parcel): Music {
            return Music(parcel)
        }

        override fun newArray(size: Int): Array<Music?> {
            return arrayOfNulls(size)
        }
    }
}
