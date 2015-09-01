package com.liuzhuni.lzn.core.index_new.adapter;

import android.app.Activity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.core.index_new.model.CheapModel;
import com.liuzhuni.lzn.utils.fileHelper.CommonUtil;

import java.util.List;

/**
 * Created by Andrew Lee on 2015/8/7.
 * E-mail:jieooo7@163.com
 * Date: 2015-08-07
 * Time: 14:57
 */
public class CheapAdapter extends BaseAdapter {


    private List<CheapModel> mList;
    private Activity mContext;
    private ImageLoader mImageLoader;


    public CheapAdapter(List<CheapModel> mList, Activity mContext,ImageLoader imageLoader) {
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
                    R.layout.cheap_item, null);
            viewHolder = new ViewHolder();

            viewHolder.title=(TextView)convertView.findViewById(R.id.news_item_title);
            viewHolder.type=(TextView)convertView.findViewById(R.id.type);
            viewHolder.oldPrice=(TextView)convertView.findViewById(R.id.old_price);
            viewHolder.price=(TextView)convertView.findViewById(R.id.price);
            viewHolder.imageIv=(NetworkImageView)convertView.findViewById(R.id.news_item_img);

            convertView.setTag(viewHolder);
        }else{

            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(mList.get(position).getTitle());
        String oldPrice="¥"+mList.get(position).getOldprice();
        SpannableString msp=new SpannableString(oldPrice);
        if(oldPrice.length()>0){

            msp.setSpan(new StrikethroughSpan(), 0, oldPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.oldPrice.setText(msp);
        }
        viewHolder.price.setText("¥" + mList.get(position).getPrice());
        switch (mList.get(position).getTypes()){

            case 1:
                viewHolder.type.setText("商品直降");

                viewHolder.type.setBackgroundColor(mContext.getResources().getColor(R.color.low));
                break;
            case 2:
                viewHolder.type.setText("拍下改价");

                viewHolder.type.setBackgroundColor(mContext.getResources().getColor(R.color.change_price));

                break;
            case 3:
                viewHolder.type.setText("领券下单");

                viewHolder.type.setBackgroundColor(mContext.getResources().getColor(R.color.get_coupon));

                break;
            case 4:
                viewHolder.type.setText("好评返现");

                viewHolder.type.setBackgroundColor(mContext.getResources().getColor(R.color.comment_best));

                break;



        }

        LinearLayout.LayoutParams rp= new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(mContext)/2-5,CommonUtil.getScreenWidth(mContext)/2-5);

        viewHolder.imageIv.setLayoutParams(rp);

        viewHolder.imageIv.setDefaultImageResId(R.drawable.publish_preload_ic);
        viewHolder.imageIv.setImageUrl(mList.get(position).getImg(), mImageLoader);
        viewHolder.imageIv.setErrorImageResId(R.drawable.publish_preload_ic);

        return convertView;

    }

    static class ViewHolder {
        NetworkImageView imageIv;
        TextView title;
        TextView oldPrice;
        TextView type;
        TextView price;
    }
}
