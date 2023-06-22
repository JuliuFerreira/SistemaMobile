package com.example.how.helper

import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import android.content.Context

open class BaseFragment: Fragment() {

    fun hideKeyboard(){
        val view = activity?.currentFocus
        if (view != null){

            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken,0)
        }

    }
}