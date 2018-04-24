package in.edu.cpi.cpica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_admin_password extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myref;
    DatabaseReference settingsref;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Integer admin_username_int;
    String username;
    EditText admin_password;

    @Override
    public void onBackPressed() {
        editor.remove("username").apply();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_admin_password);


        admin_password = (EditText) findViewById(R.id.admin_password);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myref = firebaseDatabase.getReference("Users");
        settingsref = firebaseDatabase.getReference("Settings");
        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        username = sharedPreferences.getString("username","");
        admin_username_int = sharedPreferences.getInt("admin_username_int",0);

    }

    public void check_signup_admin_password(View v) {

        TextView password_error = (TextView) findViewById(R.id.password_error);
        FloatingActionButton signup_admin_next_btn = (FloatingActionButton)findViewById(R.id.signup_admin_next_btn);
        Animation signup_admin_next_btn_error_bounce, password_error_fade_anim;
        signup_admin_next_btn_error_bounce = AnimationUtils.loadAnimation(this,R.anim.fab_btn_error_bounce);
        password_error_fade_anim=AnimationUtils.loadAnimation(this,R.anim.fade_out_fade_in_using_reverse);
        Vibrator buzzer = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
        Boolean error_occurrence;

        if (admin_password.length() < 6 && admin_password.length() > 0 ) {
            password_error.setText("Password must be 6 digits long.");
            error_occurrence =true;
        }
        else if (admin_password.length() < 1 ) {
            password_error.setText("Please enter a password.");
            error_occurrence =true;
        }
        else {
            error_occurrence =false;

        }

        if(error_occurrence){
            password_error.setAlpha(1);
            signup_admin_next_btn.startAnimation(signup_admin_next_btn_error_bounce);
            password_error.startAnimation(password_error_fade_anim);
            long[] buzz_pattern= {0,30,70,20};
            buzzer.vibrate(buzz_pattern,-1);
            signup_admin_next_btn.setRippleColor(Color.RED);
        }
        else{

            editor.putString("password",admin_password.getText().toString()).apply();

            password_error.setAlpha(0);
            signup_admin_next_btn.setRippleColor(Color.WHITE);

            admin_username_int = admin_username_int + 1;
            settingsref.child("admin_username_int").setValue(admin_username_int);

            editor.remove("admin_username_int").apply();

            myref.child(username).child("Email").setValue(sharedPreferences.getString("email",""));
            myref.child(username).child("Password").setValue(sharedPreferences.getString("password",""));
            myref.child(username).child("User_type").setValue("ADMIN");

            Intent i = new Intent(getApplicationContext(), Admin_dashboard.class);
            startActivity(i);
        }
    }
}