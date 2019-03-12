package com.example.rutil.sendbox.administrador;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rutil.sendbox.Transportista.ActivityTranspor;
import com.example.rutil.sendbox.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ActivityRegistro extends AppCompatActivity{

    // Autenticacion ============================================
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser user;

    // Base de datos ============================================
    private FirebaseDatabase baseDatos;

    // Vista ====================================================
    private EditText etNombre, etDNI, etMatricula, etValidacion;
    private ImageView ivPerfil;
    private CheckBox cbAdmin;
    private TextView tvError;

    // Otros ====================================================
    private String nombre, uid, codAdmin;

    //----------------------------------------------------------------------------------------------

    /**
     * SOBRESCRITURA DEL METODO ONCREATE
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Inicializar los elementos
        inicio();
        //Obtener el usuario
        obtenerUsuario();
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
     * METODO PARA INICIAR TODOS LOS ELEMENTOS NECESARIOS
     */
    public void inicio(){
        etNombre = (EditText) findViewById(R.id.etNombre);
        etDNI = (EditText) findViewById(R.id.etDNI);
        etMatricula = (EditText) findViewById(R.id.etMatricula);
        etValidacion = (EditText) findViewById(R.id.etValidacion);
        ivPerfil = (ImageView) findViewById(R.id.ivPerfil);
        cbAdmin = (CheckBox) findViewById(R.id.cbAdmin);
        tvError = (TextView) findViewById(R.id.tvError);

        baseDatos = FirebaseDatabase.getInstance();

        //Obtener el codigo de validacion de administrador
        DatabaseReference refCod= baseDatos.getReference("codvalidar");
        refCod.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Obtener valor del codigo de administrador
                codAdmin = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
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
                //Si se ha localizado el usuario, obtenemos su informacion
                if (user != null) {
                    obtenerDatos();
                } else
                    finish();
            }
        };
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA OBTENER LOS DATOS DEL USUARIO CON EL QUE SE HA LOGUEADO
     */
    public void obtenerDatos(){
        //Obtener datos del usuario
        nombre = user.getDisplayName();
        uid = user.getUid();

        //Obtener imagen de perfil y asignarla al ImageView
        Picasso.get().load(user.getPhotoUrl()).into(ivPerfil);
        //Establecer datos por defecto
        etNombre.setText(nombre);
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTARÁ AL SELECCIONAR LA OPCION DE REGISTRO DE ADMINISTRADOR
     * @param view
     */
    public void seleccionAdmin(View view){
        //En funcion de si esta seleccionado mostraremos unos campos u otros
        if (cbAdmin.isChecked()){
            etDNI.setVisibility(View.GONE);
            etMatricula.setVisibility(View.GONE);
            etValidacion.setVisibility(View.VISIBLE);
        }else{
            etDNI.setVisibility(View.VISIBLE);
            etMatricula.setVisibility(View.VISIBLE);
            etValidacion.setVisibility(View.GONE);
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA GUARDAR LOS DATOS INDICADOS EN FIREBASE
     * Se ejecutará al hacer click en aceptar
     * @param view
     */
    public void aceptarClick(View view){
        //Comprobar si se va a añadir la informacion de un administrador
        if(cbAdmin.isChecked()){
            //Comprobar que se ha introducido la informacion
            if(etNombre.getText().length()==0)
                tvError.setText(getString(R.string.errNombre));

            else{
                //Comprobar si el codigo introducido corresponde con el indicado
                if(etValidacion.getText().toString().equals(codAdmin)){
                    tvError.setText("");
                    //Guardar los datos del administrador en el nodo
                    DatabaseReference administradores = baseDatos.getReference("administradores");
                    administradores.child(uid).setValue(etNombre.getText().toString());
                    //Cargar Activity
                    Intent i = new Intent(this, ActivityAdmin.class);
                    startActivity(i);
                }else
                    tvError.setText(getString(R.string.errValidacion));
            }

        //En caso contrario se añadirá la informacion de un transportista
        }else{
            //comprobar que se ha introducido toda la informacion
            if(etNombre.getText().length()==0)
                tvError.setText(getString(R.string.errNombre));
            else if(etDNI.getText().length()==0)
                tvError.setText(getString(R.string.errDNI));
            else if(etMatricula.getText().length()==0)
                tvError.setText(getString(R.string.errMatricula));
            //Si se han introducido todos los datos, se almacenan
            else{
                DatabaseReference refTransp = baseDatos.getReference("transportistas/"+uid);
                refTransp.child("dni").setValue(etDNI.getText().toString());
                refTransp.child("matricula").setValue(etMatricula.getText().toString());
                refTransp.child("nombre").setValue(etNombre.getText().toString());
                refTransp.child("foto").setValue(user.getPhotoUrl().toString());
                refTransp.child("ubicacion/lat").setValue("null");
                refTransp.child("ubicacion/long").setValue("null");

                //Cargar Activity
                Intent i = new Intent(this, ActivityTranspor.class);
                startActivity(i);
            }
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA CANCELAR CUENTA ELIMINANDOLA DE FIREBASE
     * Se ejecutará al hacer clic en cancelar
     * @param view
     */
    public void cancelarClick(View view){
        firebaseAuth.signOut(); //Cerrar la sesion
        user.delete(); //Eliminar usuario
    }
}