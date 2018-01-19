package com.jiannapohotmail.com.project1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private ImageView infoImg;
    private Button infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;

    private ArrayList<PointModel> pointModels;
    private ArrayList<LatLng> positionsList;
    private HashMap<String, Marker> markerHashMap;
    private String[] imgResArray;
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        pointModels = new ArrayList<>();
        positionsList = new ArrayList<>();
        markerHashMap = new HashMap<>();
        imgResArray = getResources().getStringArray(R.array.locations);

        final SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
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
                // Here we can perform some action triggered after clicking the button
                Intent intent = new Intent(MapActivity.this, VideoActivity.class);
                intent.putExtra("video_resource", returnRightVideo(marker.getTitle()));
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
                infoImg.setBackgroundResource(returnRightImage(marker.getTitle()));

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

        setupData();

        // Add polyline
        for (int i = 0; i < positionsList.size() - 1; i++){
            Polyline line = mGoogleMap.addPolyline(new PolylineOptions()
                    .add(positionsList.get(i), positionsList.get(i + 1))
                    .width(5)
                    .color(Color.RED));
        }
        // Move camera to the first point
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointModels.get(0).getPosition(),15));
    }

    private void setupData(){
        generatePoints();
        fillDummyData();

        for (PointModel point : pointModels) {
            markerHashMap.put(point.getPointTitle(), mGoogleMap.addMarker(
                    new MarkerOptions()
                            .title(point.getPointTitle())
                            .snippet(point.getPointDescr())
                            .position(point.getPosition())
            ));
        }
    }

    private void fillDummyData(){
        for (int i = 0; i < positionsList.size(); i++)
        {
            pointModels.add(new PointModel(0,
                    imgResArray[i],
                    "DescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescription",
                    positionsList.get(i)));
        }
    }

    private void generatePoints(){
        positionsList.add(new LatLng(37.98295966, 23.72680771));
        positionsList.add(new LatLng(37.9855608, 23.72561459));
        positionsList.add(new LatLng(37.98234497, 23.73107424));
        positionsList.add(new LatLng(37.98034433, 23.72720912));
        positionsList.add(new LatLng(37.98599099, 23.723507));
        positionsList.add(new LatLng(37.98207, 23.72319334));
        positionsList.add(new LatLng(37.98204171, 23.73178142));
        positionsList.add(new LatLng(37.98785785, 23.72691545));
        positionsList.add(new LatLng(37.98371605, 23.72901413));
    }

    private int returnRightVideo(String title){
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
        else if (title.equals(imgResArray[8]))
            return R.raw.video_test;
        else
            return  0;
    }

    private int returnRightImage(String title){
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
        else if (title.equals(imgResArray[8]))
            return R.drawable.i09;
        else
            return  0;
    }

}
