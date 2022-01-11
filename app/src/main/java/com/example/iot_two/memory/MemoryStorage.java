package com.example.iot_two.memory;

public class MemoryStorage {
    private String ip = "192.168.2.51";
    private int port = 1883;
    private String geolocation = "uninitialized";
    private String smokeSensorValue = "unknown";
    private String gasSensorValue = "unknown";
    private String tempSensorValue = "unknown";
    private String uvSensorValue = "unknown";
    private static MemoryStorage instance = null;

    public static MemoryStorage getInstance() {
        if (instance == null) {
            instance = new MemoryStorage();
        }
        return instance;
    }

    public synchronized String getIp() {
        return ip;
    }

    public synchronized void setIp(String ip) {
        this.ip = ip;
    }

    public synchronized int getPort() {
        return port;
    }

    public synchronized void setPort(int port) {
        this.port = port;
    }

    public synchronized String getServerURI() {
        return "tcp://" + ip + ":" + port;
    }

    public synchronized String getGeolocation() {
        return geolocation;
    }

    public synchronized void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    public synchronized String getSmokeSensorValue() {
        return smokeSensorValue;
    }

    public synchronized void setSmokeSensorValue(String smokeSensorValue) {
        this.smokeSensorValue = smokeSensorValue;
    }

    public synchronized String getGasSensorValue() {
        return gasSensorValue;
    }

    public synchronized void setGasSensorValue(String gasSensorValue) {
        this.gasSensorValue = gasSensorValue;
    }

    public synchronized String getTempSensorValue() {
        return tempSensorValue;
    }

    public synchronized void setTempSensorValue(String tempSensorValue) {
        this.tempSensorValue = tempSensorValue;
    }

    public synchronized String getUvSensorValue() {
        return uvSensorValue;
    }

    public synchronized void setUvSensorValue(String uvSensorValue) {
        this.uvSensorValue = uvSensorValue;
    }
}