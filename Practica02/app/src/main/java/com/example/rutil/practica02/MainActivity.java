package com.example.rutil.practica02;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etUsu;
    private EditText etCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referencias
        etUsu = (EditText) findViewById(R.id.etUsuario);
        etCont = (EditText) findViewById(R.id.etContrasena);
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA COMPROBAR SI EXISTE UN USUARIO EN EL SISTEMA
     * @return
     */
    public boolean comprobarUsuario(){
        //Referenciar al fichero de usuarios
        SharedPreferences preferencias = getSharedPreferences("registros", Context.MODE_PRIVATE);
        //Buscar usuario
        String leido = preferencias.getString(etUsu.getText().toString(),  "");
        //Comprobar si se ha localizado
        if(leido.length()==0)
            return false;
        else
            return true;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA COMPROBAR SI SE HAN INTRODUCIDO DATOS EN LOS DOS CAMPOS (USUARIO Y CONTRASEÑA)
     * @return
     */
    public boolean comprobarCampos(){
        if(etUsu.getText().length()>0) {

            if (etCont.getText().length() > 0) {

                return true;

            } else {
                Toast.makeText(this, "DEBES INTRODUCIR UNA CONTRASEÑA", Toast.LENGTH_SHORT).show();
                return false;
            }

        }else {
            Toast.makeText(this, "DEBES INTRODUCIR UN USUARIO", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA INICIAR SESION EN EL SISTEMA CON USUARIO Y CONTRASEÑA
     * @param view
     */
    public void iniciar(View view){
        if(comprobarCampos()) {
            if (comprobarUsuario()) {
                //Referenciar al fichero de usuarios
                SharedPreferences preferencias = getSharedPreferences("registros", Context.MODE_PRIVATE);
                //Guardar contraseña del usuario
                String contrasena = preferencias.getString(etUsu.getText().toString(), "");
                //Comprobar si la contraseña introducida corresponde con la almacenada
                if (etCont.getText().toString().equals(contrasena)) {
                    //Si es correcta accederemos
                    Intent i = new Intent(this, SegundaActivity.class);
                    i.putExtra("usuario", etUsu.getText().toString()); //Pasamos el dato del usuario a la siguiente activity
                    startActivity(i);
                } else
                    Toast.makeText(this, "CONTRASEÑA INCORRECTA", Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(this, "EL USUARIO INDICADO NO EXISTE", Toast.LENGTH_SHORT).show();
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA AÑADIR UN NUEVO USUARIO EN EL SISTEMA AL PULSAR EL BOTON DE REGISTRO (Se añaden los datos al fichero de registros)
     * @param view
     */
    public void registro(View view){
        if(comprobarUsuario()==false){
            if(comprobarCampos()) {
                //Referenciar al fichero de usuarios
                SharedPreferences preferencias = getSharedPreferences("registros", Context.MODE_PRIVATE);
                //Crear objeto editor con el que añadir los datos
                SharedPreferences.Editor editor = preferencias.edit();
                //Guardar usuario y contraseña
                editor.putString(etUsu.getText().toString(), etCont.getText().toString());
                //Guardar los cambios
                editor.commit();
                //Una vez creado el usuario iniciaremos sesion
                iniciar(view);
            }
        }else
            Toast.makeText(this,"EL USUARIO INDICADO YA EXISTE EN EL SISTEMA",Toast.LENGTH_SHORT).show();
    }
}
