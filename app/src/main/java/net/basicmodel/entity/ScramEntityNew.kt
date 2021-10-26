package net.basicmodel.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Copyright (C) 2021,2021/10/26, a Tencent company. All rights reserved.
 *
 * User : v_xhangxie
 *
 * Desc :
 */
@Parcelize
data class ScramEntityNew(
    var id: String = "",
    var childName: String = "",
    var startTime: String = "",
    var endTime: String = "",
    var rate: String = "",
    var code: String = "",
    var location: String = "",
    var imgData: ArrayList<String>?,
    var customData: ArrayList<CustomFiledEntity>?

) : Parcelable
