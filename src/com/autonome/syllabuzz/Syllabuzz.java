package com.autonome.syllabuzz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.autonome.syllabuzz.CallStateListener;
//import android.util.Log;

public class Syllabuzz extends BroadcastReceiver {
    
    public void onReceive(Context context, Intent intent) {
    	TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    	CallStateListener callStateListener = new CallStateListener();
    	callStateListener.mContext = context;
    	tManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

}