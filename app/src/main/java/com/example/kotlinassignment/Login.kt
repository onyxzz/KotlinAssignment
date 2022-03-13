package com.example.kotlinassignment

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.kotlinassignment.databinding.ActivityHomeBinding
import com.example.kotlinassignment.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var actionBar: ActionBar

    private lateinit var progressDialog:ProgressDialog

    private lateinit var firebaseAuth:FirebaseAuth

    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Login"

        progressDialog = ProgressDialog( this )
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
        verifyUser();

        binding.registerDirect.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        binding.buttonLogin.setOnClickListener{
            verifyData()
        }

    }

    private fun verifyData() {
        email = binding.email.text.toString().trim()
        password = binding.passwordLogin.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.error = "Email not found!"
        }
        else if (TextUtils.isEmpty(password)) {
            binding.passwordLogin.error = "Please enter password!"
        }
        else {
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Login Successful.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Home::class.java))
                finish()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Login Failed! Please try again.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun verifyUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            startActivity(Intent(this, Home::class.java))
            finish()
        }
    }
}