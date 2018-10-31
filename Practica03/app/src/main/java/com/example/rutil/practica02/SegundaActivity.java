package com.example.rutil.practica02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class SegundaActivity extends AppCompatActivity {
    private ListView lvCit;
    private RadioButton rbMore, rbRub, rbPeli, rbCas, rbSin;
    private EditText etTlf, etMult;
    private TextView tvUsu;

    private String dias[] = new String[7];
    private String usuario;
    private int diaSeleccionado = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        //Referenciar elementos
        lvCit = (ListView) findViewById(R.id.lvCitas);
        rbMore = (RadioButton) findViewById(R.id.rbMoreno);
        rbRub = (RadioButton) findViewById(R.id.rbRubio);
        rbPeli = (RadioButton) findViewById(R.id.rbPelirrojo);
        rbCas = (RadioButton) findViewById(R.id.rbCastano);
        rbSin = (RadioButton) findViewById(R.id.rbSinPelo);
        etTlf = (EditText) findViewById(R.id.etTelefono);
        etMult = (EditText) findViewById(R.id.etMultilinea);
        tvUsu = (TextView) findViewById(R.id.tvNombre);

        //Recoger el nombre de usuario pasado con el intent desde la activity anterior
        usuario = getIntent().getStringExtra("usuario");

        //Escribir nombre de usuario
        tvUsu.setText(usuario);

        //Comprobamos si el usuario tiene un fichero de información y si no lo tiene lo creamos
        if (comprobarDatos()==false)
            guardarDatos(null);

        //Cargamos los datos del usuario
        recuperarDatos();

        //Acción al pulsar sobre elemento listview
        lvCit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contenidoListView(position); //Establecer el contenido con el nuevo elemento seleccionado marcado
                diaSeleccionado = position;
            }
        });
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA AÑADIR CONTENIDO AL LISTVIEW (DIAS)
     */
    public void contenidoListView(int seleccionado) {
        dias[0] = "Lunes";
        dias[1] = "Martes";
        dias[2] = "Miercoles";
        dias[3] = "Jueves";
        dias[4] = "Viernes";
        dias[5] = "Sabado";
        dias[6] = "Domingo";

        //Añadimos la marca al dia seleccionado
        dias[seleccionado] += " ✔";

        //Añadimos el contenido al ListView
        final ArrayAdapter<String> contenidoLv = new ArrayAdapter<String>(this, R.layout.activity_lista_dias, dias);
        lvCit.setAdapter(contenidoLv);
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA OBTENER EL NOMBRE DEL RADIO BUTTON SELECCIONADO
     * @return
     */
    public String radioSeleccionado() {
        if (rbMore.isChecked())
            return rbMore.getText().toString();
        if (rbRub.isChecked())
            return rbRub.getText().toString();
        if (rbPeli.isChecked())
            return rbPeli.getText().toString();
        if (rbCas.isChecked())
            return rbCas.getText().toString();
        if (rbSin.isChecked())
            return rbSin.getText().toString();
        return "";
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO ENCARGADO DE COMPROBAR SI EXISTE UN FICHERO PARA EL USUARIO CON EL QUE SE ACCEDE, EN CASO CONTRARIO SE CREARÁ UNO.
     */
    public boolean comprobarDatos() {
        //Referenciamos el archivo
        SharedPreferences preferencias = getSharedPreferences(usuario, Context.MODE_PRIVATE);

        //Buscamos los datos del usuario
        String colorDat = preferencias.getString("color", "");

        //Si no se obtienen datos, se crea el fichero con los datos de ese usuario
        if (colorDat.length() == 0)
            return false;
        else
            return true;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO ENCARGADO DE OBTENER LOS DATOS DEL ARCHIVO DE CONFIGURACIÓN DEL USUARIO Y ESCRIBIRLOS EN PANTALLA
     */
    public void recuperarDatos() {
        //Referenciamos el archivo
        SharedPreferences preferencias = getSharedPreferences(usuario, Context.MODE_PRIVATE);

        //Buscamos los datos del usuario
        String colorDat = preferencias.getString("color", "");
        int diaDat = Integer.parseInt(preferencias.getString("dia", "0"));
        String tlfDat = preferencias.getString("tlf", "");
        String textoDat = preferencias.getString("texto", "");

        //***** Escribir en pantalla datos obtenidos del archivo *****
        //************************************************************

        //En funcion del radio guardado seleccionar el boton
        if (colorDat.equalsIgnoreCase("Moreno"))
            rbMore.setChecked(true);
        if (colorDat.equalsIgnoreCase("Rubio"))
            rbRub.setChecked(true);
        if (colorDat.equalsIgnoreCase("Pelirojo"))
            rbPeli.setChecked(true);
        if (colorDat.equalsIgnoreCase("Castaño"))
            rbCas.setChecked(true);
        if (colorDat.equalsIgnoreCase("Sin Pelo"))
            rbSin.setChecked(true);

        //Escribir el contenido del listView en funcion del dato guardado
        contenidoListView(diaDat);

        //Escribir contenido de telefono
        etTlf.setText(tlfDat);

        //Escribir contenido del texto multilinea
        etMult.setText(textoDat);
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO ENCARGADO DE OBTENER LA INFORMACIÓN DE PANTALLA Y ALMACENARLA EN UN ARCHVIO DE CONFIGURACIÓN CON EL NOMBRE DE USUARIO
     */
    public void guardarDatos(View view) {
        //Referenciamos el archivo
        SharedPreferences preferencias = getSharedPreferences(usuario, Context.MODE_PRIVATE);

        //Crear objeto editor con el que añadir los datos
        SharedPreferences.Editor editor = preferencias.edit();

        //Añadir contenido al fichero
        editor.putString("color", radioSeleccionado());
        editor.putString("dia", diaSeleccionado + "");
        editor.putString("tlf", etTlf.getText().toString());
        editor.putString("texto", etMult.getText().toString());

        //Guardar los cambios
        editor.commit();

        //ESCRIBIR DATOS EN EL FICHERO
        escribirFichero();

        //Mostrar confirmación
        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show();
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA VOLVER A LA PANTALLA PINCIPAL
     *
     * @param view
     */
    public void volver(View view) {
        finish();
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA ESCRIBIR EN EL FICHERO DE TEXTO
     */
    public void escribirFichero(){
        boolean guardado=true;
        //Obtener el nombre de usuario para establecerlo al nombre de fichero
        String nomFich = usuario + ".txt";

        //Recuperar contenido mediante los sharedPreferences
        SharedPreferences preferencias = getSharedPreferences(usuario, Context.MODE_PRIVATE); //Obtener el archivo de los sharedPreferences correspondiente al usuario

        //Buscamos los datos del usuario
        String colorDat = preferencias.getString("color", "");
        int diaDat = Integer.parseInt(preferencias.getString("dia", "0"));
        String diaTxtDat="";
        String tlfDat = preferencias.getString("tlf", "");
        String textoDat = preferencias.getString("texto", "");

        switch (diaDat){
            case 0: diaTxtDat="Lunes";
                break;
            case 1: diaTxtDat="Martes";
                break;
            case 2: diaTxtDat="Miercoles";
                break;
            case 3: diaTxtDat="Jueves";
                break;
            case 4: diaTxtDat="Viernes";
                break;
            case 5: diaTxtDat="Sabado";
                break;
            case 6: diaTxtDat="Domingo";
                break;
        }

        //Crear variable con el contenido que se guardará en el archivo de texto
        String contenido = "USUARIO:"+usuario+"\nCOLOR DE PELO:"+colorDat+"\nDÍA PARA CITA:"+diaTxtDat+
                "\nTELEFONO:"+tlfDat+"\nTEXTO MULTILINEA:"+textoDat;

        //Escribir Archivo
        try {
            //Obtenemos la ruta de la sd
            File rutaSD = Environment.getExternalStorageDirectory();
            File destino = new File(rutaSD.getAbsoluteFile() + "/" + nomFich);

            //Guardamos fichero en la SD
            PrintWriter flujoS = new PrintWriter(new FileOutputStream(destino));
            flujoS.write(contenido);
            //Cerrar el flujo de salida
            flujoS.close();

            //Mostrar confirmación
            Toast.makeText(this, nomFich+" guardado! ( Ruta: "+rutaSD.getAbsolutePath()+" )", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "ERROR: No se puede guardar el fichero"+nomFich, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Vuelve a guardar la información"+nomFich, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "ERROR: No se puede guardar el fichero"+nomFich, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Vuelve a guardar la información"+nomFich, Toast.LENGTH_SHORT).show();
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA PASAR A LA TERCERA ACTIVITY
     */
    public void siguienteActivity(View view){
        Intent i = new Intent(this, TerceraActivity.class);
        i.putExtra("fichero", usuario+".txt");
        startActivity(i);
    }

}