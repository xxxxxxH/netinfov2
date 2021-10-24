package net.basicmodel.utils

import android.view.ViewGroup
import net.basicmodel.entity.CustomFiledEntity
import net.basicmodel.widget.InputEditText

class CustomFiledManager {
    companion object {
        private var i: CustomFiledManager? = null
            get() {
                field ?: run {
                    field = CustomFiledManager()
                }
                return field
            }

        @Synchronized
        fun get(): CustomFiledManager {
            return i!!
        }
    }


    fun addCustomItem(parent: ViewGroup, entity: CustomFiledEntity) {
        val customItem = InputEditText(parent.context)
        customItem.setMargin(ScreenUtils.get().dip2px(parent.context, 20f), 0, 0, 0)
        customItem.setHint(entity.name)
        customItem.setEditTextContent(entity.content)
        parent.addView(customItem)
        parent.invalidate()
    }

    fun removeCustomItem(parent: ViewGroup) {
        parent.removeAllViews()
    }
}