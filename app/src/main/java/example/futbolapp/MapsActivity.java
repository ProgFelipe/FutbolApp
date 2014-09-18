package example.futbolapp;

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

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    // latitude and longitude

    //Datos quemados
    double latitude = 6.235925000000000000;
    double longitude = -75.575136999999980000;
    private double[] MedColombia = {latitude, longitude};
    private double[] temploFutbol = {6.18324,-75.58665};
    private double[] senorGol = {6.1830726,-75.5888026};
    private double[] eafit = {6.200253, -75.578846};
    private double[] marte1 = {6.256550, -75.588950};
    private double[] marte2 = {6.256145, -75.588220};
    private double[] aristiGol = {6.155456, -75.614233};

    //
    private MarkerOptions marker;
    private CameraPosition cameraPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create marker
        //marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps ");
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
            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Medellin");
            MarkerOptions marker2 = new MarkerOptions().position(new LatLng(temploFutbol[0], temploFutbol[1])).title("Templo del futbol");
            MarkerOptions marker3 = new MarkerOptions().position(new LatLng(senorGol[0], senorGol[1])).title("Señor Gol");
            MarkerOptions marker4 = new MarkerOptions().position(new LatLng(eafit[0], eafit[1])).title("CANCHA Unv. EAFIT");
            MarkerOptions marker5 = new MarkerOptions().position(new LatLng(marte1[0], marte1[1])).title("MARTE 1");
            MarkerOptions marker6 = new MarkerOptions().position(new LatLng(marte2[0], marte2[1])).title("MARTE 2");
            MarkerOptions marker7 = new MarkerOptions().position(new LatLng(aristiGol[0], aristiGol[1])).title("Aristigol");


            // Changing marker icon
            //marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.arco_128x128));

            // adding marker
            mMap.addMarker(marker);
            mMap.addMarker(marker2);
            mMap.addMarker(marker3);
            mMap.addMarker(marker4);
            mMap.addMarker(marker5);
            mMap.addMarker(marker6);
            mMap.addMarker(marker7);
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
