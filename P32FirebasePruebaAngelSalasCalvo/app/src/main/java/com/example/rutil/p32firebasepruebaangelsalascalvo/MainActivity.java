package com.example.rutil.p32firebasepruebaangelsalascalvo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText etNombre, etDni, etAp1, etAp2, etEdad;
    FirebaseDatabase baseDatos;
    DatabaseReference agenda, edad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Referencias
        etDni = (EditText) findViewById(R.id.etDni);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etAp1 = (EditText) findViewById(R.id.etAp1);
        etAp2 = (EditText) findViewById(R.id.etAp2);
        etEdad = (EditText) findViewById(R.id.etEdad);

        baseDatos = FirebaseDatabase.getInstance();
        agenda = baseDatos.getReference("TablaAgenda");
    }

    public void logOut(View view) {
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.not_close_session, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void revoke(View view) {
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.not_revoke, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        //Obtener el campo edad
        edad = agenda.child("45603728H").child("Edad");

        //Poner a la escucha para detectar cambios en el campo
        edad.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Capturar nuevo valor y establecerlo
                String valorEdad = dataSnapshot.getValue(String.class);
                etEdad.setText(valorEdad);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void bGuardarClic(View view){
        /*
        //Crear objeto que referenciará a nuestra base de datos
        FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
        //Crear referencia con el nodo con el que queremos actuar (si no esta creado, se crea)
        DatabaseReference gatos = baseDatos.getReference("gato");
        DatabaseReference nodoGato;

        //Crear hijos del nodo
        gato.child("Nombre").setValue("Isidoro");
        gato.child("FechaN").setValue("29/06/2018");


        nodoGato = gatos.child("0255");
        nodoGato.child("Nombre").setValue("Isidora");
        nodoGato.child("FechaN").setValue("06/02/2019");
        nodoGato.child("Color").setValue("Naranja");

        nodoGato = gatos.child("0256");
        nodoGato.child("Nombre").setValue("Doraemon");
        nodoGato.child("FechaN").setValue("06/02/2019");
        nodoGato.child("Color").setValue("Azul y blanco");
        */

        DatabaseReference dni;

        dni = agenda.child(etDni.getText().toString());
        dni.child("Nombre").setValue(etNombre.getText().toString());
        dni.child("Ap1").setValue(etAp1.getText().toString());
        dni.child("Ap2").setValue(etAp2.getText().toString());
        dni.child("Edad").setValue(etEdad.getText().toString());
    }

    /**
     * METODO PARA EL BOTON MODIFICAR
     */
    public void modificarClic(View view){

    }
}
