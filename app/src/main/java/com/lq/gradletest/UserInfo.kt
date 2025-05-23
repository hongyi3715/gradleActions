package com.lq.gradletest

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable


@SuppressLint("ParcelCreator")
data class UserInfo(
    var userName: String,
    var id: String,
    var account : Float
): Parcelable{
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
       dest.writeString(id)
        dest.writeString(userName)
        dest.writeFloat(account)
    }

}