package com.example.group3.csci3130_group3_project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;


public class MainActivity extends BaseActivity implements OnMapReadyCallback {
    FirebaseAuth firebaseAuth;
    Button logout_bt;
    TextView userName;
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    public Location mCurrentLocation;

    private static final int REQUEST_FINE_LOCATION_ACCESS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SET UP
        super.onCreate(savedInstanceState);
        addNavBar();
        setActivityLayout(R.layout.activity_main);

        //CHECK LOGIN
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, CredentialActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userName = findViewById(R.id.userEmail);
        userName.setText(user.getEmail());

        // Code that allegedly checks for GPS Availability
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }


        updateLocation();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onStart(){
        super.onStart();
        updateLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng dal = new LatLng(44.6366, -63.5917);
        mMap.addMarker(new MarkerOptions().position(dal).title("Dalhousie!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dal, 15));
    }

    public void mapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.searchBar);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("User Search"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            } else {
                LatLng sydney = new LatLng(-34, 151);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
            }
        }
    }


    //Sets the location on map to the one described in the global mCurrentLocation
    public void FIND(View view) {
        if (mCurrentLocation == null) {
            Display("Current Location NULL");
            return;
        }
        LatLng HERE = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(HERE).title("I AM HERE"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HERE, 15));
    }


    /*
    * Update location will use the location manager to get the most recent GPS coordinates set in the device/emulator
    * The response is processed in MyLocationListenerGPS
    * */
    public void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Display("Permission Error");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FINE_LOCATION_ACCESS);
            return;
        }
        mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new MyLocationListenerGPS(), null);
    }

    /*
     * After updateLocation() has been called MyLocationListenerGPS handles the responses to the update
     * Once the location is received it updates the global mCurrentLocation via the location manager
     * */
    public class MyLocationListenerGPS implements LocationListener {
        @SuppressLint("MissingPermission")
        @Override
        public void onLocationChanged(Location location) {
            Display("Location Changed!");
            mCurrentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Display("Status change");
            mCurrentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Display("provider change");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Display("provider died");
        }
    }

    /*
    * If the user has not granted the app the required permissions this code handles the request and response
    * TODO: Perform some sort of error handling in case they say no and improve current error handling
    * */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION_ACCESS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }

    public void Display(String e) {
        TextView st = (TextView) findViewById(R.id.statusText);
        st.setText(e);
    }
}