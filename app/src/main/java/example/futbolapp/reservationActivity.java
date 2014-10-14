package example.futbolapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import example.futbolapp.database.external.DB_ManagerExt;
import example.futbolapp.database.local.DB_Manager;

/**
 * Created by Felipe on 09/10/2014.
 */
public class reservationActivity extends ActionBarActivity {
    private CharSequence mTitle;
    private DrawerLayout drawerLayout;
    private ListView navList;
    private ActionBarDrawerToggle drawerToggle;
    //
    private Button btnReservar ;
    private ListView listaHoras;
    private String selectedFromList;
    private int selectedItem;
    private String month, hour, day, year, fieldID;

    private String[] horas = {"7:00 am", "8:00 am", "9:00 am", "10:00 am", "11:00 am",
            "12:00 am", "1:00 pm", "2:00 pm", "3:00 pm", "4:00 pm","5:00 pm",
            "6:00 pm","7:00 pm", "8:00 pm", "9:00 pm", "10:00 pm"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.reserva);
        fieldID = "01";
        month = "-1";
        hour = "-1";
        day= "-1";
        year = "2014";

        if (extras != null) {
            fieldID = extras.getString("fieldID");
        }

        selectedFromList = "";
        selectedItem = -1;

        btnReservar = (Button)findViewById(R.id.btnReservar);
        listaHoras = (ListView)findViewById(R.id.list_horas);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, horas);
        listaHoras.setAdapter(adaptador);

        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Reserva con exito ", Toast.LENGTH_LONG).show();
                if(selectedItem != -1){
                    DB_ManagerExt dbManagerExt = new DB_ManagerExt();
                    boolean reservado = dbManagerExt.checkDisponibility(getApplicationContext(), fieldID, hour,  day,  month,  year);
                    Toast.makeText(getApplicationContext(), Boolean.toString(reservado), Toast.LENGTH_LONG).show();
                    if(reservado == true) {
                        Toast.makeText(getApplicationContext(), "Reserva con exito ", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Reserva con fallida ¡Hora no disponible!" + selectedFromList, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        mTitle = getTitle(); // Get current title
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layoutReserva);
        this.navList = (ListView) findViewById(R.id.left_drawerReserva);

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
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
        }
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
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