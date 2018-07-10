package upgradekaro.smspreference;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String colName;
    TextView tView;
    ListView lstviewrec;
    ListView sentssmslistview;
    Dbhelpre dbhelpre;
   String phonenum="+919505628345";
   // String  phonenum="9985982342";
    Button send;
    EditText edsssendmessage;
    ArrayList recievedmessages=new ArrayList();
    ArrayList Sentsmslist=new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Permissioncheckforsix();
        dbhelpre=new Dbhelpre(MainActivity.this);
        send= (Button) findViewById(R.id.btn_send);
        edsssendmessage= (EditText) findViewById(R.id.edsendmessage);
        sentssmslistview= (ListView) findViewById(R.id.sentsmslistview);
        lstviewrec= (ListView) findViewById(R.id.lstview_recsms);
        send.setOnClickListener(this);
        loadsentSmsList();
        displaySmsLog();
    }

    private void displaySmsLog() {
        StringBuilder smsBuilder = new StringBuilder();
        StringBuilder smssentBuilder=new StringBuilder();
        final String SMS_URI_INBOX = "content://sms/inbox";
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
            Cursor curread = getContentResolver().query(uri, projection, "address='"+phonenum+"'",null, "date desc");
            ////read recieved messages /////
            if (curread.moveToFirst()) {
                int index_Address = curread.getColumnIndex("address");
                int index_Body = curread.getColumnIndex("body");
                int index_date=curread.getColumnIndex("date");
                do {
                    String strAddress = curread.getString(index_Address);
                    String strbody = curread.getString(index_Body);
                   // String strdate=curread.getString(index_date);
                    Log.e("check message and date",curread.getString(index_Body)+""+curread.getString(index_date));
            //at this part you will get the messages from sms history in your mobile
                    Date date=new Date(curread.getLong(index_date));
                    String strdate=new SimpleDateFormat("dd/MM/yyyy hh:mm").format(date);
                    recievedmessages.add(strbody+"\n at "+strdate);
                    Log.d("checkmessages",""+smsBuilder);
                }
                while (curread.moveToNext());
                if (!curread.isClosed()) {
                    curread.close();
                    curread = null;
                }
                ArrayAdapter recsmsAdapter=new ArrayAdapter(MainActivity.this,R.layout.rrees,recievedmessages);
                lstviewrec.setAdapter(recsmsAdapter);
            } else {
                smsBuilder.append("no result!");
            } // end if
            ////// read sent messages///

        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if(v==send){
            String mess=edsssendmessage.getText().toString();
            if(TextUtils.isEmpty(mess)){
                edsssendmessage.setError("enter command");
            }
            else {
               // dbhelpre=new Dbhelpre(MainActivity.this);
                edsssendmessage.setText("");
                sendSMS(phonenum,mess);
                dbhelpre.CreateTableForSent(mess);
                loadsentSmsList();
            }

        }
    }

    private void loadsentSmsList() {
        ArrayAdapter arre=new ArrayAdapter(MainActivity.this,R.layout.sentitem,Sentsmslist);
        arre.clear();
        Cursor c = dbhelpre.Readdatamessages();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String name = c.getString(c.getColumnIndex("message"));
                    String sentdate=c.getString(c.getColumnIndex("datetime"));
                    //String strdate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(sentdate);
                    Sentsmslist.add(name+"\n sent at "+sentdate);
                    sentssmslistview.setAdapter(arre);
                } while (c.moveToNext());
            }
        }
    }

    public void Permissioncheckforsix(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_SMS)) {
            }
        }
        }
}
