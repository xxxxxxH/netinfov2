package net.basicmodel.ui.fragment

import android.view.View
import com.bumptech.glide.Glide
import com.xxxxxxh.mailv2.utils.Constant
import kotlinx.android.synthetic.main.layout_fragment_net.*
import net.basicmodel.R
import net.basicmodel.base.BaseFragment
import net.basicmodel.utils.OnOptionClickListener
import net.basicmodel.widget.EditViewContainer

class NetFragment : BaseFragment() ,OnOptionClickListener{
    override fun initView() {
        initContainer()
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_fragment_net
    }

    private fun initContainer() {
        for (index in 0 until layoutContainer.childCount) {
            val container = layoutContainer.getChildAt(index)
            if (container is EditViewContainer) {
                val input = container.getInputView()
                input.setHint(Constant.hint[index])
                val imageView = container.getOptionView()
                when (index) {
                    0 -> {
                        imageView.visibility = View.VISIBLE
                        imageView.setImageResource(R.mipmap.arrow_down)
                        container.setListener(this)
                        container.setTag("name")
                    }
                    1 -> {
                        imageView.visibility = View.VISIBLE
                        imageView.setImageResource(R.mipmap.activity_main_refresh_icon)
                        container.setListener(this)
                        container.setTag("refresh")
                        val edit = container.getInputView().getEditTextView()
                        edit.setText("100 , 100")
                        edit.isEnabled = false
                    }
                    else -> {
                        imageView.visibility = View.GONE
                    }
                }

            }
        }
    }

    override fun onClick(tag: String) {

    }
}