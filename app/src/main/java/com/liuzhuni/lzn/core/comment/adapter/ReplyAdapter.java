package com.liuzhuni.lzn.core.comment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.core.comment.model.ReplyModel;

import java.util.List;

/**
 * Created by Andrew Lee on 2015/7/25.
 * E-mail:jieooo7@163.com
 * Date: 2015-07-25
 * Time: 09:30
 */
public class ReplyAdapter extends BaseAdapter {


    private List<ReplyModel> mList;
    private Context mContext;


    public ReplyAdapter(List<ReplyModel> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
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

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.comment_linear_item, null);
            viewHolder = new ViewHolder();

            viewHolder.NumTv=(TextView)convertView.findViewById(R.id.num);
            viewHolder.ReplyTv=(TextView)convertView.findViewById(R.id.comment_reply);

            convertView.setTag(viewHolder);
        }else{

            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.NumTv.setText(position+1+"");
        SpannableString name=new SpannableString(mList.get(position).getUserNick() + " : ");
        name.setSpan(new ForegroundColorSpan(Color.argb(0xFF, 0x80, 0x80, 0x80)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        viewHolder.ReplyTv.setText(name);
        viewHolder.ReplyTv.append(mList.get(position).getText());

        return convertView;

    }

    static class ViewHolder {

        TextView NumTv;
        TextView ReplyTv;
    }
}
