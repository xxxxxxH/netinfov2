package net.basicmodel.ui.activity

import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.xxxxxxh.mailv2.utils.Constant
import kotlinx.android.synthetic.main.activity_main.*
import net.basicmodel.R
import net.basicmodel.base.BaseActivity
import net.basicmodel.event.MessageEvent
import net.basicmodel.ui.fragment.NetFragment
import net.basicmodel.ui.fragment.ScramFragment
import net.basicmodel.ui.fragment.TourFragment
import net.basicmodel.utils.KeyboardManager
import net.basicmodel.utils.OptionClickListener
import net.basicmodel.widget.OptionDialog
import org.greenrobot.eventbus.EventBus

class MainActivity : BaseActivity(), OnPermissionCallback, ViewPager.OnPageChangeListener,OptionClickListener {

    var netFragment:NetFragment?=null
    var scramFragment:ScramFragment?=null
    var tourFragment:TourFragment?=null

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        requestPermissions()
    }

    private fun requestPermissions() {
        XXPermissions.with(this).permission(Constant.permission).request(this)
    }

    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
        initViewpager()
    }

    private fun initViewpager() {
        netFragment = NetFragment()
        scramFragment = ScramFragment()
        tourFragment = TourFragment()
        views.add(netFragment!!)
        views.add(scramFragment!!)
        views.add(tourFragment!!)
        tab.setViewPager(viewpager, Constant.tab, this, views)
        viewpager.setOnPageChangeListener(this)
        option.setOnClickListener {
            val p = OptionDialog(this)
            p.listener = this
            p.show()
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        KeyboardManager.get().hideKeyboard(this)
    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun OptionClick(index: Int) {
        when(index){
            0 ->{
                EventBus.getDefault().post(MessageEvent("filed"))
            }
            1 ->{

            }
            2 ->{

            }
            3 ->{

            }
            4 ->{

            }
        }
    }

}