package example.futbolapp.database;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Felipe on 02/10/2014.
 */
public class MergeData {
    /* Tutorial json en read content
    *https://www.youtube.com/watch?v=qcotbMLjlA4&feature=youtu.be
    */
    //static String jsonCanchas = "http://solweb.co/reservas/api/field/fields";
    static String jsonCanchas =  "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22YHOO%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=cbfunc";
    static String stackCanchaId = "";
    static String stackCanchaName = "";
    static String stackLatitud = "";
    static String stackLongitud = "";
    static String stackPhone = "";
    static String stackDirection = "";
    static String stackInfo = "";
    static String stackIcon = "";

    public MergeData(){
        new MyAsyncTask().execute();
    }


    private class MyAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httppost = new HttpPost(jsonCanchas);

            httppost.setHeader("Content-type","application/json");
            InputStream inputStream = null;
            String result = null;
            try{
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder theStringBuilder = new StringBuilder();
                String line = null;
                while((line = reader.readLine()) != null){
                    theStringBuilder.append(line+"\n");
                }
                result = theStringBuilder.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try{
                    if(inputStream != null) inputStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            JSONObject jsonObject;

            try {
                Log.v("JSONParser RESULT", result);
                jsonObject = new JSONObject(result);
                JSONObject fieldJsonObject = jsonObject.getJSONObject("query");
                //JSONObject querJsonObject = fieldJsonObject.getJSONObject("query");

                stackCanchaName = fieldJsonObject.getString("name");
                stackLatitud = fieldJsonObject.getString("latitude");
                stackLongitud = fieldJsonObject.getString("length");
                stackDirection = fieldJsonObject.getString("url");
                stackIcon = fieldJsonObject.getString("icon");
                stackPhone = fieldJsonObject.getString("phone");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            //Show getted information of fields
        }

    }
}
