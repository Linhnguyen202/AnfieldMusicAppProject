package com.example.anfieldmusicapp

import android.content.Intent
import android.media.MediaPlayer.OnCompletionListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.anfieldmusicapp.databinding.ActivityLoginScreenBinding
import com.example.anfieldmusicapp.model.User
import com.example.anfieldmusicapp.share.sharePreferenceUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginScreen : AppCompatActivity() {
    lateinit var binding : ActivityLoginScreenBinding
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = sharePreferenceUtils.isSharedPreferencesExist(this,"USER","USER_VALUE")
        Log.d("user",user.toString())
        auth = FirebaseAuth.getInstance()
        addEvents()

    }

    private fun addEvents() {
        binding.loginBtn.setOnClickListener {
            LoginUser()
        }
        binding.signupTitle.setOnClickListener{
            val intent = Intent(this@LoginScreen,RegisterScreen::class.java)
            startActivity(intent)

        }
        binding.emailEditText.setOnFocusChangeListener { view, b ->
            if(view.isFocused){
                binding.emailContainer.error = ""
                binding.emailContainer.isErrorEnabled = false
            }

        }
        binding.passEditText.setOnFocusChangeListener { view, b ->
            if(view.isFocused){
                binding.passContainer.error = ""
                binding.passContainer.isErrorEnabled = false
            }

        }
    }
    private fun validateEmail() : Boolean{
        val email = binding.emailEditText.text.toString()
        var error : String? = null
        if(email.isNullOrEmpty()){
            error = "Email is required"
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            error = "Email is not valid"
        }
        if(error != null){
            binding.emailContainer.error = error
            binding.emailContainer.isErrorEnabled = true

        }
        return error != null
    }
    private fun validatePass() : Boolean{
        val pass = binding.passEditText.text.toString()
        var error : String? = null
        if(pass.isNullOrEmpty()){
            error = "Password is required"
        }
        else if(pass.length < 6){
            error = "Password at least 6 characters"
        }
        if(error != null){
            binding.passContainer.error = error
            binding.passContainer.isErrorEnabled = true

        }
        return error != null
    }
    private fun validate() : Boolean{
        var validateValue = false
        if(validateEmail()) validateValue = true
        if(validatePass()) validateValue = true
        return validateValue

    }
    private fun LoginUser(){
        if(!validate()){
            val email = binding.emailEditText.text.toString()
            val pass = binding.passEditText.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                try{
                    withContext(Dispatchers.Main){
                        binding.progessBar.visibility = View.VISIBLE
                        binding.signInTitle.visibility = View.GONE
                    }
                    auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
                        if(it.isSuccessful){
                            binding.progessBar.visibility = View.GONE
                            binding.signInTitle.visibility = View.VISIBLE
                            if(auth.currentUser != null){
                                val user = User(auth.currentUser!!.uid, auth.currentUser!!.displayName.toString(),auth.currentUser!!.email.toString(),auth.currentUser!!.photoUrl.toString())
                                sharePreferenceUtils.saveUser(user,this@LoginScreen)
                                val intent = Intent(this@LoginScreen,MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                    }.await()
                }
                catch (e : FirebaseAuthInvalidCredentialsException){
                    withContext(Dispatchers.Main){
                        binding.progessBar.visibility = View.GONE
                        binding.signInTitle.visibility = View.VISIBLE
                        Toast.makeText(this@LoginScreen,"Invalid password",Toast.LENGTH_LONG).show()
                    }

                }
                catch (e : FirebaseAuthInvalidUserException){
                    withContext(Dispatchers.Main){
                        binding.progessBar.visibility = View.GONE
                        binding.signInTitle.visibility = View.VISIBLE
                        Toast.makeText(this@LoginScreen,"User not exits",Toast.LENGTH_LONG).show()
                    }

                }



            }
        }

    }

}