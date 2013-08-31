package com.benzu.homeautomation;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SMSActivity extends Activity {

	private String dstPhoneNumber;
	private final String[] msg = { "GET TEMP", "TURN FAN ON", "TURN FAN OFF",
			"LIGHT ON", "LIGHT OFF", "FAN STATUS", "LIGHT STATUS" };
	// private static int ind = 0;
	TelephonyManager tMgr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout);

		tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		for (int i = 0; i < msg.length; i++) {
			Button tempButton = getButton(i);
			final int ind = i;
			tempButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dstPhoneNumber = ((TextView) findViewById(R.id.phoneNumber))
							.getText().toString();
					sendSMS(ind);

				}
			});
		}

	}

	private Button getButton(int id) {
		Button b;
		switch (id) {
		case 0:
			b = (Button) findViewById(R.id.getTemp);
			return b;
		case 1:
			b = (Button) findViewById(R.id.fanOn);
			return b;
		case 2:
			b = (Button) findViewById(R.id.fanOff);
			return b;
		case 3:
			b = (Button) findViewById(R.id.lightOn);
			;
			return b;
		case 4:
			b = (Button) findViewById(R.id.lightOff);
			return b;
		case 5:
			b = (Button) findViewById(R.id.fanStatus);
			return b;
		case 6:
			b = (Button) findViewById(R.id.lightStatus);
			return b;
		default:
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sm, menu);
		return true;
	}

	private void sendSMS(int msgID) {

		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS Sent.",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic failure",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No service",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(), "Null PDU",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS delivered.",
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getBaseContext(), "SMS not delivered.",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(dstPhoneNumber, null, msg[msgID], sentPI,
				deliveredPI);
	}

}
