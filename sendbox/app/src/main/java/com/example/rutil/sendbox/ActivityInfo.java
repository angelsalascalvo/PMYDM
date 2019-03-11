package com.example.rutil.sendbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ActivityInfo extends AppCompatActivity {
    private WebView wvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        //Referencias
        wvInfo = (WebView) findViewById(R.id.wvInfo);

        //Establecer url al WebView
        wvInfo.setWebViewClient(new WebViewClient()); //Activar el navegador WebView
        wvInfo.loadUrl("https://github.com/angelsalascalvo");
    }
}
