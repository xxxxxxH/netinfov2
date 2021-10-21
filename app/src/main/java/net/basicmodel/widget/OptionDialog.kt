package net.basicmodel.widget

import android.content.Context
import android.view.View
import com.flyco.animation.Attention.Swing
import com.flyco.dialog.widget.base.BaseDialog
import kotlinx.android.synthetic.main.layout_pop.*
import net.basicmodel.R
import net.basicmodel.utils.OptionClickListener

class OptionDialog(context: Context) : BaseDialog<OptionDialog>(context) {

    var listener: OptionClickListener? = null

    override fun onCreateView(): View {
        widthScale(0.85f)
        showAnim(Swing())
        return View.inflate(context, R.layout.layout_pop, null)
    }

    override fun setUiBeforShow() {
        filed.setOnClickListener {
            listener?.OptionClick(0)
        }
        add.setOnClickListener {
            listener?.OptionClick(1)
        }
        delete.setOnClickListener {
            listener?.OptionClick(2)
        }
        save.setOnClickListener {
            listener?.OptionClick(3)
        }
        submit.setOnClickListener {
            listener?.OptionClick(4)
        }
    }
}