package com.lucasginard.dolarpy.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.com_ven

class adapterDolar (private val localesDolar: List<com_ven>) : RecyclerView.Adapter<dolarViewHolder>() {

    var auxLocalesDolar:ArrayList<com_ven> ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): dolarViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return dolarViewHolder(layoutInflater.inflate(R.layout.item_dolar, parent, false))
    }

    override fun getItemCount(): Int = localesDolar.size

    override fun onBindViewHolder(holder: dolarViewHolder, position: Int) {
        val item = localesDolar[position]
        if (auxLocalesDolar.isNullOrEmpty()){
            holder.bind(item)
        }else{
            auxLocalesDolar!![position].let { holder.bind(it) }
        }
    }

    fun calcularCotizacion(ingresado:Int): ArrayList<com_ven>? {
        val item = localesDolar
        for (x in item){
            x.compra = ingresado * x.compra
            x.venta = ingresado * x.venta
            auxLocalesDolar?.add(x)
        }
        return auxLocalesDolar
    }
}