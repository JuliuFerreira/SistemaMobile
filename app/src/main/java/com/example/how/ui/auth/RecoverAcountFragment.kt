package com.example.how.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.how.R
import com.example.how.databinding.FragmentRecoverAcountBinding
import com.example.how.helper.BaseFragment
import com.example.how.helper.FirebaseHelper
import com.example.how.helper.initToolbar
import com.example.how.helper.showBottomSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RecoverAcountFragment : BaseFragment() {

    private var _binding: FragmentRecoverAcountBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoverAcountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)

        auth = Firebase.auth

        initClicks()
    }

    private fun initClicks() {
        binding.btnSend.setOnClickListener { validateData() }
    }

        private fun validateData(){
            val email = binding.edtEmail.text.toString().trim()

            if (email.isNotEmpty()){

                hideKeyboard()

                binding.progresssBar.isVisible = true

                recoverAccountUser(email)

            } else{

                showBottomSheet(
                    message = R.string.text_email_empty_recover_account_fragment
                )
            }
        }

    private fun recoverAccountUser(email: String){

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                   showBottomSheet(
                       message = R.string.text_email_send_sucess_recover_account_fragment
                   )
                    findNavController().navigate(R.id.action_recoverAcountFragment_to_loginFragment)

                }else{

                    showBottomSheet(
                        message = FirebaseHelper.validError(task.exception?.message ?:"")
                    )

                }

                binding.progresssBar.isVisible = false

            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}