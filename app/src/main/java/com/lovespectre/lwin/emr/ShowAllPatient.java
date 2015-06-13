package com.lovespectre.lwin.emr;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lwin on 5/29/15.
 */
public class ShowAllPatient extends ListActivity {


    //Progress Dialog
    private ProgressDialog pDialog;

    //Creating JSON Parser object
    JsonParser jParser = new JsonParser();

    ArrayList<HashMap<String, String>> patientList;
    private static String ip="192.168.43.48";
    //url to get all patient list
    private static String url_all_patient = "http://"+ip+"/openemr/get_all_patient.php";


    //JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PATIENT = "patient_data";
    private static final String TAG_PID = "id";
    private static final String TAG_FNAME = "fname";
    private static final String TAG_LNAME = "lname";

       //patient JSONArray
    JSONArray patient = null;

    EditText inputSearch;

      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_patient);


        //Hashmap for ListView
        patientList = new ArrayList<HashMap<String, String>>();



        //Loading patients in Background Thread
        new LoadAllPatient().execute();

       // Get listview
        ListView lv = getListView();


        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                     UpdatePatient.class);
                // sending pid to next activity
                in.putExtra(TAG_PID, pid);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });

    }

    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }


    class LoadAllPatient extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowAllPatient.this);
            pDialog.setMessage("Loading patients. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_patient, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Patients: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    patient = json.getJSONArray(TAG_PATIENT);

                    // looping through All Products
                    for (int i = 0; i < patient.length(); i++) {
                        JSONObject c = patient.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String fname = c.getString(TAG_FNAME);
                        String lname = c.getString(TAG_LNAME);
                        Log.i("List Item:"+fname,lname);



                       // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_FNAME, fname);
                        map.put(TAG_LNAME,lname);


                        // adding HashList to ArrayList
                        patientList.add(map);
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            NewPatient.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
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
            // dismiss the dialog after getting all products

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                   /**
                     * Updating parsed JSON data into ListView
                    */
                 ListAdapter adapter = new SimpleAdapter(
                            ShowAllPatient.this, patientList,
                            R.layout.list_item, new String[] { TAG_PID,TAG_FNAME,TAG_LNAME},new int[] { R.id.pid, R.id.fname,R.id.lname});
                    // updating listview
                    setListAdapter(adapter);
                    inputSearch=(EditText)findViewById(R.id.search);

                    inputSearch.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {


                            ArrayList<HashMap<String, String>> Templist= new ArrayList<HashMap<String,String>>();
                            String searchString =inputSearch.getText().toString();

                            if(searchString.length()>0){
                                for (int i = 0; i < patientList.size(); i++)
                                {
                                    String currentString =patientList.get(i).get(ShowAllPatient.TAG_FNAME);
                                    String currentstring =patientList.get(i).get(ShowAllPatient.TAG_LNAME);
                                    if ((searchString.equalsIgnoreCase(currentString)|| searchString.equalsIgnoreCase(currentstring)))
                                    {
                                        Templist.add(patientList.get(i));
                                    }
                                }
                                ListAdapter adapter = new SimpleAdapter(
                                        ShowAllPatient.this, Templist,
                                        R.layout.list_item, new String[] { TAG_PID,
                                        TAG_FNAME,TAG_LNAME},
                                        new int[] { R.id.pid, R.id.fname,R.id.lname });
                                // updating listview
                                setListAdapter(adapter);
                            }else{
                                ListAdapter adapter = new SimpleAdapter(
                                        ShowAllPatient.this, patientList,
                                        R.layout.list_item, new String[] { TAG_PID,
                                        TAG_FNAME,TAG_LNAME},
                                        new int[] { R.id.pid, R.id.fname,R.id.lname });
                                // updating listview
                                setListAdapter(adapter);

                            }

                        }

                        @Override
                        public void afterTextChanged(Editable s) {


                        }
                    });
                    pDialog.dismiss();

                }
            });

        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
