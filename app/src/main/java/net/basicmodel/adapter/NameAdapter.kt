package net.basicmodel.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import net.basicmodel.R

class NameAdapter(layoutResId: Int, data: ArrayList<String>?) :
    BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.item_room_name, item)
    }
}