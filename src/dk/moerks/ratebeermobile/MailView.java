package dk.moerks.ratebeermobile;

import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;
import dk.moerks.ratebeermobile.vo.Message;
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

public class MailView extends Activity {
	private static final String LOGTAG = "ReplyMail";
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
        final String messageId;
        final String from;
        final String senderId;
        final String subject;
        final String messageString;
        final Message message;
        
		// Request progress bar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.mailview);
        setProgressBarIndeterminateVisibility(true);
        
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
        	String responseString = NetBroker.doGet(getApplicationContext(), "http://ratebeer.com/showmessage/"+messageId+"/");
        	message = RBParser.parseMessage(responseString);
        	
	        TextView fromText = (TextView) findViewById(R.id.mail_reply_from);
	        TextView timeText = (TextView) findViewById(R.id.mail_reply_time);
	        TextView subjectText = (TextView) findViewById(R.id.mail_reply_subject);
	        TextView messageText = (TextView) findViewById(R.id.mail_reply_message);

	        fromText.setText(from);
	        timeText.setText(message.getTime());
	        subjectText.setText(subject);
	        messageText.setText(message.getMessage());
	        setProgressBarIndeterminateVisibility(false);
        } else {
        	message = null;
        }

        Button replyMailButton = (Button) findViewById(R.id.replyMailButton);
        replyMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
        	public void onClick(View v) {
            	Intent mailIntent = new Intent(MailView.this, MailAction.class);
            	mailIntent.putExtra("ISREPLY", true);
            	mailIntent.putExtra("MESSAGEID", messageId);
            	mailIntent.putExtra("SENDER", from);
            	mailIntent.putExtra("SENDERID", senderId);
            	mailIntent.putExtra("SUBJECT", subject);
            	mailIntent.putExtra("MESSAGE", message.getMessage());
            	startActivity(mailIntent);  
            }
        });
        
        Button deleteMailButton = (Button) findViewById(R.id.deleteMailButton);
        deleteMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
        	public void onClick(View v) {
                setProgressBarIndeterminateVisibility(true);
            	Thread deleteThread = new Thread(){
            		public void run(){
            			Looper.prepare();
            			String responseString = NetBroker.doGet(getApplicationContext(), "http://ratebeer.com/DeleteMessage.asp?MessageID=" + messageId);
            	
		    			if(responseString != null){
		   					Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_mail_deleted), Toast.LENGTH_LONG);
		   					toast.show();
			    			setResult(RESULT_OK);
			    			finish();
		    			} else {
		   					Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_mail_deleted_failed), Toast.LENGTH_LONG);
		   					toast.show();
		    			}
		    			Looper.loop();
	   		        	threadHandler.post(clearIndeterminateProgress);
            		}
               	};
              	deleteThread.start();
            }
        });
	}

	private void clearIndeterminateProgress() {
		setProgressBarIndeterminateVisibility(false);
	}
}
