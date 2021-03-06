package com.liuzhuni.lzn.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.liuzhuni.lzn.GetInfo;
import com.liuzhuni.lzn.config.AppManager;
import com.liuzhuni.lzn.sec.aes.AESHelper;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
/**
 * @名称：PostSimulation.java
 * @描述：
 * @author Andrew Lee 2014-9-21下午12:10:15
 */
public class PostSimulation {
	
	private static PostSimulation instance = null;
	private Vector properties = null;
    private final static String IMEI=((TelephonyManager)AppManager.getAppManager().currentActivity().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    private final static String CLIENT="0";
    private final static String VERSION="1";
	private String multipart_form_data = "multipart/form-data";
	private String twoHyphens = "--";
	private String boundary = "****************fD4fH3gL0hK7aI6"; // 数据分隔符
	private String lineEnd = "\r\n" ;// The value is

    private String customKey=new GetInfo().getKey(AppManager.getAppManager().currentActivity());
															// "\r\n" in
															// Windows.
	private PostSimulation() {
	}
	
	private static synchronized void syncInit() {
		if (instance == null) {
			instance = new PostSimulation();
		}
	}
	
	public static PostSimulation getInstance() {
		if (instance == null) {
			syncInit();
		}
		return instance;
	}
	public Vector getProperties() {
		return properties;
	}

	public void updateProperties() {
		PostSimulation shadow = new PostSimulation();
		properties = shadow.getProperties();
	}
	/*
	 * 上传图片内容，格式请参考HTTP 协议格式。
	 * 人人网Photos.upload中的”程序调用“http://wiki.dev.renren.com/
	 * wiki/Photos.upload#.E7.A8.8B.E5.BA.8F.E8.B0.83.E7.94.A8 对其格式解释的非常清晰。
	 * 格式如下所示： --****************fD4fH3hK7aI6 Content-Disposition: form-data;
	 * name="upload_file"; filename="apple.jpg" Content-Type: image/jpeg
	 * 
	 * 这儿是文件的内容，二进制流的形式
	 */
	private void addImageContent(String fileKey,String fileName, DataOutputStream output,Bitmap bitmap) {
//			File file=new File(fileName);
//        File file = new File(CommonUtil.getRootFilePath()+"/123456.jpg");
			StringBuilder split = new StringBuilder();
			split.append(twoHyphens + boundary + lineEnd);
			split.append("Content-Disposition: form-data; name=\""
					+ fileKey + "\"; filename=\""
					+ fileName+ "\"" + lineEnd);
			split.append("Content-Type:image/jpeg" + lineEnd);
			split.append(lineEnd);
			try {
				// 发送图片数据
				output.writeBytes(split.toString());
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
//                BitmapFactory.Options newOpts = new BitmapFactory.Options();
//                newOpts.inJustDecodeBounds = false;
//                int w=bitmap.getWidth();//200 crop已经裁剪完成
//                int h=bitmap.getHeight();
//                System.out.println("宽度"+w);
//                System.out.println("高度"+h);
//
//                float hh = 100f;//
//                float ww = 100f;//
//                int be = 1;
//                if (w > h && w > ww) {
//                    be = (int) (w / ww);
//                } else if (w < h && h > hh) {
//                    be = (int) (h / hh);
//                }
//                if (be <= 0)
//                    be = 1;
//                newOpts.inSampleSize = be;// 设置采样率
//
//                newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;// 该模式是默认的,可不设
//                newOpts.inPurgeable = true;// 同时设置才会有效
//                newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
//                byte[] buffer = out.toByteArray();
//                bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, newOpts);
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//                out.flush();
//                out.close();

//                output.write(out.toByteArray());
                out.writeTo(output);


				output.writeBytes(lineEnd);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		
	}

	/*
	 * 构建表单字段内容，格式请参考HTTP 协议格式（用FireBug可以抓取到相关数据）。(以便上传表单相对应的参数值) 格式如下所示：
	 * --****************fD4fH3hK7aI6 Content-Disposition: form-data;
	 * name="action" // 一空行，必须有 upload
	 */
	private void addFormField(String key,String value,
			DataOutputStream output) {
		StringBuilder sb = new StringBuilder();
			sb.append(twoHyphens + boundary + lineEnd);
			sb.append("Content-Disposition: form-data; name=\""
					+ key + "\"" + lineEnd);
			sb.append(lineEnd);
			sb.append(value + lineEnd);
		try {
			output.write(sb.toString().getBytes("utf-8"));// 发送表单字段数据
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 直接通过 HTTP 协议提交数据到服务器，实现表单提交功能。
	 * 
	 * @param actionUrl
	 *            上传路径
	 *            请求参数key为参数名，value为参数值
	 *            上传文件信息
	 * @return 返回请求结果
	 */
	public String post(String actionUrl,String fileKey,List<String> fileNames,Bitmap bitmap,List<String> keys,Map<String ,String> maps) {
		HttpURLConnection conn = null;
		DataOutputStream output = null;
		BufferedReader input = null;
		try {
			URL url = new URL(actionUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(120000);
			conn.setDoInput(true); // 允许输入
			conn.setDoOutput(true); // 允许输出
			conn.setUseCaches(false); // 不使用Cache

            Random random = new Random();
            long num=random.nextLong();
            String accessToken=PreferencesUtils.getValueFromSPMap(AppManager.getAppManager().currentActivity(), PreferencesUtils.Keys.TOKEN, "", PreferencesUtils.Keys.USERINFO);
            String authName=PreferencesUtils.getValueFromSPMap(AppManager.getAppManager().currentActivity(), PreferencesUtils.Keys.AUTH,"",PreferencesUtils.Keys.USERINFO);
            String md5Key="";
            String keyAuth="";
            if(TextUtils.isEmpty(accessToken)){
                md5Key= Md5Utils.md5(actionUrl.toLowerCase()+IMEI+num).toLowerCase();//create md5
                keyAuth= AESHelper.encrypt(md5Key + " " + num, customKey);//aes encode

            }else{
                md5Key=Md5Utils.md5(actionUrl.toLowerCase()+authName+num).toLowerCase();
                keyAuth=AESHelper.encrypt(md5Key.toLowerCase()+" "+num,AESHelper.decrypt(accessToken,customKey));
            }
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Custom-Auth-Name", authName);
			conn.setRequestProperty("Custom-Auth-Key", keyAuth);
			conn.setRequestProperty("imei ",IMEI);
			conn.setRequestProperty("client ",CLIENT);
			conn.setRequestProperty("version ",VERSION);
			conn.setRequestProperty("Content-Type", multipart_form_data
					+ "; boundary=" + boundary);

			conn.connect();
			output = new DataOutputStream(conn.getOutputStream());
			if(keys!=null){
				for(String key : keys){
					addFormField(key, maps.get(key), output);
				}
			}
			if(fileNames !=null){
				
				for(String fileName:fileNames){
					addImageContent(fileKey, fileName, output,bitmap);
					
				}
			}
			

//			addImageContent(files, output); // 添加图片内容
//
//			addFormField(params, output); // 添加表单字段内容

			output.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);// 数据结束标志
			output.flush();

			int code = conn.getResponseCode();
			if (code != 200) {
//				throw new RuntimeException("请求‘" + actionUrl + "’失败！");
                return null;
			}

			input = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			StringBuilder response = new StringBuilder();
			String oneLine;
			while ((oneLine = input.readLine()) != null) {
				response.append(oneLine + lineEnd);
			}

			return response.toString();
		} catch (IOException e) {
//			throw new RuntimeException(e);
            return null;
		} finally {
			// 统一释放资源
			try {
				if (output != null) {
					output.close();
				}
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
//				throw new RuntimeException(e);
			}

			if (conn != null) {
				conn.disconnect();
			}
		}
	}

}