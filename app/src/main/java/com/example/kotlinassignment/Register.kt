package com.example.kotlinassignment

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.kotlinassignment.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var actionBar: ActionBar

    private lateinit var progressDialog: ProgressDialog

    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var password = ""
    private var cpassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Register"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        progressDialog = ProgressDialog( this )
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Creating Account...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginDirect.setOnClickListener{
            startActivity(Intent(this, Login::class.java))
        }

        binding.buttonRegister.setOnClickListener{
            verifyData()
        }
    }

    private fun verifyData() {
        email = binding.emailRegister.text.toString().trim()
        password = binding.passwordRegister.text.toString().trim()
        cpassword = binding.confirmPassword.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailRegister.error = "This email is already used!"
        }
        else if (TextUtils.isEmpty(password)) {
            binding.passwordRegister.error = "Please enter password."
        }
        else if (password.length < 6) {
            binding.passwordRegister.error = "Password must contain at least 6 character!"
        }
        else if (TextUtils.isEmpty(cpassword)) {
            binding.confirmPassword.error = "Please repeat your password."
        }
        else if (password !== cpassword) {
            binding.confirmPassword.error = "Passwords do not match!"
        }
        else {
            firebaseRegister()
        }
    }

    private fun firebaseRegister() {
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Register Succesful!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Home::class.java))
                finish()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Register Failed!", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}