package com.example.angel.p08almacenamientodatospreferencesangel;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    /*
        LAS SHAREPREFERENCES SON UN TIPO DE FICHEROS QUE SE UTILIZAN PARA GUARDAR INFORMACIÓN DE
        CONFIGURACIÓN O PREFERENCIAS, ES DECIR POCA INFORMACIÓN (ALMACENAMIENTO DE VARIABLES IDENTIFICANDOSE
        POR UN NOMBRE Y CON UN VALOR DETERMINADO)
     */
    private EditText etEmailM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referencias
        etEmailM = (EditText) findViewById(R.id.etEmail);

        //Primero recuperamos email en caso de haber sido guardado previamente
        //Creamos un objeto SharedPreferences referenciando al "fichero" donde se almacenan todas las configuraciones (lo referenciamos con su nombre "datos")
        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE); //EL unico modo que podemos utilizar es el privado ya que el resto está en desuso
        etEmailM.setText(preferencias.getString("correo", "")); //Obtenemos el dato "email" del fichero "datos" (En caso de que no lo encuentre no pondra nada)
    }

    /**
     * METODO QUE SE EJECUTARÁ AL HACER CLIC SOBRE EL BOTON GUARDAR
     * Guardará email indicado
     * @param view
     */
    public void guardarClick(View view){
        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);//Referenciar el fichero donde guardar las preferencias
        //Crear objeto editor para editar el fichero de preferencias (datos)
        SharedPreferences.Editor editor = preferencias.edit();
        //Añadir el contenido al fichero en un campo llamado "correo" con el objeto editor
        editor.putString("correo", etEmailM.getText().toString());
        //Confirmar los cambios (Hacer un commit)
        editor.commit();

        //Finalizamos la aplicacion (Esto no es necesario, simplemente es para realizar pruebas)
        finish();
    }
}
