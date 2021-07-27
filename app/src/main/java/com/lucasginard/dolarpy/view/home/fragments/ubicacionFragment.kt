package com.lucasginard.dolarpy.view.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.utils.Tools
import com.lucasginard.dolarpy.utils.setTint
import com.lucasginard.dolarpy.databinding.FragmentUbicacionBinding


class ubicacionFragment : Fragment(), OnMapReadyCallback {

    private lateinit var _binding :FragmentUbicacionBinding
    private lateinit var GoogleMap: GoogleMap
    private var mapView: MapView? = null
    private var empresasCotizacion  = mapOf(
        Pair("Vision Banco Central",LatLng(-25.2894519,-57.571821)),
        Pair("BBVA",LatLng(-25.288306,-57.6262421)),
        Pair("Cambios Chaco",LatLng(-25.2935472,-57.6420617)),
        Pair("Cambios M&D",LatLng(-25.2941856,-57.5815143)),
        Pair("Mundial Cambios",LatLng(-25.2822143,-57.6549689)),
        Pair("MaxiCambios",LatLng(-25.2946103,-57.6323844)),
        Pair("La Moneda Cambios",LatLng(-25.5227634,-54.6536922)),
        Pair("Cambios Chaco S.A",LatLng(-25.3147567,-57.5814258)),
        Pair("Interfisa",LatLng(-25.3151002,-57.5814257)),
        Pair("Cambios Alberdi",LatLng(-25.3147567,-57.5814258)),
        Pair("Bonanza Cambios S.A",LatLng(-25.5109695,-54.6146669)),
        Pair("BCP",LatLng(-25.2780422,-57.5778163)),
        Pair("Cambios Alberdi",LatLng(-25.3147567,-57.5814258)),
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding  = FragmentUbicacionBinding.inflate(inflater, container, false)
        configureUI()
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

    private fun configureUI(){
        Tools.rotarImagen(_binding.ivIcon2)
        backgroundTint()
    }

    private fun backgroundTint(boolean: Boolean = false){
        if (!boolean){
            _binding.normal.setTint(R.color.primaryColor)
            _binding.satellite.setTint(R.color.secondColor)
        }else{
            _binding.normal.setTint(R.color.secondColor)
            _binding.satellite.setTint(R.color.primaryColor)
        }
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
        if (::GoogleMap.isInitialized){
            _binding.normal.setOnCheckedChangeListener { compoundButton, b ->
                if (b){
                    GoogleMap.mapType = com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL
                    backgroundTint()
                }
            }
            _binding.satellite.setOnCheckedChangeListener { compoundButton, b ->
                if (b){
                    GoogleMap.mapType = com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE
                    backgroundTint(true)
                }
            }
        }else{
            Tools.dialogCustom(requireActivity(),getString(R.string.initMaps))
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