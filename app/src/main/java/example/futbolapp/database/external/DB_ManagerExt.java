package example.futbolapp.database.external;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.internal.is;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felipegi on 12/09/2014.
 * Apoyo
 * http://www.androidsnippets.com/executing-a-http-post-request-with-httpclient
 */
public class DB_ManagerExt {

    //Obtiene las canchas por hora disponible
    public void getFieldsByDate(String date, String time){
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.solweb.co/reservas/api_movil/getAvailabilityByField.php");
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("id", date));
                nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
    }

    //Check if field is available in a setted date and time
    public boolean checkDisponibility(Context context, String fieldID,String hour, String day, String month, String year){
        boolean canReserve = false;
        Log.v("RESPUESRA RESPUESRA RESPA", "checkeando");
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://www.solweb.co/reservas/api_movil/checkDisponibility.php");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("hour", hour));
            nameValuePairs.add(new BasicNameValuePair("day", day));
            nameValuePairs.add(new BasicNameValuePair("month", month));
            nameValuePairs.add(new BasicNameValuePair("year", year));
            //nameValuePairs.add(new BasicNameValuePair("id", fieldID));
            //nameValuePairs.add(new BasicNameValuePair("idLogin", "3"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            Log.v("RESPUESRA RESPUESRA RESPA", response.toString());
            Log.v("RESPUESRA RESPUESRA RESPA", response.toString());
            Log.v("RESPUESRA RESPUESRA RESPA", response.toString());
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            Toast.makeText(context, "Error"+e, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Toast.makeText(context,"Error "+e, Toast.LENGTH_SHORT).show();
        }

            if(canReserve){
              //  setReservation(fieldID, hour,  day,  month,  year);
            }
        return canReserve;
    }

    public void setReservation(String fieldID,String hour, String day, String month, String year ){

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://www.solweb.co/reservas/api_movil/getAvailabilityByField.php");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("hour", hour));
            nameValuePairs.add(new BasicNameValuePair("day", hour));
            nameValuePairs.add(new BasicNameValuePair("month", hour));
            nameValuePairs.add(new BasicNameValuePair("year", hour));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }
}