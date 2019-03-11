package com.example.rutil.sendbox;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ActivityUsuarioInfo extends AppCompatActivity {

    // Autenticacion ============================================
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser user;

    // Base de datos ============================================
    private static FirebaseDatabase baseDatos;

    // RecyclerView =============================================
    private RecyclerView rvPaquetesUsu;
    private RecyclerView.Adapter adaptador;
    private RecyclerView.LayoutManager layoutPaqUsuarios;
    private int cGris, cVerde;
    private ArrayList<PaqueteDatos> paquetes;
    private String uid;
    private TextView tvNombre, tvMatricula, tvDni;
    private ImageView ivFoto;
    private MediaPlayer mpBorrar, mpClic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_info);
        //Referencias
        ivFoto = (ImageView) findViewById(R.id.ivUsuInfo);
        tvNombre = (TextView) findViewById(R.id.tvNombreInfo);
        tvDni = (TextView) findViewById(R.id.tvDNIInfo);
        tvMatricula = (TextView) findViewById(R.id.tvMatriculaInfo);
        mpClic = MediaPlayer.create(this, R.raw.clic);
        mpBorrar = MediaPlayer.create(this, R.raw.eliminar);


        //Obtener UID del usuario solicitado
        uid = getIntent().getStringExtra("uid");

        //Obtener el usuario logueado
        obtenerUsuario();

        baseDatos = FirebaseDatabase.getInstance();
        cGris=ContextCompat.getColor(this, R.color.gris);
        cVerde=ContextCompat.getColor(this, R.color.verde);

        //Iniciar variables del listado
        rvPaquetesUsu = (RecyclerView) findViewById(R.id.rvPaquetesUsu);
        rvPaquetesUsu.setHasFixedSize(true);
        layoutPaqUsuarios = new LinearLayoutManager(this);
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
        super.onStop();
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
                    //Rellenar el listado con los paquetes del transportista seleccionado
                    obtenerPaquetesUsuarios();
                    //Establecer los datos del perfil del transportista
                    obtenerDatosTransp();
                }
            }
        };
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA OBTENER EL MENU DEL ACTIVITY
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA REALIZAR ACCIONES SEGUN LA OPCION SELECCIONADA
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.iCerrarSesionAdmin:
                if(firebaseAuth!=null)
                    firebaseAuth.signOut(); //Cerrar la sesion
                break;
            case R.id.iMapa:
                //Abrir el activity del mapa
                Intent i = new Intent(this, MapsActivity.class);
                startActivity(i);
                break;
            case R.id.iAcerca:
                Intent intentInfo = new Intent(this, ActivityInfo.class);
                startActivity(intentInfo);
        }
        return super.onOptionsItemSelected(item);
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA RELLENAR EL ARRAY DE PAQUETES CON LOS PAQUETES DEL TRANSPORTISTA
     */
    public void obtenerPaquetesUsuarios(){
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
                            if (t.equals(uid)) {
                                Log.d("logig", t);
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
                    adaptador = new FilaPaquete(paquetes, false, mpBorrar, mpClic);
                    rvPaquetesUsu.setLayoutManager(layoutPaqUsuarios);
                    rvPaquetesUsu.setAdapter(adaptador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA OBTENER LOS DATOS DEL PERFIL DEL TRANSPORTISTA
     */
    public void obtenerDatosTransp(){
        baseDatos.getReference("transportistas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot fila : dataSnapshot.getChildren()){

                    //Si corresponde con el transportista seleccionado se muestran los datos
                    if (fila.getKey().equals(uid)) {
                        try {
                            //Establecer datos
                            tvNombre.setText(fila.child("nombre").getValue().toString());
                            tvDni.setText(fila.child("dni").getValue().toString());
                            tvMatricula.setText(fila.child("matricula").getValue().toString());

                            //Establecer foto perfil
                            String urlFoto = fila.child("foto").getValue().toString();
                            Picasso.get().load(urlFoto).into(ivFoto);

                        //Controlar que se empiece a leer info de un nuevo usuario
                        }catch(NullPointerException npe){
                        };
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}