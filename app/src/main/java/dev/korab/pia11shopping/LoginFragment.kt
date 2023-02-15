package dev.korab.pia11shopping

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.korab.pia11shopping.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    val model by viewModels<LoginViewModel> ()

    var _binding : FragmentLoginBinding? = null
    val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login, container, false)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Här kollar vi om användaren är inloggad


        val errorObserver = Observer<String> {errorMess ->
            Toast.makeText(requireContext(), errorMess, Toast.LENGTH_LONG).show()
        }
        model.errorMessage.observe(viewLifecycleOwner, errorObserver)

        //Knapp som hanterar inloggningskod till Firebase
        binding.loginButton.setOnClickListener {

            val userEmail = binding.loginEmailET.text.toString()
            val userPassword = binding.loginPasswordET.text.toString()




            model.login(userEmail, userPassword)

        }

        //Knapp som hanterar registreringskod till Firebase
        view.findViewById<Button>(R.id.registerButton).setOnClickListener {

            val userEmail = binding.loginEmailET.text.toString()
            val userPassword = binding.loginPasswordET.text.toString()

            model.register(userEmail, userPassword)

        }

    }

}