package com.lucasginard.dolarpy.view.adapter

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lucasginard.dolarpy.utils.Tools.Companion.setMargin
import com.lucasginard.dolarpy.data.model.com_ven
import com.lucasginard.dolarpy.databinding.ItemDolarBinding
import com.lucasginard.dolarpy.utils.conversionToDecimal


class dolarViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemDolarBinding.bind(view)
    fun bind(local: com_ven){
        binding.tvLocal.text = local.name
        if (local.referencial_diario != null){
            binding.linearRef.visibility = View.VISIBLE
            binding.tvCompraM.text = "₲${(local.compra.conversionToDecimal())}"
            binding.tvVentaM.text =  "₲${local.venta.conversionToDecimal()}"
            binding.tvRefM.text = "₲${local.referencial_diario?.conversionToDecimal()}"
            setMargin(binding.linearCompra, left = 8, top = 20)
            setMargin(binding.linearVenta,  left = 8, top = 50)
        }else{
            binding.linearRef.visibility = View.GONE
            binding.tvCompraM.text = "₲${local.compra.toInt().conversionToDecimal().replace(",",".")}"
            binding.tvVentaM.text =  "₲${local.venta.toInt().conversionToDecimal().replace(",",".")}"
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
        if(Resources.getSystem().displayMetrics.heightPixels <= 2060 ){
            setMargin(binding.CardViewDolar,left = 40,right = 10,top = 25)
        }
    }
}