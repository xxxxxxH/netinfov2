package net.basicmodel.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TourEntityNew(
    var id: String = "",
    var tourCode: String = "",
    var tourPoint: String = "",
    var tourLocation: String = "",
    var imgData: ArrayList<String>?,
    var customData: ArrayList<CustomFiledEntity>?
) : Parcelable
