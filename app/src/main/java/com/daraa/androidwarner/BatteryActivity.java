package com.daraa.androidwarner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BatteryActivity extends AppCompatActivity {
    private Context mContext;

    private TextView mTextViewInfo;
    private TextView mTextViewPercentage;
    private ProgressBar mProgressBar;
    private int mProgressStatus = 0;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);

            mTextViewInfo.setText("Battery Scale : " + scale);

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
            mTextViewInfo.setText(mTextViewInfo.getText() + "\nBattery Level : " + level);
            float percentage = level/ (float) scale;
            mProgressStatus = (int)((percentage)*100);
            mTextViewPercentage.setText("" + mProgressStatus + "%");
            mTextViewInfo.setText(mTextViewInfo.getText() + "\nPercentage : "+ mProgressStatus + "%");
            mProgressBar.setProgress(mProgressStatus);

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
            if(isCharging){
                mTextViewInfo.setText(mTextViewInfo.getText() + "\nStatus : Charging");
            }
            else{
                mTextViewInfo.setText(mTextViewInfo.getText() + "\nStatus : Disharging");
            }
            // Temperature
            float temperature = (float)intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)/10;
            mTextViewInfo.setText(mTextViewInfo.getText() + "\nTemperature : "+temperature + " °C");


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        mContext = getApplicationContext();
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = mContext.registerReceiver(mBroadcastReceiver,iFilter);
        mTextViewInfo = (TextView) findViewById(R.id.tv_info);
        mTextViewPercentage = (TextView) findViewById(R.id.tv_percentage);
        mProgressBar = (ProgressBar) findViewById(R.id.pb);




    }
}
