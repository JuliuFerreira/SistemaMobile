package com.example.how.ui.adapter.auth

import androidx.navigation.NavDirections
import com.example.how.AuthenticationDirections

public class RecoverAcountFragmentDirections private constructor() {
  public companion object {
    public fun actionGlobalHomeFragment(): NavDirections =
        AuthenticationDirections.actionGlobalHomeFragment()
  }
}
