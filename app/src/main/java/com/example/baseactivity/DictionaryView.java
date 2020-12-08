package com.example.baseactivity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DictionaryView extends AppCompatActivity
        implements View.OnLongClickListener, View.OnClickListener {

    private ArrayList<Dictionary> dictionary = new ArrayList<>();
    private  ArrayList<OneString> layouts = new ArrayList<>();
    private ArrayList<Boolean> selected=new ArrayList<>();
    private int AllCount, Id;
    private int Choose;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_view);

        //Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_d_v);
        //setSupportActionBar(mActionBarToolbar);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);



        //if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//
  //          String query = intent.getStringExtra(SearchManager.QUERY);
    //        mQueryTextView.setText(query);
//
  //          SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
    //                MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
      //      suggestions.saveRecentQuery(query, null);
        //}

        readFile();
        Choose = 0;
        Id=0;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dictionary_view_menu, menu);



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
                Toast.makeText(context, query, Toast.LENGTH_SHORT).show();
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

    private void refreshScrollLayout(){
        final LinearLayout scrollView = findViewById(R.id.l_dictionary_view);
        scrollView.removeAllViews();
        for(int i=0;i<layouts.size();i++)
            scrollView.addView(layouts.get(i).layout);
    }

    private void search(String query) {
        final LinearLayout scrollView = findViewById(R.id.l_dictionary_view);
        ArrayList<Integer> result_search_index = new ArrayList<>();
        for(int i=0;i<dictionary.size();i++)
            if(dictionary.get(i).En.startsWith(query) | dictionary.get(i).Ru.startsWith(query))
                result_search_index.add(i);
        scrollView.removeAllViews();

        for(int i=0;i<result_search_index.size();i++)
            /*makeRow(dictionary.get(result_search_index.get(i)),
                    dictionary.get(result_search_index.get(i)).Hard,
                    dictionary.get(result_search_index.get(i)).Favorite);*/
            scrollView.addView(layouts.get(result_search_index.get(i)).layout);
    }

    private void readFile() {
        AllCount=0;
        String Word;
        try {
            FileInputStream fileInputStream = openFileInput("dictionary.txt");
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            while ((Word = bufferedReader.readLine()) != null) {
                dictionary.add(new Dictionary(Word, bufferedReader.readLine(), bufferedReader.readLine()));
                selected.add(Boolean.FALSE);
                dictionary.get(AllCount).Hard  = bufferedReader.readLine().equals("true");
                dictionary.get(AllCount).Favorite = bufferedReader.readLine().equals("true");
                makeRow(dictionary.get(AllCount), dictionary.get(AllCount).Hard, dictionary.get(AllCount).Favorite);
                AllCount++;

            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void makeRow(Dictionary dict, boolean Hard, boolean Favorite) {
        final LinearLayout scrollView = findViewById(R.id.l_dictionary_view);
        OneString layout = new OneString(
                getResources().getDisplayMetrics().widthPixels/2-70,
                this,
                Id++
        );
        scrollView.addView(layout.layout);
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

        if(Favorite){
            layout.En.setTypeface(null, Typeface.ITALIC);
            layout.Ru.setTypeface(null, Typeface.ITALIC);
        }
        if(Hard)
            layout.layout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundHardStart));
        /*layout.setOnClickListener(this);
        layout.setOnLongClickListener(this);
        verticalLayout.addView(layout);
        layout.addView(Number, lp_num);
        layout.addView(En, lp_en);
        layout.addView(Ru, lp_ru);*/
        layout.layout.setOnLongClickListener(this);
        layout.layout.setOnClickListener(this);

        layouts.add(layout);

    }

    @Override
    public void onClick(View v) {
        if(Choose !=0) {
            if (!layouts.get(v.getId()).onChoosen) {
                v.setBackgroundColor(getResources().getColor(R.color.choose_layout));
                Choose++;
            } else {
                if(dictionary.get(v.getId()).Hard)
                    v.setBackgroundColor(getResources().getColor(R.color.colorBackgroundHardStart));
                else
                    v.setBackgroundColor(getResources().getColor(R.color.background_all_word));
                Choose--;
            }
            layouts.get(v.getId()).onChoosen = !layouts.get(v.getId()).onChoosen;

        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(Choose == 0){

            Choose++;
            layouts.get(v.getId()).onChoosen = !layouts.get(v.getId()).onChoosen;
            v.setBackgroundColor(getResources().getColor(R.color.choose_layout));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(Choose!=0){

            int id = item.getItemId();

            switch (id){
                case R.id.action_settings:
                    delete_selected();
                    break;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void delete_selected() {

        AlertDialog.Builder a_builder = new AlertDialog.Builder(DictionaryView.this);
        a_builder.setMessage("Вы действительно хоитите удалить "+make_correct_string(Choose)+"?")
                .setCancelable(false)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int count_delete=0, i=0;
                        while(true) {
                            if (layouts.get(i).onChoosen) {
                                layouts.remove(i);
                                dictionary.remove(i--);
                                count_delete++;
                            }       //Toast.makeText(this,"Test", Toast.LENGTH_SHORT).show();
                            if(count_delete==Choose)
                                break;
                            i++;
                        }
                        Choose=0;
                        refresh_file();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Удаление слов");
        alert.show();


    }

    private void refresh_file() {
            try {
                FileOutputStream F_W = openFileOutput("dictionary.txt", MODE_PRIVATE);
                for(int i=0; i<dictionary.size();i++) {
                    F_W.write((dictionary.get(i).En + "\n").getBytes());
                    F_W.write((dictionary.get(i).Ru + "\n").getBytes());
                    F_W.write((dictionary.get(i).rating + "\n").getBytes());
                    F_W.write((dictionary.get(i).Hard + "\n").getBytes());
                    F_W.write((dictionary.get(i).Favorite + "\n").getBytes());
                }
                F_W.close();
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

        refreshScrollLayout();

    }

    private String make_correct_string(int choose) {
        if(choose%10==1)
            return "1 объект";
        else if(choose%10>1 & choose%10<5)
            return Integer.toString(choose)+" объекта";
        return Integer.toString(choose)+" объектов";
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DictionaryView.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}

