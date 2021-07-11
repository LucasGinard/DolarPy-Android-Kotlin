package com.lucasginard.dolarpy.ui

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.lucasginard.dolarpy.DolarApp
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.Utils.Tools
import com.lucasginard.dolarpy.com_ven
import com.lucasginard.dolarpy.data.apiService
import com.lucasginard.dolarpy.database.DolarEntity
import com.lucasginard.dolarpy.databinding.FragmentCotizacionBinding
import com.lucasginard.dolarpy.domain.MainRepository
import com.lucasginard.dolarpy.ui.adapter.adapterDolar
import com.lucasginard.dolarpy.ui.viewModel.MainViewModel
import com.lucasginard.dolarpy.ui.viewModel.MyViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList


class cotizacionFragment : Fragment() {

    private lateinit var _binding :FragmentCotizacionBinding
    private lateinit var viewModel: MainViewModel
    lateinit var listDolarSave: MutableList<DolarEntity>
    private lateinit var adapter: adapterDolar

    private val retrofitService = apiService.getInstance()
    private var listaSave = ArrayList<com_ven>()
    private var lista = ArrayList<com_ven>()
    private var monto = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding  = FragmentCotizacionBinding.inflate(inflater, container, false)
        configureUI()
        configureRecycler()
        getDolaresIngresados()
        configureOnClickListener()
        return  _binding .root
    }

    private fun configureUI(){
        if (Tools.flatCheck){
            _binding.etMonto.visibility = View.GONE
            _binding.tvConnect.visibility = View.VISIBLE
        }
        listDolarSave = ArrayList()
        if (Tools.listBase.isNotEmpty() && Tools.flatSave){
            getListSave(Tools.listBase)
            Tools.flatSave = false
        }
    }

    private fun configureOnClickListener() {
        _binding.tvReconnect.setOnClickListener {
            getApi()
            _binding.pgLoading.visibility = View.VISIBLE
        }

        _binding.tvDataSave.setOnClickListener {
            getDolarSave()
        }

    }

    private fun configureRecycler(){
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

        }
        if (Tools.lastUpdate != ""){
            _binding.tvLastUpdate.visibility = View.VISIBLE
            _binding.tvLastUpdate.text = "${getText(R.string.lastUpdate)} ${Tools.lastUpdate}"
        }
        lista.clear()
        lista.addAll(Tools.listBase)
        val list:List<com_ven> = Tools.listBase
        listaSave.addAll(list)
        adapter.notifyDataSetChanged()
    }

    private fun getApi(){
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(MainViewModel::class.java)
        viewModel.getDolarList.observe(requireActivity(), Observer {
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
            Tools.listBase.clear()
            Tools.listBase.add(it.dolarpy.amambay)
            Tools.listBase.add(it.dolarpy.bbva)
            Tools.listBase.add(it.dolarpy.bcp)
            Tools.listBase.add(it.dolarpy.bonanza)
            Tools.listBase.add(it.dolarpy.cambiosalberdi)
            Tools.listBase.add(it.dolarpy.cambioschaco)
            Tools.listBase.add(it.dolarpy.interfisa)
            Tools.listBase.add(it.dolarpy.lamoneda)
            Tools.listBase.add(it.dolarpy.maxicambios)
            Tools.listBase.add(it.dolarpy.mundialcambios)
            Tools.listBase.add(it.dolarpy.mydcambios)
            Tools.listBase.add(it.dolarpy.set)
            Tools.listBase.add(it.dolarpy.vision)
            Tools.lastUpdate = it.update
            _binding.etMonto.visibility = View.VISIBLE
            _binding.recycler.visibility = View.VISIBLE
            _binding.tvConnect.visibility = View.GONE
            _binding.pgLoading.visibility = View.GONE
            Tools.flatCheck = false
            getDolaresIngresados()
        })

        viewModel.errorMessage.observe(requireActivity(), Observer {
            Tools.dialogCustom(requireActivity(), getString(R.string.textErrorNet))
            if (_binding.etMonto.visibility == View.VISIBLE && Tools.listBase.isNotEmpty()){
                getApi()
            }
            _binding.recycler.visibility = View.GONE
            _binding.pgLoading.visibility = View.GONE
            _binding.tvConnect.visibility = View.VISIBLE
        })
        viewModel.getAllDolar()
    }

    private fun getDolarSave() {
        GlobalScope.launch {
            listDolarSave = DolarApp.database.dolarDao().getAllDolar()
            if (Tools.listBase.isEmpty()){
                val List = ArrayList<com_ven>()
                for (x in listDolarSave){
                    val com = com_ven(x.name,x.buy,x.sell)
                    List.add(com)
                }
                Tools.listBase.clear()
                Tools.listBase.addAll(List)
                withContext(Dispatchers.Main){
                    _binding.tvConnect.visibility = View.GONE
                    _binding.etMonto.visibility = View.VISIBLE
                    _binding.recycler.visibility = View.VISIBLE
                    adapter.notifyDataSetChanged()
                    getDolaresIngresados()
                }
            }
        }
    }

    private fun getListSave(list:ArrayList<com_ven>){
        for (x in list){
            addDolar(DolarEntity(name = x.name!!,buy = x.compra,sell= x.venta))
        }
    }

    private fun addDolar(dolar: DolarEntity){
        GlobalScope.launch {
            val id = DolarApp.database.dolarDao().addDolar(dolar)
            val recoveryDolar = DolarApp.database.dolarDao().getDolarById(id)
            listDolarSave.add(recoveryDolar)
        }
    }

    companion object {
        fun newInstance() = cotizacionFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}