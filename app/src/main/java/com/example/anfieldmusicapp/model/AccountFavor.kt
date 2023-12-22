package com.example.anfieldmusicapp.model

import android.os.Parcel
import android.os.Parcelable

data class AccountFavor(
    val _id: String?,
    val createdAt: String?,
    val id_music: String?,
    val image: String?,
    val role: Int,
    val user_name: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(createdAt)
        parcel.writeString(id_music)
        parcel.writeString(image)
        parcel.writeInt(role)
        parcel.writeString(user_name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AccountFavor> {
        override fun createFromParcel(parcel: Parcel): AccountFavor {
            return AccountFavor(parcel)
        }

        override fun newArray(size: Int): Array<AccountFavor?> {
            return arrayOfNulls(size)
        }
    }
}
