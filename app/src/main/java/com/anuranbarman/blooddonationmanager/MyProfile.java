package com.anuranbarman.blooddonationmanager;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
import java.util.List;

/**
 * Created by anuran on 22/10/16.
 */

public class MyProfile extends Fragment {
    String usernamepref;
    String name,username,email,mob,location,blood;
    TextView textname,textusername,textemail,textmobile,textlocation,textblood;
    TableLayout table;
    public MyProfile(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.myprofile, container, false);
        textname = (TextView)rootView.findViewById(R.id.tvName);
        textusername = (TextView)rootView.findViewById(R.id.tvusername);
        textemail = (TextView)rootView.findViewById(R.id.tvemail);
        textmobile = (TextView)rootView.findViewById(R.id.tvmobile);
        textlocation = (TextView)rootView.findViewById(R.id.tvaddress);
        textblood = (TextView)rootView.findViewById(R.id.tvblood);
        table=(TableLayout)rootView.findViewById(R.id.requestTable);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences preferences=getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        usernamepref=preferences.getString("username",null);
        new ShowMyProfile().execute("");
        new FetchRequest().execute("");
    }

    class ShowMyProfile extends AsyncTask<String,Integer,String>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Fetching your profile information");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String surl="http://anuranbarman.com/blooddonate/profile.php";
            try {
                URL url=new URL(surl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();
                BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(usernamepref,"UTF-8");
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
            textname.setText("Name : "+name);
            textusername.setText("User Name : "+username);
            textemail.setText("Email : "+email);
            textmobile.setText("Mobile : "+mob);
            textlocation.setText("Address : "+location);
            textblood.setText("Blood Group : "+blood);
            progressDialog.dismiss();
        }
    }


    class FetchRequest extends AsyncTask<String,Integer,String>{
        ProgressDialog progressDialog;
        JSONArray jsonArray;
        List<String> typeList=new ArrayList<String>();
        List<String> qtyList=new ArrayList<String>();
        List<String> statusList=new ArrayList<String>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Fetching your requests information");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String surl="http://anuranbarman.com/blooddonate/fetchrequest.php";
            try {
                URL url=new URL(surl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();
                BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(usernamepref,"UTF-8");
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
                jsonArray=jsonObject.optJSONArray("requests");
                for(int i=0;i<jsonArray.length();i++){
                    String typeFetched=jsonArray.optJSONObject(i).optString("blood");
                    String qtyFetched=jsonArray.optJSONObject(i).optString("quantity");
                    String statusFetched=jsonArray.optJSONObject(i).optString("status");
                    typeList.add(typeFetched);
                    qtyList.add(qtyFetched);
                    statusList.add(statusFetched);
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
            table.removeAllViews();
            TableRow bloodRow=new TableRow(getActivity());
            bloodRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            TextView tvBloodHeader = new TextView(getActivity());
            tvBloodHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvBloodHeader.setBackgroundResource(R.drawable.profile_tv_bg);
            tvBloodHeader.setPadding(10, 10, 10, 10);
            tvBloodHeader.setText("Blood Group");
            bloodRow.addView(tvBloodHeader);

            TextView tvQtyHeader = new TextView(getActivity());
            tvQtyHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvQtyHeader.setBackgroundResource(R.drawable.profile_tv_bg);
            tvQtyHeader.setPadding(10, 10, 10, 10);
            tvQtyHeader.setText("Blood Quantity");
            bloodRow.addView(tvQtyHeader);

            TextView tvStatusHeader = new TextView(getActivity());
            tvStatusHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvStatusHeader.setBackgroundResource(R.drawable.profile_tv_bg);
            tvStatusHeader.setPadding(10, 10, 10, 10);
            tvStatusHeader.setText("Status");
            bloodRow.addView(tvStatusHeader);

            table.addView(bloodRow);

            for(int i=0;i<jsonArray.length();i++){
                TableRow row=new TableRow(getActivity());
                row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                    TextView tvbloodHeader = new TextView(getActivity());
                    tvbloodHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tvbloodHeader.setBackgroundResource(R.drawable.profile_tv_bg);
                    tvbloodHeader.setPadding(10, 10, 10, 10);
                    tvbloodHeader.setText(typeList.get(i));
                    row.addView(tvbloodHeader);

                    TextView tvqtyHeader = new TextView(getActivity());
                    tvqtyHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tvqtyHeader.setBackgroundResource(R.drawable.profile_tv_bg);
                    tvqtyHeader.setPadding(10, 10, 10, 10);
                    tvqtyHeader.setText(qtyList.get(i)+" Unit");
                    row.addView(tvqtyHeader);

                    TextView tvstatusHeader = new TextView(getActivity());
                    tvstatusHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tvstatusHeader.setBackgroundResource(R.drawable.profile_tv_bg);
                    tvstatusHeader.setPadding(10, 10, 10, 10);
                    tvstatusHeader.setText(statusList.get(i));
                    row.addView(tvstatusHeader);



                table.addView(row);
            }

            progressDialog.dismiss();
        }
    }




}
