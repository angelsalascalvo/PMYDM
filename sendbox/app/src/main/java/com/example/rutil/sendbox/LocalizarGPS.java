package com.example.rutil.sendbox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LocalizarGPS implements LocationListener{

    // Base de datos ============================================
    private FirebaseDatabase baseDatos;
    private FirebaseUser user;

    // GPS ======================================================
    public LocationManager lManager;
    private DatabaseReference ubicacion;

    //Otros =====================================================
    private Context context;

    //----------------------------------------------------------------------------------------------

    /**
     * CONSTRUCTOR POR DEFECTO
     */
    public LocalizarGPS (){
    }

    //----------------------------------------------------------------------------------------------

    /**
     * CONSTRUCTOR PARAMETRIZADO
     * @param context
     */
    public LocalizarGPS(Context context, FirebaseUser user) {
        this.context = context;
        this.user = user;
        //Obtener Base de Datos
        baseDatos = FirebaseDatabase.getInstance();
        ubicacion = baseDatos.getReference("transportistas").child(user.getUid()).child("ubicacion");
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTA AL CAMBIAR LA LOCALIZACION DEL GPS
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        //Almacenar longitud y latitud en base de datos
        ubicacion.child("lat").setValue("" + location.getLatitude());
        ubicacion.child("long").setValue("" + location.getLongitude());
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTA CUANDO SE ACTIVA EL GPS
     * @param provider
     */
    @Override
    public void onProviderEnabled(String provider) {
        if(lManager==null)
            iniciar();
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTA CUANDO SE DESACTIVA EL GPS
     * @param provider
     */
    @Override
    public void onProviderDisabled(String provider) {
        lManager.removeUpdates(this);
        lManager=null;
        ubicacion.child("lat").setValue("null");
        ubicacion.child("long").setValue("null");
        Toast.makeText(context, context.getString(R.string.txtGPS), Toast.LENGTH_SHORT).show();
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA INICIAR EL GPS Y EL ENVIO DE COORDENADAS A LA BASE DE DATOS
     */
    @SuppressLint("MissingPermission")
    public void iniciar(){
        //Iniciar GPS
        lManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0.3f, this);

        Location loc = lManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(loc!=null){
            ubicacion.child("lat").setValue(""+loc.getLatitude());
            ubicacion.child("long").setValue(""+loc.getLongitude());
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE PERMITE DETENER EL ENVIO DE CORDENADAS A LA BASE DE DATOS
     */
    public void detener(){
        lManager.removeUpdates(this);
        lManager=null;
        ubicacion.child("lat").setValue("null");
        ubicacion.child("long").setValue("null");
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}