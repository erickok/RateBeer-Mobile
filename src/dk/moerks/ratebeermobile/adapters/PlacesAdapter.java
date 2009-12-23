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
import dk.moerks.ratebeermobile.vo.PlacesInfo;

public class PlacesAdapter extends ArrayAdapter<PlacesInfo> {
	Activity context;
	List<PlacesInfo> places;

	public PlacesAdapter(Activity context, List<PlacesInfo> places) {
		super(context, R.layout.places_row, places);

		this.context = context;
		this.places = places;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		
		try {
			if (row == null) {
				LayoutInflater inflater = LayoutInflater.from(this.context);
				row = inflater.inflate(R.layout.places_row, null);
			}
			/*
			TextView subject = (TextView)row.findViewById(R.id.mail_row_subject);
			subject.setText(headers.get(position).getSubject());
			
			TextView status = (TextView)row.findViewById(R.id.mail_row_status);
			status.setText(headers.get(position).getStatus());

			TextView date = (TextView)row.findViewById(R.id.mail_row_date);
			date.setText(headers.get(position).getDate());

			TextView sender = (TextView)row.findViewById(R.id.mail_row_sender);
			sender.setText(headers.get(position).getSender());
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return row;
	}

}
