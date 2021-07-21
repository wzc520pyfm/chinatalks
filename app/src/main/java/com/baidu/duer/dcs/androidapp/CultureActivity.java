package com.baidu.duer.dcs.androidapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.duer.dcs.R;

public class CultureActivity extends AppCompatActivity {

    private TextView mTitle,mContent;
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_culture);

        initView();

        refreshView();

    }

    private void refreshView() {
        String title=getIntent().getStringExtra("title");
        String content=getIntent().getStringExtra("content");
        int image=getIntent().getIntExtra("image",R.mipmap.ic_culture_qingming);
        mTitle.setText(title);
        mContent.setText(content);
        mImage.setImageResource(image);
    }

    private void initView() {
        mTitle= (TextView) findViewById(R.id.hichina_culture_content_title);
        mContent= (TextView) findViewById(R.id.hichina_culture_content_content);
        mImage= (ImageView) findViewById(R.id.hichina_culture_content_image);

    }

    public void back(View view) {
        this.finish();
    }
}
