package com.lucasginard.dolarpy.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.com_ven
import com.lucasginard.dolarpy.data.apiService
import com.lucasginard.dolarpy.databinding.FragmentInicioBinding
import com.lucasginard.dolarpy.dolarpy
import com.lucasginard.dolarpy.domain.MainRepository
import com.lucasginard.dolarpy.ui.adapter.adapterDolar
import com.lucasginard.dolarpy.ui.viewModel.MainViewModel
import com.lucasginard.dolarpy.ui.viewModel.MyViewModelFactory


class fragment_inicio : Fragment() {

    private lateinit var _binding :FragmentInicioBinding
    private lateinit var adapter: adapterDolar
    private val dolarMain = mutableListOf<dolarpy>()
    val lista = ArrayList<com_ven>()
    val listaSave = ArrayList<com_ven>()
    private val mHandler: Handler = Handler(Looper.getMainLooper())
    private val retrofitService = apiService.getInstance()
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding  = FragmentInicioBinding.inflate(inflater, container, false)
        getAPI()
        configureRecycler()
        getDolaresIngresados()
        return  _binding .root
    }

     fun configureRecycler(){
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
        _binding.etMontoIngresado.doAfterTextChanged {

        }

    }

    private fun getAPI() {
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(MainViewModel::class.java)

        viewModel.getDolarList.observe(requireActivity(), Observer {
            _binding.tvLastUpdate.visibility = View.VISIBLE
            _binding.tvLastUpdate.text = "${getText(R.string.lastUpdate)} ${it.update}"
            it.dolarpy.amambay.name = "AMANBAY"
            it.dolarpy.bbva.name = "BBVA"
            it.dolarpy.bcp.name = "BCP"
            it.dolarpy.bonanza.name = "BONANZA"
            it.dolarpy.cambiosalberdi.name = "CAMBIOS ALBERDI"
            it.dolarpy.cambioschaco.name = "CAMBIOS CHACO"
            it.dolarpy.interfisa.name = "INTERFISA"
            it.dolarpy.lamoneda.name = "LA MONEDA"
            it.dolarpy.maxicambios.name = "MAXICAMBIOS"
            it.dolarpy.mundialcambios.name = "MUNDIAL CAMBIOS"
            it.dolarpy.mydcambios.name = "MYD CAMBIOS"
            it.dolarpy.set.name = "SET"
            it.dolarpy.vision.name = "Visión Banco"
            lista.clear()
            lista.add(it.dolarpy.amambay)
            lista.add(it.dolarpy.bbva)
            lista.add(it.dolarpy.bcp)
            lista.add(it.dolarpy.bonanza)
            lista.add(it.dolarpy.cambiosalberdi)
            lista.add(it.dolarpy.cambioschaco)
            lista.add(it.dolarpy.interfisa)
            lista.add(it.dolarpy.lamoneda)
            lista.add(it.dolarpy.maxicambios)
            lista.add(it.dolarpy.mundialcambios)
            lista.add(it.dolarpy.mydcambios)
            lista.add(it.dolarpy.set)
            lista.add(it.dolarpy.vision)
            adapter.notifyDataSetChanged()
        })

        viewModel.errorMessage.observe(requireActivity(), Observer {

        })
        viewModel.getAllDolar()
    }

    companion object {
        fun newInstance() =
            fragment_inicio().apply {
                arguments = Bundle().apply {

                }
            }
    }
}