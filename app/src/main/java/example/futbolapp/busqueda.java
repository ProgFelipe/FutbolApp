package example.futbolapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.GridView;

/**
 * Created by Usuario on 11/09/2014.
 */
public class busqueda extends ActionBarActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busqueda);

        GridView gridView = (GridView) findViewById(R.id.grid_view);

        // Instance of ImageAdapter Class
        gridView.setAdapter(new ImageAdapter(this));
    }
}
