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

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import dk.moerks.ratebeermobile.activity.BetterRBListActivity;
import dk.moerks.ratebeermobile.adapters.MessageAdapter;
import dk.moerks.ratebeermobile.task.RetrieveBeermailsTask;
import dk.moerks.ratebeermobile.vo.MessageHeader;

public class BeerMail extends BetterRBListActivity {
	//private static final String LOGTAG = "BeerMail";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beermail);

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

        // Retrieve BeerMails
        new RetrieveBeermailsTask(this).execute();
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
    		new RetrieveBeermailsTask(this).execute();
    	}
    }
    
    public void onBeermailsRetrieved(List<MessageHeader> results){
    	if (results != null) {
    		setListAdapter(new MessageAdapter(this, results));
			if (results.size() <= 0) {
	    		// If no mails were found, show this in a text message
				((TextView)findViewById(android.R.id.empty)).setText(R.string.beermail_nomail);
	    	}
    	}
    }
    
}
