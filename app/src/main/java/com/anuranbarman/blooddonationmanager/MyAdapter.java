package com.anuranbarman.blooddonationmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anuran on 25/10/16.
 */

public class MyAdapter extends BaseAdapter {

    Context context;
    ArrayList<Data> dataModels=new ArrayList<Data>();

    public MyAdapter(Context context,ArrayList<Data> dataModels){
        this.context=context;
        this.dataModels=dataModels;
    }
    @Override
    public int getCount() {
        return dataModels.size();
    }

    @Override
    public Object getItem(int position) {
        return dataModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (View) inflater.inflate(
                    R.layout.listitem, null);
        }

        TextView name = (TextView)convertView.findViewById(R.id.lvName);
        TextView username=(TextView)convertView.findViewById(R.id.lvUsername);
        TextView useremail=(TextView)convertView.findViewById(R.id.lvUseremail);
        TextView usermob=(TextView)convertView.findViewById(R.id.lvUsermob);
        TextView userlocation=(TextView)convertView.findViewById(R.id.lvUserlocation);
        TextView userblood=(TextView)convertView.findViewById(R.id.lvUserblood);

        name.setText(dataModels.get(position).getName());
        username.setText(dataModels.get(position).getUsername());
        useremail.setText(dataModels.get(position).getUseremail());
        usermob.setText(dataModels.get(position).getUsermob());
        userlocation.setText(dataModels.get(position).getUserlocation());
        userblood.setText(dataModels.get(position).getUserblood());

        return convertView;
    }
}
