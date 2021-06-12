package com.lucasginard.dolarpy.ui.home.fragments

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.databinding.FragmentUbicacionBinding


class ubicacionFragment : Fragment(), OnMapReadyCallback {

    private lateinit var _binding :FragmentUbicacionBinding
    private lateinit var GoogleMap: GoogleMap
    private var mapView: MapView? = null
    private var empresasCotizacion  = mapOf<String,LatLng>(
        Pair("Vision Banco Central",LatLng(-25.2894519,-57.571821)),
        Pair("BBVA",LatLng(-25.288306,-57.6262421)),
        Pair("Cambios Chaco",LatLng(-25.2935472,-57.6420617))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding  = FragmentUbicacionBinding.inflate(inflater, container, false)
        initMaps()
        mapView = _binding.mapViewF
        mapView?.onCreate(savedInstanceState)
        mapView?.onResume()
        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mapView?.getMapAsync(this)
        return _binding.root
    }

    private fun initMaps(){
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(requireActivity(), permissions,0)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap.let {
            GoogleMap = it!!
        }
        val py = LatLng(-25.294589, -57.578563)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(py, 6f))
        for (x in empresasCotizacion){
            GoogleMap.addMarker(
                MarkerOptions()
                    .position(x.value)
                    .title(x.key)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_maps))
            )
        }
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }


    companion object {
        fun newInstance() =
            ubicacionFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}