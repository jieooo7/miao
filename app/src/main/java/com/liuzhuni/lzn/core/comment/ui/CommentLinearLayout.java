package com.liuzhuni.lzn.core.comment.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.core.comment.Replyable;
import com.liuzhuni.lzn.core.comment.model.ReplyModel;
import com.liuzhuni.lzn.utils.log.CommonLog;
import com.liuzhuni.lzn.utils.log.LogFactory;

import java.util.List;

/**
 * Created by Andrew Lee on 2015/7/20.
 * E-mail:jieooo7@163.com
 * Date: 2015-07-20
 * Time: 16:56
 */
public class CommentLinearLayout extends LinearLayout {

    private CommonLog log = LogFactory.createLog("linear");
    private Context context;

    private View mView;


    public CommentLinearLayout(Context context) {
        super(context);
        this.context = context;
    }

    public CommentLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    public void bindLinearLayout(BaseAdapter adapter, final List<ReplyModel> list,final Replyable replyable) {

        this.removeAllViews();
        if (adapter == null) {
            return;
        }

        final int count = adapter.getCount();
        if (count == 0) {
            return;
        }
        if (adapter.getView(0, null, null) == null) {
            return;
        }



        if (count < 5) {

            for (int i = 0; i < count; i++) {
                View v = adapter.getView(i, null, null);
                addView(v);

                    final int index=i;
                    getChildAt(index).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                    replyable.replyFloor(list.get(index).getId(),list.get(index).getUserNick());
                        }
                    });

            }
        } else {
            for (int i = 0; i < count; i++) {
                View v = adapter.getView(i, null, null);
                addView(v);
                if (i > 2 && i < count - 1) {
                    getChildAt(i).setVisibility(View.GONE);
                }

            }
            mView = LayoutInflater.from(context).inflate(
                    R.layout.comment_text, null);
            int num = count - 4;
            ((TextView) mView.findViewById(R.id.text)).setText("展开隐藏的" + num + "层");
            addView(mView, 3);



            for (int i = 0; i < count + 1; i++){
                final int index=i;
                getChildAt(index).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(index!=3){

                            if(index<3){
                                replyable.replyFloor(list.get(index).getId(),list.get(index).getUserNick());
                            }else{
                                replyable.replyFloor(list.get(index-1).getId(),list.get(index-1).getUserNick());
                            }
                        }
                    }
                });
            }

            mView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < count + 1; i++) {
                        if (i == 3) {
                            getChildAt(i).setVisibility(View.GONE);
                        } else {
                            getChildAt(i).setVisibility(View.VISIBLE);
                        }

                    }
                }
            });

        }
    }

}
