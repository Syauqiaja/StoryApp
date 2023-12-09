package com.example.storyapp.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.remote.response.PostResponse
import com.example.storyapp.remote.retrofit.ApiConfig
import com.example.storyapp.ui.auth.login.LoginActivity
import retrofit2.Call
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViewModel()
        setActionButton()
    }

    private fun setViewModel() {
        viewModel.isLoading.observe(this){isLoading ->
            binding.progressBar.visibility = if(isLoading){
                View.VISIBLE
            }else{
                View.INVISIBLE
            }
        }

        viewModel.toastMessage.observe(this){message ->
            if(message != null){
                Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
                if(message == "Register success"){
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    viewModel.removeToast()

                    startActivity(intent)
                    finish()
                }
            }
        }

    }

    private fun setActionButton() {
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail
            val password = binding.edRegisterPassword

            when{
                name.isEmpty() -> {binding.edRegisterName.error = "Masukkan nama"}
                !email.checkValid() -> {email.requestFocus()}
                password.checkValid() -> {
                    viewModel.register(name, email.text.toString(), password.text.toString())
                }
            }
        }
        binding.btnAlreadyRegister.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun register(name: String, email: String, password: String){
        val apiService = ApiConfig().getApiService()
        val client = apiService.register(name, email, password)
        client.enqueue(object : retrofit2.Callback<PostResponse>{
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if(response.isSuccessful){
                    val responseBody = response.body()!!
                    if(responseBody.error!!){
                        Toast.makeText(this@RegisterActivity, "Register Failed : ${responseBody.message}", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@RegisterActivity, "Register success", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.putExtra("email_registered", email)
                        startActivity(intent)
                        finish()
                    }
                }else{
                    Toast.makeText(this@RegisterActivity, "onFailure : ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "onFailure : ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}