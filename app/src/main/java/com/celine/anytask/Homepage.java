package com.celine.anytask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import java.util.HashSet;
import java.util.Set;
import android.app.AlertDialog;
import android.widget.EditText;
import android.content.DialogInterface;

import android.widget.Toast;
import android.widget.AdapterView;

import static com.celine.anytask.R.id.listView;


public class Homepage extends AppCompatActivity {

    final String TAG = this.getClass().getName();

    ArrayList<String> eventList = new ArrayList<>();
    ArrayAdapter<String> adapter = null;
    ListView lv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String username = getIntent().getStringExtra("Username");

        TextView tv1 = (TextView) findViewById(R.id.TVusername);
        tv1.setText(username);

        eventList = getArrayVal(getApplicationContext());
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, eventList);
        lv = (ListView) findViewById(listView);
        lv.setAdapter(adapter);
        lv.setLongClickable(true);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, View view, final int position, long id) {
                String selectedItem = ((TextView) view).getText().toString();
                if (selectedItem.trim().equals(eventList.get(position).trim())) {
                    removeElement(selectedItem, position);
                } else {
                    Toast.makeText(getApplicationContext(),"Error Removing Element", Toast.LENGTH_LONG).show();
                }
                return true;
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_homepage, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        //Search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> templist = new ArrayList<>();

                for (String temp : eventList) {
                    if (temp.toLowerCase().contains(newText.toLowerCase())) {
                        templist.add(temp);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Homepage.this,
                        android.R.layout.simple_list_item_1, templist);
                lv.setAdapter(adapter);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_sort){
            Collections.sort(eventList);
            lv.setAdapter(adapter);

            return true;
        }
        if (id == R.id.action_login){
            Intent i = new Intent(Homepage.this,Login.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_add){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Your Task");
            final EditText input = new EditText(this);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    eventList.add(setCharCase(input.getText().toString()));
                    Collections.sort(eventList);
                    storeArrayVal(eventList, getApplicationContext());
                    lv.setAdapter(adapter);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String setCharCase(String ori_input){ //letters uppercase and lowercase
        if(ori_input.isEmpty())
            return ori_input;

        return ori_input.substring(0,1).toUpperCase()+ori_input.substring(1).toLowerCase();
    }

    public static void storeArrayVal(ArrayList inArrayList, Context context){ // store in Array
        Set WhatToWrite = new HashSet(inArrayList);
        SharedPreferences WordSearchPutPrefs = context.getSharedPreferences("dbArrayValues", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = WordSearchPutPrefs.edit();
        prefEditor.putStringSet("myArray", WhatToWrite);
        prefEditor.commit();
    }

    public static ArrayList getArrayVal(Context dan){
        SharedPreferences WordSearchGetPrefs = dan.getSharedPreferences("dbArrayValues",Activity.MODE_PRIVATE);
        Set tempSet = new HashSet();
        tempSet = WordSearchGetPrefs.getStringSet("myArray",tempSet);
        return new ArrayList<>(tempSet);
    }

    public void removeElement(String selectedItem, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove " + selectedItem + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eventList.remove(position);
                Collections.sort(eventList);
                storeArrayVal(eventList, getApplicationContext());
                lv.setAdapter(adapter);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    boolean twice;
    public void onBackPressed(){
        Log.d(TAG, "click");

        if(twice==true){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }

        Toast.makeText(Homepage.this,"Press back again to quit the application.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                twice=false;
                Log.d(TAG,"twice"+twice);
            }
        }, 3000);
        twice = true;
    }
}
