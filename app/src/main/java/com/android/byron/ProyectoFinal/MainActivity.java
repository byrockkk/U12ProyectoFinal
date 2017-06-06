package com.android.byron.ProyectoFinal;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    //Declaramos el anuncio
    private AdView adView;

    private CallbackManager cM;
    private LoginButton lB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Enlazamos con la parte grafica
        adView = (AdView) findViewById(R.id.ad_view);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        //cargamos el anuncio
        adView.loadAd(adRequest);

        //Comienza la declaracion del boton de facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        cM = CallbackManager.Factory.create();

        getFbKeyHash("k649nKZstRhFlUhNrqkie5Ciadw=");
        lB = (LoginButton) findViewById(R.id.login_facebook);
        lB.registerCallback(cM, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, "Inicio de sesion Exitoso", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Inicio de sesion Cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, "Inicio de sesion NO Exitoso", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getFbKeyHash(String packageName) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("keyHash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.println("keyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }catch (PackageManager.NameNotFoundException e){

        }catch (NoSuchAlgorithmException e){

        }
    }

    protected void onActivityResult (int reqCode, int resCode, Intent i){
        cM.onActivityResult(reqCode, resCode, i);
    }

    //Creamos metodos para los diferentes estados del AdView
    @Override
    protected void onPause() {
        if(adView != null){
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(adView != null){
            adView.resume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }

}
