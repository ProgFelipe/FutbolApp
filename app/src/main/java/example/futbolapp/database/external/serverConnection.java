package example.futbolapp.database.external;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Usuario on 12/09/2014.
 */
public class serverConnection {
    static String bd = "reservas";
    static String user = "solwebco_reserva";
    static String password = "TPsKz!)IG*Fo";
    static String url = "jdbc:mysql://www.solweb.co/"+bd;

    Connection connection = null;

    /** Constructor de DbConnection */
    public serverConnection() {
        try{
            //obtenemos el driver de para mysql
            Class.forName("com.mysql.jdbc.Driver");
            //obtenemos la conexión
            connection = DriverManager.getConnection(url, user, password);

            if (connection!=null){
                Log.d("MyApp", "Conexión a base de datos "+bd+" OK\n");
                //System.out.println("Conexión a base de datos "+bd+" OK\n");
            }else{
                Log.d("MyApp", "Conexión a base de datos "+bd+" FAIL\n");
            }
        }
        catch(SQLException e){
            Log.d("MyApp", "Error "+e+"\n");
            //System.out.println(e);
        }catch(ClassNotFoundException e){
            Log.d("MyApp", "Error "+e+"\n");
            //System.out.println(e);
        }catch(Exception e){
            Log.d("MyApp", "Error "+e+"\n");
            //System.out.println(e);
        }
    }
    /**Permite retornar la conexión*/
    public Connection getConnection(){
        return connection;
    }

    public void desconectar(){
        connection = null;
    }

    public void getHorasDisponibles(){
        try {
            // open a connection to the site
            URL url = new URL("http://solweb.co/reservas/api/reservations/availability");
            URLConnection con = url.openConnection();
            // activate the output
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());
            // send your parameters to your site
            ps.print("id=1");

            // we have to get the input stream in order to actually send the request
            con.getInputStream();

            // close the print stream
            ps.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
