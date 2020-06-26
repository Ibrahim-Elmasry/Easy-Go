package com.example.easygo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Objects;

public abstract class BaseActivity extends AppCompatActivity {
    private String mTitle;
    private Context mContext;
    private Class<?> mClass;
    private int mIntent;
    private static int LAUNCH_SECOND_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hideSystemUI();
        //makeStatusBarTransparent();

    }

    //handle onBackPressed ( arrow and nav )
    @Override
    public void onBackPressed() {
        backBtn(mIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                backBtn(mIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void showBackBtnForCustomToolBar(String title,Toolbar toolbar) {

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void backBtn(int intent) {
        if (intent == 1) {
            Intent i = new Intent(mContext, mClass);
            startActivity(i);
            finish();
        } else {
            finish();
        }
    }

    public void showBackBtn(String title, Context from, Class<?> to, int intent) {
        mTitle = title;
        mContext = from;
        mClass = to;
        mIntent = intent;
        setTitle(title);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void showBackBtn(String title) {

        mTitle = title;
        setTitle(title);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void customStartActivityForResult( Context from, Class<?> to){

        Intent i = new Intent(from, to);
        startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
    }
    public void customStartActivity( Context from, Class<?> to){

        Intent i = new Intent(from, to);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                //Log.e(TAG, "onActivityResult: "+ result );

                doSomethingWhenAnotherActivityFinish("1");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                doSomethingWhenAnotherActivityFinish("0");
            }
        }
    }

    public abstract void doSomethingWhenAnotherActivityFinish(String result);



}
