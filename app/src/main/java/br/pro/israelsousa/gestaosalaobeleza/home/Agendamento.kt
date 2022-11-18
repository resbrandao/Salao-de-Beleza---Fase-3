package br.pro.israelsousa.gestaosalaobeleza.home

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import br.pro.israelsousa.gestaosalaobeleza.databinding.ActivityAgendamentoBinding
import br.pro.israelsousa.gestaosalaobeleza.helper.FirebaseHelper
import br.pro.israelsousa.gestaosalaobeleza.model.Especialidade
import br.pro.israelsousa.gestaosalaobeleza.model.Profissional
import br.pro.israelsousa.gestaosalaobeleza.model.Servico
import br.pro.israelsousa.gestaosalaobeleza.model.Unidade
import br.pro.israelsousa.gestaosalaobeleza.repository.RetornaCliente
import br.pro.israelsousa.gestaosalaobeleza.retrofit.retrofitEspecialidadeService
import br.pro.israelsousa.gestaosalaobeleza.retrofit.retrofitProfissionalService
import br.pro.israelsousa.gestaosalaobeleza.retrofit.retrofitUnidadeService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class Agendamento() : AppCompatActivity(), DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: ActivityAgendamentoBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    var idUser: String = ""
    var cadAgendamento: HashMap<String,Any> = hashMapOf()
    lateinit var unidSalao: Spinner
    lateinit var profissionalSp: Spinner
    lateinit var especialidadeSalao: Spinner
    private var tipoServico: String = ""
    private var unidade: String = ""
    private var profissional: String = ""
    private var nomeCliente: String = ""
    private lateinit var servico: Servico
    private var newServico: Boolean = true
    private val calendar = Calendar.getInstance()
    private val formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    private val formatHour = SimpleDateFormat("hh.mm a")
    private lateinit var data : View
    private lateinit var edtHorarioView : View
    private var horario: String = ""
    private var hora: String = ""
    private var minuto: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgendamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(br.pro.israelsousa.gestaosalaobeleza.R.layout.action_bar_layout)

        var cliente = RetornaCliente().recuperaDadosCliente(this)
        data = binding.editDate
        edtHorarioView = binding.editHorario

        if (cliente != null) {
            nomeCliente = cliente.nomeCliente
        }

        data.setOnClickListener {

            DatePickerDialog(
                this, this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        edtHorarioView.setOnClickListener {
            TimePickerDialog(
                this, this, calendar.get(Calendar.MINUTE), calendar.get(Calendar.HOUR), true
            ).show()
        }
        binding.ibBack.setOnClickListener() {
            homeCall()
        }

        especialidadeSalao = binding.editServico //findViewById(R.id.edit)
        val listaEspecialidades: MutableList<String> = mutableListOf()
        val contextoEspecialidade = this
        var especialidade = retrofitEspecialidadeService().getEspecialidade()

        especialidade.enqueue(object : Callback<ArrayList<Especialidade>> {
            override fun onResponse(
                call: Call<ArrayList<Especialidade>>,
                response: Response<ArrayList<Especialidade>>
            ) {
                Log.i("xptoE", "Código HTTP: ${response.code().toString()}")
                Log.i("xptoE", response.body().toString())
                Log.i("xptoE", response.body()?.size.toString())

                val size = response.body()?.size.toString()
                val sizeInt = size.toInt()

                var especialidadeLista = Especialidade(null, null)

                for (i in 0..sizeInt - 1) {
                    especialidadeLista = response.body()?.get(i)?.let {
                        Especialidade(
                            idEspecialidade = it.idEspecialidade,
                            nomeEspecialidade = it.nomeEspecialidade
                        )
                    }!!

                    especialidadeLista.nomeEspecialidade?.let { listaEspecialidades.add(it) }
                    Log.i("xptoE", "Lista de especialidades: ${listaEspecialidades}")
                }

                especialidadeSalao.adapter = ArrayAdapter(
                    contextoEspecialidade,
                    R.layout.simple_spinner_dropdown_item,
                    listaEspecialidades
                )

                tipoServico = especialidade.toString()

                if (response.code().toInt() == 401) {
                    Toast.makeText(
                        applicationContext,
                        "Especialidade invalida",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                if (response.code().toInt() == 404) {
                    Toast.makeText(
                        applicationContext,
                        "Especialidade não encontrada",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
            }

            override fun onFailure(call: Call<ArrayList<Especialidade>>, t: Throwable?) {
                if (t != null) {
                    Log.i("xptoE", "Erro Especialidade ${t.message.toString()}")
                }
            }
        })

        especialidadeSalao.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                tipoServico = listaEspecialidades[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        unidSalao = binding.edtUnidadeSalao
        val listaUnidades: MutableList<String> = mutableListOf()
        val contexto = this
        val cidade = retrofitUnidadeService().getUnidade()

        cidade.enqueue(object : Callback<ArrayList<Unidade>> {
            override fun onResponse(
                call: Call<ArrayList<Unidade>>,
                response: Response<ArrayList<Unidade>>
            ) {
                Log.i("xptoU", "Código HTTP: ${response.code().toString()}")
                Log.i("xptoU", response.body().toString())
                Log.i("xptoU", response.body()?.size.toString())

                val size = response.body()?.size.toString()
                val sizeInt = size.toInt()

                var unidadeLista = Unidade(null, null, null, null)

                for (i in 0..sizeInt - 1) {
                    unidadeLista = response.body()?.get(i)?.let {
                        Unidade(
                            idUnidade = it.idUnidade,
                            nomeUnidade = it.nomeUnidade,
                            enderecoUnidade = it.enderecoUnidade,
                            telefoneUnidade = it.telefoneUnidade
                        )
                    }!!

                    unidadeLista.nomeUnidade?.let { listaUnidades.add(it) }
                    Log.i("xptoU", "Lista de unidades: ${listaUnidades}")
                }

                unidSalao.adapter =
                    ArrayAdapter(contexto, R.layout.simple_spinner_dropdown_item, listaUnidades)

                unidade = cidade.toString()

                if (response.code().toInt() == 401) {
                    Toast.makeText(
                        applicationContext,
                        "Produto invalido",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                if (response.code().toInt() == 404) {
                    Toast.makeText(
                        applicationContext,
                        "Produto não encontrado",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
            }

            override fun onFailure(call: Call<ArrayList<Unidade>>, t: Throwable?) {
                if (t != null) {
                    Log.i("xptoU", "Erro Produto ${t.message.toString()}")
                }
            }
        })

        unidSalao.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                unidade = listaUnidades[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        profissionalSp = binding.edtProfissionalSalao //findViewById(R.id.edit)
        val listaProfissionais: MutableList<String> = mutableListOf()
        val contextoProfissional = this
        var nome = retrofitProfissionalService().getProfissional()

        nome.enqueue(object : Callback<ArrayList<Profissional>> {
            override fun onResponse(
                call: Call<ArrayList<Profissional>>,
                response: Response<ArrayList<Profissional>>
            ) {
                Log.i("xptoP", "Código HTTP: ${response.code().toString()}")
                Log.i("xptoP", response.body().toString())
                Log.i("xptoP", response.body()?.size.toString())

                val size = response.body()?.size.toString()
                val sizeInt = size.toInt()

                var profissionalLista = Profissional(null, null)

                for (i in 0..sizeInt - 1) {
                    profissionalLista = response.body()?.get(i)?.let {
                        Profissional(
                            idProfissional = it.idProfissional,
                            nomeProfissional = it.nomeProfissional
                        )
                    }!!

                    profissionalLista.nomeProfissional?.let { listaProfissionais.add(it) }
                    Log.i("xptoP", "Lista de profissionais: ${listaProfissionais}")
                }

                profissionalSp.adapter =
                    ArrayAdapter(contextoProfissional, R.layout.simple_spinner_dropdown_item, listaProfissionais)

                profissional = nome.toString()

                if (response.code().toInt() == 401) {
                    Toast.makeText(
                        applicationContext,
                        "Profissional invalido",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                if (response.code().toInt() == 404) {
                    Toast.makeText(
                        applicationContext,
                        "Profissional não encontrado",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
            }

            override fun onFailure(call: Call<ArrayList<Profissional>>, t: Throwable?) {
                if (t != null) {
                    Log.i("xptoP", "Erro Profissional ${t.message.toString()}")
                }
            }
        })

        profissionalSp.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                profissional = listaProfissionais[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        binding.btnSave.setOnClickListener() {

            validateTask()
        }

    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
       calendar.set(year,month,day)
        displayFormatDate(calendar.timeInMillis)
    }

    override fun onTimeSet(p0: TimePicker?, min: Int,hour: Int) {

        displayFormatTime(min,hour)
    }

    private fun displayFormatTime(minutoView: Int,horaView: Int) {
        hora = if (horaView<10) "0"+horaView.toString() else horaView.toString()
        minuto=if (minutoView<10) "0"+minutoView.toString() else minutoView.toString()
        binding.editHorario.text= minuto+":"+hora
        horario = binding.editHorario.text.toString()
    }

    private fun displayFormatDate(timeStamp: Long) {
        binding.editDate.text= formatDate.format(timeStamp)

    }

    private fun homeCall() {
        val voltarHome = Intent(this, RetornaAgendamento::class.java)
        startActivity(voltarHome)
        finish()
    }

    private fun validateTask(){

            val descricao = binding.edtDescricao.text.toString().trim()
            val quantidade = binding.edtQuantidade.text.toString().trim()
            val date = binding.editDate.text

            if (tipoServico.isNotEmpty() || descricao.isNotEmpty() || quantidade.isNotEmpty()){
                idUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
                binding.progressBar.isVisible = true

                if (newServico) servico = Servico()
                servico.id = FirebaseHelper.getDatabase().push().key.toString()
                servico.status = "Aberto"
                servico.idUser = idUser
                servico.nomeCliente = nomeCliente
                servico.servico = tipoServico
                servico.descricao = descricao
                servico.quantidade = quantidade
                servico.unidade = unidade
                servico.profissional = profissional
                servico.data = date.toString() +" "+ horario.toString()

                saveTask()
            } else {
                Toast.makeText(
                    baseContext, " Por favor, preencher todos os campos",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun saveTask(){
        cadAgendamento =  hashMapOf(
            "id" to servico.id,
            "status" to servico.status,
            "idUser" to servico.idUser,
            "nomeCliente" to servico.nomeCliente,
            "servico" to servico.servico,
            "descricao" to servico.descricao,
            "quantidade" to servico.quantidade,
            "data" to servico.data,
            "unidade" to servico.unidade,
            "profissional" to servico.profissional,
            "especialidade" to servico.tipoServico

        )
        db.collection("agendamento").document(servico.id).set(cadAgendamento)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (newServico) { //nova tarefa
                        homeCall()
                        Toast.makeText(baseContext, "Tarefa salva com sucesso", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        binding.progressBar.isVisible = false
                        Toast.makeText(baseContext, "Produto atualizado", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(baseContext, "Erro ao salvar tarefa", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                binding.progressBar.isVisible = false
                Toast.makeText(baseContext, "Erro ao salvar tarefa", Toast.LENGTH_SHORT).show()
            }
    }
}