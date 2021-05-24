package com.lucasginard.dolarpy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.Utils.Tools
import com.lucasginard.dolarpy.com_ven
import com.lucasginard.dolarpy.databinding.FragmentInicioBinding
import com.lucasginard.dolarpy.ui.adapter.adapterDolar


class fragment_inicio : Fragment() {

    private lateinit var _binding :FragmentInicioBinding
    private lateinit var adapter: adapterDolar
    val lista = ArrayList<com_ven>()
    val listaSave = ArrayList<com_ven>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding  = FragmentInicioBinding.inflate(inflater, container, false)
        configureRecycler()
        getDolaresIngresados()
        return  _binding .root
    }

     fun configureRecycler(){
        adapter = adapterDolar(lista)
         _binding.rvDolar.layoutManager = GridLayoutManager(
                 requireContext(),
                 2,
                 RecyclerView.VERTICAL,
                 false
         )
         _binding .rvDolar.adapter = adapter
    }
var flat = false
    private fun getDolaresIngresados(){
        _binding.etMontoIngresado.doAfterTextChanged {
            if (!flat){
                adapter.calcularCotizacion()
                adapter.notifyDataSetChanged()
                flat = true
            }else{
                adapter.clearCotizacion()
            }
        }
        if (Tools.lastUpdate != ""){
            _binding.tvLastUpdate.visibility = View.VISIBLE
            _binding.tvLastUpdate.text = "${getText(R.string.lastUpdate)} ${Tools.lastUpdate}"
        }
        lista.addAll(Tools.listBase)
        listaSave.addAll(Tools.listBase)
        adapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance() =
            fragment_inicio().apply {
                arguments = Bundle().apply {

                }
            }
    }
}