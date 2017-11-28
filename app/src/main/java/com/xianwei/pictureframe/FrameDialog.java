package com.xianwei.pictureframe;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


//    public FrameDialog(@NonNull Context context) {
//        super(context);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog_frame);
//        ButterKnife.bind(this);
//    }

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
        Log.i("12345","black clicked");
        if (blackFrameMarker.getVisibility() == View.INVISIBLE) {
            checkedItemName = "black";
            switchToBlackFrame();
        } else {
            blackFrameMarker.setVisibility(View.INVISIBLE);
        }
    }

    private void switchToBlackFrame() {
        blackFrameMarker.setVisibility(View.VISIBLE);
        redFrameMarker.setVisibility(View.INVISIBLE);
        greenFrameMarker.setVisibility(View.INVISIBLE);
        purpleFrameMarker.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.check_red_frame)
    void checkRedFrame(){
        Log.i("12345","Red clicked");
        if (redFrameMarker.getVisibility() == View.INVISIBLE) {
            checkedItemName = "red";
            switchToRedFrame();
        } else {
            redFrameMarker.setVisibility(View.INVISIBLE);
        }
    }

    private void switchToRedFrame() {
        blackFrameMarker.setVisibility(View.INVISIBLE);
        redFrameMarker.setVisibility(View.VISIBLE);
        greenFrameMarker.setVisibility(View.INVISIBLE);
        purpleFrameMarker.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.check_green_frame)
    void checkGreenFrame(){
        Log.i("12345","Green clicked");
        if (greenFrameMarker.getVisibility() == View.INVISIBLE) {
            checkedItemName = "green";
            switchToGreenFrame();
        } else {
            greenFrameMarker.setVisibility(View.INVISIBLE);
        }
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
            purpleFrameMarker.setVisibility(View.INVISIBLE);
        }
    }

    private void switchToPurpleFrame() {
        blackFrameMarker.setVisibility(View.INVISIBLE);
        redFrameMarker.setVisibility(View.INVISIBLE);
        greenFrameMarker.setVisibility(View.INVISIBLE);
        purpleFrameMarker.setVisibility(View.VISIBLE);
    }

    public String getCheckedItemName() {
        return checkedItemName;
    }
}
