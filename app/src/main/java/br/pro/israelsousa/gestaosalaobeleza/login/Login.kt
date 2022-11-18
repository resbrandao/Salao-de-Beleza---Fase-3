package br.pro.israelsousa.gestaosalaobeleza.login

import android.app.ActionBar
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.test.platform.app.InstrumentationRegistry
import br.pro.israelsousa.gestaosalaobeleza.R
import br.pro.israelsousa.gestaosalaobeleza.databinding.ActivityLoginBinding
import br.pro.israelsousa.gestaosalaobeleza.home.RetornaAgendamento
import br.pro.israelsousa.gestaosalaobeleza.repository.RetornaCliente
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

       supportActionBar?.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM)
       supportActionBar?.setCustomView(R.layout.action_bar_layout)


        binding.btnLogin.setOnClickListener { view ->

            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (email.isNotEmpty()) {
                if (password.isNotEmpty()) {

                    validaCampo(email, password)

                } else {
                    Toast.makeText(baseContext, "Preencha o campo senha", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(baseContext, "Preencha o campo e-mail", Toast.LENGTH_SHORT).show()
            }
        }


        binding.btnRegister.setOnClickListener{
            val intent = Intent(this, CadastroConta::class.java)
            startActivity(intent)
        }

        binding.btnRecover.setOnClickListener{
            val intent = Intent(this, RecuperaConta::class.java)
            startActivity(intent)
        }

    }

    private fun validaCampo(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        var tipoUser: String
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { autenticacao ->
                if (autenticacao.isSuccessful) {
                    tipoUser =  auth?.uid.toString()
                    RetornaCliente().getCliente(this)
                    navegarTelaPrincipal()
                }
            }.addOnFailureListener {
                Toast.makeText(baseContext, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show()
            }

    }

    private fun navegarTelaPrincipal(){
        val intent = Intent(this,RetornaAgendamento::class.java)
        startActivity(intent)
        finish()
    }


}