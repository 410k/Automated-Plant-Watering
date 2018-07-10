package upgradekaro.smspreference;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by cred-user-6 on 10/2/17.
 */

public class Dbhelpre extends SQLiteOpenHelper {
    Calendar c=Calendar.getInstance();
    public Dbhelpre(Context context) {
        super(context,"iotplant.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS iotplantsent ( id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT , datetime LONG )");
    }
    public void CreateTableForSent(String message){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues cvv=new ContentValues();
        cvv.put("message",message);
       cvv.put("datetime",getDateTime());
    //    cvv.put("datetime",);
        database.insert("iotplantsent",null,cvv);

    };
    public Cursor Readdatamessages(){
       SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM iotplantsent",null);
        return cursor;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    private String getDateTime() {
        SimpleDateFormat dateFormat =new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String Formatteddate=dateFormat.format(c.getTime());
        Log.d("dateandtiime",""+Formatteddate);
        return Formatteddate;
    }
}
