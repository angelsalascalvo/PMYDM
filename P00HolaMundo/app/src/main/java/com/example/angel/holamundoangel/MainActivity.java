package com.example.angel.holamundoangel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity {
    /*
    *      CICLO DE VIDA DE LA APLICACIÓN
     */

    @Override
    // SE CREA LA APLICACION
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this,"evento OnCreate",LENGTH_SHORT).show(); //Mostramos un texto con el evento en cuestion

        int quimica = 5;
        int fisica = 5;
        int matematicas = 5;
        int media=0;
        media = (quimica+fisica+matematicas)/3;

        if (media>5){
            Toast.makeText(this, "Aprobado", LENGTH_LONG);
        }else if(media<5){
            Toast.makeText(this,"Suspenso", LENGTH_LONG);
        }else{
            Toast.makeText(this,"Cinco por los pelos", LENGTH_LONG);
        }
    }

    @Override
    //LA ACTIVIDAD DE HACE VISIBLE
    protected void onStart(){
        super.onStart(); //LLamamos al metodo padre
        Toast.makeText(this,"evento OnStart",LENGTH_SHORT).show(); //Mostramos un texto con el evento en cuestion

        int quimica = 5;
        int fisica = 5;
        int matematicas = 5;
        int media=0;
        media = (quimica+fisica+matematicas)/3;

        if (media>5){
            Toast.makeText(this, "Aprobado", LENGTH_LONG);
        }else if(media<5){
            Toast.makeText(this,"Suspenso", LENGTH_LONG);
        }else{
            Toast.makeText(this,"Cinco por los pelos", LENGTH_LONG);
        }
    }


    @Override
    //EL USUARIO REGRESA A LA ACTIVIDAD
    // PERO SIN MOSTRARSE, SIN SER VISIBLE
    protected void onRestart(){
        super.onDestroy(); //Llamamos al metodo padre
        Toast.makeText(this, "evento OnRestart", LENGTH_SHORT).show(); //Mostramos un texto con el evento en cuestion
    }

    @Override
    //LA ACTIVIDAD VUELVE A SER VISIBLE
    protected void onResume(){
        super.onResume(); //Llamamos al metodo padre
        Toast.makeText(this,"evento OnResume", LENGTH_SHORT).show(); //Mostramos un texto con el evento en cuestion

        int quimica = 5;
        int fisica = 5;
        int matematicas = 5;
        int media=0;
        media = (quimica+fisica+matematicas)/3;

        if (media>5){
            Toast.makeText(this, "Aprobado", LENGTH_LONG);
        }else if(media<5){
            Toast.makeText(this,"Suspenso", LENGTH_LONG);
        }else{
            Toast.makeText(this,"Cinco por los pelos", LENGTH_LONG);
        }
    }

    @Override
    //LA APLICACION ESTA A PUNTO DE SER DETENIDA
    protected void onPause(){
        super.onPause(); //Llamamos al metodo padre
        Toast.makeText(this,"evento OnPause", LENGTH_SHORT).show(); //Mostramos un texto con el evento en cuestion
    }

    @Override
    //LA ACTIVIDAD NO ES VISIBLE, ESTA DETENIDA
    protected void onStop(){
        super.onStop(); //Llamamos al metodo padre
        Toast.makeText(this,"evento OnStop", LENGTH_SHORT).show(); //Mostramos un texto con el evento en cuestion
    }

    @Override
    //LA ACTIVIDAD ESTA A PUNTO DE SER DESTRUIDA, PERO AUN NO ESTA DESTRUIDA
    protected void onDestroy(){
        super.onDestroy(); //Llamamos al metodo padre
        Toast.makeText(this, "evento OnDestroy", LENGTH_SHORT).show(); //Mostramos un texto con el evento en cuestion
    }


}
