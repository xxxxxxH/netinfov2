package com.xxxxxxh.mailv2.utils

import android.Manifest

object Constant {
    var permission = arrayOf(
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    var tab = arrayOf("网元信息录入", "加扰信息录入", "光纤巡缆")
}