package in.edu.cpi.cpica;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Login_password extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    StorageReference photoref;
    DatabaseReference myref;
    EditText login_password;
    TextView password_error;
    FloatingActionButton password_next_btn;
    Vibrator buzzer;
    Animation fab_btn_error_bounce,password_error_fade_anim;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String username,first_name,last_name,phone,user_icon_height,user_icon_width;
    String temp_password;
    ValueEventListener check_pass_listener;
    ProgressDialog loading;

    @Override
    public void onBackPressed() {
        editor.remove("password").apply();
        editor.remove("first_name").apply();
        editor.remove("last_name").apply();
        try {
            myref.removeEventListener(check_pass_listener);
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
        setContentView(R.layout.activity_login_password);


        sharedPreferences = getSharedPreferences("Settings",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        photoref = firebaseStorage.getReference("Users");
        login_password = (EditText) findViewById(R.id.login_password);
        loading = new ProgressDialog(Login_password.this);
        loading.setMessage("Getting you in...");
        loading.setIndeterminate(true);
        loading.setCancelable(false);


        myref = firebaseDatabase.getReference("Users");

        login_password.requestFocus();
        login_password.performClick();

        TextView forgot_password_btn = (TextView)findViewById(R.id.forgot_password_btn);
        forgot_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder password_helper = new AlertDialog.Builder(Login_password.this);
                password_helper.setMessage("Please contact your class mentor to get help with resetting your account password.");
                password_helper.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = password_helper.create();
                dialog.show();
            }
        });

        username = sharedPreferences.getString("username","");

        check_pass_listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getChildren();
                temp_password = dataSnapshot.child(username).child("Password").getValue().toString();
                first_name = dataSnapshot.child(username).child("First_name").getValue().toString();
                last_name = dataSnapshot.child(username).child("Last_name").getValue().toString();

                if(dataSnapshot.child(username).hasChild("Phone")){
                    phone =  dataSnapshot.child(username).child("Phone").getValue().toString();
                }

                if(dataSnapshot.child(username).hasChild("User_icon_height") && dataSnapshot.child(username).hasChild("User_icon_width")){
                    user_icon_height = dataSnapshot.child(username).child("User_icon_height").getValue().toString();
                    user_icon_width = dataSnapshot.child(username).child("User_icon_width").getValue().toString();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
       myref.addListenerForSingleValueEvent(check_pass_listener);

    }

    public void check_login_password(View v){

        password_error = (TextView) findViewById(R.id.password_error);
        password_next_btn = (FloatingActionButton)findViewById(R.id.password_next_btn);
        Boolean error_occurrence;
        fab_btn_error_bounce = AnimationUtils.loadAnimation(this,R.anim.fab_btn_error_bounce);
        password_error_fade_anim=AnimationUtils.loadAnimation(this,R.anim.fade_out_fade_in_using_reverse);
        buzzer = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
        //String login_username = getIntent().getExtras().getString("login_username");



        if (login_password.length() < 1 ) {
            password_error.setText("Please enter your password.");
            error_occurrence =true;
        }
        else {
            error_occurrence =false;
        }

        if(!error_occurrence){

            if(login_password.getText().toString().equals(temp_password)){

                loading.show();

                    photoref.child(username).child("user_icon").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            editor.putString("password",temp_password).apply();
                            editor.putString("first_name",first_name).apply();
                            editor.putString("last_name",last_name).apply();
                            editor.putString("phone",phone).apply();
                            editor.putString("notifications_sound_enabled","false").apply();
                            editor.putString("notifications_vibration_enabled","true").apply();

                            editor.putString("user_icon_height",String.valueOf(user_icon_height)).apply();
                            editor.putString("user_icon_width",String.valueOf(user_icon_width)).apply();

                            editor.putString("user_icon_path",uri.toString()).apply();

                            loading.cancel();

                            Intent i = new Intent(getApplicationContext(), dashboard.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(i);
                            myref.removeEventListener(check_pass_listener);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            editor.putString("password",temp_password).apply();
                            editor.putString("first_name",first_name).apply();
                            editor.putString("last_name",last_name).apply();
                            editor.putString("phone",phone).apply();
                            editor.putString("notifications_sound_enabled","false").apply();
                            editor.putString("notifications_vibration_enabled","true").apply();

                            loading.cancel();

                            Intent i = new Intent(getApplicationContext(), dashboard.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(i);
                            myref.removeEventListener(check_pass_listener);
                            finish();
                        }
                    });

            }
            else{
                password_error.setText("Password is incorrect. Please try again.");
                long[] buzz_pattern= {0,30,70,20};
                buzzer.vibrate(buzz_pattern,-1);
                password_next_btn.startAnimation(fab_btn_error_bounce);
                password_error.startAnimation(password_error_fade_anim);
                password_error.setAlpha(1);
                password_next_btn.setRippleColor(Color.RED);
            }


        }
        else{
            password_error.setText("Password is incorrect. Please try again.");
            long[] buzz_pattern= {0,30,70,20};
            buzzer.vibrate(buzz_pattern,-1);
            password_next_btn.startAnimation(fab_btn_error_bounce);
            password_error.startAnimation(password_error_fade_anim);
            password_error.setAlpha(1);
            password_next_btn.setRippleColor(Color.RED);
        }
    }

}