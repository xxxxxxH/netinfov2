package net.basicmodel.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import net.basicmodel.R

class ResourceManager {


    companion object {
        private var i: ResourceManager? = null
            get() {
                field ?: run {
                    field = ResourceManager()
                }
                return field
            }

        @Synchronized
        fun get(): ResourceManager {
            return i!!
        }
    }

    fun initTipsData(): ArrayList<String> {
        val tips = ArrayList<String>()
        tips.add("新增")
        tips.add("删除")
        tips.add("保存")
        tips.add("提交")
        return tips
    }

    fun initImagesData(): ArrayList<Int> {
        val images: ArrayList<Int> = ArrayList()
        images.add(0)
        images.add(0)
        images.add(0)
        images.add(0)
        return images
    }

    fun res2String(context: Context, id: Int): String {
        val r = context.resources
        val uri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + r.getResourcePackageName(id) + "/"
                    + r.getResourceTypeName(id) + "/"
                    + r.getResourceEntryName(id)
        )
        return uri.toString()
    }

}