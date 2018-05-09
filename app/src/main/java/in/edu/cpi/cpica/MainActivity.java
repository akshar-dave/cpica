package in.edu.cpi.cpica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","");
        String first_name = sharedPreferences.getString("first_name","");
        String password = sharedPreferences.getString("password","");
        boolean connected_to_internet = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        connected_to_internet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if(!connected_to_internet){
            Intent i = new Intent(MainActivity.this,No_internet.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        else if(connected_to_internet){
            if(username.length()>0 && password.length()>0 && first_name.length()>0) {
                Intent i = new Intent(getApplicationContext(),dashboard.class);
                startActivity(i);
                finish();
            }
            else {
                TextView welcome_text = (TextView) findViewById(R.id.welcome_text);
                ImageView logo = (ImageView) findViewById(R.id.main_logo);
                LinearLayout bottom_buttons = (LinearLayout) findViewById(R.id.main_activity_bottom_buttons);
                ImageView assignment_icon = (ImageView) findViewById(R.id.main_activity_assignment_icon);
                ImageView announcement_icon = (ImageView) findViewById(R.id.main_activity_announcement_icon);
                ImageView calendar_icon = (ImageView) findViewById(R.id.main_activity_calendar_icon);
                ImageView class_icon = (ImageView) findViewById(R.id.main_activity_class_icon);

                Animation logo_anim;
                Animation welcome_text_anim;
                Animation bottom_buttons_anim;
                Animation assignment_icon_anim;
                Animation announcement_icon_anim;
                Animation calendar_icon_anim;
                Animation class_icon_anim;

                logo_anim = AnimationUtils.loadAnimation(this, R.anim.main_activity_logo);
                welcome_text_anim = AnimationUtils.loadAnimation(this, R.anim.main_activity_welcome_text);
                bottom_buttons_anim = AnimationUtils.loadAnimation(this, R.anim.main_activity_bottom_buttons);
                assignment_icon_anim = AnimationUtils.loadAnimation(this, R.anim.main_activity_assignment_icon);
                announcement_icon_anim = AnimationUtils.loadAnimation(this, R.anim.main_activity_announcement_icon);
                calendar_icon_anim = AnimationUtils.loadAnimation(this, R.anim.main_activity_calendar_icon);
                class_icon_anim = AnimationUtils.loadAnimation(this, R.anim.main_activity_class_icon);
                logo.startAnimation(logo_anim);
                welcome_text.startAnimation(welcome_text_anim);
                bottom_buttons.startAnimation(bottom_buttons_anim);
                assignment_icon.startAnimation(assignment_icon_anim);
                announcement_icon.startAnimation(announcement_icon_anim);
                calendar_icon.startAnimation(calendar_icon_anim);
                class_icon.startAnimation(class_icon_anim);
            }
        }



    }


    public void openloginpage(View v){
        Intent i = new Intent(this,Login_username.class);
        startActivity(i);
    }

    public void opensignuppage(View v){
        Intent i = new Intent(this,Signup_usertype.class);
        startActivity(i);
    }

    public void show_assignment_icon_toast(View v){
       TextView toast_msg = new TextView(this);
        toast_msg.setTextSize(12);
        toast_msg.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        toast_msg.setText("\uD83D\uDCCB Share assignments with your friends! \uD83E\uDD1C\uD83E\uDD1B");
        Toast message = new Toast(this);
        message.setView(toast_msg);
        message.setDuration(Toast.LENGTH_SHORT);
        message.show();
    }

    public void show_class_icon_toast(View v){
        TextView toast_msg = new TextView(this);
        toast_msg.setTextSize(12);
        toast_msg.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        toast_msg.setText("\uD83D\uDC4F View exam results! \uD83D\uDCAF ");
        Toast message = new Toast(this);
        message.setView(toast_msg);
        message.setDuration(Toast.LENGTH_SHORT);
        message.show();
    }

    public void show_calendar_icon_toast(View v){
        TextView toast_msg = new TextView(this);
        toast_msg.setTextSize(12);
        toast_msg.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        toast_msg.setText("\uD83D\uDE4B Manage class attendance! \uD83D\uDE4B\u200D♂");
        Toast message = new Toast(this);
        message.setView(toast_msg);
        message.setDuration(Toast.LENGTH_SHORT);
        message.show();
    }

    public void show_announcement_icon_toast(View v){
        TextView toast_msg = new TextView(this);
        toast_msg.setTextSize(12);
        toast_msg.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        toast_msg.setText("\uD83D\uDCE3 Receive important notifications! \uD83D\uDCAC");
        Toast message = new Toast(this);
        message.setView(toast_msg);
        message.setDuration(Toast.LENGTH_SHORT);
        message.show();
    }



}


