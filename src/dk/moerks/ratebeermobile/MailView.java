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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import dk.moerks.ratebeermobile.activity.BetterRBDefaultActivity;
import dk.moerks.ratebeermobile.task.DeleteBeermailTask;
import dk.moerks.ratebeermobile.task.RetrieveBeermailTask;
import dk.moerks.ratebeermobile.vo.Message;

public class MailView extends BetterRBDefaultActivity {
	//private static final String LOGTAG = "MailView";

    String messageId;
    String from;
    String senderId;
    String subject;
	private Button replyMailButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mailview);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
        	messageId = extras.getString("MESSAGEID");
        	from = extras.getString("SENDER");
        	senderId = extras.getString("SENDERID");
        	subject = extras.getString("SUBJECT");
        } else {
        	messageId = null;
        	from = null;
        	senderId = null;
        	subject = null;
        }
        // We can already show the sender and subject
        showBasedInfo();

        if(messageId != null){
        	// Retrieve the beermail message text
        	new RetrieveBeermailTask(MailView.this).execute(messageId);
        }

        replyMailButton = (Button) findViewById(R.id.replyMailButton);
        // Disable the reply button until we have all the neccesary details
        replyMailButton.setEnabled(false);
        replyMailButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
    	        TextView messageTextT = (TextView) findViewById(R.id.mail_reply_message);
            	Intent mailIntent = new Intent(MailView.this, MailAction.class);
            	mailIntent.putExtra("ISREPLY", true);
            	mailIntent.putExtra("MESSAGEID", messageId);
            	mailIntent.putExtra("SENDER", from);
            	mailIntent.putExtra("SENDERID", senderId);
            	mailIntent.putExtra("SUBJECT", subject);
            	mailIntent.putExtra("MESSAGE", messageTextT.getText());
            	startActivity(mailIntent);  
            }
        });
        
        Button deleteMailButton = (Button) findViewById(R.id.deleteMailButton);
        deleteMailButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		new DeleteBeermailTask(MailView.this).execute(messageId);
            }
        });
	}

	public void onBeermailRetrieved(Message result) {
		
		// Show the time and message text that we just retrieved from the server
        TextView timeText = (TextView) findViewById(R.id.mail_reply_time);
        TextView messageText = (TextView) findViewById(R.id.mail_reply_message);
        timeText.setText(result.getTime());
        messageText.setText(result.getMessage());
        
        // Enable the reply button now as well, since we have all needed info
        //replyMailButton.setEnabled(true);
	}

	public void showBasedInfo() {
		// Fill in the from and subject TextViews from the Intent extras
        TextView fromText = (TextView) findViewById(R.id.mail_reply_from);
        TextView subjectText = (TextView) findViewById(R.id.mail_reply_subject);
        fromText.setText(from);
        subjectText.setText(subject);
	}

	public void onBeermailDeleted(String result) {
		Toast.makeText(this, R.string.toast_mail_deleted, Toast.LENGTH_LONG).show();
	}
	
}