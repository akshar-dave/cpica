package in.edu.cpi.cpica;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

            student_username = getIntent().getExtras().getString("student_username");
            DatabaseReference user = firebaseDatabase.getReference("Students");
            user.child(student_username).child("First Name").setValue(setup_profile_firstname.getText().toString());
            user.child(student_username).child("Last Name").setValue(setup_profile_lastname.getText().toString());
            user.child(student_username).child("Gender").setValue(setup_profile_gender.getSelectedItem().toString());


            String login_username = getIntent().getExtras().getString("login_username");
            String student_username = getIntent().getExtras().getString("student_username");
            Intent i = new Intent(getApplicationContext(),dashboard.class);
            i.putExtra("login_username",login_username);
            i.putExtra("student_username",student_username);
            startActivity(i);
        }
    });

    }


}
