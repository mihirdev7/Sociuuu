package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.socialapp.adapters.vwpagerAdapter;
import com.example.socialapp.fragments.home;
import com.example.socialapp.fragments.notification;
import com.example.socialapp.fragments.post;
import com.example.socialapp.fragments.profile;
import com.example.socialapp.fragments.searchh;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FrameLayout framel;
    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager2;
   private FirebaseAuth auth;
    //vwpagerAdapter vwpgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        framel=findViewById(R.id.framel);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        setSupportActionBar(findViewById(R.id.toolbar));
        MainActivity.this.setTitle("My Profile");
        auth=FirebaseAuth.getInstance();


        //viewPager2=findViewById(R.id.viewPager2);
        //vwpagerAdapter vwpgr= new vwpagerAdapter(this);
        //viewPager2.setAdapter(vwpgr);
        getSupportFragmentManager().beginTransaction().replace(R.id.framel,new home()).addToBackStack("fragment1").commit();

        findViewById(R.id.toolbar).setVisibility(View.GONE);

        if(!internetStatus.isInternetAvailable(this)) {
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setTitle("NO CONNECTION");
            ab.setMessage("Please Turn On Internet");
            ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            }).show();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                if (bottomNavigationView.getSelectedItemId()==item.getItemId()){
                    return true;
                }

                if (item.getItemId() == R.id.ssearch) {
                    findViewById(R.id.toolbar).setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.framel, new searchh())
                            .addToBackStack("fragment2")
                            .commit();

                    return true;
                } else if (item.getItemId() == R.id.hhome) {
                    findViewById(R.id.toolbar).setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.framel, new home())
                            .addToBackStack("fragment1").commit();
                    return true;

                } else if (item.getItemId() == R.id.ppost) {
                    findViewById(R.id.toolbar).setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.framel, new post())
                            .addToBackStack("fragment3").commit();
                    return true;

                } else if (item.getItemId() == R.id.nnotification) {
                    findViewById(R.id.toolbar).setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.framel, new notification())
                            .addToBackStack("fragment4").commit();
                    return true;

                } else if (item.getItemId() == R.id.pprofile) {
                    findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.framel, new profile())
                            .addToBackStack("fragment5").commit();
                    return true;
                }
                else{
                    return false;
                }
            }
        });

    }
      //=======================below code is for signout that placed in profile and add using menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_settingicon,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.setting){
            AlertDialog.Builder abc = new AlertDialog.Builder(this);
            abc.setTitle("LOGOUT?");
            abc.setMessage("Are you sure want to logout?");
            abc.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    auth.signOut();
                    Intent intent=new Intent(MainActivity.this,loginActivity.class);
                    startActivity(intent);
                    finish();}
            });
            abc.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
        }

    @Override
    public void onBackPressed() {  //ignore below code its just rough work
       // if (getSupportFragmentManager().getBackStackEntryCount()>0){
         //   getSupportFragmentManager().popBackStack();
        // int index=getSupportFragmentManager().getBackStackEntryCount()-2;
        // if (index>=0){
        // androidx.fragment.app.FragmentManager.BackStackEntry backStackEntry=getSupportFragmentManager().getBackStackEntryAt(index);
        // String tag= backStackEntry.getName();
            int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();

            if (backStackEntryCount > 1) {
                getSupportFragmentManager().popBackStackImmediate();
                int index = backStackEntryCount-2;
                //if (index >= 0) {
                    //String tag = getSupportFragmentManager().getBackStackEntryAt(index).getName();
                    String tag = (index >= 0) ? getSupportFragmentManager().getBackStackEntryAt(index).getName() : "";

                    switch (tag){
                    case "fragment1":
                        bottomNavigationView.setSelectedItemId(R.id.hhome);
                        break;
                    case "fragment2":
                        bottomNavigationView.setSelectedItemId(R.id.ssearch);
                        break;
                    case "fragment3":
                        bottomNavigationView.setSelectedItemId(R.id.ppost);
                        break;
                    case "fragment4":
                        bottomNavigationView.setSelectedItemId(R.id.nnotification);
                        break;
                    case "fragment5":
                        bottomNavigationView.setSelectedItemId(R.id.pprofile);
                        break;
                    default:
                        bottomNavigationView.setSelectedItemId(R.id.hhome);
                        break;
               //}
            }
        }else {
            finishAffinity();
        }
        super.onBackPressed();
    }


}
   /* viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.hhome);

                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.ssearch);

                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.ppost);

                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.nnotification);
                        break;
                    default:
                        bottomNavigationView.setSelectedItemId(R.id.pprofile);
                        break;
                }
            }
        });*/