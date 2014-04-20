/**
 * 
 */
package com.nms.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import com.nms.dms.R;
import com.nms.model.DMSInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.DropBoxManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author Hung
 * 
 */
public class InfoArrayAdapter extends ArrayAdapter<DMSInfo>
{
	Activity		context	= null;
	int				layoutId;

	List<DMSInfo>	lst		= null;

	public InfoArrayAdapter(Activity context, int layoutId, List<DMSInfo> lst)
	{
		super(context, layoutId, lst);
		this.context = context;
		this.layoutId = layoutId;
		this.lst = lst;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{

		LayoutInflater inflater = context.getLayoutInflater();
		convertView = inflater.inflate(layoutId, null);

		DMSInfo info = lst.get(position);
		TextView datetime = (TextView) convertView.findViewById(R.id.txt_datetimelog);
		TextView address = (TextView) convertView.findViewById(R.id.txt_addresslog);
		TextView gps = (TextView) convertView.findViewById(R.id.txt_gpslog);

		datetime.setText(info.getCreateDate());
		address.setText(info.getAddress());
		gps.setText("GPS (lat/long): " + info.getLat() + "/" + info.getLon());
		if (info.getStatus().equals("1"))
		{
			convertView.setBackgroundResource(R.drawable.selector_lstsend);
		}
		else if (info.getStatus().equals("0"))
		{
			datetime.setTypeface(null, Typeface.BOLD);
			address.setTypeface(null, Typeface.BOLD);
			gps.setTypeface(null, Typeface.BOLD);
		}
		return convertView;
	}
}
