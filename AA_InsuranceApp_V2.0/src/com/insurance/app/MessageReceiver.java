package com.insurance.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class MessageReceiver extends BroadcastReceiver 
{
    public void onReceive(Context context, Intent intent) {
             Bundle pudsBundle = intent.getExtras();
             Object[] pdus = (Object[]) pudsBundle.get("pdus");
             SmsMessage messages =SmsMessage.createFromPdu((byte[]) pdus[0]);    
             Log.i("BORADCAST_MESSAGE",  messages.getMessageBody());
             if(messages.getMessageBody().contains("LOCJ")) 
             {
                 abortBroadcast();
                 //Parse message
                 String message = messages.getMessageBody();
                 message = message.replaceAll("LOCJ:", "");
                 Wrapper.InvokeSMSResponse(message);
             }
             else if(messages.getMessageBody().contains("GETLOCHASH")) 
             {
                 abortBroadcast();
                 
                String response = "LOCJ:NO";
                GetLocation objGetLocation = new GetLocation(context);
 				if (objGetLocation.displayGpsStatus()) {
 					Location location = objGetLocation.getLocationData();
 					if (location != null) 
 					{
 						response = "LOCJ:"+location.getLatitude() + "," + location.getLongitude();
 					}

 				} else {
 					response="LOCJ:NO";
 				}
                 //Read Geo location value
                 //send Geo location value and sebd Reply;
                 SmsManager smsManager = SmsManager.getDefault();
                 smsManager.sendTextMessage(messages.getOriginatingAddress(), null, response, null, null);
             }
    }
}