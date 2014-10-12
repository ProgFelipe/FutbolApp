package example.futbolapp.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import example.futbolapp.R;
import example.futbolapp.database.local.DB_Manager;

/**
 * Created by Felipe on 02/10/2014.
 */
public class MergeData {
    /* Tutorial json en read content
     * Apoyado en :
    * Tutorial 16: Recuperar y Consultar Registros de SQLite con Android y mostrarlos en un ListView
    * https://www.youtube.com/watch?v=sD-pz-vKlnI
    *https://www.youtube.com/watch?v=qcotbMLjlA4&feature=youtu.be
    */
    private DB_Manager manager;
    private AQuery aq;
    private Context context;
    ArrayList<String> fields;
    public MergeData(Context context){
        this.context = context;
        fields = new ArrayList<String>();
        manager = new DB_Manager(context);
        aq = new AQuery(context);
    }

    public void getFields(){
        //JSON URL
        String url = "http://solweb.co/reservas/api/field/fields";
        //Make Asynchronous call using AJAX method and show progress gif until get info
        aq.ajax(url, JSONObject.class, context,"jsonCallback");
    }

    public void jsonCallback(String url, JSONObject json, AjaxStatus status) {
        //When JSON is not null
        Log.v("JSON", json.toString());
        if (json != null) {

            String jsonResponse = "";
            try {
                //Get json as Array
                JSONArray jsonArray = json.getJSONArray("field");
                if (jsonArray != null) {
                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        //Get the name of the field from array index
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        fields.add(jsonObject.getString("name"));

                        //SQLite
                        if(manager.buscarIdCancha(jsonObject.getString("id")) == false)
                        {
                            manager.insertarCancha(Integer.parseInt(jsonObject.getString("id")), jsonObject.getString("name"), jsonObject.getString("address"),
                                    jsonObject.getString("phone"), jsonObject.getString("latitude"), jsonObject.getString("length"),
                                    jsonObject.getString("icon"), jsonObject.getString("description"));
                        }
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(aq.getContext(), "Error in parsing JSON", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(aq.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
            //ArrayList a = manager.fromCursorToArrayListString(manager.cargarCursorCanchas());
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line, a);
            //Log.v("numero de canhaS----- ", Integer.toString(a.size()));
            //listView.setAdapter(adapter);
            //autoComplete.setAdapter(new ArrayAdapter<String>(this,R.layout.list_details, a));
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
                Toast.makeText(aq.getContext(),"Unexpected Error occured",Toast.LENGTH_SHORT).show();
            }
        }
    }


}
