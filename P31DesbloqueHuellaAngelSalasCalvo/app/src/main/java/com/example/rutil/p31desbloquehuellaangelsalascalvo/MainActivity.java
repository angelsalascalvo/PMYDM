package com.example.rutil.p31desbloquehuellaangelsalascalvo;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_NAME = "yourKey";
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private TextView tvMensajem;
    private FingerprintManager.CryptoObject cryptoObjeto;
    private FingerprintManager huellaManager;
    private KeyguardManager claveManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Referencias
        tvMensajem = (TextView) findViewById(R.id.tvMensaje);

        //se comprueba que la version es superior a marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //se crean la clave y la huella
            claveManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            huellaManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

            //Se comprueba que el dispositivo tiene sensor de huella
            if (!huellaManager.isHardwareDetected()) {
                tvMensajem.setText("Tu dispositivo no tiene lector de huella");
            }

            //se comprueban los permisos para usar el sensor
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                tvMensajem.setText("Tienes que conceder los permisos de acceso al lector de huella");
            }

            //se comprueba que se tiene una huella registrada
            if (!huellaManager.hasEnrolledFingerprints()) {
                tvMensajem.setText("No tienes ninguna huella registrada en configuracion");
            }

            if (!claveManager.isKeyguardSecure()) {
                tvMensajem.setText("Tienes que activar el bloqueo de la pantalla");
            } else {
                // si pasa todas las condiciones, genera la llave.
                generateKey();
                if (initCipher()) {
                    //si cipher se inicia correctamente, crea un cryptoObjeto//
                    cryptoObjeto = new FingerprintManager.CryptoObject(cipher);
                    //se crea un objeto de la clase fingerprinthandler y comienza laautenticacion
                    FingerprintHandler huella = new FingerprintHandler(this);
                    huella.autenticar(huellaManager, cryptoObjeto);
                }
            }
        }
    }

    //Acceder al android keystore y generar una clave de encriptado
    private void generateKey() {
        try {
            //Se obtiene la keystore usando el standar(“AndroidKeystore”)
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyGenerator =

            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            //se carga vacia
            keyStore.load(null);

            //Se inicia el keystore. Los parametros indican el proposito del keystore, en nuestro caso
            //de encriptacion y desencriptacion
            //EROOR PORQUE NECESITA LA API 23 MINIMO
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    //aqui se configura que nos pida la autenticacion cada vez
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            //Se genera
            keyGenerator.generateKey();
        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {

            exc.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------

    //Con este metodo iniciamos cipher
    public boolean initCipher() {
        try {
            //se crea un objeto cipher, las propiedades que recibe son genericas para el uso del escaner de huella
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);

        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Fallo con cipher", e);
        }
        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //devuelve verdadero si se ha iniciado correctamente
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            //Devuelve falso si ha fallado
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
}