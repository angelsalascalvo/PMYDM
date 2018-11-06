package com.example.angel.p11sqliteangelsalascalvo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etIdentificadorm, etNombrem, etResidenciam, etEdadm, etCurso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referenciar componentes
        etIdentificadorm = (EditText) findViewById(R.id.etIdentificadorAlu);
        etNombrem = (EditText) findViewById(R.id.etNombreAlu);
        etResidenciam = (EditText) findViewById(R.id.etResidenciaAlu);
        etEdadm = (EditText) findViewById(R.id.etEdadAlu);
        etCurso = (EditText) findViewById(R.id.etCursoAlu);
    }

    //----------------------------------------------------------------------------------------------

    //No olvidar crear la clase .java para poder administrar y poder crear nuestra base de datos SQLite
    public void registrarClick(View view){
        //Crear la base de datos con los datos correspondientes
        AdminSQLiteOpenHelper instituto = new AdminSQLiteOpenHelper(this, "instituto",null, 1);
        //Instancia referenciando base de datos ABIERTA par escritura
        SQLiteDatabase BaseDeDatos = instituto.getWritableDatabase();

        //Almacenamos los datos introducidos por pantalla
        String identificadorValor = etIdentificadorm.getText().toString();
        String nombreValor = etNombrem.getText().toString();
        String residenciaValor = etResidenciam.getText().toString();
        String edadValor = etEdadm.getText().toString();
        String cursoValor = etCurso.getText().toString();

        //Comprobamos que los campos de texto no se encuentren vacios
        if (identificadorValor.isEmpty()||nombreValor.isEmpty()||residenciaValor.isEmpty()||edadValor.isEmpty()||cursoValor.isEmpty()) {
            //Cerramos la base de datos para no dejarla abierta
            BaseDeDatos.close();
            Toast.makeText(this, "Debe completar todos los datos del alumno", Toast.LENGTH_SHORT).show();
        }else{
            //Si no estan vacios
            //Creamos la fila que vamos a añadir, la instanciamos y añadimos los valores a cada una de sus columnas
            ContentValues fila = new ContentValues();
            fila.put("identificador", identificadorValor);
            fila.put("nombre", nombreValor);
            fila.put("residencia", residenciaValor);
            fila.put("edad", edadValor);
            fila.put("curso", cursoValor);
            //Insertar fila en una tabla
            long res = BaseDeDatos.insert("alumnos", null, fila);

            //Cerramos la base de datos para no dejarla abierta, realizando su commit correspondiente
            BaseDeDatos.close();

            //Indicar al usuario si se ha añadido el alumno
            if(res == -1)
                Toast.makeText(this, "ERROR: Ya existe un alumno con ese identificador", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(this, "Alumno agregado!", Toast.LENGTH_SHORT).show();
                //Limpiar los campos de texto
                etIdentificadorm.setText("");
                etNombrem.setText("");
                etResidenciam.setText("");
                etEdadm.setText("");
                etCurso.setText("");
            }
        }
    }

    //----------------------------------------------------------------------------------------------

    public void buscarClick(View view) {
        //Crear la base de datos con los datos correspondientes
        AdminSQLiteOpenHelper instituto = new AdminSQLiteOpenHelper(this, "instituto", null, 1);
        //Instancia referenciando base de datos ABIERTA par escritura
        SQLiteDatabase BaseDeDatos = instituto.getReadableDatabase();
        //Almacer al codigo por el que vamos a buscar
        String identificador = etIdentificadorm.getText().toString();

        //Comprobar que el campo no esta vacio
        if(identificador.isEmpty()==false){
            String sentencia = "select nombre, residencia, edad, curso from alumnos where identificador = "+identificador+";";
            //Creamos un cursor que es una referencia al conjunto de filas que devuelve un seclect;
            Cursor fila = BaseDeDatos.rawQuery(sentencia, null); //El null hace referencia a las columnas del cursor con las que queremos trabajar, en este caso todas

            //Mover el cursor al principio de los datos devueltos por la consulta, devuelve si se puede mover correctamente o no (boolean),
            // es decir hay datos en esa posicion
            if(fila.moveToFirst()){
                //Obtener los datos del resultado del select (cursor)
                etNombrem.setText(fila.getString(0));
                etResidenciam.setText(fila.getString(1));
                etEdadm.setText(fila.getString(2));
                etCurso.setText(fila.getString(3));
            }else
                Toast.makeText(this, "Alumno no encontrado", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(this, "Debe introducir un identificador de alumno", Toast.LENGTH_SHORT).show();

        //Cerramos la base de datos para no dejarla abierta
        BaseDeDatos.close();
    }

    //----------------------------------------------------------------------------------------------

    public void modificarClick(View view){
        //Crear la base de datos con los datos correspondientes
        AdminSQLiteOpenHelper instituto = new AdminSQLiteOpenHelper(this, "instituto",null, 1);
        //Instancia referenciando base de datos ABIERTA par escritura
        SQLiteDatabase BaseDeDatos = instituto.getWritableDatabase();

        //Almacenamos los datos introducidos por pantalla
        String identificadorValor = etIdentificadorm.getText().toString();
        String nombreValor = etNombrem.getText().toString();
        String residenciaValor = etResidenciam.getText().toString();
        String edadValor = etEdadm.getText().toString();
        String cursoValor = etCurso.getText().toString();

        //Comprobamos que los campos de texto no se encuentren vacios

        if (identificadorValor.isEmpty()||nombreValor.isEmpty()||residenciaValor.isEmpty()||edadValor.isEmpty()||cursoValor.isEmpty()) {
            //Cerramos la base de datos para no dejarla abierta
            BaseDeDatos.close();
            Toast.makeText(this, "Debe completar todos los datos del almuno", Toast.LENGTH_SHORT).show();
        }else{
            //Si no estan vacios
            //Creamos la fila que vamos a modifica, la instanciamos y añadimos los valores a cada una de sus columnas
            ContentValues fila = new ContentValues();
            fila.put("identificador", identificadorValor);
            fila.put("nombre", nombreValor);
            fila.put("residencia", residenciaValor);
            fila.put("edad", edadValor);
            fila.put("curso", cursoValor);

            //modificar un registro de la tabla (descripcion y/o precio) donde el codigo sea igual al codigo
            //estableciendo por contenido el contentValues que hemos creado con la informacion.
            // Y devuelve el numero de tuplas modificadas
            int numModificadas = BaseDeDatos.update("alumnos", fila,"identificador="+identificadorValor ,null);

            //Comprobar si se ha modificado
            if(numModificadas==0) {
                Toast.makeText(this, "No se ha modificado el alumno", Toast.LENGTH_SHORT).show();
            }else if(numModificadas == 1) {
                Toast.makeText(this, "Alumno modificado!", Toast.LENGTH_SHORT).show();

                //Limpiar los campos de texto
                etIdentificadorm.setText("");
                etNombrem.setText("");
                etResidenciam.setText("");
                etEdadm.setText("");
                etCurso.setText("");
            }

            //Cerramos la base de datos para no dejarla abierta, realizando su commit correspondiente
            BaseDeDatos.close();
        }
    }

    //----------------------------------------------------------------------------------------------

    public void eliminarClick(View view) {
        //Crear la base de datos con los datos correspondientes
        AdminSQLiteOpenHelper instituto = new AdminSQLiteOpenHelper(this, "instituto", null, 1);
        //Instancia referenciando base de datos ABIERTA par escritura
        SQLiteDatabase BaseDeDatos = instituto.getWritableDatabase();

        //Almacenamos los datos introducidos por pantalla
        String identificadorValor = etIdentificadorm.getText().toString();

        //Comprobamos que los campos de texto no se encuentren vacios

        if (identificadorValor.isEmpty()) {
            //Cerramos la base de datos para no dejarla abierta
            BaseDeDatos.close();
            Toast.makeText(this, "Debe introducir el identificador del alumno a borrar", Toast.LENGTH_SHORT).show();
        } else {
            //modificar un registro de la tabla (descripcion y/o precio) donde el codigo sea igual al codigo introducido en el campo de texto
            // Y devuelve el numero de tuplas eliminadas
            int numEliminadas = BaseDeDatos.delete("alumnos", "identificador=" + identificadorValor, null);

            //Comprobar si se ha eliminado
            if (numEliminadas == 0) {
                Toast.makeText(this, "No se ha podido borrar ningun alumno con dicho identificador", Toast.LENGTH_SHORT).show();
            } else if (numEliminadas == 1) {
                Toast.makeText(this, "Alumno eliminado!", Toast.LENGTH_SHORT).show();

                //Limpiar los campos de texto
                etIdentificadorm.setText("");
                etNombrem.setText("");
                etResidenciam.setText("");
                etEdadm.setText("");
                etCurso.setText("");
            }

            //Cerramos la base de datos para no dejarla abierta, realizando su commit correspondiente
            BaseDeDatos.close();
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA PASAR A LA SIGUIENTE ACTIVITY DONDE MOSTRAR EL CONTENIDO DE LA TABLA ALUMNOS
     * @param view
     */
    public void listarClick(View view){
        Intent i = new Intent(this, SegundaActivity.class);

        //ArrayList donde almacenaremos el contenido
        ArrayList<String> contenido = new ArrayList<String>();

        //Crear la base de datos con los datos correspondientes
        AdminSQLiteOpenHelper instituto = new AdminSQLiteOpenHelper(this, "instituto", null, 1);
        //Instancia referenciando base de datos ABIERTA par escritura
        SQLiteDatabase BaseDeDatos = instituto.getReadableDatabase();

        String sentencia = "select * from alumnos";
        //Creamos un cursor que es una referencia al conjunto de filas que devuelve un seclect;
        Cursor resultado = BaseDeDatos.rawQuery(sentencia, null); //El null hace referencia a las columnas del cursor con las que queremos trabajar, en este caso todas

        //Mover el cursor al principio de los datos devueltos por la consulta, devuelve si se puede mover correctamente o no (boolean),
        // es decir hay datos en esa posicion
        if(resultado.moveToFirst()) {
            //Seguieremos recorriendo el cursor mientras no nos encontremos en la posicion de despues del ultimo elemento del cursor
            while (!resultado.isAfterLast()){
                //Obtener los datos del resultado del select (cursor)
                String id = resultado.getString(0);
                String nom = resultado.getString(1);
                String res = resultado.getString(2);
                String ed = resultado.getString(3);
                String cur = resultado.getString(4);

                //Añadir el dato leido al listado
                contenido.add(id+" - "+nom+ " | Residencia: "+res+" | Edad: "+ed+" años | Curso: "+cur);

                //Avanzamos el cursor al siguiente elemento
                resultado.moveToNext();
            }
        }else
            contenido.add("No se han encontrado datos");

        //Cerramos la base de datos para no dejarla abierta
        BaseDeDatos.close();

        //Pasar a la siguiente activity el contenido a mostrar
        i.putExtra("contenido", contenido);
        //Abrir la siguiente activity
        startActivity(i);
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA BUSCAR TODOS LOS DATOS EN FUNCION DE UN CURSO
     * @param view
     */
    public void buscarCursoClick(View view){
        Intent i = new Intent(this, SegundaActivity.class);
        //Obtener el contenido del editText del curso
        String cursoValor = etCurso.getText().toString();

        //Comprobar que se ha introducido informacion el cuadro de texto de curso
        if(cursoValor.isEmpty())
            Toast.makeText(this, "Debe indicar un curso por el que buscar", Toast.LENGTH_SHORT).show();
        else{
            //ArrayList donde almacenaremos el contenido
            ArrayList<String> contenido = new ArrayList<String>();

            //Crear la base de datos con los datos correspondientes
            AdminSQLiteOpenHelper instituto = new AdminSQLiteOpenHelper(this, "instituto", null, 1);
            //Instancia referenciando base de datos ABIERTA par escritura
            SQLiteDatabase BaseDeDatos = instituto.getReadableDatabase();

            String sentencia = "select * from alumnos where curso = '"+cursoValor+"';";
            //Creamos un cursor que es una referencia al conjunto de filas que devuelve un seclect;
            Cursor resultado = BaseDeDatos.rawQuery(sentencia, null); //El null hace referencia a las columnas del cursor con las que queremos trabajar, en este caso todas

            //Mover el cursor al principio de los datos devueltos por la consulta, devuelve si se puede mover correctamente o no (boolean),
            // es decir hay datos en esa posicion
            if(resultado.moveToFirst()) {
                //Seguieremos recorriendo el cursor mientras no nos encontremos en la posicion de despues del ultimo elemento del cursor
                while (!resultado.isAfterLast()){
                    //Obtener los datos del resultado del select (cursor)
                    String id = resultado.getString(0);
                    String nom = resultado.getString(1);
                    String res = resultado.getString(2);
                    String ed = resultado.getString(3);
                    String cur = resultado.getString(4);

                    //Añadir el dato leido al listado
                    contenido.add(id+" - "+nom+ " | Residencia: "+res+" | Edad: "+ed+" años | Curso: "+cur);

                    //Avanzamos el cursor al siguiente elemento
                    resultado.moveToNext();
                }

                //Mandar con el Intent el contenido a mostrar
                i.putExtra("contenido", contenido);

                //Cerramos la base de datos para no dejarla abierta
                BaseDeDatos.close();

                //Limpiar los campos de texto
                etIdentificadorm.setText("");
                etNombrem.setText("");
                etResidenciam.setText("");
                etEdadm.setText("");
                etCurso.setText("");

                //Pasar al siguiente activity
                startActivity(i);
            }else{
                //Cerramos la base de datos para no dejarla abierta
                BaseDeDatos.close();
                Toast.makeText(this, "No se han encontrado alumnos en ese curso", Toast.LENGTH_SHORT).show();
            }
        }
    }

}