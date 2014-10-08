package example.futbolapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
//Json libraries
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//SQLite
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
    private Cursor cursor;
    private DB_Manager manager;
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
        //Instantiate AQuery Object
        aq = new AQuery(this);
        fields = new ArrayList<String>();
        listView = (ListView)findViewById(R.id.listViewSearch);
        //autoComplete = (AutoCompleteTextView)findViewById(R.id.autocomplete);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(aq.getContext(), "Go to see field info and option to register", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(searchActivity.this, MapsActivity.class));
                    }
                }
        );
        searchView = (SearchView) findViewById(R.id.searchFieldNameView);
        searchView.setQueryHint("Ingresa el nombre");
        //Buscar Cancha por nombre al hacer click en boton buscar
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener(){

                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        //startActivity(new Intent(searchActivity.this, eventsActivity.class));
                        new BuscarTask().execute();
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {

                        return false;
                    }
                });

        //SQlite
        manager = new DB_Manager(this);
        getFields();
        //Text view two_line_list por defecto de android

        cursor = manager.cargarCursorCanchas();
        String[] from = new String[]{manager.CN_Nombre,manager.CN_Telefono};
        int [] to = new int[]{android.R.id.text1,android.R.id.text2};
        adapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,cursor, from, to,0);
        listView.setAdapter(adapter);

        }

    public void getFields(){
        //JSON URL
        String url = "http://solweb.co/reservas/api/field/fields";
        //Make Asynchronous call using AJAX method and show progress gif until get info
        aq.progress(R.id.progressBarSearch).ajax(url, JSONObject.class, this,"jsonCallback");
    }

    public void jsonCallback(String url, JSONObject json, AjaxStatus status) {
        //When JSON is not null
        Log.v("JSON", json.toString());
        if (json != null) {

            String jsonResponse = "";
            try {
                //Get json as Array
                JSONArray jsonArray = json.getJSONArray("field");
                DB_Manager manager = new DB_Manager(this);
                if (jsonArray != null) {
                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        //Get the name of the field from array index
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        fields.add(jsonObject.getString("name"));

                        //SQLite
                        //Creación datos en tabla cancha si no estan
                        //String nomb, String direccion, String tel,
                        //String latitud, String longitud,
                        // String icono, String info
                        //if(manager.buscarCancha(jsonObject.getString("name")){
                        //Add data that is not in the local data base
                        //Log.v("Esta la cancha ", Boolean.toString(manager.buscarIdCancha(jsonObject.getString("id"))) );
                        if(manager.buscarIdCancha(jsonObject.getString("id")) == false)
                        {
                            manager.insertarCancha(Integer.parseInt(jsonObject.getString("id")), jsonObject.getString("name"), jsonObject.getString("address"),
                                    jsonObject.getString("phone"), jsonObject.getString("latitude"), jsonObject.getString("length"),
                                    jsonObject.getString("icon"), jsonObject.getString("description"));
                            //Log.v("Agregando ", "Cancha");
                        }
                        //}


        //Insertar en la base de datos registro de usuario
        /*manager.registrarUsuario("Johan","ElCampeon10");
        manager.insertarCancha("El templo","Cra 43 123 313","21232123","6.18324",
                "-75.58665","","Canchas de futbol el templo del futbl");*/
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(aq.getContext(), "Error in parsing JSON", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(aq.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
            //ArrayList a = manager.fromCursorToArrayListString(manager.cargarCursorCanchas());
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line, a);
            //Log.v("numero de canhaS----- ", Integer.toString(a.size()));
            //listView.setAdapter(adapter);
            //autoComplete.setAdapter(new ArrayAdapter<String>(this,R.layout.list_details, a));
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
}
