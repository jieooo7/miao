package com.liuzhuni.lzn.core.personInfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.Base2Activity;
import com.liuzhuni.lzn.config.MessageWhat;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.goods.ToBuyActivity;
import com.liuzhuni.lzn.core.index_new.utils.MessageSpan;
import com.liuzhuni.lzn.core.model.BaseListModel;
import com.liuzhuni.lzn.core.personInfo.adapter.MessageAdapter;
import com.liuzhuni.lzn.core.personInfo.model.MessageModel;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.volley.GsonBaseRequest;

import java.util.ArrayList;
import java.util.List;

public class MessageCenterActivity extends Base2Activity {

    @ViewInject(R.id.title_left)
    private TextView mBackTv;
    @ViewInject(R.id.title_right)
    private TextView mRightTv;
    @ViewInject(R.id.title_middle)
    private TextView mMiddleTv;
    @ViewInject(R.id.message_list_view)
    private ListView mlistView;

    private List<MessageModel> mList;
    private List<MessageModel> mCurrentList;
    private MessageAdapter mAdapter;
    private int mTotal=1;
    private int mIndex=0;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == MessageWhat.LINK) {
                MessageSpan ms = (MessageSpan) msg.obj;
                Object[] spans = (Object[])ms.getObj();

                for (Object span : spans) {
                    if (span instanceof URLSpan) {

                        String url=((URLSpan) span).getURL();
                        if(url.startsWith("http")||url.startsWith("https")){

                            Intent intent = new Intent(MessageCenterActivity.this, ToBuyActivity.class);
                            Bundle bundle = new Bundle();

                            bundle.putString("url",url);
                            bundle.putString("title", "");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else if(url.startsWith("huim")){
                            Uri uri=Uri.parse(url);

                            Intent contentIntent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(contentIntent);
                        }

                    }
                }
            }
        };
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        initData();
        findViewById();
        initUI();
        setListener();
    }


    @Override
    protected void initData() {

        mList=new ArrayList<MessageModel>();

        PreferencesUtils.putIntToSPMap(MessageCenterActivity.this, PreferencesUtils.Keys.UN_READ, 0, PreferencesUtils.Keys.USERINFO);

    }

    @Override
    protected void findViewById() {

        ViewUtils.inject(this);

    }

    @Override
    protected void initUI() {

        mBackTv.setText(getResources().getText(R.string.me));
        mMiddleTv.setText(getResources().getString(R.string.message_center));
        mRightTv.setVisibility(View.GONE);

        mAdapter=new MessageAdapter(this,mList,handler);

        mlistView.setAdapter(mAdapter);

        if(mIndex<mTotal){
            pullData(mIndex);
        }

    }

    @Override
    protected void setListener() {

    }

    protected synchronized void pullData(final int id){
        executeRequest(new GsonBaseRequest<BaseListModel<MessageModel>>(Request.Method.GET, UrlConfig.MESSAGE_CENTER+id,new TypeToken<BaseListModel<MessageModel>>(){}.getType(),responseListener(),errorListener()){

        });
    }

    private Response.Listener<BaseListModel<MessageModel>> responseListener() {
        return new Response.Listener<BaseListModel<MessageModel>>(){
            @Override
            public void onResponse(BaseListModel<MessageModel> indexMessageModel) {

                if(indexMessageModel.getRet()==0){
                    mTotal=indexMessageModel.getTotalpage();
                    mIndex++;

                    if(indexMessageModel.getData()!=null){

                        mCurrentList=indexMessageModel.getData();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mList.addAll(0,mCurrentList);
                                mAdapter.notifyDataSetChanged();
                            }
                        });

                    }
                }
                //mlist  为空时 显示错误页面

            }
        };

    }

    @OnClick(R.id.title_left)
    public void back(View v){
        finish();
    }


}
