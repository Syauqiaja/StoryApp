package com.example.storyapp.ui.auth.login

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.model.UserPreferences
import com.example.storyapp.ui.auth.register.RegisterActivity
import com.example.storyapp.ui.main.MainActivity

class LoginActivity : AppCompatActivity(){
    private val loginViewModel by viewModels<LoginViewModel>()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreferences(this)

        setupViewModel()
        setupButtonAction()
    }

    private fun setupButtonAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail
            val password = binding.edLoginPassword
            when{
                !email.checkValid() -> {
                    binding.edLoginEmail.requestFocus()
                }
                password.checkValid() ->{
                    loginViewModel.login(email.text.toString(), password.text.toString())
                }
            }
        }
        binding.btnCreateAccount.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupViewModel() {
        loginViewModel.isLoading.observe(this){isLoading ->
            binding.progressBar.visibility = if(isLoading){
                View.VISIBLE
            }else{
                View.INVISIBLE
            }
        }
        loginViewModel.toastMessage.observe(this){message ->
            if(message != null){
                Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                loginViewModel.removeToast()
            }
        }
        loginViewModel.userModel.observe(this){user ->
            if(user.isLogin){
                userPreferences.saveUser(user)
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }

}