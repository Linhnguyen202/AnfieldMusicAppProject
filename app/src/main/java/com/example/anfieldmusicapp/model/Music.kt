package com.example.anfieldmusicapp.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Music(
    val __v: Int? = null,
    val _id: String?  = null,
//    val category: String?  = null,
//    val createdAt: String?  = null,
//    val favorite: Double?  = null,
    val account_favorite: List<AccountFavor>?  = null,
    val id_account: String?  = null,
    val image_music: String?  = null,
    val link_mv: String?  = null,
    val name_music: String?  = null,
    val name_singer: String? = null,
    val seconds: Double?  = null,
    val slug_category: String? = null,
    val slug_name_music: String? = null,
    val slug_name_singer: String? = null,
    val slug_subscribe: String? = null,
    val src_music: String? = null,
    val subscribe: String? = null,
    val sum_comment: String? = null,
    val time_format: String? = null,
    val updatedAt: String? = null,
    val view: Int? = null
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readDouble(),
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
        parcel.writeInt(__v?:1)
        parcel.writeString(_id)
//        parcel.writeString(category)
//        parcel.writeString(createdAt)
//        parcel.writeDouble(favorite!!)
        arrayListOf<AccountFavor>().apply {
            parcel.writeList(this)
        }
        parcel.writeString(id_account)
        parcel.writeString(image_music)
        parcel.writeString(link_mv)
        parcel.writeString(name_music)
        parcel.writeString(name_singer)
        parcel.writeDouble(seconds!!)
        parcel.writeString(slug_category)
        parcel.writeString(slug_name_music)
        parcel.writeString(slug_name_singer)
        parcel.writeString(slug_subscribe)
        parcel.writeString(src_music)
        parcel.writeString(subscribe)
        parcel.writeString(sum_comment)
        parcel.writeString(time_format)
        parcel.writeString(updatedAt)
        parcel.writeInt(view!!)
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
