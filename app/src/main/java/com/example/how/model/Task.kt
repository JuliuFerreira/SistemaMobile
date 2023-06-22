package com.example.how.model


import android.os.Parcelable
import com.example.how.helper.FirebaseHelper
import kotlinx.parcelize.Parcelize


@Parcelize
data class Task(
    var id: String = "",
    var description: String = "",
    var status: Int = 0
) : Parcelable{
    // Data class que representa uma tarefa.

    init {
        this.id = FirebaseHelper.getDatabase().push().key ?: ""
        // Inicialização para gerar um ID único para a tarefa usando a classe FirebaseHelper.
        // Se não for possível obter o ID do banco de dados, uma string vazia é atribuída.
    }
}
