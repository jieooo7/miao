package com.liuzhuni.lzn.core.goods.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.core.goods.model.ShopModel;

import java.util.List;

/**
 * Created by Andrew Lee on 2015/4/24.
 * E-mail:jieooo7@163.com
 * Date: 2015-04-24
 * Time: 16:09
 */
public class GoodsAdapter extends BaseAdapter {
    private List<ShopModel> mList;
    private Context mContext;
    private ImageLoader mImageLoader;

    public GoodsAdapter(Context context, List<ShopModel> list,ImageLoader imageLoader) {
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
                    R.layout.goods_item, null);
            viewHolder = new ViewHolder();

            viewHolder.shopNameTv=(TextView)convertView.findViewById(R.id.shop_name_item);
            viewHolder.priceTv=(TextView)convertView.findViewById(R.id.price_item);
            viewHolder.shopIv=(NetworkImageView)convertView.findViewById(R.id.shop_logo);

            convertView.setTag(viewHolder);
        }else{

            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.shopNameTv.setText(mList.get(position).getMall());
        viewHolder.priceTv.setText(mList.get(position).getPrice());
        viewHolder.shopIv.setImageUrl(mList.get(position).getImg(),mImageLoader);

        return convertView;

    }

    static class ViewHolder {

        NetworkImageView shopIv;
        TextView shopNameTv;
        TextView priceTv;
    }
}
