package com.example.storyapp.ui.map

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.removeItemAt
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMaps2Binding
import com.example.storyapp.model.UserPreferences
import com.example.storyapp.remote.response.StoryItem
import com.example.storyapp.ui.auth.login.LoginActivity
import com.example.storyapp.ui.main.MainActivity

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMaps2Binding
    private val viewModel by viewModels<MapsViewModel>()
    private lateinit var userPreferences: UserPreferences
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPreferences = UserPreferences(this)

        binding = ActivityMaps2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.defaultmenu, menu)
        menu?.removeItemAt(2)
        return true
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
            R.id.list_view -> {
                val intent = Intent(this@MapsActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    private fun setViewModel() {
        viewModel.stories.observe(this){stories ->
            if(!stories.isNullOrEmpty()){
                for (item : StoryItem in stories){
                    addMarker(item)
                }
                val bounds: LatLngBounds = boundsBuilder.build()
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        bounds,
                        resources.displayMetrics.widthPixels,
                        resources.displayMetrics.heightPixels,
                        300
                    )
                )
            }
        }
        viewModel.getAllStories(userPreferences.getUser()){message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addMarker(storyItem: StoryItem){
        val storyPosition = LatLng(storyItem.lat!!, storyItem.lon!!)
        mMap.addMarker(
            MarkerOptions()
                .position(storyPosition)
                .title(storyItem.name)
        )
        boundsBuilder.include(storyPosition)
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e("MapActivity", "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e("MapActivity", "Can't find style. Error: ", exception)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        setMapStyle()
    }
}