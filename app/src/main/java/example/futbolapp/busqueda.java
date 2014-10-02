package example.futbolapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


import example.futbolapp.database.MergeData;
import example.futbolapp.database.local.DB_Manager;

/**
 * Created by Usuario on 11/09/2014.
 */
public class busqueda extends ActionBarActivity {
    private Cursor cursor;
    private DB_Manager manager;
    private ListView lista;
    SimpleCursorAdapter adapter;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busqueda);
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
        new MergeData();


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


}
