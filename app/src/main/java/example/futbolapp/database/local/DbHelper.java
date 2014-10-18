package example.futbolapp.database.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Felipe on 19/09/2014.
 */
public class DbHelper extends SQLiteOpenHelper{
    //SQLite
    private static final String DB_NAME = "canchas.sqlite";
    private static final int DB_SCHEME_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(DB_Manager.CREATE_TABLE_USUARIO);
        db.execSQL(DB_Manager.CREATE_TABLE_CANCHAS);
    }

    @Override
    //Para actualizar esquema
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
