package example.futbolapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Usuario on 11/09/2014.
 */
public class mainActivity  extends Activity{
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);

    }

    public void onBtnClick(View view){
        Intent intent;
           switch (view.getId()){
               case R.id.btn_programa:
                   intent = new Intent(this, programa.class);
                   break;
               case R.id.btnMapa:
                   intent = new Intent(this, MapsActivity.class);
                   break;
               default:
                   intent = new Intent(this, busqueda.class);
                   break;
           }
        startActivity(intent);
    }
}
