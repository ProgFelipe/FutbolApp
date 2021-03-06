package example.futbolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import example.futbolapp.View.DrawerItemCustomAdapter;
import example.futbolapp.View.Item;
import example.futbolapp.View.MyAdapter;
import example.futbolapp.View.ObjectDrawerItem;
import example.futbolapp.database.local.DB_Manager;

/**
 * Created by FelipeGI on 11/09/2014.
 */
public class eventsActivity extends Activity {
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    public static ListView listView;
    private DB_Manager manager;
    public static ArrayList idReservas;
    private Button btnCancelar;
    private String Seleccion;
    private Session currentSession;
    private String idUser;
    public static String cancelarId;
    private MyAdapter adapter2;

    public static ArrayList<Item> items;
    private Session.StatusCallback sessionStatusCallback;

    //AQuery object
    AQuery aq;
    //


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventos);
        cancelarId = "";
        currentSession = null;
        Bundle extras = getIntent().getExtras();

        // create instace for sessionStatusCallback
        sessionStatusCallback = new Session.StatusCallback() {

            @Override
            public void call(Session session, SessionState state, Exception exception) {
                onSessionStateChange(session, state, exception);

            }
        };

        //getTheIdUser
        SharedPreferences sharedpreferences = getSharedPreferences
                (LoginApp.MyPREFERENCES, Context.MODE_PRIVATE);
        idUser =  LoginApp.idUsuario;
        idUser = sharedpreferences.getString(idUser, "");
        manager = new DB_Manager(this);
        btnCancelar = (Button) findViewById(R.id.btnCancelarReserva);
        listView = (ListView)findViewById(R.id.listViewReservado);
        //Instantiate AQuery Object
        aq = new AQuery(this);
        mTitle = mDrawerTitle = getTitle();

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cancelarId.length() > 0){
                new AlertDialog.Builder(v.getContext())
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("¿Desea cancelar?")
                        .setMessage(Seleccion)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    cancelarReserva(cancelarId);
                                    getReservationByUser(idUser);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }else{Toast.makeText(v.getContext(),"Seleccione reserva a cancelar",Toast.LENGTH_LONG).show();}
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSelectedItem(position);
            }
        });

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

        getReservationByUser(idUser);
    }


    /**
     * this method is used by the facebook API
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (currentSession != null) {
            currentSession.onActivityResult(this, requestCode, resultCode, data);
        }
    }
    /**
     * Connects the user to facebook
     */
    public void connectToFB() {
        List<String> permissions = new ArrayList<String>();
        permissions.add("publish_stream");

        currentSession = new Session.Builder(this).build();
        currentSession.addCallback(sessionStatusCallback);

        Session.OpenRequest openRequest = new Session.OpenRequest(this);
        openRequest.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
        openRequest.setRequestCode(Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE);
        openRequest.setPermissions(permissions);
        currentSession.openForPublish(openRequest);
    }
    /**
     * manages the session state change. This method is called after the
     * <code>connectToFB()</code> method.
     *
     * @param session
     * @param state
     * @param exception
     */
    private void onSessionStateChange(Session session, SessionState state,
                                      Exception exception) {
        if (session != currentSession) {
            return;
        }

        if (state.isOpened()) {
            // Log in just happened.
            Toast.makeText(getApplicationContext(), "session opened",
                    Toast.LENGTH_SHORT).show();
        } else if (state.isClosed()) {
            // Log out just happened. Update the UI.
            Toast.makeText(getApplicationContext(), "session closed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void setSelectedItem(int position){
        Seleccion = items.get(position).getTitle()+" "+items.get(position).getDescription();
        cancelarId = idReservas.get(position).toString();
    }

    public Session getSession(){
        return currentSession;
    }
    /**
     * Publishes story on the logged user's wall
     */
    public void publishEvents() {
        Bundle params = new Bundle();
        params.putString("name", "Reserva Cancha Finder");
        params.putString("caption", "CanchaFinder");
        params.putString("description", Seleccion);
        params.putString("link", "http://www.solweb.co/reservas/CanchaFinder/index.php");
        params.putString("picture","http://www.solweb.co/reservas/api_movil/ic_launcher.png");

        WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(this,currentSession, params))
                .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,FacebookException error) {
                        if (error == null) {
                            // When the story is posted, echo the success
                            // and the post Id.
                            final String postId = values.getString("post_id");
                            if (postId != null) {
                                // do some stuff
                            } else {
                                // User clicked the Cancel button
                                Toast.makeText(getApplicationContext(),
                                        "Publish cancelled", Toast.LENGTH_SHORT).show();
                            }
                        } else if (error instanceof FacebookOperationCanceledException) {
                            // User clicked the "x" button
                            Toast.makeText(getApplicationContext(),
                                    "Publish cancelled", Toast.LENGTH_SHORT).show();
                        } else {
                            // Generic, ex: network error
                            Toast.makeText(getApplicationContext(),
                                    "Error posting story", Toast.LENGTH_SHORT).show();
                        }
                    }

                }).setFrom("").build();
        feedDialog.show();

    }
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
                                Intent i = new Intent(eventsActivity.this, LoginApp.class);
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


    public void getReservationByUser(String idUser){
        //JSON URL
        String url = "http://solweb.co/reservas/api/reservations/byuser/"+idUser;
        //Make Asynchronous call using AJAX method and show progress gif until get info
        aq.progress(R.id.progressBarEvents).ajax(url, JSONObject.class, this,"jsonCallback");
    }

    public void jsonCallback(String url, JSONObject json, AjaxStatus status) {
        //When JSON is not null
        ArrayList reservas = new ArrayList();
        idReservas = new ArrayList();
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
                        //Example
//{"reservation": [{"id":"46","name":"carlos","hour":"1900","day":"20","month":"05","year":"2014",
// "description":"","idField":"41","idLogin":"5"}]}
//Elements to calendar view
                        Cursor c = manager.buscarCanchaById(jsonObject.getString("idField"));
                        String nombreCancha = "";
                        if (c.moveToFirst()){ // data?
                            nombreCancha = c.getString(c.getColumnIndex("name"));
                        }
                        reservas.add(i+1+"."+ nombreCancha);
                        reservas.add("hora: "+jsonObject.getString("hour") + "- Día: " + jsonObject.getString("day") + "/" + jsonObject.getString("month") + "/" +
                                        jsonObject.getString("year"));
                        idReservas.add(jsonObject.getString("id"));
                //jsonObject.getString("description");
                //jsonObject.getString("idField");
                //jsonObject.getString("idLogin");
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(aq.getContext(), "Error in parsing JSON", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(aq.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
            // 1. pass context and data to the custom adapter
            items = generateData(reservas);
            adapter2 = new MyAdapter(this, items);

            // 3. setListAdapter
            listView.setAdapter(adapter2);

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

    public void shareOnFacebook(View view){
        //view.setVisibility(View.GONE);
        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);
        setSelectedItem(position);

        currentSession = getSession();
        //setSelectedItem(listView.getSelectedItemPosition());
        if(currentSession != null && currentSession.isOpened()) {
            publishEvents();
        }else{
            connectToFB();
        }
    }
    public void shareWhereYouWant(View view){
        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);
        setSelectedItem(position);
        if(Seleccion != null){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Cancha Finder,"+Seleccion);
        startActivity(Intent.createChooser(shareIntent, "Compartiendo Reserva"));}else{
            Toast.makeText(getApplicationContext(),"Seleccione un item de la lista", Toast.LENGTH_SHORT).show();
        }
    }
    public void cancelarReserva(String idReserva){
        //JSON URL
        String url = "http://solweb.co/reservas/api/reservations/delete/"+idReserva;
        //Make Asynchronous call using AJAX method and show progress gif until get info
        aq.progress(R.id.progressBarSearch).ajax(url, String.class, this,"jsonCancel");
    }

    public void jsonCancel(String url, String string, AjaxStatus status) {
        if (string != null) {
            try {
                //Get json as Array
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Toast.makeText(aq.getContext(), "Error in parsing JSON", Toast.LENGTH_LONG).show();
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
    private ArrayList<Item> generateData(ArrayList <String> a){
        ArrayList<Item> items = new ArrayList<Item>();
        //items.add(new Item(a,b));
        for(int i = 0; i < a.size(); i += 2){
            items.add(new Item(a.get(i), a.get(i+1)));
        }
        return items;
    }
}
