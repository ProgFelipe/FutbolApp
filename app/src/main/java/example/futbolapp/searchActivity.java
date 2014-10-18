package example.futbolapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
//Json libraries
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//SQLite
import example.futbolapp.View.DrawerItemCustomAdapter;
import example.futbolapp.View.ObjectDrawerItem;
import example.futbolapp.database.MergeData;
import example.futbolapp.database.local.DB_Manager;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import java.util.ArrayList;

/**
 * Created by Usuario on 11/09/2014.
 * Apoyado en :
 * Tutorial 16: Recuperar y Consultar Registros de SQLite con Android y mostrarlos en un ListView
 * https://www.youtube.com/watch?v=sD-pz-vKlnI
 *
 */

public class searchActivity extends ActionBarActivity {
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    //
    private ProgressDialog pDialog;
    //
    private Cursor cursor;
    private DB_Manager manager;
    private boolean searchDone;
    private ListView listView;
    SimpleCursorAdapter adapter;
    private SearchView searchView ;
    private AutoCompleteTextView autoComplete;
    //AQuery object
    AQuery aq;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busqueda);
        searchDone = false;
        //Instantiate AQuery Object
        aq = new AQuery(this);
        listView = (ListView)findViewById(R.id.listViewSearch);
        //autoComplete = (AutoCompleteTextView)findViewById(R.id.autocomplete);

        //Cambia de vista al hacer click en un item de la lista de canchas
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //Toast.makeText(aq.getContext(), "", Toast.LENGTH_LONG).show();
                        TextView tv = (TextView)view.findViewById(android.R.id.text1);
                        String info = tv.getText().toString();

                        Intent intent = new Intent(getApplicationContext(), fieldActivity.class);
                        Cursor c = manager.buscarCancha(info);
                        if(c != null && c.getCount() > 0) {
                            c.moveToFirst();
                            intent.putExtra("fieldID", c.getString(0));
                        }
                        startActivity(intent);
                    }
                }
        );
        //Agrega a la vista de busqueda los metodos al buscar
        searchView = (SearchView) findViewById(R.id.searchFieldNameView);
        searchView.setQueryHint("Ingresa el nombre");
        //Buscar Cancha por nombre al hacer click en boton buscar
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener(){

                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        //startActivity(new Intent(searchActivity.this, eventsActivity.class));
                        searchDone = true;
                        new BuscarTask().execute();
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {

                        return false;
                    }
                });
        //Nav Drawer
        mTitle = mDrawerTitle = getTitle();

        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[4];

        drawerItem[0] = new ObjectDrawerItem(android.R.drawable.ic_menu_today, getResources().getString(R.string.nav0));
        drawerItem[1] = new ObjectDrawerItem(android.R.drawable.ic_menu_search, getResources().getString(R.string.nav1));
        drawerItem[2] = new ObjectDrawerItem(android.R.drawable.ic_menu_search, getResources().getString(R.string.nav2));
        drawerItem[3] = new ObjectDrawerItem(android.R.drawable.ic_dialog_map, getResources().getString(R.string.nav3));

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        //
        //SQlite
        manager = new DB_Manager(this);
        getFields();
        }
    public void getFields(){
        //JSON URL
        String url = "http://solweb.co/reservas/api/field/fields";
        //Make Asynchronous call using AJAX method and show progress gif until get info
        aq.progress(R.id.progressBarSearch).ajax(url, JSONObject.class, this,"jsonCallback");
    }

    public void jsonCallback(String url, JSONObject json, AjaxStatus status) {
        //When JSON is not null
        if (json != null) {
            //Log.v("JSON", json.toString());
            String jsonResponse = "";
            try {
                //Get json as Array
                JSONArray jsonArray = json.getJSONArray("field");
                if (jsonArray != null) {
                    int len = jsonArray.length();
                    manager.eliminarCanchas();
                    for (int i = 0; i < len; i++) {
                        //Get the name of the field from array index
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //SQLite
                        if(manager.buscarIdCancha(jsonObject.getString("id")) == false)
                        {
                            manager.insertarCancha(Integer.parseInt(jsonObject.getString("id")), jsonObject.getString("name"), jsonObject.getString("address"),
                                    jsonObject.getString("phone"), jsonObject.getString("latitude"), jsonObject.getString("length"),
                                    jsonObject.getString("icon"), jsonObject.getString("description"));
                        }
                        /*else{
                            manager.modificarInfoCancha(Integer.parseInt(jsonObject.getString("id")), jsonObject.getString("name"), jsonObject.getString("address"),
                                    jsonObject.getString("phone"), jsonObject.getString("latitude"), jsonObject.getString("length"),
                                    jsonObject.getString("icon"), jsonObject.getString("description"));
                        }*/
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(aq.getContext(), "Error in parsing JSON", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(aq.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
            cursor = manager.cargarCursorCanchas();
            String[] from = new String[]{manager.CN_Nombre,manager.CN_Telefono};
            int [] to = new int[]{android.R.id.text1,android.R.id.text2};
            adapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,cursor, from, to,0);
            listView.setAdapter(adapter);
        }
        //When JSON is null
        else {
            //When response code is 500 (Internal Server Error)
            if(status.getCode() == 500){
                Toast.makeText(aq.getContext(),"Server is busy or down. Try again!",Toast.LENGTH_SHORT).show();
            }
            //When response code is 404 (Not found)
            else if(status.getCode() == 404){
                Toast.makeText(aq.getContext(),"Resource not found!",Toast.LENGTH_SHORT).show();
            }
            //When response code is other 500 or 404
            else{
                Toast.makeText(aq.getContext(),"Verifique su conexión",Toast.LENGTH_SHORT).show();
            }
            cursor = manager.cargarCursorCanchas();
            String[] from = new String[]{manager.CN_Nombre,manager.CN_Telefono};
            int [] to = new int[]{android.R.id.text1,android.R.id.text2};
            adapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,cursor, from, to,0);
            listView.setAdapter(adapter);
        }
    }


    public void getAllFields(){
        Toast.makeText(aq.getContext(),"Obteniendo canchas ...",Toast.LENGTH_SHORT).show();

        synchronized(this) {
            //SQlite
            manager = new DB_Manager(this);
            //Busca cambios en la base de datos local con respecto a la externa
            //Si los hay actualiza la base de datos local
            MergeData merge = new MergeData(this, this.aq, manager);
            merge.getCanchas();
            //Text view two_line_list por defecto de android
        }
        synchronized(this) {
            cursor = manager.cargarCursorCanchas();
            String[] from = new String[]{manager.CN_Nombre, manager.CN_Telefono};
            int[] to = new int[]{android.R.id.text1, android.R.id.text2};
            adapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, cursor, from, to, 0);
            listView.setAdapter(adapter);
        }
    }
    private class BuscarTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected  void onPreExecute(){
            pDialog = new ProgressDialog(searchActivity.this);
            pDialog.setMessage("Por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
            //Toast.makeText(getApplicationContext(),"Buscando cancha ...", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            cursor = manager.buscarCancha(searchView.getQuery().toString());
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.changeCursor(cursor);
            if (pDialog.isShowing())
                pDialog.dismiss();
            //Toast.makeText(getApplicationContext(),"Busqueda Finalizada ...", Toast.LENGTH_SHORT).show();
            //Show getted information of fields
        }
    }
    @Override
    public void onBackPressed() {
        if(searchDone){
            searchDone = false;
            getAllFields();
        }else{
            finish();
        }
    }
    //Navigation Drawer methods
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }


    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                new eventsActivity();
                new Bundle();
                this.startActivity(new Intent(this, eventsActivity.class));
                finish();
                break;
            case 1:
                new searchActivity();
                new Bundle();
                this.startActivity(new Intent(this, searchActivity.class));
                finish();
                break;
            case 2:
                new searchInTime();
                new Bundle();
                this.startActivity(new Intent(this, searchInTime.class));
                finish();
                break;
            case 3:
                new MapsActivity();
                new Bundle();
                this.startActivity(new Intent(this, MapsActivity.class));
                finish();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            getActionBar().setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }

    }
}
