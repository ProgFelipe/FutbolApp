package example.futbolapp;

import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import example.futbolapp.database.local.DB_Manager;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    // latitude and longitude

    //Datos quemados
    double latitude = 6.235925000000000000;
    double longitude = -75.575136999999980000;
    private double[] MedColombia = {latitude, longitude};

    //
    private MarkerOptions marker;
    private CameraPosition cameraPosition;
    private DB_Manager manager;
    private Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create marker
        //marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps ");
        manager = new DB_Manager(this);
        c = manager.cargarCursorCanchas();


        cameraPosition = new CameraPosition.Builder().target(
                new LatLng(MedColombia[0], MedColombia[1])).zoom(12).build();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            // adding marker
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            // create marker
            MarkerOptions [] markers = new MarkerOptions[c.getCount()];

            c.moveToFirst();
            int i = 0;
            if(c.getString(1) != null || c.getString(4) != null || c.getString(5) != null || c.getString(3) != null) {
                while (c.isAfterLast() == false) {
                    markers[i] = new MarkerOptions().position(new LatLng(Double.parseDouble(c.getString(4)), Double.parseDouble(c.getString(5))))
                            .title(c.getString(1))
                            .snippet(c.getString(3) + "\n" + c.getString(7));
                    i++;
                    c.moveToNext();
                }
            }

;
            // Changing marker icon
            //marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.arco_128x128));

            // adding marker
            for(i = 0; i < markers.length; i++){
                mMap.addMarker(markers[i]);
            }
            mMap.getUiSettings().setCompassEnabled(true);
            //mMap user location and icon location
            mMap.setMyLocationEnabled(true);

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}
