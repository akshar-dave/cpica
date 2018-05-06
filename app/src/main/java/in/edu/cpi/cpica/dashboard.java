package in.edu.cpi.cpica;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    Boolean is_fab_open;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference myref;
    DatabaseReference notificationsref;
    NotificationManager nm;
    SharedPreferences sharedPreferences;
    String notification_text,notification_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        is_fab_open = false;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        myref = firebaseDatabase.getReference("Users");
        notificationsref = firebaseDatabase.getReference("Notifications");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);


        notificationsref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String notification_count_string = Long.toString(dataSnapshot.getChildrenCount());
                Integer notification_count = Integer.parseInt(notification_count_string);

                Integer i;
                for (i=1;i<=notification_count;i++){
                    if(dataSnapshot.child(i.toString()).child("read_by").hasChild(sharedPreferences.getString("username",""))){
                        Toast.makeText(getApplicationContext(),"Notification "+i.toString()+" is read.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //Toast.makeText(getApplicationContext(),"Notification "+i.toString()+" was not read.",Toast.LENGTH_SHORT).show();

                            dataSnapshot.getChildren();

                            notification_text = dataSnapshot.child(i.toString()).child("notification_text").getValue().toString();
                            notification_title = dataSnapshot.child(i.toString()).child("notification_title").getValue().toString();
                            NotificationCompat.Builder notify1 = new NotificationCompat.Builder(getApplicationContext());
                            notify1.setAutoCancel(true);
                            notify1.setContentTitle(dataSnapshot.child(i.toString()).child("notification_title").getValue().toString());
                            notify1.setContentText(dataSnapshot.child(i.toString()).child("notification_text").getValue().toString());
                            notify1.setSmallIcon(R.drawable.logo);
                            if(dataSnapshot.child(i.toString()).child("is_important").getValue().toString().equals("true")){
                                notify1.setPriority(Notification.PRIORITY_MAX);
                            }

                            Intent open_activity_intent = new Intent(getApplicationContext(),Detailed_notification.class);
                            Intent mark_as_read_intent = new Intent(getApplicationContext(),Detailed_notification.class);

                            open_activity_intent.putExtra("notification_id",i.toString());
                            PendingIntent open_activity_pi = PendingIntent.getActivity(getApplicationContext(),0,open_activity_intent,PendingIntent.FLAG_UPDATE_CURRENT);
                            notify1.setContentIntent(open_activity_pi);

                            nm.notify(i,notify1.build());


                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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



        //this will decide whether to remove the fab on start or not
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","");
        if(username.contains("CPI")){
            fab.setVisibility(View.VISIBLE);
        }
        else{
            RelativeLayout dashboard_content = (RelativeLayout)findViewById(R.id.dashboard_content);
            dashboard_content.removeView(fab);
        }



    }

public void mark_as_read(){

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
                finish();
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

    public void fab_onclick(View view){


        final FloatingActionButton mark_attendance_fab,add_results_fab,new_announcement_fab;
        final RelativeLayout fab_shade;
        final TextView mark_attendance_fab_label,add_results_fab_label,new_announcement_fab_label;
        final Animation fab_shade_show_anim,fab_shade_hide_anim,fab_open_anim,fab_close_anim,fab_btn_zoom_in_anim,fab_btn_zoom_out_anim,fab_btn_label_zoom_in_anim,fab_btn_label_zoom_out_anim;

        mark_attendance_fab = (FloatingActionButton)findViewById(R.id.mark_attendance_fab);
        add_results_fab = (FloatingActionButton)findViewById(R.id.add_results_fab);
        new_announcement_fab = (FloatingActionButton)findViewById(R.id.new_announcement_fab);
        fab_shade = (RelativeLayout)findViewById(R.id.fab_shade);
        mark_attendance_fab_label = (TextView)findViewById(R.id.mark_attendance_fab_label);
        add_results_fab_label = (TextView)findViewById(R.id.add_results_fab_label);
        new_announcement_fab_label = (TextView)findViewById(R.id.new_announcement_fab_label);
        fab_shade_show_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_shade_show);
        fab_shade_hide_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_shade_hide);
        fab_open_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fab_close_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        fab_btn_zoom_in_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_btn_zoom_in);
        fab_btn_zoom_out_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_btn_zoom_out);
        fab_btn_label_zoom_in_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_btn_label_zoom_in);
        fab_btn_label_zoom_out_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_btn_label_zoom_out);



        if(!is_fab_open){

            mark_attendance_fab.setVisibility(View.VISIBLE);
            add_results_fab.setVisibility(View.VISIBLE);
            new_announcement_fab.setVisibility(View.VISIBLE);
            mark_attendance_fab_label.setVisibility(View.VISIBLE);
            add_results_fab_label.setVisibility(View.VISIBLE);
            new_announcement_fab_label.setVisibility(View.VISIBLE);
            fab_shade.setVisibility(View.VISIBLE);

            mark_attendance_fab.startAnimation(fab_btn_zoom_in_anim);
            add_results_fab.startAnimation(fab_btn_zoom_in_anim);
            new_announcement_fab.startAnimation(fab_btn_zoom_in_anim);
            mark_attendance_fab_label.startAnimation(fab_btn_label_zoom_in_anim);
            add_results_fab_label.startAnimation(fab_btn_label_zoom_in_anim);
            new_announcement_fab_label.startAnimation(fab_btn_label_zoom_in_anim);
            fab_shade.startAnimation(fab_shade_show_anim);
            fab.startAnimation(fab_open_anim);

            is_fab_open=true; //fab is now open
        }
        else if(is_fab_open){


            mark_attendance_fab.startAnimation(fab_btn_zoom_out_anim);
            add_results_fab.startAnimation(fab_btn_zoom_out_anim);
            new_announcement_fab.startAnimation(fab_btn_zoom_out_anim);
            mark_attendance_fab_label.startAnimation(fab_btn_label_zoom_out_anim);
            add_results_fab_label.startAnimation(fab_btn_label_zoom_out_anim);
            new_announcement_fab_label.startAnimation(fab_btn_label_zoom_out_anim);
            fab_shade.startAnimation(fab_shade_hide_anim);
            fab.startAnimation(fab_close_anim);

            mark_attendance_fab.setVisibility(View.GONE);
            add_results_fab.setVisibility(View.GONE);
            new_announcement_fab.setVisibility(View.GONE);
            mark_attendance_fab_label.setVisibility(View.GONE);
            add_results_fab_label.setVisibility(View.GONE);
            new_announcement_fab_label.setVisibility(View.GONE);
            fab_shade.setVisibility(View.GONE);

            is_fab_open=false; //fab has been closed
        }

        fab_shade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_fab_open){
                    mark_attendance_fab.startAnimation(fab_btn_zoom_out_anim);
                    add_results_fab.startAnimation(fab_btn_zoom_out_anim);
                    new_announcement_fab.startAnimation(fab_btn_zoom_out_anim);
                    mark_attendance_fab_label.startAnimation(fab_btn_label_zoom_out_anim);
                    add_results_fab_label.startAnimation(fab_btn_label_zoom_out_anim);
                    new_announcement_fab_label.startAnimation(fab_btn_label_zoom_out_anim);
                    fab_shade.startAnimation(fab_shade_hide_anim);
                    fab.startAnimation(fab_close_anim);

                    mark_attendance_fab.setVisibility(View.GONE);
                    add_results_fab.setVisibility(View.GONE);
                    new_announcement_fab.setVisibility(View.GONE);
                    mark_attendance_fab_label.setVisibility(View.GONE);
                    add_results_fab_label.setVisibility(View.GONE);
                    new_announcement_fab_label.setVisibility(View.GONE);
                    fab_shade.setVisibility(View.GONE);

                    is_fab_open=false; //fab has been closed
                }

            }
        });

    }

    public void new_announcement_fab_onclick(View v){
        //this handles click events for the NEW ANNOUNCEMENT BUTTON on dashboard
        Intent i = new Intent(getApplicationContext(),New_notification.class);
        startActivity(i);
    }

    public void mark_attendance_fab_onclick(View v){
        //this handles click events for the MARK ATTENDANCE BUTTON on dashboard
    }

    public void add_results_fab_onclick(View v){
        //this handles click events for the ADD RESULTS BUTTON on dashboard
    }

}





