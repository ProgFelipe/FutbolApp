package example.futbolapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.LoginButton;

import org.w3c.dom.Text;

public class Login extends FragmentActivity {

    private String user;
    private String passw;
    private String persona = "David";
    private String contrasenia = "12345";
    private TextView aux;
    private TextView aux1;
    private MainFragment mainFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        //Button btnLogin = (Button) findViewById(R.id.btnLogin);
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
