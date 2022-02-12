package net.basicmodel.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils
import android.widget.Toast

object ClipUtils {
    fun copy(context: Context,content:String){
        val cmb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cmb.setPrimaryClip(ClipData.newPlainText(null,content))
        Toast.makeText(context,"复制路径成功",Toast.LENGTH_SHORT).show()
    }
}