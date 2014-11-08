package example.futbolapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Felipe on 07/11/2014.
 */
public class RegisterActivity extends Activity{
    private static String url = "http://solweb.co/reservas/api/login/logins/add";
    AQuery aq;
    public static final String name = "nameKey";
    public static final String pass = "passwordKey";
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String idUsuario = "idUsuario";
    SharedPreferences sharedpreferences;

    private EditText usuario,contrasenia,recontrasenia;
    private static boolean UserExist;
    private String user;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_registro);

        UserExist = false;

        usuario = (EditText) findViewById(R.id.usuaRegistro);
        contrasenia = (EditText) findViewById(R.id.passRegistro);
        recontrasenia = (EditText) findViewById(R.id.RepassRegistro);
        Button btnRegistro = (Button) findViewById(R.id.btnRegistro);
        aq = new AQuery(this);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (usuario.getText().toString().trim().equals("") && contrasenia.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
                } else if (usuario.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Ingrese el usuario", Toast.LENGTH_SHORT).show();
                } else if (contrasenia.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Ingrese la contraseña", Toast.LENGTH_SHORT).show();
                } else if(contrasenia.getText().toString().trim().equals(recontrasenia.getText().toString())){
                    //checkUsuario();
                    registroUrl();
                }else{
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coincide", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registroUrl(){
        user = usuario.getText().toString();
        password = contrasenia.getText().toString();
        try {
           password = LoginApp.sha1(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String url2 = "http://solweb.co/reservas/api/login/logins/add/"+ user+"-"+password;
        aq.ajax(url2, String.class, this, "registrado");
    }

    public void registrado(String url, String result, AjaxStatus status) {
        //When result is not null
        if (result != null) {
            if(result.equals("0"))
            {Toast.makeText(aq.getContext(), "El Nombre de usuario ya existe", Toast.LENGTH_SHORT).show();
            }else{
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(name, user);
                editor.putString(pass, password);
                editor.putString(idUsuario, result);
                editor.commit();

                Toast.makeText(aq.getContext(), "Bienvenido a CanchaFinder "+user, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, mainActivity.class);
                startActivity(intent);
                finish();
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
}
