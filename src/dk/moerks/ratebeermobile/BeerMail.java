package dk.moerks.ratebeermobile;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import dk.moerks.ratebeermobile.activity.RBActivity;
import dk.moerks.ratebeermobile.adapters.MessageAdapter;
import dk.moerks.ratebeermobile.exceptions.LoginException;
import dk.moerks.ratebeermobile.exceptions.NetworkException;
import dk.moerks.ratebeermobile.exceptions.RBParserException;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;
import dk.moerks.ratebeermobile.vo.MessageHeader;

public class BeerMail extends RBActivity {
	private static final String LOGTAG = "BeerMail";
	private List<MessageHeader> results = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beermail);
        
    	indeterminateStart("Refreshing Beermails...");
    	getBeerMails();
    	
        Button composeMailButton = (Button) findViewById(R.id.newMailButton);
        composeMailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Activity composeActivity = new MailAction();
            	Intent mailIntent = new Intent(BeerMail.this, MailAction.class);
            	mailIntent.putExtra("ISREPLY", false);
            	composeActivity.setIntent(mailIntent);
            	startActivityForResult(mailIntent, 100);
            }
        });

	}
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	
    	MessageHeader item = (MessageHeader) getListView().getItemAtPosition(position);
    	Intent viewIntent = new Intent(BeerMail.this, MailView.class);
    	viewIntent.putExtra("MESSAGEID", item.getMessageId());
    	viewIntent.putExtra("SENDERID", item.getSenderId());
    	viewIntent.putExtra("SENDER", item.getSender());
    	viewIntent.putExtra("SUBJECT", item.getSubject());
    	startActivityForResult(viewIntent, RESULT_OK);
    }
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	if(resultCode == RESULT_OK){
        	indeterminateStart("Refreshing Beermails...");
        	getBeerMails();
    	}
    }
    
    protected void update(){
		if(results != null){
			refreshList(BeerMail.this, results);
		}
		indeterminateStop();
    }
    
    private void refreshList(Activity context, List<MessageHeader> results){
    	MessageAdapter adapter = new MessageAdapter(context, results);
    	setListAdapter(adapter);
    }
    
    private void getBeerMails(){
    	Thread beermailThread = new Thread(){
    		public void run(){
    			try {
    				String responseString = NetBroker.doRBGet(getApplicationContext(), "http://ratebeer.com/user/messages/");
    			
    				results = RBParser.parseBeermail(responseString);
    				threadHandler.post(update);
				} catch(RBParserException e){
				} catch(NetworkException e){
				} catch(LoginException e){
				}
       		}
    	};
    	beermailThread.start();
    }
}
