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
import com.liuzhuni.lzn.core.select.model.PriceModel;

import java.util.List;

/**
 * Created by Andrew Lee on 2015/8/7.
 * E-mail:jieooo7@163.com
 * Date: 2015-08-07
 * Time: 17:01
 */
public class PriceAdapter extends BaseAdapter {


    private List<PriceModel> mList;
    private Context mContext;

    public PriceAdapter(Context mContext, List<PriceModel> mList) {
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
        viewHolder.textTv.setText(mList.get(position).getPriceshow());
        if(mList.get(position).isSelect()){

            viewHolder.imageIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.woxiangmai_price_checkbox_true));
            viewHolder.textTv.setTextColor(mContext.getResources().getColor(R.color.red));
        }else {

            viewHolder.imageIv.setImageDrawable(null);
            viewHolder.textTv.setTextColor(mContext.getResources().getColor(R.color.me_info_text));
        }

        viewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(SelectActivity.sPriceFlag){
//                    SelectActivity.sPriceFlag=false;
                    if(mList.get(position).isSelect()){
                        mList.get(position).setSelect(false);
                    }else{
                        mList.get(position).setSelect(true);
                    }
                    for(int i=0;i<getCount();i++){
                        if(i!=position){
                            mList.get(i).setSelect(false);
                        }

                    }
                    notifyDataSetChanged();
//                }
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
