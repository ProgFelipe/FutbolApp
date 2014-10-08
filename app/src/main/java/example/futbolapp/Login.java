package example.futbolapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.Signature;

import com.facebook.android.Facebook;
import com.facebook.widget.LoginButton;

import org.w3c.dom.Text;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends FragmentActivity {

    private String user;
    private String passw;
    private String persona = "David";
    private String contrasenia = "12345";
    private TextView aux;
    private TextView aux1;
    //private MainFragment mainFragment;

    Facebook fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String API_ID = getString(R.string.AppID);
        fb = new Facebook(API_ID);

        Button loginFb = (Button) findViewById(R.id.authButton);

        loginFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fb.isSessionValid())
                {
                    try {
                        fb.logout(getApplicationContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        // para obtener la key hash y colocarla en el proyecto
        /*
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "example.futbolapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                System.out.println("KeyHash : "+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        */
        /*
        if (savedInstanceState == null) {
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
        }
        */

        TextView usuario = (TextView) findViewById(R.id.usuario);
        TextView password = (TextView) findViewById(R.id.password);

        aux = usuario;
        aux1 = password;
    }

    public void login(View view)
    {
        user = String.valueOf(aux.getText());
        passw = String.valueOf(aux1.getText());

        if(user.equals(persona) && passw.equals(contrasenia))
        {
            Intent intent = new Intent(this, mainActivity.class);
            startActivity(intent);
        }else
        {
            Toast.makeText(getApplicationContext(),"Verifique los datos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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
