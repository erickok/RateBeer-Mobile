/*
 * Copyright 2010, Jesper Fussing Mørk
 *
 * This file is part of Ratebeer Mobile for Android.
 *
 * Ratebeer Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Ratebeer Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Ratebeer Mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.moerks.ratebeermobile;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import dk.moerks.ratebeermobile.activity.BetterRBDefaultActivity;
import dk.moerks.ratebeermobile.task.RetrieveUserIdTask;
import dk.moerks.ratebeermobile.task.SendBeermailTask;

public class MailAction extends BetterRBDefaultActivity {
	private static final String LOGTAG = "MailAction";

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mailaction);
		
		new RetrieveUserIdTask(this).execute();

		final boolean replyMode;
		final String messageId;
		final String from;
		final String senderId;
		final String subject;
		final String message;
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
        	replyMode = extras.getBoolean("ISREPLY");
        	if(replyMode){
	        	messageId = extras.getString("MESSAGEID");
	        	from = extras.getString("SENDER");
	        	senderId = extras.getString("SENDERID");
	        	subject = extras.getString("SUBJECT");
	        	message = extras.getString("MESSAGE");
	        	extras.putString("CURRENT_USER_ID", null);
	        	
	        	EditText fromText = (EditText) findViewById(R.id.mail_action_to);
	        	fromText.setEnabled(false);
	            EditText subjectText = (EditText) findViewById(R.id.mail_action_subject);
	            EditText messageText = (EditText) findViewById(R.id.mail_action_message);
	            
	            fromText.setText(from);
	            subjectText.setText(subject);
	            messageText.setText("\n\n......................................................\n" + message);
        	} else {
        		messageId = null;
        		from = null;
        		senderId = null;
        		subject = null;
        		message = null;
        	}
        } else {
        	replyMode = false;
    		messageId = null;
    		from = null;
    		senderId = null;
    		subject = null;
    		message = null;
        }
        
        Button sendMailButton = (Button) findViewById(R.id.sendMailButton);
        sendMailButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {

	        	EditText fromText = (EditText) findViewById(R.id.mail_action_to);
	            EditText subjectText = (EditText) findViewById(R.id.mail_action_subject);
	            EditText messageText = (EditText) findViewById(R.id.mail_action_message);
	            
        		// Prepare the 'send' action
    			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        		if(replyMode){  
        			parameters.add(new BasicNameValuePair("UserID", senderId));
        			parameters.add(new BasicNameValuePair("MessID", messageId));
        			parameters.add(new BasicNameValuePair("Referrer", "http://ratebeer.com/showmessage/" + messageId + "/"));
        			parameters.add(new BasicNameValuePair("text2", from));
        			parameters.add(new BasicNameValuePair("Subject", subject));
        			parameters.add(new BasicNameValuePair("Body", messageText.getText().toString()));
        			parameters.add(new BasicNameValuePair("nAllowEmail", "0"));
        			parameters.add(new BasicNameValuePair("nCc", "0"));
        			parameters.add(new BasicNameValuePair("nCcEmail", "android@moerks.dk"));
        			parameters.add(new BasicNameValuePair("nCcEmail2", "android@moerks.dk"));
            	} else {
            		Log.d(LOGTAG, "USERID: " + getUserId());
        	        parameters.add(new BasicNameValuePair("nSource", getUserId())); //MY User Id
        			parameters.add(new BasicNameValuePair("Referrer", "http://ratebeer.com/inbox"));
        			parameters.add(new BasicNameValuePair("UserID", "0"));
        			parameters.add(new BasicNameValuePair("RecipientName", fromText.getText().toString()));
        			parameters.add(new BasicNameValuePair("Subject", subjectText.getText().toString()));
        			parameters.add(new BasicNameValuePair("Body", messageText.getText().toString()));
        			parameters.add(new BasicNameValuePair("nAllowEmail", "1"));
        			parameters.add(new BasicNameValuePair("nCc", "0"));
        			parameters.add(new BasicNameValuePair("nCcEmail", "android@moerks.dk"));
        			parameters.add(new BasicNameValuePair("nCcEmail2", "android@moerks.dk"));
            	}
        		
        		new SendBeermailTask(MailAction.this, replyMode).execute(parameters.toArray(new NameValuePair[] {}));
        	}
        });
	}
	
	public void onBeermailSend() {
		Toast.makeText(getApplicationContext(), getText(R.string.toast_mail_sent), Toast.LENGTH_LONG).show();
	}
	
}
