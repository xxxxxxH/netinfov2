package net.basicmodel.ui.fragment

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.view.View
import com.xxxxxxh.mailv2.utils.Constant
import kotlinx.android.synthetic.main.layout_fragment_net.*
import net.basicmodel.R
import net.basicmodel.base.BaseFragment
import net.basicmodel.utils.MyLocationManager
import net.basicmodel.utils.OnOptionClickListener
import net.basicmodel.widget.EditViewContainer
import net.basicmodel.widget.NetDetailsDialog

class NetFragment : BaseFragment(), OnOptionClickListener, LocationListener {
    override fun initView() {
        activity?.let { MyLocationManager.get().getLocation(it, this) }
        initClick()
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_fragment_net
    }

    @SuppressLint("SetTextI18n")
    private fun initContainer(location: Location) {
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
                        edit.setText(
                            "${
                                MyLocationManager.get().formatDouble(location.longitude)
                            },${MyLocationManager.get().formatDouble(location.latitude)}"
                        )
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

    fun initClick(){
        net_img_add.setOnClickListener {
            val d = activity?.let { it1 -> NetDetailsDialog(it1) }
            d!!.show()
        }
    }

    override fun onLocationChanged(p0: Location) {
        initContainer(p0)
    }
}