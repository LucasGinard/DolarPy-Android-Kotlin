package com.lucasginard.dolarpy.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.Utils.Tools
import com.lucasginard.dolarpy.com_ven

class adapterDolar (private val localesDolar: ArrayList<com_ven>) : RecyclerView.Adapter<dolarViewHolder>() {

    var auxLocalesDolar:ArrayList<com_ven> ?= null

    init {
        if (auxLocalesDolar.isNullOrEmpty()){
            auxLocalesDolar = localesDolar
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): dolarViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return dolarViewHolder(layoutInflater.inflate(R.layout.item_dolar, parent, false))
    }

    override fun getItemCount(): Int = auxLocalesDolar!!.size

    override fun onBindViewHolder(holder: dolarViewHolder, position: Int) {
        val item = auxLocalesDolar?.get(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    fun calcularCotizacion(ingresado:Int ?= null){
        val item = localesDolar
        for ((i,x) in item.withIndex()){
            x.compra = 5 * auxLocalesDolar?.get(i)!!.compra
            x.venta = 5 * auxLocalesDolar?.get(i)!!.venta
            if (x.referencialDiario != null){
                x.referencialDiario = 5 * auxLocalesDolar?.get(i)!!.referencialDiario!!
            }
        }
    }

    fun clearCotizacion(){
        auxLocalesDolar?.clear()
        auxLocalesDolar?.addAll(Tools.listBase)
        notifyDataSetChanged()
    }

}