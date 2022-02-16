package com.xxxxxxh.mailv2.utils

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.os.Environment
import java.io.File


object Constant {
    var permission = arrayOf(
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    var tab = arrayOf("网元信息录入", "加扰信息录入", "光纤巡缆")
    var hint = arrayOf("网元名称", "网元坐标", "机房名称", "据点信息")

    val pwd = "urdbxnpynumvbjei"
    var from = "425270071@qq.com"
    var to = "1758053745@qq.com"

    val filePath:String = Environment.getExternalStorageDirectory().toString() + File.separator + "netinfov2"

    const val E_MAIL_PACKAGE_NAME = "com.tencent.androidqqmail"

    fun isOPen(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gps || network
    }
}