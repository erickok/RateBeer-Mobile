package dk.moerks.ratebeermobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import dk.moerks.ratebeermobile.activity.RBActivity;
import dk.moerks.ratebeermobile.exceptions.LoginException;
import dk.moerks.ratebeermobile.exceptions.NetworkException;
import dk.moerks.ratebeermobile.exceptions.RBParserException;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;
import dk.moerks.ratebeermobile.vo.Message;

public class MailView extends RBActivity {
	private static final String LOGTAG = "MailView";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mailview);

        final String messageId;
        final String from;
        final String senderId;
        final String subject;
        Message message = null;
        
        indeterminateStart("Retrieving message...");
        
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

        if(messageId != null){
        	try {
            	String responseString = NetBroker.doRBGet(getApplicationContext(), "http://ratebeer.com/showmessage/"+messageId+"/");
        		message = RBParser.parseMessage(responseString);
        	} catch(RBParserException e){
        	} catch(NetworkException e){
        	} catch(LoginException e){
        	}
        	
	        TextView fromText = (TextView) findViewById(R.id.mail_reply_from);
	        TextView timeText = (TextView) findViewById(R.id.mail_reply_time);
	        TextView subjectText = (TextView) findViewById(R.id.mail_reply_subject);
	        TextView messageText = (TextView) findViewById(R.id.mail_reply_message);

	        fromText.setText(from);
	        timeText.setText(message.getTime());
	        subjectText.setText(subject);
	        messageText.setText(message.getMessage());
	        indeterminateStop();
        } else {
        	message = null;
        }

        Button replyMailButton = (Button) findViewById(R.id.replyMailButton);
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
                setProgressBarIndeterminateVisibility(true);
            	Thread deleteThread = new Thread(){
            		public void run(){
            			Looper.prepare();
            			try {
            				NetBroker.doRBGet(getApplicationContext(), "http://ratebeer.com/DeleteMessage.asp?MessageID=" + messageId);
            	
        					Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_mail_deleted), Toast.LENGTH_LONG);
        					toast.show();
        					setResult(RESULT_OK);
        					finish();
		    			} catch(NetworkException e){
		    			} catch(LoginException e){
		    			} 
		    			Looper.loop();
	   		        	threadHandler.post(update);
            		}
               	};
              	deleteThread.start();
            }
        });
	}
}
