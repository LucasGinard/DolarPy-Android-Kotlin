package com.lucasginard.dolarpy.view.home.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.com_ven
import com.lucasginard.dolarpy.data.apiService
import com.lucasginard.dolarpy.database.DolarEntity
import com.lucasginard.dolarpy.databinding.FragmentCotizacionBinding
import com.lucasginard.dolarpy.domain.MainRepository
import com.lucasginard.dolarpy.utils.Tools
import com.lucasginard.dolarpy.utils.setTint
import com.lucasginard.dolarpy.view.adapter.adapterDolar
import com.lucasginard.dolarpy.view.viewModel.MainViewModel
import com.lucasginard.dolarpy.view.viewModel.MyViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CotizacionFragment : Fragment() {

    private lateinit var listDolarSave: MutableList<DolarEntity>
    private lateinit var preference: SharedPreferences
    private lateinit var _binding :FragmentCotizacionBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: adapterDolar

    private val retrofitService = apiService.getInstance()
    private val sharedName = "UpdateSave"
    private var listaSave = ArrayList<com_ven>()
    private var lista = ArrayList<com_ven>()
    private var isBuy = true
    private var isLess = true
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
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(MainViewModel::class.java)
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
        backgroundTint()
        if (requireContext().resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK ==  Configuration.UI_MODE_NIGHT_NO){
            _binding.btnConfigOrder.setTint(R.color.secondColor)
        }else{
            _binding.btnConfigOrder.setTint(R.color.white)
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

        _binding.rbMore.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                adapter.setOrderUp(isBuy,monto)
                backgroundTint(true)
                isLess = false
                saveOrder("isLess",isLess)
            }
        }

        _binding.rbLess.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                adapter.setOrderDown(isBuy,monto)
                backgroundTint()
                isLess = true
                saveOrder("isLess",isLess)
            }
        }

        _binding.tvSetOrder.setOnClickListener {
            showMenu(it,R.menu.item_spinner_buy)
        }

        val visibilityFilter = {
            if (_binding.linearConfig.visibility == View.GONE){
                _binding.linearConfig.visibility = View.VISIBLE
                _binding.btnConfigOrder.setBackgroundResource(R.drawable.ic_arrow_up)
            }else{
                _binding.linearConfig.visibility = View.GONE
                _binding.btnConfigOrder.setBackgroundResource(R.drawable.ic_arrow_drop_down_circle)
            }
        }
        _binding.btnConfigOrder.setOnClickListener {
            Tools.animationY(_binding.linearConfig)
            visibilityFilter()
        }

        _binding.linearConfig.setOnLongClickListener {
            visibilityFilter()
            true
        }

        _binding.refreshList.setOnRefreshListener {
            getApi()
            _binding.refreshList.isRefreshing = false
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            if (menuItem.itemId == R.id.nav_sell){
                _binding.tvSetOrder.text = getString(R.string.dolarSellTitle)
                isBuy = false
                saveOrder("isBuy",isBuy)
                orderList()
            }
            if (menuItem.itemId == R.id.nav_buy){
                _binding.tvSetOrder.text = getString(R.string.dolarBuyTitle)
                isBuy = true
                saveOrder("isBuy",isBuy)
                orderList()
            }
            return@setOnMenuItemClickListener true
        }
        popup.setOnDismissListener {

        }
        popup.show()
    }

    private fun configureRecycler(){
        adapter = adapterDolar(lista)
        orderList()
        _binding.rvDolar.layoutManager = GridLayoutManager(
            requireContext(),
            2,
            RecyclerView.VERTICAL,
            false
        )
        _binding .rvDolar.adapter = adapter
        if (Tools.flatRecyclerSave){
            _binding.tvConnect.visibility = View.GONE
            _binding.etMonto.visibility = View.VISIBLE
            _binding.recycler.visibility = View.VISIBLE
            _binding.tvLastUpdate.visibility = View.VISIBLE
            _binding.btnRefresh.visibility = View.VISIBLE
            _binding.tvLastUpdate.text = "${getString(R.string.tvUpdateSave)}${preference.getString("lastUpdate","")}"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getDolaresIngresados(){
        _binding.etMontoIngresado.doAfterTextChanged {
            if(_binding.etMontoIngresado.text.toString() == "."){
                _binding.etMontoIngresado.setText("0.")
                _binding.etMontoIngresado.setSelection(2)
            }
            if (!it.isNullOrEmpty() && _binding.etMontoIngresado.text.toString() != "0."){
                monto = _binding.etMontoIngresado.text.toString()
                adapter.calcularCotizacion(monto.toDouble())
                adapter.notifyDataSetChanged()
            }else{
                monto = ""
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
        viewModel.getDolarList.observe(requireActivity(), {
            it.dolarpy.amambay.name = "AMANBAY"
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
            it.dolarpy.eurocambios.name = "Euro Cambios"
            it.dolarpy.gnbfusion.name = "GNB FUSIÓN"
            Tools.listBase.clear()
            Tools.listBase.add(it.dolarpy.amambay)
            Tools.listBase.add(it.dolarpy.gnbfusion)
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
            Tools.listBase.add(it.dolarpy.eurocambios)
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
            orderList()
            Tools.flatRecyclerSave = false
        })

        viewModel.errorMessage.observe(requireActivity(), {
            Tools.dialogCustom(requireActivity(), getString(R.string.textErrorNet),{})
            if (_binding.etMonto.visibility == View.VISIBLE && Tools.listBase.isNotEmpty() && !Tools.flatCheck){
                getApi()
                _binding.recycler.visibility = View.GONE
                _binding.pgLoading.visibility = View.GONE
                _binding.tvConnect.visibility = View.VISIBLE
            }
            _binding.pgLoading.visibility = View.GONE
        })
        viewModel.getAllDolar()
    }

    private fun getDolarSave() {
        GlobalScope.launch {
            listDolarSave = viewModel.getAllDolarListSave()
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
                    Tools.flatRecyclerSave = true
                    orderList()
                }
            }
        }
    }

    private fun getListSave(list:ArrayList<com_ven>){
        if(preference.getString("lastUpdate","") != ""){
            GlobalScope.launch {
                listDolarSave = viewModel.getAllDolarListSave()
                withContext (Dispatchers.Main) {
                    updateList(list)
                }
            }
        }else{
            for (x in list){
                viewModel.addDolar(DolarEntity(name = x.name!!,buy = x.compra,sell= x.venta),listDolarSave)
            }
        }
    }

    private fun updateList(list: ArrayList<com_ven>) {
        for(x in list){
            Log.d("valorTest", "Nombre: ${x.name}/${x.compra}/${x.venta}")
        }
        if (listDolarSave.isNotEmpty()){
            var flat = false
            var idDolar = 0
            for (x in list){
                for (listSave in listDolarSave){
                    if (x.name == listSave.name){
                        flat = true
                        idDolar = listSave.id
                    }
                }
                if (flat){
                    viewModel.updateDolar(DolarEntity(id = idDolar,name = x.name!!,buy = x.compra,sell = x.venta))
                }else{
                    viewModel.addDolar(DolarEntity(name = x.name!!,buy = x.compra,sell= x.venta),listDolarSave)
                }
            }
        }
    }

    private fun saveStringUpdate(name:String) {
        val editor = preference.edit()
        editor.putString("lastUpdate",name)
        editor.apply()
    }

    private fun saveOrder(key:String,valueSave:Boolean) {
        val editor = preference.edit()
        editor.putBoolean(key,valueSave)
        editor.apply()
    }

    private fun backgroundTint(boolean: Boolean = false){
        if (!boolean){
            _binding.rbMore.setTint(R.color.common_google_signin_btn_text_light_focused)
            _binding.rbLess.setTint(R.color.primaryColor)
        }else{
            _binding.rbMore.setTint(R.color.primaryColor)
            _binding.rbLess.setTint(R.color.common_google_signin_btn_text_light_focused)
        }
    }

    private fun orderList(){
        isBuy = preference.getBoolean("isBuy",true)
        isLess = preference.getBoolean("isLess",true)
        if (isLess){
            adapter.setOrderDown(isBuy,monto)
        }else{
            adapter.setOrderUp(isBuy,monto)
        }
        if (!isBuy) _binding.tvSetOrder.text = getString(R.string.dolarSellTitle)
        if (!isLess){
            _binding.rbMore.isChecked = true
            backgroundTint(true)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance() = CotizacionFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}