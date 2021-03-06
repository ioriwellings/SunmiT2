package com.sunmi.sunmit2demo.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sunmi.sunmit2demo.R;
import com.sunmi.sunmit2demo.utils.ResourcesUtils;

/**
 * Created by highsixty on 2018/3/14.
 * mail  gaolulin@sunmi.com
 */

public class PayDialog extends AppCompatDialogFragment implements View.OnClickListener {
    private RadioGroup radioGroup;
    private ImageView ivTop;
    private ImageView ivBottom;
    private ImageView ivLogo;
    private TextView tvDescrib;
    private TextView tvMoney;
    private Button btnCancel;
    private Button btnOk;
    private Button btnComplete;
    private LinearLayout llyPay;
    private LinearLayout llyPayComplete;
    private RadioButton rbOne, rbTwo;
    private String mMoney;
    private TextView tvMoneyComplete;
    boolean isShow = false;//防多次点击

    public PayDialog() {
        super();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.paydialog_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initAction();
        initData();
    }

    private void initView(View view) {
        radioGroup = (RadioGroup) view.findViewById(R.id.rg);
        ivTop = (ImageView) view.findViewById(R.id.iv_top);
        ivBottom = (ImageView) view.findViewById(R.id.iv_bottom);
        ivLogo = (ImageView) view.findViewById(R.id.iv_logo);
        tvDescrib = (TextView) view.findViewById(R.id.tv_describ);
        tvMoney = (TextView) view.findViewById(R.id.tv_money);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnComplete = (Button) view.findViewById(R.id.btn_complete);
        llyPay = (LinearLayout) view.findViewById(R.id.lly_pay);
        llyPayComplete = (LinearLayout) view.findViewById(R.id.lly_pay_complete);
        rbOne = (RadioButton) view.findViewById(R.id.rbone);
        rbTwo = (RadioButton) view.findViewById(R.id.rbtwo);
        tvMoneyComplete = (TextView) view.findViewById(R.id.tv_money_complete);

        rbOne.setAlpha(1f);
        rbTwo.setAlpha(0.7f);
    }

    private void initAction() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbone:
                        ivTop.setVisibility(View.VISIBLE);
                        ivBottom.setVisibility(View.GONE);
                        tvDescrib.setText(ResourcesUtils.getString(getContext(), R.string.tips_pay_money));
                        ivLogo.setImageResource(R.drawable.cash);
                        btnOk.setVisibility(View.VISIBLE);
                        rbOne.setAlpha(1f);
                        rbTwo.setAlpha(0.7f);
                        break;
                    case R.id.rbtwo:
                        ivTop.setVisibility(View.GONE);
                        ivBottom.setVisibility(View.VISIBLE);
                        tvDescrib.setText(ResourcesUtils.getString(getContext(), R.string.tips_pay_qrcode));
                        ivLogo.setImageResource(R.drawable.paycode);
                        btnOk.setVisibility(View.GONE);
                        rbOne.setAlpha(0.7f);
                        rbTwo.setAlpha(1);
                        break;
                    default:
                        break;
                }
            }
        });
        btnComplete.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void initData() {
        Bundle bundle = getArguments();
        mMoney = bundle.getString("MONEY", "0.00");
        tvMoney.setText(mMoney);
        tvMoneyComplete.setText(mMoney);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                if (completeListener != null) {
                    completeListener.onCancel();
                }
                dismiss();
                break;
            case R.id.btn_ok:
                rbOne.setClickable(false);
                rbTwo.setClickable(false);
                llyPay.setVisibility(View.GONE);
                llyPayComplete.setVisibility(View.VISIBLE);
                if (completeListener != null) {
                    completeListener.onSuccess();
                }
                break;
            case R.id.btn_complete:
                if (completeListener != null) {
                    completeListener.onComplete();
                }
                dismiss();
                break;
            default:
                break;
        }
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        if (isShow) {
            return;
        }
        super.show(manager, tag);
        isShow = true;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        isShow = false;
    }

    private OnCompleteListener completeListener = null;

    public void setCompleteListener(OnCompleteListener completeListener) {
        this.completeListener = completeListener;
    }

    public interface OnCompleteListener {
        void onCancel();
        void onSuccess();
        void onComplete();
    }
}
