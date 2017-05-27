package com.reeching.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.reeching.bluegrass.R;

/**
 * Created by Administrator on 2017/3/12.
 */

public class DialogUtils {

        public static Dialog showPromptDailog(Context context, LayoutInflater inflater, int layout){
            Dialog dialog  = new Dialog(context, R.style.NoDialogTitle);
            View view = inflater.inflate(layout,null);
            dialog.setContentView(view);
            return dialog;
        }
}
