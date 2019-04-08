package com.daraa.androidwarner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Button battery_button = view.findViewById(R.id.battery_saver);
        battery_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BatteryActivity.class);
                startActivity(intent);
            }
        });

        Button alarm_button = view.findViewById(R.id.alarm);
        alarm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AlarmActivity.class);
                startActivity(intent);
            }
        });

        Button speed_button = view.findViewById(R.id.safety_speed);
        speed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocationActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
