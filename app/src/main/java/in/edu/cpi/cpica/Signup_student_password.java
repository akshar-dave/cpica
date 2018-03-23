package in.edu.cpi.cpica;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class Signup_student_password extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ProgressBar signup_student_password_progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_student_password);

        firebaseAuth=FirebaseAuth.getInstance();
        signup_student_password_progressbar = (ProgressBar)findViewById(R.id.signup_student_password_progressbar);

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

        if (student_password.length() < 6 && student_password.length() > 0 ) {
            password_error.setText("Password must be 6 digits long.");
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
            String email = getIntent().getExtras().getString("email");
            signup_student_password_progressbar.setAlpha(1);

            firebaseAuth.createUserWithEmailAndPassword(email,student_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent i = new Intent(getApplicationContext(), Student_dashboard.class);
                        String student_username = getIntent().getExtras().getString("student_username");
                        i.putExtra("student_username",student_username);
                        startActivity(i);
                        signup_student_password_progressbar.setAlpha(0);
                    }
                    else{
                        signup_student_password_progressbar.setAlpha(0);
                        AlertDialog.Builder signup_error = new AlertDialog.Builder(Signup_student_password.this);
                        String signup_error_msg="Something went wrong. Here's what you can try:<br><br>• Make sure you have an <b>active internet connection</b>.<br><br>• Verify that <b>Google Play Services</b> is enabled on your device and is up-to-date.<br><br>• Try re-checking the <b>email</b>.<br><br>• The email might already be registered. <b>Try logging in</b>.";
                        signup_error.setTitle("Oops!");
                        signup_error.setMessage(Html.fromHtml(signup_error_msg));
                        signup_error.setIcon(R.drawable.error);
                        signup_error.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        signup_error.show();
                    }
                }
            });



        }
    }

}
