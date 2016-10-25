package com.anuranbarman.blooddonationmanager;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by anuran on 22/10/16.
 */

public class DrawerItemCustomAdapter extends ArrayAdapter<DataModel> {

    Context mContext;
    int layoutResourceId;
    DataModel data[] = null;

    public DrawerItemCustomAdapter(Context context, int resource, DataModel[] data) {
        super(context, resource, data);
        this.layoutResourceId = resource;
        this.mContext = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);

        DataModel folder = data[position];


        textViewName.setText(folder.name);

        return listItem;
    }
}
