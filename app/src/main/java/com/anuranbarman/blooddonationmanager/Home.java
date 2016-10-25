package com.anuranbarman.blooddonationmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by anuran on 22/10/16.
 */

public class Home extends AppCompatActivity {

    public boolean hasInternet;
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();

        DataModel[] drawerItem = new DataModel[7];

        drawerItem[0] = new DataModel("My Profile");
        drawerItem[1] = new DataModel("Upcoming Blood Donation Campaign");
        drawerItem[2] = new DataModel("All Donors");
        drawerItem[3] = new DataModel("Search Donor");
        drawerItem[4] = new DataModel("Request Blood");
        drawerItem[5] = new DataModel("Logout");
        drawerItem[6] = new DataModel("About App");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(Home.this, R.layout.listview_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new MyProfile();
                break;
            case 1:
                fragment = new ActiveBloodCamp();
                break;
            case 2:
                fragment = new AllDonors();
                break;
            case 3:
                fragment = new SearchDonor();
                break;
            case 4:
                fragment = new RequestBlood();
                break;
            case 5:
                fragment = new Logout();
                break;
            case 6:
                fragment = new About();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
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
                AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(Home.this);
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
