package com.example.angel.p11sqliteangelsalascalvo;

import android.content.ContentValues;
import android.database.Cursor;
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
        //Instancia referenciando base de datos ABIERTA par escritura
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

            //Limpiar los campos de texto
            etCodigom.setText("");
            etDescripcionm.setText("");
            etPreciom.setText("");

            //Indicar al usuario que se ha añadido el producto
            Toast.makeText(this, "Producto insertado!", Toast.LENGTH_SHORT).show();
        }
    }

    //----------------------------------------------------------------------------------------------

    public void buscarClick(View view) {
        //Crear la base de datos con los datos correspondientes
        AdminSQLiteOpenHelper administracion = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        //Instancia referenciando base de datos ABIERTA par escritura
        SQLiteDatabase BaseDeDatos = administracion.getReadableDatabase();
        //Almacer al codigo por el que vamos a buscar
        String codigo = etCodigom.getText().toString();

        //Comprobar que el campo no esta vacio
        if(codigo.isEmpty()==false){
            String sentencia = "select descripcion, precio from articulos where codigo = "+codigo+";";
            //Creamos un cursor que es una referencia al conjunto de filas que devuelve un seclect;
            Cursor fila = BaseDeDatos.rawQuery(sentencia, null); //El null hace referencia a las columnas del cursor con las que queremos trabajar, en este caso todas

            //Mover el cursor al principio de los datos devueltos por la consulta, devuelve si se puede mover correctamente o no (boolean),
            // es decir hay datos en esa posicion
            if(fila.moveToFirst()){
                //Obtener los datos del resultado del select (cursor)
                etDescripcionm.setText(fila.getString(0));
                etPreciom.setText(fila.getString(1));
            }else
                Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(this, "Introduce un código de producto", Toast.LENGTH_SHORT).show();

        //Cerramos la base de datos para no dejarla abierta
        BaseDeDatos.close();
    }

    //----------------------------------------------------------------------------------------------

    public void modificarClick(View view){
        //Crear la base de datos con los datos correspondientes
        AdminSQLiteOpenHelper administracion = new AdminSQLiteOpenHelper(this, "administracion",null, 1);
        //Instancia referenciando base de datos ABIERTA par escritura
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
            //Creamos la fila que vamos a modifica, la instanciamos y añadimos los valores a cada una de sus columnas
            ContentValues fila = new ContentValues();
            fila.put("codigo", codigoValor);
            fila.put("descripcion", descripcionValor);
            fila.put("precio", precioValor);

            //modificar un registro de la tabla (descripcion y/o precio) donde el codigo sea igual al codigo
            //estableciendo por contenido el contentValues que hemos creado con la informacion.
            // Y devuelve el numero de tuplas modificadas
            int numModificadas = BaseDeDatos.update("articulos", fila,"codigo="+codigoValor ,null);

            //Comprobar si se ha modificado
            if(numModificadas==0) {
                Toast.makeText(this, "No se ha modificado el producto", Toast.LENGTH_SHORT).show();
            }else if(numModificadas == 1) {
                Toast.makeText(this, "Producto modificado!", Toast.LENGTH_SHORT).show();

                //Limpiar los campos de texto
                etCodigom.setText("");
                etDescripcionm.setText("");
                etPreciom.setText("");
            }

            //Cerramos la base de datos para no dejarla abierta, realizando su commit correspondiente
            BaseDeDatos.close();
        }
    }

    public void eliminarClick(View view) {
        //Crear la base de datos con los datos correspondientes
        AdminSQLiteOpenHelper administracion = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        //Instancia referenciando base de datos ABIERTA par escritura
        SQLiteDatabase BaseDeDatos = administracion.getWritableDatabase();

        //Almacenamos los datos introducidos por pantalla
        String codigoValor = etCodigom.getText().toString();

        //Comprobamos que los campos de texto no se encuentren vacios

        if (codigoValor.isEmpty()) {
            //Cerramos la base de datos para no dejarla abierta
            BaseDeDatos.close();
            Toast.makeText(this, "Debe introducir el codigo producto a borrar", Toast.LENGTH_SHORT).show();
        } else {
            //modificar un registro de la tabla (descripcion y/o precio) donde el codigo sea igual al codigo introducido en el campo de texto
            // Y devuelve el numero de tuplas eliminadas
            int numEliminadas = BaseDeDatos.delete("articulos", "codigo=" + codigoValor, null);

            //Comprobar si se ha eliminado
            if (numEliminadas == 0) {
                Toast.makeText(this, "No se ha podido borrar ningun producto con dicho codigo", Toast.LENGTH_SHORT).show();
            } else if (numEliminadas == 1) {
                Toast.makeText(this, "Producto eliminado!", Toast.LENGTH_SHORT).show();

                //Limpiar los campos de texto
                etCodigom.setText("");
                etDescripcionm.setText("");
                etPreciom.setText("");
            }

            //Cerramos la base de datos para no dejarla abierta, realizando su commit correspondiente
            BaseDeDatos.close();
        }
    }
}

/*
    Campo color de tipo editText plain, incluir el nuevo campo de tipo color en la base de datos

    Boton listar todos: Abre un nuevo activity con un control de texto multilinea que muestre el resultado de la consulta,
    en este activity tambien boton de volver.

    Aplicacion p11Repaso2 con una base de datos del tema que queramos con los mismos botones que la anterior
    y añadir boton de buscar por otro campo diferente, mostrar información en el segundo activity a traves de multiline o list view
 */