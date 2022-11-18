package br.pro.israelsousa.gestaosalaobeleza.login

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.pro.israelsousa.gestaosalaobeleza.R
import br.pro.israelsousa.gestaosalaobeleza.databinding.ActivityRecuperaContaBinding
import com.google.firebase.auth.FirebaseAuth

class RecuperaConta : AppCompatActivity() {
   private lateinit var binding: ActivityRecuperaContaBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecuperaContaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.action_bar_layout)

        binding.btnSend.setOnClickListener { view ->
            val email = binding.edtEmail.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(baseContext, "Preencha seu e-mail", Toast.LENGTH_SHORT).show()
            } else {
                auth.sendPasswordResetEmail(email).addOnCompleteListener() { recover ->
                    if (recover.isSuccessful) {
                        Toast.makeText(baseContext, "Verifique seu e-mail", Toast.LENGTH_SHORT).show()
                        binding.edtEmail.setText("")
                    }
                }.addOnFailureListener {

                }
            }

        }

        //val btn_logout = findViewById<ImageButton>(R.id.ibLogout)
        binding.toolbar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val voltarHome = Intent(this, Login::class.java)
            startActivity(voltarHome)
            finish()
        }
    }
}