package com.example.myapplication.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.Fragments.DetailedBusinessNewsFragment;
import com.example.myapplication.Fragments.SearchPageFragment;
import com.example.myapplication.R;

public class SearchPageActivity extends AppCompatActivity {
   public static String queryword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        queryword=getIntent().getStringExtra("query");
        setTitle("Search Results For " + queryword);
        setContentView(R.layout.activity_search_page);

        System.out.println("Search Results For " + queryword);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

}
