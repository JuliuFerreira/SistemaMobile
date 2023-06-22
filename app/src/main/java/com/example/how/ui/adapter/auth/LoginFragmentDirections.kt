package com.example.how.ui.adapter.auth

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.how.AuthenticationDirections
import com.example.how.R

public class LoginFragmentDirections private constructor() {
  public companion object {
    public fun actionLoginFragmentToRegisterFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_loginFragment_to_registerFragment)

    public fun actionLoginFragmentToRecoverAcountFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_loginFragment_to_recoverAcountFragment)

    public fun actionGlobalHomeFragment(): NavDirections =
        AuthenticationDirections.actionGlobalHomeFragment()
  }
}
