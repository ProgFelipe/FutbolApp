package example.futbolapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import example.futbolapp.database.local.DB_Manager;

/**
 * Created by Felipe on 09/10/2014.
 */
public class reservationActivity extends ActionBarActivity {
    private DB_Manager manager;
    private Button btnReservar;
    private String selectedFromList;
    private int selectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserva);
        selectedFromList = "";
        selectedItem = -1;
        Bundle extras = getIntent().getExtras();
        btnReservar = (Button)findViewById(R.id.btnReservar);
        String canchaID = "CanchaN";
        if (extras != null) {
            canchaID = extras.getString("fieldID");
        }

        final ListView lv = (ListView) findViewById(R.id.list_horas);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                selectedFromList =(String) (lv.getItemAtPosition(myItemInt));
                selectedItem = myItemInt;

            }
        });
        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Reserva con exito ", Toast.LENGTH_LONG).show();
                if(selectedItem != -1){
                    Toast.makeText(getApplicationContext(), "Reserva con exito "+selectedFromList, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}