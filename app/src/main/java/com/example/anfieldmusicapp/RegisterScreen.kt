package com.example.anfieldmusicapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.anfieldmusicapp.databinding.ActivityRegisterScreenBinding
import com.example.anfieldmusicapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RegisterScreen : AppCompatActivity() {
    lateinit var binding : ActivityRegisterScreenBinding // view binding
    lateinit var auth : FirebaseAuth // authentication
    lateinit var db : FirebaseDatabase // realtime db
    lateinit var reference : DatabaseReference // link api /https://anfieldauth-default-rtdb.firebaseio.com/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // add binding vào view của activity
        binding = ActivityRegisterScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Khởi tạo
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        reference = db.getReference("User") // https://anfieldauth-default-rtdb.firebaseio.com/User
//        addEvents()

    }








}