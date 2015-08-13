package com.liuzhuni.lzn.core.index_new.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.core.index_new.model.NewsModel;
import com.liuzhuni.lzn.utils.fileHelper.CommonUtil;

import java.util.List;

/**
 * Created by Andrew Lee on 2015/7/14.
 * E-mail:jieooo7@163.com
 * Date: 2015-07-14
 * Time: 10:50
 */
public class AllNewsAdapter extends BaseAdapter {

    private List<NewsModel> mList;
    private Activity mContext;
    private ImageLoader mImageLoader;


    public AllNewsAdapter(List<NewsModel> mList, Activity mContext,ImageLoader imageLoader) {
        this.mList = mList;
        this.mContext = mContext;
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
                    R.layout.item_news, null);
            viewHolder = new ViewHolder();

            viewHolder.title=(TextView)convertView.findViewById(R.id.news_item_title);
            viewHolder.mall=(TextView)convertView.findViewById(R.id.news_item_mall);
            viewHolder.time=(TextView)convertView.findViewById(R.id.news_item_time);
            viewHolder.price=(TextView)convertView.findViewById(R.id.news_item_price);
            viewHolder.imageIv=(NetworkImageView)convertView.findViewById(R.id.news_item_img);

            convertView.setTag(viewHolder);
        }else{

            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(mList.get(position).getTitle());
        viewHolder.mall.setText(mList.get(position).getMall());
        viewHolder.time.setText(mList.get(position).getTime());
        viewHolder.price.setText(mList.get(position).getTitle1());

        RelativeLayout.LayoutParams rp= new RelativeLayout.LayoutParams(CommonUtil.getScreenWidth(mContext)/2-5,CommonUtil.getScreenWidth(mContext)/2-5);

        viewHolder.imageIv.setLayoutParams(rp);

        viewHolder.imageIv.setDefaultImageResId(R.drawable.publish_preload_ic);
        viewHolder.imageIv.setImageUrl(mList.get(position).getPic(),mImageLoader);
        viewHolder.imageIv.setErrorImageResId(R.drawable.publish_preload_ic);

        return convertView;

    }

    static class ViewHolder {
        NetworkImageView imageIv;
        TextView title;
        TextView mall;
        TextView time;
        TextView price;
    }

}
