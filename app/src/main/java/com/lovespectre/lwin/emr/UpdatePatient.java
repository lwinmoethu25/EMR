package com.lovespectre.lwin.emr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.alertdialogpro.AlertDialogPro;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwin on 5/30/15.
 */
public class UpdatePatient extends Activity {



    EditText fName;
    EditText lName;
    EditText city;

    ImageButton btnSave;
    ImageButton btnDelete;

    String id;
    String fname;
    String lname;
    String upcity;

    private ProgressDialog pDialog;

    JsonParser jsonParser = new JsonParser();


    // single patient url
    private static final String url_patient_detials = "http://192.168.43.48/openemr/get_patient_details.php";

    // url to update patient
    private static final String url_update_patient = "http://192.168.43.48/openemr/update_patient.php";

    // url to delete patient
    private static final String url_delete_patient = "http://192.168.43.48/openemr/delete_patient.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PATIENT = "patient_data";
    private static final String TAG_PID = "id";
    private static final String TAG_FNAME = "fname";
    private static final String TAG_LNAME= "lname";
    private static final String TAG_CITY= "city";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_patient);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // save button
        btnSave = (ImageButton) findViewById(R.id.btnSave);
        btnDelete = (ImageButton) findViewById(R.id.btnDelete);

        // getting patient details from intent
        Intent i = getIntent();

        // getting patient id (pid) from intent
        id = i.getStringExtra(TAG_PID);
        Log.i("Get From Intent:",TAG_PID);

        // Getting complete patient details in background thread
        new GetPatientDetails().execute();

        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // starting background task to update patient

            new SavePatientDetails().execute();
            }
        });

        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                fname=fName.getText().toString();
                lname=lName.getText().toString();

                AlertDialogPro.Builder alert=new AlertDialogPro.Builder(UpdatePatient.this);
                alert.setTitle("Delete Patient");
                alert.setIcon(R.drawable.ic_delete_black_36dp);
                alert.setMessage("Are you sure to delete "+fname+" "+lname+ "?");
                alert.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // deleting patient in background thread
                        new DeletePatient().execute();
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                alert.show();

             }
        });

    }

    class GetPatientDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdatePatient.this);
            pDialog.setMessage("Loading patient details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting patient details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("id",id));

                        // getting patient details by making HTTP request
                        // Note that patient details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(url_patient_detials, "GET", params);

                        // check your log for json response
                        Log.d("Single Patient Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received patient details
                            JSONArray patientObj = json.getJSONArray(TAG_PATIENT); // JSON Array

                            // get first patient object from JSON Array
                            JSONObject patient = patientObj.getJSONObject(0);

                            // patient with this pid found
                            // Edit Text
                            fName = (EditText) findViewById(R.id.upfirstName);
                            lName = (EditText) findViewById(R.id.upLastName);
                            city = (EditText) findViewById(R.id.upCity);

                            // display product data in EditText
                            fName.setText(patient.getString(TAG_FNAME));
                            lName.setText(patient.getString(TAG_LNAME));
                            city.setText(patient.getString(TAG_CITY));

                        }else{
                            // patient with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }

    /**
     * Background Async Task to  Save product Details
     * */
    class SavePatientDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdatePatient.this);
            pDialog.setMessage("Saving patient ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving patient
         * */
        protected String doInBackground(String... args) {

            // getting updated data from EditTexts
             fname = fName.getText().toString();
             lname = lName.getText().toString();
             upcity = city.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_PID, id));
            params.add(new BasicNameValuePair(TAG_FNAME, fname));
            params.add(new BasicNameValuePair(TAG_LNAME, lname));
            params.add(new BasicNameValuePair(TAG_CITY,  upcity));

            // sending modified data through http request
            // Notice that update patient url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_patient,"POST", params);

            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about product update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }

    /*****************************************************************
     * Background Async Task to Delete Product
     * */
    class DeletePatient extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdatePatient.this);
            pDialog.setMessage("Deleting Patient...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Deleting product
         * */
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id", id));

                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(url_delete_patient, "POST", params);

                // check your log for json response
                Log.d("Delete Product", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // product successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about product deletion
                    setResult(100, i);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();

        }

    }
}
