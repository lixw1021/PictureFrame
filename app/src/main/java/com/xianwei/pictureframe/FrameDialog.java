package com.xianwei.pictureframe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xianwei li on 11/27/2017.
 */

public class FrameDialog extends Dialog {
    @BindView(R.id.check_black_frame)
    ImageView blackFrameChecker;
    @BindView(R.id.check_red_frame)
    ImageView redFrameChecker;
    @BindView(R.id.check_green_frame)
    ImageView greenFrameChecker;
    @BindView(R.id.check_purple_frame)
    ImageView purpleFrameChecker;

    public FrameDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_frame);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.check_black_frame)
    void checkBlackFrame(){
        Log.i("12345","black clicked");
        if (blackFrameChecker.getVisibility() == View.INVISIBLE) {
            switchToBlackFrame();
        } else {
            blackFrameChecker.setVisibility(View.INVISIBLE);
        }
    }

    private void switchToBlackFrame() {
        blackFrameChecker.setVisibility(View.VISIBLE);
        redFrameChecker.setVisibility(View.INVISIBLE);
        greenFrameChecker.setVisibility(View.INVISIBLE);
        purpleFrameChecker.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.check_red_frame)
    void checkRedFrame(){
        if (redFrameChecker.getVisibility() == View.INVISIBLE) {
            switchToRedFrame();
        } else {
            redFrameChecker.setVisibility(View.INVISIBLE);
        }
    }

    private void switchToRedFrame() {
        blackFrameChecker.setVisibility(View.INVISIBLE);
        redFrameChecker.setVisibility(View.VISIBLE);
        greenFrameChecker.setVisibility(View.INVISIBLE);
        purpleFrameChecker.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.check_green_frame)
    void checkGreenFrame(){
        if (greenFrameChecker.getVisibility() == View.INVISIBLE) {
            switchToGreenFrame();
        } else {
            greenFrameChecker.setVisibility(View.INVISIBLE);
        }
    }

    private void switchToGreenFrame() {
        blackFrameChecker.setVisibility(View.INVISIBLE);
        redFrameChecker.setVisibility(View.INVISIBLE);
        greenFrameChecker.setVisibility(View.VISIBLE);
        purpleFrameChecker.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.check_purple_frame)
    void checkPurpleFrame(){
        if (purpleFrameChecker.getVisibility() == View.INVISIBLE) {
            switchToPurpleFrame();
        } else {
            purpleFrameChecker.setVisibility(View.INVISIBLE);
        }
    }

    private void switchToPurpleFrame() {
        blackFrameChecker.setVisibility(View.INVISIBLE);
        redFrameChecker.setVisibility(View.INVISIBLE);
        greenFrameChecker.setVisibility(View.INVISIBLE);
        purpleFrameChecker.setVisibility(View.VISIBLE);
    }
}
