package com.example.how.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.how.R
import com.example.how.databinding.FragmentFormTaskBinding
import com.example.how.helper.BaseFragment
import com.example.how.helper.FirebaseHelper
import com.example.how.helper.initToolbar
import com.example.how.model.Task


class FormTaskFragment : BaseFragment() {

    private val args: FormTaskFragmentArgs by navArgs()

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: Task
    private var newTask: Boolean = true
    private var statusTask: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)

        initListeners()

        getArgs()
    }

    private fun getArgs(){

        args.task.let {
            if (it != null) {
                task = it

                configTask()
            }
        }
    }

    private fun configTask(){ // Configura novo status no botão editar
        newTask = false
        statusTask = task.status
        binding.textToolbar.text = "Editando tarefa..."

        binding.edtDescription.setText(task.description)

        setStatus()
    }

    private fun setStatus(){ // Seta o status da tarefa

        binding.radioGroup.check(
            when (task.status){

                0 ->{
                    R.id.rbTodo
                }
                1 ->{
                    R.id.rbDoing
                }
                else ->{
                    R.id.rbDone
                }
            }
        )

    }

    private fun initListeners() { // Recebe as ações

        binding.btnSave.setOnClickListener { validateData() }

        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            statusTask = when(id){
                R.id.rbTodo -> 0
                R.id.rbDoing -> 1
                else -> 2

            }

        }
    }

    private fun validateData(){ // Valida se a descrição e o status estão preenchidos

        val description = binding.edtDescription.text.toString().trim()

        if (description.isNotEmpty()) {

            hideKeyboard()

            binding.progresssBar.isVisible = true

            if(newTask) task = Task()
            task.description = description
            task.status = statusTask

            saveTask()

        }else{

            Toast.makeText(
                requireContext(),
                "Digite uma tarefa",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveTask(){ //salvar no banco de dados (firebase)

        FirebaseHelper
            .getDatabase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(task.id)
            .setValue(task)
            .addOnCompleteListener {task ->
                if (task.isSuccessful){
                    if (newTask){ // Adiciona nova tarefa
                        findNavController().popBackStack()
                        Toast.makeText(
                            requireContext(),
                            "Tarefa salva com sucesso.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{ // Editar tarefa
                        binding.progresssBar.isVisible = false
                        Toast.makeText(
                            requireContext(),
                            "Tarefa atualizada com sucesso.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }else{
                    Toast.makeText(requireContext(), "Erro ao salvar a tarefa.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                binding.progresssBar.isVisible = false
                Toast.makeText(requireContext(), "Erro ao salvar a tarefa.", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() { // Limpa os campos
        super.onDestroyView()
        _binding = null
    }
}