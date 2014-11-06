package example.futbolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import example.futbolapp.View.DrawerItemCustomAdapter;
import example.futbolapp.View.ObjectDrawerItem;
import example.futbolapp.database.local.DB_Manager;

/**
 * Created by Felipe on 08/10/2014.
 */
public class searchInTime extends Activity {
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList <String> idFields;
    AQuery aq;
    //
    private ProgressDialog pDialog;
    // URL to get fields by date JSON
    private static String url;
    //
    private static String hour;
    private static String minute;
    private static String day;
    private static String month;
    private static String year;
    private static TextView txtFecha;
    private static TextView txtHora;

    private static Button btnDate;
    private Button btnBuscar;
    private static Button btnHour;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busqueda_horario);
        //Get System Date
        Calendar c = Calendar.getInstance();
        minute = "";
        if(c.get(Calendar.DAY_OF_WEEK)+1 < 10){day =  "0"+Integer.toString(c.get(Calendar.DAY_OF_WEEK)+1);}else{
        day =  Integer.toString(c.get(Calendar.DAY_OF_WEEK)+1);}
        if(c.get(Calendar.MONTH) < 10){month = "0"+Integer.toString(c.get(Calendar.MONTH));}else{
        month = Integer.toString(c.get(Calendar.MONTH));}
        year = Integer.toString(c.get(Calendar.YEAR));

        //Instantiate AQuery Object
        aq = new AQuery(this);
        btnHour = (Button)findViewById(R.id.btnBsqHora);
        btnDate = (Button)findViewById(R.id.btnBsqFecha);
        btnBuscar = (Button)findViewById(R.id.btnBsqInTime);
        txtFecha = (TextView) findViewById(R.id.txtresultFecha);
        txtFecha.setText(day+"/"+month+"/"+year);
        txtHora = (TextView) findViewById(R.id.txtresultHora);
        txtFecha.setText(day + "/" + month + "/"  + year);
        // Font path
        String fontPath = "fonts/RobotoTTF/Roboto-LightItalic.ttf";
        String fontPath2 = "fonts/RobotoTTF/Roboto-LightItalic.ttf";
        String fontPath3 = "fonts/RobotoTTF/Roboto-Bold.ttf";
        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
        Typeface tf3 = Typeface.createFromAsset(getAssets(), fontPath3);
        // Applying font
        btnHour.setTypeface(tf);
        btnDate.setTypeface(tf2);
        btnBuscar.setTypeface(tf3);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1200-10-09-2014
                url = "http://solweb.co/reservas/api/reservations/fieldsavailability/" + hour + "-" + day + "-" + month + "-" + year;
                // Calling async task to get json
                new GetFields().execute();
           }
        });
        //Drawer
        mTitle = mDrawerTitle = getTitle();

        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[6];
        drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_ballsoccer, getResources().getString(R.string.nav));
        drawerItem[1] = new ObjectDrawerItem(android.R.drawable.ic_menu_today, getResources().getString(R.string.nav0));
        drawerItem[2] = new ObjectDrawerItem(android.R.drawable.ic_menu_search, getResources().getString(R.string.nav1));
        drawerItem[3] = new ObjectDrawerItem(android.R.drawable.ic_menu_search, getResources().getString(R.string.nav2));
        drawerItem[4] = new ObjectDrawerItem(android.R.drawable.ic_dialog_map, getResources().getString(R.string.nav3));
        drawerItem[5] = new ObjectDrawerItem(android.R.drawable.ic_lock_power_off, getResources().getString(R.string.nav4));

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

    }

    //Date Picker
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        }

        public void onDateSet(DatePicker view, int yr, int m, int d) {
            // Do something with the date chosen by the user
            //Toast.makeText(getActivity().getBaseContext() , "Seleccionó:  día "+Integer.toString(day)+" mes "+Integer.toString(month), Toast.LENGTH_LONG).show();
            if(m < 10){
                month = "0"+Integer.toString(m);
            }else{
                month = Integer.toString(m);
            }
            if(d < 10){
                day = "0"+Integer.toString(d);
            }else{
                day = Integer.toString(d);
            }
            year = Integer.toString(yr);
            btnDate.setBackground(getResources().getDrawable(R.drawable.btn_defaulton));
            txtFecha.setText(day + "/" + month + "/"  + year);
        }
    }

    //Time picker
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hr, int mn) {
            // Do something with the time chosen by the user
            //Toast.makeText(getActivity().getBaseContext() , "Seleccionó:  Hora "+Integer.toString(hour)+" minute "+Integer.toString(minute), Toast.LENGTH_LONG).show();
            if(hr < 10){
                hour = "0"+Integer.toString(hr);
            }else{
                hour = Integer.toString(hr);
            }
            if(mn < 10){
                minute = "0"+Integer.toString(mn);
            }else{
                minute = Integer.toString(mn);
            }
            btnHour.setBackground(getResources().getDrawable(R.drawable.btn_defaulton));
            txtHora.setText(hour+" : "+minute);
        }
    }

    //Drawer methods
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
                new mainActivity();
                new Bundle();
                this.startActivity(new Intent(this, mainActivity.class));
                finish();
                break;
            case 1:
                new eventsActivity();
                new Bundle();
                this.startActivity(new Intent(this, eventsActivity.class));
                finish();
                break;
            case 2:
                new searchActivity();
                new Bundle();
                this.startActivity(new Intent(this, searchActivity.class));
                finish();
                break;
            case 3:
                new searchInTime();
                new Bundle();
                this.startActivity(new Intent(this, searchInTime.class));
                finish();
                break;
            case 4:
                new MapsActivity();
                new Bundle();
                this.startActivity(new Intent(this, MapsActivity.class));
                finish();
                break;
            case 5:
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("Saliendo CanchaFinder")
                        .setMessage("¿Esta seguro que desea cerrar sessión?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedpreferences = getSharedPreferences
                                        (LoginApp.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.clear();
                                editor.commit();
                                startActivity(new Intent(getApplicationContext(), LoginApp.class));
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
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
            //Log.e("MainActivity", "Error in creating fragment");
        }

    }

    //Searching ...
    /**
     * Async task class to get json by making HTTP call
     * */
            private class GetFields extends AsyncTask<Void, Void, Void> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    // Showing progress dialog
                    pDialog = new ProgressDialog(searchInTime.this);
                    pDialog.setMessage("Por favor espere...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                }

                @Override
                protected Void doInBackground(Void... arg0) {
                    getDisponibility();
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                    // Dismiss the progress dialog
                    if (pDialog.isShowing())
                        pDialog.dismiss();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void getDisponibility(){
        //JSON URL
        //Make Asynchronous call using AJAX method and show progress gif until get info
        aq.progress(R.id.progressBarEvents).ajax(url, JSONObject.class, this,"jsonCallback");
    }

    public void jsonCallback(String url, JSONObject json, AjaxStatus status) {
        idFields = new ArrayList();
        //When JSON is not null
        if (json != null) {
            //Log.v("JSON", json.toString());
            String jsonResponse = "";
            try {
                //Get json as Array
                JSONArray jsonArray = json.getJSONArray("reservation");
                //Toast.makeText(aq.getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                if (jsonArray != null) {
                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        //Get the events
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        idFields.add(jsonObject.getString("id"));
                    }
                }
                showPopup(searchInTime.this);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(aq.getContext(), "Error in parsing JSON", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(aq.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
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
                Toast.makeText(aq.getContext(),"Verifique su conexion",Toast.LENGTH_SHORT).show();
            }
        }
    }


    // The method that displays the popup.
    //References: http://androidresearch.wordpress.com/2012/05/06/how-to-create-popups-in-android/
    private void showPopup(final Activity context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(searchInTime.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.popup, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Disponibles");

        ListView lv = (ListView) convertView.findViewById(R.id.listViewPopup);
        final DB_Manager manager = new DB_Manager(this);
        String[] from = new String[]{manager.CN_Nombre,manager.CN_Telefono};
        int [] to = new int[]{android.R.id.text1,android.R.id.text2};
        ArrayList  <Cursor> cursores  = new ArrayList();
        Cursor cursor;
        /*
        Busca la cancha en la base de datos local el id de la cancha y agrega el cursor encontrado en un arreglo
         */
        for(int c = 0; c < idFields.size(); c++) {
            cursor = manager.buscarCanchaById(idFields.get(c));
            cursores.add(cursor);
        }
        Cursor [] bar = cursores.toArray(new Cursor[cursores.size()]);
        //el arreglo
        if(bar.length > 0) {
            Cursor extendedCursor = new MergeCursor(bar);
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, extendedCursor, from, to, 0);
            lv.setAdapter(adapter);

            //Cambia de vista al hacer click en un item de la lista de canchas
            lv.setOnItemClickListener(
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
        }
        alertDialog.show();
    }
}
