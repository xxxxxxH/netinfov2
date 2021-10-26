package net.basicmodel.ui.fragment

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.text.TextUtils
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.ToastUtils
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.layout_fragment_scram.*
import net.basicmodel.R
import net.basicmodel.adapter.ImageAdapter
import net.basicmodel.base.BaseFragment
import net.basicmodel.entity.CustomFiledEntity
import net.basicmodel.entity.ScramEntityNew
import net.basicmodel.event.MessageEvent
import net.basicmodel.ui.activity.MainActivity
import net.basicmodel.utils.*
import net.basicmodel.widget.CustomDialog
import net.basicmodel.widget.NameDialog
import net.basicmodel.widget.SaveDialog
import net.basicmodel.widget.SelectDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.collections.ArrayList

class ScramFragment : BaseFragment(), LocationListener, OnOptionClickListener, PhotoCallBack,
    TimeSelectListener {
    var imgAdapter: ImageAdapter? = null
    override fun initView() {
        EventBus.getDefault().register(this)
        activity?.let { MyLocationManager.get().getLocation(it, this) }
        initContainer()
        initClick()
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_fragment_scram
    }

    private fun initContainer() {
        scramId.getInputView().setHint("加扰ID")
        scramId.getInputView().getEditTextView().isEnabled = false
        scramId.getOptionView().setImageResource(R.mipmap.arrow_down)
        scramId.setTag("ID")
        scramId.setListener(this)
        childName.setHint("子网名")
        startTime.getInputView().setHint("加扰开始时间")
        startTime.setTag("time")
        startTime.setListener(this)
        MyLocationManager.get().formatDate(Date())?.let {
            startTime.getInputView().setEditTextContent(it)
            endTime.getInputView().setEditTextContent(it)
        }
        endTime.getInputView().setHint("加扰结束时间")
        startTime.getOptionView().setImageResource(R.mipmap.activity_main_refresh_icon)
        rate.setHint("加扰频率")
        code.setHint("加扰码型")
        scramLocation.getInputView().setHint("经纬度")
        scramLocation.getOptionView().setImageResource(R.mipmap.activity_main_refresh_icon)
        scramLocation.setTag("scramLocation")
        scramLocation.setListener(this)

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initClick() {

        startTime.getInputView().getEditTextView().setOnTouchListener { v, event ->
            event?.let {
                if (it.action == MotionEvent.ACTION_DOWN) {
                    TimePickerManager.get().createTimePicker(
                        requireActivity(),
                        "start",
                        this@ScramFragment
                    ).show()
                }
            }
            false
        }

        endTime.getInputView().getEditTextView().setOnTouchListener { v, event ->
            event?.let {
                if (it.action == MotionEvent.ACTION_DOWN) {
                    TimePickerManager.get().createTimePicker(
                        requireActivity(),
                        "endTime",
                        this@ScramFragment
                    ).show()
                }
            }
            false
        }

        img_add.setOnClickListener {
            val d = SelectDialog(requireActivity(), requireActivity(), this)
            d.show()
        }
    }

    private fun setImgAdapter(data: ArrayList<String>?) {
        imgAdapter = ImageAdapter(R.layout.layout_item_img, data)
        img_recycler.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        img_recycler.adapter = imgAdapter
        imgAdapter!!.addChildClickViewIds(R.id.item_del)
        imgAdapter!!.setOnItemChildClickListener { adapter, view, position ->
            adapter.removeAt(position)
        }
    }

    fun getData(): ScramEntityNew? {
        val idStr = scramId.getInputView().getEditTextContent()
        if (TextUtils.isEmpty(idStr)) return null
        val childNameStr = childName.getEditTextContent()
        val startTimeStr = startTime.getInputView().getEditTextContent()
        val endTimeStr = endTime.getInputView().getEditTextContent()
        val rateStr = rate.getEditTextContent()
        val codeStr = code.getEditTextContent()
        val locationStr = scramLocation.getInputView().getEditTextContent()
        val imgData: ArrayList<String>? =
            if (imgAdapter == null) null else imgAdapter!!.data as ArrayList<String>
        val customData: ArrayList<CustomFiledEntity>? =
            if (customRoot.childCount == 0) null else CustomFiledManager.get()
                .getCustomData(customRoot)
        return ScramEntityNew(
            idStr,
            childNameStr,
            startTimeStr,
            endTimeStr,
            rateStr,
            codeStr,
            locationStr,
            imgData,
            customData
        )
    }

    fun setData(entityNew: ScramEntityNew) {
        scramId.getInputView().setEditTextContent(entityNew.id)
        childName.setEditTextContent(entityNew.childName)
        startTime.getInputView().setEditTextContent(entityNew.startTime)
        endTime.getInputView().setEditTextContent(entityNew.endTime)
        rate.setEditTextContent(entityNew.rate)
        code.setEditTextContent(entityNew.code)
        scramLocation.getInputView().setEditTextContent(entityNew.location)
        setImgAdapter(entityNew.imgData)
        entityNew.customData?.let {
            CustomFiledManager.get().removeCustomItem(customRoot)
            for (item in it) {
                CustomFiledManager.get().addCustomItem(customRoot, item)
            }
        }
    }

    fun clear() {
        scramId.getInputView().setEditTextContent("")
        childName.setEditTextContent("")
        rate.setEditTextContent("")
        code.setEditTextContent("")
        setImgAdapter(null)
        CustomFiledManager.get().removeCustomItem(customRoot)
    }

    override fun onLocationChanged(location: Location) {
        (activity as MainActivity).closeDlg()
        val lot = MyLocationManager.get().formatDouble(location.longitude)
        val lat = MyLocationManager.get().formatDouble(location.latitude)
        scramLocation.getInputView().setEditTextContent(
            "${lot},${lat}"
        )
        if (TextUtils.isEmpty(scramId.getInputView().getEditTextContent())) {
            scramId.getInputView().setEditTextContent(
                "${System.currentTimeMillis()}${lot!!.replace(".", "")}${lat!!.replace(".", "")}"
            )
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

    override fun onClick(vararg tag: String) {
        when (tag[0]) {
            "ID" -> {
                val data = MMKVUtils.getKeys("scram")
                if (data == null || data.size == 0) {
                    ToastUtils.s(activity, "暂无数据")
                } else {
                    val d = activity?.let { NameDialog(it, data, 1) }
                    d!!.show()
                }
            }
            "scramLocation" -> {
                (activity as MainActivity).showDlg()
                scramId.getInputView().setEditTextContent("")
            }
            "customOk" -> {
                val entity = CustomFiledEntity()
                entity.name = tag[1]
                entity.content = tag[2]
                CustomFiledManager.get().addCustomItem(customRoot, entity)
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun timeSelect(flag: String, time: String) {
        when (flag) {
            "start" -> {
                startTime.getInputView().setEditTextContent(time)
                startTime.getInputView().getEditTextView().clearFocus()
            }
            "end" -> endTime.getInputView().setEditTextContent(time)
        }
    }

    override fun dialogDismiss() {
        startTime.getInputView().getEditTextView().clearFocus()
    }

    override fun onPause() {
        super.onPause()
        FocusManager.get().clearFocus(scramRoot)
        FocusManager.get().clearFocus(customRoot)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MessageEvent) {
        val msg = event.getMessage()
        when (msg[0] as String) {
            "add1" -> {
                val d = activity?.let { SaveDialog(it, 1) }
                d!!.show()
            }
            "delete1" -> {
                MMKV.defaultMMKV()!!.remove(scramId.getInputView().getEditTextContent())
                MMKVUtils.deleteKey(scramId.getInputView().getEditTextContent(), "scram")
                clear()
            }
            "save1" -> {
                val s = scramId.getInputView().getEditTextContent()
                if (!TextUtils.isEmpty(s)) {
                    MMKVUtils.saveKeys("scram", s)
                    MMKV.defaultMMKV()!!.encode(s, getData())
                }
            }
            "submit1" -> {

            }
            "filed1" -> {
                val d = activity?.let { CustomDialog(it) }
                d!!.listener = this
                d.show()
            }
            "nameSelect" -> {
                if (msg[1] == 1) {
                    val data = MMKV.defaultMMKV()!!
                        .decodeParcelable(msg[2] as String, ScramEntityNew::class.java)
                    data?.let {
                        setData(it)
                    }
                }
            }
            "saveCurrentData" -> {
                if (msg[1] == 1) {
                    val s = scramId.getInputView().getEditTextContent()
                    if (!TextUtils.isEmpty(s)) {
                        MMKVUtils.saveKeys("scram", s)
                        MMKV.defaultMMKV()!!.encode(s, getData())
                        clear()
                    }
                }
            }
        }
    }


}