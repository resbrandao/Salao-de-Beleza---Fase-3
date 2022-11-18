package br.pro.israelsousa.gestaosalaobeleza.model

import com.google.gson.annotations.SerializedName

data class Especialidade (

    @SerializedName("idEspecialidade") val idEspecialidade: Integer?,
    @SerializedName("nomeEspecialidade") val nomeEspecialidade: String?

)