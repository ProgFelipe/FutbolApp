package example.futbolapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Login extends Activity {

    private String user;
    private String passw;
    private String persona = "David";
    private String contrasenia = "12345";
    private TextView aux;
    private TextView aux1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
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
