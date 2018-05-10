package in.edu.cpi.cpica;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
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
import android.widget.ProgressBar;
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
    RelativeLayout no_new_notifications_container,dashboard_content;
    String notification_text,notification_title,notification_color,sending_to;
    ProgressBar dashboard_progressbar;
    Animation dashboard_bell_icon_anim,dashboard_bell_off_icon_anim,fab_btn_anim,slow_fade_in_anim,slow_fade_out_anim;
    ImageView dashboard_bell_off_icon,dashboard_bell_icon;
    TextView dashboard_no_new_notifications;
    NotificationCompat.Builder notify1;
    Integer notification_count;
    Integer read_notification_count=0;
    Integer i;
    String username;
    ValueEventListener valuelistener1;
    ConnectivityManager connectivityManager;
    Boolean connected_to_internet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //SystemClock.sleep(1000);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        is_fab_open = false;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        dashboard_content = (RelativeLayout)findViewById(R.id.dashboard_content);
        no_new_notifications_container = (RelativeLayout)findViewById(R.id.no_new_notifications_container);
        dashboard_progressbar = (ProgressBar)findViewById(R.id.dashboard_progressbar);
        dashboard_progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#3f51b5"), PorterDuff.Mode.SRC_IN);

        dashboard_bell_icon_anim = AnimationUtils.loadAnimation(this,R.anim.dashboard_bell_icon);
        dashboard_bell_off_icon_anim = AnimationUtils.loadAnimation(this,R.anim.dashboard_bell_off_icon);
        fab_btn_anim = AnimationUtils.loadAnimation(this,R.anim.fab_btn_zoom_in);
        dashboard_bell_off_icon = (ImageView)findViewById(R.id.dashboard_bell_off_icon);
        dashboard_bell_icon = (ImageView)findViewById(R.id.dashboard_bell_icon);
        dashboard_no_new_notifications = (TextView)findViewById(R.id.dashboard_no_new_notifications);
        slow_fade_in_anim = AnimationUtils.loadAnimation(this,R.anim.slow_fade_in);
        slow_fade_out_anim = AnimationUtils.loadAnimation(this,R.anim.slow_fade_out);
        notify1 = new NotificationCompat.Builder(getApplicationContext());
        notify1.setAutoCancel(true);
        notify1.setSmallIcon(R.drawable.logo);
        notify1.setVibrate(new long[] {50});
        username = sharedPreferences.getString("username","");
        connected_to_internet = false;
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        myref = firebaseDatabase.getReference("Users");
        notificationsref = firebaseDatabase.getReference("Notifications");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //this will decide whether to remove the fab on start or not
        if(username.contains("CPI")){
            fab.setVisibility(View.VISIBLE);
            fab.startAnimation(fab_btn_anim);
        }
        else{
            RelativeLayout dashboard_content = (RelativeLayout)findViewById(R.id.dashboard_content);
            dashboard_content.removeView(fab);
        }
        //----------------------------------------------------------

        dashboard_content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                notificationsref.removeEventListener(valuelistener1);
                startActivity(new Intent(getApplicationContext(),Overview_notification.class));
                finish();

                return true;
            }
        });

        valuelistener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getChildren();
                //notification code goes here----------------------
                String notification_count_string = Long.toString(dataSnapshot.getChildrenCount());
                notification_count = Integer.parseInt(notification_count_string);

                try{
                    for(i=1;i<=notification_count;i++){
                        sending_to = dataSnapshot.child(i.toString()).child("sending_to").getValue().toString();
                        notification_text = dataSnapshot.child(i.toString()).child("notification_text").getValue().toString();
                        notification_title = dataSnapshot.child(i.toString()).child("notification_title").getValue().toString();

                        if(dataSnapshot.child(i.toString()).child("read_by").hasChild(username)){
                            read_notification_count+=1;
                        }
                        else{ //this means the notification isn't read by this USERNAME.
                            if(sending_to!=null && notification_title!=null && notification_text!=null){
                                if(sending_to.equals("EVERYONE")){
                                    notify1.setContentTitle(notification_title);
                                    notify1.setContentText(notification_text);


                                    Intent open_activity_intent = new Intent(getApplicationContext(), Detailed_notification.class);

                                    open_activity_intent.putExtra("notification_id", i.toString());
                                    open_activity_intent.putExtra("notification_title",notification_title);
                                    open_activity_intent.putExtra("notification_text",notification_text);
                                    open_activity_intent.putExtra("notification_color",notification_color);

                                    PendingIntent open_activity_pi = PendingIntent.getActivity(getApplicationContext(), i, open_activity_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    notify1.setContentIntent(open_activity_pi);
                                    nm.notify(i, notify1.build());
                                }
                                else{
                                    if(username.contains("CPI")){
                                        if(sending_to.equals("ADMINS")){
                                            notify1.setContentTitle(notification_title);
                                            notify1.setContentText(notification_text);


                                            Intent open_activity_intent = new Intent(getApplicationContext(), Detailed_notification.class);

                                            open_activity_intent.putExtra("notification_id", i.toString());
                                            open_activity_intent.putExtra("notification_title",notification_title);
                                            open_activity_intent.putExtra("notification_text",notification_text);
                                            open_activity_intent.putExtra("notification_color",notification_color);

                                            PendingIntent open_activity_pi = PendingIntent.getActivity(getApplicationContext(), i, open_activity_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            notify1.setContentIntent(open_activity_pi);
                                            nm.notify(i, notify1.build());
                                        }
                                        else{
                                            read_notification_count += 1;
                                        }
                                    }
                                    else{ //if username isn't containing CPI---
                                        if(username.contains("FYA") || username.contains("FYB") || username.contains("SYA") || username.contains("SYB")  || username.contains("TYA") || username.contains("TYB")){
                                            if(sending_to.equals("STUDENTS")){
                                                notify1.setContentTitle(notification_title);
                                                notify1.setContentText(notification_text);


                                                Intent open_activity_intent = new Intent(getApplicationContext(), Detailed_notification.class);

                                                open_activity_intent.putExtra("notification_id", i.toString());
                                                open_activity_intent.putExtra("notification_title",notification_title);
                                                open_activity_intent.putExtra("notification_text",notification_text);
                                                open_activity_intent.putExtra("notification_color",notification_color);

                                                PendingIntent open_activity_pi = PendingIntent.getActivity(getApplicationContext(),i, open_activity_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                                notify1.setContentIntent(open_activity_pi);
                                                nm.notify(i, notify1.build());
                                            }
                                            else{
                                                read_notification_count += 1;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                    finish();
                }

                if(read_notification_count.equals(notification_count)){ // means there are no new notifications.
                    dashboard_progressbar.startAnimation(slow_fade_out_anim);
                    no_new_notifications_container.setVisibility(View.VISIBLE);

                    dashboard_bell_off_icon.startAnimation(dashboard_bell_off_icon_anim);
                    dashboard_bell_icon.startAnimation(dashboard_bell_icon_anim);
                    dashboard_no_new_notifications.setText("No new notifications.");
                    dashboard_no_new_notifications.startAnimation(slow_fade_in_anim);
                }
                else{
                    dashboard_progressbar.startAnimation(slow_fade_out_anim);
                    no_new_notifications_container.setVisibility(View.VISIBLE);
                    dashboard_bell_icon.setVisibility(View.VISIBLE);
                    dashboard_bell_off_icon.setVisibility(View.INVISIBLE);
                    dashboard_bell_off_icon_anim.cancel();
                    dashboard_bell_icon_anim.cancel();

                    if(notification_count-read_notification_count<0){
                        finish();
                         }
                    else{
                        if((notification_count-read_notification_count)==1){
                            dashboard_no_new_notifications.setText((notification_count-read_notification_count)+" unread notification.");
                        }
                        else{
                            dashboard_no_new_notifications.setText((notification_count-read_notification_count)+" unread notifications.");
                        }
                    }

                    dashboard_no_new_notifications.startAnimation(slow_fade_in_anim);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        notificationsref.addValueEventListener(valuelistener1);


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
            notificationsref.removeEventListener(valuelistener1);
            startActivity(new Intent(getApplicationContext(),Overview_notification.class));
            finish();
            //Toast.makeText(getApplicationContext(),"Please connect to a database.", Toast.LENGTH_SHORT).show();
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
                nm.cancelAll();
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

            mark_attendance_fab.setClickable(true);
            add_results_fab.setClickable(true);
            new_announcement_fab.setClickable(true);
            mark_attendance_fab_label.setClickable(true);
            add_results_fab_label.setClickable(true);
            new_announcement_fab_label.setClickable(true);
            fab_shade.setClickable(true);

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

            mark_attendance_fab.setClickable(false);
            add_results_fab.setClickable(false);
            new_announcement_fab.setClickable(false);
            mark_attendance_fab_label.setClickable(false);
            add_results_fab_label.setClickable(false);
            new_announcement_fab_label.setClickable(false);
            fab_shade.setClickable(false);

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
                    mark_attendance_fab.setClickable(false);
                    add_results_fab.setClickable(false);
                    new_announcement_fab.setClickable(false);
                    mark_attendance_fab_label.setClickable(false);
                    add_results_fab_label.setClickable(false);
                    new_announcement_fab_label.setClickable(false);
                    fab_shade.setClickable(false);

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
        notificationsref.removeEventListener(valuelistener1);
        Intent i = new Intent(getApplicationContext(),New_notification.class);
        startActivity(i);
        finish();
    }

    public void mark_attendance_fab_onclick(View v){
        //this handles click events for the MARK ATTENDANCE BUTTON on dashboard
    }

    public void add_results_fab_onclick(View v){
        //this handles click events for the ADD RESULTS BUTTON on dashboard
    }

}