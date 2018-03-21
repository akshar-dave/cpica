package in.edu.cpi.cpica;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.graphics.Typeface.BOLD;
import static android.widget.Toast.makeText;

public class Signup_student_username extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_student_username);
    }

    public void show_username_help(View v){
        AlertDialog.Builder username_help_dialog = new AlertDialog.Builder(this);
        String help_message_text = "• Select your academic year and division from the given options.<br><br>• If there are no divisions, please select Division A.<br><br>• Please make sure your roll no. is correct and does not start with a zero(0).";
        username_help_dialog.setMessage(Html.fromHtml(help_message_text));
        username_help_dialog.setTitle("Signup Help");
        username_help_dialog.setIcon(R.drawable.info);
        username_help_dialog.setPositiveButton("OKAY",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int a) {
                dialog.cancel();

            }
        });
        username_help_dialog.show();
    }

    public void show_student_username(View v){
        RadioButton class_fy = (RadioButton)findViewById(R.id.class_fy);
        RadioButton class_sy = (RadioButton)findViewById(R.id.class_sy);
        RadioButton class_ty = (RadioButton)findViewById(R.id.class_ty);
        RadioButton div_a = (RadioButton)findViewById(R.id.div_a);
        RadioButton div_b = (RadioButton)findViewById(R.id.div_b);
        EditText roll_no = (EditText)findViewById(R.id.roll_no);
        RadioGroup class_group = (RadioGroup) findViewById(R.id.class_group);
        RadioGroup div_group = (RadioGroup) findViewById(R.id.div_group);
        String username_toast = "Your assigned username is: ";
        Boolean error_occurrence=false;
        Vibrator buzzer = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
        FloatingActionButton signup_student_username_next_btn = (FloatingActionButton)findViewById(R.id.signup_student_username_next_btn);
        Animation fab_btn_error_bounce;
        fab_btn_error_bounce= AnimationUtils.loadAnimation(this,R.anim.fab_btn_error_bounce);
        String student_username ="";


        if(div_group.getCheckedRadioButtonId()==-1 || class_group.getCheckedRadioButtonId()==-1 || roll_no.length()<1){
            username_toast="Please fill all details to continue";
            error_occurrence=true;
        }
        else {
            if (class_fy.isChecked() || class_sy.isChecked() || class_ty.isChecked()) {
                if (class_fy.isChecked()) {
                    username_toast = username_toast + "FY";
                    student_username = "FY";
                } else if (class_sy.isChecked()) {
                    username_toast = username_toast + "SY";
                    student_username = "SY";
                } else if (class_ty.isChecked()) {
                    username_toast = username_toast + "TY";
                    student_username = "TY";
                }

            } else{
                username_toast = "Please fill all details to continue";
                error_occurrence=true;
            }

            if (div_a.isChecked() || div_b.isChecked()) {

                if (div_a.isChecked()) {
                    username_toast = username_toast + "A";
                    student_username = student_username + "A";
                } else if (div_b.isChecked()) {
                    username_toast = username_toast + "B";
                    student_username = student_username + "B";
                }
            } else {
                username_toast = "Please fill all details to continue";
                error_occurrence=true;
            }


           if(roll_no.length()>0){
               if(roll_no.getText().toString().equals("0") || roll_no.getText().toString().equals("00") || roll_no.getText().toString().equals("000")){
                   username_toast="Please enter a valid roll number";
                   error_occurrence=true;
               }
               else{
                  if(roll_no.length()>0 && roll_no.getText().toString().substring(0,1).equals("0")){
                      if(roll_no.length()>1 && roll_no.getText().toString().substring(1,2).equals("0")){
                          error_occurrence=false;
                          roll_no.getText().delete(1,2);   //deleting the two zeroes at the beginning
                          roll_no.getText().delete(0,1);
                          username_toast=username_toast+roll_no.getText().toString();
                          student_username = student_username + roll_no.getText().toString();
                      }
                      else{
                          if(roll_no.length()>1 && roll_no.getText().toString().substring(0,1).equals("0")){
                              error_occurrence=false;
                              roll_no.getText().delete(0,1);  //deleting the zero at the beginning
                              username_toast=username_toast+roll_no.getText().toString();
                              student_username = student_username + roll_no.getText().toString();
                          }
                          else{
                              username_toast=username_toast+roll_no.getText().toString();
                              student_username = student_username + roll_no.getText().toString();
                              error_occurrence=false;
                          }
                      }
                  }
                  else{
                          username_toast=username_toast+roll_no.getText().toString();
                        student_username = student_username + roll_no.getText().toString();
                          error_occurrence=false;
                  }
               }
           }
           else{
               username_toast="Please fill all details to continue";
               error_occurrence=true;
           }
        }
        if(error_occurrence==true){
            signup_student_username_next_btn.startAnimation(fab_btn_error_bounce);
            long[] buzz_pattern= {0,30,70,20};
            buzzer.vibrate(buzz_pattern,-1);
            signup_student_username_next_btn.setRippleColor(Color.RED);
            Toast show_message = Toast.makeText(this,username_toast,Toast.LENGTH_SHORT);
            show_message.show();
        }
        else{
            Toast show_message = Toast.makeText(this,username_toast,Toast.LENGTH_LONG);
            show_message.show();
            signup_student_username_next_btn.setRippleColor(Color.WHITE);
            Intent i = new Intent(this,Signup_student_password.class);
            i.putExtra("student_username",student_username);
            startActivity(i);
        }

    }

}
