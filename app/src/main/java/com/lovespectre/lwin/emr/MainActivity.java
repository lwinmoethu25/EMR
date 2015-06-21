package com.lovespectre.lwin.emr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alertdialogpro.AlertDialogPro;
import com.lovespectre.lwin.custom.MyAdapter;


public class MainActivity extends ActionBarActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final String ip=((EditText) findViewById(R.id.iptxt)).getText().toString();

        final SharedPreferences ip_setting= getSharedPreferences("ip",Activity.MODE_PRIVATE);
        final SharedPreferences.Editor editor=ip_setting.edit();
        editor.putString("Ip_Address",ip);
        editor.commit();



        String TITLES[] = {"Home", "New Patient", "Show Patient", "Message", "Setting", "Exit"};
        int ICONS[] = {R.drawable.ic_home_black_24dp, R.drawable.ic_person_add_black_24dp, R.drawable.ic_person_black_24dp, R.drawable.ic_message_black_24dp,
                R.drawable.ic_settings_black_24dp, R.drawable.ic_exit_to_app_black_36dp};

        String NAME = "OPEN EMR";
        String EMAIL = "lwinmoethu25@gmail.com";
        int PROFILE = R.drawable.logo;
        final DrawerLayout Drawer;
        ActionBarDrawerToggle mDrawerToggle;
        RecyclerView mRecyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE,this);

        mRecyclerView.setAdapter(mAdapter);

        final GestureDetector mGestureDetector=new GestureDetector(MainActivity.this,new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener(){


            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());



                if(child!=null && mGestureDetector.onTouchEvent(motionEvent)){
                    Drawer.closeDrawers();
                    //Toast.makeText(MainActivity.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                    onTouchDrawer(recyclerView.getChildPosition(child));
                    return true;

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };

        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

    // Buttons
    Button btnNew = (Button) findViewById(R.id.btnNew);
    Button btnShow = (Button) findViewById(R.id.btnShow);


    btnNew.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent i=new Intent(getApplicationContext(),NewPatient.class);
            // i.putExtra(IP,ip);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
    });



    btnShow.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent i=new Intent(getApplicationContext(),ShowAllPatient.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
    });

}

    public void onTouchDrawer(final int position){


        switch (position){

            case 0:
                Toast.makeText(this,"Open EMR",Toast.LENGTH_SHORT).show();
                break;

            case 1:
                openFragment(new Home());
                break;

            case 2:
                Intent intent2=new Intent(this,NewPatient.class);
                startActivity(intent2);
                break;

            case 3:
                Intent intent3 = new Intent(this, ShowAllPatient.class);
                startActivity(intent3);
                break;

            case 4:
                openFragment(new Message());
                break;

            case 6:

                onBackPressed();
                break;

            default:

                break;

        }
    }



    private void openFragment(final Fragment fragment){

      getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();


    }


  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            AlertDialogPro.Builder builder = new AlertDialogPro.Builder(this);
            builder.setTitle("Exit")
                    .setIcon(R.drawable.ic_exit_to_app_black_36dp)
                    .setMessage("Are you sure to exit?")
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            MainActivity.super.onBackPressed();

                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialogPro alert = builder.create();
            alert.show();

        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
