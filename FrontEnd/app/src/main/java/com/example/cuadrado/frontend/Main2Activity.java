package com.example.cuadrado.frontend;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.AsyncListUtil;
import android.support.v7.widget.MenuItemHoverListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by cuadrado on 06/06/2017.
 */

public class Main2Activity extends AppCompatActivity {
    TextView tv1;
    DrawerLayout drawerLayout;
    NavigationView navView;
    Toolbar appbar;


    private EditText mSearchBoxEditText;

    private TextView mUrlDisplayTextView;

    private TextView mSearchResultsTextView;
    TextView mErrorMessageTextView;
    ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tv1=(TextView)findViewById(R.id.Bienvenido);
        Bundle bundle=getIntent().getExtras();
        tv1.setText("Hola " + bundle.getString("Hola")+"   ");
       // Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
       // FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        ListView listView =(ListView)findViewById(R.id.list_view);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);

        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);
        mErrorMessageTextView =(TextView)findViewById(R.id.tv_error_message_display);
        mLoadingIndicator= (ProgressBar)findViewById(R.id.pb_loading_indicator);

        //Codigo para Darawer Layout
        drawerLayout= (DrawerLayout)findViewById(R.id.drawer_layout);
        navView = (NavigationView)findViewById(R.id.navview);
        final DrawerLayout finalDrawerLayout = drawerLayout;
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        boolean fragmentTransaction = false;
                        Fragment fragment = null;
                        switch (menuItem.getItemId()){
                            case R.id.menu_seccion_1:
                                fragment = new Fragment1();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_seccion_2:
                                fragment = new Fragment2();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_seccion_3:
                                fragment = new Fragment3();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_opcion_1:
                                Log.i("NavigationView", "Pulsada opción 1");
                                break;
                            case R.id.menu_opcion_2:
                                Log.i("NavigationView", "Pulsada opción 2");
                                break;
                        }
                        if(fragmentTransaction) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .commit();

                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                        }
                        finalDrawerLayout.closeDrawers();

                        return true;
                    }
                }
        );
        appbar = (Toolbar)findViewById(R.id.appbar);
        //setSupportActionBar(appbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.logo_upiita);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public class GithubQueryTask extends AsyncTask<URL, Void, String>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(URL... urls){
            URL searchUrl = urls[0];
            String githubSearchResults = null;
            try{
                githubSearchResults= NetworkUtils.getResponseFromHttpUrl(searchUrl);
            }catch (IOException e){
                e.printStackTrace();
            }
            return githubSearchResults;
        }
        @Override
        protected void onPostExecute(String githubSearchResults){
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(githubSearchResults != null && !githubSearchResults.equals("")){
                showJsonDataView();
                mSearchResultsTextView.setText(githubSearchResults);
            }else{
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // COMPLETED (9) Within onCreateOptionsMenu, use getMenuInflater().inflate to inflate the menu
        getMenuInflater().inflate(R.menu.main, menu);
        // COMPLETED (10) Return true to display your menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemThatWasClickedId= item.getItemId();
        if(itemThatWasClickedId == R.id.action_search){
            Context context=Main2Activity.this;
            String textToShow ="SearchClicked";
            Toast.makeText(context,textToShow, Toast.LENGTH_SHORT).show();
            return true;
        }
        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
    private void makeGithubSearchQuery(){
        String githubQuery = mSearchBoxEditText.getText().toString();
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        mUrlDisplayTextView.setText(githubSearchUrl.toString());
        String githubSearchResults = null;
        new GithubQueryTask().execute(githubSearchUrl);
        /*try{
            githubSearchResults =NetworkUtils.getResponseFromHttpUrl(githubSearchUrl);
            mSearchResultsTextView.setText(githubSearchResults);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    private void showJsonDataView(){
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage(){
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

}