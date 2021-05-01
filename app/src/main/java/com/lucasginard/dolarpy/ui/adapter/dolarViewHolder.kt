package com.lucasginard.dolarpy.ui.adapter

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
        var  df =  DecimalFormat("#,###");
        binding.tvCompraM.text = "₲${df.format(local.compra.toInt()).replace(",",".")}"
        binding.tvVentaM.text =  "₲${df.format(local.venta.toInt()).replace(",",".")}"
        binding.tvLocal.text = local.name
        if (local.referencialDiario != null){
            var  df =  DecimalFormat("##0,000.00");
            binding.linearRef.visibility = View.VISIBLE
            binding.tvCompraM.text = "₲${df.format(local.compra)}"
            binding.tvVentaM.text =  "₲${df.format(local.venta)}"
            binding.tvRefM.text = "₲${df.format(local.referencialDiario)}"
        }else{
            if (Resources.getSystem().displayMetrics.heightPixels > 800){
                setMargin(binding.linearCompra, 8, 0, 40, 0)
                setMargin(binding.linearVenta, 8, 0, 70, 0)
            }
        }
    }
}