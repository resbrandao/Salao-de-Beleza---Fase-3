package br.pro.israelsousa.gestaosalaobeleza.home

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.pro.israelsousa.gestaosalaobeleza.R
import br.pro.israelsousa.gestaosalaobeleza.adapter.AgendamentoAdapter
import br.pro.israelsousa.gestaosalaobeleza.databinding.ActivityRetornaAgendamentoBinding
import br.pro.israelsousa.gestaosalaobeleza.login.Login
import br.pro.israelsousa.gestaosalaobeleza.model.Servico
import br.pro.israelsousa.gestaosalaobeleza.repository.ConsultaAgendamento
import br.pro.israelsousa.gestaosalaobeleza.repository.RetornaCliente
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RetornaAgendamento : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView
    private val servicoList = mutableListOf<Servico>()//ArrayList<Servico>()//
    private var tipoCliente : String = ""
    private var unidadeSalao : String = ""
    private lateinit var binding: ActivityRetornaAgendamentoBinding
    private lateinit var loadingPB: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retorna_agendamento)
        binding = ActivityRetornaAgendamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = findViewById(R.id.recyclerView)
        loadingPB = binding.idPBLoading

        var cliente = RetornaCliente().recuperaDadosCliente(this)
        var nomeCliente : TextView=findViewById(R.id.text_view_cliente)

        supportActionBar?.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.action_bar_layout)

        if (cliente != null) {
            nomeCliente.text = cliente.nomeCliente
            tipoCliente = cliente.tipoCliente
            unidadeSalao = cliente.unidadeSalao
        }


        val btn_add = findViewById<FloatingActionButton>(R.id.btn_add_servico)
        btn_add.setOnClickListener{
            val cadastrarServico = Intent(this, Agendamento::class.java)
            startActivity(cadastrarServico)
        }
        val btn_logout = findViewById<ImageButton>(R.id.ibLogout)
        btn_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val voltarHome = Intent(this, Login::class.java)
            startActivity(voltarHome)
            finish()
        }

        loadingPB.visibility = View.VISIBLE
       ConsultaAgendamento().getTask(this,recyclerView, tipoCliente ,unidadeSalao )
        loadingPB.visibility = View.GONE
    }




    }
