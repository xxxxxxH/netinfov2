package net.basicmodel.ui.activity

import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.AMap.OnMapLoadedListener
import com.amap.api.maps2d.AMap.OnMarkerDragListener
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.*
import kotlinx.android.synthetic.main.layout_activity_map.*
import net.basicmodel.R
import net.basicmodel.event.MessageEvent
import net.basicmodel.utils.MyLocationManager
import org.greenrobot.eventbus.EventBus

class MapActivity : AppCompatActivity(), OnMapLoadedListener, OnMarkerDragListener,
    OnLocationChangedListener {
    var mMapView: MapView? = null
    var aMap: AMap? = null
    var myLocationStyle: MyLocationStyle? = null
    var markerOptions: MarkerOptions? = null
    var marker: Marker? = null
    var index = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_map)
        mMapView = findViewById(R.id.map)
        mMapView!!.onCreate(savedInstanceState)
        initMap()
        initView()
    }


    fun initView() {
        val i = intent
        index = i.getIntExtra("index", -1)
        map_confirm.setOnClickListener {
            var latLng: LatLng? = null
            var lat = ""
            var longt = ""
            if (marker == null) {
                latLng = LatLng(
                    aMap!!.cameraPosition.target.latitude,
                    aMap!!.cameraPosition.target.longitude
                )
                lat = if (latLng.latitude != 0.0) MyLocationManager.get()
                    .formatDouble(latLng.latitude)!! else ""
                longt =
                    if (latLng.longitude != 0.0) MyLocationManager.get()
                        .formatDouble(latLng.longitude)!! else ""
            } else {
                latLng = marker!!.position
                lat = if (latLng.latitude != 0.0) MyLocationManager.get()
                    .formatDouble(latLng.latitude)!! else ""
                longt =
                    if (latLng.longitude != 0.0) MyLocationManager.get()
                        .formatDouble(latLng.longitude)!! else ""
            }
            EventBus.getDefault().post(MessageEvent("map", index, longt, lat))
            finish()
        }
    }

    private fun initMap() {
        aMap = mMapView!!.map
        myLocationStyle =
            MyLocationStyle() //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle!!.interval(2000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap!!.setMyLocationStyle(myLocationStyle) //设置定位蓝点的Style
        aMap!!.uiSettings.isMyLocationButtonEnabled = true //设置默认定位按钮是否显示，非必需设置。
        aMap!!.isMyLocationEnabled = true // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap!!.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    aMap!!.cameraPosition.target.latitude,
                    aMap!!.cameraPosition.target.longitude
                ), 16f
            )
        )
        aMap!!.setOnMapLoadedListener(this)
        aMap!!.setOnMarkerDragListener(this)
    }

    private fun addMarker(lat: Double, longt: Double) {
        if (marker != null) {
            marker!!.remove()
        }
        markerOptions = MarkerOptions()
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            .position(LatLng(lat, longt))
            .title("当前纬度：" + MyLocationManager.get().formatDouble(lat))
            .snippet("当前经度：" + MyLocationManager.get().formatDouble(longt))
            .draggable(true)
        marker = aMap!!.addMarker(markerOptions)
        marker!!.showInfoWindow()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView!!.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView!!.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mMapView!!.onSaveInstanceState(outState)
    }

    override fun onMapLoaded() {
        Thread {
            try {
                Thread.sleep(3000)
                aMap?.let {
                    addMarker(it.cameraPosition.target.latitude, it.cameraPosition.target.longitude)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }.start()
    }

    override fun onMarkerDragStart(p0: Marker?) {

    }

    override fun onMarkerDrag(p0: Marker?) {

    }

    override fun onMarkerDragEnd(p0: Marker?) {
        addMarker(p0!!.position.latitude, p0.position.longitude)
    }

    override fun onLocationChanged(p0: Location?) {

    }
}