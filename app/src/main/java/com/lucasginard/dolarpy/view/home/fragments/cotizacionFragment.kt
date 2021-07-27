package com.lucasginard.dolarpy.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
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
import com.lucasginard.dolarpy.utils.Tools
import com.lucasginard.dolarpy.com_ven
import com.lucasginard.dolarpy.data.apiService
import com.lucasginard.dolarpy.database.DolarEntity
import com.lucasginard.dolarpy.databinding.FragmentCotizacionBinding
import com.lucasginard.dolarpy.domain.MainRepository
import com.lucasginard.dolarpy.view.adapter.adapterDolar
import com.lucasginard.dolarpy.view.viewModel.MainViewModel
import com.lucasginard.dolarpy.view.viewModel.MyViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList


class cotizacionFragment : Fragment() {

    private lateinit var listDolarSave: MutableList<DolarEntity>
    private lateinit var preference: SharedPreferences
    private lateinit var _binding :FragmentCotizacionBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: adapterDolar

    private val retrofitService = apiService.getInstance()
    private val sharedName = "UpdateSave"
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
        preference = requireActivity().getSharedPreferences(sharedName,Context.MODE_PRIVATE)
        if (Tools.flatCheck){
            _binding.etMonto.visibility = View.GONE
            _binding.recycler.visibility = View.GONE
            _binding.tvConnect.visibility = View.VISIBLE
        }

        listDolarSave = ArrayList()
        if (Tools.listBase.isNotEmpty() && Tools.flatSave){
            getListSave(Tools.listBase)
            Tools.flatSave = false
        }

        if (Tools.lastUpdate != ""){
            _binding.tvLastUpdate.visibility = View.VISIBLE
            _binding.tvLastUpdate.text = "${getText(R.string.lastUpdate)} ${Tools.lastUpdate}"
            saveStringUpdate(Tools.lastUpdate)
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

        _binding.btnRefresh.setOnClickListener {
            getApi()
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
        Tools.listBase.removeIf { it.venta == 0.0 && it.compra == 0.0 }
        lista.clear()
        lista.addAll(Tools.listBase)
        val list:List<com_ven> = Tools.listBase
        listaSave.addAll(list)
        adapter.notifyDataSetChanged()
    }

    private fun getApi(){
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(MainViewModel::class.java)
        viewModel.getDolarList.observe(requireActivity(), Observer {
            deleteDolarList()
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
            _binding.btnRefresh.visibility = View.GONE
            Tools.flatCheck = false
            getDolaresIngresados()
            saveStringUpdate(it.update)
            if (Tools.lastUpdate != ""){
                _binding.tvLastUpdate.visibility = View.VISIBLE
                _binding.tvLastUpdate.text = "${getText(R.string.lastUpdate)} ${Tools.lastUpdate}"
                saveStringUpdate(Tools.lastUpdate)
            }
            getListSave(Tools.listBase)
        })

        viewModel.errorMessage.observe(requireActivity(), Observer {
            Tools.dialogCustom(requireActivity(), getString(R.string.textErrorNet))
            if (_binding.etMonto.visibility == View.VISIBLE && Tools.listBase.isNotEmpty() && !Tools.flatCheck){
                getApi()
                _binding.recycler.visibility = View.GONE
                _binding.pgLoading.visibility = View.GONE
                _binding.tvConnect.visibility = View.VISIBLE
            }
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
                    _binding.tvLastUpdate.visibility = View.VISIBLE
                    _binding.btnRefresh.visibility = View.VISIBLE
                    adapter.notifyDataSetChanged()
                    getDolaresIngresados()
                    _binding.tvLastUpdate.text = "${getString(R.string.tvUpdateSave)}${preference.getString("lastUpdate","")}"
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

    private fun saveStringUpdate(name:String) {
        val editor = preference.edit()
        editor.putString("lastUpdate",name)
        editor.apply()
    }

    private fun deleteDolarList(){
        GlobalScope.launch {
            DolarApp.database.dolarDao().deleteDates()
        }
    }

    companion object {
        fun newInstance() = cotizacionFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}