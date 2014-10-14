package example.futbolapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.android.Facebook;

public class LoginApp extends FragmentActivity {

    private String user;
    private String passw;
    private String persona = "David";
    private String contrasenia = "12345";
    public EditText usuario;
    public EditText password;
    public Button btnlogin;
    private Facebook fb;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    public void login(View view)
    {
        /*usuario = (EditText)findViewById(R.id.usuarioLogin);
        password = (EditText)findViewById(R.id.passwordLogin);
        user = usuario.getText().toString();
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
        Intent intent = new Intent(this, mainActivity.class);
        startActivity(intent);
        finish();
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
