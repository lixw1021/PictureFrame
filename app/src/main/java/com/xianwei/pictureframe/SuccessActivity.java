package com.xianwei.pictureframe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuccessActivity extends Activity {

    @BindView(R.id.button_home)
    TextView homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_home)
    void returnMainActivity(){
        Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
