package in.edu.cpi.cpica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Student_dashboard extends AppCompatActivity {

    EditText setup_profile_firstname;
    EditText setup_profile_lastname;
    FirebaseDatabase firebaseDatabase;
    Spinner setup_profile_gender;
    String student_username;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        setup_profile_gender = (Spinner)findViewById(R.id.setup_profile_gender);
        setup_profile_lastname=(EditText)findViewById(R.id.setup_profile_lastname);
        setup_profile_firstname=(EditText)findViewById(R.id.setup_profile_firstname);
        firebaseDatabase = new Signup_student_password().firebaseDatabase;


        Button try_beta_btn = (Button)findViewById(R.id.try_beta_btn);
        try_beta_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            if(TextUtils.isEmpty(setup_profile_firstname.getText().toString().trim()) || TextUtils.isEmpty(setup_profile_lastname.getText().toString().trim()) || setup_profile_gender.getSelectedItem().toString().equals("Gender")){
                Toast.makeText(getApplicationContext(),"Please enter all details to proceed.",Toast.LENGTH_SHORT).show();
            }
            else{
                SharedPreferences sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //student_username = getIntent().getExtras().getString("student_username");
                String username = sharedPreferences.getString("username","");
                editor.putString("first_name",setup_profile_firstname.getText().toString()).apply();
                editor.putString("last_name",setup_profile_lastname.getText().toString()).apply();
                editor.putString("gender",setup_profile_gender.getSelectedItem().toString()).apply();
                String first_name = sharedPreferences.getString("first_name","");
                String last_name = sharedPreferences.getString("last_name","");
                String gender = sharedPreferences.getString("gender","");

                DatabaseReference user = firebaseDatabase.getReference("Users");
                user.child(username).child("First_name").setValue(first_name);
                user.child(username).child("Last_name").setValue(last_name);
                user.child(username).child("Gender").setValue(gender);


                //String login_username = getIntent().getExtras().getString("login_username");
                //String student_username = getIntent().getExtras().getString("student_username");
                Intent i = new Intent(getApplicationContext(),dashboard.class);

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //i.putExtra("login_username",login_username);
                //i.putExtra("student_username",student_username);
                startActivity(i);
                finish();
            }


        }
    });

    }

    @Override
    public void onBackPressed() {
        Vibrator buzzer = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
        long[] buzz_pattern= {0,30,70,20};
        buzzer.vibrate(buzz_pattern,-1);
    }
}
