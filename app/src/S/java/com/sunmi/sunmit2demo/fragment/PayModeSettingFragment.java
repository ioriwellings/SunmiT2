package com.sunmi.sunmit2demo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.sunmi.sunmit2demo.BaseFragment;
import com.sunmi.sunmit2demo.R;
import com.sunmi.sunmit2demo.dialog.PayDialog;
import com.sunmi.sunmit2demo.ui.MainActivity;
import com.sunmi.sunmit2demo.utils.SharePreferenceUtil;

public class PayModeSettingFragment extends BaseFragment{

    Switch  face;
    @Override
    protected int setView() {
        return R.layout.fragment_pay_mode_setting;
    }

    @Override
    protected void init(View view) {
        face =view.findViewById(R.id.sw_face);
        int payMode= (int) SharePreferenceUtil.getParam(getContext(), PayDialog.PAY_MODE_KEY,7);
        switch (payMode){
            case PayDialog.PAY_FACE:
                face.setChecked(true);
                break;
            case PayDialog.PAY_FACE|PayDialog.PAY_CODE|PayDialog.PAY_CASH:
                face.setChecked(false);
                break;
        }


    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        face.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SharePreferenceUtil.setParam(getContext(),PayDialog.PAY_MODE_KEY, PayDialog.PAY_FACE);
                }else {
                    if(MainActivity.isVertical){
                        SharePreferenceUtil.setParam(getContext(),PayDialog.PAY_MODE_KEY, PayDialog.PAY_FACE|PayDialog.PAY_CODE);
                    }else {
                        SharePreferenceUtil.setParam(getContext(),PayDialog.PAY_MODE_KEY, PayDialog.PAY_FACE|PayDialog.PAY_CODE|PayDialog.PAY_CASH);
                    }
                }
            }
        });

    }
}
