package wrld.team1.com.world3dworkpresence;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.buildings.BuildingHighlight;
import com.eegeo.mapapi.buildings.BuildingHighlightOptions;
import com.eegeo.mapapi.buildings.BuildingsApi;
import com.eegeo.mapapi.camera.CameraPosition;
import com.eegeo.mapapi.camera.CameraUpdateFactory;
import com.eegeo.mapapi.geometry.LatLngAlt;
import com.eegeo.mapapi.map.OnInitialStreamingCompleteListener;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.markers.Marker;
import com.eegeo.mapapi.markers.MarkerOptions;

public class MapsActivity extends AppCompatActivity {
    private MapView mapView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EegeoApi.init(this, getString(R.string.api_key));
        setContentView(R.layout.activity_maps);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback(){
            @Override
            public void onMapReady(final EegeoMap eegeoMap)
            {
                eegeoMap.addOnMapClickListener(new OnMapClickedHandler(eegeoMap));
                eegeoMap.addInitialStreamingCompleteListener(new OnInitialStreamingCompleteListener()
                {
                    @Override
                    public void onInitialStreamingComplete()
                    {
                        CameraPosition position = new CameraPosition.Builder()
                                .target(56.460083, -2.978135)
                                .indoor("westport_house", 2)
                                .zoom(19)
                                .bearing(270)
                                .build();
                        eegeoMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
                    }
                });

                Toast.makeText(MapsActivity.this, "Welcome to Westport House workspace.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
    }


    public class OnMapClickedHandler implements EegeoMap.OnMapClickListener
    {
        private EegeoMap map = null;
        private Marker marker = null;

        OnMapClickedHandler(EegeoMap eegeoMap)
        {
            this.map = eegeoMap;
        }

        @Override
        public void onMapClick(LatLngAlt point)
        {
            if(marker != null)
            {
                map.removeMarker(marker);
                marker = null;
            }
            MarkerOptions mOptions = new MarkerOptions().position(point.toLatLng()).labelText("Selected");

            if(map.getActiveIndoorMap() != null)
            {
                mOptions.indoor(map.getActiveIndoorMap().id, map.getCurrentFloorIndex());
            }

            marker = map.addMarker(mOptions);
            Toast.makeText(MapsActivity.this, String.format("Lat: %f  ,  Lng: %f  , Alt: %f m", point.latitude, point.longitude, point.altitude), Toast.LENGTH_LONG).show();
        }

    }
}
