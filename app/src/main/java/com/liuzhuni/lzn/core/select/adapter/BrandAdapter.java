package com.liuzhuni.lzn.core.select.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.core.select.model.BrandModel;

import java.util.List;

/**
 * Created by Andrew Lee on 2015/4/28.
 * E-mail:jieooo7@163.com
 * Date: 2015-04-28
 * Time: 09:35
 */
public class BrandAdapter extends BaseAdapter{

    private List<BrandModel> mList;
    private Context mContext;

    public BrandAdapter(Context mContext, List<BrandModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.brand_item, null);
            viewHolder = new ViewHolder();

            viewHolder.textTv=(TextView)convertView.findViewById(R.id.brand_item_tv);
            viewHolder.imageIv=(ImageView)convertView.findViewById(R.id.brand_item_iv);
            viewHolder.rl=(RelativeLayout)convertView.findViewById(R.id.brand_rl);

            convertView.setTag(viewHolder);
        }else{

            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textTv.setText(mList.get(position).getBrand());
        if(mList.get(position).isSelect()){

            viewHolder.imageIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.woxiangmai_checkbox_true));
            viewHolder.textTv.setTextColor(mContext.getResources().getColor(R.color.red));
        }else {

            viewHolder.imageIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.woxiangmai_checkbox_false));
            viewHolder.textTv.setTextColor(mContext.getResources().getColor(R.color.me_info_text));
        }

        viewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mList.get(position).isSelect()){
                    mList.get(position).setSelect(false);
                }else{
                    mList.get(position).setSelect(true);
                }
                BrandAdapter.this.notifyDataSetChanged();
            }
        });

        return convertView;

    }


    static class ViewHolder {
        TextView textTv;
        ImageView imageIv;
        RelativeLayout rl;
    }


}
