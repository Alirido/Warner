package com.daraa.androidwarner;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends AppCompatActivity {

    static boolean status;
    static TextView mSpeed;

    Location mLastLocation;
    LocationManager mLocationManager;

    static long startTime, endTime;
    static ProgressDialog mProgressDialog;
    static int p=0;

    LocationService mService;

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.LocalBinder binder  = (LocationService.LocalBinder) service;
            mService = binder.getService();
            status = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            status = false;
        }
    };

    @Override
    protected void onDestroy() {
        if(status){
        unbindService();
        }
        super.onDestroy();
    }

    public void unbindService() {
        if(!status){
            Intent i = new Intent(getApplicationContext(),LocationService.class);
            unbindService(sc);
            status = false;
        }
    }

    @Override
    public void onBackPressed() {
        if(!status){
            super.onBackPressed();
        }
        else{
            moveTaskToBack(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case 1000:
            {
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this,"GRANTED", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"DENIED", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);



        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            }, 1000);
        }

        mSpeed = (TextView)findViewById(R.id.display_speed);

        checkGPS();
        mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return;
        if(!status)
            bindService();

    }

    private void bindService() {
        if(status)
            return;
        Intent i = new Intent(getApplicationContext(),LocationService.class);
        bindService(i,sc,BIND_AUTO_CREATE);
        status = true;
        startTime = System.currentTimeMillis();
    }

    private void checkGPS(){
        mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        if(!mLocationManager.isProviderEnabled(mLocationManager.GPS_PROVIDER))
            showGPSDisabledAlert();
    }

    private void showGPSDisabledAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Enable GPS to use application")
                .setCancelable(false)
                .setPositiveButton("EnableGPS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i ) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);

                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
