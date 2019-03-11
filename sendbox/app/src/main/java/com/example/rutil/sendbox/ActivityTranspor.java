package com.example.rutil.sendbox;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ActivityTranspor extends AppCompatActivity {

    // Autenticacion ============================================
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser user;

    // Base de datos ============================================
    private static FirebaseDatabase baseDatos;

    // RecyclerView =============================================
    private RecyclerView rvPaquetes;
    private RecyclerView.Adapter adaptador;
    private RecyclerView.LayoutManager layoutPaquetes;

    // Otros =====================================================
    private ArrayList<PaqueteDatos> paquetes;
    private FloatingActionButton bCrear;
    private int cGris, cVerde;
    public static String sEntregado, sPendiente;
    private LocalizarGPS localizarGPS= new LocalizarGPS();
    private TextView tvNombrePer, tvDNIPer, tvMatPer;
    private ImageView ivFotoPer;
    private boolean creadoGPS;
    private MediaPlayer mpBorrar, mpClic;


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
        creadoGPS=false;
        mpClic = MediaPlayer.create(this, R.raw.clic);
        mpBorrar = MediaPlayer.create(this, R.raw.eliminar);

        //Obtener el usuario logueado
        obtenerUsuario();

        //Iniciar variables del listado
        rvPaquetes = (RecyclerView) findViewById(R.id.rvPaquetes);
        rvPaquetes.setHasFixedSize(true); //Indicar que el tamaño del listado no depende de los elemntos que tenga
        layoutPaquetes = new LinearLayoutManager(this);

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
        //Quitamos el escuchador si estaba instanciado
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
        if(localizarGPS.lManager!=null) {
            localizarGPS.detener();
            Log.d("wwwww", "gpsDetenido");
        }
        super.onStop();
    }

    //----------------------------------------------------------------------------------------------

    @Override
    protected void onResume() {
        if(localizarGPS.lManager==null && creadoGPS)
            localizarGPS.iniciar();
        Log.d("wwwww", "gpsActivado");
        super.onResume();
    }


    //----------------------------------------------------------------------------------------------

    /**
     * SOBRESCRITURA DEL METODO QUE SE EJECUTA AL PULSAR EL BOTON DE VOLVER PARA CERRAR LA APP
     */
    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
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
                    //Activar envio ubicacion si no esta ya creado
                    if(!creadoGPS)
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
        baseDatos.getReference("transportistas").child(user.getUid()).child("foto").setValue(user.getPhotoUrl().toString());
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
                    adaptador = new FilaPaquete(paquetes, true, mpBorrar, mpClic);
                    rvPaquetes.setLayoutManager(layoutPaquetes);
                    rvPaquetes.setAdapter(adaptador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //----------------------------------------------------------------------------------------------


    /**
     * METODO PARA ACTIVAR EL ENVIO DE UBICANCION GPS
     */
    public void activarGPS(){
        localizarGPS = new LocalizarGPS(this, user);
        localizarGPS.iniciar();
        creadoGPS=true;
        Log.d("wwwww", "gpsCreado");
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

    /**
     * METODO PARA OBTENER EL MENU DEL ACTIVITY
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_transpor, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * METODO PARA REALIZAR ACCIONES SEGUN LA OPCION SELECCIONADA
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.iCerrarSesion:
                if(firebaseAuth!=null)
                    firebaseAuth.signOut(); //Cerrar la sesion
                break;
            case R.id.iPerfil:
                mostrarPerfil();
                break;
            case R.id.iAcercaT:
                Intent intentInfo = new Intent(this, ActivityInfo.class);
                startActivity(intentInfo);
        }
        return super.onOptionsItemSelected(item);
    }



    public void mostrarPerfil(){
        //Declarar layout para establecer datos
        LayoutInflater factory = LayoutInflater.from(this);
        View dialog = factory.inflate(R.layout.dialogo_perfil, null);

        //Obtener Referencias
        tvNombrePer = (TextView) dialog.findViewById(R.id.tvNombrePer);
        tvDNIPer = (TextView) dialog.findViewById(R.id.tvDNIPer);
        tvMatPer = (TextView) dialog.findViewById(R.id.tvMatPer);
        ivFotoPer = (ImageView) dialog.findViewById(R.id.ivFotoPer);
        //Obtener imagen de perfil y asignarla al ImageView
        Picasso.get().load(user.getPhotoUrl()).into(ivFotoPer);


        AlertDialog.Builder builder = new AlertDialog.Builder(this)
        .setView(dialog)
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })
        .setCancelable(false);

        //Crear dialogo del perfil
        final AlertDialog dialogoPerfil = builder.create();

        //Obtener los datos del perfil
        final DatabaseReference refTran = baseDatos.getReference("transportistas");
        refTran.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot fila : dataSnapshot.getChildren()){
                    if(fila.getKey().equals(user.getUid())){
                        tvNombrePer.setText(fila.child("nombre").getValue().toString());
                        tvDNIPer.setText(fila.child("dni").getValue().toString());
                        tvMatPer.setText(fila.child("matricula").getValue().toString());
                    }
                    //Una vez obtenidos los datos mostrar dialogo perfil
                    dialogoPerfil.show();
                    //Desactivar escucha de datos
                    refTran.removeEventListener(this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
