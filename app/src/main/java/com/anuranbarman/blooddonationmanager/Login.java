package com.anuranbarman.blooddonationmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by anuran on 22/10/16.
 */

public class Login extends Activity {
    public boolean hasInternet;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    EditText etusername,etpassword;
    Button btnLogin,btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        etusername=(EditText)findViewById(R.id.etLoginUsername);
        etpassword=(EditText)findViewById(R.id.etLoginPassword);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnRegister=(Button)findViewById(R.id.btnRegister);
        new CheckInternet().execute();
        prefs=getSharedPreferences("mypref", Context.MODE_PRIVATE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username,password;
                username=etusername.getText().toString();
                password=etpassword.getText().toString();
                new LoginTask().execute(username,password);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(Login.this,Register.class);
                startActivity(i);
                finish();
            }
        });

    }

    class LoginTask extends AsyncTask<String,Integer,Integer>{

        int success;
        String username,password;
        @Override
        protected Integer doInBackground(String... params) {

            username=params[0];
            password=params[1];
            String surl="http://anuranbarman.com/blooddonate/login.php";
            try {
                URL url=new URL(surl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();
                BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("user_pass","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
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
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(success==1){
                editor=prefs.edit();
                editor.putString("username",username);
                editor.putString("password",password);
                editor.putBoolean("isLogged",true);
                editor.commit();
                Intent i = new Intent(Login.this,Home.class);
                startActivity(i);
            }else if(success==0){
                Toast.makeText(Login.this,"Login failed.Please try again",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public boolean hasActiveInternetConnection() {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("CHECK", "Error checking internet connection", e);
            }
        } else {
            Log.d("CHECK", "No network available!");
        }
        return false;
    }

    class CheckInternet extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            hasInternet=hasActiveInternetConnection();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(hasInternet==false){
                AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(Login.this);
                alerDialogBuilder.setMessage("The app does not work without Internet.Please connect to Internet.");
                alerDialogBuilder.setCancelable(false);
                alerDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                AlertDialog dialog = alerDialogBuilder.create();
                dialog.show();
            }
        }
    }
}
