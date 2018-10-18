package com.example.angel.p09agendaangelsalas;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText etNomb, etInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Referencias
        etNomb=(EditText) findViewById(R.id.etNombre);
        etInfo=(EditText) findViewById(R.id.etInformacion);
    }

    /**
     * METODO ENCARGADO DE GUARDAR UN NUEVO CONTACTO
     * @param view
     */
    public void GuardarClick(View view){
        //Referencia Archivo de preferencias (Fichero Agenda)
        SharedPreferences preferencias = getSharedPreferences("Agenda", Context.MODE_PRIVATE);
        //Crear objeto editor con el que añadir los datos
        SharedPreferences.Editor editor = preferencias.edit();
        //Escibir datos en el fichero
        editor.putString(etNomb.getText().toString(),etInfo.getText().toString()); //Se creará un registro que tendra por identificador el nombre de usuario y por contenido lo que se escriba en el editText "etInformacion".
        //Guardar los cambios
        editor.commit();
        //Mostrar indicacion al usuario de que ha sido guardado el dato
        Toast.makeText(this,"Contacto guardado",Toast.LENGTH_SHORT).show();
        //Limpiar los elementos
        Limpiar(view);
    }

    /**
     * METODO ENCARGADO DE RECUPERAR INFORMACIÓN DE UN CONTACTO
     * @param view
     */
    public void BuscarClick(View view){
        //Referencia Archivo de preferencias (Fichero Agenda)
        SharedPreferences preferencias = getSharedPreferences("Agenda", Context.MODE_PRIVATE);
        //Buscamos el dato que tiene por nombre el indicado en el editText(), si no lo encuentra, obtiene nada.
        String datos = preferencias.getString((etNomb.getText()).toString(), "");

        //Comprobamos si se ha encontrado algo, en caso contrario lo indicaremos
        if (datos.equals("")) {
            Toast.makeText(this, "Contacto no encontrado", Toast.LENGTH_SHORT).show();
            Limpiar(view);
        }else
            etInfo.setText(datos);
    }

    /**
     * METODO ENCARGADO DE LIMPIAR EL TEXTO DE LOS COMPONENTES
     * @param view
     */
    public void Limpiar(View view){
        etNomb.setText("");
        etInfo.setText("");
    }
}
