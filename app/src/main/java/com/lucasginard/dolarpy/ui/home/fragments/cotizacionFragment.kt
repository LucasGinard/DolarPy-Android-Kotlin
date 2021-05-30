package com.lucasginard.dolarpy.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.lucasginard.dolarpy.databinding.FragmentCotizacionBinding
import com.lucasginard.dolarpy.ui.adapter.adapterDolar


class cotizacionFragment : Fragment() {

    private lateinit var _binding :FragmentCotizacionBinding
    private lateinit var adapter: adapterDolar
    private var lista = ArrayList<com_ven>()
    private var listaSave = ArrayList<com_ven>()
    private var monto = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding  = FragmentCotizacionBinding.inflate(inflater, container, false)
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

    @SuppressLint("SetTextI18n")
    private fun getDolaresIngresados(){
        _binding.etMontoIngresado.doAfterTextChanged {
            if (!it.isNullOrEmpty()){
                monto = _binding.etMontoIngresado.text.toString()
                adapter.calcularCotizacion(monto.toInt())
                adapter.notifyDataSetChanged()
            }else{
                adapter.clearCotizacion()
            }

            Log.d("ValorOriginal","$monto")
        }
        if (Tools.lastUpdate != ""){
            _binding.tvLastUpdate.visibility = View.VISIBLE
            _binding.tvLastUpdate.text = "${getText(R.string.lastUpdate)} ${Tools.lastUpdate}"
        }
        lista.addAll(Tools.listBase)
        val list:List<com_ven> = Tools.listBase
        listaSave.addAll(list)
        adapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance() = cotizacionFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}