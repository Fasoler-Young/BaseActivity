package com.example.baseactivity;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;

public class ViewAll extends AppCompatActivity {

    private  ArrayList<OneString> layouts = new ArrayList<>();
    private ArrayList<Dictionary> dictionary = new ArrayList<>();
    private ArrayList<String> groups = new ArrayList<>();
    private LinearLayout scrollView;
    private Toolbar toolbar;
    private int Id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        scrollView = findViewById(R.id.l_view_all);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        File file = getFilesDir();
        read_all(file);
        int x = (int) Math.random();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_all_menu, menu);



        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        android.widget.SearchView searchView = (android.widget.SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
// Здесь можно указать будет ли строка поиска изначально развернута или свернута в значок
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            TextView textView = findViewById(R.id.text);
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                Toast.makeText(ViewAll.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.equals(""))
                    refreshScrollLayout();
                else
                    search(newText);

                return false;
            }
        });
        return true;
    }

    private void makeRow(Dictionary dict) {
        OneString layout = new OneString(
                getResources().getDisplayMetrics().widthPixels/2-70,
                this,
                Id++
        );
        /*LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setId(Id++);
        LinearLayout.LayoutParams ll =new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        ll.setMargins(20, 20, 20, 0);
        layout.setLayoutParams(ll);

        LinearLayout.LayoutParams lp_en = new LinearLayout.LayoutParams(
                getResources().getDisplayMetrics().widthPixels/2-50,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        lp_en.setMargins(20,0, 0, 2);
        LinearLayout.LayoutParams lp_ru = new LinearLayout.LayoutParams(
                getResources().getDisplayMetrics().widthPixels/2-50,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        ViewGroup.LayoutParams lp_num = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        lp_ru.setMargins(0,0, 20, 2);

        TextView Number = new TextView(this);
        TextView En = new TextView(this);
        TextView Ru = new TextView(this);
        Number.setGravity(Gravity.START);
        En.setGravity(Gravity.CENTER_HORIZONTAL);
        Ru.setGravity(Gravity.CENTER_HORIZONTAL);
        Number.setText(Integer.toString(dictionary.size()));//dodelat
        En.setText(dict.En);
        Ru.setText(dict.Ru);
        /*Number.setTextSize(R.dimen.all_words);
        En.setTextSize(R.dimen.all_words);
        Ru.setTextSize(R.dimen.all_words);*/

        layout.En.setText(dict.En);
        layout.Ru.setText(dict.Ru);
        layout.En.setTextColor(getResources().getColor(R.color.colorFrameCard));
        layout.Ru.setTextColor(getResources().getColor(R.color.colorFrameCard));
        layout.En.setTextSize(20);
        layout.Ru.setTextSize(20);

        if(dict.Favorite){
            layout.En.setTypeface(null, Typeface.ITALIC);
            layout.Ru.setTypeface(null, Typeface.ITALIC);
        }
        if(dict.Hard)
            layout.layout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundHardStart));
        /*layout.setOnClickListener(this);
        layout.setOnLongClickListener(this);
        verticalLayout.addView(layout);
        layout.addView(Number, lp_num);
        layout.addView(En, lp_en);
        layout.addView(Ru, lp_ru);*/

        //layout.layout.setOnLongClickListener(this);
        //layout.layout.setOnClickListener(this);
        scrollView.addView(layout.layout);
        layouts.add(layout);

    }

    private void search(String query) {
        ArrayList<Dictionary> result_search = new ArrayList<>();
        for(int i=0;i<dictionary.size();i++)
            if(dictionary.get(i).En.startsWith(query) | dictionary.get(i).Ru.startsWith(query))
                result_search.add(dictionary.get(i));
        sort_by_en(result_search);
        scrollView.removeAllViews();
        //AddScroll(query);

        layouts.clear();
        for(int i=0;i<result_search.size();i++) {
            makeRow(result_search.get(i));
            //scrollView.addView(layouts.get(i).layout);
        }
    }

    private void AddScroll(String query) {
        ScrollView sc=new ScrollView(this);
        for(int i=0;i<dictionary.size();i++)

            //sc.addView();

        scrollView.addView(sc);

    }

    private void refreshScrollLayout(){
        recreate();//research id clear
        //scrollView.removeAllViews();
        //scrollView.addView(findViewById(R.id.t_l_view_all));
        //setContentView(R.layout.activity_view_all);

    }
    private void read_all(File file) {
        //File dir=getFilesDir();
        //for(String file : dir.list()){
        if(file.isFile() & file.getName().endsWith(".txt")){
            try{
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader reader = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String word;
                while( ( word = bufferedReader.readLine())!=null){
                    dictionary.add(new Dictionary(word.split(";")));
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }else if(file.isDirectory()){
            if(groups.size()>0) {
                if (!groups.get(groups.size() - 1).equals(file.getName()))
                    groups.add(file.getName());
            }else
                groups.add(file.getName());
            for(File file1 : file.listFiles())
                read_all(file1);
        }


    }


    private void sort_by_en(ArrayList<Dictionary> arrayList){
        for(int i=arrayList.size()-1; i>0;i--) {
            for (int j = 0; j < i; j++) {
                if (arrayList.get(j).En.compareTo(arrayList.get(j + 1).En) > 0) {
                    Dictionary temp = new Dictionary(arrayList.get(j));
                    arrayList.set(j, arrayList.get(j + 1));
                    arrayList.set(j + 1, temp);
                }
            }
        }
    }

    private void sort_by_group(ArrayList<Dictionary> arrayList){
        for(int i=arrayList.size()-1; i>0;i--) {
            for (int j = 0; j < i; j++) {

            }
        }
    }
}






