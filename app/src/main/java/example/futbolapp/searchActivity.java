package example.futbolapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
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
    private CharSequence mTitle;
    private DrawerLayout drawerLayout;
    private ListView navList;
    private ActionBarDrawerToggle drawerToggle;
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

    ArrayList<String> fields;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busqueda);
        searchDone = false;
        //Instantiate AQuery Object
        aq = new AQuery(this);
        fields = new ArrayList<String>();
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
        //
        mTitle = getTitle(); // Get current title
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layoutBsq);
        this.navList = (ListView) findViewById(R.id.left_drawerBsqNombre);

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
                    for (int i = 0; i < len; i++) {
                        //Get the name of the field from array index
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        fields.add(jsonObject.getString("name"));

                        //SQLite
                        if(manager.buscarIdCancha(jsonObject.getString("id")) == false)
                        {
                            manager.insertarCancha(Integer.parseInt(jsonObject.getString("id")), jsonObject.getString("name"), jsonObject.getString("address"),
                                    jsonObject.getString("phone"), jsonObject.getString("latitude"), jsonObject.getString("length"),
                                    jsonObject.getString("icon"), jsonObject.getString("description"));
                        }
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
                Toast.makeText(aq.getContext(),"Unexpected Error occured",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(),"Buscando cancha ...", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            cursor = manager.buscarCancha(searchView.getQuery().toString());
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.changeCursor(cursor);
            Toast.makeText(getApplicationContext(),"Busqueda Finalizada ...", Toast.LENGTH_SHORT).show();
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
        switch (position) {
            case 0:
                ActionBarActivity prg = new eventsActivity();
                Bundle args = new Bundle();
                this.startActivity(new Intent(this, eventsActivity.class));
                finish();
                break;
            case 1:
                ActionBarActivity bsc = new searchActivity();
                new Bundle();
                finish();
                this.startActivity(new Intent(this, searchActivity.class));
                break;
            case 2:
                ActionBarActivity bscTime = new searchInTime();
                new Bundle();
                finish();
                this.startActivity(new Intent(this, searchInTime.class));
                break;
            case 3:
                MapsActivity map = new MapsActivity();
                new Bundle();
                finish();
                this.startActivity(new Intent(this, MapsActivity.class));
                break;
        }
    }
}
