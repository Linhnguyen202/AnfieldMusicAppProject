package com.example.anfieldmusicapp.layout

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.anfieldmusicapp.MainActivity
import com.example.anfieldmusicapp.R
import com.example.anfieldmusicapp.databinding.FragmentEditUserProfileScreenBinding
import com.example.anfieldmusicapp.model.User
import com.example.anfieldmusicapp.share.sharePreferenceUtils
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class EditUserProfileScreen : Fragment() {
        lateinit var binding : FragmentEditUserProfileScreenBinding
        val auth : FirebaseAuth by lazy {
            (activity as MainActivity).auth
        }
    var imageProfile : Uri? = null // link ảnh

    lateinit var db : FirebaseDatabase
    lateinit var reference : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditUserProfileScreenBinding.inflate(layoutInflater)
        return binding.root
    }
    lateinit var myView : View;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this@EditUserProfileScreen.myView = view
        db = FirebaseDatabase.getInstance()
        reference = db.getReference("User")
        val user = sharePreferenceUtils.getUser(requireContext())
        binding.usernameEditText.setText(user.firstName.toString())
        Glide.with(this).load(user.image).into(binding.imageProfile)

        binding.toolbar.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.updateBtn.setOnClickListener {
            updateUserData();
        }
        binding.cameraBtn.setOnClickListener {
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }


    }

    private fun updateUserData() {
        val profileUser = UserProfileChangeRequest.Builder()
            .setDisplayName(binding.usernameEditText.text.toString()) // tên
            .setPhotoUri(imageProfile) // ảnh
            .build()
            binding.progessBar.visibility = View.VISIBLE
            binding.updateBtn.visibility = View.GONE
            CoroutineScope(Dispatchers.IO).launch{
              auth.currentUser!!.updateProfile(profileUser).await() // cập nhật ảnh với tên
                withContext(Dispatchers.Main){
                    binding.progessBar.visibility = View.GONE
                    binding.updateBtn.visibility = View.VISIBLE
                    val user = User(auth.currentUser!!.uid,auth.currentUser!!.displayName.toString(),auth.currentUser!!.email.toString(),auth.currentUser!!.photoUrl.toString())
                    // lưu thông tin đấy vào realtime db
                    saveUser(user);
                }
          }
    }

    private fun saveUser(user: User) {
        val updates = hashMapOf<String, Any>(
           user.id.toString() to user
        )
        reference.updateChildren(updates).addOnCompleteListener {
            if(it.isSuccessful){
                Snackbar.make(this@EditUserProfileScreen.myView,"Update user Successfully",
                    Snackbar.LENGTH_SHORT).show()
                sharePreferenceUtils.saveUser(user,requireContext())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageProfile = data?.data
        binding.imageProfile.setImageURI(imageProfile)
    }

}