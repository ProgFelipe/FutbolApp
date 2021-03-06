package example.futbolapp.database.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import example.futbolapp.database.local.DbHelper;

/**
 * Created by Felipe on 19/09/2014.
 */
public class DB_Manager {
    public static final String TABLE_CANCHAS = "canchas";

    public static final String CN_IdCancha = "_id";
    public static final String CN_Nombre = "name";
    public static final String CN_Direccion = "dir";
    public static final String CN_Telefono = "tel";
    public static final String CN_Latitud = "lat";
    public static final String CN_Longitud = "long";
    public static final String CN_Icono = "_icon";
    public static final String CN_Informacion = "info";



   //Creacion de la tabla canchas

    public static final String CREATE_TABLE_CANCHAS = "create table "+TABLE_CANCHAS+" ("
            + CN_IdCancha + " integer primary key, "
            + CN_Nombre + " text not null, "
            + CN_Direccion + " text not null, "
            + CN_Telefono + " text not null, "
            + CN_Latitud + " text not null, "
            + CN_Longitud + " text not null, "
            + CN_Icono + " text null ,"
            + CN_Informacion + " text); ";

    /*public static final String TABLE_NAME1 = "usuario";
    public static final String CN_IdUsuario = "_id";
    public static final String CN_NombreUsuario = "nombre";
    public static final String CN_Password = "clave";

    public static final String CREATE_TABLE_USUARIO = "create table "+TABLE_NAME1+" ("
            + CN_IdUsuario + " integer primary key autoincrement, "
            + CN_NombreUsuario + " text not null, "
            + CN_Password + " text not null); ";*/


    private DbHelper helper;
    private SQLiteDatabase db;

    public DB_Manager(Context context) {
        helper = new DbHelper(context);
        db = helper.getWritableDatabase();
    }


    private ContentValues setContentValuesCancha(Integer id, String nomb, String direccion, String tel,
                                          String latitud, String longitud, String Icono, String info){
        ContentValues valores = new ContentValues();
        valores.put(CN_IdCancha, id);
        valores.put(CN_Nombre, nomb);
        valores.put(CN_Direccion, direccion);
        valores.put(CN_Telefono, tel);
        valores.put(CN_Latitud, latitud);
        valores.put(CN_Longitud, longitud);
        valores.put(CN_Icono, Icono);
        valores.put(CN_Informacion, info);

        return valores;
    }

    public void insertarCancha(int id, String nomb, String direccion, String tel,
                               String latitud, String longitud, String icono, String info){
        db.insert(TABLE_CANCHAS, null, setContentValuesCancha(id,nomb,direccion,tel,latitud,longitud,icono, info));
    }

  /*//FUTURE USE
   private ContentValues setContentValuesUsuario(String nomb, String clave){
        ContentValues valores = new ContentValues();
        valores.put(CN_NombreUsuario, nomb);
        valores.put(CN_Password, clave);

        return valores;
    }

    public void registrarUsuario(String nomb, String clave){
        db.insert(TABLE_NAME1, null, setContentValuesUsuario(nomb, clave));
    }*/

    public void eliminarCancha(String ID){
        db.delete(TABLE_CANCHAS, CN_IdCancha+"=?",new String[]{ID});
    }

    public void eliminarCanchas(){
        db.execSQL("delete from "+ TABLE_CANCHAS);
    }
    public void modificarInfoCancha(Integer id, String nomb, String direccion, String tel,
                                    String latitud, String longitud, String Icono, String info){
        db.update(TABLE_CANCHAS, setContentValuesCancha(id,nomb, direccion , tel , latitud, longitud, Icono, info),CN_IdCancha+"=?",new String []{Integer.toString(id)});
    }
    public Cursor cargarCursorCanchas(){
        String[] columnas = new String[]{CN_IdCancha,CN_Nombre,CN_Direccion,CN_Telefono,CN_Latitud,CN_Longitud,CN_Icono,CN_Informacion};
        //Retorna todos los registros de la base de datos
        return db.query(TABLE_CANCHAS, columnas, null, null, null, null, null, null);
    }
    public Cursor buscarCancha(String nombre){
        String[] columnas = new String[]{CN_IdCancha,CN_Nombre,CN_Direccion,CN_Telefono,CN_Latitud,CN_Longitud,CN_Icono,CN_Informacion};
        return db.query(TABLE_CANCHAS, columnas, CN_Nombre +"=?", new String []{nombre}, null, null, null, null);
    }

    public Cursor buscarCanchaById(String id){
        String[] columnas = new String[]{CN_IdCancha,CN_Nombre,CN_Direccion,CN_Telefono,CN_Latitud,CN_Longitud,CN_Icono,CN_Informacion};
        return db.query(TABLE_CANCHAS, columnas, CN_IdCancha +"=?", new String []{id}, null, null, null, null);
    }
    public boolean buscarIdCancha(String id){
        String[] columnas = new String[]{CN_IdCancha,CN_Nombre,CN_Direccion,CN_Telefono,CN_Latitud,CN_Longitud,CN_Icono,CN_Informacion};
        if(db.query(TABLE_CANCHAS, columnas, CN_IdCancha +"=?", new String []{id}, null, null, null, null).getCount() > 0){
            return true;
        }

        return false;

    }
    public ArrayList<String> fromCursorToArrayListString(Cursor c){
        ArrayList<String> result = new ArrayList<String>();
        c.moveToFirst();
        for(int i = 0; i < c.getCount(); i++){
            String row = c.getString(c.getColumnIndex(CN_Nombre));
            //You can here manipulate a single string as you please
            result.add(row);
            c.moveToNext();
        }
        return result;
    }
}