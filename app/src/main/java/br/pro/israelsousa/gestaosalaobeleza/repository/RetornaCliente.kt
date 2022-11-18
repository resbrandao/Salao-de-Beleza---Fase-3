package br.pro.israelsousa.gestaosalaobeleza.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.pro.israelsousa.gestaosalaobeleza.model.Cliente
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.FileNotFoundException
import java.io.IOException

class RetornaCliente(): AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var idCliente: String
    private lateinit var nomeCliente: String
    private lateinit var tipoCliente: String
    private lateinit var unidade: String

    @SuppressLint("Range")
    fun getCliente(context: Context) {
        nomeCliente=""
        var tpUser =
            db.collection("users").whereEqualTo("idUser", auth.currentUser?.uid.toString()).get()
                .addOnSuccessListener { docs ->
                    for (doc in docs) {
                        if (doc != null ) {
                           // RetornaAgendamento().updateCliente(doc.data?.getValue("nomeCompleto").toString())//(doc.data?.getValue("nomeCompleto").toString())
                            idCliente = doc.data.get("idUser").toString()
                            nomeCliente = doc.data.get("nomeCompleto").toString()
                            tipoCliente = doc.data.get("tipo").toString()
                            unidade = doc.data.get("unidadeSalao").toString()

                            gravaDadosCliente(context,idCliente,nomeCliente,tipoCliente,unidade)

//                            Log.d(
//                                "sucesso",
//                                "TIPO DO USUÁRIO : ${doc.data?.getValue("nomeCompleto").toString()}"
////                                "TIPO DO USUÁRIO : ${doc.data?.getValue("tipo")}"
//                                                       )
                            break
                        } else {
                            Log.d("falha", "No such document")
                        }
                    }

                }

       return
    }

    fun gravaDadosCliente(context: Context , idCliente : String, nomeCliente: String, tipoCliente: String, unidade: String) {

        try {
            val db = DatabaseManager(context,"CLIENTE")
            db.removeCliente()
            db.insereCliente(idCliente,nomeCliente,tipoCliente,unidade)

        }
        catch (e: FileNotFoundException){
            Log.i("gravaDadoArquivo","FileNotFoundException")
        }
        catch (e: IOException){
            Log.i("gravaDadoArquivo","IOException")
        }
    }

    fun recuperaDadosCliente(context: Context): Cliente? {

        try{
            var cliente = Cliente()
            var teste = ""
            val db = DatabaseManager(context,"CLIENTE")

            cliente =  db.listaCliente()

            return cliente
        }
        catch (e: FileNotFoundException){
            return null
        }
        catch (e: IOException){
            return null
        }
    }
}
