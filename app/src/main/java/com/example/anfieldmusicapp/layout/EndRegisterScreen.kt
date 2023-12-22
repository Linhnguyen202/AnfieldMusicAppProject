package com.example.anfieldmusicapp.layout

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.RegisterScreen
import com.example.anfieldmusicapp.databinding.FragmentEndRegisterScreenBinding
import com.example.anfieldmusicapp.model.User
import com.example.anfieldmusicapp.share.sharePreferenceUtils.saveUser
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EndRegisterScreen : Fragment() {
   lateinit var binding : FragmentEndRegisterScreenBinding
    val args : EndRegisterScreenArgs by navArgs()
   val auth by lazy {
       (activity as RegisterScreen).auth
   }
    val reference by lazy {
        (activity as RegisterScreen).reference
    }
    var imageProfile : Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEndRegisterScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEvents()
    }
    private fun validateUsername() : Boolean{
        val username = binding.usernameEditText.text.toString()
        var error : String? = null
        if(username.isNullOrEmpty()){
            error = "Email is required"
        }
        if(error != null){
            binding.usernameContainer.error = error
            binding.usernameContainer.isErrorEnabled = true

        }
        return error != null
    }

    private fun addEvents() {
        binding.cameraBtn.setOnClickListener {
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }


        binding.signUpBtn.setOnClickListener {
            onSubmit()
        }
        binding.signInTitle.setOnClickListener {
            findNavController().navigate(R.id.action_endRegisterScreen_to_loginScreen)
        }
        binding.usernameEditText.setOnFocusChangeListener { view, b ->
            if(view.isFocused){
                binding.usernameContainer.error = ""
                binding.usernameContainer.isErrorEnabled = false
            }

        }
    }
    private fun validate() : Boolean{
        var validateValue = false
        if(validateUsername()) validateValue = true
        return validateValue

    }
    private fun onSubmit() {
        if(!validate()){
            val email = args.Email
            val pass = args.Password
            val username = binding.usernameEditText.text
            val profileUser = UserProfileChangeRequest.Builder()
                .setDisplayName(username.toString())
                .setPhotoUri(imageProfile)
                .build()
            CoroutineScope(Dispatchers.IO).launch {
                try{
                    withContext(Dispatchers.Main){
                        binding.progessBar.visibility = View.VISIBLE
                        binding.signupTitle.visibility = View.GONE
                    }
                    auth.createUserWithEmailAndPassword(email,pass).await()
                    withContext(Dispatchers.Main){
                        if(auth.currentUser != null){
                            withContext(Dispatchers.IO){
                                auth.currentUser!!.updateProfile(profileUser).await()
                                withContext(Dispatchers.Main){
                                val user = User(auth.currentUser!!.uid,auth.currentUser!!.displayName.toString(),auth.currentUser!!.email.toString(),auth.currentUser!!.photoUrl.toString())
                                saveUser(user)
                                }
                            }
                        }
                    }
                }
                catch (e : Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(),e.toString(), Toast.LENGTH_LONG).show()
                    }

                }
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageProfile = data?.data
        binding.imageProfile.setImageURI(imageProfile)
    }
      suspend fun saveUser(user: User){
          withContext(Dispatchers.IO){
              reference.child(user.id!!).setValue(user).addOnCompleteListener {
                  if(it.isSuccessful){
                      Toast.makeText(requireContext(),"Register Successfully",Toast.LENGTH_LONG).show()
                      findNavController().navigate(R.id.action_endRegisterScreen_to_loginScreen)
                      binding.progessBar.visibility = View.GONE
                      binding.signupTitle.visibility = View.VISIBLE
                  }
                  else{

                  }
              }
          }


    }

}