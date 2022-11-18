package br.pro.israelsousa.gestaosalaobeleza.model

class Cliente {

    var idCliente: String = ""
    var nomeCliente: String = ""
    var tipoCliente: String = ""
    var unidadeSalao: String = ""


    override fun toString(): String {
        return "Servico(idCliente=$idCliente, nomeCliente='$nomeCliente', tipoCliente='$tipoCliente', unidadeSalao='$unidadeSalao')"
    }

}