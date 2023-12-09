package com.example.storyapp.ui.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.removeItemAt
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.ViewModelPagingFactory
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.model.UserPreferences
import com.example.storyapp.ui.adapter.LoadingStateAdapter
import com.example.storyapp.ui.adapter.StoryListAdapter
import com.example.storyapp.ui.auth.login.LoginActivity
import com.example.storyapp.ui.map.MapsActivity
import com.example.storyapp.ui.story.StoryActivity

class MainActivity : AppCompatActivity() {
    private lateinit var  viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreferences(this)

        setViewModel()
        assignBinding()
    }

    private fun assignBinding() {
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.scrollToPosition(0)

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this@MainActivity, StoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.defaultmenu, menu)
        menu?.removeItemAt(3)
        return true
    }

    override fun onResume() {
        super.onResume()
        setAdapter()
        binding.rvStory.scrollToPosition(0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_logout ->{
                userPreferences.logOut()
                startActivity(Intent(this, LoginActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this as Activity).toBundle())
                finish()
            }
            R.id.change_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.maps_view -> {
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    private fun setAdapter(){
        val adapter = StoryListAdapter()
        viewModel.stories(userPreferences.getUser().token).observe(this){
            adapter.submitData(lifecycle, it)
        }
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelPagingFactory(this)
        ) [MainViewModel::class.java]

        setAdapter()

        binding.cvEmpty.visibility = View.INVISIBLE

        if(!userPreferences.getUser().isLogin){
            startActivity(Intent(this, LoginActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this as Activity).toBundle())
            finish()
        }
    }
}