package com.liuzhuni.lzn.core.index_new.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.core.index_new.model.FilterModel;

import java.util.List;

/**
 * Created by Andrew Lee on 2015/7/14.
 * E-mail:jieooo7@163.com
 * Date: 2015-07-14
 * Time: 16:03
 */
public class FilterAdapter  extends BaseAdapter {

    private List<FilterModel> mList;
    private Context mContext;

    public FilterAdapter(List<FilterModel> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
//        ToastUtil.show(mContext, "什么回事");
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
                    R.layout.filter_item, null);
            viewHolder = new ViewHolder();

            viewHolder.title=(TextView)convertView.findViewById(R.id.filter_item_tv);

            convertView.setTag(viewHolder);
        }else{

            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(mList.get(position).getTheName());

        viewHolder.title.setTextColor(mList.get(position).isSelected()?mContext.getResources().getColor(R.color.key):mContext.getResources().getColor(R.color.filter_text));
        viewHolder.title.setBackgroundDrawable(mList.get(position).isSelected()?mContext.getResources().getDrawable(R.drawable.filter_selected):mContext.getResources().getDrawable(R.drawable.filter_unselect));

        return convertView;

    }

    static class ViewHolder {
        TextView title;
    }


}
