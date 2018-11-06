package com.example.angel.p11sqliteangelsalascalvo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

/**
 * @author Ángel Salas Calvo
 * CLASE ENCARGADA DE LA ADMINISTRACION DE SQLite
 */
public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    //IMPORTANTE!!! ELIMINAR ANOTACIONES "@"
    //@androidx.annotation.Nullable @Nullable
    //@androidx.annotation.Nullable @Nullable
    //@androidx.annotation.Nullable @Nullable
    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //Metodos abstractos que deben ser implementados

    //Indicaciones de que se tiene que realizar al crear una base de datos a traves de esta clase
    @Override
    public void onCreate(SQLiteDatabase baseDatos) {
        //TIPOS DE DATOS SQLite
        // int -> numeros
        // text -> texto
        // real -> numero con decimal

        //Crear una tabla en la base de datos con sentencia sql
        baseDatos.execSQL("create table alumnos (" +
                "identificador int primary key," +
                "nombre text, " +
                "residencia text,"+
                "edad real,"+
                "curso text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase baseDatos, int oldVersion, int newVersion) {

    }
}
