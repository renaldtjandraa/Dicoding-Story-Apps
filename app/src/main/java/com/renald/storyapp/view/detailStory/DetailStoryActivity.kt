package com.renald.storyapp.view.detailStory

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.renald.storyapp.data.Result
import com.renald.storyapp.databinding.ActivityDetailStoryBinding
import com.renald.storyapp.factory.ViewModelFactory
import com.renald.storyapp.helper.ShowLoading

class DetailStoryActivity : AppCompatActivity(), ShowLoading {

    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: DetailStoryViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()
        setupAction()

    }

    override fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(binding.root.context)
    }

    private fun setupAction() {
        val storyId = intent.getStringExtra("data").toString()
        viewModel.getDetailStory(storyId).observe(this@DetailStoryActivity) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, "Failed to Load Page", Toast.LENGTH_SHORT).show()
                    }

                    is Result.Success -> {
                        showLoading(false)
                        Toast.makeText(this, "Load Page Success!", Toast.LENGTH_SHORT).show()
                        binding.apply {
                            tvDetailName.text = result.data.story.name
                            tvDetailDescription.text = result.data.story.description
                        }
                        Glide.with(applicationContext).load(result.data.story.photoUrl)
                            .into(binding.ivDetailPhoto)
                    }
                }
            }
        }
    }
}

