package com.liuzhuni.lzn.core.siri.adapter;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.Base2Activity;
import com.liuzhuni.lzn.config.Check;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.goods.ui.LinearLayoutForListView;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.core.select.SelectActivity;
import com.liuzhuni.lzn.core.siri.TextSiriActivity;
import com.liuzhuni.lzn.core.siri.model.GoodsListModel;
import com.liuzhuni.lzn.core.siri.model.SiriModel;
import com.liuzhuni.lzn.db.DatabaseOperate;
import com.liuzhuni.lzn.db.DbModel;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonRequest;

import java.util.List;
import java.util.Map;

import static com.liuzhuni.lzn.core.siri.TextSiriActivity.REQUEST_CODE;

/**
 * Created by Andrew Lee on 2015/4/28.
 * E-mail:jieooo7@163.com
 * Date: 2015-04-28
 * Time: 14:46
 */
public class SiriAdapter extends BaseAdapter {

    private List<SiriModel> mList;
    private List<SiriModel> mBackList;
    private Base2Activity mActivity;
    private ImageLoader mImageLoader;

    private SQLiteDatabase db;

    private final int TYPE_COUNT = 6;
    public static final int TIME = 0;
    public static final int RIGHT_TEXT = 1;
    public static final int LEFT_TEXT = 2;
    public static final int RIGHT_GOODS = 3;
    public static final int RIGHT_DIALOG = 4;
    public static final int RIGHT_TEXT_CLICK = 5;


    public SiriAdapter(List<SiriModel> mList, List<SiriModel> mBackList, Base2Activity mActivity, ImageLoader mImageLoader,SQLiteDatabase db) {
        this.mList = mList;
        this.mActivity = mActivity;
        this.mImageLoader = mImageLoader;
        this.mBackList = mBackList;
        this.db=db;
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
        return this.TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {

        switch (mList.get(position).getType()) {
            case 0:
                return this.TIME;
            case 1:
                return this.RIGHT_TEXT;
            case 2:
                return this.LEFT_TEXT;
            case 3:
                return this.RIGHT_GOODS;
            case 4:
                return this.RIGHT_DIALOG;
            case 5:
                return this.RIGHT_TEXT_CLICK;

        }
        return 0;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ListView lv = (ListView) parent;
        ViewHolderTime viewTime = null;
        ViewHolderRight viewRight = null;
        ViewHolderLeft viewLeft = null;
        ViewHolderLeftClick viewLeftClick = null;
        ViewHolderDialog viewDialog = null;
        ViewHolderGoods viewGoods = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TIME:
                    convertView = LayoutInflater.from(mActivity).inflate(
                            R.layout.siri_time_item, null);

                    viewTime = new ViewHolderTime();
                    viewTime.timeTv = (TextView) convertView.findViewById(R.id.siri_time_tv);
                    convertView.setTag(viewTime);
                    break;
                case RIGHT_TEXT:
                    convertView = LayoutInflater.from(mActivity).inflate(
                            R.layout.siri_right_normal, null);

                    viewRight = new ViewHolderRight();
                    viewRight.leftTv = (TextView) convertView.findViewById(R.id.siri_right_normal_text);
                    viewRight.headIv = (NetworkImageView) convertView.findViewById(R.id.siri_head_iv);
                    convertView.setTag(viewRight);
                    break;
                case LEFT_TEXT:
                    convertView = LayoutInflater.from(mActivity).inflate(
                            R.layout.siri_left_normal, null);

                    viewLeft = new ViewHolderLeft();
                    viewLeft.rightTv = (TextView) convertView.findViewById(R.id.siri_left_normal_text);
                    convertView.setTag(viewLeft);

                    break;

                case RIGHT_GOODS:

                    convertView = LayoutInflater.from(mActivity).inflate(
                            R.layout.siri_goods, null);

                    viewGoods = new ViewHolderGoods();
                    viewGoods.ll = (LinearLayoutForListView) convertView.findViewById(R.id.siri_goods_ll);
                    convertView.setTag(viewGoods);

                    break;

                case RIGHT_DIALOG:
                    convertView = LayoutInflater.from(mActivity).inflate(
                            R.layout.siri_dialog, null);

                    viewDialog = new ViewHolderDialog();
                    viewDialog.okTv = (Button) convertView.findViewById(R.id.siri_dialog_ok);
                    viewDialog.goTv = (Button) convertView.findViewById(R.id.siri_dialog_wait);
                    convertView.setTag(viewDialog);

                    break;
                case RIGHT_TEXT_CLICK:
                    convertView = LayoutInflater.from(mActivity).inflate(
                            R.layout.siri_left_normal_buy, null);

                    viewLeftClick = new ViewHolderLeftClick();
                    viewLeftClick.textTv = (TextView) convertView.findViewById(R.id.siri_buy_text);
                    viewLeftClick.textClickTv = (TextView) convertView.findViewById(R.id.siri_buy_click);
                    convertView.setTag(viewLeftClick);

                    break;

            }
        } else {

            switch (type) {
                case TIME:

                    viewTime = (ViewHolderTime) convertView.getTag();
                    break;
                case RIGHT_TEXT:

                    viewRight = (ViewHolderRight) convertView.getTag();
                    break;
                case LEFT_TEXT:

                    viewLeft = (ViewHolderLeft) convertView.getTag();

                    break;

                case RIGHT_GOODS:


                    viewGoods = (ViewHolderGoods) convertView.getTag();

                    break;

                case RIGHT_DIALOG:

                    viewDialog = (ViewHolderDialog) convertView.getTag();

                    break;
                case RIGHT_TEXT_CLICK:

                    viewLeftClick = (ViewHolderLeftClick) convertView.getTag();

                    break;


            }
        }


