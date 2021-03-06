package com.example.rutil.sendbox.Transportista;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rutil.sendbox.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ActivityCrearPaquete extends AppCompatActivity{

    // Autenticacion ============================================
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser user;

    // Base de datos ============================================
    private FirebaseDatabase baseDatos;

    // Otros ====================================================
    private EditText etNombreDest, etCodigo, etDireccion;
    private TextView tvError;
    private boolean valido;
    private Button bAnadir;
    private ZXingScannerView scanner;
    public static final String CODIGO_BARRAS = "codBarras";
    public static final int CODIGO_RESPUESTA =1234;


    //----------------------------------------------------------------------------------------------

    /**
     * SOBRESCRITURA DEL METODO ON CREATE
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_paquete);
        //Obtener usuario registrado
        obtenerUsuario();
        //Iniciar variables
        baseDatos = FirebaseDatabase.getInstance();
        etNombreDest = (EditText) findViewById(R.id.etNombreDestCp);
        etCodigo = (EditText) findViewById(R.id.etCodigoCp);
        etDireccion = (EditText) findViewById(R.id.etDireccionCp);
        tvError = (TextView) findViewById(R.id.tvErrorCp);
        bAnadir = (Button) findViewById(R.id.bAnadirCp);

        //Poner boton a la escucha
        bAnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anadirClic();
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
     * METODO QUE SE EJECUTARÁ AL HACER CLIC EN EL BOTON AÑADIR
     */
    public void anadirClic(){
        //Comprobaciones
        if(etNombreDest.getText().length()==0 || etDireccion.getText().length()==0 ||
                etCodigo.getText().length()==0)
            tvError.setText(getString(R.string.errCampos));
        else {
            valido = true;
            final DatabaseReference listPaquetes = baseDatos.getReference("paquetes");
            //Comprobar que no existe un producto con ese codigo
            listPaquetes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot fila : dataSnapshot.getChildren()) {
                        if (fila.getKey().equalsIgnoreCase(etCodigo.getText().toString())) {
                            valido = false;
                        }
                    }
                    //Una vez comprobado llamar al metodo para guardar datos si es posible
                    guardarDatos();
                    //Eliminar el listener
                    listPaquetes.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA ALMACENAR LOS DATOS SI ES UN CODIGO VALIDO
     */
    public void guardarDatos(){
        //Si es valido el codigo actuamos
        if (valido) {
            //Guardar datos
            DatabaseReference pq = baseDatos.getReference("paquetes").child(etCodigo.getText().toString());
            pq.child("destinatario").setValue(etNombreDest.getText().toString());
            pq.child("direccion").setValue(etDireccion.getText().toString());
            pq.child("entregado").setValue("no");
            pq.child("transportista").setValue(user.getUid());

            Toast.makeText(this, getString(R.string.guardado), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            tvError.setText(getString(R.string.errDuplicado));
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA DESCARTAR LOS CAMBIOS Y VOLVER A LA ANTERIOR ACTIVITY
     * @param view
     */
    public void descartarClic(View view){
        finish();
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
            }
        };
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTARÁ AL PULSAR EL BOTON DE CODIGO DE BARRAS
     * Abrirña la vista que permitirá escanear el codigo de barras
     * @param view
     */
    public void leerCodigo(View view){
        Intent i = new Intent(this, ActivityScanner.class);
        startActivityForResult(i, CODIGO_RESPUESTA);
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA OBTENER EL CODIGO LEIDO CON EL LECTOR
     * @param reqCode
     * @param resCode
     * @param datos
     */
    public void onActivityResult(int reqCode, int resCode, Intent datos){
        if(reqCode== CODIGO_RESPUESTA && resCode==RESULT_OK){
            //Establecer codigo en el cuadro de texto
            etCodigo.setText(datos.getStringExtra(CODIGO_BARRAS));
        }
        super.onActivityResult(reqCode, resCode, datos);
    }
}