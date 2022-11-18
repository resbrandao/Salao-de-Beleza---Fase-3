package br.pro.israelsousa.gestaosalaobeleza.service

import br.pro.israelsousa.gestaosalaobeleza.model.Profissional
import retrofit2.Call
import retrofit2.http.GET
import java.util.ArrayList

interface ProfissionalService {

    @GET("profissionais")
    fun getProfissional()  : Call<ArrayList<Profissional>>

}