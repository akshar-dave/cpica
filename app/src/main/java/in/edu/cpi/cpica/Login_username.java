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
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Login_username extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_username);




        FloatingActionButton username_next_btn = (FloatingActionButton) findViewById(R.id.username_next_btn);
        Animation fab_zoom_in_anim;
        fab_zoom_in_anim = AnimationUtils.loadAnimation(this, R.anim.fab_btn_zoom_in);
        username_next_btn.startAnimation(fab_zoom_in_anim);

        TextView forgot_username_btn = (TextView)findViewById(R.id.forgot_username_btn);
        forgot_username_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder username_helper = new AlertDialog.Builder(Login_username.this);
                View helper_layout = getLayoutInflater().inflate(R.layout.login_username_helper_layout,null);
                final Spinner login_username_helper_class = (Spinner) helper_layout.findViewById(R.id.login_username_helper_class);
                final Spinner login_username_helper_division = (Spinner) helper_layout.findViewById(R.id.login_username_helper_division);
                final EditText login_username_helper_roll_no = (EditText) helper_layout.findViewById(R.id.login_username_helper_roll_no);
                final EditText login_username = (EditText) findViewById(R.id.login_username);


                username_helper.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //no code for onCLICK here.
                    }
                });
                username_helper.setNeutralButton("BACK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                username_helper.setTitle("Enter you class details");
                username_helper.setCancelable(false);
                username_helper.setView(helper_layout);
                final AlertDialog dialog = username_helper.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String helper_username_string="";
                        Animation next_btn_error_bounce;
                        next_btn_error_bounce = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_btn_error_bounce);

                        if(login_username_helper_class.getSelectedItem().toString().equals("-") ||
                                login_username_helper_division.getSelectedItem().toString().equals("-") ||
                                login_username_helper_roll_no.length()<1 ||
                                login_username_helper_roll_no.getText().toString().equals("000") ||
                                login_username_helper_roll_no.getText().toString().equals("00") ||
                                login_username_helper_roll_no.getText().toString().equals("0")){

                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).startAnimation(next_btn_error_bounce);

                            Vibrator buzzer = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            long[] buzz_pattern= {0,30,70,20};
                            buzzer.vibrate(buzz_pattern,-1);

                            Toast.makeText(getApplicationContext(),"Please enter all details correctly.",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(login_username_helper_roll_no.length()>0 && login_username_helper_roll_no.getText().toString().substring(0,1).equals("0")){
                                if(login_username_helper_roll_no.length()>1 && login_username_helper_roll_no.getText().toString().substring(1,2).equals("0")){
                                    login_username_helper_roll_no.getText().delete(1,2);   //deleting the two zeroes at the beginning
                                    login_username_helper_roll_no.getText().delete(0,1);
                                }
                                else{
                                    if(login_username_helper_roll_no.length()>1 && login_username_helper_roll_no.getText().toString().substring(0,1).equals("0")){
                                        login_username_helper_roll_no.getText().delete(0,1);  //deleting the zero at the beginning
                                    }
                                }
                            }

                            helper_username_string+=login_username_helper_class.getSelectedItem().toString();
                            helper_username_string+=login_username_helper_division.getSelectedItem().toString();
                            helper_username_string+=login_username_helper_roll_no.getText().toString();
                            login_username.setText("");
                            login_username.setText(helper_username_string);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });


    }

    public void open_password_page(View v) {

        TextView username_error = (TextView) findViewById(R.id.username_error);
        EditText login_username = (EditText) findViewById(R.id.login_username);
        FloatingActionButton username_next_btn = (FloatingActionButton) findViewById(R.id.username_next_btn);
        String username;
        username = login_username.getText().toString().replaceAll(" ","");
        login_username.setText(username.toUpperCase());
        String number_string ="^[1-9]*$";  //string containing numbers ->> 1 to 9
        String number_string_with_zero ="^[0-9]*$";   //string containing numbers ->> 0 to 9
        Boolean error_occurrence =false;
        Animation fab_btn_error_bounce,username_error_fade_anim;
        fab_btn_error_bounce = AnimationUtils.loadAnimation(this,R.anim.fab_btn_error_bounce);
        username_error_fade_anim=AnimationUtils.loadAnimation(this,R.anim.fade_out_fade_in_using_reverse);
        Vibrator buzzer = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        if (login_username.length() == 0) {
            error_occurrence =true;
            username_error.setText("Please enter your username."); // BECAUSE the username isn't entered.
        } else {
            if (login_username.length() <= 3) {  //BECAUSE the username is shorter than 4 chars.
                error_occurrence =true;
                username_error.setText("Username is invalid."); // BECAUSE the length is shorter than 4.
            }
            else{
                if(login_username.getText().toString().substring(0,3).contentEquals("CPI") || login_username.getText().toString().substring(0,3).contentEquals("FYA") || login_username.getText().toString().substring(0,3).contentEquals("FYB") || login_username.getText().toString().substring(0,3).contentEquals("SYA") || login_username.getText().toString().substring(0,3).contentEquals("SYB") || login_username.getText().toString().substring(0,3).contentEquals("TYA") || login_username.getText().toString().substring(0,3).contentEquals("TYB")) {
                    if(login_username.getText().toString().substring(3,4).equals("0")) {
                        if(login_username.length()>4 && login_username.getText().toString().substring(4,5).equals("0")){
                            if (login_username.length()>5 && login_username.getText().toString().substring(5,6).equals("0")){
                                username_error.setText("Username is invalid.\n\n• Verify that the roll number is correct.");
                                error_occurrence =true;
                            }
                            else{
                                username_error.setText("Username is invalid.\n\n• Try removing any zeroes(0) before your roll number.\nOR\n• Verify that the roll number is correct.");
                                error_occurrence =true;
                            }
                        }
                        else{
                            username_error.setText("Username is invalid.\n\n• Try removing any zeroes(0) before your roll number.\nOR\n• Verify that the roll number is correct.");
                            error_occurrence =true;
                        }
                    }
                    else{ //VALIDATE IF ROLL NUMBER PART CONTAINS NUMBERS

                        if(login_username.length()==6){ //6th char == number?
                            if(login_username.getText().toString().substring(5,6).matches(number_string_with_zero)){
                                if(login_username.getText().toString().substring(4,5).matches(number_string_with_zero)){
                                    if(login_username.getText().toString().substring(3,4).matches(number_string)){
                                        error_occurrence =false;
                                    }
                                    else{
                                        username_error.setText("Username is invalid.\n\n• Make sure that the username contains only alphanumeric characters.");
                                        error_occurrence =true;
                                    }
                                }
                                else{
                                    username_error.setText("Username is invalid.\n\n• Make sure that the username contains only alphanumeric characters.");
                                    username_error.setAlpha(1);
                                    error_occurrence =true;
                                }
                            }
                            else{
                                username_error.setText("Username is invalid.\n\n• Make sure that the username contains only alphanumeric characters.");
                                error_occurrence =true;
                            }
                        }
                        else if(login_username.length()==5){ //5th char == number?
                            if(login_username.getText().toString().substring(4,5).matches(number_string_with_zero)){
                                if(login_username.getText().toString().substring(3,4).matches(number_string)){
                                    error_occurrence =false;
                                }
                                else{
                                    username_error.setText("Username is invalid.\n\n• Make sure that the username contains only alphanumeric characters.");
                                    error_occurrence =true;
                                }
                            }
                            else{
                                username_error.setText("Username is invalid.\n\n• Make sure that the username contains only alphanumeric characters.");
                                error_occurrence =true;
                            }
                        }
                        else if(login_username.length()==4){ //4th char == number?
                            if(login_username.getText().toString().substring(3,4).matches(number_string)){
                                error_occurrence =false;
                            }
                            else{
                                username_error.setText("Username is invalid.\n\n• Make sure that the username contains only alphanumeric characters.");
                                error_occurrence =true;
                            }
                        }
                        else{
                            username_error.setText("Username is invalid.");
                            error_occurrence =true;
                        }

                    }
                }
                else{
                    username_error.setText("Username is invalid."); // BECAUSE the first 3 letters are invalid.
                    error_occurrence =true;
                }
            }


        }

        //username text box validation ends here--
        if (!error_occurrence){  //there was no error
            Intent i = new Intent(this, Login_password.class);
            i.putExtra("login_username",login_username.getText().toString());
            startActivity(i);
            username_error.setAlpha(0);
            username_next_btn.setRippleColor(Color.WHITE);
        }
        else{ // there was some error
            long[] buzz_pattern= {0,30,70,20};
            buzzer.vibrate(buzz_pattern,-1);
            username_next_btn.startAnimation(fab_btn_error_bounce);
            username_error.startAnimation(username_error_fade_anim);
            username_error.setAlpha(1);
            username_next_btn.setRippleColor(Color.RED);
        }
        SharedPreferences sharedPreferences = getSharedPreferences("Settings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username",login_username.getText().toString());
        editor.apply();
    }
}
