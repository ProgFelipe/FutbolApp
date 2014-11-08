package example.futbolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import example.futbolapp.View.DrawerItemCustomAdapter;
import example.futbolapp.View.ObjectDrawerItem;

/**
 * Created by Felipegi on 11/09/2014.
 */
public class mainActivity  extends Activity implements BaseSliderView.OnSliderClickListener{
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private int sliderEffect;
    private TextView newstext;
    private ListView listView;
    //Slider
    private SliderLayout newsSlider;
    //AQuery object
    AQuery aq;
    private String idUser;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        listView = (ListView)findViewById(R.id.listViewMatches);
        newstext = (TextView)findViewById(R.id.textViewMain);
        newstext.setMovementMethod(new ScrollingMovementMethod());
        sliderEffect = 0;
        //Instantiate AQuery Object
        aq = new AQuery(this);
        SharedPreferences sharedpreferences = getSharedPreferences
                (LoginApp.MyPREFERENCES, Context.MODE_PRIVATE);
        idUser =  LoginApp.idUsuario;
        idUser = sharedpreferences.getString(idUser, "");

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

        //Slider
        newsSlider = (SliderLayout)findViewById(R.id.slider);
        /*HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");*/

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("NEWS 1",R.drawable.field1);
        file_maps.put("NEWS 2",R.drawable.field2);
        file_maps.put("NEW 3",R.drawable.field3);
        file_maps.put("NEWS 4", R.drawable.field4);
        file_maps.put("NEWS 5", R.drawable.field6);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.getBundle()
                    .putString("extra",name);

