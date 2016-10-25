package com.anuranbarman.blooddonationmanager;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by anuran on 22/10/16.
 */

public class ActiveBloodCamp extends Fragment {

    String usernamepref,title,venue,date,time;
    TextView texttitle,textvenue,textdate,texttime;
    public ActiveBloodCamp(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activebloodcamp, container, false);
        texttitle=(TextView)rootView.findViewById(R.id.tvTitle);
        textvenue=(TextView)rootView.findViewById(R.id.tvvenue);
        textdate=(TextView)rootView.findViewById(R.id.tvdate);
        texttime=(TextView)rootView.findViewById(R.id.tvtime);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new ShowCamp().execute("");
    }

    class ShowCamp extends AsyncTask<String,Integer,String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Fetching upcoming blood donation camp information");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String surl="http://anuranbarman.com/blooddonate/activecamp.php";
            try {
                URL url=new URL(surl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream is=httpURLConnection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(is,"iso-8859-1"));
                String response="";
                String line="";
                while((line=reader.readLine()) !=null){
                    response+=line;
                }
                JSONObject jsonObject=new JSONObject(response);
                title=jsonObject.optString("title");
                venue=jsonObject.optString("venue");
                date=jsonObject.optString("date");
                time=jsonObject.optString("time");



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            texttitle.setText("Title : "+title);
            textvenue.setText("Venue : "+venue);
            textdate.setText("Date : "+date);
            texttime.setText("Time : "+time);
            progressDialog.dismiss();
        }
    }
}
