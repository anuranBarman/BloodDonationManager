package com.anuranbarman.blooddonationmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class RequestBlood extends Fragment {

    String selectedtype,selectedqty,usernamepref;
    TextView apos,aneg,bpos,bneg,abpos,abneg,opos,oneg;
    Button request;
    SharedPreferences sharedPreferences;
    Spinner type=null,qty=null;
    List<String> list= new ArrayList<String>();
    List<String> bloodtypes= new ArrayList<String>();
    List<String> bloodqty= new ArrayList<String>();
    public RequestBlood(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.requestblood, container, false);
        apos=(TextView)rootView.findViewById(R.id.tvApos);
        bpos=(TextView)rootView.findViewById(R.id.tvBpos);
        abpos=(TextView)rootView.findViewById(R.id.tvABpos);
        opos=(TextView)rootView.findViewById(R.id.tvOpos);
        aneg=(TextView)rootView.findViewById(R.id.tvAneg);
        bneg=(TextView)rootView.findViewById(R.id.tvBneg);
        abneg=(TextView)rootView.findViewById(R.id.tvABneg);
        oneg=(TextView)rootView.findViewById(R.id.tvOneg);
        request=(Button)rootView.findViewById(R.id.btnRequest);
        type=(Spinner)rootView.findViewById(R.id.typespin);
        qty=(Spinner)rootView.findViewById(R.id.qtyspin);
        sharedPreferences=getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernamepref=sharedPreferences.getString("username",null);
        new ShowQuantity().execute("");

        bloodtypes.add("A+");
        bloodtypes.add("A-");
        bloodtypes.add("B+");
        bloodtypes.add("B-");
        bloodtypes.add("AB+");
        bloodtypes.add("AB-");
        bloodtypes.add("O+");
        bloodtypes.add("O-");

        bloodqty.add("1");
        bloodqty.add("2");
        bloodqty.add("3");
        bloodqty.add("4");
        bloodqty.add("5");

        ArrayAdapter<String> typeadapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,bloodtypes);
        typeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeadapter);


        ArrayAdapter<String> qtyadapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,bloodqty);
        qtyadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qty.setAdapter(qtyadapter);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedtype=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedqty=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedtype !=null && selectedqty !=null){
                    new CheckQuantity().execute("");
                }else{
                    Toast.makeText(getActivity(),"Please select Blood Group and Blood Quantity before making request",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    class ShowQuantity extends AsyncTask<String,Integer,String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Fetching blood quantity information");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String surl="http://anuranbarman.com/blooddonate/bloodbank.php";
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
                JSONArray jsonArray=jsonObject.optJSONArray("bloods");
                for(int i=0;i<jsonArray.length();i++){
                    String qty=jsonArray.optJSONObject(i).optString("quantity");
                    list.add(qty);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            apos.setText(list.get(0)+" Units");
            aneg.setText(list.get(1)+" Units");
            bpos.setText(list.get(2)+" Units");
            bneg.setText(list.get(3)+" Units");
            abpos.setText(list.get(4)+" Units");
            abneg.setText(list.get(5)+" Units");
            opos.setText(list.get(6)+" Units");
            oneg.setText(list.get(7)+" Units");
            progressDialog.dismiss();
        }
    }

    class CheckQuantity extends AsyncTask<String,Integer,String> {
        ProgressDialog progressDialog;
        int success;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Requesting for Blood");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String surl="http://anuranbarman.com/blooddonate/request.php";
            try {
                URL url=new URL(surl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();
                BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(selectedtype,"UTF-8")+"&"+
                        URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(usernamepref,"UTF-8")+"&"+
                        URLEncoder.encode("qty","UTF-8")+"="+URLEncoder.encode(selectedqty,"UTF-8");
                writer.write(data);
                writer.flush();
                writer.close();
                os.close();
                InputStream is=httpURLConnection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(is,"iso-8859-1"));
                String response="";
                String line="";
                while((line=reader.readLine()) !=null){
                    response+=line;
                }

                JSONObject jsonObject=new JSONObject(response);
                success = jsonObject.optInt("success");



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
            apos.setText(list.get(0)+" Units");
            aneg.setText(list.get(1)+" Units");
            bpos.setText(list.get(2)+" Units");
            bneg.setText(list.get(3)+" Units");
            abpos.setText(list.get(4)+" Units");
            abneg.setText(list.get(5)+" Units");
            opos.setText(list.get(6)+" Units");
            oneg.setText(list.get(7)+" Units");
            progressDialog.dismiss();
            if(success==1){
                Toast.makeText(getActivity(),"Successfully put request.Wait for confirmation.Go to My Profile tab to view your requests.",Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(getActivity(),"Not enough Blood available.Please try again later.",Toast.LENGTH_SHORT).show();

            }
        }
    }
}
