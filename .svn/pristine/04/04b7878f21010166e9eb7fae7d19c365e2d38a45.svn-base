package com.liuzhuni.lzn.base;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.liuzhuni.lzn.volley.RequestManager;

public abstract class BaseFragment extends Fragment {
	

    protected void executeRequest(Request<?> request) {
        RequestManager.addRequest(request, getActivity());
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
    }
}

