package com.lucasginard.dolarpy.data.model

import androidx.annotation.Keep
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

@Keep
data class dolarpyResponse(
        @SerializedName("dolarpy") var dolarpy:JsonObject,
        @SerializedName("updated") var update: String
)

@Keep
data class com_ven(
        var name: String? = "",
        var compra: Double,
        var venta: Double,
        var referencial_diario: Double? = null
)