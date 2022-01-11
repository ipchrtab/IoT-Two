package com.example.iot_two;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iot_two.memory.MemoryStorage;

public class ManualGps extends AppCompatActivity {
    private final MemoryStorage memory = MemoryStorage.getInstance();

    private void saveChanges(String location) {

        memory.setGeolocation(location);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_gps);
    }

    public void confirmManualGps1 (View v){
        String location = "37.96809452684323,23.76630586399502";
        saveChanges(location);
    }

    public void confirmManualGps2 (View v){
        String location = "37.96799937191987,23.766603589104385";
        saveChanges(location);
    }

    public void confirmManualGps3 (View v){
        String location = "37.967779456380754,23.767174897611685";
        saveChanges(location);
    }

    public void confirmManualGps4 (View v){
        String location = "37.96790421900921,23.76626294807113";
        saveChanges(location);
    }
}