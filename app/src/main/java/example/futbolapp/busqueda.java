package example.futbolapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import example.futbolapp.database.DB_Manager;

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
        manager = new DB_Manager(this);
        cursor = manager.cargarCursorCanchas();
        lista = (ListView)findViewById(R.id.listView);
        String[] from = new String[]{manager.CN_Nombre,manager.CN_Informacion};
        int [] to = new int[]{android.R.id.text1,android.R.id.text2};
        //Text view two_line_list por defecto de android
        adapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,cursor, from, to,0);
        lista.setAdapter(adapter);
        //GridView gridView = (GridView) findViewById(R.id.grid_view);
        // Instance of ImageAdapter Class
        //gridView.setAdapter(new ImageAdapter(this));
    }
}
