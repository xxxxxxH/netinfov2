package net.basicmodel.base

import android.app.Application
import com.luck.picture.lib.tools.ToastUtils
import com.tencent.mmkv.MMKV

class MailApplication :Application(){
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
    }
}