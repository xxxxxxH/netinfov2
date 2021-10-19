package net.basicmodel.ui.activity

import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.xxxxxxh.mailv2.utils.Constant
import kotlinx.android.synthetic.main.activity_main.*
import net.basicmodel.R
import net.basicmodel.base.BaseActivity
import net.basicmodel.ui.fragment.NetFragment
import net.basicmodel.ui.fragment.ScramFragment
import net.basicmodel.ui.fragment.TourFragment

class MainActivity : BaseActivity(), OnPermissionCallback {

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
        views.add(NetFragment())
        views.add(ScramFragment())
        views.add(TourFragment())
        tab.setViewPager(viewpager, Constant.tab, this, views)
    }

}