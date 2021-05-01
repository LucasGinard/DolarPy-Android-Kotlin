package com.lucasginard.dolarpy.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lucasginard.dolarpy.com_ven
import com.lucasginard.dolarpy.data.apiService
import com.lucasginard.dolarpy.databinding.FragmentInicioBinding
import com.lucasginard.dolarpy.dolarpy
import com.lucasginard.dolarpy.ui.adapter.adapterDolar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class fragment_inicio : Fragment() {

    private lateinit var _binding :FragmentInicioBinding
    private lateinit var adapter: adapterDolar
    private val dolarMain = mutableListOf<dolarpy>()
    val lista = ArrayList<com_ven>()
    private val mHandler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding  = FragmentInicioBinding.inflate(inflater, container, false)
        testRecycler()
        getDolares()
        getDolaresIngresados()
        return  _binding .root
    }

     fun testRecycler(){
        adapter = adapterDolar(lista)
         _binding .rvDolar.layoutManager = GridLayoutManager(
                 requireContext(),
                 2,
                 RecyclerView.VERTICAL,
                 false
         )
         _binding .rvDolar.adapter = adapter
    }

    private fun getDolaresIngresados(){
        var monto = _binding.etMontoIngresado.text

        _binding.etMontoIngresado.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val a = adapter.calcularCotizacion(5)
                if (a != null) {
                    lista.clear()
                    lista.addAll(a)
                }
                adapter.notifyDataSetChanged()
                Log.d("funcionaDone","si kpelu")
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun getAPI(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://dolar.melizeche.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun getDolares(){
        CoroutineScope(Dispatchers.IO).launch{
            val response = getAPI().create(apiService::class.java).getDolar("1.0")
            val dolar = response.body()
            mHandler.post {
                if (response.isSuccessful){
                    val cotizacionesDolar = dolar?.dolarpy
                    dolarMain.clear()
                    if (cotizacionesDolar != null) {
                        dolarMain.addAll(listOf(cotizacionesDolar))
                    }

                    dolarMain[0].amambay.name = "AMANBAY"
                    lista.add(dolarMain[0].amambay)

                    dolarMain[0].bbva.name = "BBVA"
                    lista.add(dolarMain[0].bbva)

                    dolarMain[0].bcp.name = "BCP"
                    lista.add(dolarMain[0].bcp)

                    dolarMain[0].bonanza.name = "BONANZA"
                    lista.add(dolarMain[0].bonanza)

                    dolarMain[0].cambiosalberdi.name = "CAMBIOS ALBERDI"
                    lista.add(dolarMain[0].cambiosalberdi)

                    dolarMain[0].cambioschaco.name = "CAMBIOS CHACO"
                    lista.add(dolarMain[0].cambioschaco)

                    dolarMain[0].interfisa.name = "INTERFISA"
                    lista.add(dolarMain[0].interfisa)

                    dolarMain[0].lamoneda.name = "LA MONEDA"
                    lista.add(dolarMain[0].lamoneda)

                    dolarMain[0].maxicambios.name = "MAXICAMBIOS"
                    lista.add(dolarMain[0].maxicambios)

                    dolarMain[0].mundialcambios.name = "MUNDIAL CAMBIOS"
                    lista.add(dolarMain[0].mundialcambios)

                    dolarMain[0].mydcambios.name = "MYD CAMBIOS"
                    lista.add(dolarMain[0].mydcambios)

                    dolarMain[0].set.name = "SET"
                    lista.add(dolarMain[0].set)

                    dolarMain[0].vision.name = "Visión Banco"
                    lista.add(dolarMain[0].vision)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    companion object {
        fun newInstance() =
            fragment_inicio().apply {
                arguments = Bundle().apply {

                }
            }
    }
}