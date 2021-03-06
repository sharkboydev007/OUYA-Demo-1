package tv.ouya.sdk;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import tv.ouya.console.api.DebugInput;
import tv.ouya.console.api.OuyaActivity;
import tv.ouya.console.api.OuyaController;

public class OuyaUnityActivity extends OuyaActivity
{
	private static final String TAG = OuyaUnityActivity.class.getSimpleName();	

	static {
    	Log.i(TAG, "Loading lib-ouya-ndk...");
        System.loadLibrary("-ouya-ndk");
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
	}

	public native void dispatchGenericMotionEventNative(int deviceId, int axis, float value);
    public native void dispatchKeyEventNative(int deviceId, int keyCode, int action);

	/*
	@Override
	public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
		DebugInput.debugMotionEvent(motionEvent);
		return super.dispatchGenericMotionEvent(motionEvent);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent keyEvent) {
		Log.i(TAG, "dispatchKeyEvent keyCode=" + DebugInput.debugGetButtonName(keyEvent.getKeyCode()));
		return super.dispatchKeyEvent(keyEvent);
	}
	*/

	@Override
	public boolean onGenericMotionEvent(MotionEvent motionEvent) {
		//DebugInput.debugMotionEvent(motionEvent);

		int playerNum = OuyaController.getPlayerNumByDeviceId(motionEvent.getDeviceId());	    
	    if (playerNum < 0) {
	    	Log.e(TAG, "Failed to find playerId for Controller="+motionEvent.getDevice().getName());
	    	return true;
	    }

		dispatchGenericMotionEventNative(playerNum, OuyaController.AXIS_LS_X, motionEvent.getAxisValue(OuyaController.AXIS_LS_X));
		dispatchGenericMotionEventNative(playerNum, OuyaController.AXIS_LS_Y, motionEvent.getAxisValue(OuyaController.AXIS_LS_Y));
		dispatchGenericMotionEventNative(playerNum, OuyaController.AXIS_RS_X, motionEvent.getAxisValue(OuyaController.AXIS_RS_X));
		dispatchGenericMotionEventNative(playerNum, OuyaController.AXIS_RS_Y, motionEvent.getAxisValue(OuyaController.AXIS_RS_Y));
		dispatchGenericMotionEventNative(playerNum, OuyaController.AXIS_L2, motionEvent.getAxisValue(OuyaController.AXIS_L2));
		dispatchGenericMotionEventNative(playerNum, OuyaController.AXIS_R2, motionEvent.getAxisValue(OuyaController.AXIS_R2));
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
		//Log.i(TAG, "onKeyUp keyCode=" + DebugInput.debugGetButtonName(keyCode));

		int playerNum = OuyaController.getPlayerNumByDeviceId(keyEvent.getDeviceId());	    
	    if (playerNum < 0) {
	    	Log.e(TAG, "Failed to find playerId for Controller="+keyEvent.getDevice().getName());
	    	return true;
	    }

		int action = keyEvent.getAction();
		dispatchKeyEventNative(playerNum, keyCode, action);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
		//Log.i(TAG, "onKeyDown keyCode=" + DebugInput.debugGetButtonName(keyCode));

		int playerNum = OuyaController.getPlayerNumByDeviceId(keyEvent.getDeviceId());	    
	    if (playerNum < 0) {
	    	Log.e(TAG, "Failed to find playerId for Controller="+keyEvent.getDevice().getName());
	    	return true;
	    }

		int action = keyEvent.getAction();
		dispatchKeyEventNative(playerNum, keyCode, action);
		return true;
	}
}