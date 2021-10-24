package net.basicmodel.utils

import net.basicmodel.entity.NetDetailsEntity

class DataHandleManager {
    companion object {
        private var i: DataHandleManager? = null
            get() {
                field ?: run {
                    field = DataHandleManager()
                }
                return field
            }

        @Synchronized
        fun get(): DataHandleManager {
            return i!!
        }
    }

    fun removeDuplicate(list: ArrayList<NetDetailsEntity>): ArrayList<NetDetailsEntity> {
        val tempList = ArrayList<NetDetailsEntity>(list.size)
        for (i in 0 until list.size) {
            if (!tempList.contains(list[i])) tempList.add(list[i])
        }
        return tempList
    }
}