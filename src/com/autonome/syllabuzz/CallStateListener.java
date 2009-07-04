package com.autonome.syllabuzz;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.Contacts.People;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
//import android.util.Log;
import android.os.Vibrator;
import com.autonome.syllabuzz.Syllable;

public class CallStateListener extends PhoneStateListener {
	public Context mContext;
	
	public void onCallStateChanged(int state, String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);
		//Log.d("DEBUG", TelephonyManager.CALL_STATE_OFFHOOK + " weeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee are def in here: " + state);

		switch(state) {
			case TelephonyManager.CALL_STATE_IDLE:
				//Log.d("DEBUG", "phhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhone is idle");
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				//Log.d("DEBUG", "phoooooooooooooooooooooooooooooooooooooon is off hook");
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				//Log.d("DEBUG", "phoooooooooooooooooooooooooooooonnnnnnnnnnnnnnnnnnnnnnneeeeeeeeeeeeeeeeee is ringing");
				handleCall(incomingNumber);
				break;
			default:
				//Log.d("DEBUG", "The staaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaate is " + state);

		}
	}
	
	private void handleCall(String incomingNumber) {
		// get contact for number
    	String name = getBestMatchingContactForNumber(incomingNumber);
    	if (name.length() > 0) {
    		
    		// get syllable count for contact name
    		int count = Syllable.syllable(name);
    		
    		if (count > 0) {
    			
    			int len = count * 2; // iterate silence/vibrate
    			long[] vibratePattern = new long[len];
    			for (int i = 0; i < len; i++) {
    				vibratePattern[i] = (i % 2 == 0) ? 200 : 400;
    			}
    			
	    		// vibrate
	    		Vibrator vibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
	    		vibrator.vibrate(vibratePattern, -1);
    		}
    	}
	}
	
	protected String getBestMatchingContactForNumber(String aNumberDialed) {
        // find a contact matching the number dialed
        String name = "";
        
    	//String[] projection = new String[] { People.NAME };
    	/* searching by phone not working
    	String where = People.NUMBER + " == '" + numberDialed + "'";
    	Cursor contacts = cr.query(People.CONTENT_URI, projection, where, null, null);
    	*/
        
    	ContentResolver cr = mContext.getContentResolver();
        Cursor contacts = cr.query(People.CONTENT_URI, null, null, null, null);
        
        String num;
    	if (contacts.getCount() != 0) {
    		contacts.moveToFirst();
    		do {
    			num = contacts.getString(contacts.getColumnIndex(People.NUMBER));
    			if (num != null && num.length() > 0) {
    				if (PhoneNumberUtils.compare(aNumberDialed, num)) {
    					name = contacts.getString(contacts.getColumnIndex(People.NAME));
	    				break;
    				}
    			}
    		} while (contacts.moveToNext());
    	}
    	
    	contacts.close();
        
    	return name;
    }
	
    public static int countEm(java.lang.String t) {
        String [] p = {"[^aeiouy\\W]*[aeiouy]++[^aeiouy\\W]*(e\\W)*\\W*","", "e+\\W","","[aeiouy]++","", "\\W","","[^aeiouy\\W]++\\W++",""};
        if (t.equals("e"))  return 0;
        for (int i = 0; i < p.length/2; i++)
            if (t.matches(p[2*i]+".*"))
                return  (i+1)%2 + countEm(t.replaceFirst(p[2*i],p[2*i+1]));
        return 0;
    }

} 