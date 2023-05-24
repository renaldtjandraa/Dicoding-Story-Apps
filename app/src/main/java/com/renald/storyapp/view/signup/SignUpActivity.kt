package com.renald.storyapp.view.signup

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
import com.renald.storyapp.data.Result
import com.renald.storyapp.databinding.ActivitySignUpBinding
import com.renald.storyapp.factory.ViewModelFactory
import com.renald.storyapp.helper.ShowLoading
import com.renald.storyapp.view.login.LoginActivity

class SignUpActivity : AppCompatActivity(), ShowLoading {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: SignUpViewModel by viewModels { factory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        playAnimation()
        setButtonEnable()
        setupAction()
        supportActionBar?.hide()
    }

    private fun checkPassword() {
        binding.registerButton.isEnabled = !binding.signUpPassword.isError
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(binding.root.context)
    }

    private fun setButtonEnable() {
        binding.signUpPassword.addTextChangedListener(object : TextWatcher {
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

    override fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvSignUp, View.ALPHA, 1f).setDuration(500)
        val image = ObjectAnimator.ofFloat(binding.logo, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.signUpName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.signUpEmail, View.ALPHA, 1f).setDuration(500)
        val password =
            ObjectAnimator.ofFloat(binding.signUpPassword, View.ALPHA, 1f).setDuration(500)
        val registerButton =
            ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(500)
        val alreadyHaveAccount =
            ObjectAnimator.ofFloat(binding.tvAlreadyHaveAccount, View.ALPHA, 1f).setDuration(500)
        val tvlogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(
                title,
                image,
                name,
                email,
                password,
                registerButton,
                alreadyHaveAccount,
                tvlogin
            )
            startDelay = 500
        }.start()
    }

    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            val name = binding.signUpName.text.toString()
            val email = binding.signUpEmail.text.toString()
            val password = binding.signUpPassword.text.toString()
            viewModel.signUp(name, email, password).observe(this@SignUpActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(this, "Failed to Register", Toast.LENGTH_SHORT).show()
                        }

                        is Result.Success -> {
                            showLoading(false)
                            Toast.makeText(this, "Register Success!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    }
                }
            }
        }


        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
        }
    }
}