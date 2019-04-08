package com.daraa.androidwarner;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

import javax.xml.parsers.FactoryConfigurationError;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final static long INTERVAL = 500;
    private final static long FASTEST_INTERVAL = 250;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation, lStart, lEnd;

    static double distance = 0;

    private final IBinder mBinder = new LocalBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        return mBinder;
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try{
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

        }catch (SecurityException e){

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LocationActivity.mProgressDialog.dismiss();
        mCurrentLocation=location;
        if(lStart==null){
            lStart = lEnd = mCurrentLocation;
        }
        else{
            lEnd = mCurrentLocation;
        }

        updateUI();
    }

    private void updateUI() {
        if(LocationActivity.p == 0){
            distance = distance+(lStart.distanceTo(lEnd)/1000.00);
            LocationActivity.endTime = System.currentTimeMillis();
            long diff = LocationActivity.endTime - LocationActivity.startTime;
            diff = TimeUnit.MILLISECONDS.toHours(diff);

            double speed = distance/diff;

            LocationActivity.mSpeed.setText(speed+" Km/h");

            lStart=lEnd;


        }
    }

    @Override
    public boolean onUnbind(Intent intent){
        atopLocationUpdates();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
        lStart = lEnd = null;
        distance = 0;
        return super.onUnbind(intent);
    }

    private void atopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
        distance = 0;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class LocalBinder extends Binder {
        public LocationService getService(){
            return LocationService.this;
        }
    }
}
