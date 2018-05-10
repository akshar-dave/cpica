package in.edu.cpi.cpica;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_username extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference myref;
    EditText login_username;
    TextView username_error;
    ProgressBar username_progressbar;
    String email;
    ValueEventListener check_username_listener;

    @Override
    public void onBackPressed() {
        SharedPreferences sharedPreferences = getSharedPreferences("Settings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email").apply();
        try{
            myref.removeEventListener(check_username_listener);
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_username);


            firebaseAuth = FirebaseAuth.getInstance();



        firebaseDatabase = FirebaseDatabase.getInstance();

        myref = firebaseDatabase.getReference("Users");

        firebaseAuth.signInWithEmailAndPassword("authuser@gmail.com","authpass").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){

                }
            }
        });

        login_username = (EditText) findViewById(R.id.login_username);
        username_error = (TextView) findViewById(R.id.username_error);

        login_username.requestFocus();
        login_username.setSelected(true);

        FloatingActionButton username_next_btn = (FloatingActionButton) findViewById(R.id.username_next_btn);
        Animation fab_zoom_in_anim;
        fab_zoom_in_anim = AnimationUtils.loadAnimation(this, R.anim.fab_btn_zoom_in);
        username_next_btn.startAnimation(fab_zoom_in_anim);
        username_progressbar = (ProgressBar)findViewById(R.id.username_progressbar);


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



        FloatingActionButton username_next_btn = (FloatingActionButton) findViewById(R.id.username_next_btn);
        String tempusername;
        tempusername = login_username.getText().toString().replaceAll(" ","");
        login_username.setText(tempusername.toUpperCase());
        String number_string ="^[1-9]*$";  //string containing numbers ->> 1 to 9
        String number_string_with_zero ="^[0-9]*$";   //string containing numbers ->> 0 to 9
        Boolean error_occurrence;
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

            //i.putExtra("login_username",login_username.getText().toString());
            username_error.setAlpha(0);
            username_next_btn.setRippleColor(Color.WHITE);
            verify_username();
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

    private void verify_username(){

        username_progressbar.setAlpha(1);

        check_username_listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getChildren();
                String username = login_username.getText().toString();
                if(dataSnapshot.child(username).hasChildren()){
                    email = dataSnapshot.child(username).child("Email").getValue().toString();

                    SharedPreferences sharedPreferences = getSharedPreferences("Settings",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email",email);
                    editor.apply();

                    username_progressbar.setAlpha(0);

                    Intent i = new Intent(getApplicationContext(), Login_password.class);
                    startActivity(i);
                    myref.removeEventListener(check_username_listener);
                }
                else{
                    username_error.setText("Please complete the sign up process.");
                    username_error.setAlpha(1);
                    username_progressbar.setAlpha(0);

                    AlertDialog.Builder signup_incomplete_msg = new AlertDialog.Builder(Login_username.this);
                    signup_incomplete_msg.setMessage("Seems like you have not joined yet. Please sign up to continue to the app.");
                    signup_incomplete_msg.setTitle("Haven't joined?");
                    signup_incomplete_msg.setPositiveButton("JOIN", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(),Signup_usertype.class));
                            myref.removeEventListener(check_username_listener);
                            finish();
                        }
                    });
                    signup_incomplete_msg.setCancelable(false);
                    signup_incomplete_msg.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    signup_incomplete_msg.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

       myref.addListenerForSingleValueEvent(check_username_listener);


    }
}