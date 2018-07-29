
package com.example.group3.csci3130_group3_project;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivityDir extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,DirectionFinderListener {

    private  static final int REQUEST_LOCATION_CODE=99;
    private GoogleMap mMap;
    private GoogleApiClient cli;
    private LocationRequest locationRequest;
    MarkerOptions start;
    MarkerOptions end;
    private Location lastloc;
    private Button btnFindPath;
    private Marker currLocMarker;
    double latitude,longitude;
    private  String cur;
    private  String de;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    private  String dest;//destination

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_dir);
        //get intent
        Intent i=getIntent();
        dest=i.getStringExtra("loc");


        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
        {
            checkLoPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        btnFindPath=(Button)findViewById(R.id.pathBt);
        mapFragment.getMapAsync(this);

        //click the path button
        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMap.clear();
                sendRequest();
            }
        });

    }

    private void sendRequest() {
        String origin = cur;
        String destination = de;
        if (origin.isEmpty()) {
            Toast.makeText(this, R.string.mapsactivity_enter_origin_address, Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, R.string.mapsactivity_enter_destination_address, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                    {
                        if(cli==null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                    else
                    {
                        Toast.makeText(this,R.string.mapsactivity_pd,Toast.LENGTH_LONG).show();

                    }
                    return;
                }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
           if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
               buildGoogleApiClient();
               mMap.setMyLocationEnabled(true);
           }

        findDes();


        return;


    }

    protected synchronized void buildGoogleApiClient()
    {
        cli=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        cli.connect();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest=new LocationRequest();

        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(cli,locationRequest,this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }

    @Override
    public void onLocationChanged(Location location)
    {
        lastloc=location;
        if(currLocMarker!=null)
        {
            currLocMarker.remove();
        }
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng);
        cur=getAddress(location.getLatitude(),location.getLongitude());
        markerOptions.title("current location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        getAddress(location.getLatitude(),location.getLongitude());
        start=markerOptions;
        currLocMarker=mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(13));
        if(cli!=null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(cli,this);
        }
    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            //add = add + "\n" + obj.getCountryName();
           // add = add + "\n" + obj.getCountryCode();
           // add = add + "\n" + obj.getAdminArea();
            add = add + "" + obj.getPostalCode();
           // add = add + "" + obj.getSubAdminArea();
            //add = add + " " + obj.getLocality();
            //add = add + " " + obj.getSubThoroughfare();
            return add;
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return "";
    }
    public boolean checkLoPermission()
    {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_CHECKIN_PROPERTIES},REQUEST_LOCATION_CODE);
            }else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_CHECKIN_PROPERTIES},REQUEST_LOCATION_CODE);

            }
            return false;
        }else
        {
            return true;
        }
    }

    public void findDes() {
        TextView destText=(TextView) findViewById(R.id.dirtextView);
        destText.setText(dest);
        List<Address> addressList=null;
        MarkerOptions markerOptions=new MarkerOptions();

        Geocoder geocoder=new Geocoder(this);
        try {
            addressList=geocoder.getFromLocationName(dest,5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i=0;i<addressList.size();i++)
        {
            Address address = addressList.get(i);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            latitude = address.getLatitude();
            longitude = address.getLongitude();
            markerOptions.position(latLng);
            de=getAddress(address.getLatitude(),address.getLongitude());
            markerOptions.title("Destination");
            end=markerOptions;
            mMap.addMarker(markerOptions);
        }
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, String.valueOf(R.string.mapsactivity_wait) ,
                String.valueOf(R.string.mapsactivity_nav), true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(false).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }


}

