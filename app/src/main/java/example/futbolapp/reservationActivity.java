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
import android.support.v4.app.FragmentActivity;
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
public class reservationActivity extends FragmentActivity {
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
                    boolean reservado = dbManagerExt.checkFieldDisponibility( fieldID, hour,  day,  month,  year);
                    if(reservado) {
                        Toast.makeText(getApplicationContext(), "Reserva con exito ", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Reserva con fallida ¡Hora no disponible!" + selectedFromList, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

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
}