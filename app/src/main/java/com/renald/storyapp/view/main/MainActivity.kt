package com.renald.storyapp.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.renald.storyapp.adapter.LoadingStateAdapter
import com.renald.storyapp.adapter.StoryListAdapter
import com.renald.storyapp.controller.local.UserPreference
import com.renald.storyapp.databinding.ActivityMainBinding
import com.renald.storyapp.factory.ViewModelFactory
import com.renald.storyapp.helper.ShowLoading
import com.renald.storyapp.view.addStory.AddStoryActivity
import com.renald.storyapp.view.login.LoginActivity
import com.renald.storyapp.view.maps.MapsActivity


class MainActivity : AppCompatActivity(), ShowLoading {

    private lateinit var binding: ActivityMainBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: MainViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvStory.layoutManager = LinearLayoutManager(this)

        setupViewModel()
        getData()
        setupAction()
        setupView()

    }

    private fun getData() {
        val adapter = StoryListAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.stories.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(binding.root.context)
    }

    override fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun logout() {
        val pref = UserPreference(this)
        pref.clearUser()
        Toast.makeText(this, "You have logged out successfully", Toast.LENGTH_SHORT).show()
    }

    private fun setupAction() {
        binding.actionLogout.setOnClickListener {
            logout()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.actionMaps.setOnClickListener {
            startActivity(Intent(this@MainActivity, MapsActivity::class.java))
        }
        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupView() {
        supportActionBar?.hide()
        val pref = UserPreference(this)
        binding.tvName.text = pref.getUser().name
    }
}

