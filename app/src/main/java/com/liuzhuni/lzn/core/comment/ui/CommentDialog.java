package com.liuzhuni.lzn.core.comment.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuzhuni.lzn.R;

/**
 * Created by Andrew Lee on 2015/7/25.
 * E-mail:jieooo7@163.com
 * Date: 2015-07-25
 * Time: 16:54
 */
public class CommentDialog {

    public TextView mTitle;
    public ImageView mCloseBtn;
    public EditText mEdit;
    public TextView mSubmit;


    private Dialog mDialog;

    public CommentDialog(Activity context) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_dialog, null);
        mDialog = new Dialog(context, R.style.MyDialog);

        mDialog.setContentView(view);

        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) dm.widthPixels;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
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

        mTitle = (TextView) view.findViewById(R.id.title);
        mCloseBtn = (ImageView) view.findViewById(R.id.close_btn);
        mEdit = (EditText) view.findViewById(R.id.et_dialog);
        mSubmit = (TextView) view.findViewById(R.id.comment_dia_sumit);

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


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