            newsSlider.addSlider(textSliderView);
        }
        newsSlider.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        newsSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        newsSlider.setCustomAnimation(new DescriptionAnimation());
        newsSlider.setDuration(4000);
        //
        getData("http://solweb.co/reservas/api/promotion/promotions", "promotions");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher)
                .setTitle("Saliendo CanchaFinder")
                .setMessage("¿Esta seguro que desea salir?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    //Navigation Drawer Methods
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_matchs:
                getData("http://www.football-data.org/fixtures","fixtures");
                listView.setVisibility(View.VISIBLE);
                newstext.setVisibility(View.INVISIBLE);
                return true;
            case R.id.action_aboutus:
                getData("http://solweb.co/reservas/api/promotion/promotions","promotions");
                newstext.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    @Override
    public void onSliderClick(BaseSliderView slider) {
        switch(sliderEffect){
            case 0:
                newsSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                sliderEffect++;
                break;
            case 1:
                newsSlider.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
                sliderEffect++;
                break;
            case 2:
                newsSlider.setPresetTransformer(SliderLayout.Transformer.CubeIn);
                sliderEffect++;
                break;
            case 3:
                newsSlider.setPresetTransformer(SliderLayout.Transformer.DepthPage);
                sliderEffect++;
                break;
            case 4:
                newsSlider.setPresetTransformer(SliderLayout.Transformer.Fade);
                sliderEffect++;
                break;
            case 5:
                newsSlider.setPresetTransformer(SliderLayout.Transformer.FlipHorizontal);
                sliderEffect++;
                break;
            case 6:
                newsSlider.setPresetTransformer(SliderLayout.Transformer.FlipPage);
                sliderEffect++;
                break;
            case 7:
                newsSlider.setPresetTransformer(SliderLayout.Transformer.Foreground2Background);
                sliderEffect++;
                break;
            case 8:
                newsSlider.setPresetTransformer(SliderLayout.Transformer.RotateUp);
                sliderEffect = 0;
                break;
            default:
                newsSlider.setPresetTransformer(SliderLayout.Transformer.Default);


        }
         /*//Other Effects
        newsSlider.setPresetTransformer(SliderLayout.Transformer.RotateDown);
        newsSlider.setPresetTransformer(SliderLayout.Transformer.Stack);
        newsSlider.setPresetTransformer(SliderLayout.Transformer.Tablet);
        newsSlider.setPresetTransformer(SliderLayout.Transformer.ZoomIn);
        newsSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
        newsSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);*/
        Toast.makeText(this,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(mainActivity.this, mainActivity.class);
                // set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
            case 1:
                new eventsActivity();
                new Bundle();
                this.startActivity(new Intent(this, eventsActivity.class));
                break;
            case 2:
                new searchActivity();
                new Bundle();
                this.startActivity(new Intent(this, searchActivity.class));
                break;
            case 3:
                new searchInTime();
                new Bundle();
                this.startActivity(new Intent(this, searchInTime.class));
                break;
            case 4:
                new MapsActivity();
                new Bundle();
                this.startActivity(new Intent(this, MapsActivity.class));
                break;
            case 5:
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("Saliendo CanchaFinder")
                        .setMessage("¿Esta seguro que desea cerrar sessión?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedpreferences = getSharedPreferences
                                        (LoginApp.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.clear();
                                editor.commit();
                                Intent i = new Intent(mainActivity.this, LoginApp.class);
                                // set the new task and clear flags
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                               /* Session session = Session.getActiveSession();
                                if (session != null) {
                                    session.closeAndClearTokenInformation();
                                }*/
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

    public void getData(String url, String type){
        //JSON URL
        //Make Asynchronous call using AJAX method and show progress gif until get info
        if(type.equals("promotions")){aq.progress(R.id.progressBarSearch).ajax(url, JSONObject.class, this,"jsonCallbackPromotions");}
        if(type.equals("fixtures")){ aq.progress(R.id.progressBarSearch).ajax(url, JSONArray.class, this,"jsonCallbackFixtures");}
    }

    public void jsonCallbackPromotions(String url, JSONObject json, AjaxStatus status) {
        //When JSON is not null
        String promociones = "";
        if (json != null) {
            //Log.v("JSON", json.toString());
            String jsonResponse = "";
            try {
                //Get json as Array
                JSONArray jsonArray = json.getJSONArray("promotion");
                if (jsonArray != null) {
                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        //Get the name of the field from array index
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                            promociones += jsonObject.getString("texto");
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(aq.getContext(), "Error in parsing JSON", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(aq.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
            newstext.setGravity(Gravity.CENTER);
            newstext.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
            newstext.setText(promociones);
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
    public void jsonCallbackFixtures(String url, JSONArray json, AjaxStatus status) {
        //When JSON is not null
        String promociones = "PARTIDOS HOY\n";
        ArrayList<String> matches = new ArrayList<String>();
        if (json != null) {
            try {
                //Get json as Array
                if (json != null) {
                    Calendar c = Calendar.getInstance();
                    String dia= "";
                    matches.add("*   HOY");
                    if(c.get(Calendar.DAY_OF_WEEK)+1 < 10){dia =  "0"+Integer.toString(c.get(Calendar.DAY_OF_WEEK)+1);}else{
                        dia =  Integer.toString(c.get(Calendar.DAY_OF_WEEK)+1);}
                    int len = json.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject jsonObject = json.getJSONObject(i);
                        String date = jsonObject.getString("date");
                        String month = date.substring(5, 7);
                        String time = date.substring(11, 19);
                        String day = date.substring(8, 10);
                        if(day.equals(dia)){
                            matches.add(jsonObject.getString("homeTeam")+ " VS " + jsonObject.getString("awayTeam"));
                        }
                    }
                    if(c.get(Calendar.DAY_OF_WEEK)+1 < 10){dia =  "0"+Integer.toString(c.get(Calendar.DAY_OF_WEEK)+2);}else{
                        dia =  Integer.toString(c.get(Calendar.DAY_OF_WEEK)+2);}
                    matches.add("*  MAÑANA");
                    for (int i = 0; i < len; i++) {
                        JSONObject jsonObject = json.getJSONObject(i);
                        String date = jsonObject.getString("date");
                        String month = date.substring(5, 7);
                        String time = date.substring(11, 19);
                        String day = date.substring(8, 10);
                        if(day.equals(dia)){
                            matches.add(jsonObject.getString("homeTeam")+ " VS " + jsonObject.getString("awayTeam"));
                        }
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(aq.getContext(), "Error in parsing JSON", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(aq.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

            //On List view improvements
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, matches);
            listView.setAdapter(adapter);
            /*
            newstext.setTypeface(null, Typeface.BOLD);
            newstext.setGravity(Gravity.LEFT);
            newstext.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
            newstext.setText(promociones);*/
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
}