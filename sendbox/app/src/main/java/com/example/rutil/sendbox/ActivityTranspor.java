package com.example.rutil.sendbox;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityTranspor extends AppCompatActivity {

    // Autenticacion ============================================
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser user;

    // Base de datos ============================================
    private FirebaseDatabase baseDatos;

    // RecyclerView =============================================
    private RecyclerView rvProductos;
    private RecyclerView.Adapter adaptador;
    private RecyclerView.LayoutManager layoutProductos;

    // Otros =====================================================
    ArrayList<PaqueteDatos> paquetes;
    FloatingActionButton bCrear;

    //----------------------------------------------------------------------------------------------

    /**
     * SOBRESCRITURA DEL METODO ONCREATE
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transpor);
        baseDatos = FirebaseDatabase.getInstance();
        //Obtener el usuario logueado
        obtenerUsuario();
        //Obtener los listados de todos los paquetes del usuario
        obtenerPaquetes();

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
                                    pq.setEstado(getString(R.string.estEntregado));
                                } else {
                                    pq.setImgEstado(R.drawable.pendiente);
                                    pq.setEstado(getString(R.string.estPendiente));
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
}
