package com.liuzhuni.lzn.core.city;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.Base2Activity;
import com.liuzhuni.lzn.core.city.db.DBManager;
import com.liuzhuni.lzn.core.city.model.CityModel;
import com.liuzhuni.lzn.core.city.ui.MyLetterListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CityActivity extends Base2Activity {


    @ViewInject(R.id.overlay)
    private TextView overlay;
    @ViewInject(R.id.city_list)
    private ListView mCityLit;
    @ViewInject(R.id.cityLetterListView)
    private MyLetterListView letterListView;

    private TextView headTv;

    private HashMap<String, Integer> alphaIndexer;
    private ArrayList<CityModel> mCityNames;

    private BaseAdapter adapter;

    private String[] sections;


    private OverlayThread overlayThread;
    private SQLiteDatabase database;

    public LocationClient mLocationClient;
    public CityLocationListener locListener;

    private String currentCity="";


    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        mLocationClient = new LocationClient(this.getApplicationContext());
        locListener = new CityLocationListener();
        mLocationClient.registerLocationListener(locListener);
        initLoc();

        initData();
        findViewById();
        initUI();
        setListener();
    }



    protected void initLoc() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09");
        option.setIsNeedAddress(true);
        option.setScanSpan(50000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    protected void initData() {

        DBManager dbManager = new DBManager(this);
        dbManager.openDateBase();
        dbManager.closeDatabase();
        database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/"
                + DBManager.DB_NAME, null);

        overlayThread = new OverlayThread();
        alphaIndexer = new HashMap<String, Integer>();

        mCityNames = getCityNames();



    }

    @Override
    protected void findViewById() {
        ViewUtils.inject(this);
    }

    @Override
    protected void initUI() {

        View headView = LayoutInflater.from(CityActivity.this).inflate(
                R.layout.city_head, null);
        headTv = (TextView) headView.findViewById(R.id.city_head_tv);
        headTv.setText("正在定位中...");
        if(mCityLit.getHeaderViewsCount()==0){
            mCityLit.addHeaderView(headView);
        }



        setAdapter(mCityNames);


    }

    @Override
    protected void setListener() {

        letterListView
                .setOnTouchingLetterChangedListener(new LetterListViewListener());

        mCityLit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = mCityLit.getItemAtPosition(position);
                if(obj instanceof CityModel){
                    String city=((CityModel)obj).getCityName();

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("city", city);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }




            }
        });



    }


    private void setAdapter(List<CityModel> list) {
        if (list != null) {
            adapter = new ListAdapter(this, list);
            mCityLit.setAdapter(adapter);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        database.close();
    }

    /**
     * 从数据库获取城市数据
     *
     * @return
     */
    private ArrayList<CityModel> getCityNames() {
        ArrayList<CityModel> names = new ArrayList<CityModel>();

        Cursor cursor = database.rawQuery(
                "SELECT * FROM T_city ORDER BY Letter", null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            CityModel cityModel = new CityModel();
            cityModel.setCityName(cursor.getString(cursor
                    .getColumnIndex("ZoneName")));
            cityModel.setNameSort(cursor.getString(cursor
                    .getColumnIndex("Letter")));
            cityModel.setZongID(cursor.getString(cursor
                    .getColumnIndex("ZoneId")));
            cityModel.setCityNameEN(cursor.getString(cursor
                    .getColumnIndex("EN")));
            names.add(cityModel);
        }
        cursor.close();
        return names;
    }


    @OnClick(R.id.to_buy_close)
    public void back(View v) {

        finish();
    }


    private class ListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<CityModel> list;

        public ListAdapter(Context context, List<CityModel> list) {

            this.inflater = LayoutInflater.from(context);
            this.list = list;
            alphaIndexer = new HashMap<String, Integer>();
            sections = new String[list.size()];

            for (int i = 0; i < list.size(); i++) {
                // 当前汉语拼音首字母
                // getAlpha(list.get(i));
                String currentStr = list.get(i).getNameSort();
                // 上一个汉语拼音首字母，如果不存在为“ ”
                String previewStr = (i - 1) >= 0 ? list.get(i - 1)
                        .getNameSort() : " ";
                if (!previewStr.equals(currentStr)) {
                    String name = list.get(i).getNameSort();
                    alphaIndexer.put(name, i);
                    sections[i] = name;
                }
            }

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.city_detail, null);
                holder = new ViewHolder();
                holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(list.get(position).getCityName());
            String currentStr = list.get(position).getNameSort();
            String previewStr = (position - 1) >= 0 ? list.get(position - 1)
                    .getNameSort() : " ";
            if (!previewStr.equals(currentStr)) {
                holder.alpha.setVisibility(View.VISIBLE);
                if (currentStr.equals("#"))
                    holder.alpha.setText("热门城市");
                else
                    holder.alpha.setText(currentStr);
            } else {
                holder.alpha.setVisibility(View.GONE);
            }
            return convertView;
        }

        private class ViewHolder {
            TextView alpha;
            TextView name;
        }

    }


    private class LetterListViewListener implements
            MyLetterListView.OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(final String s) {
            if (alphaIndexer.get(s) != null) {
                int position = alphaIndexer.get(s);
                mCityLit.setSelection(position);
                overlay.setText(sections[position]);
                overlay.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                // 延迟一秒后执行，让overlay为不可见
                handler.postDelayed(overlayThread, 1500);
            }
        }

    }

    // 设置overlay不可见
    private class OverlayThread implements Runnable {

        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }

    }


    public class CityLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            currentCity=bdLocation.getCity();
            if (!TextUtils.isEmpty(currentCity)) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        View headView = LayoutInflater.from(CityActivity.this).inflate(
//                                R.layout.city_head, null);
//                        headTv = (TextView) headView.findViewById(R.id.city_head_tv);
                        headTv.setText("当前城市:"+currentCity);
//                        if(mCityLit.getHeaderViewsCount()==0){
//                            mCityLit.addHeaderView(headView);
//                        }
                        headTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!TextUtils.isEmpty(currentCity)){
                                    //返回
                                    Intent intent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("city", currentCity);
                                    intent.putExtras(bundle);
                                    setResult(RESULT_OK, intent);
                                    finish();


                                }else{
                                    finish();
                                }
                            }
                        });
                    }
                }) ;
            }
            mLocationClient.stop();
        }
    }


}
