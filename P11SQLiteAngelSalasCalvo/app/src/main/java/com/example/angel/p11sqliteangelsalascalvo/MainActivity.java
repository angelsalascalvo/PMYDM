package com.example.angel.p11sqliteangelsalascalvo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etCodigom, etDescripcionm, etPreciom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referenciar componentes
        etCodigom = (EditText) findViewById(R.id.etCodigoProd);
        etDescripcionm = (EditText) findViewById(R.id.etDescripcionProd);
        etPreciom = (EditText) findViewById(R.id.etPrecioProd);
    }

    //No olvidar crear la clase .java para poder administrar y poder crear nuestra base de datos SQLite
    public void registrarClick(View view){
        //Crear la base de datos con los datos correspondientes
        AdminSQLiteOpenHelper administracion = new AdminSQLiteOpenHelper(this, "administracion",null, 1);
        //Abrir base de datos
        SQLiteDatabase BaseDeDatos = administracion.getWritableDatabase();

        //Almacenamos los datos introducidos por pantalla
        String codigoValor = etCodigom.getText().toString();
        String descripcionValor = etDescripcionm.getText().toString();
        String precioValor = etPreciom.getText().toString();

        //Comprobamos que los campos de texto no se encuentren vacios
        if (codigoValor.isEmpty()||descripcionValor.isEmpty()||precioValor.isEmpty()) {
            //Cerramos la base de datos para no dejarla abierta
            BaseDeDatos.close();
            Toast.makeText(this, "Debe completar todos los datos del producto", Toast.LENGTH_SHORT).show();
        }else{
            //Si no estan vacios
            //Creamos la fila que vamos a añadir, la instanciamos y añadimos los valores a cada una de sus columnas
            ContentValues fila = new ContentValues();
            fila.put("codigo", codigoValor);
            fila.put("descripcion", descripcionValor);
            fila.put("precio", precioValor);
            //Insertar fila en una tabla
            BaseDeDatos.insert("articulos", null, fila);

            //Cerramos la base de datos para no dejarla abierta, realizando su commit correspondiente
            BaseDeDatos.close();
        }

        //Limpiar los campos de texto
        etCodigom.setText("");
        etDescripcionm.setText("");
        etPreciom.setText("");

        //Indicar al usuario que se ha añadido el producto
        Toast.makeText(this, "Producto insertado!", Toast.LENGTH_SHORT).show();
    }


}
