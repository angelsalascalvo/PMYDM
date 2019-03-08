package com.example.rutil.sendbox;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

public class LocalizarGPS implements LocationListener{

    // Base de datos ============================================
    private FirebaseDatabase baseDatos;
    private FirebaseUser user;

    private LocationManager lManager;
    private Context context;
    DatabaseReference ubicacion;

    /**
     * CONSTRUCTOR PARAMETRIZADO
     * @param context
     */
    @SuppressLint("MissingPermission")
    public LocalizarGPS(Context context, FirebaseUser user) {
        this.context = context;
        this.user = user;
        //Obtener Base de Datos
        baseDatos = FirebaseDatabase.getInstance();
        Log.d("AAAAA", "->"+user.getUid());
        ubicacion = baseDatos.getReference("transportistas").child(user.getUid()).child("ubicacion");

        //Iniciar GPS
        lManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0.5f, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        //Almacenar longitud y latitud en base de datos
        ubicacion.child("lat").setValue(""+location.getLatitude());
        ubicacion.child("long").setValue(""+location.getLongitude());
        Log.d("AAAAA", "->>"+user.getUid());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        lManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


            //lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0.2f, this);
            Log.d("AAAAA", "-|"+user.getUid());

    }

    @Override
    public void onProviderDisabled(String provider) {
        ubicacion.child("lat").setValue("noooo");
        ubicacion.child("long").setValue("noooo");
        Log.d("AAAAA", "->>eeeee");
    }

    public void detener(){
        ubicacion.child("lat").setValue("noooo");
        ubicacion.child("long").setValue("noooo");
    }
}
