package com.liuzhuni.lzn.core.personInfo.ui;

import android.app.Activity;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.liuzhuni.lzn.R;

/**
 * Created by Andrew Lee on 2015/5/4.
 * E-mail:jieooo7@163.com
 * Date: 2015-05-04
 * Time: 16:05
 */
public class LoginoutDialog {

    public TextView mSubmit;
    public TextView mCancel;

    private TextView mContent;

    private Dialog mDialog;

    public LoginoutDialog(Activity context, String string) {

        View view = LayoutInflater.from(context).inflate(R.layout.loginout_dialog, null);
        mDialog = new Dialog(context, R.style.MyDialog);

        mDialog.setContentView(view);

        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.width = dm.widthPixels - DensityUtil.dip2px(context, 100);
        lp.width =(int) (dm.widthPixels*0.85);
        lp.height = (int) (0.65 * lp.width);
        dialogWindow.setAttributes(lp);
        mDialog.setCanceledOnTouchOutside(true);
//        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode,
//                                 KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    dismiss();
//                    return true;
//                }
//                return false;
//            }
//        });

        mContent = (TextView) view.findViewById(R.id.text_content);
        mSubmit = (TextView) view.findViewById(R.id.loginout_submit);
        mCancel = (TextView) view.findViewById(R.id.loginout_cancel);

        if (string != null) {

            mContent.setText(string);
        }


    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {

        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
