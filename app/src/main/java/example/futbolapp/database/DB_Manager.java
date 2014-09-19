package example.futbolapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Felipe on 19/09/2014.
 */
public class DB_Manager {
    public static final String TABLE_NAME = "canchas";

    public static final String CN_Id = "_id";
    public static final String CN_Nombre = "name";
    public static final String CN_Direccion = "dir";
    public static final String CN_Telefono = "tel";
    public static final String CN_Latitud = "lat";
    public static final String CN_Longitud = "long";
    public static final String CN_Icono = "_icon";
    public static final String CN_Informacion = "info";

   //Creacion de la tabla canchas

    public static final String CREATE_TABLE_CANCHAS = "create table "+TABLE_NAME+" ("
            + CN_Id + " integer primary key autoincrement, "
            + CN_Nombre + " text not null, "
            + CN_Direccion + " text not null, "
            + CN_Telefono + " text not null, "
            + CN_Latitud + " text not null, "
            + CN_Longitud + " text not null, "
            + CN_Icono + " text null ,"
            + CN_Informacion + " text); ";

    public static final String TABLE_NAME1 = "usuario";
    public static final String CN_IdUsuario = "_id";
    public static final String CN_NombreUsuario = "nombre";
    public static final String CN_Password = "clave";

    public static final String CREATE_TABLE_USUARIO = "create table "+TABLE_NAME1+" ("
            + CN_Id + " integer primary key autoincrement, "
            + CN_NombreUsuario + " text not null, "
            + CN_Password + " text not null); ";


    private DbHelper helper;
    private SQLiteDatabase db;

    public DB_Manager(Context context) {
        helper = new DbHelper(context);
        db = helper.getWritableDatabase();
    }


    public ContentValues setContentValuesCancha(String nomb, String direccion, String tel,
                                          String latitud, String longitud, String Icono, String info){
        ContentValues valores = new ContentValues();
        valores.put(CN_Nombre, nomb);
        valores.put(CN_Direccion, direccion);
        valores.put(CN_Telefono, tel);
        valores.put(CN_Latitud, latitud);
        valores.put(CN_Longitud, longitud);
        valores.put(CN_Icono, Icono);
        valores.put(CN_Informacion, info);

        return valores;
    }

    public void insertarCancha(String nomb, String direccion, String tel,
                               String latitud, String longitud, String icono, String info){
        db.insert(TABLE_NAME, null, setContentValuesCancha(nomb,direccion,tel,latitud,longitud,icono, info));
    }

    public ContentValues setContentValuesUsuario(String nomb, String clave){
        ContentValues valores = new ContentValues();
        valores.put(CN_NombreUsuario, nomb);
        valores.put(CN_Password, clave);

        return valores;
    }

    public void registrarUsuario(String nomb, String clave){
        db.insert(TABLE_NAME1, null, setContentValuesUsuario(nomb, clave));
    }
}
