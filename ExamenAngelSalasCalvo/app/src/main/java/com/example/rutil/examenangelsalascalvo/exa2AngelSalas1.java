package com.example.rutil.examenangelsalascalvo;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class exa2AngelSalas1 extends AppCompatActivity {

    private TextView tvUsuariom;
    private ImageButton ibDisco1m, ibDisco2m, ibDisco3m;
    private WebView wvNavegadorm;
    private String usuario;
    private int posicion = 0;
    private MediaPlayer discos[] =new MediaPlayer[3];
    private String url[] = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exa2_angel_salas1);
        //Referencias
        tvUsuariom = (TextView) findViewById(R.id.tvUsuario);
        ibDisco1m = (ImageButton) findViewById(R.id.ibDisco1);
        ibDisco2m = (ImageButton) findViewById(R.id.ibDisco2);
        ibDisco3m = (ImageButton) findViewById(R.id.ibDisco3);
        wvNavegadorm = (WebView) findViewById(R.id.wvNavegador);

        //Obtener el dato del intent
        usuario = getIntent().getStringExtra("usuario");
        tvUsuariom.setText(usuario);

        //Array con los mediaPlayer
        discos[0] = MediaPlayer.create(this, R.raw.canciondisco1);
        discos[1] = MediaPlayer.create(this, R.raw.canciondisco2);
        discos[2] = MediaPlayer.create(this, R.raw.canciondisco3);

        //Array con las direcciones web
        url[0] = "http://www.musicarelajante.es/";
        url[1] = "https://www.freeaudiolibrary.com/es/";
        url[2] = "https://www.freemusicprojects.com/es/";

        //Iniciar navegador
        wvNavegadorm.setWebViewClient(new WebViewClient());
    }

    /**
     * METODO QUE SE EJECUTA CUANDO SE PULSA UN BOTON DEL SCROLLVIEW
     */
    public void discoClick(View view){
        //Si existe una cancion reproduciendose la pausamos y reiniciamos la canción
        if(discos[posicion].isPlaying()){
            discos[posicion].pause();
            discos[posicion].seekTo(0);
        }
        //Establecer la cancion y web en funcion del boton pulsado
        switch (view.getId()) {
            case R.id.ibDisco1:
                posicion=0;
                break;
            case R.id.ibDisco2:
                posicion=1;
                break;
            case R.id.ibDisco3:
                posicion=2;
                break;
        }
        //Reproducir la canción
        discos[posicion].start();
        //Cargar la url en el WebView
        wvNavegadorm.loadUrl(url[posicion]);
    }

    /**
     * METODO PARA VOLVER A LA ACTIVITY ANTERIOR
     */
    public void anteriorClick(View view){
        discos[posicion].pause();
        finish();
    }
}



