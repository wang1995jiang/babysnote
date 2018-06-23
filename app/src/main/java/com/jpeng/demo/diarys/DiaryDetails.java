package com.jpeng.demo.diarys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jpeng.demo.ActivityCollector;
import com.jpeng.demo.R;

public class DiaryDetails extends AppCompatActivity implements View.OnClickListener {

    TextView time,weather,mood;
    EditText editText;
    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_details);

        ActivityCollector.addActivity(this);

        back=(LinearLayout)findViewById(R.id.back_toolbar);
        time=(TextView) findViewById(R.id.time_diary_detail);
        weather=(TextView) findViewById(R.id.weather_diary_detail);
        mood=(TextView) findViewById(R.id.mood_diary_detail);
        editText=(EditText) findViewById(R.id.content_diary_detail);

        time.setText(Diary.diaryEntityClick.getTime());
        weather.setText(Diary.diaryEntityClick.getWeather());
        mood.setText(Diary.diaryEntityClick.getMood());
        editText.setText(Diary.diaryEntityClick.getContent());

        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.back_toolbar){
            finish();
            ActivityCollector.removeActivity(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
