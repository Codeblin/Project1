package com.jiannapohotmail.com.project1.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jiannapohotmail.com.project1.data.managers.SharedPreferencesManager;
import com.jiannapohotmail.com.project1.views.MapWrapperLayout;
import com.jiannapohotmail.com.project1.listeners.OnInfoWindowElemTouchListener;
import com.jiannapohotmail.com.project1.R;
import com.jiannapohotmail.com.project1.data.models.PointModel;

import java.util.ArrayList;
import java.util.HashMap;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    // Info window views
    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private ImageView infoImg;
    private Button infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;
    // Data structures
    private ArrayList<PointModel> pointModels;
    private HashMap<String, Marker> markerHashMap;
    // Variables
    private GoogleMap mGoogleMap;
    private int DestinationIndex;
    // User location
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String provider;
    // Constants
    private final float REQUIRED_DISTANCE = 25f; // meters
    // Managers
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        init();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(provider, 1000, 1, locationListener);

        final SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_help:
                Intent intent = new Intent(MapActivity.this, HelpActivity.class);
                intent.putExtra("visibility_flag", View.GONE);
                startActivity(intent);
                return true;
            case R.id.menu_about:
                Intent intentabout = new Intent(MapActivity.this, AboutActivity.class);
                startActivity(intentabout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init(){
        sharedPreferencesManager = new SharedPreferencesManager(this);
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesManager.SHARED_PREF_DATA, MODE_PRIVATE);
        DestinationIndex = sharedPreferences.getInt(SharedPreferencesManager.KEY_DESTINATION_INDEX, 0);

        pointModels = sharedPreferencesManager.GetListFromSharedPreferences();
        markerHashMap = new HashMap<>();

        userTrackingInit();
    }

    private void userTrackingInit(){
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        assert locationManager != null;
        provider = locationManager.getBestProvider(criteria, true);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (DestinationIndex < pointModels.size() - 1){
                    Location destinationLocation = new Location("currentDestination");
                    destinationLocation.setLatitude(pointModels.get(DestinationIndex).getLatitude());
                    destinationLocation.setLongitude(pointModels.get(DestinationIndex).getLongitude());

                    if (location.distanceTo(destinationLocation) < REQUIRED_DISTANCE){
                        markerHashMap.get(pointModels.get(DestinationIndex + 1).getPointTitle()).setVisible(true);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                markerHashMap.get(pointModels.get(DestinationIndex + 1).getPointTitle()).getPosition(), 15);
                        mGoogleMap.animateCamera(cameraUpdate);

                        // Set destination to be the next item on the location list and update flag for marker that is active
                        DestinationIndex++;
                        pointModels.get(DestinationIndex).setActive(true);
                        sharedPreferencesManager.SaveDataToSharedPreferences(pointModels, DestinationIndex);
                    }
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        setupData();
        final MapWrapperLayout mapWrapperLayout = findViewById(R.id.map_relative_layout);

        // MapWrapperLayout initialization
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge
        mapWrapperLayout.init(mGoogleMap, getPixelsFromDp(this, 39 + 20));

        // We want to reuse the info window for all the markers,
        // so let's create only one class member instance
        this.infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.point_info_window, null);
        this.infoTitle = infoWindow.findViewById(R.id.location_info_window_txt);
        this.infoSnippet = infoWindow.findViewById(R.id.descr_info_window_txt);
        this.infoButton = infoWindow.findViewById(R.id.info_window_btn);
        this.infoImg = infoWindow.findViewById(R.id.info_window_img);

        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        this.infoButtonListener = new OnInfoWindowElemTouchListener(infoButton)
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                //Get video resource of the clicked button and start activity with it
                Intent intent = new Intent(MapActivity.this, VideoActivity.class);
                intent.putExtra("video_resource", findRightVideo(marker.getTitle()));
                startActivity(intent);
            }
        };
        this.infoButton.setOnTouchListener(infoButtonListener);


        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Setting up the infoWindow with current's marker info
                infoTitle.setText(marker.getTitle());
                infoSnippet.setText(marker.getSnippet());
                infoButtonListener.setMarker(marker);
                infoImg.setBackgroundResource(findRightImage(marker.getTitle()));

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

        // Move camera to the first point
        LatLng location = new LatLng(pointModels.get(0).getLatitude(), pointModels.get(0).getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));

        // Check permissions and set user Location
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    private void setupData(){

        for (PointModel point : pointModels) {
            LatLng location = new LatLng(point.getLatitude(), point.getLongitude());
            markerHashMap.put(point.getPointTitle(), mGoogleMap.addMarker(
                    new MarkerOptions()
                            .title(point.getPointTitle())
                            .snippet(point.getPointDescr())
                            .position(location)
                            .visible(point.IsActive())
            ));
        }
    }

    private int findRightVideo(String title){
        if (title.equals(pointModels.get(0).getPointTitle()))
            return R.raw.diplomatiki_afetiria_teliko_compressed;
        else if (title.equals(pointModels.get(1).getPointTitle()))
            return R.raw.diplomatiki_stasi_2_espo_compressed;
        else if (title.equals(pointModels.get(2).getPointTitle()))
            return R.raw.eam_theatrou_2_compressed;
        else if (title.equals(pointModels.get(3).getPointTitle()))
            return R.raw.epon_spoudazousa_compressed;
        else if (title.equals(pointModels.get(4).getPointTitle()))
            return R.raw.v5korai;
        else if (title.equals(pointModels.get(5).getPointTitle()))
            return R.raw.v6omirou;
        else if (title.equals(pointModels.get(6).getPointTitle()))
            return R.raw.v7merlin;
        else if (title.equals(pointModels.get(7).getPointTitle()))
            return R.raw.v8syntagma;
        else
            return  0;
    }

    private int findRightImage(String title){
        if (title.equals(pointModels.get(0).getPointTitle()))
            return R.drawable.i01;
        else if (title.equals(pointModels.get(1).getPointTitle()))
            return R.drawable.i02;
        else if (title.equals(pointModels.get(2).getPointTitle()))
            return R.drawable.i03;
        else if (title.equals(pointModels.get(3).getPointTitle()))
            return R.drawable.i04;
        else if (title.equals(pointModels.get(4).getPointTitle()))
            return R.drawable.i05;
        else if (title.equals(pointModels.get(5).getPointTitle()))
            return R.drawable.i06;
        else if (title.equals(pointModels.get(6).getPointTitle()))
            return R.drawable.i07;
        else if (title.equals(pointModels.get(7).getPointTitle()))
            return R.drawable.i08;
        else
            return  0;
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }
}
