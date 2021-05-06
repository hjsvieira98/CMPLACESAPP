package com.example.cmplacesapp.Mapas

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.cmplacesapp.LocalDB.AppDatabase
import com.example.cmplacesapp.LocalDB.Notes
import com.example.cmplacesapp.Notas.EditNotas
import com.example.cmplacesapp.R
import com.example.cmplacesapp.RecycleAddapter.CustomAdapter
import com.example.cmplacesapp.retrofit.EndPoints
import com.example.cmplacesapp.retrofit.Incidentes
import com.example.cmplacesapp.retrofit.ServiceBuilder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("DEPRECATION")
internal class MapaActivity : AppCompatActivity(), OnMapReadyCallback, SensorEventListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mMap: GoogleMap? = null;
    private lateinit var sensorManager: SensorManager
    private var pressure: Sensor? = null
    private var cpuTemperature: Sensor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)
        title = "Mapa";
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        cpuTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.filtermenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter -> {
                FilterFragment().filter().show(supportFragmentManager)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val sharedPref = getSharedPreferences("AUTH", Context.MODE_PRIVATE) ?: return
        val user_id = sharedPref.getString("user_id", "0")
        mMap = googleMap
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }

        mMap!!.isMyLocationEnabled = true;
        mMap!!.setOnMarkerClickListener { marker ->

            var incidente = marker?.tag as Incidentes;

                    var title = marker?.title;
                    var Description = marker?.snippet
                    val intent = Intent(this, EditaMarker::class.java)
                    intent.putExtra("ID", incidente.id.toString());
                    intent.putExtra("Image", incidente.image);
                    intent.putExtra("Title", title);
                    intent.putExtra("Description", Description);
                    intent.putExtra("user_id", incidente.user_id.toString());
                    intent.putExtra("tipoIncidente", incidente.tipoIncidente);
                    this.startActivity(intent)




            true
        }
        getMarkers()

    }

    private fun goToLocationZoom(lat: Double, lng: Double, zoom: Float) {
        val ll = LatLng(lat, lng)
        val cameraPosition = CameraPosition.Builder()
            .target(ll)
            .zoom(zoom)
            .bearing(45f)
            .tilt(60f)
            .build()
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    fun novoIncidenteF(view: View) {
        val EntraNovoIncidente = Intent(this, NovoIncidente::class.java).apply {}
        startActivity(EntraNovoIncidente)
    }

    private fun createMarker(
        latitude: Double,
        longitude: Double,
        title: String?,
        snippet: String?,
        map: GoogleMap?,
        icon: Bitmap,
        incidentes: Incidentes
    ): Marker? {
        val height = 100
        val width = 100
        val b: Bitmap = icon;
        val smallMarker: Bitmap = Bitmap.createScaledBitmap(b, width, height, false)
        val smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker)
        val marker = map?.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))
                .title(title)
                .snippet(snippet)
                .icon(smallMarkerIcon)
        )
        marker?.tag = incidentes;
        return marker
    }

    override fun onResume() {
        super.onResume()
        if (mMap != null) {
            getMarkers();
        }
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun getMarkers() {

        mMap!!.clear();
        val sharedPref = getSharedPreferences("AUTH", Context.MODE_PRIVATE) ?: return
        val preferences: SharedPreferences = getSharedPreferences("FILTERMAP", Context.MODE_PRIVATE)
        val checkbox1 = preferences.getBoolean("0", true)
        val checkbox2 = preferences.getBoolean("1", true)
        val checkbox3 = preferences.getBoolean("2", true)
        val checkbox4 = preferences.getBoolean("3", true)
        val user_id = sharedPref.getString("user_id", "0")
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getIncidents(checkbox1,checkbox2,checkbox3,checkbox4);
        val radius = preferences.getInt("radius",0);
       if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.latitude?.let { goToLocationZoom(it, location.longitude, 15f) }

                call.enqueue(object : Callback<List<Incidentes>> {
                    override fun onResponse(
                        call: Call<List<Incidentes>>,
                        response: Response<List<Incidentes>>
                    ) {

                        response.body()?.forEach { incident ->


                            var icon =
                                BitmapFactory.decodeResource(resources, R.drawable.markerred)
                            if (user_id?.toInt() != incident.user_id) {
                                icon =
                                    BitmapFactory.decodeResource(resources, R.drawable.markerblue)
                            }
                            if (incident.latitude != null && incident.longitude != null) {
                                if (radius != 0) {
                                    val locationA = Location("me")
                                    Log.e("Teste",location!!.latitude.toString())
                                    locationA.latitude = location!!.latitude
                                    locationA.longitude = location!!.longitude
                                    val locationB = Location("marker")

                                    locationB.latitude = incident.latitude.toDouble()
                                    locationB.longitude = incident.longitude.toDouble()

                                    val distance: Float = locationA.distanceTo(locationB)
                                    if (distance < radius) {
                                        createMarker(
                                            incident.latitude.toDouble(),
                                            incident.longitude.toDouble(),
                                            incident.title,
                                            incident.description,
                                            mMap,
                                            icon,
                                            incident
                                        )
                                    }
                                } else {
                                    createMarker(
                                        incident.latitude.toDouble(),
                                        incident.longitude.toDouble(),
                                        incident.title,
                                        incident.description,
                                        mMap,
                                        icon,
                                        incident
                                    )
                                }

                            }

                        }

                    }

                    override fun onFailure(call: Call<List<Incidentes>>, t: Throwable) {
                        Toast.makeText(this@MapaActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
    }
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event!!.values[0] < 20000.0) {
            mMap!!.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this@MapaActivity,
                            R.raw.map_in_night
                    )
            );
        } else {
            mMap!!.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this@MapaActivity,
                            R.raw.map_in_day
                    )
            );
        }
    }


    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}