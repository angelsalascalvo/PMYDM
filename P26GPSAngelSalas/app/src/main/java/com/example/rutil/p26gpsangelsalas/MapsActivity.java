package com.example.rutil.p26gpsangelsalas;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LocationManager locationManager; // Manejador de la posicion
    private LocationListener locationListener; // Escuchador para detectar la posicion

    private final long MIN_TIME = 0; //Tiempo minimo de actualización en ms (1000 = 1s)
    private final long MIN_DIST = 0; //Distancia minima fde actualización de la localización en metros

    private LatLng latitudLongitud; // Objeto que guarda la latitud y longitud

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //COMPROBAR SI LOS PERMISOS ESTAN SOLICITADOS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            //Si los permisos no estan concedidos se vuelven a solicitar.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Lineas para añadir un punto en el mapa
        if (latitudLongitud == null) {
            LatLng sydney = new LatLng(37.346857, -2.437246);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in TIJOLA"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }

        //Lanzar el evento que obtiene nuestra localización
        localizarGPS();
    }

    //----------------------------------------------------------------------------------------------

    public void localizarGPS() {
        //Instanciar objetos
        locationManager = (LocationManager) MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Si el listener ha encontrado una localización, borramos todos los puntos de marcado
                if (location != null)
                    mMap.clear();

                //Con estas lineas cambiamos el marcador de posicion en el mapa
                latitudLongitud = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latitudLongitud).title("Mi posicion"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latitudLongitud));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //Solicitados que se actualice frecuentemente, pasandole el servicio de GPS que utilizamos (GPS_PROVIDER),
        //el tiempo mínimo de actualización (MIN TIME), la distancia minima de actualización (MIN_DIST) y el listener
        //que nos coge / encuentra la localización
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        } catch (SecurityException e) {
        }
    }
}
