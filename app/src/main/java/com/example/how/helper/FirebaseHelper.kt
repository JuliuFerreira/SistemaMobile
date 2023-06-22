package com.example.how.helper

import com.example.how.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseHelper {
    // Classe auxiliar para interagir com o Firebase.

    companion object {

        fun getDatabase() = FirebaseDatabase.getInstance().reference

        private fun getAuth() = FirebaseAuth.getInstance()

        fun getIdUser() = getAuth().uid

        fun isAutenticated() = getAuth().currentUser != null

        fun validError(error: String) : Int {
            // Implementação da função de validação do erro e retorno do código de erro correspondente.

            return when{
                error.contains("There is no user record corresponding to this identifier") ->{
                    R.string.account_not_registered_register_fragment
                }
                error.contains("The email address is badly formatted") ->{
                    R.string.invalid_email_register_fragment
                }
                error.contains("The password is invalid or the user does not have a password") ->{
                    R.string.invalid_password_register_fragment
                }
                error.contains("The email address is already in use by another account") ->{
                    R.string.email_in_use_register_fragment
                }
                error.contains("Password should be at least 6 characters") ->{

                    R.string.strong_password_register_fragment
                }
                else -> {
                    R.string.error_generic
                }
            }
        }
    }
}