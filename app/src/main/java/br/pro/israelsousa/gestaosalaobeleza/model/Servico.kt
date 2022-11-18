package br.pro.israelsousa.gestaosalaobeleza.model

import java.text.DateFormat
import java.time.LocalDate
import java.util.*

class Servico{


    var id: String = ""
    var status: String = "Aberto"
    var idUser: String=""
    var tipoServico: String=""
    var servico: String = ""
    var nomeCliente: String = ""
    var descricao: String = ""
    var quantidade: String = ""
    var unidade: String = ""
    var profissional: String = ""
    var data: String = ""
    var horario: String = ""

    override fun toString(): String {
        return "Servico(id=$id, idUser='$idUser', especialidade='$tipoServico, 'servico='$servico', description='$descricao', quantidade=$quantidade, unidade=$unidade, data=$data,horario=$horario,profissional=$profissional)"
    }
}