        switch (type) {
            case TIME:

                viewTime.timeTv.setText((String) mList.get(position).getBody());
                break;
            case RIGHT_TEXT:
                viewRight.headIv.setDefaultImageResId(R.drawable.my_touxiang);
                if (Check.hashead(mActivity)) {
                    viewRight.headIv.setImageUrl(PreferencesUtils.getValueFromSPMap(mActivity, PreferencesUtils.Keys.HEAD_URL, "", PreferencesUtils.Keys.USERINFO), mImageLoader);//用户头像地址
                    viewRight.headIv.setErrorImageResId(R.drawable.my_touxiang);
                }
                viewRight.leftTv.setText((String) mList.get(position).getBody());
                break;
            case LEFT_TEXT:

                viewLeft.rightTv.setText((String) mList.get(position).getBody());
//                viewLeft.rightTv.setEnabled();
                break;

            case RIGHT_GOODS:
                viewGoods.ll.bindLinearLayout(new SimpleGoodAdapter((GoodsListModel) mList.get(position).getBody(), mActivity, mImageLoader,mList.get(position).getId(),db,mList,position));
//                viewGoods.ll.bindLinearLayout(new SimpleGoodAdapter((GoodsListModel) mList.get(position).getBody(), mActivity, mImageLoader));
                break;

            case RIGHT_DIALOG:

/*
* 弹出的反馈对话,选择是否满意,添加网络请求以及相关UI处理
* */


                viewDialog.okTv.setOnClickListener(new OkClick(viewDialog.okTv,viewDialog.goTv,position));

                viewDialog.goTv.setOnClickListener(new GoClick(viewDialog.okTv,viewDialog.goTv,position));

                if(!((boolean) mList.get(position).getBody())){

                    viewDialog.okTv.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.siri_dialog_unselect));
                    viewDialog.goTv.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.siri_dialog_unselect));
                    viewDialog.goTv.setTextColor(mActivity.getResources().getColor(R.color.dialog_text));
                    viewDialog.okTv.setTextColor(mActivity.getResources().getColor(R.color.dialog_text));

                }else{
                    viewDialog.okTv.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.siri_dialog_red_back));
                    viewDialog.goTv.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.siri_dialog_white));
                    viewDialog.goTv.setTextColor(mActivity.getResources().getColor(R.color.key));
                    viewDialog.okTv.setTextColor(mActivity.getResources().getColor(R.color.white));
                }

                break;
            case RIGHT_TEXT_CLICK:

                viewLeftClick.textTv.setText((String) mList.get(position).getBody());
                viewLeftClick.textClickTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转
                        Intent intent = new Intent(mActivity, SelectActivity.class);
                        mActivity.startActivityForResult(intent, REQUEST_CODE);

                    }
                });
                break;

        }

        return convertView;
    }

    public synchronized void pullBackData(final int id) {
        mActivity.executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, UrlConfig.BUY_FEED, BaseModel.class, responseBackListener(), mActivity.errorListener(false)) {
            protected Map<String, String> getParams() {
                return new ApiParams().with("id", "" + id).with("state", "1");//1代表满意
            }

        });
    }

    private Response.Listener<BaseModel> responseBackListener() {
        return new Response.Listener<BaseModel>() {
            @Override
            public void onResponse(BaseModel model) {

            }
        };

    }

    static class ViewHolderTime {
        TextView timeTv;

    }

    static class ViewHolderRight {
        TextView leftTv;
        NetworkImageView headIv;

    }

    static class ViewHolderLeft {
        TextView rightTv;

    }

    static class ViewHolderLeftClick {
        TextView textTv;
        TextView textClickTv;

    }

    static class ViewHolderDialog {
        Button okTv;
        Button goTv;

    }

    static class ViewHolderGoods {
        LinearLayoutForListView ll;

    }


    public class OkClick implements View.OnClickListener {

        private Button okButton;
        private Button goButton;
        private int position;

        public OkClick(Button okButton, Button goButton, int position) {
            this.okButton = okButton;
            this.goButton = goButton;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if ((boolean) mList.get(position).getBody()) {
//                            mList.add(new SiriModel<String>(SiriAdapter.RIGHT_TEXT, mActivity.getResources().getString(R.string.dialog_six)));
//                            mList.add(new SiriModel<String>(SiriAdapter.LEFT_TEXT, mActivity.getResources().getString(R.string.dialog_sev)));

//                            notifyDataSetChanged();
                mList.get(position).setBody(false);
                pullBackData(mList.get(position).getId());
                mBackList.add(new SiriModel<String>(SiriAdapter.LEFT_TEXT, mActivity.getResources().getString(R.string.dialog_nine)));
                DatabaseOperate.insert(db, new DbModel(SiriAdapter.LEFT_TEXT, 0, 0,  mActivity.getResources().getString(R.string.dialog_nine)));
                ++TextSiriActivity.addNew;
                DatabaseOperate.update(db,mList.get(position).getId());
// okButton.setBackgroundColor(mActivity.getResources().getColor(R.color.button_unselect_back));
//                goButton.setBackgroundColor(mActivity.getResources().getColor(R.color.button_unselect_back));
                notifyDataSetChanged();
            }
        }
    }

    public class GoClick implements View.OnClickListener {

        private Button okButton;
        private Button goButton;
        private int position;

        public GoClick(Button okButton, Button goButton, int position) {
            this.okButton = okButton;
            this.goButton = goButton;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
//            网络请求

            if ((boolean) mList.get(position).getBody()) {
                mBackList.add(new SiriModel<String>(SiriAdapter.RIGHT_TEXT, mActivity.getResources().getString(R.string.dialog_six)));
                mBackList.add(new SiriModel<String>(SiriAdapter.LEFT_TEXT, mActivity.getResources().getString(R.string.dialog_sev)));
                DatabaseOperate.insert(db, new DbModel(SiriAdapter.RIGHT_TEXT,0, 0,  mActivity.getResources().getString(R.string.dialog_six)));
                ++TextSiriActivity.addNew;
                DatabaseOperate.insert(db, new DbModel(SiriAdapter.LEFT_TEXT, 0, 0,  mActivity.getResources().getString(R.string.dialog_sev)));
                ++TextSiriActivity.addNew;
                DatabaseOperate.update(db,mList.get(position).getId());

//                okButton.setBackgroundColor(mActivity.getResources().getColor(R.color.button_unselect_back));
//                goButton.setBackgroundColor(mActivity.getResources().getColor(R.color.button_unselect_back));

                mList.get(position).setBody(false);
                notifyDataSetChanged();
            }
        }
    }

}
