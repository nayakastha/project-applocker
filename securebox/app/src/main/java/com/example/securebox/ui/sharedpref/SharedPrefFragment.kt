package com.example.securebox.ui.sharedpref

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.securebox.R
import com.example.securebox.util.Utility.displaySnackBar
import kotlinx.android.synthetic.main.fragment_shared_pref.*

class SharedPrefFragment : Fragment() {

    private val viewModel by viewModels<SharedPrefViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_shared_pref, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.apply {
            getUserName()
            getUserEmail()
        }
        fab_save_pref.setOnClickListener {
            if (!TextUtils.isEmpty(txtUserId.text) && !TextUtils.isEmpty(txtEmail.text)) {
                viewModel.userName.value = txtUserId.text.toString()
                viewModel.userEmail.value = txtEmail.text.toString()
                viewModel.saveUserData()
            } else {
                displaySnackBar("Please fill all the fields!", requireView())
            }

        }
    }

    private fun observeViewModel() {
        viewModel.userName.observe(viewLifecycleOwner, Observer {
            txtUserId.setText(it)
        })

        viewModel.userEmail.observe(viewLifecycleOwner, Observer {
            txtEmail.setText(it)
        })

        viewModel.snackBarMsg.observe(viewLifecycleOwner, Observer {
            displaySnackBar(it, requireView())
        })

    }
}