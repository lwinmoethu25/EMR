package com.lovespectre.lwin.emr;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alertdialogpro.AlertDialogPro;

/**
 * Created by lwin on 6/20/15.
 */
public class Home extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.add_patient,container,false);


       Toolbar toolbar=(Toolbar)view.findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        /*Button btnNew = (Button) view.findViewById(R.id.btnNew);
        Button btnShow = (Button) view.findViewById(R.id.btnShow);

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getActivity(),NewPatient.class);
                // i.putExtra(IP,ip);view
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });



        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getActivity(),ShowAllPatient.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
*/


        return view;
    }




    public void onBackPressed() {
        if (getActivity().isTaskRoot()) {
            AlertDialogPro.Builder builder = new AlertDialogPro.Builder(getActivity());
            builder.setTitle("Exit")
                    .setIcon(R.drawable.ic_exit_to_app_black_36dp)
                    .setMessage("Are you sure to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Home.super.getActivity().onBackPressed();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialogPro alert = builder.create();
            alert.show();

        } else {
            super.getActivity().onBackPressed();
        }
    }

    private void setSupportActionBar(Toolbar toolbar) {

    }



}
