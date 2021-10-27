package net.basicmodel.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.text.TextUtils
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.ToastUtils
import com.tencent.mmkv.MMKV
import com.xxxxxxh.mailv2.utils.Constant
import kotlinx.android.synthetic.main.layout_fragment_tour.*
import net.basicmodel.R
import net.basicmodel.adapter.ImageAdapter
import net.basicmodel.base.BaseFragment
import net.basicmodel.entity.CustomFiledEntity
import net.basicmodel.entity.TourEntityNew
import net.basicmodel.event.MessageEvent
import net.basicmodel.sendmail.EmailUtil
import net.basicmodel.sendmail.UsefulSTMP
import net.basicmodel.ui.activity.MainActivity
import net.basicmodel.ui.activity.MapActivity
import net.basicmodel.utils.*
import net.basicmodel.widget.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class TourFragment : BaseFragment(), OnOptionClickListener, LocationListener, PhotoCallBack {
    var imgAdapter: ImageAdapter? = null
    override fun initView() {
        EventBus.getDefault().register(this)
        activity?.let { MyLocationManager.get().getLocation(it, this) }
        initContainer()
        initClick()
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_fragment_tour
    }

    private fun initContainer() {
        tourID.getInputView().setHint("ID")
        tourID.getOptionView().setImageResource(R.mipmap.arrow_down)
        tourID.setTag("tourDrop")
        tourID.setListener(this)
        tourCode.setHint("桩号")
        tourPoint.setHint("站点")
        tourLocation.getInputView().setHint("经纬度")
        tourLocation.getOptionView().setImageResource(R.mipmap.activity_main_refresh_icon)
        tourLocation.setTag("tourRefresh")
        tourLocation.setListener(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initClick() {
        img_add.setOnClickListener {
            SelectDialog(requireActivity(), requireActivity(), this).show()
        }
        tourLocation.getInputView().getEditTextView().setOnTouchListener { p0, p1 ->
            p1?.let {
                if (it.action == MotionEvent.ACTION_DOWN) {
                    val i = Intent(activity, MapActivity::class.java)
                    i.putExtra("index", 2)
                    startActivity(i)
                }
            }
            false
        }
    }

    override fun onClick(vararg tag: String) {
        when (tag[0]) {
            "tourDrop" -> {
                val data = MMKVUtils.getKeys("tour")
                if (data == null || data.size == 0) {
                    ToastUtils.s(activity, "暂无数据")
                } else {
                    val d = activity?.let { NameDialog(it, data, 2) }
                    d!!.show()
                }
            }
            "tourRefresh" -> {
                activity?.let { LoadingDialogManager.get().show(it) }
                tourLocation.getInputView().setEditTextContent("")
            }
            "customOk" -> {
                val entity = CustomFiledEntity()
                entity.name = tag[1]
                entity.content = tag[2]
                CustomFiledManager.get().addCustomItem(tourCustomRoot, entity)
            }
        }
    }

    override fun onLocationChanged(p0: Location) {
        LoadingDialogManager.get().close()
        val lot = MyLocationManager.get().formatDouble(p0.longitude)
        val lat = MyLocationManager.get().formatDouble(p0.latitude)
        tourLocation.getInputView().setEditTextContent(
            "${lot},${lat}"
        )
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

    fun getData(): TourEntityNew? {
        val idStr = tourID.getInputView().getEditTextContent()
        if (TextUtils.isEmpty(idStr)) return null
        val codeStr = tourCode.getEditTextContent()
        val pointStr = tourPoint.getEditTextContent()
        val locationStr = tourLocation.getInputView().getEditTextContent()
        val imgData: ArrayList<String>? =
            if (imgAdapter == null) null else imgAdapter!!.data as ArrayList<String>
        val customData: ArrayList<CustomFiledEntity>? =
            if (tourCustomRoot.childCount == 0) null else CustomFiledManager.get()
                .getCustomData(tourCustomRoot)
        return TourEntityNew(idStr, codeStr, pointStr, locationStr, imgData, customData)
    }

    fun setData(entityNew: TourEntityNew) {
        tourID.getInputView().setEditTextContent(entityNew.id)
        tourCode.setEditTextContent(entityNew.tourCode)
        tourPoint.setEditTextContent(entityNew.tourPoint)
        tourLocation.getInputView().setEditTextContent(entityNew.tourLocation)
        setImgAdapter(entityNew.imgData)
        entityNew.customData?.let {
            CustomFiledManager.get().removeCustomItem(tourCustomRoot)
            for (item in it) {
                CustomFiledManager.get().addCustomItem(tourCustomRoot, item)
            }
        }
    }

    fun clear() {
        tourID.getInputView().setEditTextContent("")
        tourCode.setEditTextContent("")
        tourPoint.setEditTextContent("")
        setImgAdapter(null)
        CustomFiledManager.get().removeCustomItem(tourCustomRoot)
    }

    override fun onPause() {
        super.onPause()
        FocusManager.get().clearFocus(tourCustomRoot)
        FocusManager.get().clearFocus(tourCustomRoot)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MessageEvent) {
        val msg = event.getMessage()
        when (msg[0] as String) {
            "filed2" -> {
                val d = activity?.let { CustomDialog(it) }
                d!!.listener = this
                d.show()
            }
            "add2" -> {
                val d = activity?.let { SaveDialog(it, 2) }
                d!!.show()
            }
            "delete2" -> {
                MMKV.defaultMMKV()!!.remove(tourID.getInputView().getEditTextContent())
                MMKVUtils.deleteKey(tourID.getInputView().getEditTextContent(), "tour")
                clear()
            }
            "save2" -> {
                val s = tourID.getInputView().getEditTextContent()
                if (!TextUtils.isEmpty(s)) {
                    MMKVUtils.saveKeys("tour", s)
                    MMKV.defaultMMKV()!!.encode(s, getData())
                }
            }
            "submit2" -> {
                AddressDialog(requireActivity(), 2).show()
            }
            "send" -> {
                activity?.let { LoadingDialogManager.get().show(it) }
                val data = DataHandleManager.get().handleData2(
                    DataHandleManager.get()
                        .getCurAllData2(tourID.getInputView().getEditTextContent(), getData()!!)
                )

                Thread {
                    val result = EmailUtil.autoSendMail(
                        (activity as MainActivity).getThemeText(2),
                        data.toString(),
                        msg[1] as String,
                        UsefulSTMP.QQ,
                        Constant.from,
                        Constant.pwd,
                        null
                    )
                    EventBus.getDefault().post(MessageEvent("result", result, 2))
                }.start()
            }
            "result" -> {
                LoadingDialogManager.get().close()
                if (msg[1] as Boolean && msg[2] == 2) {
                    DataHandleManager.get().deleteData("tour")
                    clear()
                    ToastUtils.s(activity, "发送成功")
                } else {
                    ToastUtils.s(activity, "发送失败")
                }
            }
            "nameSelect" -> {
                if (msg[1] == 2) {
                    val data = MMKV.defaultMMKV()!!
                        .decodeParcelable(msg[2] as String, TourEntityNew::class.java)
                    data?.let {
                        setData(it)
                    }
                }
            }
            "saveCurrentData" -> {
                val s = tourID.getInputView().getEditTextContent()
                if (!TextUtils.isEmpty(s)) {
                    MMKVUtils.saveKeys("tour", s)
                    MMKV.defaultMMKV()!!.encode(s, getData())
                    clear()
                }
            }
            "map" -> {
                if (msg[1] == 2) {
                    tourLocation.getInputView().setEditTextContent("${msg[2]},${msg[3]}")
                }
            }
        }
    }


}