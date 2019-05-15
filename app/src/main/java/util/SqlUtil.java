package util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class SqlUtil {
    boolean flag=false;
    public Sqliteopenhelper sqliteopenhelper;
    public boolean login(Context context,String databasename,String sql,String name,String id){
        sqliteopenhelper = new Sqliteopenhelper(context, databasename, null, 1);
        SQLiteDatabase db = sqliteopenhelper.getWritableDatabase();
        Cursor cursor =db.rawQuery(sql,new String[]{name,id});
        if (cursor.moveToFirst() == false) {

        } else {
            flag = true;
            Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
        }
       cursor.close();
        return flag;
    }

}
