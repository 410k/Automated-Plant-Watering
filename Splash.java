package upgradekaro.smspreference;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.ldoublem.loadingviewlib.view.LVBlock;


public class Splash extends AppCompatActivity {
    private static int TimeDelay=3000;
    LVBlock lvGearsTwo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        lvGearsTwo= (LVBlock) findViewById(R.id.LVB);
        //lvGearsTwo.setViewColor(R.color.grey);
        lvGearsTwo.startAnim();
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lvGearsTwo.stopAnim();
                Intent splashIntent=new Intent(Splash.this,MainActivity.class);
                startActivity(splashIntent);
                finish();
            }
        },TimeDelay);
    }
}
