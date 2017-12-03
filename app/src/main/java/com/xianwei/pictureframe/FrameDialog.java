package com.xianwei.pictureframe;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xianwei li on 11/27/2017.
 */

public class FrameDialog extends DialogFragment {
    @BindView(R.id.black_marker)
    ImageView blackFrameMarker;
    @BindView(R.id.red_marker)
    ImageView redFrameMarker;
    @BindView(R.id.green_marker)
    ImageView greenFrameMarker;
    @BindView(R.id.purple_marker)
    ImageView purpleFrameMarker;
    @BindView(R.id.check_black_frame)
    ImageView blackFrameCheckBox;
    @BindView(R.id.check_red_frame)
    ImageView redFrameCheckBox;
    @BindView(R.id.check_green_frame)
    ImageView greenFrameCheckBox;
    @BindView(R.id.check_purple_frame)
    ImageView purpleFrameCheckBox;


    private String checkedItemName;

    public static FrameDialog newInstance(String checkedItemName) {
        FrameDialog frameDialog = new FrameDialog();
        Bundle args = new Bundle();
        args.putString("checkedItemName", checkedItemName);
        frameDialog.setArguments(args);
        return frameDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_frame, container);
        ButterKnife.bind(this,view);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        checkedItemName = getArguments().getString("checkedItemName");
        setupDialog();
        return view;
    }

    private void setupDialog() {
        switch (checkedItemName) {
            case "black":
                checkBlackFrame();
                break;
            case "red":
                checkRedFrame();
                break;
            case "green":
                checkGreenFrame();
                break;
            case "purple":
                checkPurpleFrame();
            default:
                break;
        }
    }

    @OnClick(R.id.check_black_frame)
    void checkBlackFrame(){
        if (blackFrameMarker.getVisibility() == View.INVISIBLE) {
            checkedItemName = "black";
            switchToBlackFrame();
        } else {
            checkedItemName = "none";
            blackFrameMarker.setVisibility(View.INVISIBLE);
        }
        setFrame(checkedItemName);
    }

    private void switchToBlackFrame() {
        blackFrameMarker.setVisibility(View.VISIBLE);
        redFrameMarker.setVisibility(View.INVISIBLE);
        greenFrameMarker.setVisibility(View.INVISIBLE);
        purpleFrameMarker.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.check_red_frame)
    void checkRedFrame(){
        if (redFrameMarker.getVisibility() == View.INVISIBLE) {
            checkedItemName = "red";
            switchToRedFrame();
        } else {
            checkedItemName = "none";
            redFrameMarker.setVisibility(View.INVISIBLE);
        }
        setFrame(checkedItemName);
    }

    private void switchToRedFrame() {
        blackFrameMarker.setVisibility(View.INVISIBLE);
        redFrameMarker.setVisibility(View.VISIBLE);
        greenFrameMarker.setVisibility(View.INVISIBLE);
        purpleFrameMarker.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.check_green_frame)
    void checkGreenFrame(){
        if (greenFrameMarker.getVisibility() == View.INVISIBLE) {
            checkedItemName = "green";
            switchToGreenFrame();
        } else {
            checkedItemName = "none";
            greenFrameMarker.setVisibility(View.INVISIBLE);
        }
        setFrame(checkedItemName);
    }

    private void switchToGreenFrame() {
        blackFrameMarker.setVisibility(View.INVISIBLE);
        redFrameMarker.setVisibility(View.INVISIBLE);
        greenFrameMarker.setVisibility(View.VISIBLE);
        purpleFrameMarker.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.check_purple_frame)
    void checkPurpleFrame(){
        if (purpleFrameMarker.getVisibility() == View.INVISIBLE) {
            checkedItemName = "purple";
            switchToPurpleFrame();
        } else {
            checkedItemName = "none";
            purpleFrameMarker.setVisibility(View.INVISIBLE);
        }
        setFrame(checkedItemName);
    }

    private void switchToPurpleFrame() {
        blackFrameMarker.setVisibility(View.INVISIBLE);
        redFrameMarker.setVisibility(View.INVISIBLE);
        greenFrameMarker.setVisibility(View.INVISIBLE);
        purpleFrameMarker.setVisibility(View.VISIBLE);
    }

    public void setFrame(String checkedItemName){
        CameraActivity cameraActivity = (CameraActivity) getActivity();
        cameraActivity.onSelectValue(checkedItemName);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
