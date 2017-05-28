package ru.annin.truckmonitor.presentation.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import ru.annin.truckmonitor.R
import ru.annin.truckmonitor.domain.model.CheckPoints
import ru.annin.truckmonitor.presentation.common.BaseViewDelegate
import ru.annin.truckmonitor.utils.safeLet
import ru.annin.truckmonitor.utils.toBitmap

/**
 * Экран карты.
 *
 * @author Pavel Annin.
 */
class MapFragment : MvpAppCompatFragment(), OnMapReadyCallback {

    companion object {
        private const val ARG_CHECK_POINTS = "ru.annin.truckmonitor.arg.check_points"
        @JvmStatic fun newInstance(contracts: List<CheckPoints>?) = MapFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_CHECK_POINTS, contracts?.let { ArrayList(it) })
            }
        }
    }

    // Component's
    private lateinit var viewDelegate: ViewDelegate
    private var map: GoogleMap? = null

    // Data's
    private var checkPoints: List<CheckPoints>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Data's
        checkPoints = arguments?.getSerializable(ARG_CHECK_POINTS) as ArrayList<CheckPoints>? ?: ArrayList()

        viewDelegate = ViewDelegate(view).apply {
            vMap.onCreate(null)
            vMap.getMapAsync(this@MapFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        viewDelegate.vMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewDelegate.vMap.onPause()
    }

    override fun onDestroy() {
        viewDelegate.vMap.onDestroy()


        super.onDestroy()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        map?.let {
            it.uiSettings.isMyLocationButtonEnabled = true
            it.isMyLocationEnabled = true
        }
        updateCheckPoints(checkPoints)
    }

    fun updateCheckPoints(checkPoints: List<CheckPoints>?) {
        safeLet(map, checkPoints) { map, points ->
            map.clear()
            // Marker
            points.forEach {
                map.addMarker(MarkerOptions()
                        .position(LatLng(it.coordinate.latitude.toDouble(), it.coordinate.longitude.toDouble()))
                        .title(it.name)
                        .snippet(it.address)
                        .icon(BitmapDescriptorFactory.fromBitmap(if (it.fact.isNullOrBlank())
                            R.drawable.ic_marker.toBitmap(activity) else R.drawable.ic_marker_disable.toBitmap(activity)))
                )
            }

            // Polyline
            val poly = PolylineOptions()
                    .width(4f)
                    .color(Color.RED)
                    .geodesic(true)
            points.indices
                    .filter { it <= points.size - 2 }
                    .forEach {
                        poly.add(LatLng(points[it].coordinate.latitude.toDouble(), points[it].coordinate.longitude.toDouble()),
                                LatLng(points[it + 1].coordinate.latitude.toDouble(), points[it + 1].coordinate.longitude.toDouble()))
                    }
            map.addPolyline(poly)
        }
    }

    private class ViewDelegate(vRoot: View) : BaseViewDelegate(vRoot) {

        // View's
        val vMap by findView<MapView>(R.id.v_map)
    }
}