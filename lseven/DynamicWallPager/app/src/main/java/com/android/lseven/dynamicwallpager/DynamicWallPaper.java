package com.android.lseven.dynamicwallpager;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by lili.zheng on 2017/5/15.
 */

public class DynamicWallPaper extends WallpaperService{
    @Override
    public Engine onCreateEngine() {
        return new MyEngine();
    }

    public class MyEngine extends Engine{//壁纸的生命周期、Surface状态的变化以及对用户的输入事件进行响应

        private MediaPlayer mediaPlayer = null ;

        @Override
        public SurfaceHolder getSurfaceHolder() {
            return super.getSurfaceHolder();
        }

        @Override
        public void setTouchEventsEnabled(boolean enabled) {
            super.setTouchEventsEnabled(enabled);
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            initMediaPlayer(holder);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            Log.d("LSeven","MyEngine#onVisibilityChanged visible = " + visible);
            //此处如果不写画面不会播放出来
            //播放的画面下面会自带有“设置壁纸”透明按钮可进行设置壁纸操作
            if (visible) {
                mediaPlayer.start();
            } else {
                mediaPlayer.pause();
            }
        }

        private void initMediaPlayer(SurfaceHolder holder){
            mediaPlayer = new MediaPlayer();
            try {
                AssetManager assetMg = getApplicationContext().getAssets();
                AssetFileDescriptor fileDescriptor = assetMg.openFd(此处资源asset请从鸿洋大神那获取);
                mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                        fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                mediaPlayer.setDisplay(holder);
                mediaPlayer.prepare();
                mediaPlayer.setLooping(true);
                mediaPlayer.setVolume(0, 0);
                mediaPlayer.prepare();
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            if (null!=mediaPlayer){
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }


}
