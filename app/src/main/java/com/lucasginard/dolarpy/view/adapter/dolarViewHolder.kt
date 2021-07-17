package com.lucasginard.dolarpy.view.adapter

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lucasginard.dolarpy.Utils.Tools.Companion.setMargin
import com.lucasginard.dolarpy.com_ven
import com.lucasginard.dolarpy.databinding.ItemDolarBinding
import java.text.DecimalFormat


class dolarViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemDolarBinding.bind(view)
    fun bind(local: com_ven){
        binding.tvLocal.text = local.name
        val df =  DecimalFormat("#,###")
        if (local.referencialDiario != null && local.name == "BCP"){
            //val df =  DecimalFormat("##0,000.00")
            binding.linearRef.visibility = View.VISIBLE
            binding.tvCompraM.text = "₲${df.format(local.compra)}"
            binding.tvVentaM.text =  "₲${df.format(local.venta)}"
            binding.tvRefM.text = "₲${df.format(local.referencialDiario)}"
            setMargin(binding.linearCompra, left = 8, top = 20)
            setMargin(binding.linearVenta,  left = 8, top = 50)
        }else{
            binding.linearRef.visibility = View.GONE
            binding.tvCompraM.text = "₲${df.format(local.compra.toInt()).replace(",",".")}"
            binding.tvVentaM.text =  "₲${df.format(local.venta.toInt()).replace(",",".")}"
            if (Resources.getSystem().displayMetrics.heightPixels > 800 && binding.linearRef.visibility == View.GONE){
                setMargin(binding.linearCompra, left = 8, top = 40)
                setMargin(binding.linearVenta,  left = 8, top = 70)
            }
        }
        if (binding.tvCompraM.text.length > 8){
            binding.tvCompraM.textSize = 11F
            binding.tvVentaM.textSize = 11F
            binding.tvRefM.textSize = 11F
        }else{
            binding.tvCompraM.textSize = 14F
            binding.tvVentaM.textSize = 14F
            binding.tvRefM.textSize = 14F
        }
    }
}