package com.anuranbarman.blooddonationmanager;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anuran on 22/10/16.
 */

public class AllDonors extends Fragment {


    ListView list;
    String url="http://anuranbarman.com/blooddonate/data.php";
    ProgressDialog progressDialog;
    MyAdapter adapter;
    Data dm;
    ArrayList<Data> dataModelArrayList=new ArrayList<Data>();
    public AllDonors(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.alldonors, container, false);
        list=(ListView)rootView.findViewById(R.id.listView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new FetchUsers().execute();

    }


    class FetchUsers extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Fetching users information");
            progressDialog.setTitle("Please be patient");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL surl=new URL(url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)surl.openConnection();
                InputStream is=httpURLConnection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(is,"iso-8859-1"));
                String response="";
                String line="";
                while((line=reader.readLine()) !=null){
                    response+=line;
                }
                JSONObject jsonObject=new JSONObject(response);
                JSONArray jsonArray=jsonObject.optJSONArray("users");
                for(int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject2=jsonArray.getJSONObject(i);
                        String name=jsonObject2.optString("name");
                        String user_name=jsonObject2.optString("user_name");
                        String user_email=jsonObject2.optString("user_email");
                        String user_mob=jsonObject2.optString("user_mob");
                        String user_location=jsonObject2.optString("user_location");
                        String user_blood=jsonObject2.optString("user_blood");

                    dm=new Data(name,user_name,user_email,user_mob,user_location,user_blood);
                    dataModelArrayList.add(dm);
                }


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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            adapter=new MyAdapter(getActivity(),dataModelArrayList);
            list.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }


}
