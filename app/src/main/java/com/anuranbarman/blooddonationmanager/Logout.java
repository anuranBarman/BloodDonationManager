package com.anuranbarman.blooddonationmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by anuran on 22/10/16.
 */

public class Logout extends Fragment {
    public Logout(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.logout, container, false);
        final AlertDialog.Builder alertdialog= new AlertDialog.Builder(getActivity());
        alertdialog.setMessage("Are you sure, you want to Log out?");
        alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences=getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
                boolean isLogged=sharedPreferences.getBoolean("isLogged",false);
                if(isLogged){
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean("isLogged",false);
                    editor.commit();
                    Intent i = new Intent(getActivity(),Login.class);
                    startActivity(i);
                    getActivity().finish();
                }
            }
        });
        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    return;
            }
        });
        alertdialog.setCancelable(false);
        AlertDialog alertDialog=alertdialog.create();
        alertDialog.show();
        return rootView;
    }
}
