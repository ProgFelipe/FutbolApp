package example.futbolapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class LoginApp extends Activity {

    private String user;
    private String passw;
    public EditText usuario;
    public EditText password;

    public static final String name = "nameKey";
    public static final String pass = "passwordKey";
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String idUsuario = "idUsuario";


    private Button loginFace, btnLogin, btnregister;
    private Button publishButton;
    private Boolean logged;

    private Session.StatusCallback sessionStatusCallback;
    private static Session currentSession;

    //AQuery object
    AQuery aq;
    SharedPreferences sharedpreferences;
    private static Boolean loginOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_app);
        //Instantiate AQuery Object
        aq = new AQuery(this);
        logged = false;
        /*TextView header = (TextView) findViewById(R.id.txtHeaderLogin);
        header.setTextColor(Color.rgb(239, 252, 240));

        EditText usuarioLogin = (EditText) findViewById(R.id.usuarioLogin);
        usuarioLogin.setTextColor(Color.rgb(239, 252, 240));

        EditText passwordLogin = (EditText) findViewById(R.id.passwordLogin);
        passwordLogin.setTextColor(Color.rgb(239, 252, 240));*/
        // create instace for sessionStatusCallback
        sessionStatusCallback = new Session.StatusCallback() {

            @Override
            public void call(Session session, SessionState state, Exception exception) {
                onSessionStateChange(session, state, exception);

            }
        };
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        btnregister = (Button) findViewById(R.id.btnRegistro);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });
        //Facebook login button
        loginFace = (Button) findViewById(R.id.authButton);
        loginFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(logged == false){
                    //connectToFB();
                    logged = true;
                    startActivity(new Intent(getApplicationContext(), mainActivity.class));
                    finish();
                }else{
                    if (currentSession != null) {
                        logged = false;
                        currentSession.closeAndClearTokenInformation();
                    }
                }
            }
        });
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
    public static Session getSession(){
        return currentSession;
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

    /**
     * Publishes story on the logged user's wall
     */
    public void publishStory() {
        Bundle params = new Bundle();
        params.putString("name", "testing Android Share");
        params.putString("caption", "test-caption: Felipe GI from android");
        params.putString("description", "test-description");
        params.putString("link", "https://www.youtube.com/watch?v=8zl_dPFyXcY");
        params.putString("picture","http://icons.iconarchive.com/icons/martz90/circle/512/android-icon.png");

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
    protected void onResume() {
        sharedpreferences=getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(name))
        {
            if(sharedpreferences.contains(pass)){
                Intent i = new Intent(this, example.futbolapp.
                        mainActivity.class);
                startActivity(i);
            }
        }
        super.onResume();
    }

    /*
    Login Normal
    llama a CheckUser para mirar si usuario y contraseña coinciden
     */
    public void login(){
        usuario = (EditText)findViewById(R.id.usuarioLogin);
        password = (EditText)findViewById(R.id.passwordLogin);
        user = usuario.getText().toString().trim();
        passw = password.getText().toString().trim();
        if (user.matches("") && passw.matches("")) {
            Toast.makeText(this, "Ingrese Usuario y Contraseña", Toast.LENGTH_SHORT).show();
        }else {
            if (user.matches("")) {
                Toast.makeText(this, "Ingrese Usuario", Toast.LENGTH_SHORT).show();
            }else
            if (passw.matches("")) {
                Toast.makeText(this, "Ingrese Contraseña", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    passw = sha1(passw);
                    loginOK = false;
                    checkUser();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        }
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
            try {
                //Get json as Array
                JSONArray jsonArray = json.getJSONArray("login");
                if (jsonArray != null) {
                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        //Get the name of the field from array index
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(user.equals(jsonObject.getString("user").trim())){
                            //Log.d("Usuario hallado ","True");
                            if(passw.equals(jsonObject.getString("pass").trim())){
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(name, user);
                                editor.putString(pass, passw);
                                editor.putString(idUsuario, jsonObject.getString("id"));
                                editor.commit();
                                loginOK = true;
                            }
                        }
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
    public static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
