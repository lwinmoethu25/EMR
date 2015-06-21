package com.lovespectre.lwin.emr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lwin on 6/21/15.
 */
public class Message extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.message,container,false);
        return view;
    }
}
