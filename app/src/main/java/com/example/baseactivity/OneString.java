package com.example.baseactivity;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OneString {
    public TextView En, Ru;
    public LinearLayout layout;
    public boolean onChoosen;

    OneString(int width, Context context, int Id){
        onChoosen = false;
        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setId(Id);
        LinearLayout.LayoutParams ll =new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        ll.setMargins(20, 20, 20, 0);
        layout.setLayoutParams(ll);

        LinearLayout.LayoutParams lp_en = new LinearLayout.LayoutParams(
                width, LinearLayout.LayoutParams.WRAP_CONTENT
        );

        lp_en.setMargins(20,0, 0, 2);
        LinearLayout.LayoutParams lp_ru = new LinearLayout.LayoutParams(
                width, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams lp_num = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lp_num.setMargins(10,0,10, 2);
        lp_ru.setMargins(20,0, 20, 2);

        En = new TextView(context);
        Ru = new TextView(context);

        En.setGravity(Gravity.START);
        Ru.setGravity(Gravity.START);
        layout.addView(En, lp_en);
        layout.addView(Ru, lp_ru);
    }
}
