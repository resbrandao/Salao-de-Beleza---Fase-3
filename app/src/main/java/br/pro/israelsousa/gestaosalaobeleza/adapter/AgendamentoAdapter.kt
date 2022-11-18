package br.pro.israelsousa.gestaosalaobeleza.adapter

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.pro.israelsousa.gestaosalaobeleza.R
import br.pro.israelsousa.gestaosalaobeleza.home.Agendamento
import br.pro.israelsousa.gestaosalaobeleza.model.Servico
import br.pro.israelsousa.gestaosalaobeleza.repository.RetornaCliente

class AgendamentoAdapter(private val itemsList: List<Servico>) :
    RecyclerView.Adapter<AgendamentoAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_agendamento, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]

        holder.status.text = item.status
        holder.nomeCliente.text = item.nomeCliente
        holder.servico.text = item.servico
        holder.descricao.text = item.descricao
        holder.quantidade.text = item.quantidade
        holder.unidade.text = item.unidade
        holder.data.text = item.data + " " + item.horario
        holder.profissional.text = item.profissional
        holder.tipoServico.text = item.servico
    }
    override fun getItemCount()= itemsList.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val status: TextView = itemView.findViewById<TextView>(R.id.text_status)
        val nomeCliente: TextView = itemView.findViewById<TextView>(R.id.text_view_cliente)
        val servico: TextView = itemView.findViewById<TextView>(R.id.text_view_servico)
        val descricao: TextView = itemView.findViewById<TextView>(R.id.text_view_descricao)
        val quantidade: TextView = itemView.findViewById<TextView>(R.id.text_view_quantidade)
        val unidade: TextView = itemView.findViewById<TextView>(R.id.text_view_unidade)
        val data: TextView = itemView.findViewById<TextView>(R.id.text_view_data)
        val profissional: TextView = itemView.findViewById<TextView>(R.id.text_view_profissional)
        val tipoServico: TextView = itemView.findViewById<TextView>(R.id.text_view_servico)





        }
    }
