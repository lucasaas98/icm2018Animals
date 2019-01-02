package ua.pt.naturespot.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ua.pt.naturespot.Fragment.Fragment_AddSightings;
import ua.pt.naturespot.Fragment.Fragment_Explore;
import ua.pt.naturespot.Fragment.Fragment_ModeratorOptions;
import ua.pt.naturespot.Fragment.Fragment_MyIdentifications;
import ua.pt.naturespot.Fragment.Fragment_MySightings;
import ua.pt.naturespot.Fragment.Fragment_Profile;
import ua.pt.naturespot.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    private String mPhotoUrl;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Drawable d = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
        toolbar.setOverflowIcon(d);


        // BEGIN: ===== Floating Action Button =====
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Fragment_AddSightings.class);
                MainActivity.this.startActivity(myIntent);


            }
        });
        fab.hide();
        // END:   ===== Floating Action Button =====


        // BEGIN: ===== Drawer Layout & Navigation View =====
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setVisible(false);
        navigationView.getMenu().getItem(2).setVisible(false);

        // END:   ===== Drawer Layout & Navigation View =====


        // BEGIN: ===== FRAGMENT =====
        Fragment fragment = new Fragment_MySightings();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.screen_area, fragment);
        fragmentTransaction.commit();
        // END  : ===== FRAGMENT =====


        // BEGIN: ===== Firebase Authentication =====
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }
        // END:   ===== Firebase Authentication =====


        //mDatabase = FirebaseDatabase.getInstance().getReference();
        //DatabaseReference mRef = mDatabase.getDatabase().getReference("copyright");
        //mRef.setValue("©2018 Moranguitos. All rights Reserved");


        // BEGIN: ======== Nav Header Main ==========
        final String userEmail = mFirebaseUser.getEmail();
        final DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference("users");
        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot here = dataSnapshot;
                boolean flag = false;
                for(DataSnapshot fin : here.getChildren()){
                    String email = (String) fin.child("userEmail").getValue();
                    if (email.equals(userEmail)) {
                        String userName = (String) fin.child("userName").getValue();

                        String verified = (String) fin.child("userVerifier").getValue();

                        if (verified.equals("true")) {
                            navigationView.getMenu().getItem(2).setVisible(true);
                        }

                        View v = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
                        TextView TuserName = v.findViewById(R.id.nameUser);
                        TuserName.setText(userName);
                        TextView TemailName = v.findViewById(R.id.userEmail);
                        TemailName.setText(userEmail);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // END:   ========= Nav Header Main ==========



        // BEGIN: ======== CHECK IF MODERATOR ========
        final DatabaseReference mDatabase3 = FirebaseDatabase.getInstance().getReference("moderators");
        mDatabase3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot here = dataSnapshot;

                for(DataSnapshot fin : here.getChildren()){
                    String email = (String) fin.child("email").getValue();;
                    if (email != null) {
                        if (email.equals(userEmail)) {
                            navigationView.getMenu().getItem(0).setVisible(true);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // END:   ======== CHECK IF MODERATOR ========
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragment = new Fragment_MySightings();
        } else if (id == R.id.nav_moderate) {
            fragment = new Fragment_ModeratorOptions();
        } else if (id == R.id.nav_identifications) {
            fragment = new Fragment_MyIdentifications();
        } else if (id == R.id.nav_explore) {
            fragment = new Fragment_Explore();
        } else if (id == R.id.nav_editprofile) {
            fragment = new Fragment_Profile();
        } else if (id == R.id.nav_exit) {
            //FirebaseAuth.getInstance().getCurrentUser().
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if (fragment!=null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.screen_area, fragment);
            fragmentTransaction.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
