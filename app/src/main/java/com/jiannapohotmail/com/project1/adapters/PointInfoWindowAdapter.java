package com.jiannapohotmail.com.project1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.jiannapohotmail.com.project1.R;


public class PointInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    Context context;
    LayoutInflater inflater;

    public PointInfoWindowAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.point_info_window, null);

        TextView location = v.findViewById(R.id.location_info_window_txt);
        TextView descr = v.findViewById(R.id.descr_info_window_txt);
        ImageView img = v.findViewById(R.id.info_window_img);

        location.setText(marker.getTitle());
        descr.setText(marker.getSnippet());

        return v;

    }
}
