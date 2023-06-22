package com.example.how.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.how.R
import com.example.how.databinding.FragmentResetPasswordBinding
import com.example.how.helper.BaseFragment
import com.example.how.helper.FirebaseHelper
import com.example.how.helper.initToolbar
import com.example.how.helper.showBottomSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ResetPasswordFragment : BaseFragment() {
    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)

        auth = Firebase.auth

        initClicks()
    }

    private fun initClicks() {
        binding.btnResetPassword.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val emailAddress = user?.email

            if (emailAddress != null) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showBottomSheet(
                                message = R.string.text_message_reset_password_fragment
                            )
                            // Email de redefinição de senha enviado com sucesso
                            FirebaseAuth.getInstance().signOut()
                            findNavController().navigate(R.id.action_resetPasswordFragment3_to_authentication)
                            // Instrua o usuário a verificar o email e redefinir a senha
                        } else {
                            showBottomSheet(
                                message = FirebaseHelper.validError(task.exception?.message ?:"")
                            )
                        }
                    }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}