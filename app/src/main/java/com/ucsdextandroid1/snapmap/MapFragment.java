package com.ucsdextandroid1.snapmap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ucsdextandroid1.snapmap.util.WindowUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjaylward on 2019-04-26
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final int ZOOM_LEVEL = 7;

    private MapView mapView;
    private @Nullable GoogleMap googleMap;
    private UserLocationsAdapter adapter;

    private Drawable vectorDrawable;

    private List<Marker> markers = new ArrayList<>();

    public static MapFragment create() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.fm_list);
        recyclerView.setVisibility(View.VISIBLE);  //IT WAS GONE AND CHANGE TO VISIBLE TO SEE THE BOTTOM BOX

        WindowUtil.doOnApplyWindowInsetsToMargins(recyclerView, false, true);

        vectorDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_figure_black);

        mapView = root.findViewById(R.id.fm_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));

        adapter = new UserLocationsAdapter();

        recyclerView.setAdapter(adapter);

 //       DataSources.getInstance().getActiveUserLocations(new DataSources.Callback<List<UserLocationData>>() {
 //           @Override
 //           public void onDataFetched(List<UserLocationData> data) {
//
 //               adapter.setItems(data);
 //           }
 //       });
        //NEW end

        updateAdapter();
        updateUserLocation();

        return root;
    }

    private void updateAdapter() {

        DataSources.getInstance().getActiveUserLocations(new DataSources.Callback<List<UserLocationData>>() {
            @Override
            public void onDataFetched(List<UserLocationData> data) {
                adapter.setItems(data);
            }
        });
    }

    private void updateUserLocation() {

        String userId = "user_04";

        UserLocationData locationData = new UserLocationData(
                "#05688F", // or do color: #4527ao if not passing the value from string  //getString(R.color.test_color)
                32.8248175,
                -117.3891616,
                "San Diego, CA",
                 userId,
                "Sheirman"
        );

        DataSources.getInstance().updateUser(userId, locationData, new DataSources.Callback<UserLocationData>() {
            @Override
            public void onDataFetched(UserLocationData data) {
                Toast.makeText(getContext(), data != null ? "Success" : "Failure", Toast.LENGTH_SHORT)
                        .show();

                if(data != null)
                    updateAdapter();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
        vectorDrawable = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

//    private MarkerOptions createMarker(UserLocationData data, LatLng latLng) {
//        return new MarkerOptions()
//                .position(latLng)
//                .title(data.getUserName())
//                .snippet(data.getLocationName())
//                .icon(bitmapDescriptorFromVector(Color.parseColor(data.getColor())));
//    }

//    private void selectMarkerAtIndex(int index) {
//        if(!markers.isEmpty() && googleMap != null) {
//            Marker marker = markers.get(index);
//            marker.showInfoWindow();
//            googleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), ZOOM_LEVEL, null);
//        }
//    }

    private BitmapDescriptor bitmapDescriptorFromVector(@ColorInt int tint) {
        vectorDrawable.setTint(tint);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(38,24));
        markerOptions.title("Athens, Greece");
        googleMap.addMarker(markerOptions);
    }
}
