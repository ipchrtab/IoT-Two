package com.example.iot_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.iot_two.memory.MemoryStorage;
import com.google.android.material.textfield.TextInputLayout;


public class SettingsActivity extends AppCompatActivity {

    private TextInputLayout textInputIp;
    private TextInputLayout textInputPort;
    private final MemoryStorage memory = MemoryStorage.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activity);

        getSupportActionBar().setTitle("Server IP Address and Port");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textInputIp = findViewById(R.id.ip_input);
        textInputPort = findViewById(R.id.port_input);
    }

    //ip validation (further validation can be done)
    private boolean validateIp() {
        String ipInput = textInputIp.getEditText().getText().toString().trim();
        if (ipInput.isEmpty()) {
            textInputIp.setError("Field can't be empty");
            return false;
        } else {
            textInputIp.setError(null);
            return true;
        }
    }

    //port validation (further validation can be done)
    private boolean validatePort() {
        String portInput = textInputPort.getEditText().getText().toString().trim();
        if (portInput.isEmpty()) {
            textInputPort.setError("Field can't be empty");
            return false;
        } else {
            textInputPort.setError(null);
            return true;
        }
    }

    public void confirmInput(View v) {
        if (!validateIp() | !validatePort()) {
            return;
        }

        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();

        memory.setIp(textInputIp.getEditText().getText().toString());
        memory.setPort(Integer.parseInt(textInputPort.getEditText().getText().toString()));

        finish();
    }
}