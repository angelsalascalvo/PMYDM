package com.example.rutil.sendbox;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityTranspor extends AppCompatActivity {

    // Autenticacion ============================================
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser user;

    // Base de datos ============================================
    private static FirebaseDatabase baseDatos;

    // RecyclerView =============================================
    private RecyclerView rvProductos;
    private RecyclerView.Adapter adaptador;
    private RecyclerView.LayoutManager layoutProductos;

    // Otros =====================================================
    private ArrayList<PaqueteDatos> paquetes;
    private FloatingActionButton bCrear;
    private int cGris, cVerde;
    public static String sEntregado, sPendiente;
    private LocalizarGPS localizarGPS;
    private boolean permisos;

    //----------------------------------------------------------------------------------------------

    /**
     * SOBRESCRITURA DEL METODO ONCREATE
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transpor);

        //Iniciar Variables
        cGris=ContextCompat.getColor(this, R.color.gris);
        cVerde=ContextCompat.getColor(this, R.color.verde);
        sEntregado=getString(R.string.estEntregado);
        sPendiente=getString(R.string.estPendiente);
        baseDatos = FirebaseDatabase.getInstance();

        //Comprobar permisos para GPS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            }else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        //Obtener el usuario logueado
        obtenerUsuario();

        //Iniciar variables
        rvProductos = (RecyclerView) findViewById(R.id.rvPaquetes);
        rvProductos.setHasFixedSize(true); //Indicar que el tamaño del listado no depende de los elemntos que tenga
        layoutProductos = new LinearLayoutManager(this);

        //Poner boton flotante a la escucha
        bCrear = (FloatingActionButton) findViewById(R.id.fabCrear);
        bCrear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                crearClic();
            }
        });

    }

    //----------------------------------------------------------------------------------------------

    /**
     * SOBRESCRITURA DEL METODO ONSTART
     */
    @Override
    protected void onStart() {
        super.onStart();
        //Añade el escuchador para obtener cuenta de firebase
        if (firebaseAuthListener!=null)
            firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    //----------------------------------------------------------------------------------------------

    /**
     * SOBRESCRITURA DEL METODO ONSTOP
     */
    @Override
    protected void onStop() {
        super.onStop();
        //Quitamos el escuchador si estaba instanciado
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTARÁ AL HACER CLIC EN EL BOTON FLOTANTE PARA AÑADIR UN PAQUETE
     */
    public void crearClic(){
        Intent i = new Intent(this, ActivityCrearPaquete.class);
        startActivity(i);
    }


    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA OBTENER EL OBJETO USUARIO DE FIREBASE QUE REFERENCIA AL USUARIO
     */
    public void obtenerUsuario(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                //Si no se localiza el usuario se finaliza la ejecucion
                if (user == null)
                    finish();
                //Si se ha obtenido bien el usuario, se ejecutan el resto de metodos
                else {
                    //Obtener los listados de todos los paquetes del usuario
                    obtenerPaquetes();
                    //Activar envio ubicacion
                    activarGPS();
                }
            }
        };
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA RELLENAR EL ARRAY DE PAQUETES CON LOS PAQUETES DEL REPARTIDOR
     */
    public void obtenerPaquetes(){
        baseDatos.getReference("paquetes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Si se ha obtenido algun dato
                if(dataSnapshot.exists()){
                    paquetes=new ArrayList<PaqueteDatos>();
                    //Recorrer cada uno de los paquetes del grupo paquetes
                    for(DataSnapshot fila : dataSnapshot.getChildren()){
                        try {


                            //Obtener el valor del transportista que lo reparte
                            String t = fila.child("transportista").getValue().toString();

                            //Si el paquete es de ese transportista, se almacena en el array
                            if (t.equals(user.getUid())) {
                                //Crear objeto con los datos del paquete
                                PaqueteDatos pq = new PaqueteDatos();
                                pq.setCodigo(fila.getKey());
                                pq.setDireccion(fila.child("direccion").getValue().toString());
                                pq.setNombre(fila.child("destinatario").getValue().toString());
                                if (fila.child("entregado").getValue().toString().equalsIgnoreCase("si")) {
                                    pq.setImgEstado(R.drawable.listo);
                                    pq.setEntregado(getString(R.string.estEntregado));
                                    pq.setImagenButtonEstado(R.drawable.boton_pendiente);
                                    pq.setColorEstado(cGris);
                                } else {
                                    pq.setImgEstado(R.drawable.pendiente);
                                    pq.setEntregado(getString(R.string.estPendiente));
                                    pq.setImagenButtonEstado(R.drawable.entregado);
                                    pq.setColorEstado(cVerde);
                                }

                                //Añadir paquete
                                paquetes.add(pq);
                            }
                        //Controlar que al guardar un nuevo registro este cargando los datos
                        }catch (NullPointerException ex){
                        }
                    }
                    //Asignar listado al recyclerview
                    adaptador = new FilaPaquete(paquetes);
                    rvProductos.setLayoutManager(layoutProductos);
                    rvProductos.setAdapter(adaptador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * METODO PARA COMPROBAR SI SE HA ACEPTADO LOS PERMISOS
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode)
        {
            case 1:
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    permisos=true;
                else
                    finishAffinity();

                break;
        }
    }

    //----------------------------------------------------------------------------------------------


    /**
     * METODO PARA ACTIVAR EL ENVIO DE UBICANCION GPS
     */
    public void activarGPS(){
        localizarGPS = new LocalizarGPS(this, user);
    }

    /**
     * METODO PARA MARCAR UN PAQUETE COMO ENTREGADO
     */
    public static void entregarPaquete(String codPaquete){
        baseDatos.getReference("paquetes").child(codPaquete).child("entregado").setValue("si");
    }

    /**
     * METODO PARA MARCAR UN PAQUETE COMO PENDIENTE
     */
    public static void pendientePaquete(String codPaquete){
        baseDatos.getReference("paquetes").child(codPaquete).child("entregado").setValue("no");
    }

    /**
     * METODO PARA ELIMINAR UN PAQUETE DE LA BASE DE DATOS
     * @param codPaquete
     */
    public static void borrarPaquete(String codPaquete){
        baseDatos.getReference("paquetes").child(codPaquete).removeValue();
    }


}
