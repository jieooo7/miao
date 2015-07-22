package com.liuzhuni.lzn.example.qr_codescan;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.Base2Activity;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.goods.GoodsActivity;
import com.liuzhuni.lzn.core.goods.model.GoodsModel;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.core.personInfo.HelpActivity;
import com.liuzhuni.lzn.mining.app.zxing.camera.CameraManager;
import com.liuzhuni.lzn.mining.app.zxing.decoding.CaptureActivityHandler;
import com.liuzhuni.lzn.mining.app.zxing.decoding.InactivityTimer;
import com.liuzhuni.lzn.mining.app.zxing.view.ViewfinderView;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonBaseRequest;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;

/**
 * Initial the camera
 * @author Ryan.Tang
 */
public class MipcaActivityCapture extends Base2Activity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;//test


    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        if(!PreferencesUtils.getBooleanFromSPMap(this, PreferencesUtils.Keys.IS_FIRST_CODE)){
            PreferencesUtils.putBooleanToSPMap(activity, PreferencesUtils.Keys.IS_FIRST_CODE, true);
            Intent intent=new Intent(MipcaActivityCapture.this,HelpActivity.class);
            Bundle goodsBundle = new Bundle();
            goodsBundle.putBoolean("isHelp", false);
            intent.putExtras(goodsBundle);
            startActivity(intent);
        }


            setContentView(R.layout.activity_capture);
            //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);

            initUi();
            CameraManager.init(getApplication());
            viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}


    public void initUi(){

        TextView leftTv=(TextView) findViewById(R.id.title_left);
        TextView middleTv=(TextView) findViewById(R.id.title_middle);
        TextView rightTv=(TextView) findViewById(R.id.title_right);
        TextView helpTv=(TextView) findViewById(R.id.scan_help_tv);

        leftTv.setText(getResources().getText(R.string.i_want_back));
        middleTv.setText(getResources().getText(R.string.scan_title));
        rightTv.setVisibility(View.GONE);


        leftTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        helpTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转帮助页面

                Intent intent=new Intent(MipcaActivityCapture.this,HelpActivity.class);
                Bundle goodsBundle = new Bundle();
                goodsBundle.putBoolean("isHelp", false);
                intent.putExtras(goodsBundle);
                startActivity(intent);
            }
        });



    }

	@Override
	protected void onResume() {
		super.onResume();

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
		
	}

	@Override
	protected void onPause() {
		super.onPause();
        viewfinderView.initDraw();

		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
        super.onDestroy();
        inactivityTimer.shutdown();
	}

    @Override
    protected void initData() {

    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void setListener() {

    }

    /**
	 * ����ɨ����
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		if (resultString.equals("")) {
			Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		}else {

            loadingdialog.show();
            pullCodeGoodsData(resultString);

//			Intent resultIntent = new Intent();
//			Bundle bundle = new Bundle();
//			bundle.putString("result", resultString);
////			bundle.putParcelable("bitmap", barcode);
//			resultIntent.putExtras(bundle);
//			this.setResult(RESULT_OK, resultIntent);
		}



//        CameraManager.init(getApplication());
//        hasSurface = false;
//        inactivityTimer = new InactivityTimer(this);
//
//
//
//
//继续 扫描
//        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
//        SurfaceHolder surfaceHolder = surfaceView.getHolder();
//        if (hasSurface) {
//            initCamera(surfaceHolder);
//        } else {
//            surfaceHolder.addCallback(this);
//            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        }
//        decodeFormats = null;
//        characterSet = null;
//
//        playBeep = true;
//        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
//        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
//            playBeep = false;
//        }
//        initBeepSound();
//        vibrate = true;



	}


    protected synchronized void pullCodeGoodsData(final String code){
        executeRequest(new GsonBaseRequest<BaseModel<GoodsModel>>(Request.Method.POST, UrlConfig.SCAN,new TypeToken<BaseModel<GoodsModel>>(){}.getType(),responseGoodsListener(),errorScanListener()){

            protected Map<String, String> getParams() {
                return new ApiParams().with("code", code);
            }

        });
    }

    private Response.Listener<BaseModel<GoodsModel>> responseGoodsListener() {
        return new Response.Listener<BaseModel<GoodsModel>>(){
            @Override
            public void onResponse(BaseModel<GoodsModel> goodsModel) {
                loadingdialog.dismiss();
                if(goodsModel.getRet()==0){
                    if(goodsModel.getData()==null){
                        //提示没找到
                        ToastUtil.show(MipcaActivityCapture.this,getResources().getText(R.string.scan_error));

                        return;
                    }
                    Intent intent = new Intent(MipcaActivityCapture.this, GoodsActivity.class);
                    Bundle goodsBundle = new Bundle();
                    goodsBundle.putBoolean("isCode", true);
                    goodsBundle.putSerializable("model",goodsModel.getData());
                    intent.putExtras(goodsBundle);
                    startActivity(intent);


                    MipcaActivityCapture.this.finish();



                }else{
                    ToastUtil.show(MipcaActivityCapture.this,goodsModel.getMes());
                }

            }
        };

    }

    public Response.ErrorListener errorScanListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(activity,getResources().getText(R.string.error_retry), Toast.LENGTH_LONG).show();
                loadingdialog.dismiss();
                if(error.networkResponse!=null){
                    if(error.networkResponse.statusCode==402){//重新登录
                    }else{
//                        ToastUtil.customShow(MipcaActivityCapture.this, getResources().getText(R.string.error_retry));
                    }
                }else{
//                    ToastUtil.customShow(MipcaActivityCapture.this, getResources().getText(R.string.bad_net));
                }
            }
        };
    }



	
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}