package in.edu.cpi.cpica;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference myref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        myref = firebaseDatabase.getReference("Users");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Please connect to a database.", Toast.LENGTH_SHORT).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Animation dashboard_bell_icon_anim = AnimationUtils.loadAnimation(this,R.anim.dashboard_bell_icon);
        Animation dashboard_bell_off_icon_anim = AnimationUtils.loadAnimation(this,R.anim.dashboard_bell_off_icon);
        Animation fab_btn_anim = AnimationUtils.loadAnimation(this,R.anim.fab_btn_zoom_in);
        ImageView dashboard_bell_off_icon = (ImageView)findViewById(R.id.dashboard_bell_off_icon);
        ImageView dashboard_bell_icon = (ImageView)findViewById(R.id.dashboard_bell_icon);
        TextView dashboard_no_new_notifications = (TextView)findViewById(R.id.dashboard_no_new_notifications);
        Animation slow_fade_in_anim = AnimationUtils.loadAnimation(this,R.anim.slow_fade_in);


        dashboard_bell_off_icon.startAnimation(dashboard_bell_off_icon_anim);
        dashboard_bell_icon.startAnimation(dashboard_bell_icon_anim);
        fab.startAnimation(fab_btn_anim);
        dashboard_no_new_notifications.startAnimation(slow_fade_in_anim);


    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //String login_username = getIntent().getExtras().getString("login_username");
        //String student_username = getIntent().getExtras().getString("student_username");

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","");
        String first_name = sharedPreferences.getString("first_name","");
        String last_name = sharedPreferences.getString("last_name","");
        TextView drawer_username = (TextView)findViewById(R.id.username);
        TextView username_welcome_text = (TextView)findViewById(R.id.username_welcome_text);
        drawer_username.setText(username);
        if(first_name.length()>0) {
            username_welcome_text.setText(first_name+"\n" + last_name);
        }

        RelativeLayout username_badge = (RelativeLayout)findViewById(R.id.username_badge);

        if(username.contains("CPI")){
            username_badge.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.admin_badge));
        }
        else{
            username_badge.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.verified));
        }


        /*if (student_username==null) {
            drawer_username.setText(username);
        }
        else{
            drawer_username.setText(student_username);
        }*/

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notifications) {
            Toast.makeText(getApplicationContext(),"Please connect to a database.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_assignments) {
            Toast.makeText(getApplicationContext(),"Please connect to a database.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_results) {
            Toast.makeText(getApplicationContext(),"Please connect to a database.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_attendance) {
            Toast.makeText(getApplicationContext(),"Please connect to a database.", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(View v){

        AlertDialog.Builder logout_confirmation = new AlertDialog.Builder(dashboard.this);
        logout_confirmation.setMessage("Are you sure?");
        logout_confirmation.setPositiveButton("LOG OUT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                SharedPreferences sharedPreferences = getSharedPreferences("Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.remove("first_name");
                editor.remove("last_name");
                editor.remove("username");
                editor.remove("password");
                editor.remove("gender");
                editor.remove("email");
                editor.apply();

                Toast.makeText(getApplicationContext(),"You have been logged out successfully.",Toast.LENGTH_SHORT).show();

            }
        });

        logout_confirmation.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
            }
        });

        AlertDialog dialog = logout_confirmation.create();
        dialog.show();


    }

}





