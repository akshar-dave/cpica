package in.edu.cpi.cpica;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static in.edu.cpi.cpica.R.id.signup_usertype_needhelp_btn;

public class Signup_usertype extends AppCompatActivity {

    RadioButton student;
    RadioButton admin;
    String usertype_is="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_usertype);

        FloatingActionButton usertype_next_btn = (FloatingActionButton)findViewById(R.id.usertype_next_btn);
        Animation fab_zoom_in_anim;
        fab_zoom_in_anim= AnimationUtils.loadAnimation(this,R.anim.fab_btn_zoom_in);
        usertype_next_btn.startAnimation(fab_zoom_in_anim);
        student = (RadioButton) findViewById(R.id.usertype_student);
        admin = (RadioButton) findViewById(R.id.usertype_admin);


        usertype_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(student.isChecked()){
                    usertype_is="student";
                    Intent i = new Intent(Signup_usertype.this,Signup_email.class);
                    i.putExtra("usertype_is",usertype_is);
                    startActivity(i);
                }
                else if(admin.isChecked()){
                    usertype_is="admin";
                    Intent i = new Intent(Signup_usertype.this,Signup_email.class);
                    i.putExtra("usertype_is",usertype_is);
                    startActivity(i);
                }
            }
        });



    }



    public void show_usertype_help(View v){
        final RadioButton admin = (RadioButton) findViewById(R.id.usertype_admin);
        final EditText enabler_password = new EditText(this);
        enabler_password.setLongClickable(false);
        enabler_password.setPadding(50,25,0,25);
        enabler_password.setHint("••••");
        enabler_password.setInputType(InputType.TYPE_CLASS_NUMBER);
        enabler_password.setBackgroundColor(Color.WHITE);
        enabler_password.setTransformationMethod(PasswordTransformationMethod.getInstance());  // hide the characters
        enabler_password.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(4)});  // set max length to 4

        final AlertDialog.Builder admin_enabler = new AlertDialog.Builder(this);
        admin_enabler.setCancelable(false);
        admin_enabler.setView(enabler_password);
        admin_enabler.setMessage("Enter the verification code you received to enable admin user type for signup");
        admin_enabler.setPositiveButton("VERIFY",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int a) {
                //onCLICK code not written here.
            }
        });
        admin_enabler.setNeutralButton("CANCEL",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int a) {
                dialog.cancel();
            }
        });



        final AlertDialog.Builder more_message = new AlertDialog.Builder(this);
        more_message.setNeutralButton("ENABLE ADMIN USERTYPE",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int a) {
                dialog.cancel();
                final AlertDialog dialog2 =admin_enabler.create();
                dialog2.show();
                dialog2.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Calendar calender = Calendar.getInstance();
                        SimpleDateFormat dateformat1 = new SimpleDateFormat("hhmm");
                        final String current_time12 = dateformat1.format(calender.getTime());
                        SimpleDateFormat dateformat2 = new SimpleDateFormat("HHmm");
                        final String current_time24 = dateformat2.format(calender.getTime());


                        Vibrator buzzer = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        long[] buzz_pattern= {0,30,70,20};
                        Animation verify_btn_error_bounce;
                        verify_btn_error_bounce = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_btn_error_bounce);


                        if(enabler_password.getText().toString().equals(current_time12)||enabler_password.getText().toString().equals(current_time24)){
                            admin.setEnabled(true);
                            admin.setAlpha(1);
                            dialog2.cancel();
                            Toast.makeText(getApplicationContext(),"Admin signup has been enabled.", Toast.LENGTH_SHORT).show();
                        }
                        else if(enabler_password.length()<1){
                            buzzer.vibrate(buzz_pattern,-1);
                            dialog2.getButton(DialogInterface.BUTTON_POSITIVE).startAnimation(verify_btn_error_bounce);
                            Toast.makeText(getApplicationContext(),"Please enter the verification code", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            buzzer.vibrate(buzz_pattern,-1);
                            dialog2.getButton(DialogInterface.BUTTON_POSITIVE).startAnimation(verify_btn_error_bounce);
                            Toast.makeText(getApplicationContext(),"Invalid code. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                enabler_password.hasFocus();
                enabler_password.requestFocus();
            }
        });


        AlertDialog.Builder help_message = new AlertDialog.Builder(this);
        String help_message_text = "Select <b>student</b> if you are a student at CPICA or select <b>admin</b> if you work there.";
        help_message.setMessage(Html.fromHtml(help_message_text));
        help_message.setPositiveButton("OKAY",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int a) {
                   dialog.cancel();

            }
        });
        if(admin.isEnabled()==false){
            help_message.setNeutralButton("MORE",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int b) {
                    dialog.cancel();
                    more_message.show();
                }
            });
        }
        else{

        }
        help_message.show();




    }

}
