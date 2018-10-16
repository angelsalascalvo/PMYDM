package com.example.rutil.practica01angelsalascalvo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Activity2 extends AppCompatActivity {
    private String [] listDias = {"Lunes","Martes","Miercoles","Jueves","Viernes","Sabado","Domingo"};
    private ListView lvDiasSem;
    private RadioButton rbEn, rbFe, rbMa, rbAb;
    private TextView tvPrinc;
    private String dia="Lunes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        lvDiasSem = (ListView) findViewById(R.id.lvDiasSemana);
        tvPrinc = (TextView) findViewById(R.id.tvPrincipal);
        //Referenciar radio buton;
        rbEn = (RadioButton) findViewById(R.id.rbEnero);
        rbFe = (RadioButton) findViewById(R.id.rbFebrero);
        rbMa = (RadioButton) findViewById(R.id.rbMarzo);
        rbAb = (RadioButton) findViewById(R.id.rbAbril);

        //Asignar al listView los elementos y aplicar el XML con el estilo y formato
        ArrayAdapter<String> adapter = new ArrayAdapter<String >(this,R.layout.elementos_listview, listDias);
        lvDiasSem.setAdapter(adapter);

        lvDiasSem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dia = listDias[position];
                establecerDia(null);
            }
        });
    }

    public void volver(View view){
        //Metodo que nos permite volver a la pantalla de login
        Intent principal = new Intent(this, MainActivity.class);
        startActivity(principal);
    }

    public void establecerDia(View view){
        String mes="Enero";
        if(rbEn.isChecked()==true)
            mes="Enero";
        if(rbFe.isChecked()==true)
            mes="Febrero";
        if(rbMa.isChecked()==true)
            mes="Marzo";
        if(rbAb.isChecked()==true)
            mes="Abril";

        tvPrinc.setText("Hoy es "+ dia+" de "+mes);
    }
}
