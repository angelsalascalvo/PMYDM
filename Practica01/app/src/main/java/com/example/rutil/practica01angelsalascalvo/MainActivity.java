package com.example.rutil.practica01angelsalascalvo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText etUsu, etContr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUsu = (EditText) findViewById(R.id.etUsuario);
        etContr = (EditText) findViewById(R.id.etContrasena);
    }

    public void acceder(View view){
        String usuarioLeido = etUsu.getText().toString();
        String contrasenaLeido = etContr.getText().toString();

        if(usuarioLeido.equalsIgnoreCase("Maria") && contrasenaLeido.equals("maria2018")){
            //Si es correcto pasamos al siguiente Activity
            Intent siguiente = new Intent(this, Activity2.class);
            startActivity(siguiente);
        }else{
            //Si los datos son erroneos lo indicamos en pantalla
            Toast.makeText(this,"LOGIN INCORRECTO", Toast.LENGTH_SHORT).show();
        }
    }
}
