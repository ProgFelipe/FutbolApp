package example.futbolapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
//Json libraries
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import example.futbolapp.database.MergeData;
import example.futbolapp.database.local.DB_Manager;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import java.util.ArrayList;

/**
 * Created by Usuario on 11/09/2014.
 */
public class busqueda extends ActionBarActivity {
    private Cursor cursor;
    private DB_Manager manager;
    private ListView lista;
    SimpleCursorAdapter adapter;
    //AQuery object
    AQuery aq;

    ListView listView;

    SearchView searchView ;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busqueda);
        //Instantiate AQuery Object
        aq = new AQuery(this);
        listView = (ListView)findViewById(R.id.listViewSearch);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(aq.getContext(), "Go to see field info and option to register", Toast.LENGTH_LONG).show();
                    }
                }
        );
        searchView = (SearchView) findViewById(R.id.searchView);
        String SearchedField = searchView.toString();
        //Update fields with JSON response
        getFields();

        //SQLite
        //Creación o retorno de la base de datoS
        /*DB_Manager manager = new DB_Manager(this);
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
                "6.256145"," -75.588220","","Canchas de futbol el Marte 2");*/
        //Get all fields from the web service
        //And put them in this layout
        //new MergeData();



       /* manager = new DB_Manager(this);
        cursor = manager.cargarCursorCanchas();
        lista = (ListView)findViewById(R.id.listView);
        String[] from = new String[]{manager.CN_Nombre,manager.CN_Informacion};
        int [] to = new int[]{android.R.id.text1,android.R.id.text2};
        //Text view two_line_list por defecto de android
        adapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,cursor, from, to,0);
        lista.setAdapter(adapter);*/
        //GridView gridView = (GridView) findViewById(R.id.grid_view);
        // Instance of ImageAdapter Class
        //gridView.setAdapter(new ImageAdapter(this));

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
            ArrayList<String> fields = new ArrayList<String>();
            String jsonResponse = "";
            try {
                //Get json as Array
                JSONArray jsonArray = json.getJSONArray("field");
                if (jsonArray != null) {
                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        //Get the name of the field from array index
                        fields.add(jsonArray.getJSONObject(i).getString("name"));
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(aq.getContext(), "Error in parsing JSON", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(aq.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line, fields);
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
        }
    }
}
