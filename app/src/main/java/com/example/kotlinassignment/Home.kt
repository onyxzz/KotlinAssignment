package com.example.kotlinassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.kotlinassignment.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var actionBar: ActionBar

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Home"

        firebaseAuth = FirebaseAuth.getInstance()
        verifyUser()

        binding.buttonLogOut.setOnClickListener {
            firebaseAuth.signOut()
            verifyUser()
        }
    }

    private fun verifyUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            val email = firebaseUser.email
            binding.username.text = email
        }
        else {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}