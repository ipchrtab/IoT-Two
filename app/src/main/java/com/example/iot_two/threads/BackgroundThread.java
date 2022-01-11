package com.example.iot_two.threads;

import android.content.Context;
import android.os.BatteryManager;
import android.util.Log;
import com.example.iot_two.memory.MemoryStorage;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.nio.charset.StandardCharsets;

public class BackgroundThread extends Thread implements IMqttActionListener {
    private final MemoryStorage memory = MemoryStorage.getInstance();
    private final String clientId = MqttClient.generateClientId();
    private MqttAndroidClient client;
    private final Context context;
    private boolean running = true;

    public BackgroundThread(Context context) {
        this.context = context;
    }

    public void startThread() {
        try {
            String serverURI = memory.getServerURI();
            client = new MqttAndroidClient(context, serverURI, clientId);
            IMqttToken token = client.connect();
            token.setActionCallback(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void stopThread() {
        Log.i("BackgroundThread", "stopping");
        running = false;
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        super.start();
    }

    @Override
    public void run() {
        try {
            while (running) {
                BatteryManager bm = (BatteryManager) context.getSystemService(context.BATTERY_SERVICE);
                String percentage = String.valueOf(bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY));

                String topic = "IOT2";
                String payload = memory.getGeolocation() + "," + percentage + "," + memory.getSmokeSensorValue() + "," + memory.getGasSensorValue() + "," + memory.getTempSensorValue() + "," + memory.getUvSensorValue();

                Log.i("***BackgroundThread***", "Sending sample to: " + memory.getServerURI() + ": " + payload);
                byte[] encodedPayload;
                try {
                    encodedPayload = payload.getBytes(StandardCharsets.UTF_8);
                    MqttMessage message = new MqttMessage(encodedPayload);
                    message.setRetained(true);
                    client.publish(topic, message);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                Thread.sleep(1000);
            }

            client.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.i("BackgroundThread", "exited");
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        exception.printStackTrace();
        Log.i("Connection failed: ", exception.getMessage());
    }

}