package br.pro.israelsousa.gestaosalaobeleza.model

import com.google.gson.annotations.SerializedName

data class Profissional (

    @SerializedName("idProfissional") val idProfissional: Integer?,
    @SerializedName("nomeProfissional") val nomeProfissional: String?

)
