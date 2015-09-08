package com.liuzhuni.lzn.core.personInfo.adapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.core.goods.ToBuyActivity;
import com.liuzhuni.lzn.core.personInfo.model.CouponModel;
import com.liuzhuni.lzn.core.personInfo.ui.RepeatImage;
import com.liuzhuni.lzn.utils.ToastUtil;

import java.util.List;

/**
 * Created by Andrew Lee on 2015/9/6.
 * E-mail:jieooo7@163.com
 * Date: 2015-09-06
 * Time: 16:53
 */
public class CouponAdapter extends BaseAdapter {

    private List<CouponModel> mList;

    private Context mContext;


    public CouponAdapter(Context context, List<CouponModel> mList) {

        this.mList = mList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewholder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.coupon_item, null);
            viewholder = new ViewHolder();

            viewholder.topRi = (RepeatImage) convertView.findViewById(R.id.top_ri);
            viewholder.goTv = (ImageView) convertView.findViewById(R.id.go);

            viewholder.titleTv = (TextView) convertView.findViewById(R.id.coupon_title);
            viewholder.couponNum = (TextView) convertView.findViewById(R.id.coupon_num);
            viewholder.couponPwd = (TextView) convertView.findViewById(R.id.coupon_passwd);
            viewholder.timeTv = (TextView) convertView.findViewById(R.id.coupon_time);


            convertView.setTag(viewholder);

        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        CouponModel model = mList.get(position);
        viewholder.titleTv.setText(model.getTitle());
        viewholder.couponNum.setText(model.getCode());
        viewholder.couponPwd.setText(model.getPwd());

        if (model.isExpired()) {//过期

            viewholder.topRi.setTheDrawable(R.drawable.ic_home_s);

            viewholder.timeTv.setText("有效期至 : " + model.getEnddate() + " (已过期)");
            viewholder.titleTv.setTextColor(mContext.getResources().getColor(R.color.regist_text));
            viewholder.couponNum.setTextColor(mContext.getResources().getColor(R.color.regist_text));
            viewholder.couponPwd.setTextColor(mContext.getResources().getColor(R.color.regist_text));

        } else {
            viewholder.topRi.setTheDrawable(R.drawable.ic_home_s);
            viewholder.titleTv.setTextColor(mContext.getResources().getColor(R.color.me_info_text));
            viewholder.couponNum.setTextColor(mContext.getResources().getColor(R.color.index_text));
            viewholder.couponPwd.setTextColor(mContext.getResources().getColor(R.color.index_text));


            viewholder.timeTv.setText("有效期至 : " + model.getEnddate());
        }

        final String url = model.getUrl();

        viewholder.goTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, ToBuyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                bundle.putString("title", "");
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        final String coupon_num = model.getCode();

        viewholder.couponNum.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyToClipboard(coupon_num);
                ToastUtil.customShow(mContext, mContext.getText(R.string.copy_tips));


                return true;
            }
        });

        final String coupon_pwd = model.getPwd();

        viewholder.couponPwd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyToClipboard(coupon_pwd);
                ToastUtil.customShow(mContext, mContext.getText(R.string.copy_tips));
                return true;
            }
        });


        return convertView;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")
    private void copyToClipboard(String str) {//复制剪切板
        int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(str);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("text label", str);
            clipboard.setPrimaryClip(clip);
        }
    }

    static class ViewHolder {

        RepeatImage topRi;
        TextView titleTv;
        TextView couponNum;
        TextView couponPwd;
        TextView timeTv;
        ImageView goTv;


    }


}
