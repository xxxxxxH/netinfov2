package net.basicmodel.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.luck.picture.lib.entity.LocalMedia
import com.xxxxxxh.mailv2.utils.Constant
import kotlinx.android.synthetic.main.layout_fragment_net.*
import net.basicmodel.R
import net.basicmodel.adapter.ImageAdapter
import net.basicmodel.adapter.NetInfoAdapter
import net.basicmodel.base.BaseFragment
import net.basicmodel.entity.CustomFiledEntity
import net.basicmodel.entity.NetDetailsEntity
import net.basicmodel.event.MessageEvent
import net.basicmodel.ui.activity.NetDetailsActivity
import net.basicmodel.utils.*
import net.basicmodel.widget.CustomDialog
import net.basicmodel.widget.EditViewContainer
import net.basicmodel.widget.NetDetailsDialog
import net.basicmodel.widget.SelectDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NetFragment : BaseFragment(), OnOptionClickListener, LocationListener, OnDialogClickListener,
    PhotoCallBack {
    var netInfoAdapter: NetInfoAdapter? = null
    var imgAdapter: ImageAdapter? = null
    var locationTv: EditText? = null
    var onItem: Boolean = false
    override fun initView() {
        EventBus.getDefault().register(this)
        activity?.let { MyLocationManager.get().getLocation(it, this) }
        initContainer()
        initClick()
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
                        locationTv = edit
                        edit.isEnabled = false
                    }
                    else -> {
                        imageView.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onClick(vararg tag: String) {
        val type = tag[0]
        when (type) {
            "customOk" -> {
                val entity = CustomFiledEntity()
                entity.name = tag[1]
                entity.content = tag[2]
                CustomFiledManager.get().addCustomItem(customRoot, entity)
            }
        }
    }

    private fun initClick() {
        net_img_add.setOnClickListener {
            val d = activity?.let { it1 -> NetDetailsDialog(it1) }
            d!!.listenter = this
            d.show()
        }
        img_add.setOnClickListener {
            val d = SelectDialog(requireActivity(), requireActivity(), this)
            d.show()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(p0: Location) {
        locationTv!!.setText(
            "${
                MyLocationManager.get().formatDouble(p0.longitude)
            },${MyLocationManager.get().formatDouble(p0.latitude)}"
        )
    }

    override fun confirmClick(content: String) {
        val i = Intent(activity, NetDetailsActivity::class.java)
        i.putExtra("isNew", true)
        i.putExtra("index", if (netInfoAdapter == null) 1 else setIndex() + 1)
        i.putExtra("count", content.toInt())
        requireActivity().startActivity(i)
    }

    private fun setNetInfoAdapter(data: ArrayList<NetDetailsEntity>) {
        netInfoAdapter = NetInfoAdapter(R.layout.layout_item_netinfo, data)
        net_recycler.layoutManager = LinearLayoutManager(activity)
        net_recycler.adapter = netInfoAdapter
        netInfoAdapter!!.addChildClickViewIds(R.id.netInfoIv)
        netInfoAdapter!!.setOnItemChildClickListener { adapter, view, position ->
            adapter.removeAt(position)
        }
        netInfoAdapter!!.setOnItemClickListener { adapter, view, position ->
            onItem = true
            val i = Intent(activity, NetDetailsActivity::class.java)
            i.putExtra("isNew", false)
            i.putExtra("data", adapter.data as ArrayList<NetDetailsEntity>)
            requireActivity().startActivity(i)
        }
    }

    private fun setIndex(): Int {
        val s =
            (netInfoAdapter!!.data[netInfoAdapter!!.itemCount - 1].netDetailsBoardName).toString()
        return if (TextUtils.isEmpty(s)) 0 else s.substring(s.length - 1).toInt()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MessageEvent) {
        val msg = event.getMessage()
        when (msg[0] as String) {
            "netInfo" -> {
                val data = msg[1] as ArrayList<NetDetailsEntity>
                if (netInfoAdapter == null) {
                    setNetInfoAdapter(data)
                } else {
                    netInfoAdapter!!.addData(data)
                }
            }
            "dataChanged" -> {
                netInfoAdapter!!.setNewInstance(msg[1] as ArrayList<NetDetailsEntity>)
            }
            "filed" -> {

                val d = activity?.let { CustomDialog(it) }
                d!!.listener = this
                d.show()
            }
        }
    }

    private fun setImgAdapter(data: ArrayList<String>) {
        imgAdapter = ImageAdapter(R.layout.layout_item_img, data)
        img_recycler.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        img_recycler.adapter = imgAdapter
        imgAdapter!!.addChildClickViewIds(R.id.item_del)
        imgAdapter!!.setOnItemChildClickListener { adapter, view, position ->
            adapter.removeAt(position)
        }
    }

    override fun gallery(data: ArrayList<LocalMedia>) {
        if (imgAdapter == null) {
            setImgAdapter(PhotoManager.get().handle(data))
        } else {
            imgAdapter!!.addData(PhotoManager.get().handle(data))
        }
    }

    override fun camera(path: String) {
        val data = ArrayList<String>()
        data.add(path)
        if (imgAdapter == null) {
            setImgAdapter(data)
        } else {
            imgAdapter!!.addData(data)
        }
    }

    fun getData() {
        val nameStr = name.getInputView().getEditTextContent()
        if (TextUtils.isEmpty(nameStr)) return
        val locationStr = locationTv?.text.toString()
        val roomStr = room.getInputView().getEditTextContent()
        val point = point.getInputView().getEditTextContent()
        val netInfo: ArrayList<NetDetailsEntity>? =
            if (netInfoAdapter == null) null else netInfoAdapter!!.data as ArrayList<NetDetailsEntity>
        val imgData: ArrayList<String>? =
            if (imgAdapter == null) null else imgAdapter!!.data as ArrayList<String>
    }

}