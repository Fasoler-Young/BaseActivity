package com.example.baseactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AllWords extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private ArrayList<Dictionary> dictionary = new ArrayList<>();
    private  ArrayList<OneString> layouts = new ArrayList<>();
    private int AllCount, Id;
    private boolean Choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_words);

        readFile();
        Choose = false;
        Id=0;

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
                boolean Hard  = Boolean.getBoolean(bufferedReader.readLine());
                boolean Favorite = Boolean.getBoolean(bufferedReader.readLine());
                makeRow(dictionary.get(AllCount), Hard, Favorite);
                AllCount++;

            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void makeRow(Dictionary dict, boolean Hard, boolean Favorite) {
        final LinearLayout verticalLayout = findViewById(R.id.l_layout_all_words);
        OneString layout = new OneString(
                getResources().getDisplayMetrics().widthPixels/2-70,
                this,
                Id++
        );
        verticalLayout.addView(layout.layout);
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

        //layout.Num.setText(Integer.toString(dictionary.size()));//dodelat
        layout.En.setText(dict.En);
        layout.Ru.setText(dict.Ru);
        //layout.Num.setTextSize(20);
        layout.En.setTextSize(20);
        layout.Ru.setTextSize(20);

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
        if(Choose) {
            if (!layouts.get(v.getId()).onChoosen) {
                v.setBackgroundColor(getResources().getColor(R.color.choose_layout));
            } else
                v.setBackgroundColor(getResources().getColor(R.color.colorBackgroundHardStart));
        }
        layouts.get(v.getId()).onChoosen = !layouts.get(v.getId()).onChoosen;
        Toast.makeText(this, Integer.toString(v.getId()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        if(!layouts.get(v.getId()).onChoosen)
            Choose = true;
        layouts.get(v.getId()).onChoosen = !layouts.get(v.getId()).onChoosen;
        Toast.makeText(
                this,
                layouts.get(v.getId()).En.getText().toString(),
                Toast.LENGTH_SHORT).show();
        if(layouts.get(v.getId()).onChoosen)
            v.setBackgroundColor(getResources().getColor(R.color.choose_layout));
        else
            v.setBackgroundColor(getResources().getColor(R.color.colorBackgroundHardStart));
        return true;
    }
}
