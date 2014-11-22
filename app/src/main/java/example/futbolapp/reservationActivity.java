package example.futbolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import example.futbolapp.View.DrawerItemCustomAdapter;
import example.futbolapp.View.ObjectDrawerItem;
import example.futbolapp.database.external.ServicesHandler;
import example.futbolapp.database.local.DB_Manager;

/**
 * Created by Felipe on 09/10/2014.
 */
public class reservationActivity extends Activity {
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private CheckBox chkToday;
    private static String idUser;
    private static String userName;
    private ArrayList idFields;
    private static Boolean canReserv;
    private ProgressBar progressBar;
    AQuery aq;
    //
    private ProgressDialog pDialog;
    // URL to get fields by date JSON
    private static String url = "http://solweb.co/reservas/api/reservations/availability";
    JSONArray fields = null;
    //
    private Button btnReservar, btnFecha ;
    private ListView listaHoras;
    private static boolean selectedHour, selectedDate;
    private static String hour,idField;
    private static int month,  day, year;
    private String[] horas = {"07  -  7:00 am", "08  -  8:00 am", "09  -  9:00 am", "10  -  10:00 am", "11  -  11:00 am",
            "12  -  12:00 am", "13  -  1:00 pm", "14  -  2:00 pm", "15  -  3:00 pm", "16 -  4:00 pm","17  -  5:00 pm",
            "18  -  6:00 pm","19  -  7:00 pm", "20  -  8:00 pm", "21  -  9:00 pm", "22  -  10:00 pm"};
    private NotificationManager nm;
    private static final int ID_NOTIFICACION_CREAR = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.reserva);
        idField = "";
        month = 0;
        day= 0;
        year = 0;
        hour = "";
        canReserv = false;
        selectedHour = false;
        selectedDate = false;
        //Instantiate AQuery Object
        aq = new AQuery(this);
        if (extras != null) {
            idField = extras.getString("idField");
        }
        progressBar = (ProgressBar) findViewById(R.id.progressBarReserva);
        btnFecha = (Button)findViewById(R.id.btnFechaReserva);
        btnReservar = (Button)findViewById(R.id.btnReservar);
        listaHoras = (ListView)findViewById(R.id.list_horas);
        chkToday = (CheckBox)findViewById(R.id.chkToday);

        // Font path
        String fontPath0 = "fonts/antipasto/Antipasto_regular.otf";
        String fontPath1 = "fonts/RobotoTTF/Roboto-Medium.ttf";
        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath0);
        Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath1);
        // Applying font
        btnFecha.setTypeface(tf);
        chkToday.setTypeface(tf2);

        nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, horas);
        listaHoras.setAdapter(adaptador);
        listaHoras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hour = horas[i].substring(0,2).trim();
                selectedHour = true;
                Toast.makeText(getApplicationContext(), "Hora seleccionada "+hour, Toast.LENGTH_LONG).show();
            }
        });
        chkToday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selectedDate = true;
                    Calendar c = Calendar.getInstance();
                    day = c.get(Calendar.DAY_OF_MONTH);
                    month = c.get(Calendar.MONTH)+1;
                    year = c.get(Calendar.YEAR);
                }
            }
        });
        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedHour == true && selectedDate == true){
                    //createNotification();
                    //1200-10-09-2014
                    if(month < 10 && day < 10) {
                        url = "http://solweb.co/reservas/api/reservations/fieldsavailability/" + hour + "-0" + day + "-0" + month + "-" + year;
                    }else{
                        url = "http://solweb.co/reservas/api/reservations/fieldsavailability/" + hour + "-" + day + "-" + month + "-" + year;
                        if(month < 10){
                            url = "http://solweb.co/reservas/api/reservations/fieldsavailability/" + hour + "-" + day + "-0" + month + "-" + year;
                        }
                        if(day < 10){
                            url = "http://solweb.co/reservas/api/reservations/fieldsavailability/" + hour + "-0" + day + "-" + month + "-" + year;
                        }
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    getDisponibility();
                }else{
                    if(selectedHour == false && selectedDate == false){
                        Toast.makeText(getApplicationContext(), "Ingrese Hora y Fecha", Toast.LENGTH_LONG).show();
                    }else{
                        if(selectedDate == false){
                            Toast.makeText(getApplicationContext(), "Ingrese Fecha", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Ingrese Hora", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
        //Nav Drawer
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

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int Year, int Month, int Day) {
            // Do something with the date chosen by the user
            selectedDate = true;
            month = Month+1;
            day = Day+1;
            year = Year;

        }
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
    //Navigation Drawer Methods
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
                                Intent i = new Intent(reservationActivity.this, LoginApp.class);
                                // set the new task and clear flags
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
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
    public void createNotification(){
        DB_Manager manager = new DB_Manager(this);
        Cursor cursor = manager.buscarCanchaById(idField);
        String nombreCancha = "";
        if (cursor.moveToFirst()){ // data?
            nombreCancha = cursor.getString(cursor.getColumnIndex("name"));
        }
        cursor.close();
        Notification notificacion = new Notification(
                R.drawable.ic_notification,
                "Reserva con éxito",
                System.currentTimeMillis() );
        PendingIntent intencionPendiente = PendingIntent.getActivity(
                this, 0, new Intent(this, reservationActivity.class), 0);
        notificacion.setLatestEventInfo(this, "Reserva con éxito",
                "En "+nombreCancha+" a las "+hour+":00 , del "+day+"/"+month+"/"+year, intencionPendiente);
        nm.notify(ID_NOTIFICACION_CREAR, notificacion);
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class SetReservation extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(reservationActivity.this);
            pDialog.setMessage("Por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServicesHandler sh = new ServicesHandler();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("idField", idField));
            params.add(new BasicNameValuePair("hour", hour));
            params.add(new BasicNameValuePair("day", Integer.toString(day).trim()));
            params.add(new BasicNameValuePair("month", Integer.toString(month).trim()));
            params.add(new BasicNameValuePair("year", Integer.toString(year).trim()));
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServicesHandler.GET, params);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    fields = jsonObj.getJSONArray("reservation");
                    Toast.makeText(getBaseContext(), fields.toString(), Toast.LENGTH_LONG).show();
                    // looping through All Contacts
                    for (int i = 0; i < fields.length(); i++) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            createNotification();
            Toast.makeText(getApplicationContext(), "Reserva con exito ", Toast.LENGTH_LONG).show();
        }
    }
    public void reservaPorUrl(){
        SharedPreferences sharedpreferences = getSharedPreferences
                (LoginApp.MyPREFERENCES, Context.MODE_PRIVATE);
        idUser =  LoginApp.idUsuario;
        idUser = sharedpreferences.getString(idUser, "");
        userName = LoginApp.name;
        userName = sharedpreferences.getString(userName, "");
        String url2 = "";
        if(month < 10 && day < 10) {
            url2 = "http://solweb.co/reservas/api/reservations/add/"+userName+"-"+hour+"-0"+day+"-0"+month+"-"+year+"-"+"descripcion"+"-"+idField+"-"+idUser;
        }else{
            url2 = "http://solweb.co/reservas/api/reservations/add/"+userName+"-"+hour+"-"+day+"-"+month+"-"+year+"-"+"descripcion"+"-"+idField+"-"+idUser;
            if(month < 10){
                url2 = "http://solweb.co/reservas/api/reservations/add/"+userName+"-"+hour+"-"+day+"-0"+month+"-"+year+"-"+"descripcion"+"-"+idField+"-"+idUser;
            }
            if(day < 10){
                url2 = "http://solweb.co/reservas/api/reservations/add/"+userName+"-"+hour+"-0"+day+"-"+month+"-"+year+"-"+"descripcion"+"-"+idField+"-"+idUser;
            }
        }
        aq.progress(R.id.progressBarReserva).ajax(url2, String.class, this,"reservated");
    }
    public void reservated(String url, String result, AjaxStatus status) {
        //When JSON is not null
        if (result != null) {
            //Log.v("JSON", json.toString());
            String jsonResponse = "";
            if(result.equals("Exitoso")){
                createNotification();
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
                Toast.makeText(aq.getContext(),"Verifique su conexión",Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void getDisponibility(){
        //JSON URL
        //Make Asynchronous call using AJAX method and show progress gif until get info
        aq.progress(R.id.progressBarReserva).ajax(url, JSONObject.class, this,"jsonCallback");
    }

    public void jsonCallback(String url, JSONObject json, AjaxStatus status) {
        //When JSON is not null
        idFields = new ArrayList();
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
                //Log.d("NUMERO DE IDS ENCONTRADAS ", Integer.toString(idFields.size()));
                int fieldpresent = 0;
                for(int count = 0 ; count <idFields.size(); count++){
                    if(idField.equals(idFields.get(count))){
                        fieldpresent++;
                    }
                }
                if(fieldpresent > 0){
                    canReserv = true;
                }
                idFields.clear();
                if(canReserv){
                    canReserv = false;
                    reservaPorUrl();}else{
                    Toast.makeText(getApplicationContext(), "Cancha ocupada a las "+hour+":00 del "+day+"/"+month+"/"+year, Toast.LENGTH_LONG).show();
                }

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
        progressBar.setVisibility(View.GONE);
    }

}