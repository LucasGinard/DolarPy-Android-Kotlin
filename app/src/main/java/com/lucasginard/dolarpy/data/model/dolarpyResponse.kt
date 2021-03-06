package com.lucasginard.dolarpy.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class dolarpyResponse(
        @SerializedName("dolarpy") var dolarpy: dolarpy,
        @SerializedName("updated") var update: String
)

@Keep
data class dolarpy(
        val amambay: com_ven,
        val comven: com_ven,
        val bcp: com_ven,
        val bonanza: com_ven,
        val cambiosalberdi: com_ven,
        val cambioschaco: com_ven,
        val eurocambios: com_ven,
        val interfisa: com_ven,
        val lamoneda: com_ven,
        val maxicambios: com_ven,
        val mundialcambios: com_ven,
        val mydcambios: com_ven,
        val set: com_ven,
        val vision: com_ven,
        val gnbfusion: com_ven
)

@Keep
data class com_ven(
        var name: String? = "",
        var compra: Double,
        var venta: Double,
        @SerializedName("referencial_diario") var referencialDiario: Double? = null
)