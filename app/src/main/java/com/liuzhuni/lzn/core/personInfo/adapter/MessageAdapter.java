package com.liuzhuni.lzn.core.personInfo.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.core.goods.ToBuyActivity;
import com.liuzhuni.lzn.core.personInfo.model.MessageModel;

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
    private Handler handler;

    public MessageAdapter(Context mContext,List<MessageModel> mList, Handler _handler) {
        this.mList = mList;
        this.mContext = mContext;
        this.handler = _handler;
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
        if(convertView==null){

                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.message_center_top, null);


            viewHolder = new ViewHolder();
            viewHolder.messageTv=(TextView) convertView.findViewById(R.id.content);
            viewHolder.timeTv=(TextView) convertView.findViewById(R.id.time);
            viewHolder.seeTv=(TextView) convertView.findViewById(R.id.to_see);
            viewHolder.ll=(LinearLayout) convertView.findViewById(R.id.ll);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.messageTv.setText(mList.get(position).getMessage());
        viewHolder.timeTv.setText(mList.get(position).getDate());
        final String url=mList.get(position).getUrl();
        viewHolder.seeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(url.startsWith("http")||url.startsWith("https")){

                    Intent intent = new Intent(mContext, ToBuyActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putString("url",url);
                    bundle.putString("title", "");
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }else if(url.startsWith("huim")){
                    Uri uri=Uri.parse(url);

                    Intent contentIntent = new Intent(Intent.ACTION_VIEW, uri);
                    mContext.startActivity(contentIntent);
                }
            }
        });


        if(mList.get(position).isRead()){

            viewHolder.ll.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        }else{

            viewHolder.ll.setBackgroundColor(mContext.getResources().getColor(R.color.messgae_back));

        }



        return convertView;


    }



    static class ViewHolder {
        TextView timeTv;
        TextView messageTv;
        TextView seeTv;
        LinearLayout ll;

    }


}
