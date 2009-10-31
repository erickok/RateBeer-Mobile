package dk.moerks.ratebeermobile;

import java.util.List;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import dk.moerks.ratebeermobile.adapters.MessageAdapter;
import dk.moerks.ratebeermobile.io.NetBroker;
import dk.moerks.ratebeermobile.util.RBParser;
import dk.moerks.ratebeermobile.vo.MessageHeader;
import dk.moerks.ratebeermobile.vo.SearchResult;

public class BeerMail extends ListActivity {
	private static final String LOGTAG = "BeerMail";
	private List<MessageHeader> results = null;
	private ProgressDialog beermailDialog = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		// Request progress bar
        setContentView(R.layout.beermail);
        
    	beermailDialog = ProgressDialog.show(BeerMail.this, getText(R.string.beermail_retrieving), getText(R.string.beermail_retrieving_text));
    	
    	beermailDialog.setOnDismissListener(new ProgressDialog.OnDismissListener(){
    		public void onDismiss(DialogInterface dialog){
    			if(results != null){
    				refreshList(BeerMail.this, results);
    			} else {
    				Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.toast_network_error), Toast.LENGTH_LONG);
   					toast.show();
    			}
    		}
    	});
    	
    	Thread beermailThread = new Thread(){
    		public void run(){
    			String responseString = NetBroker.doGet(getApplicationContext(), "http://ratebeer.com/user/messages/");
    			
    			if(responseString != null){
    				results = RBParser.parseBeermail(responseString);
    			} else {
    				results = null;
    			}
    			
    			beermailDialog.dismiss();
       		}
    	};
    	beermailThread.start();
    	
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
        	beermailDialog = ProgressDialog.show(BeerMail.this, getText(R.string.beermail_retrieving), getText(R.string.beermail_retrieving_text));    		
        	Thread beermailThread = new Thread(){
        		public void run(){
        			String responseString = NetBroker.doGet(getApplicationContext(), "http://ratebeer.com/user/messages/");
        			
        			if(responseString != null){
        				results = RBParser.parseBeermail(responseString);
        			} else {
        				results = null;
        			}
        			
        			beermailDialog.dismiss();
           		}
        	};
        	beermailThread.start();
    	}
    }
    
    private void refreshList(Activity context, List<MessageHeader> results){
    	MessageAdapter adapter = new MessageAdapter(context, results);
    	setListAdapter(adapter);
    }
}
