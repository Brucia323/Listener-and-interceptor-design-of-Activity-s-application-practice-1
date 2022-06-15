//MyService.java
package com.example.myapplication;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.IOException;

public class MyService extends Service {
    TelephonyManager telephonyManager;
    MediaRecorder mediaRecorder = null;
    boolean startedState = false;
    MyListen myListen;

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myListen = new MyListen();
        telephonyManager.listen(myListen, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        telephonyManager.listen(myListen, PhoneStateListener.LISTEN_NONE);
        myListen = null;
    }

    class MyListen extends PhoneStateListener {
        /**
         * Callback invoked when device call state changes.
         * <p>
         * Reports the state of Telephony (mobile) calls on the device for the registered subscription.
         * <p>
         * Note: the registration subId comes from {@link TelephonyManager} object which registers
         * PhoneStateListener by {@link TelephonyManager#listen(PhoneStateListener, int)}.
         * If this TelephonyManager object was created with
         * {@link TelephonyManager#createForSubscriptionId(int)}, then the callback applies to the
         * subId. Otherwise, this callback applies to
         * {@link SubscriptionManager#getDefaultSubscriptionId()}.
         * <p>
         * Note: The state returned here may differ from that returned by
         * {@link TelephonyManager#getCallState()}. Receivers of this callback should be aware that
         * calling {@link TelephonyManager#getCallState()} from within this callback may return a
         * different state than the callback reports.
         * <p>
         * Requires Permission:
         * {@link Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE} for applications
         * targeting API level 31+.
         *
         * @param state       call state
         * @param phoneNumber call phone number. If application does not have
         *                    {@link Manifest.permission#READ_CALL_LOG READ_CALL_LOG} permission or carrier
         *                    privileges (see {@link TelephonyManager#hasCarrierPrivileges}), an empty string will be
         *                    passed as an argument.
         * @deprecated Use {@link TelephonyCallback.CallStateListener} instead.
         */
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            super.onCallStateChanged(state, phoneNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if (startedState) {
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;
                        startedState = false;
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".3gp");
                    String path = file.getAbsolutePath();
                    mediaRecorder.setOutputFile(path);
                    mediaRecorder.setAudioChannels(MediaRecorder.AudioEncoder.DEFAULT);
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                        startedState = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    break;
            }
        }
    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}