package dev.korab.pia11shopping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Firebase.auth.addAuthStateListener {
            if(Firebase.auth.currentUser != null) {
                supportFragmentManager.commit {
                    replace(R.id.mainFragContainer, ShoppingFragment())
                    findViewById<FragmentContainerView>(R.id.mainFragContainer).visibility = View.VISIBLE
                }

            } else {
                supportFragmentManager.commit {
                    replace(R.id.mainFragContainer, LoginFragment())
                    findViewById<FragmentContainerView>(R.id.mainFragContainer).visibility = View.VISIBLE
                }

            }
        }

    }
}