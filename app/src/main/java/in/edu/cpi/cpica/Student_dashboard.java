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
import android.widget.Spinner;

import com.google.firebase.database.FirebaseDatabase;

public class Student_dashboard extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);


        Button try_beta_btn = (Button)findViewById(R.id.try_beta_btn);
        try_beta_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
