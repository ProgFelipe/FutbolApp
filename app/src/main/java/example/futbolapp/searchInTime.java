package example.futbolapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

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

/**
 * Created by Felipe on 08/10/2014.
 */
public class searchInTime extends ActionBarActivity {
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    //
    private ProgressDialog pDialog;
    // URL to get fields by date JSON
    private static String url = "http://solweb.co/reservas/api/reservations/fieldsavailability";
    //
    private static int hour;
    private static int minute;
    private static int day;
    private static int month;
    private static int year;
    // soccer fields JSONArray
    JSONArray fields = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busqueda_horario);
        //Get System Date
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        day = c.get(Calendar.DAY_OF_WEEK);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        //
        Button btnHour = (Button)findViewById(R.id.btnBsqHora);
        Button btnDate = (Button)findViewById(R.id.btnBsqFecha);
        Button btnBuscar = (Button)findViewById(R.id.btnBsqInTime);
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
                // Calling async task to get json
                new GetFields().execute();
                /*Toast.makeText(getBaseContext() , "Buscando: "+Integer.toString(hour)+":"+Integer.toString(minute)+", "+Integer.toString(day)+"/"+
                        "/"+Integer.toString(month)+"/"+Integer.toString(year), Toast.LENGTH_LONG).show();*/
            }
        });
        //Drawer
        mTitle = mDrawerTitle = getTitle();

        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[4];

        drawerItem[0] = new ObjectDrawerItem(android.R.drawable.ic_menu_today, getResources().getString(R.string.nav0));
        drawerItem[1] = new ObjectDrawerItem(android.R.drawable.ic_menu_search, getResources().getString(R.string.nav1));
        drawerItem[2] = new ObjectDrawerItem(android.R.drawable.ic_menu_search, getResources().getString(R.string.nav2));
        drawerItem[3] = new ObjectDrawerItem(android.R.drawable.ic_dialog_map, getResources().getString(R.string.nav3));

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
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Toast.makeText(getActivity().getBaseContext() , "Seleccionó:  día "+Integer.toString(day)+" mes "+Integer.toString(month), Toast.LENGTH_LONG).show();
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
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hour, int minute) {
            // Do something with the time chosen by the user
            Toast.makeText(getActivity().getBaseContext() , "Seleccionó:  Hora "+Integer.toString(hour)+" minute "+Integer.toString(minute), Toast.LENGTH_LONG).show();
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
                new eventsActivity();
                new Bundle();
                this.startActivity(new Intent(this, eventsActivity.class));
                finish();
                break;
            case 1:
                new searchActivity();
                new Bundle();
                this.startActivity(new Intent(this, searchActivity.class));
                finish();
                break;
            case 2:
                new searchInTime();
                new Bundle();
                this.startActivity(new Intent(this, searchInTime.class));
                finish();
                break;
            case 3:
                new MapsActivity();
                new Bundle();
                this.startActivity(new Intent(this, MapsActivity.class));
                finish();
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
            Log.e("MainActivity", "Error in creating fragment");
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
            // Creating service handler class instance
            ServicesHandler sh = new ServicesHandler();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("hour", Integer.toString(hour)));
            params.add(new BasicNameValuePair("day", Integer.toString(day)));
            params.add(new BasicNameValuePair("month", Integer.toString(month)));
            params.add(new BasicNameValuePair("year", Integer.toString(year)));
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServicesHandler.GET, params);

            Log.v("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    fields = jsonObj.getJSONArray("reservation");
                    Toast.makeText(getBaseContext(), fields.toString(), Toast.LENGTH_LONG).show();
                    // looping through All Contacts
                    for (int i = 0; i < fields.length(); i++) {
                        /*JSONObject c = fields.getJSONObject(i);

                        Integer.parseInt(jsonObject.getString("id")), jsonObject.getString("name"), jsonObject.getString("address"),
                                jsonObject.getString("phone"), jsonObject.getString("latitude"), jsonObject.getString("length"),
                                jsonObject.getString("icon"), jsonObject.getString("description"));
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String email = c.getString(TAG_EMAIL);
                        String address = c.getString(TAG_ADDRESS);
                        String gender = c.getString(TAG_GENDER);

                        // Phone node is JSON Object
                        JSONObject phone = c.getJSONObject(TAG_PHONE);
                        String mobile = phone.getString(TAG_PHONE_MOBILE);
                        String home = phone.getString(TAG_PHONE_HOME);
                        String office = phone.getString(TAG_PHONE_OFFICE);

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_ID, id);
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_EMAIL, email);
                        contact.put(TAG_PHONE_MOBILE, mobile);

                        // adding contact to contact list
                        contactList.add(contact);*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            /*ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, contactList,
                    R.layout.list_item, new String[] { TAG_NAME, TAG_EMAIL,
                    TAG_PHONE_MOBILE }, new int[] { R.id.name,
                    R.id.email, R.id.mobile });

            setListAdapter(adapter);*/
        }

    }

}
