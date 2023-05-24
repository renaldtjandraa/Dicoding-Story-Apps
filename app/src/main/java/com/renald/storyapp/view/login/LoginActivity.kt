package com.renald.storyapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.renald.storyapp.controller.local.UserPreference
import com.renald.storyapp.data.Result
import com.renald.storyapp.databinding.ActivityLoginBinding
import com.renald.storyapp.factory.ViewModelFactory
import com.renald.storyapp.helper.ShowLoading
import com.renald.storyapp.model.entity.UserModel
import com.renald.storyapp.model.response.LoginResponse
import com.renald.storyapp.view.main.MainActivity
import com.renald.storyapp.view.signup.SignUpActivity

class LoginActivity : AppCompatActivity(), ShowLoading {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        checkPreference()
        setupViewModel()
        setButtonEnable()
        playAnimation()
        setUpAction()


    }

    fun setupViewModel() {
        factory = ViewModelFactory.getInstance(binding.root.context)
    }

    override fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun checkPassword() {
        binding.LoginButton.isEnabled = !binding.loginPassword.isError
    }

    private fun setButtonEnable() {
        binding.loginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                checkPassword()
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkPassword()
            }

            override fun afterTextChanged(s: Editable) {
                checkPassword()
            }

        })
    }

    private fun setUpAction() {
        binding.LoginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()
            viewModel.login(email, password).observe(this@LoginActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(this, "Failed to Login", Toast.LENGTH_SHORT).show()
                        }
                        is Result.Success -> {
                            showLoading(false)
                            Toast.makeText(this, "Login Success!", Toast.LENGTH_SHORT).show()
                            saveUserToPreference(result.data)

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    }
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

    }

    private fun saveUserToPreference(data: LoginResponse) {
        val pref = UserPreference(this)
        val result = data.loginResult
        val prefData = UserModel(result?.userId, result?.name,result?.token)
        pref.saveUser(prefData)
    }

    private fun checkPreference() {
        val pref = UserPreference(this)
        val token = pref.getUser().token
        if (token != null) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        val image = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.loginEmail, View.ALPHA, 1f).setDuration(500)
        val password =
            ObjectAnimator.ofFloat(binding.loginPassword, View.ALPHA, 1f).setDuration(500)
        val loginButton =
            ObjectAnimator.ofFloat(binding.LoginButton, View.ALPHA, 1f).setDuration(500)
        val dontHaveAccount =
            ObjectAnimator.ofFloat(binding.tvDontHaveAccount, View.ALPHA, 1f).setDuration(500)
        val tvRegister = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(
                title,
                image,
                email,
                password,
                loginButton,
                dontHaveAccount,
                tvRegister
            )
            startDelay = 500
        }.start()
    }
}



