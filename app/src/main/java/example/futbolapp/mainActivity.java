package example.futbolapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import example.futbolapp.database.DB_Manager;
import example.futbolapp.database.DbHelper;
import example.futbolapp.database.serverConnection;

/**
 * Created by Usuario on 11/09/2014.
 */
public class mainActivity  extends ActionBarActivity {

    private DrawerLayout drawerLayout;
    private ListView navList;
    private CharSequence mTitle;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        //SQLite
        //Creación o retorno de la base de datoS
        DB_Manager manager = new DB_Manager(this);
        //Insertar en la base de datos registro de usuario
        manager.registrarUsuario("Johan","ElCampeon10");
        //String nomb, String direccion, String tel,
        //String latitud, String longitud,
        // String icono, String info
        manager.insertarCancha("El templo","Cra 43 123 313","21232123","6.18324",
                "-75.58665","","Canchas de futbol el templo del futbl");
        manager.insertarCancha("El Señor Gol","Cra 43 123 313","21232123",
                "6.1830726","-75.5888026","","Canchas de futbol el Señor Gol");
        manager.insertarCancha("Unv. Eafit canchas","Cra 43 123 313","21232123",
                "6.200253"," -75.578846","","Canchas de futbol el Unv. Eafit");
        manager.insertarCancha("Marte 1","Cra 43 123 313","21232123",
                "6.256550"," -75.588950","","Canchas de futbol el Marte 1");
        manager.insertarCancha("Marte 2","Cra 43 123 313","21232123",
                "6.256145"," -75.588220","","Canchas de futbol el Marte 2");
        new serverConnection();
        mTitle = getTitle(); // Get current title

        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.navList = (ListView) findViewById(R.id.left_drawer);

        // Load an array of options names
        final String[] names = getResources().getStringArray(
                R.array.nav_options);

        // Set previous array as adapter of the list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, names);
        navList.setAdapter(adapter);
        navList.setOnItemClickListener(new DrawerItemClickListener());
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_navigation_drawer, R.string.open_drawer,
                R.string.close_drawer) {

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                // creates call to onPrepareOptionsMenu()
                supportInvalidateOptionsMenu();
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("Selecciona opción");
                // creates call to onPrepareOptionsMenu()
                supportInvalidateOptionsMenu();
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }
    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Get text from resources
        mTitle = getResources().getStringArray(R.array.nav_options)[position];

        // Create a new fragment and specify the option to show based on
        // position
       switch (position){
            case 0:
                ActionBarActivity prg = new programa();
                Bundle args = new Bundle();
                this.startActivity(new Intent(this, programa.class));
                break;
            case 1:
                ActionBarActivity bsc = new busqueda();
                new Bundle();
                this.startActivity(new Intent(this, busqueda.class));
                break;
            case 2:
                MapsActivity map = new MapsActivity();
                new Bundle();
                this.startActivity(new Intent(this, MapsActivity.class));
                break;
        }

         /*   Fragment fragment = new MyFragment();
            Bundle args = new Bundle();
            args.putString(MyFragment.KEY_TEXT, mTitle.toString());
            fragment.setArguments(args);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
          */
        // Highlight the selected item, update the title, and close the drawer
        navList.setItemChecked(position, true);
        getSupportActionBar().setTitle(mTitle);
        drawerLayout.closeDrawer(navList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    /*
 * Called whenever we call invalidateOptionsMenu()
 */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content
        // view
        boolean drawerOpen = drawerLayout.isDrawerOpen(navList);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Called by the system when the device configuration changes while your
        // activity is running
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }
    /*public void onBtnClick(View view){
        Intent intent;
           switch (view.getId()){
               case R.id.btn_programa:
                   intent = new Intent(this, programa.class);
                   break;
               case R.id.btnMapa:
                   intent = new Intent(this, MapsActivity.class);
                   break;
               default:
                   intent = new Intent(this, busqueda.class);
                   break;
           }
        startActivity(intent);
    }*/
}