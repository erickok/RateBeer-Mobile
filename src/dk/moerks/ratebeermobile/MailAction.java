package dk.moerks.ratebeermobile;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MailAction extends Activity {
	final Handler threadHandler = new Handler();
    // Create runnable for posting
    final Runnable clearIndeterminateProgress = new Runnable() {
        public void run() {
        	clearIndeterminateProgress();
        }
    };

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.mailaction);

		final boolean replyMode;
		final String messageId;
		final String from;
		final String senderId;
		final String subject;
		final String message;
		final String currentUserId;
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
        	replyMode = extras.getBoolean("ISREPLY");
        	if(replyMode){
	        	messageId = extras.getString("MESSAGEID");
	        	from = extras.getString("SENDER");
	        	senderId = extras.getString("SENDERID");
	        	subject = extras.getString("SUBJECT");
	        	message = extras.getString("MESSAGE");
	        	currentUserId = null;
	        	
	        	EditText fromText = (EditText) findViewById(R.id.mail_action_to);
	        	fromText.setEnabled(false);
	            EditText subjectText = (EditText) findViewById(R.id.mail_action_subject);
	            EditText messageText = (EditText) findViewById(R.id.mail_action_message);
	            
	            fromText.setText(from);
	            subjectText.setText(subject);
	            messageText.setText("\n\n......................................................\n" + message);
        	} else {
        		String responseString = NetBroker.doGet(getApplicationContext(), "http://ratebeer.com/user/messages/");
        		currentUserId = RBParser.parseUserId(responseString);
        		messageId = null;
        		from = null;
        		senderId = null;
        		subject = null;
        		message = null;
        	}
        } else {
        	currentUserId = null;
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
                setProgressBarIndeterminateVisibility(true);

            	Thread sendThread = new Thread(){
            		public void run(){
            			Looper.prepare();
			        	EditText fromText = (EditText) findViewById(R.id.mail_action_to);
			            EditText subjectText = (EditText) findViewById(R.id.mail_action_subject);
			            EditText messageText = (EditText) findViewById(R.id.mail_action_message);
		
			            String responseString = null;
			            if(replyMode){	
		        			List<NameValuePair> parameters = new ArrayList<NameValuePair>();  
		        			parameters.add(new BasicNameValuePair("UserID", senderId));
		        			parameters.add(new BasicNameValuePair("MessID", messageId));
		        			parameters.add(new BasicNameValuePair("Referrer", "http://ratebeer.com/showmessage/" + messageId + "/"));
		        			parameters.add(new BasicNameValuePair("text2", from));
		        			parameters.add(new BasicNameValuePair("Subject", subject));
		        			parameters.add(new BasicNameValuePair("Body", messageText.getText().toString()));
		        			parameters.add(new BasicNameValuePair("nAllowEmail", "0"));
		        			parameters.add(new BasicNameValuePair("nCc", ""));
		        			parameters.add(new BasicNameValuePair("nCcEmail", ""));
		        			parameters.add(new BasicNameValuePair("nCcEmail2", ""));
		        			responseString = NetBroker.doPost(getApplicationContext(), "http://ratebeer.com/SaveMessage.asp", parameters);
		            	} else {
		        			List<NameValuePair> parameters = new ArrayList<NameValuePair>();  
		        			parameters.add(new BasicNameValuePair("nSource", currentUserId)); //MY User Id
		        			parameters.add(new BasicNameValuePair("UserID", "0"));
		        			parameters.add(new BasicNameValuePair("Referrer", "http://ratebeer.com/user/messages/"));
		        			parameters.add(new BasicNameValuePair("RecipientName", fromText.getText().toString()));
		        			parameters.add(new BasicNameValuePair("Subject", subjectText.getText().toString()));
		        			parameters.add(new BasicNameValuePair("Body", messageText.getText().toString()));
		        			parameters.add(new BasicNameValuePair("nAllowEmail", "0"));
		        			parameters.add(new BasicNameValuePair("nCc", ""));
		        			parameters.add(new BasicNameValuePair("nCcEmail", ""));
		        			parameters.add(new BasicNameValuePair("nCcEmail2", ""));
		        			responseString = NetBroker.doPost(getApplicationContext(), "http://ratebeer.com/savemessage/", parameters);
		            	}
			            
		    			if(responseString != null){
		   					Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_mail_sent), Toast.LENGTH_LONG);
		   					toast.show();
		   					setResult(RESULT_OK);
		   					finish();
		   		        	threadHandler.post(clearIndeterminateProgress);
		    			} else {
		   					Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_mail_failed), Toast.LENGTH_LONG);
		   					toast.show();
		    			}
		    			Looper.loop();
            		}
            	};
                sendThread.start();
            }
        });

	}

	private void clearIndeterminateProgress() {
		setProgressBarIndeterminateVisibility(false);
	}

}
