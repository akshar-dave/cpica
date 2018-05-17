package in.edu.cpi.cpica;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_dashboard extends AppCompatActivity {

    Button try_beta_btn;
    EditText setup_profile_firstname,setup_profile_lastname;
    Spinner setup_profile_gender;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myref;
    DatabaseReference settingsref;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String username,first_name,last_name,gender;

    @Override
    public void onBackPressed() {
        // cannot go back. needs to finish entering the details.
        Toast.makeText(getApplicationContext(),"Please complete the sign up process.",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        try_beta_btn = (Button)findViewById(R.id.try_beta_btn);
        setup_profile_firstname = (EditText)findViewById(R.id.setup_profile_firstname);
        setup_profile_lastname = (EditText)findViewById(R.id.setup_profile_lastname);
        setup_profile_gender = (Spinner)findViewById(R.id.setup_profile_gender);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myref = firebaseDatabase.getReference("Users");
        settingsref = firebaseDatabase.getReference("Settings");
        sharedPreferences = getSharedPreferences("Settings",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        username = sharedPreferences.getString("username","");

        first_name = sharedPreferences.getString("first_name","");
        last_name = sharedPreferences.getString("last_name","");
        gender = sharedPreferences.getString("gender","");

        try_beta_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(setup_profile_firstname.getText().toString().trim()) || TextUtils.isEmpty(setup_profile_lastname.getText().toString().trim()) || setup_profile_gender.getSelectedItem().toString().equals("Gender")){
                    Toast.makeText(getApplicationContext(),"Please enter all details to proceed.",Toast.LENGTH_SHORT).show();
                }
                else{
                    editor.putString("first_name",setup_profile_firstname.getText().toString()).apply();
                    editor.putString("last_name",setup_profile_lastname.getText().toString()).apply();
                    editor.putString("gender",setup_profile_gender.getSelectedItem().toString()).apply();
                    editor.putString("notifications_sound_enabled","false").apply();
                    editor.putString("notifications_vibration_enabled","true").apply();
                    
                    myref.child(username).child("First_name").setValue(setup_profile_firstname.getText().toString());
                    myref.child(username).child("Last_name").setValue(setup_profile_lastname.getText().toString());
                    myref.child(username).child("Gender").setValue(setup_profile_gender.getSelectedItem().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent i = new Intent(getApplicationContext(),dashboard.class);

                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(i);
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"There was some error. Please make sure you have an active internet connection and try again.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

    }
}
