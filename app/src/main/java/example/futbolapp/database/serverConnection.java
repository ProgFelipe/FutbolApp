package example.futbolapp.database;

import android.util.Log;

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
}
