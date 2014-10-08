package example.futbolapp;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

import example.futbolapp.database.local.DB_Manager;

/**
 * Created by Felipe on 04/10/2014.
 */
public class fieldActivity extends ActionBarActivity {
    private DB_Manager manager;
    private TextView nombreCancha;
    private TextView direccionCancha;
    private TextView telCancha;
    private TextView infoCancha;
    private Button buttonReservar;
    private ImageView imgCancha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancha);
        Bundle extras = getIntent().getExtras();
        String value = "CanchaN";
        if (extras != null) {
            value = extras.getString("fieldID");
        }
        //Elements
        nombreCancha = (TextView) findViewById(R.id.nombreCancha);
        direccionCancha = (TextView) findViewById(R.id.dirCancha);
        telCancha = (TextView) findViewById(R.id.telCancha);
        infoCancha = (TextView) findViewById(R.id.infoCancha);
        imgCancha = (ImageView) findViewById(R.id.ImgCancha);
        //Get Database cursor
        manager = new DB_Manager(this);
        Cursor c = manager.buscarCanchaById(value);
        Log.v("Nombre :", Integer.toString(c.getCount()));
        try {
            c.moveToFirst();
            if(c != null && c.getCount() > 0){
                nombreCancha.setText(c.getString(1));
                direccionCancha.setText(c.getString(2));
                telCancha.setText(c.getString(3));
                infoCancha.setText(c.getString(7));
                /*if(c.getString(6) != null){
                URL newurl = new URL(c.getString(6));
                Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                imgCancha.setImageBitmap(mIcon_val);
                }*/
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"A ocurrido un error con la aplicacion", Toast.LENGTH_SHORT).show();
        }

        //Set Elements data from database


    }
}
