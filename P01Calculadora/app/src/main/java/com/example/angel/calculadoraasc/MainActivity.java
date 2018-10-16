package com.example.angel.calculadoraasc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //Declarar variables para referenciar elementos del mainActivity
    private EditText etOp1, etOp2;
    private TextView tvResul, tvTit;
    private RadioButton rbEspa, rbAle, rbKur, rbJap;
    private Button bSum, bRes;
    private Spinner spOpci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Buscamos los elementos y mediante la clase R que sirve como inferfaz entre la parte visual y el código los referenciaremos.
        //Capturar objetos de la activity
        etOp1= (EditText) findViewById(R.id.etOperando1);
        etOp2= (EditText) findViewById(R.id.etOperando2);
        tvResul= (TextView) findViewById(R.id.tvResultado);
        tvTit= (TextView) findViewById(R.id.tvTitulo);
        bSum= (Button) findViewById(R.id.bSumar);
        bRes= (Button) findViewById(R.id.bRestar);

        /*
                IDIOMAS
         */
        rbEspa= (RadioButton) findViewById(R.id.rbEspanol);
        rbAle= (RadioButton) findViewById(R.id.rbAleman);
        rbKur= (RadioButton) findViewById(R.id.rbKurdo);
        rbJap= (RadioButton) findViewById(R.id.rbJapones);

        /*
                SPINNER
         */
        //Buscar el objeto spinner para referenciarlo
        spOpci = (Spinner) findViewById(R.id.spOpciones);
        //Crear opciones para el spinner
        String opciones[]={"sumar", "restar", "multiplicar", "dividir"};
        //Indicamos al spiner que coja de este array las opciones
        ArrayAdapter <String> adapter = new ArrayAdapter(this, R.layout.spinner_opciones, opciones); //Creamos un tipo de dato Array Adapter con el layout que hemos creado personalizando el estilo
        spOpci.setAdapter(adapter); //Asignamos este objeto al elemento

    }

    //----------------------------------------------------------------------------------------------

    /**
     * Metodo para sumar los valores de los operandos.
     * @param vista
     */
    public void sumar(View vista){
        int num1, num2, suma;
        String cadNum1, cadNum2, cadResul;

        //Obtenemos el valor texto que se ha introducido en los EditText
        cadNum1 = etOp1.getText().toString();
        cadNum2 = etOp2.getText().toString();

        //Comprobamos que los editText de los operandos tengan valores introducidos, en caso contrario no calculamos nada
        if(cadNum1.length()!=0 && cadNum2.length()!=0) {

            //Convertimos el valor a un numero
            num1 = Integer.parseInt(cadNum1);
            num2 = Integer.parseInt(cadNum2);

            //Realizar la operacion
            suma = num1 + num2;

            //Convertir resultado a string y asignarla al resultado
            cadResul = String.valueOf(suma);
            tvResul.setText(cadResul);
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Metodo para restar los valores de los operandos.
     * @param vista
     */
    public void restar(View vista){ //??
        int num1, num2, suma;
        String cadNum1, cadNum2, cadResul;

        //Obtenemos el valor texto que se ha introducido en los EditText
        cadNum1 = etOp1.getText().toString();
        cadNum2 = etOp2.getText().toString();

        //Comprobamos que los editText de los operandos tengan valores introducidos, en caso contrario no calculamos nada
        if(cadNum1.length()!=0 && cadNum2.length()!=0){

            //Convertimos el valor a un numero
            num1 = Integer.parseInt(cadNum1);
            num2 = Integer.parseInt(cadNum2);

            //Realizar la operacion
            suma = num1-num2;

            //Convertir resultado a string y asignarla al resultado
            cadResul = String.valueOf(suma);
            tvResul.setText(cadResul);
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Metodo para restar los valores de los operandos.
     * @param vista
     */
    public void multiplicar(View vista){ //??
        int num1, num2, suma;
        String cadNum1, cadNum2, cadResul;

        //Obtenemos el valor texto que se ha introducido en los EditText
        cadNum1 = etOp1.getText().toString();
        cadNum2 = etOp2.getText().toString();

        //Comprobamos que los editText de los operandos tengan valores introducidos, en caso contrario no calculamos nada
        if(cadNum1.length()!=0 && cadNum2.length()!=0){

            //Convertimos el valor a un numero
            num1 = Integer.parseInt(cadNum1);
            num2 = Integer.parseInt(cadNum2);

            //Realizar la operacion
            suma = num1*num2;

            //Convertir resultado a string y asignarla al resultado
            cadResul = String.valueOf(suma);
            tvResul.setText(cadResul);
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Metodo para restar los valores de los operandos.
     * @param vista
     */
    public void dividir(View vista){ //??
        int num1, num2, suma=0;
        String cadNum1, cadNum2, cadResul;

        //Obtenemos el valor texto que se ha introducido en los EditText
        cadNum1 = etOp1.getText().toString();
        cadNum2 = etOp2.getText().toString();

        //Comprobamos que los editText de los operandos tengan valores introducidos, en caso contrario no calculamos nada
        if(cadNum1.length()!=0 && cadNum2.length()!=0){

            //Convertimos el valor a un numero
            num1 = Integer.parseInt(cadNum1);
            num2 = Integer.parseInt(cadNum2);

            //Realizar la operacion
            try {
                suma = num1 / num2;
            }catch (Exception e){
            }

            //Convertir resultado a string y asignarla al resultado
            cadResul = String.valueOf(suma);
            tvResul.setText(cadResul);
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Metodo traducir el texto en funcion del radio buton seleccionado
     * @param view
     */
    public void traducir(View view){
        if(rbEspa.isChecked()==true){
            tvTit.setText(getResources().getString(R.string.sEtitulo)); //Metodo para recuperar datos del archivo string
            etOp1.setHint(getResources().getString(R.string.sEop1));
            etOp2.setHint(getResources().getString(R.string.sEop2));
            bSum.setTextSize(24);
            bSum.setText(getResources().getString(R.string.sEsumar));
            bRes.setTextSize(24);
            bRes.setText(getResources().getString(R.string.sErestar));
            tvResul.setText(getResources().getString(R.string.sEresul));

        } else if(rbAle.isChecked()==true){
            tvTit.setText(getResources().getString(R.string.sAtitulo)); //Metodo para recuperar datos del archivo string
            etOp1.setHint(getResources().getString(R.string.sAop1));
            etOp2.setHint(getResources().getString(R.string.sAop2));
            bSum.setText(getResources().getString(R.string.sAsumar));
            bSum.setTextSize(16); //Ajustar el tamaño de la fuente
            bRes.setText(getResources().getString(R.string.sArestar));
            bRes.setTextSize(16); //Ajustar el tamaño de la fuente
            tvResul.setText(getResources().getString(R.string.sAresul));

        }else if(rbKur.isChecked()==true){
            tvTit.setText(getResources().getString(R.string.sKtitulo)); //Metodo para recuperar datos del archivo string
            etOp1.setHint(getResources().getString(R.string.sKop1));
            etOp2.setHint(getResources().getString(R.string.sKop2));
            bSum.setTextSize(24);
            bSum.setText(getResources().getString(R.string.sKsumar));
            bRes.setTextSize(24);
            bRes.setText(getResources().getString(R.string.sKrestar));
            tvResul.setText(getResources().getString(R.string.sKresul));

        }else if(rbJap.isChecked()==true){
            tvTit.setText(getResources().getString(R.string.sJtitulo)); //Metodo para recuperar datos del archivo string
            etOp1.setHint(getResources().getString(R.string.sJop1));
            etOp2.setHint(getResources().getString(R.string.sJop2));
            bSum.setTextSize(24);
            bSum.setText(getResources().getString(R.string.sJsumar));
            bRes.setTextSize(24);
            bRes.setText(getResources().getString(R.string.sJrestar));
            tvResul.setText(getResources().getString(R.string.sJresul));
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     *
     * @param view
     */
    public void cogerOpcion(View view){
        String seleccionado = spOpci.getSelectedItem().toString(); //Devuelve el elemento seleccionado en el item
        switch(seleccionado) {
            case "sumar":
                sumar(view);  //Debemos pasarle un objeto de tipo View (el mismo que hemos pasado por parametro a este metodo)
                break;
            case "restar":
                restar(view);
                break;
            case "multiplicar":
                multiplicar(view);
                break;
            case "dividir":
                dividir(view);
                break;
        }
    }
/*
    Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()){
        public void onItemSelected(AdapterView<> arg0, View arg1, int arg2, long arg3){
            //Acciones para cuando se selecciona un item
        }

        public void onNothingSelected(AdapterView<> arg0, View arg1, int arg2, long arg3){
            //Acciones para cuando se deseleccione un item
        }
    });

*/
}
