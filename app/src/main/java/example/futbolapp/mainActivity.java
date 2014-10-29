package example.futbolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

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
    //Slider
    private SliderLayout newsSlider;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        TextView newstext = (TextView)findViewById(R.id.textViewMain);
        newstext.setMovementMethod(new ScrollingMovementMethod());
        sliderEffect = 0;
        //Nav Drawer
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[5];
        drawerItem[0] = new ObjectDrawerItem(android.R.drawable.ic_menu_today, getResources().getString(R.string.nav0));
        drawerItem[1] = new ObjectDrawerItem(android.R.drawable.ic_menu_search, getResources().getString(R.string.nav1));
        drawerItem[2] = new ObjectDrawerItem(android.R.drawable.ic_menu_search, getResources().getString(R.string.nav2));
        drawerItem[3] = new ObjectDrawerItem(android.R.drawable.ic_dialog_map, getResources().getString(R.string.nav3));
        drawerItem[4] = new ObjectDrawerItem(android.R.drawable.ic_lock_power_off, getResources().getString(R.string.nav4));
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
        /*file_maps.put("Hannibal",R.drawable.hannibal);
        file_maps.put("Big Bang Theory",R.drawable.bigbang);
        file_maps.put("House of Cards",R.drawable.house);
        file_maps.put("Game of Thrones", R.drawable.game_of_thrones);*/
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
                new eventsActivity();
                new Bundle();
                this.startActivity(new Intent(this, eventsActivity.class));
                break;
            case 1:
                new searchActivity();
                new Bundle();
                this.startActivity(new Intent(this, searchActivity.class));
                break;
            case 2:
                new searchInTime();
                new Bundle();
                this.startActivity(new Intent(this, searchInTime.class));
                break;
            case 3:
                new MapsActivity();
                new Bundle();
                this.startActivity(new Intent(this, MapsActivity.class));
                break;
            case 4:
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
}