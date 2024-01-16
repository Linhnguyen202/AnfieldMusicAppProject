package com.example.anfieldmusicapp.layout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.anfieldmusicapp.LoginScreen
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.RegisterScreen
import com.example.anfieldmusicapp.databinding.FragmentStartRegisterScreenBinding
import com.example.anfieldmusicapp.model.User
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class StartRegisterScreen : Fragment() {
    lateinit var binding : FragmentStartRegisterScreenBinding

    val auth by lazy {
        (activity as RegisterScreen).auth // lấy biến auth cua tk cha registerScreen
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentStartRegisterScreenBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEvents()
    }

    private fun addEvents() {

        // submit đăng kí
        binding.submitBtn.setOnClickListener {
            registerUser() // hàm đăng kí
        }

        // hàm chuyển về trang đăng nhập
        binding.signInTitle.setOnClickListener {
            findNavController().navigate(R.id.action_startRegisterScreen_to_loginScreen)
        }

        // kiểm tra ô email text có đang thay đổi không
        binding.emailEditText.setOnFocusChangeListener { view, b ->
            if(view.isFocused){
                binding.emailContainer.error = "" // cho error bằng chuỗi rỗng
                binding.emailContainer.isErrorEnabled = false // tắt error
            }

        }
        // kiểm tra ô password text có đang thay đổi không
        binding.passEditText.setOnFocusChangeListener { view, b ->
            if(view.isFocused){
                binding.passContainer.error = ""
                binding.passContainer.isErrorEnabled = false
            }

        }

        // kiểm tra ô confirm password text có đang thay đổi không
        binding.confirmPassEditText.setOnFocusChangeListener { view, b ->
            if(view.isFocused){
                binding.confirmPassContainer.error = ""
                binding.confirmPassContainer.isErrorEnabled = false
            }

        }
    }


    private fun validateEmail() : Boolean{
        val email = binding.emailEditText.text.toString() // lất dữ liệu text của email Edittext

        var error : String? = null // THonng báo lỗi

        // kiểm tra email
        if(email.isNullOrEmpty()){ // kiểm tra email rỗng
            error = "Email is required"
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){ // kiểm tra định dạng
            error = "Email is not valid"
        }

        // kiểm tra thông báo lỗi (error) có null
        if(error != null){
            binding.emailContainer.error = error
            binding.emailContainer.isErrorEnabled = true

        }

        return error == null
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
        return error == null
    }

    private fun validateConfirmPass() : Boolean{
        val pass = binding.passEditText.text.toString()
        val confirmPass = binding.confirmPassEditText.text.toString()

        var error : String? = null

        if(pass.isNullOrEmpty()){
            error = "Confirm Password is required"
        }
//        else if(confirmPass.equals(pass)){
//            error = "Confirm Password is not same Password"
//        }

        if(error != null){
            binding.confirmPassContainer.error = error
            binding.confirmPassContainer.isErrorEnabled = true

        }

        return error == null
    }

    private fun registerUser(){
        if(validateEmail() && validatePass() && validateConfirmPass()){
           val email = binding.emailEditText.text.toString()
            val pass = binding.passEditText.text.toString()
            val bundle = bundleOf(
                "Email" to email.toString(),
                "Password" to pass.toString()
            )
            findNavController().navigate(R.id.action_startRegisterScreen_to_endRegisterScreen,bundle)
       }
    }


}