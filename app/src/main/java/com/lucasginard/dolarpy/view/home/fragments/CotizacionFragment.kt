package com.lucasginard.dolarpy.view.home.fragments

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.data.apiService
import com.lucasginard.dolarpy.data.model.com_ven
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
    private lateinit var _binding :FragmentCotizacionBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: adapterDolar

    private val retrofitService = apiService.getInstance()
    private var listaSave = ArrayList<com_ven>()
    private var lista = ArrayList<com_ven>()
    private var isBuy = true
    private var isLess = true
    private var monto = ""

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
            viewModel.setLastUpdateText(Tools.lastUpdate)
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
                viewModel.setIsLess(isLess)
            }
        }

        _binding.rbLess.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                adapter.setOrderDown(isBuy,monto)
                backgroundTint()
                isLess = true
                viewModel.setIsLess(isLess)
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
                viewModel.setIsBuy(isBuy)
                orderList()
            }
            if (menuItem.itemId == R.id.nav_buy){
                _binding.tvSetOrder.text = getString(R.string.dolarBuyTitle)
                isBuy = true
                viewModel.setIsBuy(isBuy)
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
            _binding.tvLastUpdate.text = "${getString(R.string.tvUpdateSave)}${viewModel.getLastUpdateText()}"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getDolaresIngresados(){
        _binding.etMontoIngresado.doAfterTextChanged {
            try {
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
                _binding.etMontoIngresado.error = null
            }catch (e:Exception){
                _binding.etMontoIngresado.error = ""
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
        viewModel.getDolarList.observe(requireActivity()) {
            val json = it.dolarpy
            Tools.listBase.clear()
            val entrySet: Set<Map.Entry<String, JsonElement?>> = json.entrySet()
            for ((key) in entrySet) {
                val gson = Gson().fromJson(json.get(key),com_ven::class.java)
                gson.name = key
                Tools.listBase.add(gson)
            }
            Tools.lastUpdate = it.update
            _binding.etMonto.visibility = View.VISIBLE
            _binding.recycler.visibility = View.VISIBLE
            _binding.tvConnect.visibility = View.GONE
            _binding.btnRefresh.visibility = View.GONE
            _binding.listLoading.visibility = View.GONE
            _binding.rvDolar.visibility = View.VISIBLE
            Tools.flatCheck = false
            getDolaresIngresados()
            if (Tools.lastUpdate != "") {
                _binding.tvLastUpdate.visibility = View.VISIBLE
                if (activity != null) {
                    _binding.tvLastUpdate.text =
                        "${getText(R.string.lastUpdate)} ${Tools.lastUpdate}"
                }
                viewModel.setLastUpdateText(Tools.lastUpdate)
            } else {
                viewModel.setLastUpdateText(it.update)
            }
            getListSave(Tools.listBase)
            orderList()
            Tools.flatRecyclerSave = false
        }

        viewModel.errorMessage.observe(requireActivity()) {
            if (this.activity != null) {
                Tools.dialogCustom(requireActivity(), getString(R.string.textErrorNet), {})
                _binding.listLoading.visibility = View.GONE
                _binding.recycler.visibility = View.GONE
                _binding.tvConnect.visibility = View.VISIBLE
            }
            if (_binding.etMonto.visibility == View.VISIBLE && Tools.listBase.isNotEmpty() && !Tools.flatCheck) {
                getApi()
                _binding.listLoading.visibility = View.GONE
                _binding.recycler.visibility = View.GONE
                _binding.tvConnect.visibility = View.VISIBLE
            }
        }
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
                    _binding.rvDolar.visibility = View.VISIBLE
                    _binding.tvLastUpdate.visibility = View.VISIBLE
                    _binding.btnRefresh.visibility = View.VISIBLE
                    adapter.notifyDataSetChanged()
                    getDolaresIngresados()
                    _binding.tvLastUpdate.text = "${getString(R.string.tvUpdateSave)}${viewModel.getLastUpdateText()}"
                    Tools.flatRecyclerSave = true
                    orderList()
                }
            }
        }
    }

    private fun getListSave(list:ArrayList<com_ven>){
        if(viewModel.getLastUpdateText() != ""){
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
        isBuy = viewModel.getIsBuy()
        isLess = viewModel.getIsLess()
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
        if(Tools.listBase.isEmpty()){
            getApi()
        }else{
            _binding.rvDolar.visibility = View.VISIBLE
            _binding.listLoading.visibility = View.GONE
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            _binding.etMontoIngresado.setText(it.getString("amountEdit") ?: "")
            getDolaresIngresados()
        }
    }

    companion object {
        fun newInstance() = CotizacionFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}