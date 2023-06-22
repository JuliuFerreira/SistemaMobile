package com.example.how.ui

// Tela das tarefas "A fazer".

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
import com.example.how.databinding.FragmentTodoBinding
import com.example.how.helper.FirebaseHelper
import com.example.how.model.Task
import com.example.how.ui.adapter.TaskAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class TodoFragment : Fragment() { //

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    private val tasklist = mutableListOf<Task>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClicks()

        getTasks()
    }
    private fun initClicks(){
        binding.fabAdd.setOnClickListener {
            val action = HomeFragmentDirections
                .actionHomeFragmentToFormTaskFragment(null)
            findNavController().navigate(action)
        }

    }
    private fun getTasks(){

        FirebaseHelper
            .getDatabase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        tasklist.clear()
                        for (snap in snapshot.children) {
                            val task = snap.getValue(Task::class.java) as Task

                            if (task.status == 0) tasklist.add(task)
                        }


                        tasklist.reverse()
                        initAdapter()
                    }

                    tasksEmpty()

                    binding.progresssBar.isVisible = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Erro", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun tasksEmpty(){
        binding.info.text = if(tasklist.isEmpty()){
            getText(R.string.text_task_list_empty_todo_fragment)
        }else {
                ""
        }
    }

    private fun initAdapter(){
        binding.rvTask.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTask.setHasFixedSize(true)
        taskAdapter = TaskAdapter(requireContext(), tasklist) {task, select ->
            optionSelect(task, select)
        }
        binding.rvTask.adapter = taskAdapter
    }
    private fun optionSelect(task: Task,select: Int ){ // Navega para as telas de editar e excluir.
        when(select){
            TaskAdapter.SELECT_REMOVE -> {
                deleteTask(task)
            }
            TaskAdapter.SELECT_EDIT -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToFormTaskFragment(task)
                findNavController().navigate(action)
            }
            TaskAdapter.SELECT_NEXT ->{
                task.status = 1
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
    private fun deleteTask(task: Task){ //REMOVE A TAREFA SELECIONADA.
        FirebaseHelper
            .getDatabase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(task.id)
            .removeValue()

        tasklist.remove(task)
        taskAdapter.notifyDataSetChanged()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}