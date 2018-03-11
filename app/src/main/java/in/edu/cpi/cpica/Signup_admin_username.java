package in.edu.cpi.cpica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Signup_admin_username extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_admin_username);
    }

    public void next(View v){
        Intent i = new Intent(this,Signup_admin_password.class);
        startActivity(i);
    }
}
