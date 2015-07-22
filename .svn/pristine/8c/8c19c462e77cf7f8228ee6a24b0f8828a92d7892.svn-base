package com.liuzhuni.lzn.core.index.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.core.index.model.IndexModel;

import java.util.List;

/**
 * Created by Andrew Lee on 2015/4/22.
 * E-mail:jieooo7@163.com
 * Date: 2015-04-22
 * Time: 10:18
 */
public class IndexAdapter extends BaseAdapter {
    private List<IndexModel> mList;
    private Context mContext;
    private ImageLoader mImageLoader;

    public IndexAdapter(Context context, List<IndexModel> list,ImageLoader imageLoader) {
        this.mList = list;
        this.mContext = context;
        this.mImageLoader=imageLoader;
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
                    R.layout.index_item, null);
            viewHolder = new ViewHolder();

            viewHolder.headIv=(NetworkImageView)convertView.findViewById(R.id.index_head_iv);
            viewHolder.textTv=(TextView)convertView.findViewById(R.id.index_body_tv);
            viewHolder.timeTv=(TextView)convertView.findViewById(R.id.index_time_tv);

            convertView.setTag(viewHolder);
        }else{

            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.timeTv.setText(mList.get(position).getTime());
        viewHolder.textTv.setText(mList.get(position).getName());
        viewHolder.textTv.append(mContext.getString(R.string.index_want_buy));
        if(mList.get(position).getKey()!=null){
            viewHolder.textTv.append(mList.get(position).getKey());
        }
        viewHolder.headIv.setDefaultImageResId(R.drawable.my_touxiang);
        viewHolder.headIv.setImageUrl(mList.get(position).getPic(),mImageLoader);
        viewHolder.headIv.setErrorImageResId(R.drawable.my_touxiang);
        return convertView;

    }

    static class ViewHolder {
        NetworkImageView headIv;
        TextView textTv;
        TextView timeTv;

    }

}