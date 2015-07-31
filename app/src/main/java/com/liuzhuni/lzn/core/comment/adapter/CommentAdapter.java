package com.liuzhuni.lzn.core.comment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.core.comment.model.CommentModel;
import com.liuzhuni.lzn.core.comment.ui.CommentLinearLayout;

import java.util.List;

/**
 * Created by Andrew Lee on 2015/7/21.
 * E-mail:jieooo7@163.com
 * Date: 2015-07-21
 * Time: 09:46
 */
public class CommentAdapter extends BaseAdapter{


    private List<CommentModel> mList;
    private Context mContext;
    private ImageLoader mImageLoader;


    public interface ReplyListener{
        public void reply(int pos);
    }

    private ReplyListener mReplyListener;

    public CommentAdapter(Context context, List<CommentModel> list,ImageLoader imageLoader) {
        this.mList = list;
        this.mContext = context;
        this.mImageLoader=imageLoader;
    }


    public void setReplyListener(ReplyListener replyListener){
        this.mReplyListener=replyListener;

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
                    R.layout.comment_detail, null);
            viewHolder = new ViewHolder();

            viewHolder.floorTv=(TextView)convertView.findViewById(R.id.floor);
            viewHolder.nameTv=(TextView)convertView.findViewById(R.id.user_name);
            viewHolder.timeTv=(TextView)convertView.findViewById(R.id.the_time);
            viewHolder.contentTv=(TextView)convertView.findViewById(R.id.content);
            viewHolder.linear=(CommentLinearLayout)convertView.findViewById(R.id.linear);
            viewHolder.headIv=(NetworkImageView)convertView.findViewById(R.id.user_head);

            convertView.setTag(viewHolder);
        }else{

            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.floorTv.setText(mList.get(position).getOrderNum()+"楼");
        viewHolder.nameTv.setText(mList.get(position).getUserNick());
        viewHolder.timeTv.setText(mList.get(position).getCreateTime());
        viewHolder.contentTv.setText(mList.get(position).getText());
        if(mList.get(position).getComment()!=null){
            viewHolder.linear.bindLinearLayout(new ReplyAdapter(mList.get(position).getComment(),mContext));
        }else{
            mList.get(position).setComment(null);
        }
        viewHolder.headIv.setImageUrl(mList.get(position).getUserPic(), mImageLoader);


        viewHolder.contentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReplyListener.reply(position);
            }
        });

        return convertView;

    }

    static class ViewHolder {

        NetworkImageView headIv;
        TextView floorTv;
        TextView nameTv;
        TextView timeTv;
        TextView contentTv;
        CommentLinearLayout linear;
    }
}
