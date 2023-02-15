package dev.korab.pia11shopping

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel : ViewModel() {

    val errorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


    fun login(email : String, password : String) {

        if (email == "") {
            errorMessage.value = "Fyll in e-post"
            return
        }
        if (password == "") {
            errorMessage.value = "Fyll in lösenord"
            return
        }

        Firebase.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {task ->
            if (task.isSuccessful == false) {
                errorMessage.value = "Fel inloggning"
            }
        }

    }

    fun register(email : String, password : String) {

        if (email == "") {
            errorMessage.value = "Fyll in e-post"
            return
        }
        if (password == "") {
            errorMessage.value = "Fyll in lösenord"
            return
        }

        Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task ->

            if (task.isSuccessful == false) {
                errorMessage.value = task.exception!!.localizedMessage!!
            }

        }
    }

}