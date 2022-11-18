package br.pro.israelsousa.gestaosalaobeleza

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import br.pro.israelsousa.gestaosalaobeleza.databinding.ActivitySplashBinding
import br.pro.israelsousa.gestaosalaobeleza.login.Login
import com.google.firebase.auth.FirebaseAuth

class Splash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.action_bar_layout)

        Handler(Looper.getMainLooper()).postDelayed(this::checkAuth, 1000)

    }

    private fun checkAuth(){
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}