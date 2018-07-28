package com.example.group3.csci3130_group3_project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, DirectionFinderListener {
    FirebaseAuth firebaseAuth;
    public DatabaseReference firebaseReference;
    public FirebaseDatabase firebaseDBInstance;
    //Button logout_bt;
   // TextView userName;
    String uid;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    public Location mCurrentLocation;
    private LatLngBounds.Builder mBounds = new LatLngBounds.Builder();
   // public Button dirBt;
    public SearchView searchBar;
    //below is used for callbacks in permission checking
    private static final int REQUEST_FINE_LOCATION_ACCESS = 1;
    //

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SET UP
        super.onCreate(savedInstanceState);
        addNavBar();
        setActivityLayout(R.layout.activity_main);
        searchBar = (SearchView) findViewById(R.id.searchBar);
        searchBar.setSubmitButtonEnabled(true);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //**************************BT**************************************
       /* dirBt=(Button)findViewById(R.id.dir_button);


        dirBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((EditText)findViewById(R.id.searchBar)).getText().toString().trim().length()>2)
                {
                    String val=((EditText)findViewById(R.id.searchBar)).getText().toString().trim();
                    Intent i=new Intent( MainActivity.this,MapsActivityDir.class);
                    i.putExtra("loc",val);
                    startActivity(i);
                }else
                {

                }
            }
        });
        //**************************BT**************************************
        */
        //CHECK LOGIN
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, CredentialActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
       // userName = findViewById(R.id.userEmail);
       // userName.setText(user.getEmail());
        uid = user.getUid();
        firebaseDBInstance = FirebaseDatabase.getInstance();

        //Connect to Google API client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API).addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        // Code that checks for enabled Location Services
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mLocationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER,true);
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.mapsactivity_loaction_services_notactive);
            builder.setMessage(R.string.mapsactivity_gps_location_not_active);
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

        //COLOR THE
        colorBG();
    }

    public void colorBG() {
        final String[] colorString = {"FFFFFF"}; //default
        ChildEventListener userListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserProfile thisUser = dataSnapshot.getValue(UserProfile.class);
                if (thisUser != null) {
                    colorString[0] = thisUser.getFavoriteColor();
                    Log.d("Favorite COLOR:", colorString[0]);
                }
                View thisView = findViewById(R.id.drawer_layout);
                int color;
                try {
                    color = Color.parseColor(String.valueOf(colorString[0]));
                } catch (Exception e) {
                    color = Color.parseColor(String.valueOf("#FFFFFF"));
                }
                thisView.setBackgroundColor(color);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                View thisView = findViewById(R.id.mainViewForBG);
//                View rootOfThisView = thisView.getRootView();
//                UserProfile thisUser = dataSnapshot.getValue(UserProfile.class);
//                if (thisUser != null) {
//                    String colorString = thisUser.getFavoriteColor();
//                    Log.d("Favorite COLOR:", colorString);
//                    int color = Color.parseColor(String.valueOf(colorString));
//                    rootOfThisView.setBackgroundColor(color);

                UserProfile thisUser = dataSnapshot.getValue(UserProfile.class);
                if (thisUser != null) {
                    colorString[0] = thisUser.getFavoriteColor();
                    Log.d("Favorite COLOR:", colorString[0]);
                }
                View thisView = findViewById(R.id.drawer_layout);
                int color;
                try {
                    color = Color.parseColor(String.valueOf(colorString[0]));
                } catch (Exception e) {
                    color = Color.parseColor(String.valueOf("#FFFFFF"));
                }
                thisView.setBackgroundColor(color);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, CredentialActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseDBInstance = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDBInstance.getReference();
        firebaseReference.child("users").child(user.getUid()).child("userprofile").addChildEventListener(userListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateLocation();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        UiSettings settings = mMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
        //Need to explicitly check for permission before accessing location
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FINE_LOCATION_ACCESS);
        }


        mMap.setMyLocationEnabled(true);

        /* Add a marker in Dal and move the camera
        This code was for learning purposes only.
        LatLng dal = new LatLng(44.6366, -63.5917);
        mMap.addMarker(new MarkerOptions().position(dal).title("Dalhousie!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dal));
        */
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                addPointToViewPort(ll);
                // we only want to grab the location once, to allow the user to pan and zoom freely.
                mMap.setOnMyLocationChangeListener(null);
            }
        });
        mMap.setOnMapLongClickListener(this);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if(getIntent().hasExtra("Favorite")) {
                    Favorite sentFavorite = (Favorite) getIntent().getSerializableExtra("Favorite");
                    if (sentFavorite != null) {
                        LatLng sentLocation = new LatLng(sentFavorite.getmLatitude(), sentFavorite.getmLongitude());
                        performSearch(sentLocation);

                    }
                }
                //Check for serializable extras for navigation
                if(getIntent().hasExtra("Address")){
                    String location = (String) getIntent().getSerializableExtra("Address");
                    performSearch(location);
                }
                if(getIntent().hasExtra("Course Sent")){
                    Course sentCourse = (Course) getIntent().getSerializableExtra("Course Sent");
                    if (sentCourse != null){
                        String address = sentCourse.address;
                        performSearch(address);
                    }
                }
                if(getIntent().hasExtra("my course")){
                    Course sentCourse = (Course) getIntent().getSerializableExtra("my course");
                    if(sentCourse != null){
                        String address = sentCourse.address;
                        performSearch(address);
                    }
                }
            }
        });


    }

    private void addPointToViewPort(LatLng newPoint) {
        mBounds.include(newPoint);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mBounds.build(),
                findViewById(R.id.searchBar).getHeight()));
    }


    public void performSearch(LatLng location){
        List<Address> addressList = null;
        List<Address> currentLocation = null;
        if (location != null) {
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                    currentLocation = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(addressList != null && currentLocation != null) {
                    if (!addressList.isEmpty()) {
                        Address address = addressList.get(0);
                        Address originAddress = currentLocation.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        String destination = address.getAddressLine(0) + "" + address.getPostalCode();
                        String origin = originAddress.getAddressLine(0) + "" + originAddress.getPostalCode();
                        sendRequest(origin, destination);
                    } else {
                        Toast.makeText(this, R.string.mapsactivity_place_not_found, Toast.LENGTH_LONG);
                    }
                }
        }

    }
    public void performSearch(String input){
        List<Address> addressList = null;
        List<Address> currentLocation = null;
        String location = input;
        Log.d("Perform Search:", "Prior to location confirm");
        if (location != null) {
            if(!location.isEmpty()) {

                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                    currentLocation = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("Perform Search:", "Prior to address confirm");
                if(addressList != null && currentLocation != null) {
                    if (!addressList.isEmpty()) {
                        Address address = addressList.get(0);
                        Address originAddress = currentLocation.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        String destination = address.getAddressLine(0) + "" + address.getPostalCode();
                        String origin = originAddress.getAddressLine(0) + "" + originAddress.getPostalCode();
                        Log.d("Perform search:", origin + " " + destination);
                        sendRequest(origin, destination);
                    } else {
                        Toast.makeText(this, R.string.mapsactivity_place_not_found, Toast.LENGTH_LONG);
                    }
                }
            }
        }
    }
    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, String.valueOf(R.string.mapsactivity_wait),
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
        LatLngBounds.Builder routeBuilder = new LatLngBounds.Builder();

        for (Route route : routes) {
            Log.d(String.format("Route %s to ", route.startAddress), route.endAddress );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.routeTime)).setText(route.duration.text);
            ((TextView) findViewById(R.id.routeDistance)).setText(route.distance.text);

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

            for (int i = 0; i < route.points.size(); i++) {
                polylineOptions.add(route.points.get(i));
                Log.d(String.format("Route finder point %d", i), String.format("%f, %f", route.points.get(i).latitude, route.points.get(i).longitude));
                routeBuilder.include(route.points.get(i));
            }
            LatLngBounds routeBounds = routeBuilder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(routeBounds, 10));
            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }


    //Sets the location on map to the one described in the global mCurrentLocation
  /*Commented out this method as GoogleMaps provides a UI element which does the same thing
    public void FIND(View view) {
        if (mCurrentLocation == null) {
            Display("Current Location NULL");
            return;
        }
        LatLng HERE = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(HERE).title("I AM HERE"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(HERE));
    }
    *****************************************************************************/

    /*
    * Update location will use the location manager to get the most recent GPS coordinates set in the device/emulator
    * The response is processed in MyLocationListenerGPS
    * */
    public void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          //  Display("Permission Error");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FINE_LOCATION_ACCESS);
            return;
        }
        mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new MyLocationListenerGPS(), null);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        AddFavoriteDialog addFavoriteDialog = new AddFavoriteDialog();
        addFavoriteDialog.setNewLocation(latLng);
        addFavoriteDialog.show(getSupportFragmentManager(), "Favorites");
    }

    /*
     * After updateLocation() has been called MyLocationListenerGPS handles the responses to the update
     * Once the location is received it updates the global mCurrentLocation via the location manager
      */

    public class MyLocationListenerGPS implements LocationListener {
        @SuppressLint("MissingPermission")
        @Override
        public void onLocationChanged(Location location) {
           // Display("Location Changed!");
            mCurrentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
         //   Display("Status change");
            mCurrentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        @Override
        public void onProviderEnabled(String provider) {
           // Display("provider change");
        }

        @Override
        public void onProviderDisabled(String provider) {
            //Display("provider died");
        }
    }

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

    private void sendRequest(String origin, String destination) {
        Log.d("Location request:", "sendRequest entered");
        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
