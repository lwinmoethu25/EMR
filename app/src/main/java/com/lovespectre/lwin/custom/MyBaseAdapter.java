package com.lovespectre.lwin.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovespectre.lwin.emr.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by lwin on 6/17/15.
 */
public class MyBaseAdapter extends BaseAdapter {


    ArrayList<ShowItem> myList=new ArrayList<ShowItem>();
    ArrayList<ShowItem> mfilterList;

    LayoutInflater inflater;
    Context context;


    public MyBaseAdapter(Context context, ArrayList<ShowItem> myList) {
        this.context = context;
        this.myList = myList;
        this.mfilterList=myList;
        inflater=LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyViewHolder myViewHolder;

        if(convertView==null){

            convertView=inflater.inflate(R.layout.list_item,parent,false);
            myViewHolder=new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);

        }else{

            myViewHolder=(MyViewHolder) convertView.getTag();
        }

        ShowItem showItem=(ShowItem) getItem(position);

        myViewHolder.id.setText(showItem.getId());
        myViewHolder.fName.setText(showItem.getFname());
        myViewHolder.lName.setText(showItem.getLname());
        myViewHolder.City.setText(showItem.getCity());
        myViewHolder.icon.setImageResource(R.drawable.ic_face_black);

        return convertView;
    }

    private class MyViewHolder{

        TextView fName,lName,City,id;
        ImageView icon;

       public MyViewHolder(View item){

           id=(TextView) item.findViewById(R.id.pid);
           fName=(TextView) item.findViewById(R.id.fname);
           lName=(TextView) item.findViewById(R.id.lname);
           City= (TextView) item.findViewById(R.id.city);
           icon=(ImageView) item.findViewById(R.id.img);
       }
    }
}
