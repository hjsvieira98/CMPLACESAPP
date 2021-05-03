package com.example.cmplacesapp.Mapas

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.cmplacesapp.Notas.EditNotas
import com.example.cmplacesapp.R
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


internal class MapaActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private lateinit var marker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return
        }
        val sharedPref = getSharedPreferences("AUTH", Context.MODE_PRIVATE) ?: return
        val user_id = sharedPref.getString("user_id","0")
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getIncidents();
        call.enqueue(object : Callback<List<Incidentes>> {
            override fun onResponse(call: Call<List<Incidentes>>, response: Response<List<Incidentes>>) {

                response.body()?.forEach { incident ->
                    var icon =  BitmapFactory.decodeResource(resources,R.drawable.makervermelha)
                    if(user_id?.toInt() != incident.user_id){
                        icon = BitmapFactory.decodeResource(resources,R.drawable.markerazul)
                    }
                    if(incident.latitude != null && incident.longitude != null){
                        createMarker(incident.latitude.toDouble(),
                            incident.longitude.toDouble(),
                            incident.title,
                            incident.description,
                            incident.image,
                            googleMap,
                            icon,
                            incident
                        )
                    }

                }

            }

            override fun onFailure(call: Call<List<Incidentes>>, t: Throwable) {
                Toast.makeText(this@MapaActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener { marker ->
            var incidente = marker?.tag as Incidentes;
            var title = marker?.title;
            var Description = marker?.snippet
            val intent = Intent(this, EditaMarker::class.java)
            intent.putExtra("ID",  incidente.id);
            intent.putExtra("Image",  incidente.image);
            intent.putExtra("Title",title);
            intent.putExtra("Description",Description);
            intent.putExtra("user_id",  incidente.user_id);
            this.startActivity(intent)
            true
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
              // Got last known location. In some rare situations this can be null.
                location?.latitude?.let { goToLocationZoom(it,location.longitude,15f) }
            }


    }
    private fun goToLocationZoom(
        lat: Double,
        lng: Double,
        zoom: Float
    ) {
        val ll = LatLng(lat, lng)
        val cameraPosition = CameraPosition.Builder()
            .target(ll)
            .zoom(zoom)
            .bearing(45f)
            .tilt(60f)
            .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
     fun novoIncidenteF(view: View){
           val EntraNovoIncidente = Intent(this, NovoIncidente::class.java).apply {
                     }

                       startActivity(EntraNovoIncidente)
    }

    private fun createMarker(
        latitude: Double,
        longitude: Double,
        title: String?,
        snippet: String?,
        image: String?,
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

}