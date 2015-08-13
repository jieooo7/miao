package com.liuzhuni.lzn.core.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.liuzhuni.lzn.utils.fileHelper.FileHelper;
import com.liuzhuni.lzn.utils.fileHelper.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CacheService extends Service {
   private final long WEEK_TIME=7*24*60*60*1000; //一周时间


    public CacheService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();


        boolean isDelete;
        final List<File> fileList=new ArrayList<File>();
        final File cacheDir = new File(FileManager.getSaveFilePath()+"/"+this.getPackageName()+"/cache");

        new Thread(new Runnable() {
            @Override
            public void run() {
                deleteFile(cacheDir,fileList);
            }
        }).start();


    }


    private void deleteFile(File cacheDir,List<File> fileList){
        Date date=new Date();
        long currentTime=date.getTime();
        if (cacheDir.isDirectory()) {
            File[] list = cacheDir.listFiles();
            if(list!=null){
                for(File file:list){
                    if(currentTime-file.lastModified()>WEEK_TIME){
                        fileList.add(file);
                    }

                }
            }
            if(fileList.size()>0){
                for(File file:fileList){
                    FileHelper.deleteDirectory(file);
                }
            }
            stopSelf();//删除完成后,停止服务

        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
