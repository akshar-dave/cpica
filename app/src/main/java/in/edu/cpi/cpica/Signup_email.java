package in.edu.cpi.cpica;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

public class Signup_email extends AppCompatActivity {

    private FloatingActionButton signup_email_nextbtn;
    private TextView signup_email_needhelp_btn;
    private EditText signup_email_box;
    private TextView signup_email_error;
    private Animation fab_btn_error_anim;
    private Vibrator buzzer;
    Boolean error_occurrence = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_email);

        signup_email_nextbtn = (FloatingActionButton)findViewById(R.id.signup_email_nextbtn);
        signup_email_needhelp_btn = (TextView)findViewById(R.id.signup_email_needhelp_btn);
        signup_email_box = (EditText)findViewById(R.id.signup_email_box);
        signup_email_error = (TextView)findViewById(R.id.signup_email_error);
        fab_btn_error_anim = AnimationUtils.loadAnimation(Signup_email.this,R.anim.fab_btn_error_bounce);
        buzzer = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);


        signup_email_nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(signup_email_box.getText().toString().trim())){
                       if(Patterns.EMAIL_ADDRESS.matcher(signup_email_box.getText().toString().trim()).matches()){
                           error_occurrence=false;
                           signup_email_nextbtn.setRippleColor(Color.WHITE);
                       }
                       else{
                           error_occurrence=true;
                           signup_email_error.setText("Invalid email address.");
                       }
                }
                else{
                    error_occurrence=true;
                    signup_email_error.setText("Please enter your email address.");

                }

                if(error_occurrence){
                    signup_email_nextbtn.setRippleColor(Color.RED);
                    signup_email_nextbtn.startAnimation(fab_btn_error_anim);
                    long[] buzz_pattern= {0,30,70,20};
                    buzzer.vibrate(buzz_pattern,-1);
                    signup_email_error.setAlpha(1);
                }
                else if(!error_occurrence){

                    signup_email_error.setAlpha(0);
                    String email=signup_email_box.getText().toString().trim();

                    String usertype_is = getIntent().getExtras().getString("usertype_is");
                    if(usertype_is.equals("student")){
                        Intent i = new Intent(Signup_email.this,Signup_student_username.class);

                        SharedPreferences sharedPreferences = getSharedPreferences("Settings",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("email",email).apply();

                        // i.putExtra("email",email);
                        startActivity(i);
                    }
                    else if(usertype_is.equals("admin")){
                        Intent i = new Intent(Signup_email.this,Signup_admin_username.class);

                        SharedPreferences sharedPreferences = getSharedPreferences("Settings",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("email",email).apply();
                        //i.putExtra("email",email);
                        finish();
                        startActivity(i);
                    }
                }


            }
        });


        signup_email_needhelp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder email_help = new AlertDialog.Builder(Signup_email.this);
                String email_help_msg = "Enter you current, <b>valid</b> email address that you have access to. This may be used later for verification.";
                email_help.setMessage(Html.fromHtml(email_help_msg));
                email_help.setTitle("Signup Help");
                email_help.setIcon(R.drawable.info);
                email_help.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                email_help.show();
            }
        });

    }
}
