package com.example.rutil.sendbox.administrador;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import com.example.rutil.sendbox.ActivityInfo;
import com.example.rutil.sendbox.ActivityLogin;
import com.example.rutil.sendbox.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityAdmin extends AppCompatActivity {

    // Autenticacion ============================================
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser user;
    private GoogleApiClient googleApiClient;

    // Base de datos ============================================
    private static FirebaseDatabase baseDatos;

    // RecyclerView =============================================
    private RecyclerView rvUsuarios;
    private RecyclerView.Adapter adaptador;
    private RecyclerView.LayoutManager layoutUsuarios;
    private ArrayList<TransporDatos> listTransport;
    private Context context;
    private MediaPlayer mpClic;

    //----------------------------------------------------------------------------------------------

    /**
     * SOBRESCRITURA DEL METODO ONCREATE
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //Obtener el usuario logueado
        obtenerUsuario();

        baseDatos = FirebaseDatabase.getInstance();
        //Iniciar variables del listado
        context=this;
        rvUsuarios = (RecyclerView) findViewById(R.id.rvTransportistas);
        rvUsuarios.setHasFixedSize(true);
        layoutUsuarios = new LinearLayoutManager(this);
        mpClic = MediaPlayer.create(this, R.raw.clic);

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
     * SOBRESCRITURA DEL METODO QUE SE EJECUTA AL PULSAR EL BOTON DE VOLVER PARA CERRAR LA APP
     */
    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA OBTENER EL OBJETO USUARIO DE FIREBASE QUE REFERENCIA AL USUARIO
     */
    public void obtenerUsuario(){
        //Obtener objeto para cerrar sesion
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

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
                    //Obtener el listado de transportistas
                    obtenertransportistas();
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
                    Auth.GoogleSignInApi.signOut(googleApiClient);
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
     * METODO PARA RELLENAR EL ARRAY DE PAQUETES CON LOS PAQUETES DEL REPARTIDOR
     */
    public void obtenertransportistas(){
        baseDatos.getReference("transportistas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Si se ha obtenido algun dato
                if(dataSnapshot.exists()){
                    listTransport=new ArrayList<TransporDatos>();
                    //Recorrer cada uno de los paquetes del grupo paquetes
                    for(DataSnapshot fila : dataSnapshot.getChildren()){
                        try {
                            //Crear objeto con los datos del transportista
                            TransporDatos t = new TransporDatos();
                            t.setNombre(fila.child("nombre").getValue().toString());
                            t.setDni(fila.child("dni").getValue().toString());
                            t.setMatricula(fila.child("matricula").getValue().toString());
                            t.setFoto(fila.child("foto").getValue().toString());
                            t.setUid(fila.getKey());

                            //Añadir paquete
                            listTransport.add(t);
                            //Controlar que al guardar un nuevo registro este cargando los datos
                        }catch (NullPointerException ex){
                        }
                    }
                    //Asignar listado al recyclerview
                    adaptador = new FilaUsuario(listTransport, context, mpClic);
                    rvUsuarios.setLayoutManager(layoutUsuarios);
                    rvUsuarios.setAdapter(adaptador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}