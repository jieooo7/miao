package com.liuzhuni.lzn.core.personInfo.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.liuzhuni.lzn.R;

/**
 * Created by Andrew Lee on 2015/5/5.
 * E-mail:jieooo7@163.com
 * Date: 2015-05-05
 * Time: 14:43
 */
public class PicDialog {

    private TextView mTitle;
    public TextView mItemOne;
    public TextView mItemTwo;


    private Dialog mDialog;

    public PicDialog(Activity context, String title, String item1, String item2) {
        View view = LayoutInflater.from(context).inflate(R.layout.pic_dialog, null);
        mDialog = new Dialog(context, R.style.MyDialog);

        mDialog.setContentView(view);

        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (dm.widthPixels*0.85);
        lp.height = (int) (0.7 * lp.width);
        dialogWindow.setAttributes(lp);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        mTitle = (TextView) view.findViewById(R.id.pic_text_title);
        mItemOne = (TextView) view.findViewById(R.id.pic_text_one);
        mItemTwo = (TextView) view.findViewById(R.id.pic_text_two);

        if (title != null) {

            mTitle.setText(title);
        }
        if (item1 != null) {

            mItemOne.setText(item1);
        }
        if (item2 != null) {

            mItemTwo.setText(item2);
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
