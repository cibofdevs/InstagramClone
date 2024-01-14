package com.cibofdevs.instagramclone

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.cibofdevs.instagramclone.databinding.DialogLoginBinding

class LoginDialog(val callback: AuthCallback): DialogFragment() {

    lateinit var binding: DialogLoginBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            binding = DialogLoginBinding.inflate(it.layoutInflater)

            binding.loginButon.setOnClickListener {
                binding.errorField.visibility = View.GONE

                val username = binding.usernameET.text.toString()
                val password = binding.passwordET.text.toString()
                var msg = ""

                if (username.isEmpty()) msg += "Username cannot be empty\n"
                if (password.isEmpty()) msg += "Password cannot be empty\n"

                if (msg.isEmpty()) {
                    callback.onLogin(username, password)
                    this@LoginDialog.dismiss()
                } else {
                    msg = msg.substring(0, msg.length - 1)
                    binding.errorField.text = msg
                    binding.errorField.visibility = View.VISIBLE
                }
            }

            return AlertDialog.Builder(it)
                .setView(binding.root)
                .create()
        } ?: super.onCreateDialog(savedInstanceState)
    }
}