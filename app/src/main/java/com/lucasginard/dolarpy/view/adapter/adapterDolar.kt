package com.lucasginard.dolarpy.view.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.com_ven
import com.lucasginard.dolarpy.utils.Tools

class adapterDolar (var localesDolar: ArrayList<com_ven>) : RecyclerView.Adapter<dolarViewHolder>() {

    var auxLocalesDolar:ArrayList<com_ven> ?= null

    init {
        if (auxLocalesDolar.isNullOrEmpty()){
            auxLocalesDolar = localesDolar
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): dolarViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if(Resources.getSystem().displayMetrics.heightPixels <= 2060 ){
            dolarViewHolder(layoutInflater.inflate(R.layout.item_dolar_small, parent, false))
        }else{
            dolarViewHolder(layoutInflater.inflate(R.layout.item_dolar, parent, false))
        }
    }

    override fun getItemCount(): Int = auxLocalesDolar!!.size

    override fun onBindViewHolder(holder: dolarViewHolder, position: Int) {
        val item = auxLocalesDolar?.get(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    fun calcularCotizacion(ingresado:Double ?= null){
        val listItems = auxLocalesDolar
        val itemaux = ArrayList<com_ven>()
        for ((i,x) in listItems!!.withIndex()){
            val item = com_ven("",0.0,0.0,null)
            if (ingresado != null) {
                item.name = x.name
                item.compra = ingresado * Tools.listBase[i].compra
                item.venta  = ingresado * Tools.listBase[i].venta
                if (Tools.listBase[i].referencialDiario != null){
                    item.referencialDiario = ingresado * Tools.listBase[i].referencialDiario!!
                }
            }
            itemaux.add(item)
        }
        if (ingresado != null) {
            localesDolar.clear()
            localesDolar.addAll(itemaux)
            itemaux.clear()
            notifyDataSetChanged()
        }
    }

    fun clearCotizacion(){
        auxLocalesDolar?.clear()
        auxLocalesDolar?.addAll(Tools.listBase)
        notifyDataSetChanged()
    }

    fun setOrderUp(isBuy:Boolean = true,calculate:String?=""){
        val aux = Tools.listBase
        val resu = if (isBuy){
            aux.sortedByDescending{it.compra}
        }else{
            aux.sortedByDescending{it.venta}
        }
        auxLocalesDolar?.clear()
        auxLocalesDolar?.addAll(resu)
        Tools.listBase.clear()
        Tools.listBase.addAll(resu)
        if(calculate != "" && calculate?.isNotEmpty() == true) calcularCotizacion(calculate.toDouble())
        notifyDataSetChanged()
    }

    fun setOrderDown(isBuy:Boolean = true,calculate:String?=""){
        val aux = Tools.listBase
        val resu = if (isBuy){
            aux.sortedBy{it.compra}
        }else{
            aux.sortedBy{it.venta}
        }
        auxLocalesDolar?.clear()
        auxLocalesDolar?.addAll(resu)
        Tools.listBase.clear()
        Tools.listBase.addAll(resu)
        if(calculate != "" && calculate?.isNotEmpty() == true) calcularCotizacion(calculate.toDouble())
        notifyDataSetChanged()
    }

}