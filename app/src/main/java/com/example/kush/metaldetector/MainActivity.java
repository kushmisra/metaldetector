package com.example.kush.metaldetector;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView tv;
    float x;
    float y;
    float z;
    double calibrate =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        Log.i("reached", "oncreate");
        Toast.makeText(this, "reached oncreate", Toast.LENGTH_LONG).show();
    }

    public void calibrate(View v){

        String s = (String) tv.getText();
        calibrate = Double.parseDouble(s);

    }

    public void clicked(View view) {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor mymag = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, mymag, SensorManager.SENSOR_DELAY_UI);
        Log.i("reached", "clicked: click kar diya ");
        Toast.makeText(this, "reached clicked", Toast.LENGTH_SHORT).show();
    }


    LocationListener ll = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            float lat = (float) location.getLatitude();
            float lon = (float) location.getLongitude();
            long timemilli = System.currentTimeMillis();
            float alt = (float) location.getAltitude();
            GeomagneticField gmf = new GeomagneticField(lat,lon,alt,timemilli);
            Log.i("reached", "onSensorChanged: gmf aa gya");
            float ex = gmf.getX()/1000;
            float ey = gmf.getY()/1000;
            float ez = gmf.getZ()/1000;
            Log.i("reached", "onSensorChanged: ex ey ez aa gye");
            double resulte = Math.sqrt(ex*ex+ey*ey+ez*ez);
            double result = Math.sqrt(x*x+y*y+z*z);
            tv.setText(String.valueOf(resulte-result-calibrate));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(i);

        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];
        Log.i("reached", "onSensorChanged: just before access fine location ");
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                             Manifest.permission.INTERNET},10);
            return;
        }
        lm.requestLocationUpdates("gps", 2000, 2, ll);
        Log.i("reached", "onSensorChanged: after access fine location ");
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}
