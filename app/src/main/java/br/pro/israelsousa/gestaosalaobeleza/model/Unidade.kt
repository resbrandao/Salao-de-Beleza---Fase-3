package br.pro.israelsousa.gestaosalaobeleza.model

import com.google.gson.annotations.SerializedName

data class Unidade (

    @SerializedName("idUnidade") val idUnidade: Integer?,
    @SerializedName("nomeUnidade") val nomeUnidade: String?,
    @SerializedName("enderecoUnidade") val enderecoUnidade: String?,
    @SerializedName("telefoneUnidade") val telefoneUnidade: String?


)