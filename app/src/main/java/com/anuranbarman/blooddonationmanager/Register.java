package com.anuranbarman.blooddonationmanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.ArrayList;

public class Register extends AppCompatActivity {

    EditText name,username,pass,email,mob,location;
    Button btnReg;
    Spinner spinner;
    String selectedGroup=null;
    ArrayList<String> groups = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        name=(EditText)findViewById(R.id.etName);
        username=(EditText)findViewById(R.id.etUserName);
        pass=(EditText)findViewById(R.id.etUserPass);
        email=(EditText)findViewById(R.id.etemail);
        mob=(EditText)findViewById(R.id.etmob);
        location=(EditText)findViewById(R.id.etaddress);
        btnReg=(Button)findViewById(R.id.btnReg);
        spinner=(Spinner)findViewById(R.id.groupSpinner);
        groups.add("A+");
        groups.add("A-");
        groups.add("B+");
        groups.add("B-");
        groups.add("AB+");
        groups.add("AB-");
        groups.add("O+");
        groups.add("O-");
        ArrayAdapter<String> spinneradapter = new ArrayAdapter<String>(Register.this,android.R.layout.simple_spinner_item,groups);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinneradapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGroup=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Register.this,"You must select your blood group.",Toast.LENGTH_SHORT).show();
            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sname,susername,spass,semail,smob,slocation,sgroup;
                sname=name.getText().toString();
                susername=username.getText().toString();
                semail=email.getText().toString();
                spass=pass.getText().toString();
                smob=mob.getText().toString();
                slocation=location.getText().toString();
                if(sname.equals("") || susername.equals("") || semail.equals("") || spass.equals("") || smob.equals("") || slocation.equals("") || selectedGroup==null){
                    Toast.makeText(Register.this,"Please fill up all fields before proceeding.",Toast.LENGTH_SHORT).show();
                    return;
                }
                new BackgroundTask().execute(sname,susername,spass,semail,smob,slocation,selectedGroup);
            }
        });
    }

    class BackgroundTask extends AsyncTask<String,String,Integer>{

        int success;

        @Override
        protected Integer doInBackground(String... params) {
            String surl="http://anuranbarman.com/blooddonate/register.php";
            String name=params[0];
            String userName=params[1];
            String userPass=params[2];
            String userEmail=params[3];
            String userMob=params[4];
            String userLocation=params[5];
            String userBlood=params[6];
            try {
                URL url=new URL(surl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();
                BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                        URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(userName,"UTF-8")+"&"+
                        URLEncoder.encode("user_pass","UTF-8")+"="+URLEncoder.encode(userPass,"UTF-8")+"&"+
                        URLEncoder.encode("user_email","UTF-8")+"="+URLEncoder.encode(userEmail,"UTF-8")+"&"+
                        URLEncoder.encode("user_mob","UTF-8")+"="+URLEncoder.encode(userMob,"UTF-8")+"&"+
                        URLEncoder.encode("user_location","UTF-8")+"="+URLEncoder.encode(userLocation,"UTF-8")+"&"+
                        URLEncoder.encode("user_blood","UTF-8")+"="+URLEncoder.encode(userBlood,"UTF-8");
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
                success=jsonObject.optInt("success");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return success;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            if(success==1){
                Toast.makeText(Register.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Register.this,Login.class);
                startActivity(i);
                finish();
            }else if(success==0){
                Toast.makeText(Register.this,"Username and Email are already in use",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Register.this,"Registration went wrong.",Toast.LENGTH_SHORT).show();
            }

        }
    }


}
