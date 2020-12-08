package com.example.baseactivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

//проверить работы рпи отсутствии слов
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnTouchListener {

    private TextView HardWordCount, AllWordCount, NewWordCount, FavoriteWordCount;
    private Button Card;
    private ViewGroup MoveCard;
    private float dX, dY;
    private ImageView AddWord;
    private int HardCount, FavoriteCount, NewCount, AllCount, ActualIndex, countOfWords;
    private int [] Sequence;
    private boolean fr_add_word_exist= false, EnToRu;
    public ArrayList<Dictionary> dictionaries = new ArrayList<Dictionary>();
    private FragmentManager fm_add_word = getFragmentManager();
    private learningMode Mode;

    enum learningMode{
        Hard, All, Favorite, New
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Mode = learningMode.All;


        NewCount = 0;
        HardCount=0;
        FavoriteCount = 0;
        AllCount = 0;
        EnToRu = true;
        ActualIndex = 0;

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //read_all();

        String Word;
        try {
            FileInputStream fileInputStream = openFileInput("dictionary.txt");
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuffer stringBuffer = new StringBuffer();

            while ((Word = bufferedReader.readLine()) != null) {
                dictionaries.add(new Dictionary(Word, bufferedReader.readLine(), bufferedReader.readLine()));
                if(bufferedReader.readLine().equals("true")){
                    HardCount++;
                    dictionaries.get(AllCount-1).Hard=true;
                }
                if(bufferedReader.readLine().equals("true")){
                    FavoriteCount++;
                    dictionaries.get(AllCount).Favorite=true;
                }
                if(NewCount < 11)
                    NewCount++;
                AllCount++;
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }



        Card = findViewById(R.id.txt_card);
        RelativeLayout relativeLayout = findViewById(R.id.move);
        Sequence =new int[(int) (1.5*AllCount)];
        makeSequence();
        if(AllCount>0){
            checkSize(dictionaries.get(Sequence[ActualIndex]).En);
            Card.setText(dictionaries.get(Sequence[ActualIndex++]).En);
        }
        else
            Card.setText(R.string.no_words);

        if(!fr_add_word_exist)
            Card.setOnTouchListener(this);



        HardWordCount = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_hard_mode));
        initializeCountDrawer(HardWordCount);
        HardWordCount.setText(Integer.toString(HardCount));
        AllWordCount = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_all_words));
        initializeCountDrawer(AllWordCount);
        AllWordCount.setText(Integer.toString(AllCount));
        FavoriteWordCount = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_favorites_words));
        initializeCountDrawer(FavoriteWordCount);
        FavoriteWordCount.setText(Integer.toString(FavoriteCount));
        NewWordCount = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_new_words_mode));
        initializeCountDrawer(NewWordCount);
        NewWordCount.setText(Integer.toString(NewCount));
    }

    private void read_all() {
        File dir=getFilesDir();
        for(String file : dir.list()){
            if(file.endsWith(".txt"))
                try{
                    FileInputStream fis = openFileInput(file);
                    InputStreamReader reader = new InputStreamReader(fis);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String word;
                    while( ( word = bufferedReader.readLine())!=null){
                        dictionaries.add(new Dictionary(word.split(";")));
                    }

                }catch (IOException e){
                    e.printStackTrace();
                }
        }
    }

    private void write_all(){

    }

    private void NextCard() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.disappear);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(ActualIndex >= countOfWords) {
                    ActualIndex -= countOfWords;
                    shuffleArray(Sequence, countOfWords);
                }
                EnToRu = true;
                float dimen = 60;
                String word = (AllCount>0) ? (dictionaries.get(Sequence[ActualIndex++]).En) : getString(R.string.no_words);
                checkSize(word);
                Card.setText(word);
                Card.animate()
                        .x(79)
                        .y(395)
                        .setDuration(0)
                        .start();
                Card.startAnimation(AnimationUtils.loadAnimation(Card.getContext(), R.anim.appear));
        }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        Card.startAnimation(animation);

    }

    private void checkSize(String string) {
        int longestWord = 0;
        for(int i=0; i< string.length(); i++){
            longestWord++;
            if(string.charAt(i) == ' ')
                longestWord = 0;

        }
        float dimen = 60.0F;
        if(longestWord>7)
            dimen*= 7.0F/longestWord;
        if(string.length()>20)
            dimen = 60.0F*20/string.length();
        Card.setTextSize(dimen);
    }

    private  void shuffleArray(int[] ar, int actualLength) {
        Random rnd = new Random();
        for (int i = actualLength - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    private void initializeCountDrawer(TextView textView) {
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(fr_add_word_exist) {
            fr_add_word_exist=false;

            EditText Ru = findViewById(R.id.ru_txt), En = findViewById(R.id.en_txt);
            LinearLayout layout = findViewById(R.id.l_add_word);
            Ru.setText("");
            En.setText("");
            layout.removeAllViews();
            Card.setVisibility(View.VISIBLE);
            Card.startAnimation(AnimationUtils.loadAnimation(this, R.anim.appear));

        }else {
            AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);
            a_builder.setMessage("Вы действительно хоитите уйти?")
                    .setCancelable(false)
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = a_builder.create();
            alert.setTitle("Закрыть приложение?");
            alert.show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_saw_all_words) {
            finish();
            Intent intent = new Intent(MainActivity.this, DictionaryView.class);
            startActivity(intent);
        }else if(id == R.id.nav_all_words) {
            Mode = learningMode.All;
            makeSequence();
        }else if(id == R.id.nav_new_words_mode){
            Mode = learningMode.New;
            Intent intent = new Intent(MainActivity.this, ViewAll.class);
            startActivity(intent);
            makeSequence();
        }else if(id == R.id.nav_hard_mode){
            Mode = learningMode.Hard;
            read_and_write();
            if(HardCount < 3)
                Toast.makeText(this, "Трудных слов мало", Toast.LENGTH_SHORT).show();
            else
                makeSequence();
        }else if(id == R.id.nav_favorites_words){
            Mode = learningMode.Favorite;
            makeSequence();
        }else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void read_and_write() {
        try{
            //getAssets().open("wifi2.txt")
            //String file = "Example1.txt";
            //FileInputStream fis = openFileInput(file);
            InputStreamReader reader = new InputStreamReader(getAssets().open("Example1.txt"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String word, temp="";
            ArrayList<String> str = new ArrayList<>();
            while( ( word = bufferedReader.readLine())!=null){
                word=word.substring(0, word.length()-4);
                if(!word.endsWith(temp) & !temp.equals("")){
                    write_example(str);
                    str.clear();
                }
                //word=word.replace(';', '/');
                str.add(word);
                temp=word.substring(word.lastIndexOf(';')+1);

            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void write_example(ArrayList<String> str) {
        String firstWord = str.get(0).replace(';', '/');
        String file_name = firstWord.substring(firstWord.lastIndexOf('/')+1)+".txt";
        int index=0;
        for(int i=0; i<6;i++)
            index=firstWord.indexOf('/', index+1);
        String path_name = firstWord.substring(index+1, firstWord.lastIndexOf('/'));
        boolean b=false;

        File path =new File(("/data/data/com.example.baseactivity/files"+File.separator+ path_name));
        b = path.mkdirs();
        path = new File("/data/data/com.example.baseactivity/files" + File.separator + path_name ,file_name);


        try{
            FileOutputStream fw = new FileOutputStream(path, true);
            b=path.exists();
            b=path.isFile();
            for(String word : str) {
                fw.write((word+"\n").getBytes());
            }
            fw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        //File file = new File();

        String word;
        try {
            FileInputStream fileInputStream=new FileInputStream(path);
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            word= bufferedReader.readLine();
            fileInputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }


    }

    public void NewWord(MenuItem item) {
        if (!fr_add_word_exist) {

            fr_add_word_exist=true;

            Card.setVisibility(View.INVISIBLE);

            final EditText Ru, En;
            Context context = this;
            final LinearLayout layout = findViewById(R.id.l_add_word);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER);
            layout.setBackgroundResource(R.drawable.fr_add_word);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(100, 100, 100, 0);

            LinearLayout.LayoutParams btn_param = new LinearLayout.LayoutParams(150, 150);
            btn_param.setMargins(300, 50, 50, 50);

            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(30);

            En = new EditText(context);
            En.setHint("En");
            En.setId(R.id.en_txt);
            En.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            En.setHintTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
            En.setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
            En.setFilters(FilterArray);

            Ru = new EditText(context);
            Ru.setHint("Ru");
            Ru.setId(R.id.ru_txt);
            Ru.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            Ru.setHintTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
            Ru.setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
            Ru.setFilters(FilterArray);

        /*Button AddToDictionary = new Button(context);
        AddToDictionary.setText("  Добавить в словарь  ");
        AddToDictionary.setBackgroundResource(R.drawable.words_card);
        AddToDictionary.setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
        */
            ImageView AddToDictionary = new ImageView(context);
            AddToDictionary.setId(R.id.btn_add_word);

            AddToDictionary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String en=En.getText().toString(), ru = Ru.getText().toString();
                    if(!en.equals("") && !ru.equals("")){
                        writeToFile(en, ru);
                        fr_add_word_exist=false;
                        Card.setVisibility(View.VISIBLE);
                        Ru.setText("");
                        En.setText("");
                        layout.removeAllViews();
                    }

                }
            });
            AddToDictionary.setBackgroundResource(R.drawable.btn_add_word);

             LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
             layoutParams.setMargins(100, 200, 100, 0);

            layout.addView(En, lp);
            layout.addView(Ru, lp);
            layout.addView(AddToDictionary, btn_param);
            //setContentView(layout, layoutParams);


        }
    }
//change write
    private void writeToFile(String en, String ru) {//change write
        dictionaries.add(new Dictionary(en, ru, "0"));
        AllCount++;
        if(Sequence.length < AllCount)
            resize();
        else
            makeSequence();

        if(NewCount < 11)
            NewCount++;
        AllWordCount.setText(Integer.toString(AllCount));
        NewWordCount.setText(Integer.toString(NewCount));
        try {
            FileOutputStream F_W = openFileOutput("dictionary.txt", MODE_APPEND);
            F_W.write((en + "\n").getBytes());
            F_W.write((ru + "\n").getBytes());
            F_W.write(("0\n").getBytes());
            F_W.write(("false\nfalse\n").getBytes());
            F_W.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void resize() {
        Sequence = new int[(int) (1.5*AllCount)];
        makeSequence();
    }

    private void makeSequence() {

        switch (Mode){
            case All:
                for(int i=0; i < AllCount; i++)
                    Sequence[i]=i;
                countOfWords = AllCount;
                break;
            case New:
                for(int i=0; i<NewCount; i++)
                    Sequence[i]=i;
                countOfWords = NewCount;
                break;
            case Hard:
                int count = 0, i= 0;
                while(count < HardCount)
                    if(dictionaries.get(i).Hard)
                        Sequence[count] = i++;
                    countOfWords = HardCount;
                break;
            case Favorite:
                int count2 = 0, i2= 0;
                while(count2 < FavoriteCount)
                    if(dictionaries.get(i2).Favorite)
                        Sequence[count2] = i2++;
                    countOfWords = FavoriteCount;
                break;
        }
        shuffleArray(Sequence, countOfWords);

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        view.performClick();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                Card.animate()
                        .x(event.getRawX() + dX)
                        .y(event.getRawY() + dY)
                        .setDuration(0)
                        .start();
                break;
            case MotionEvent.ACTION_UP:
                if (Card.getY() < 125.0)
                    NextCard();
                else if(Card.getY() > 670)
                    RollCard();
                else {
                    view.animate()
                            .x(79)
                            .y(395)
                            .setDuration(500)
                            .start();
                }
                break;
            default:
                return true;
        }
        return true;
    }

    private void RollCard() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.roll);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String Word;
                if(EnToRu)
                    Word = (AllCount>0) ? (dictionaries.get(Sequence[ActualIndex-1]).Ru) : getString(R.string.no_words_ru);
                else
                    Word = (AllCount>0) ? (dictionaries.get(Sequence[ActualIndex-1]).En) : getString(R.string.no_words);
                checkSize(Word);
                Card.setText(Word);
                Card.startAnimation(AnimationUtils.loadAnimation(Card.getContext(), R.anim.roll_back));
                EnToRu = !EnToRu;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        Card.startAnimation(animation);
        Card.animate()
                .x(79)
                .y(395)
                .setDuration(1500)
                .start();

        //File dir=getFilesDir();
        //File file=new File(dir.getAbsolutePath()+"/"+dir.list()[0]);
        //Toast.makeText(this, Integer.toString(dir.list().length)+ dir.isFile()+"  "+String.valueOf(file.exists()),Toast.LENGTH_LONG).show();
    }


}
