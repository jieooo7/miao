package com.liuzhuni.lzn.core.personInfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.core.personInfo.model.MessageModel;
import com.liuzhuni.lzn.utils.DensityUtil;

import java.util.List;

/**
 * Created by Andrew Lee on 2015/4/18.
 * E-mail:jieooo7@163.com
 * Date: 2015-04-18
 * Time: 14:52
 */
public class MessageAdapter extends BaseAdapter {

    private List<MessageModel> mList;
    private Context mContext;
    private final int FIRST=0;
    private final int OTHER=1;

    public MessageAdapter(Context mContext,List<MessageModel> mList) {
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0) {
            return this.FIRST;

        } else {
            return this.OTHER;
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder = null;
        if(convertView==null){
            switch (getItemViewType(position)){

                case FIRST:

                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.message_center_top, null);

                    break;
                case OTHER:

                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.message_center_middle, null);

                    break;
                default:
                    break;

            }
            viewHolder = new ViewHolder();
            viewHolder.messageTv=(TextView) convertView.findViewById(R.id.message_center_tv);
            viewHolder.yearTv=(TextView) convertView.findViewById(R.id.message_year);
            viewHolder.monthTv=(TextView) convertView.findViewById(R.id.message_month);
            viewHolder.line=(TextView) convertView.findViewById(R.id.message_line);
//            if(getCount()>0){
//                int height=viewHolder.messageTv.getHeight()+30;
//                RelativeLayout.LayoutParams rp= new RelativeLayout.LayoutParams(DensityUtil.dip2px(mContext,2),height);
//                viewHolder.line.setLayoutParams(rp);
//            }
            convertView.setTag(viewHolder);



        }else{

            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.messageTv.setText(mList.get(position).getMessage());
        viewHolder.monthTv.setText(mList.get(position).getMonth());
        viewHolder.yearTv.setText(mList.get(position).getYear());
        if(!mList.get(position).isRead()){
            viewHolder.messageTv.setBackgroundResource(R.drawable.my_xiaoxi_bg_yidu);
        }
//        int height=viewHolder.messageTv.getHeight()+30;
        LinearLayout.LayoutParams rp= new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 2),viewHolder.messageTv.getMeasuredHeight()+DensityUtil.dip2px(mContext, 95));
//        RelativeLayout.LayoutParams rp= new RelativeLayout.LayoutParams(DensityUtil.dip2px(mContext, 2),height);
        if(viewHolder.line!=null){
            viewHolder.line.setLayoutParams(rp);
//            viewHolder.line.setHeight(viewHolder.messageTv.getHeight()+300);
        }
        //setLayoutParams(rp);
        return convertView;


    }



    static class ViewHolder {
        TextView monthTv;
        TextView yearTv;
        TextView messageTv;
        TextView line;

    }


}