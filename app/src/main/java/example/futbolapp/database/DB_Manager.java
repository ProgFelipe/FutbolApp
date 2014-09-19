package example.futbolapp.database;

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
            + CN_Icono + " ,"
            + CN_Informacion + " text); ";

    public static final String TABLE_NAME1 = "usuario";
    public static final String CN_IdUsuario = "_id";
    public static final String CN_NombreUsuario = "nombre";
    public static final String CN_Password = "clave";

    public static final String CREATE_TABLE_USUARIO = "create table "+TABLE_NAME1+" ("
            + CN_Id + " integer primary key autoincrement, "
            + CN_NombreUsuario + " text not null, "
            + CN_Password + " text not null); ";
}
