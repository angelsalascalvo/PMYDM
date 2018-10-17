package com.example.angel.p06pasoinfdosactivitysangelsalas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SegundoActivity extends AppCompatActivity {
    private TextView tvResul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segundo);
        //Referencias a los elementos
        tvResul = (TextView) findViewById(R.id.tvResultado);

        //Obtener el dato que es enviado desde otra activity a traves del objeto de tipo Intent inicando el nombre que referencia al dato
        // (Intent es un elemento intermedio compartido por las activitys)
        String datoRecibido = getIntent().getStringExtra("nombre");

        //Establecer el dato recibido en el TextView
        tvResul.setText("Hola "+datoRecibido+"!");
    }

    /**
     * Metodo del boton "Volver"
     * @param view
     */
    public void volverClick(View view){
        Intent anterior = new Intent(this, MainActivity.class);
        startActivity(anterior);
    }
}
