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
package dk.moerks.ratebeermobile.adapters;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import dk.moerks.ratebeermobile.R;
import dk.moerks.ratebeermobile.vo.MessageHeader;

public class MessageAdapter extends ArrayAdapter<MessageHeader> {
	Activity context;
	List<MessageHeader> headers;

	public MessageAdapter(Activity context, List<MessageHeader> headers) {
		super(context, R.layout.message_row, headers);

		this.context = context;
		this.headers = headers;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		
		try {
			if (row == null) {
				LayoutInflater inflater = LayoutInflater.from(this.context);
				row = inflater.inflate(R.layout.message_row, null);
			}
			
			TextView subject = (TextView)row.findViewById(R.id.mail_row_subject);
			subject.setText(headers.get(position).getSubject());
			
			TextView status = (TextView)row.findViewById(R.id.mail_row_status);
			status.setText(headers.get(position).getStatus());

			TextView date = (TextView)row.findViewById(R.id.mail_row_date);
			date.setText(headers.get(position).getDate());

			TextView sender = (TextView)row.findViewById(R.id.mail_row_sender);
			sender.setText(headers.get(position).getSender());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return row;
	}
}
