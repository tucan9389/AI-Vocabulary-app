package org.andoidtown.ai_vocabulary.mainactivity_component;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.andoidtown.ai_vocabulary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    ViewPager mainVP;
    PagerAdapter pagerAdapter;
    ArrayList<Button> pagerButtonList;
    Button graphTapButton;
    Button testTapButton;
    Button managementTapButton;
    SQLiteDatabase db;
    Fragment achievementFragment;
    Fragment wordTestFragment;
    Fragment managementWordFragment;
    static SimpleDateFormat format;
    public static final String databaseName = "vocabularyDataBase";
    boolean isFirst = true;
    @Override
    protected void onResume()
    {
        super.onResume();
        pagerAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainVP = findViewById(R.id.mainViewPager);
        graphTapButton = findViewById(R.id.growthGraphTapButton);
        testTapButton = findViewById(R.id.wordTestTapButton);
        managementTapButton = findViewById(R.id.managementWordsTapButton);

        pagerButtonList = new ArrayList<>();
        pagerButtonList.add(graphTapButton);
        pagerButtonList.add(testTapButton);
        pagerButtonList.add(managementTapButton);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mainVP.setAdapter(pagerAdapter);
        mainVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                /*if (i == 1)
                {
                    if(wordTestFragment != null)
                    {
                        ((WordTestFragment)wordTestFragment).setButtonText();
                        ((WordTestFragment)wordTestFragment).setStatusMyProgress();
                    }
                }*/
            }

            @Override
            public void onPageSelected(int i) {
                setButtonNotSelected(pagerButtonList.get(i));
                setButtonSelected(pagerButtonList.get(i));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mainVP.setCurrentItem(1);

        graphTapButton.setOnClickListener(movePageListener);
        graphTapButton.setTag(0);
        testTapButton.setOnClickListener(movePageListener);
        testTapButton.setTag(1);
        managementTapButton.setOnClickListener(movePageListener);
        managementTapButton.setTag(2);

        for(int i = 0; i < pagerButtonList.size(); i++)
        {
            pagerButtonList.get(i).setTextColor(getResources().getColor(R.color.colorWhite));
        }
        if ( createDatabase(databaseName) ) {
            createWordTable();
            createWordGroupTable();
            createWordTestTable();
            createIncorrectWordListTable();
        }
        AdjustDateManager adjustDateManager = new AdjustDateManager();
        adjustDateManager.adjustTestDate();
    }
    private boolean createDatabase(String databaseName)
    {
        try{

            db = openOrCreateDatabase(databaseName,MODE_PRIVATE,null);
        } catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    private void createWordTable()
    {
        try{
            db.execSQL("create table word(" +
                    "value text," +
                    "meaning text," +
                    "correct_answer_num integer," +
                    "incorrect_answer_num integer," +
                    "group_name text" +
                    "" +
                    ");");
        }
        catch(Exception ex)
        {
            Log.d("exception",ex.toString());
        }
    }
    private void createWordGroupTable()
    {
        try{
            db.execSQL("create table word_group (" +
                    "group_name text PRIMARY KEY," +
                    "registered_date datetime," +
                    "num_of_test integer," +
                    "next_test_date datetime)");
        }
        catch(Exception ex)
        {
            Log.d("exception",ex.toString());
        }
    }
    private void createWordTestTable()
    {
        try{
            db.execSQL("create table word_test (" +
                    "test_date date," +
                    "correct_answer_num integer," +
                    "incorrect_answer_num integer," +
                    "test_time datetime," +
                    "group_name text" +
                    ")");
        }
        catch (Exception ex)
        {
            Log.d("exception", ex.toString());
        }
    }
    private void createIncorrectWordListTable()
    {
        try{
            db.execSQL("create table incorrect_word (" +
                    "incorrect_word text, " +
                    "incorrect_word_meaning text, " +
                    "incorrect_word_group_name text" +
                    ")");
        }
        catch (Exception ex)
        {
            Log.d("exception", ex.toString());
        }
    }

    public SimpleDateFormat getStandardFormat()
    {
        if(format == null)
        {
            format = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        }
        return format;
    }
    public void setButtonNotSelected(Button view)
    {
        for(int i = 0; i < pagerButtonList.size(); i++)
        {
            if(pagerButtonList.get(i) != view)
            {
                pagerButtonList.get(i).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }
    }
    public void setButtonSelected(Button view)
    {
        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }
    View.OnClickListener movePageListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int tag = (int) v.getTag();

            mainVP.setCurrentItem(tag);
        }
    };


    private class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(android.support.v4.app.FragmentManager  fm)
        {
            super(fm);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position)
        {
            switch (position) {
                case 0:
                    achievementFragment = new AchievementGraphFragment();
                    return achievementFragment;
                case 1:
                    wordTestFragment = new WordTestFragment();
                    return wordTestFragment;
                case 2:
                    managementWordFragment = new ManagementWordsFragment();
                    return managementWordFragment;
                default:
                    wordTestFragment = new WordTestFragment();
                    return wordTestFragment;
            }
        }
        @Override
        public int getCount()
        {
            return 3;
        }

    }
    public SQLiteDatabase getDB()
    {
        return this.db;
    }
    public class AdjustDateManager
    {
        public void adjustTestDate()
        {
            long diffDay = getDiffLastTestDayToToday();
            if(diffDay > 1)
            {
                diffDay--;
            }
            else if(diffDay == -1)
            {
                diffDay = getDiffFirstTestDayToToday();
            }
            if(diffDay > 0)
            {
                queryToAdjustTestDates(diffDay);
            }
        }
        public long getDiffLastTestDayToToday()
        {
            long diffDay = -1;
            Date lastTestDay;
            Date today;
            if(db == null)
            {
                db = openOrCreateDatabase(databaseName,MODE_PRIVATE,null);
            }

            try
            {
                Cursor cursor = db.rawQuery("select test_date from word_test order by test_date desc limit 1",null);
                boolean isThereTestLog = (cursor.getCount() != 0);
                if(isThereTestLog)
                {
                    cursor.moveToNext();
                    lastTestDay = stringToDate(cursor.getString(0));
                    Calendar calendar = Calendar.getInstance();
                    today = stringToDate(getStandardFormat().format(calendar.getTime()));
                    long diffMillis = getDayDifference(today, lastTestDay);
                    diffDay = TimeUnit.DAYS.convert(diffMillis,TimeUnit.MICROSECONDS);
                    Log.d("diffDayInTestLog",Long.toString(diffDay));
                }
            }
            catch (Exception ex)
            {
                Log.d("adjustTestException",ex.toString());
                diffDay = -1;
            }
            return diffDay;
        }
        public long getDiffFirstTestDayToToday()
        {
            long diffDay = -1;
            Cursor cursor = db.rawQuery("SELECT next_test_date FROM word_group",null);
            boolean isFirstTest = cursor.getCount() == 1;
            if(isFirstTest) {
                cursor.moveToNext();
                Date today = stringToDate(getStandardFormat().format(Calendar.getInstance().getTime()));
                Date firstTestDay = stringToDate(cursor.getString(0));
                diffDay = getDayDifference(today, firstTestDay);
                Log.d("today",today.toString());
                Log.d("firstDay",firstTestDay.toString());
                Log.d("diffDay",Long.toString(diffDay));
            }
            return diffDay;
        }
        public Date stringToDate(String date)
        {
            Date lastTestDay = new Date();
            SimpleDateFormat dateFormat = getStandardFormat();

            if(!date.equals(""));
            {
                try {
                    Log.d("date",date);
                    lastTestDay = dateFormat.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return lastTestDay;
        }
        public long getDayDifference(Date substractee, Date substracter)
        {
            long diffMillis = substractee.getTime() - substracter.getTime();
            Log.d("diffMilli",Long.toString(diffMillis));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(diffMillis);
            long diffDay = calendar.get(Calendar.DAY_OF_MONTH) - 1;
            Log.d("diffDay",Long.toString(diffDay));
            return diffDay;
        }
        public void queryToAdjustTestDates(long diffDay)
        {
            Log.d("차이값 조정",Long.toString(diffDay));
            SQLiteDatabase db = openOrCreateDatabase(databaseName,MODE_PRIVATE,null);
            String values[] = {};
            db.execSQL("update word_group set next_test_date = datetime(next_test_date,'+"+diffDay+" day')",values);
        }
    }
}
