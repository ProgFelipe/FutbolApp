package example.futbolapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;
//Json libraries
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//SQLite
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

        getFields();
        }

    public void getFields(){
        //SQlite
        manager = new DB_Manager(this);
        //getFields();
        //Busca cambios en la base de datos local con respecto a la externa
        MergeData merge = new MergeData(this);
        merge.getFields();


        //Text view two_line_list por defecto de android
        cursor = manager.cargarCursorCanchas();
        String[] from = new String[]{manager.CN_Nombre,manager.CN_Telefono};
        int [] to = new int[]{android.R.id.text1,android.R.id.text2};
        adapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,cursor, from, to,0);
        listView.setAdapter(adapter);
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
            getFields();
        }else{
            finish();
        }
    }
}
