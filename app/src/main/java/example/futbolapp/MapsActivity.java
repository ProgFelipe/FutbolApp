package example.futbolapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import example.futbolapp.database.local.DB_Manager;

public class MapsActivity extends FragmentActivity implements OnInfoWindowClickListener{

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
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            // adding marker
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            // create markers of soccer fields
            if(c.getCount() != 0) {
                MarkerOptions[] markers = new MarkerOptions[c.getCount()];

                c.moveToFirst();
                int i = 0;
                if (c.getString(1) != null || c.getString(4) != null || c.getString(5) != null || c.getString(3) != null) {
                    while (c.isAfterLast() == false) {
                        if(c.getString(7).matches("null")){
                            markers[i] = new MarkerOptions().position(new LatLng(Double.parseDouble(c.getString(4)), Double.parseDouble(c.getString(5))))
                                    .title(c.getString(1))
                                    .snippet(c.getString(3))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        }else{
                        markers[i] = new MarkerOptions().position(new LatLng(Double.parseDouble(c.getString(4)), Double.parseDouble(c.getString(5))))
                                .title(c.getString(1))
                                .snippet(c.getString(3) + "\n" + c.getString(7))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        }
                        i++;
                        c.moveToNext();
                    }
                }
                // adding marker
                for (i = 0; i < markers.length; i++) {
                    mMap.addMarker(markers[i]);
                }
            }
            mMap.getUiSettings().setCompassEnabled(true);
            //mMap user location and icon location
            mMap.setMyLocationEnabled(true);

        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */

    @Override
    public void onInfoWindowClick(Marker marker) {

        Intent intent = new Intent(getApplicationContext(), fieldActivity.class);
        Cursor c = manager.buscarCancha(marker.getTitle());
        if(c != null && c.getCount() > 0) {
            c.moveToFirst();
            intent.putExtra("fieldID", c.getString(0));
        }
        startActivity(intent);
        Toast.makeText(getBaseContext(),
                "Seleccionó " + marker.getTitle(),
                Toast.LENGTH_SHORT).show();
    }


}
