package example.futbolapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.facebook.android.Facebook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import example.futbolapp.database.local.DB_Manager;

public class LoginApp extends FragmentActivity {

    private String user;
    private String passw;
    public EditText usuario;
    public EditText password;
    public Button btnlogin;
    private ProgressDialog pDialog;
    private Facebook fb;
    private MainFragment mainFragment;
    //AQuery object
    AQuery aq;
    private static Boolean loginOK;
    DB_Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Instantiate AQuery Object
        aq = new AQuery(this);
        /*if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            mainFragment = new MainFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, mainFragment)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            mainFragment = (MainFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }*/
        setContentView(R.layout.activity_login_app);

       /*if(!mainFragment.isLoggedIn())
        {
            setContentView(R.layout.activity_login_app);
        }else{
           setContentView(R.layout.actividad_principal);
        }*/
    }

    public void checkUser(){
        //JSON URL
        String url = "http://solweb.co/reservas/api/login/logins";
        //Make Asynchronous call using AJAX method and show progress gif until get info
        aq.progress(R.id.progressBarSearch).ajax(url, JSONObject.class, this,"jsonCallback");
    }

    public void jsonCallback(String url, JSONObject json, AjaxStatus status) {
        //When JSON is not null
        if (json != null) {
            //Log.v("JSON", json.toString());
            String jsonResponse = "";
            try {
                //Get json as Array
                JSONArray jsonArray = json.getJSONArray("login");
                if (jsonArray != null) {
                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        //Get the name of the field from array index
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.e("Usuario contraseña: ==> ",jsonObject.getString("user")+", "+jsonObject.getString("pass") );
                        Log.d("Usuario contraseña: Local ==> ",user+", "+passw);
                        if(user.equals(jsonObject.getString("user").trim())){
                            Log.d("Usuario hallado ","True");
                            if(passw.equals(jsonObject.getString("pass").trim())){
                                Log.d("Password hallado ","True");
                                loginOK = true;
                            }
                        }
                        //SQLite
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(aq.getContext(), "Error in parsing JSON", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(aq.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
            if(loginOK) {
                Intent intent = new Intent(this, mainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(aq.getContext(), "Verifique que los datos esten correctos.", Toast.LENGTH_LONG).show();
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

    public void login(View view)
    {
        /*
        if (user.matches("")) {
            Toast.makeText(this, "You did not enter a username", Toast.LENGTH_SHORT).show();
        }
        passw = password.getText().toString();
        Log.v("User", usuario.getText().toString());
        Log.v("Password ", passw);
        Toast.makeText(getApplicationContext(), "usuario"+user, Toast.LENGTH_SHORT).show();
        if(user.trim().equals(persona) && passw.trim().equals(contrasenia))
        {
        Intent intent = new Intent(this, mainActivity.class);
        startActivity(intent);
        }else
        {
          Toast.makeText(getApplicationContext(), "Verifique los datos", Toast.LENGTH_SHORT).show();
        }*/
        //SQlite
        usuario = (EditText)findViewById(R.id.usuarioLogin);
        password = (EditText)findViewById(R.id.passwordLogin);
        user = usuario.getText().toString().trim();
        passw = password.getText().toString().trim();
        if (user.matches("")) {
            Toast.makeText(this, "Ingrese Usuario", Toast.LENGTH_SHORT).show();
        }
        if (passw.matches("")) {
            Toast.makeText(this, "Ingrese Contraseña", Toast.LENGTH_SHORT).show();
        }
        Log.v("User", usuario.getText().toString());
        Log.v("Password ", passw);
        loginOK = false;
        checkUser();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
