package com.example.rutil.sendbox;

import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityLogin extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // Autenticacion ============================================
    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private SignInButton signInButton;
    public static final int SIGN_IN_CODE = 777;

    // Base de datos ============================================
    private FirebaseDatabase baseDatos;
    private ArrayList<String> uidAdmins = new ArrayList<String>(); //UID usuarios administradores
    private ArrayList<String> uidTransp = new ArrayList<String>(); //UID usuarios transportistas

    // Otros ====================================================
    private boolean adminObtenidos, transpObtenidos, iniciado;
    //----------------------------------------------------------------------------------------------

    /**
     * SOBRESCRITURA DEL METODO ONCREATE
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        adminObtenidos=false;
        transpObtenidos=false;
        iniciado=false;

        //Referencia a la base de datos
        baseDatos = FirebaseDatabase.getInstance();
        //Referencia al objeto de autenticacion de firebase (usuario)
        firebaseAuth = FirebaseAuth.getInstance();
        // Llamada a los metodos para obtener los diferentes usuarios
        obtenerAdministradores();
        obtenerTransportistas();
    }

    //----------------------------------------------------------------------------------------------

    /**
     * SOBRESCRITURA DEL METODO ONSTART
     */
    @Override
    protected void onStart() {
        super.onStart();
        //Añade el escuchador para obtener cuenta de firebase
        if(firebaseAuthListener!=null)
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
     * METODO PARA INICIAR LA FUNCIONALIDAD DEL ACTIVITY
     */
    public void iniciar(){
        //Se comprueba si ya esta logueado de anteriores sesiones
        if(firebaseAuth!=null){
            //Si se esta logueado obtenemos el usuario
            FirebaseUser user = firebaseAuth.getCurrentUser();
            //Abrimos la activity correspondiente al usuario
            siguienteActivity(user);
        }
        //Si no esta logueado, se abre el inicio de sesion con google (seleccion de cuenta)
        else{
            inicioSesion();
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA INICIALIZAR Y LLAMAR ABRIR EL DIALOGO DE SELECCION DE CUENTAS DE GOOGLE PARA EL LOGUEO
     */
    public void inicioSesion(){

        //Ajustes de la ventana de inicio de sesion propia de google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //Ventana de inicio de sesion propia de google (Dialogo emergente)
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Boton Iniciar sesion, programar accion al pulsarlo
        signInButton = (SignInButton) findViewById(R.id.signInButton);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abrir el intent correspondiente con la ventana de inicio de sesion para seleccionar correo
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });

        //Objeto de escucha para obtener el usuario del listado de utenticacion de firebase
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    siguienteActivity(user);
                }
            }
        };
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTA AL FINALIZAR LA VENTANA DE SELECCION DE CUENTA DE GOOGLE PARA EL INICIO DE SESION
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Se comprueba si se ha obtenido el codigo enviado satisfactoriamente (requestCode)
        if (requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * SI SE OBTIENE BIEN LA CUENTA SE ASIGNA LA CUENTA SELECCIONADA AL LISTADO DE AUTENTICACION DE FIREBASE
     * @param result
     */
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            //Llamada al metodo para agregar la cuenta al listado de firebase
            firebaseAuthWithGoogle(result.getSignInAccount());
        } else {
            Toast.makeText(this, R.string.not_log_in, Toast.LENGTH_SHORT).show();
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA ALMACENAR LA CUENTA SELECCIONADA EN EL LISTADO DE FIREBASE AUTH
     * @param signInAccount
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
        //Pasar el token para almacenar el usuario
        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.not_firebase_auth, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO ENCARGADO DE ABRIR EL SIGUIENTE INTENT DEPENDIENDO DEL TIPO DE USUARIO
     * Si es administrador, transportista o aun no ha indicado sus datos.
     * @param user
     */
    private void siguienteActivity(FirebaseUser user) {
        boolean admin = false;
        boolean transp = false;

        //Comprobar si el usuario es administrador
        for(int i=0; i<uidAdmins.size();i++){
            if(user.getUid().equals(uidAdmins.get(i)))
                admin=true;
        }
        Log.d("Datito", "Tamano "+uidTransp.size());
        //Si no es administrador comprobar si esta registrado como transportista
        if(!admin){
            Log.d("Datito", "Tamano "+uidTransp.size());
            for(int i=0; i<uidTransp.size();i++){
                Log.d("Datito", ""+uidTransp.get(i));
                if(user.getUid().equals(uidTransp.get(i)))
                    transp=true;
            }
        }

        //Iniciar activity segun el tipo de usuario
        if(admin){
            Log.d("Datito", "Adminnnnnnnnnnnnnnnnnnnnnnnn");

        }else if(transp){
            Log.d("Datito", "Transssssssssssss");
            Intent i = new Intent(this, ActivityTranspor.class);
            startActivity(i);

        //En caso contrario se mostrará el activity de registro
        }else{
            Log.d("Datito", "No registradooooooooo");
            Intent i = new Intent(this, ActivityRegistro.class);
            startActivity(i);
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA OBTENER LOS UID DE LOS USUARIOS ADMINISTRADORES DE LA APLICACION
     * Se almacenan en el array correspondiente
     */
    public void obtenerAdministradores(){
        final DatabaseReference administradores = baseDatos.getReference("administradores");

        // Poner a la escucha el nodo de administradores
        administradores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Cuando se produce un cambio en el nodo, se actualiza en el ArrayList
                uidAdmins=new ArrayList<String>();
                for(DataSnapshot hijo : dataSnapshot.getChildren()) {
                    uidAdmins.add(hijo.getKey());
                }
                adminObtenidos=true;

                //Al obtener los datos si se han obtenido los de transportistas
                // y no se ha iniciado se inicia la funcionalidad
                if(transpObtenidos && iniciado==false){
                    iniciar();
                    iniciado = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA OBTENER LOS UID DE LOS TRANSPORTISTAS DE LA APLICACIÓN
     * Se almacenan en el array correspondiente
     */
    public void obtenerTransportistas(){
        DatabaseReference transportistas = baseDatos.getReference("transportistas");

        //Poner a la escucha el nodo de transportistas
        transportistas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Al producirse un cambio en un nodo obtenemos los identificadores de los usuarios
                //transportistas y los almacenamos en el array.
                uidTransp=new ArrayList<String>();
                for(DataSnapshot hijo : dataSnapshot.getChildren()) {
                    uidTransp.add(hijo.getKey());
                    Log.d("Datito", "el bueno"+hijo.getKey());
                }
                transpObtenidos=true;

                //Al obtener los datos si se han obtenido los de administradores
                // y no se ha iniciado se inicia la funcionalidad
                if(adminObtenidos && iniciado==false){
                    iniciar();
                    iniciado = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //----------------------------------------------------------------------------------------------

    /**
     * SOBRESCRITURA DEL METODO onConnectionFailed
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}