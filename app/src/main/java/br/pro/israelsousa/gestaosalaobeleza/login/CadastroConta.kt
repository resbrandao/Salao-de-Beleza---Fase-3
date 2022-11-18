package br.pro.israelsousa.gestaosalaobeleza.login

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import br.pro.israelsousa.gestaosalaobeleza.R
import br.pro.israelsousa.gestaosalaobeleza.databinding.ActivityCadastroContaBinding
import br.pro.israelsousa.gestaosalaobeleza.home.RetornaAgendamento
import br.pro.israelsousa.gestaosalaobeleza.model.Unidade
import br.pro.israelsousa.gestaosalaobeleza.retrofit.retrofitUnidadeService
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class CadastroConta : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroContaBinding
    var idUser: String = ""
    var tipoUser:String = ""
    var nomeCompleto: String = ""
    var cadUsuario: HashMap<String,Any> = hashMapOf()
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    lateinit var unidSalao: Spinner
    private var unidade: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroContaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.action_bar_layout)

        binding.toolbar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val voltarHome = Intent(this, Login::class.java)
            startActivity(voltarHome)
            finish()
        }

/*        unidSalao = binding.edtUnidadeSalaoConta //findViewById(R.id.edit)
        var cidade = arrayOf("São Paulo", "Belo Horizonte")
        unidSalao.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,cidade)

        unidSalao.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                unidade = cidade[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
*/
        unidSalao = binding.edtUnidadeSalaoConta //findViewById(R.id.edit)
        val listaUnidades: MutableList<String> = mutableListOf()
        val contexto = this
        val cidade = retrofitUnidadeService().getUnidade()

        Log.i("xpto", "Cidade: ${cidade}")

        cidade.enqueue(object : Callback<ArrayList<Unidade>> {
            override fun onResponse(
                call: Call<ArrayList<Unidade>>,
                response: Response<ArrayList<Unidade>>
            ) {
                Log.i("xpto", "Código HTTP: ${response.code().toString()}")
                Log.i("xpto", response.body().toString())
                Log.i("xpto", response.body()?.size.toString())

                val size = response.body()?.size.toString()
                val sizeInt = size.toInt()

                var unidadeLista = Unidade(null, null, null, null)

                for (i in 0..sizeInt - 1) {
                    Log.i("xpto", "Entrou no for")
                    unidadeLista = response.body()?.get(i)?.let {
                        Unidade(
                            idUnidade = it.idUnidade,
                            nomeUnidade = it.nomeUnidade,
                            enderecoUnidade = it.enderecoUnidade,
                            telefoneUnidade = it.telefoneUnidade
                        )
                    }!!

                    unidadeLista.nomeUnidade?.let { listaUnidades.add(it) }
                    Log.i("xpto", "Lista de unidades: ${listaUnidades}")
                }

                unidSalao.adapter =
                    ArrayAdapter(contexto, android.R.layout.simple_spinner_dropdown_item, listaUnidades)

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
                    Log.i("xpto", "Erro Produto ${t.message.toString()}")
                }
            }
        })

        unidSalao.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                unidade = listaUnidades[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        binding.btnRegister.setOnClickListener { view ->

            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val fullName = binding.edtNomeCompleto.text.toString()


            if (email.isEmpty() || password.isEmpty()|| fullName.isEmpty()) {
                Toast.makeText(baseContext, "Por favor preencher todos os campos", Toast.LENGTH_SHORT).show()
            }else {

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { cadastro ->
                        if (cadastro.isSuccessful) {
                            Toast.makeText(baseContext, "Usuário cadastrado", Toast.LENGTH_SHORT).show()
                            binding.edtEmail.setText("")
                            binding.edtPassword.setText("")

                            nomeCompleto = fullName
                            binding.progressBar.isVisible = false
                            idUser = cadastro.getResult().user?.uid.toString()
                            onRadioButtonClicked(view)
                            db.collection("users").document(idUser!!).set(cadUsuario)
                            navegarTelaPrincipal()
                        }
                    }.addOnFailureListener { exception ->
                        val mensagemErro = when (exception) {
                            is FirebaseAuthWeakPasswordException -> "Digite uma senha com minimo 6 caracteres"
                            is FirebaseAuthInvalidCredentialsException -> "Digite um e-mail valido"
                            is FirebaseAuthUserCollisionException -> "Usuario ja cadastrado"
                            is FirebaseNetworkException -> "Sem conexão com a internet"
                            else -> "Erro ao cadastrar"
                        }
                        Toast.makeText(baseContext, mensagemErro, Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }


private fun navegarTelaPrincipal(){
    val intent = Intent(this, RetornaAgendamento::class.java)
    startActivity(intent)
    finish()
}


    fun onRadioButtonClicked(view: View) {
        val edtConta = binding.edtUnidadeSalaoConta
        val textConta = binding.textViewUnidadeConta

        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.cliente ->
                    if (checked) {
                        tipoUser = "Cliente"
                        edtConta.visibility = View.GONE
                        textConta.visibility = View.GONE
                    }
                R.id.empresa ->
                    if (checked) {
                        tipoUser = "Empresa"
                        edtConta.visibility = View.VISIBLE
                        textConta.visibility = View.VISIBLE
                    }
            }



        }

        cadUsuario =  hashMapOf(
            "tipo" to tipoUser,
            "idUser" to idUser,
            "nomeCompleto" to nomeCompleto,
            "unidadeSalao" to unidade
        )


    }
}
