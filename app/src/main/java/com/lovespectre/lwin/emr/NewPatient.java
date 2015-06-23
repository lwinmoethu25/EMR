package com.lovespectre.lwin.emr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lwin on 5/29/15.
 */
public class NewPatient extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText inputCity;
    TextView txtDate;

    private RadioButton rd;

    JsonParser jsonParser=new JsonParser();

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    private static String url_create_patient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_patient);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        final SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
        final String ipAddress=prefs.getString("IP",null);

        // url to create new patient
        url_create_patient = "http://"+ipAddress+"/openemr/create_patient.php";


        // Edit Text
        firstName = (EditText) findViewById(R.id.firstName);
        lastName=(EditText) findViewById(R.id.lastName);
        inputCity = (EditText) findViewById(R.id.inputCity);

       final Button btnCreate = (Button) findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // creating new patient in background thread
                new CreateNewPatient().execute();
            }
        });
    }

    public void datepicker(View view){

        final Calendar c=Calendar.getInstance();
        int mYear=c.get(Calendar.YEAR);
        int mMonth=c.get(Calendar.MONTH);
        int mDay=c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog picker=new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {


            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                txtDate=(TextView) findViewById(R.id.showdate);
                txtDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

            }
        },mYear,mMonth,mDay);
        picker.show();
    }

    public void onRadioButtonClicked(View view){


        rd = (RadioButton)view;

        if(rd.isChecked()){
           Toast.makeText(getApplicationContext(), "You selected " + rd.getText(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
         * Background Async Task to Create new Patient
         * */
        class CreateNewPatient extends AsyncTask<String, String, String> {

            /**
             * Before starting background thread Show Progress Dialog
             */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(NewPatient.this);
                pDialog.setMessage("Creating Patient..");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            /**
             * Creating patient
             */
            protected String doInBackground(String... args) {
                String fname= firstName.getText().toString();
                String lname= lastName.getText().toString();
                String city = inputCity.getText().toString();
                String dob  = txtDate.getText().toString();
                String sex  = rd.getText().toString();



                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("fname",fname));
                params.add(new BasicNameValuePair("lname",lname));
                params.add(new BasicNameValuePair("DOB",dob));
                params.add(new BasicNameValuePair("sex",sex));
                params.add(new BasicNameValuePair("city", city));
                // getting JSON Object
                // Note that create product url accepts POST method

                Log.i("Create Response", url_create_patient+fname+lname+city+dob+sex);
                JSONObject json = jsonParser.makeHttpRequest(url_create_patient, "POST", params);


                // check log cat fro response
                Log.i("Create Response", json.toString());

                // check for success tag
                try {


                    int success = json.getInt(TAG_SUCCESS);


                    if (success == 1) {
                        // successfully created patient
                        Intent i = new Intent(getApplicationContext(), ShowAllPatient.class);
                        startActivity(i);

                        // closing this screen
                        finish();
                    } else {
                         finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }


            /**
             * After completing background task Dismiss the progress dialog
             * *
             */
            protected void onPostExecute(String file_url) {
                // dismiss the dialog once done
                pDialog.dismiss();
            }


        }

    @Override
    public void onBackPressed() {
       super.onBackPressed();

    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
