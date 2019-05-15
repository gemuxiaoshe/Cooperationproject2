package util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class Sqliteopenhelper extends SQLiteOpenHelper {
    private Context mcontext;
    public static final String CREATE_USERS = "create table User(" + "id integer primary key autoincrement,"
            + "username text,"
            + "userpass text,"
            + "phone integer,"
            +"email text)";

    public Sqliteopenhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
         mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
          db.execSQL(CREATE_USERS);
      //  Toast.makeText(mcontext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
