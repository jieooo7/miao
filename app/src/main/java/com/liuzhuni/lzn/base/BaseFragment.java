package com.liuzhuni.lzn.base;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.core.loading.LoadingDialog;
import com.liuzhuni.lzn.core.login.LoginActivity;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.utils.fileHelper.CommonUtil;
import com.liuzhuni.lzn.volley.RequestManager;

public abstract class BaseFragment extends Fragment {

    public LoadingDialog loadingdialog=null;
    public boolean isTouch=true;


    public void executeRequest(Request<?> request) {
        if(loadingdialog==null){

            loadingdialog = new LoadingDialog(getActivity());
        }
        if (!CommonUtil.checkNetState(getActivity())) {
//            ToastUtil.customShow(this, getResources().getText(R.string.bad_net));
            return;
        }
        RequestManager.addRequest(request, this);

    }

    public Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(activity,getResources().getText(R.string.error_retry), Toast.LENGTH_LONG).show();
                loadingdialog.dismiss();

                isTouch=true;
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 401) {//重新登录
                        PreferencesUtils.putBooleanToSPMap(getActivity(), PreferencesUtils.Keys.IS_LOGIN, false);
                        PreferencesUtils.clearSPMap(getActivity(), PreferencesUtils.Keys.USERINFO);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
//                        ToastUtil.customShow(Base2Activity.this, getResources().getText(R.string.error_retry));
                    }
                } else {
//                    ToastUtil.customShow(Base2Activity.this, getResources().getText(R.string.bad_net));
                }
//                RequestManager.cancelAll(this);
            }
        };
    }

    public Response.ErrorListener errorListener(final boolean isToast) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(activity,getResources().getText(R.string.error_retry), Toast.LENGTH_LONG).show();
                loadingdialog.dismiss();
                isTouch=true;
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 401) {//重新登录
                        PreferencesUtils.putBooleanToSPMap(getActivity(), PreferencesUtils.Keys.IS_LOGIN, false);
                        PreferencesUtils.clearSPMap(getActivity(), PreferencesUtils.Keys.USERINFO);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        if (isToast) {
                            ToastUtil.customShow(getActivity(), getResources().getText(R.string.error_retry));
                        }
                    }
                } else {
                    if (isToast) {
                        ToastUtil.customShow(getActivity(), getResources().getText(R.string.bad_net));
                    }
                }
//                RequestManager.cancelAll(this);
            }
        };
    }

}

