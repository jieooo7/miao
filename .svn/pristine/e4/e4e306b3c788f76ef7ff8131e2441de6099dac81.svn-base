package com.liuzhuni.lzn.core.buylist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.core.buylist.model.BuyListModel;

import java.util.List;

/**
 * Created by Andrew Lee on 2015/4/23.
 * E-mail:jieooo7@163.com
 * Date: 2015-04-23
 * Time: 11:37
 */
public class FinishAdapter extends BaseAdapter {
    private List<BuyListModel> mList;
    private Context mContext;

    public FinishAdapter(Context context, List<BuyListModel> list) {
        this.mList = list;
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

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.fragment_finish_item, null);
            viewHolder = new ViewHolder();

            viewHolder.textTv=(TextView)convertView.findViewById(R.id.finish_item_tv);

            convertView.setTag(viewHolder);
        }else{

            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textTv.setText(mList.get(position).getValue());

        return convertView;

    }

    static class ViewHolder {
        TextView textTv;
    }

}
