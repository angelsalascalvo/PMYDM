package com.example.rutil.sendbox;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    // Base de datos ============================================
    private static FirebaseDatabase baseDatos;

    private GoogleMap mMap;
    private ArrayList<Marker> marcadores = new ArrayList<Marker>();
    private ArrayList<String> transportistas = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Iniciar variables
        baseDatos = FirebaseDatabase.getInstance();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        obtenerTransportistas();
        // Add a marker in Sydney and move the camera
        LatLng inicial = new LatLng(40.349161, -3.771514);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inicial, 4));
        //Poner los elementos del mapa a la escucha
        mMap.setOnInfoWindowClickListener(this);
    }

    public void obtenerTransportistas(){
        final DatabaseReference transport = baseDatos.getReference("transportistas");
        transport.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot fila : dataSnapshot.getChildren()){
                    boolean encontrado = false;
                    String lat = fila.child("ubicacion").child("lat").getValue().toString();
                    String lon = fila.child("ubicacion").child("long").getValue().toString();

                    if(!lat.equals("null") && !lon.equals("null")){

                        //Obtener la nueva ubicacion
                        LatLng ubicacion = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                        for(int i =0; i<transportistas.size();i++){
                            //Compruebo si existe ese transportista en el mapa
                            if(fila.getKey().equals(transportistas.get(i))){
                                //Cambiar ubicacion
                                marcadores.get(i).setPosition(ubicacion);
                                encontrado=true;
                            }
                        }

                        //Si no se encuentra el transportista se añade
                        if(!encontrado){
                            //Obtener icono del marcador
                            int height = 50;
                            int width = 50;
                            BitmapDrawable imagen=(BitmapDrawable)getResources().getDrawable(R.drawable.marcador);
                            Bitmap b=imagen.getBitmap();
                            Bitmap iconoMarcador = Bitmap.createScaledBitmap(b, width, height, false);

                            transportistas.add(fila.getKey());
                            Marker marcador = mMap.addMarker(new MarkerOptions().position(ubicacion)
                                    .title(fila.child("nombre").getValue().toString()).icon(BitmapDescriptorFactory.fromBitmap(iconoMarcador)));
                            marcadores.add(marcador);
                        }

                    }else{
                        //Eliminar transportista si existe
                        for(int i=0;i<transportistas.size();i++){
                            if(transportistas.get(i).equals(fila.getKey())){
                                transportistas.remove(i);
                                marcadores.get(i).remove();
                                marcadores.remove(i);
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTARÁ AL PULSAR SOBRE EL NOMBRE DE UN MARCADOR
     * @param marker
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        //Recorrer todos los marcadores para abrir el activity del seleccionado
        for(int i=0;i<marcadores.size();i++){
            if(marcadores.get(i).toString().equals(marker.toString())){
                //Abrir Activity del usuario
                Intent intent = new Intent(this, ActivityUsuarioInfo.class);
                intent.putExtra("uid", transportistas.get(i));
                startActivity(intent);
            }
        }
    }
}