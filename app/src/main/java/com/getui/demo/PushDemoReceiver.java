package com.getui.demo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.igexin.sdk.PushConsts;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.core.index_new.model.PushModel;
import com.liuzhuni.lzn.core.siri.TextSiriActivity;
import com.liuzhuni.lzn.utils.PreferencesUtils;

public class PushDemoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {

            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");

//			String taskid = bundle.getString("taskid");
//			String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
//			boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
//			System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));
//            System.out.println(new String(payload));
                if (payload != null) {
                    String data = new String(payload);//使用透传的字段解析出Huim://detail?id=跳转协议

                    try {
                        PushModel model = new Gson().fromJson(data, PushModel.class);
//                    Log.e("getuidedata", data);
                        Uri uri = Uri.parse(model.getContent());

                        if (uri.getHost().startsWith("temp")) {

                            Intent codeIntent = new Intent(context, TextSiriActivity.class); //需要跳转到那个界面

                            codeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(codeIntent);
                        }

                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }


//                showNotification(context, -1, model.getText(), model.getTitle(), model.getContent(), null);//全部使用透传

               /* try {
                    BaseListModel<DbModel> dbModel=new Gson().fromJson(data,new TypeToken<BaseListModel<DbModel>>() {
                    }.getType());
                    if(dbModel.getRet()==0){
                        Intent codeIntent = new Intent(context, TextSiriActivity.class); //需要跳转到那个界面
                        codeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        Bundle goodsBundle = new Bundle();
                        goodsBundle.putString("body",new Gson().toJson(dbModel.getData()));
                        goodsBundle.putInt("id", dbModel.getId());
                        goodsBundle.putBoolean("ispush", true);
                        codeIntent.putExtras(goodsBundle);
                        codeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(codeIntent);
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }

                Log.d("GetuiSdkDemo", "Got Payload:" +data);*/
                }
                break;
            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
//                System.out.println("个推ID" + cid);
                if (!TextUtils.isEmpty(cid)) {
                    if (!cid.equals(PreferencesUtils.getValueFromSPMap(context, PreferencesUtils.Keys.CLIENTID, "", PreferencesUtils.Keys.USERINFO))) {//存储cid
                        PreferencesUtils.putValueToSPMap(context, PreferencesUtils.Keys.CLIENTID, cid, PreferencesUtils.Keys.USERINFO);
                        PreferencesUtils.putBooleanToSPMap(context, PreferencesUtils.Keys.IS_SEND_CLIENTID, false, PreferencesUtils.Keys.USERINFO);
                    }
                }

                break;
            case PushConsts.THIRDPART_FEEDBACK:
            /*String appid = bundle.getString("appid");
            String taskid = bundle.getString("taskid");
			String actionid = bundle.getString("actionid");
			String result = bundle.getString("result");
			long timestamp = bundle.getLong("timestamp");

			Log.d("GetuiSdkDemo", "appid = " + appid);
			Log.d("GetuiSdkDemo", "taskid = " + taskid);
			Log.d("GetuiSdkDemo", "actionid = " + actionid);
			Log.d("GetuiSdkDemo", "result = " + result);
			Log.d("GetuiSdkDemo", "timestamp = " + timestamp);*/
                break;
            default:
                break;
        }
    }

    /**
     * 显示通知栏
     */
    public static void showNotification(Context context, int id, String description, String bigText, String scheme, String delScheme) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.push);//小icon
        builder.setContentText(description);
        builder.setContentTitle(bigText);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(bigText);
        bigTextStyle.bigText(bigText);
        builder.setStyle(bigTextStyle);

        //用户点击提醒后做的动作
        Uri content_url = Uri.parse(scheme);
        Intent contentIntent = new Intent(Intent.ACTION_VIEW, content_url);
        if (id < 0) {
            id = R.string.app_name;
        }
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context,
                id, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentPendingIntent);

        if (!TextUtils.isEmpty(delScheme)) {
            Uri delUri = Uri.parse(delScheme);
            Intent delIntent = new Intent(Intent.ACTION_VIEW, delUri);

            PendingIntent delPendingIntent = PendingIntent.getActivity(context,
                    id, delIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setDeleteIntent(delPendingIntent);//设置清除通知时的响应
        }

        Notification notification = builder.build();
        notification.tickerText = description;

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
//		notificationManager.notify(R.string.app_name, notification);
        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }

}
