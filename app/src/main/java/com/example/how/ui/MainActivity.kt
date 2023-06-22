package com.example.how.ui

// Importa as classes do projeto
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment
import com.example.how.R
import com.example.how.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    // Variável para armazenar a referência ao Binding da atividade.

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Infla o layout da atividade usando o Binding específico (ActivityMainBinding).

        setContentView(binding.root)
        // Define o layout da atividade como a raiz do layout inflado pelo Binding.

        supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        // Localiza o fragmento de navegação (NavHostFragment) na hierarquia de fragmentos usando o ID fornecido pelo Binding.
        // O resultado é convertido em um NavHostFragment para uso posterior.
    }
}