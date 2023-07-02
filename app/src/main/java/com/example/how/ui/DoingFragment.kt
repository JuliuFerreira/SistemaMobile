package com.example.how.ui

// Tela das tarefas "Fazendo".

//Importa as classes do projeto
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.how.R
import com.example.how.databinding.FragmentDoingBinding
import com.example.how.helper.FirebaseHelper
import com.example.how.model.Task
import com.example.how.ui.adapter.TaskAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class DoingFragment : Fragment() {
    /**
    Essa é uma classe em Kotlin no Android Studio que representa um fragmento chamado
    "DoingFragment".
    Ela estende a classe Fragment e fornece métodos e funcionalidades específicas para lidar com
    o ciclo de vida do fragmento.
    */

    private var _binding: FragmentDoingBinding? = null
    // Variável para armazenar a referência para o objeto de binding do fragmento

    private val binding get() = _binding!!
    // Propriedade para acessar o objeto de binding de forma conveniente


    private lateinit var taskAdapter: TaskAdapter
    // Adaptador responsável por exibir a lista de tarefas no RecyclerView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Sobrescrita do método onCreateView() para inflar e retornar a raiz do layout do fragmento.

        _binding = FragmentDoingBinding.inflate(inflater, container, false)
        // Infla o layout do fragmento usando o Binding específico (FragmentDoingBinding).

        return binding.root
        // Retorna a raiz do layout do fragmento, que é representada pelo atributo root do Binding.

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Executa as ações necessárias ao criar a visualização do fragmento

        initAdapter()
        // Inicializa o adaptador para exibir as tarefas
        getTasks()
        // Chamada do método getTasks()
    }
    private fun getTasks(){
        // Recupera a referência do banco de dados do Firebase
        FirebaseHelper
            .getDatabase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        val tasklist = mutableListOf<Task>()
                        // Limpa a lista de tarefas antes de preenchê-la novamente


                        for (snap in snapshot.children) {
                            // Percorre os dados do snapshot para obter as tarefas


                            val task = snap.getValue(Task::class.java) as Task
                            // Converte o snapshot em um objeto do tipo Task


                            if (task.status == 1) tasklist.add(task)
                            // Converte o snapshot em um objeto do tipo Task

                        }

                        tasklist.reverse()
                        // Inverte a ordem da lista de tarefa
                        taskAdapter.submitList(tasklist)
                        tasksEmpty(tasklist)

                    }


                    binding.progresssBar.isVisible = false
                    // Torna a barra de progresso invisível

                }

                override fun onCancelled(error: DatabaseError) {

                    Toast.makeText(requireContext(), "Erro", Toast.LENGTH_SHORT).show()
                    // Exibe um Toast de erro em caso de falha no carregamento dos dados
                }


            })
    }

    private fun tasksEmpty(tasklist: List<Task>){
        binding.info.text = if(tasklist.isEmpty()){
            getText(R.string.text_task_list_empty_doing_fragment)
        }else {
            ""
        }
    }
    private fun initAdapter(){
        /**
         * Inicializa o adaptador para exibir as tarefas.
         * Configura o gerenciador de layout, tamanho fixo e adaptador para a exibição das tarefas.
         * Define um callback para tratar as ações do usuário nas tarefas.
         */
        binding.rvTask.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTask.setHasFixedSize(true)
        taskAdapter = TaskAdapter(requireContext()) {task, select ->
            optionSelect(task, select)
        }
        binding.rvTask.adapter = taskAdapter
    }
    private fun optionSelect(task: Task,select: Int ){
        // Executa ação com base na opção selecionada

        when(select){
            TaskAdapter.SELECT_REMOVE -> {
                // Executa ação de exclusão da tarefa

                deleteTask(task)
            }
            TaskAdapter.SELECT_EDIT -> {
                // Executa ação de edição da tarefa, navegando para o formulário de tarefa

                val action = HomeFragmentDirections
                    .actionHomeFragmentToFormTaskFragment(task)
                findNavController().navigate(action)
            }
            TaskAdapter.SELECT_BACK ->{
                // Define o status da tarefa como 0 (voltar)

                task.status = 0
                // Atualiza a tarefa no banco de dados

                updateTask(task)
            }
            TaskAdapter.SELECT_NEXT ->{
                // Define o status da tarefa como 2 (avançar)

                task.status = 2
                // Atualiza a tarefa no banco de dados
                updateTask(task)
            }

        }
    }
    private fun updateTask(task: Task){ //salvar no banco de dados (firebase)

        FirebaseHelper
            .getDatabase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(task.id)
            .setValue(task)
            .addOnCompleteListener {task ->
                if (task.isSuccessful){
                    Toast.makeText(
                        requireContext(),
                        "Tarefa atualizada com sucesso.",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    Toast.makeText(requireContext(), "Erro ao salvar a tarefa.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                binding.progresssBar.isVisible = false
                Toast.makeText(requireContext(), "Erro ao salvar a tarefa.", Toast.LENGTH_SHORT).show()
            }
    }
    private fun deleteTask(task: Task) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Remover tarefa")
        alertDialogBuilder.setMessage("Você tem certeza que deseja remover esta tarefa?")
        alertDialogBuilder.setPositiveButton("Remover") { _, _ ->

            confirmDeleteTask(task)
        }

        alertDialogBuilder.setNegativeButton("Cancelar", null)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
    private fun confirmDeleteTask(task: Task) {
        FirebaseHelper
            .getDatabase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(task.id)
            .removeValue()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(
                        requireContext(),
                        "Tarefa removida com sucesso.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Não foi possivel remover a tarefa.", Toast.LENGTH_SHORT)
                    .show()
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
        // Libera a referência para o objeto de binding do fragmento, evitando vazamentos de memória
    }


}