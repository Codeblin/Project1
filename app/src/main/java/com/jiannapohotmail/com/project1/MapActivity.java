package com.jiannapohotmail.com.project1;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
    private ArrayList<LatLng> positionsList;
    private HashMap<String, Marker> markerHashMap;
    // Resource arrays
    private String[] imgResArray;
    private String[] descrResArray;
    // Variables
    private GoogleMap mGoogleMap;
    private int DestinationIndex = 0;
    // User location
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String provider;
    // Constants
    private final float REQUIRED_DISTANCE = 25f; // meters

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
                startActivity(new Intent(MapActivity.this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init(){
        pointModels = new ArrayList<>();
        positionsList = new ArrayList<>();
        markerHashMap = new HashMap<>();
        imgResArray = getResources().getStringArray(R.array.locations);
        descrResArray = getResources().getStringArray(R.array.descriptions);

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
                    destinationLocation.setLatitude(positionsList.get(DestinationIndex).latitude);
                    destinationLocation.setLongitude(positionsList.get(DestinationIndex).longitude);

                    if (location.distanceTo(destinationLocation) < REQUIRED_DISTANCE){
                        //pointModels.get(DestinationIndex).setActive(false);
                        markerHashMap.get(imgResArray[DestinationIndex + 1]).setVisible(true);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                markerHashMap.get(imgResArray[DestinationIndex + 1]).getPosition(), 18);
                        mGoogleMap.animateCamera(cameraUpdate);

                        // Set destination to be the next item on the location list
                        DestinationIndex++;
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
                // Get video resource of the clicked button and start activity with it
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
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointModels.get(0).getPosition(),15));

        // Check permissions and set user Location
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    private void setupData(){
        generatePoints();
        fillPointData();

        for (PointModel point : pointModels) {
            markerHashMap.put(point.getPointTitle(), mGoogleMap.addMarker(
                    new MarkerOptions()
                            .title(point.getPointTitle())
                            .snippet(point.getPointDescr())
                            .position(point.getPosition())
                            .visible(point.IsActive())
            ));
        }
    }

    private void fillPointData(){
        for (int i = 0; i < positionsList.size(); i++)
        {
            pointModels.add(new PointModel(0,
                    imgResArray[i],
                    descrResArray[i],
                    positionsList.get(i),
                    i == 0));
        }
    }

    private void generatePoints(){
        positionsList.add(new LatLng(37.983759, 23.728071));
        positionsList.add(new LatLng(37.984636, 23.729551));
        positionsList.add(new LatLng(37.982675, 23.731091));
        positionsList.add(new LatLng(37.981897, 23.733619));
        positionsList.add(new LatLng(37.979796, 23.732544));
        positionsList.add(new LatLng(37.978673, 23.734347));
        positionsList.add(new LatLng(37.976599, 23.738636));
        positionsList.add(new LatLng(37.975651, 23.734001));
    }

    private int findRightVideo(String title){
        if (title.equals(imgResArray[0]))
            return R.raw.video_test;
        else if (title.equals(imgResArray[1]))
            return R.raw.video_test;
        else if (title.equals(imgResArray[2]))
            return R.raw.video_test;
        else if (title.equals(imgResArray[3]))
            return R.raw.video_test;
        else if (title.equals(imgResArray[4]))
            return R.raw.video_test;
        else if (title.equals(imgResArray[5]))
            return R.raw.video_test;
        else if (title.equals(imgResArray[6]))
            return R.raw.video_test;
        else if (title.equals(imgResArray[7]))
            return R.raw.video_test;
        else
            return  0;
    }

    private int findRightImage(String title){
        if (title.equals(imgResArray[0]))
            return R.drawable.i01;
        else if (title.equals(imgResArray[1]))
            return R.drawable.i02;
        else if (title.equals(imgResArray[2]))
            return R.drawable.i03;
        else if (title.equals(imgResArray[3]))
            return R.drawable.i04;
        else if (title.equals(imgResArray[4]))
            return R.drawable.i05;
        else if (title.equals(imgResArray[5]))
            return R.drawable.i06;
        else if (title.equals(imgResArray[6]))
            return R.drawable.i07;
        else if (title.equals(imgResArray[7]))
            return R.drawable.i08;
        else
            return  0;
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }
}
