package net.basicmodel.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tencent.mmkv.MMKV
import java.util.ArrayList

abstract class BaseActivity:AppCompatActivity() {
    var views: ArrayList<Fragment> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        MMKV.initialize(this)
        initView()
    }

    abstract fun getLayoutId():Int

    abstract fun initView()

}