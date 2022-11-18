package br.pro.israelsousa.gestaosalaobeleza.retrofit

import br.pro.israelsousa.gestaosalaobeleza.service.EspecialidadeService
import br.pro.israelsousa.gestaosalaobeleza.service.ProfissionalService
import br.pro.israelsousa.gestaosalaobeleza.service.UnidadeService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Url
const val BASE_URL = "http://10.0.2.2:8080/"

fun getRetrofitFactory() =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

fun retrofitUnidadeService(): UnidadeService =
    getRetrofitFactory()
        .create(UnidadeService::class.java)

fun retrofitProfissionalService(): ProfissionalService =
    getRetrofitFactory()
        .create(ProfissionalService::class.java)

fun retrofitEspecialidadeService(): EspecialidadeService =
    getRetrofitFactory()
        .create(EspecialidadeService::class.java)
