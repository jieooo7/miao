package com.liuzhuni.lzn.core.index_new.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.core.index_new.model.PickModel;

import java.util.List;

/**
 * Created by Andrew Lee on 2015/7/13.
 * E-mail:jieooo7@163.com
 * Date: 2015-07-13
 * Time: 09:49
 */
public class PickAdapter extends BaseAdapter {


    private List<PickModel> mList;
    private ImageLoader mImageLoader;

    private Context mContext;


    public PickAdapter(Context context,List<PickModel> mList, ImageLoader mImageLoader) {
        this.mList = mList;
        this.mImageLoader = mImageLoader;
        this.mContext=context;
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


        ViewHolderImage normalHolder = null;
        if(convertView==null){

                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.index_good_item, null);
                    normalHolder=new ViewHolderImage();
                    normalHolder.headIv = (NetworkImageView) convertView.findViewById(R.id.index_item_img);
                    normalHolder.timeTv = (TextView) convertView.findViewById(R.id.index_item_time);
                    normalHolder.mallTv = (TextView) convertView.findViewById(R.id.index_item_mall);
                    normalHolder.titleTv = (TextView) convertView.findViewById(R.id.index_item_title);
                    normalHolder.priceTv = (TextView) convertView.findViewById(R.id.index_item_price);
                    normalHolder.readTv = (TextView) convertView.findViewById(R.id.index_item_read);
                    normalHolder.reviewTv = (TextView) convertView.findViewById(R.id.index_item_review);
                    normalHolder.lowPriceTv = (TextView) convertView.findViewById(R.id.index_item_shenjia);
                    normalHolder.cheapTv = (TextView) convertView.findViewById(R.id.index_item_cheap);
                    normalHolder.globalTv = (TextView) convertView.findViewById(R.id.index_item_global);
                    normalHolder.slowTv = (TextView) convertView.findViewById(R.id.index_item_slow);
                    normalHolder.expiredTv = (ImageView) convertView.findViewById(R.id.index_item_expired);
                    convertView.setTag(normalHolder);





        }else{


                    normalHolder=(ViewHolderImage)convertView.getTag();

        }


                normalHolder.timeTv.setText(mList.get(position).getTime());
                normalHolder.mallTv.setText(mList.get(position).getMall());
                normalHolder.titleTv.setText(mList.get(position).getTitle());
                normalHolder.priceTv.setText(mList.get(position).getTitle1());

                normalHolder.readTv.setText(mList.get(position).getRead() > 0 ? "" + mList.get(position).getRead() : "");

                normalHolder.reviewTv.setText(mList.get(position).getReview() > 0 ? "" + mList.get(position).getReview() : "");
//
                normalHolder.mallTv.setText(mList.get(position).getMall());


//         headIv.setDefaultImageResId(R.drawable.my_touxiang);
                normalHolder.headIv.setImageUrl(mList.get(position).getPic(), mImageLoader);
//         headIv.setErrorImageResId(R.drawable.my_touxiang);

                normalHolder.slowTv.setVisibility(mList.get(position).isShoumanwu() ? View.VISIBLE : View.GONE);
                normalHolder.lowPriceTv.setVisibility(mList.get(position).isShenjia() ? View.VISIBLE : View.GONE);
                normalHolder.cheapTv.setVisibility(mList.get(position).isBaicai() ? View.VISIBLE : View.GONE);
                normalHolder.globalTv.setVisibility(mList.get(position).isQuanqiugou() ? View.VISIBLE : View.GONE);
                normalHolder. expiredTv.setVisibility(mList.get(position).isExpired() ? View.VISIBLE : View.GONE);




        return convertView;


    }



    static class ViewHolderImage {
        NetworkImageView headIv;
        TextView timeTv;
        TextView mallTv ;
        TextView titleTv;
        TextView priceTv ;
        TextView readTv ;
        TextView reviewTv ;
        TextView lowPriceTv ;
        TextView cheapTv ;
        TextView globalTv ;
        TextView slowTv ;
        ImageView expiredTv;
    }


}
