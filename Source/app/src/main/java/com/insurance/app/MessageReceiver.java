package com.insurance.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import com.itextpdf.text.pdf.PdfObject;

public class MessageReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        SmsMessage messages = SmsMessage.createFromPdu((byte[]) ((Object[]) intent.getExtras().get("pdus"))[0]);
        Log.i("BORADCAST_MESSAGE", messages.getMessageBody());
        /*if (messages.getMessageBody().contains("LOCJ")) {
            abortBroadcast();
            Wrapper.InvokeSMSResponse(messages.getMessageBody().replaceAll("LOCJ:", PdfObject.NOTHING));
        } else if (messages.getMessageBody().contains("GETLOCHASH")) {
            abortBroadcast();
            String response = "LOCJ:NO";
            GetLocation objGetLocation = new GetLocation(context);
            if (objGetLocation.displayGpsStatus().booleanValue()) {
                Location location = objGetLocation.getLocationData();
                if (location != null) {
                    response = "LOCJ:" + location.getLatitude() + "," + location.getLongitude();
                }
            } else {
                response = "LOCJ:NO";
            }
            SmsManager.getDefault().sendTextMessage(messages.getOriginatingAddress(), null, response, null, null);
        }*/
    }
}
