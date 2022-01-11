package com.example.iot_two;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.iot_two.interfaces.AutoGps;
import com.example.iot_two.memory.MemoryStorage;
import com.example.iot_two.threads.BackgroundThread;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity
        implements SmokeFragment.SmokeFragmentListener, GasFragment.GasFragmentListener,
        TempFragment.TempFragmentListener, UVFragment.UVFragmentListener, AutoGps {

    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback connectivityCallBack;
    private AlertDialog internetDialog;
    private Toast backToast;
    private long backPressedTime;
    private final MemoryStorage memory = MemoryStorage.getInstance();
    private BackgroundThread backgroundThread;
    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdapter adapter;
    private static final int PERMISSION_LOCATION = 1000;
    boolean manualGPS = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing tabLayout
        tabLayout = findViewById(R.id.tab_layout);
        pager2 = findViewById(R.id.view_pager2);
        tabLayout.addTab(tabLayout.newTab().setText("Smoke"));
        tabLayout.addTab(tabLayout.newTab().setText("Gas"));
        tabLayout.addTab(tabLayout.newTab().setText("Temp"));
        tabLayout.addTab(tabLayout.newTab().setText("UV"));
        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle());
        pager2.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        //internet connectivity check
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest request = new NetworkRequest.Builder().build();
        connectivityCallBack = new ConnectivityManager.NetworkCallback() {

            //action when network is lost
            @Override
            public void onLost(Network network) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Dialog_Alert)
                        .setTitle("No internet")
                        .setMessage("Connection Lost.")

                        //ok button -> close app
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAndRemoveTask();
                            }
                        })
                        //cancel button -> back to app
                        .setNegativeButton("Cancel", null).setCancelable(false);
                internetDialog = builder.show();
            }
            //closing dialog if the network is available before "ok"
            @Override
            public void onAvailable(Network network) {
                if (internetDialog != null) {
                    internetDialog.dismiss();
                }
            }
        };

        //registering the callback
        connectivityManager.registerNetworkCallback(request, connectivityCallBack);

        //MQTT client thread
        backgroundThread = new BackgroundThread(this);
        backgroundThread.startThread();
    }

    //input of smoke slider
    @Override
    public void onInputSensorSmoke(String sensorSmoke) {
        memory.setSmokeSensorValue(sensorSmoke);
    }

    //input of gas slider
    @Override
    public void onInputSensorGas(String sensorGas) {
        memory.setGasSensorValue(sensorGas);
    }

    //input of temp slider
    @Override
    public void onInputSensorTemp(String sensorTemp) {
        memory.setTempSensorValue(sensorTemp);
    }

    //input of uv slider
    @Override
    public void onInputSensorUV(String sensorUV) {
        memory.setUvSensorValue(sensorUV);
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.iot_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_ip_port:
                Intent intent = new Intent(this, SettingsActivity.class);
                this.startActivity(intent);
                break;
            case R.id.sub_item_manual:
                manualGPS = true;
                Intent intentGps = new Intent(this, ManualGps.class);
                this.startActivity(intentGps);
                break;
            case R.id.sub_item_auto:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
                } else {
                    showLocation();
                }
                Toast.makeText(this, "Auto GPS data selected", Toast.LENGTH_SHORT).show();
                manualGPS = false;
                return true;
            case R.id.item_exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Exit Application");
                builder.setIcon(R.drawable.ic_exit);
                builder.setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    //GPS permissions
    @SuppressLint("MissingPermission")
    public void showLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            Toast.makeText(this, "Enable GPS", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showLocation();
            } else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    //Auto GPS
    @Override
    public void onLocationChanged(Location location) {
        if (!manualGPS) {
            memory.setGeolocation(location.getLatitude() + "," + location.getLongitude());
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();

        backgroundThread.stopThread();
        backgroundThread = new BackgroundThread(this);
        backgroundThread.startThread();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        connectivityManager.unregisterNetworkCallback(connectivityCallBack);

        try {
            backgroundThread.stopThread();
            backgroundThread.join();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provide, int status, Bundle extras) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }

}