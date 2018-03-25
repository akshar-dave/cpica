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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup_student_password extends AppCompatActivity {

    FloatingActionButton signup_student_next_btn;
    public FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    public FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    int verifycount;
    AlertDialog.Builder email_verification;
    String student_username;

    ProgressBar signup_student_password_progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_student_password);

        signup_student_password_progressbar = (ProgressBar)findViewById(R.id.signup_student_password_progressbar);

    }

    public void check_signup_student_password(View v) {
        Boolean error_occurrence =false;
        TextView password_error = (TextView) findViewById(R.id.password_error);
        final EditText student_password = (EditText) findViewById(R.id.student_password);
        Animation fab_btn_error_bounce,password_error_fade_anim;
        fab_btn_error_bounce= AnimationUtils.loadAnimation(this,R.anim.fab_btn_error_bounce);
        signup_student_next_btn = (FloatingActionButton) findViewById(R.id.signup_student_next_btn);
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

        if(error_occurrence){
            long[] buzz_pattern= {0,30,70,20};
            buzzer.vibrate(buzz_pattern,-1);
            password_error.setAlpha(1);
            signup_student_next_btn.setRippleColor(Color.RED);
            signup_student_next_btn.startAnimation(fab_btn_error_bounce);
            password_error.startAnimation(password_error_fade_anim);
        }
        else{   //no error occurred.
            password_error.setAlpha(0);
            signup_student_next_btn.setRippleColor(Color.WHITE);
            final String email = getIntent().getExtras().getString("email");
            signup_student_password_progressbar.setAlpha(1);

            firebaseAuth.createUserWithEmailAndPassword(email,student_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        signup_student_next_btn.setEnabled(false);
                        verifycount = 0;

                        email_verification = new AlertDialog.Builder(Signup_student_password.this);
                        email_verification.setTitle("Verify your email");
                        email_verification.setMessage("Please check your inbox for a verification mail and click on the link to complete verification.");
                        email_verification.setIcon(R.drawable.email);
                        email_verification.setCancelable(false);
                        email_verification.setPositiveButton("VERIFY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                signup_student_password_progressbar.setAlpha(1);

                                firebaseAuth.signInWithEmailAndPassword(email,student_password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                        Boolean isVerified = firebaseUser.isEmailVerified();
                                        signup_student_password_progressbar.setAlpha(0);

                                        if(isVerified){
                                            Intent i = new Intent(getApplicationContext(), Student_dashboard.class);
                                            student_username = getIntent().getExtras().getString("student_username");
                                            i.putExtra("student_username",student_username);
                                            finish();
                                            startActivity(i);

                                            DatabaseReference user = firebaseDatabase.getReference("Students");
                                            user.child(student_username).child("Email").setValue(email);
                                            user.child(student_username).child("Password").setValue(student_password.getText().toString());

                                        }
                                        else{
                                            signup_student_password_progressbar.setAlpha(0);
                                            firebaseAuth.signOut();
                                            email_verification.show();
                                            Toast.makeText(getApplicationContext(),"Please verify your email",Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });





                            }
                        });
                        email_verification.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getApplicationContext(),Signup_email.class);
                                finish();
                                firebaseUser.delete();
                                DatabaseReference user = firebaseDatabase.getReference(student_username);
                                user.removeValue();
                                startActivity(i);
                            }
                        });


                        if (verifycount==0){
                            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        verifycount+=1;
                                        email_verification.show();
                                        signup_student_password_progressbar.setAlpha(0);
                                        firebaseAuth.signOut();
                                        Toast.makeText(getApplicationContext(),"Verification email sent to "+firebaseUser.getEmail(),Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Failed to send verification mail. Please try again.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                        else{

                        }


                    }
                    else{
                        signup_student_next_btn.setEnabled(true);
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
