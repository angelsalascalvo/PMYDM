package com.example.rutil.p20camarafotosangelsalas;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ImageView ivVisorm;
    private String mCurrentPhotoPath;
    private static final int RESPUESTA_HACER_FOTO=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Referenciar elementos
        ivVisorm = (ImageView) findViewById(R.id.ivVisor);

        //Comprobar que los permisos estan concedidos
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
           && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
           //Si no estan proporcionados, los volvemos a solicitar
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                               Manifest.permission.CAMERA}, 1000);
        }
    }

    /**
     * METODO QUE CREA UN FICHERO DE IMAGEN CON UN NOMBRE UNICO PARA CADA FOTOGRAFIA
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "fotoP20_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * METODO QUE SE EJECUTARÁ AL HACER CLIC SOBRE EL BOTON PARA HACE LA FOTO
     * @param view
     */
    public void hacerFotoClick(View view){
        //Se cierra de forma momentanea el actual activity para mostrar la aplicación de camara
        Intent camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //Activity de la camara

        //Comprobar si la camara ha devuelto un objeto (se ha realizado la foto)
        // o si devuelve null (no se ha realizado la foto)
        if(camara.resolveActivity(getPackageManager())!=null){
            File ficheroFoto=null;

            try {
                //Recuperar el nombre y la ruta del fichero (es decir el propio fichero)
                ficheroFoto = createImageFile();
            } catch (IOException e) {
                Toast.makeText(this, "No se pudo crear el fichero", Toast.LENGTH_SHORT).show();
            }

            //Si se ha creado el fichero, podemos realizar las acciones correspondientes
            if (ficheroFoto!=null){
                //Crear uri con la propia foto en cuestion
                Uri fotoUri = FileProvider.getUriForFile(this,"com.example.andoroid.fileprovider", ficheroFoto);
                //Enviar la ruta URI junto con el intent
                camara.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                //Llamada al intent camara pasandole la ruta donde debe almacenar el fichero
                startActivityForResult(camara, RESPUESTA_HACER_FOTO);
            }

        }
    }

    /**
     * METODO PARA RECUPERAR IMAGEN Y ESTABLECERLA EN EL IMAGE VIEW
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESPUESTA_HACER_FOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivVisorm.setImageBitmap(imageBitmap);
        }
    }
}
