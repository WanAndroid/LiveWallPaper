package com.liumeng.liveWallPager;

import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.io.File;

/**
 * Created by liumeng on 2017/5/12.
 */

public class CameraLiveWallpaper extends WallpaperService {


    @Override
    public Engine onCreateEngine() {
//        return new VideoEngine();

        return new VideoEngine();
    }


    class CameraEngine extends Engine implements Camera.PreviewCallback {

        private Camera mCamera;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            Log.i("aaa","oncreate");
            startPreview();
            setTouchEventsEnabled(true);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            stopPreview();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {

                startPreview();
            } else {
                Log.i("aaa","wallpager invisible");
                stopPreview();
            }
        }

        public void startPreview() {

//            if(mCamera != null){
//                mCamera.setPreviewCallback(null);
//                mCamera.release();
//                mCamera = null;
//            }
            if (mCamera == null) {
                Log.i("aaa", "wallpager startPreview " + System.currentTimeMillis());

                try {
                    mCamera = Camera.open(0);
                    if (mCamera != null) {
                        mCamera.setDisplayOrientation(90);
                        mCamera.setPreviewDisplay(getSurfaceHolder());

                        mCamera.startPreview();
                    }
                } catch (Exception e) {
                    Log.i("aaa","wallpager "+e.getMessage());
                }


            }

        }

        public void stopPreview() {
            if (mCamera != null) {
                try {
                    mCamera.stopPreview();
                    mCamera.setPreviewCallback(null);

                } catch (Exception e) {
                    Log.i("aaa", "Exception " + System.currentTimeMillis());
                } finally {
                    mCamera.release();
                    mCamera = null;
                }


                Log.i("aaa", "wallpager stopPreview " + System.currentTimeMillis());
            }
        }


        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            mCamera.addCallbackBuffer(data);
        }
    }


    /**
     * 播放视频
     */
    class VideoEngine extends Engine{

        private MediaPlayer mediaPlayer ;
        private String videoPath ;

        /**
         * 播放
         */
        private void play(SurfaceHolder surfaceHolder,String videoPath){
            mediaPlayer = new MediaPlayer();
            // 设置多媒体流类型
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            // 设置用于展示mediaPlayer的容器
            mediaPlayer.setSurface(surfaceHolder.getSurface());
//            mediaPlayer.setDisplay(surfaceHolder);//用这个方法会报 Wallpapers do not support keep screen on
            try {
                mediaPlayer.setDataSource(videoPath);
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            } catch (Exception e) {
                Log.i("通知", "播放过程中出现了错误哦");
            }
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);
            videoPath = Environment.getExternalStorageDirectory()+ File.separator+"myVideo"+File.separator+"lm.mp4";
            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    play(holder,videoPath);
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    if(mediaPlayer != null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                }

            });

        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
        }
    }

}
