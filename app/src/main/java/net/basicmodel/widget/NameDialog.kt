package net.basicmodel.widget

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.flyco.dialog.widget.base.BaseDialog
import kotlinx.android.synthetic.main.layout_dialog_name.*
import net.basicmodel.R
import net.basicmodel.adapter.NameAdapter
import net.basicmodel.event.MessageEvent
import org.greenrobot.eventbus.EventBus

class NameDialog(context: Context, val data: ArrayList<String>) : BaseDialog<NameDialog>(context) {
    override fun onCreateView(): View {
        widthScale(0.85f)
        return View.inflate(context, R.layout.layout_dialog_name, null)
    }

    override fun setUiBeforShow() {
        val nameAdapter = NameAdapter(R.layout.layout_item_room, data)
        dialog_recycler.layoutManager = LinearLayoutManager(context)
        dialog_recycler.adapter = nameAdapter
        nameAdapter.setOnItemClickListener { adapter, view, position ->
            EventBus.getDefault().post(MessageEvent("nameSelect", adapter.data[position] as String))
            dismiss()
        }
    }
}