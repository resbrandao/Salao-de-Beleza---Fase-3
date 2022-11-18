package br.pro.israelsousa.gestaosalaobeleza.repository

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.pro.israelsousa.gestaosalaobeleza.adapter.AgendamentoAdapter
import br.pro.israelsousa.gestaosalaobeleza.model.Servico
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ConsultaAgendamento {

    private val servicoList = mutableListOf<Servico>()
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun getTask(context: Context,recyclerView: RecyclerView, tipoCliente : String,unidadeSalao : String) {

        if (tipoCliente == "Empresa") {

            var agendamento =
                db.collection("agendamento")
                    .whereEqualTo("unidade", unidadeSalao)
                    .get().addOnSuccessListener { docs ->
                        // initAdapter(docs)
                        for (doc in docs) {
                            if (doc != null) {
                                val task = doc.toObject(Servico::class.java)
                                servicoList.add(task)

                                Log.d(
                                    "consulta1",
                                    "Consulta com sucesso : ${doc.data?.getValue("servico")}"
                                )
                            } else {
                                Log.d(ContentValues.TAG, "No such document")
                            }
                        }

                        initAdapter(servicoList,context,recyclerView)
                    }

        } else {
            var agendamento =
                db.collection("agendamento")
                    .whereEqualTo("idUser", auth.currentUser?.uid.toString())
                    .get().addOnSuccessListener { docs ->
                        // initAdapter(docs)
                        for (doc in docs) {
                            if (doc != null) {

                                val task = doc.toObject(Servico::class.java)
                                servicoList.add(task)

                                Log.d(
                                    "consulta2",
                                    "Consulta com sucesso : ${doc.data?.getValue("servico")}"
                                )
                            } else {
                                Log.d(ContentValues.TAG, "No such document")
                            }
                        }

                        initAdapter(servicoList,context,recyclerView)
                    }
        }


    }
    fun initAdapter(listaServico: List<Servico>,context :Context, recyclerView: RecyclerView) {

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = AgendamentoAdapter(listaServico)

        }
    }


}