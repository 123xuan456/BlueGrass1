package com.reeching.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reeching.bluegrass.R;

/**
 * Created by Administrator on 2017/6/1.
 */
public class LoadingDiaLog extends Dialog{
    private TextView tv;

    public LoadingDiaLog(Context context) {
        super(context, R.style.loadingDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_bar);
        LinearLayout linearLayout = (LinearLayout) this
                .findViewById(R.id.LinearLayout);
        linearLayout.getBackground().setAlpha(150);
        tv = (TextView) this.findViewById(R.id.tv);
        tv.setText("正在登录...");
    }
}
