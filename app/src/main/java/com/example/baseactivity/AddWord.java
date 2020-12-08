package com.example.baseactivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import static android.content.Context.MODE_APPEND;


public class AddWord extends Fragment {

    private EditText Ru, En;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context context = getActivity().getApplicationContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundResource(R.drawable.fr_add_word);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(100, 100, 100, 0);

        LinearLayout.LayoutParams btn_param = new LinearLayout.LayoutParams(150, 150);
        btn_param.setMargins(300, 50, 50, 50);


        En = new EditText(context);
        En.setHint("En");
        En.setId(R.id.en_txt);
        En.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        En.setHintTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
        En.setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));

        Ru = new EditText(context);
        Ru.setHint("Ru");
        Ru.setId(R.id.ru_txt);
        Ru.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        Ru.setHintTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
        Ru.setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));

        /*Button AddToDictionary = new Button(context);
        AddToDictionary.setText("  Добавить в словарь  ");
        AddToDictionary.setBackgroundResource(R.drawable.words_card);
        AddToDictionary.setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
        */
        ImageView AddToDictionary = new ImageView(context);
        AddToDictionary.setId(R.id.btn_add_word);

        AddToDictionary.setBackgroundResource(R.drawable.btn_add_word);

        layout.addView(En, lp);
        layout.addView(Ru, lp);
        layout.addView(AddToDictionary, btn_param);


        return layout;
    }

    private void writeToFile(String s_en, String s_ru) {
        try{
            FileWriter writer = new FileWriter("dictionary.txt", true);
            writer.write(s_en + "\n" + s_ru + "\n" + "0\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}