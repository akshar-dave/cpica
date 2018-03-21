package in.edu.cpi.cpica;

import android.content.Context;
import android.content.Intent;
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

public class Signup_student_password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_student_password);
    }

    public void check_signup_student_password(View v) {
        Boolean error_occurrence =false;
        TextView password_error = (TextView) findViewById(R.id.password_error);
        EditText student_password = (EditText) findViewById(R.id.student_password);
        Animation fab_btn_error_bounce,password_error_fade_anim;
        fab_btn_error_bounce= AnimationUtils.loadAnimation(this,R.anim.fab_btn_error_bounce);
        FloatingActionButton signup_student_next_btn = (FloatingActionButton) findViewById(R.id.signup_student_next_btn);
        password_error_fade_anim=AnimationUtils.loadAnimation(this,R.anim.fade_out_fade_in_using_reverse);
        Vibrator buzzer = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);

        if (student_password.length() < 4 && student_password.length() > 0 ) {
            password_error.setText("Password must be 4 digits long.");
            error_occurrence =true;
        }
        else if (student_password.length() < 1 ) {
            password_error.setText("Please enter a password.");
            error_occurrence =true;
        }
        else {
            error_occurrence =false;

        }

        if(error_occurrence==true){
            long[] buzz_pattern= {0,30,70,20};
            buzzer.vibrate(buzz_pattern,-1);
            password_error.setAlpha(1);
            signup_student_next_btn.setRippleColor(Color.RED);
            signup_student_next_btn.startAnimation(fab_btn_error_bounce);
            password_error.startAnimation(password_error_fade_anim);
        }
        else{
            password_error.setAlpha(0);
            signup_student_next_btn.setRippleColor(Color.WHITE);
            String student_username = getIntent().getExtras().getString("student_username");
            Intent i = new Intent(this, Student_dashboard.class);
            i.putExtra("student_username",student_username);
            startActivity(i);

        }
    }
}
