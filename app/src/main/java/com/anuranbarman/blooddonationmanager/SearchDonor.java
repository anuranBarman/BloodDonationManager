package com.anuranbarman.blooddonationmanager;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by anuran on 25/10/16.
 */

public class SearchDonor extends Fragment {
    TextView tvname,tvusername,tvuseremail,tvusermob,tvuserlocation,tvuserblood;
    String name,username,email,mob,location,blood;
    EditText et;
    Button search;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.searchdonor,container,false);
        tvname=(TextView)rootView.findViewById(R.id.tvSearchName);
        tvusername=(TextView)rootView.findViewById(R.id.tvSearchusername);
        tvuseremail=(TextView)rootView.findViewById(R.id.tvSearchemail);
        tvuserlocation=(TextView)rootView.findViewById(R.id.tvSearchaddress);
        tvusermob=(TextView)rootView.findViewById(R.id.tvSearchmobile);
        tvuserblood=(TextView)rootView.findViewById(R.id.tvSearchblood);
        et=(EditText)rootView.findViewById(R.id.searchDonorUsername);
        search=(Button)rootView.findViewById(R.id.btnSearch);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query=et.getText().toString();
                query=query.trim();
                if(query.equals("")){
                    Toast.makeText(getActivity(),"You must provide username of the donor.",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    new ShowProfile().execute(query);
                }

            }
        });

    }


    class ShowProfile extends AsyncTask<String,Integer,String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Fetching donor profile information");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String surl="http://anuranbarman.com/blooddonate/search.php";
            try {
                URL url=new URL(surl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();
                BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(params[0],"UTF-8");
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
                name=jsonObject.optString("name");
                username=jsonObject.optString("username");
                email=jsonObject.optString("email");
                mob=jsonObject.optString("mob");
                location=jsonObject.optString("location");
                blood=jsonObject.optString("blood");


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
            tvname.setText("Name : "+name);
            tvusername.setText("User Name : "+username);
            tvuseremail.setText("Email : "+email);
            tvusermob.setText("Mobile : "+mob);
            tvuserlocation.setText("Address : "+location);
            tvuserblood.setText("Blood Group : "+blood);
            progressDialog.dismiss();
        }
    }
}
