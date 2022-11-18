package br.pro.israelsousa.gestaosalaobeleza.service

import br.pro.israelsousa.gestaosalaobeleza.model.Especialidade
import br.pro.israelsousa.gestaosalaobeleza.model.Unidade
import retrofit2.Call
import retrofit2.http.GET
import java.util.ArrayList

interface EspecialidadeService {

    @GET("especialidades")
    fun getEspecialidade()  : Call<ArrayList<Especialidade>>

}