/**
 * 
 */
package com.nms.dms;

import java.util.ArrayList;
import java.util.List;

import com.nms.adapter.InfoArrayAdapter;
import com.nms.adapter.TitleNavigationAdapter;
import com.nms.database.DatabaseHandler;
import com.nms.model.DMSInfo;
import com.nms.model.SpinnerNavItem;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Hung
 * 
 */
@SuppressLint("NewApi")
public class ActivityLog extends BaseActivity implements OnClickListener {
	
	ListView lv;
	DatabaseHandler				db								= new DatabaseHandler(this);
	List<DMSInfo>				lst								= null;
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_log, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_log);
		loadTabs();
		loadLog();
		
	}
	
	// Cấu hình tab
	public void loadTabs()
	{
		// Lấy Tabhost id ra trước (cái này của built - in android
		final TabHost tab = (TabHost) findViewById(android.R.id.tabhost);
		// gọi lệnh setup
		tab.setup();
		TabHost.TabSpec spec;
		// Tạo tab1
		spec = tab.newTabSpec("t1");
		spec.setContent(R.id.tab_home);
		spec.setIndicator("Thông tin");
		tab.addTab(spec);
		// Tạo tab2
		spec = tab.newTabSpec("t2");
		spec.setContent(R.id.tab_log);
		spec.setIndicator("Log");
		tab.addTab(spec);
		// Tạo tab3
		spec = tab.newTabSpec("t3");
		spec.setContent(R.id.tab_setting);
		spec.setIndicator("Cấu hình");
		tab.addTab(spec);

		tab.setCurrentTab(1);
		tab.setOnTabChangedListener(new TabHost.OnTabChangeListener()
		{
			public void onTabChanged(String arg0)
			{
				if (arg0.equals("t1"))
				{
					Intent intent = new Intent(ActivityLog.this, ActivityHome.class);
	                startActivity(intent);      
	                finish();
				}
				else if (arg0.equals("t2"))
				{
					loadLog();
					
				}
				else if (arg0.equals("t3"))
				{

				}
			}
		});
	}
	
	public void loadLog()
	{
		final CharSequence[] items = {"Gửi", "Xóa"};
		lv = (ListView) findViewById(R.id.listview);

		lst = db.getAllInfo();

		InfoArrayAdapter myArr = new InfoArrayAdapter(this, R.layout.list_row, lst);
		lv.setAdapter(myArr);
		
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3)
			{
				AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivityLog.this);
				
				builder1.setItems(items, new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int item)
					{
						if(item == 0)
						{
							db.updateInfo(lst.get(arg2));
							loadLog();
							Toast.makeText(getApplicationContext(), "Gửi thành công.", Toast.LENGTH_SHORT).show();
						}
						else if (item == 1)
						{
							db.deleteRecord(lst.get(arg2));
							loadLog();
							Toast.makeText(getApplicationContext(), "Xóa thành công.", Toast.LENGTH_SHORT).show();
						}
					}
				});
				builder1.show();
				return true;
			}
			
		});
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				final Dialog dialog = new Dialog(ActivityLog.this);
				dialog.setContentView(R.layout.dialog);
				dialog.setTitle("Thông tin log");

				TextView time = (TextView) dialog.findViewById(R.id.txtdialog_time);
				TextView dia_operator = (TextView) dialog.findViewById(R.id.txtdialog_operator);
				TextView dia_type = (TextView) dialog.findViewById(R.id.txtdialog_type);
				TextView dia_lac = (TextView) dialog.findViewById(R.id.txtdialog_lac);
				TextView dia_cid = (TextView) dialog.findViewById(R.id.txtdialog_cid);
				TextView dia_rnc = (TextView) dialog.findViewById(R.id.txtdialog_rnc);
				TextView dia_psc = (TextView) dialog.findViewById(R.id.txtdialog_psc);
				TextView dia_signal = (TextView) dialog.findViewById(R.id.txtdialog_signal);
				TextView dia_gps = (TextView) dialog.findViewById(R.id.txtdialog_gps);
				TextView dia_address = (TextView) dialog.findViewById(R.id.txtdialog_address);
				TextView dia_syncIssue = (TextView) dialog.findViewById(R.id.txtdialog_syncIssue);
				TextView dia_createOutlet = (TextView) dialog.findViewById(R.id.txtdialog_createOutlet);
				TextView dia_loginstatus = (TextView) dialog.findViewById(R.id.txtdialog_loginstatus);
				TextView dia_checkstock = (TextView) dialog.findViewById(R.id.txtdialog_checkstock);
				TextView dia_routeplan = (TextView) dialog.findViewById(R.id.txtdialog_routePlan);
				TextView dia_voiceIssue = (TextView) dialog.findViewById(R.id.txtdialog_voiceissue);
				TextView dia_smsIssue = (TextView) dialog.findViewById(R.id.txtdialog_smsissue);
				TextView dia_dataSpeedIssue = (TextView) dialog.findViewById(R.id.txtdialog_dataspeedissue);
				TextView dia_coverageIssue = (TextView) dialog.findViewById(R.id.txtdialog_coverageissue);
				TextView dia_lockHanset = (TextView) dialog.findViewById(R.id.txtdialog_lockhandsetissue);
				TextView dia_errorDevice = (TextView) dialog.findViewById(R.id.txtdialog_errordevice);
				

				time.setText(String.valueOf(lst.get(arg2).getCreateDate()));
				dia_operator.setText(String.valueOf(lst.get(arg2).getOperator()));
				dia_type.setText(String.valueOf(lst.get(arg2).getType()));
				dia_lac.setText(String.valueOf(lst.get(arg2).getLac()));
				dia_cid.setText(String.valueOf(lst.get(arg2).getCid()));
				dia_rnc.setText(String.valueOf(lst.get(arg2).getRnc()));
				dia_psc.setText(String.valueOf(lst.get(arg2).getPsc()));
				dia_signal.setText(String.valueOf(lst.get(arg2).getSignal()));
				dia_gps.setText(String.valueOf(lst.get(arg2).getLat() + "/" + lst.get(arg2).getLon()));
				dia_address.setText(String.valueOf(lst.get(arg2).getAddress()));
				Resources res = getResources();
				dia_syncIssue.setText(String.valueOf(res.getStringArray(R.array.syncIssue)[Integer.parseInt(lst.get(arg2).getSyncIssue())]));
				dia_createOutlet.setText(String.valueOf(res.getStringArray(R.array.outletIssue)[Integer
						.parseInt(lst.get(arg2).getCreateOutletIssue())]));
				dia_loginstatus.setText(String.valueOf(res.getStringArray(R.array.loginStatus)[Integer.parseInt(lst.get(arg2).getLoginStatus())]));
				dia_checkstock.setText(String.valueOf(res.getStringArray(R.array.checkStocks)[Integer.parseInt(lst.get(arg2).getCheckStocks())]));
				dia_routeplan.setText(String.valueOf(lst.get(arg2).getRoutePlanIssue()));
				dia_voiceIssue.setText(String.valueOf(res.getStringArray(R.array.voiceIssue)[Integer.parseInt(lst.get(arg2).getVoiceIssue())]));
				dia_smsIssue.setText(String.valueOf(res.getStringArray(R.array.smsIssue)[Integer.parseInt(lst.get(arg2).getSmsIssue())]));
				dia_dataSpeedIssue.setText(String.valueOf(res.getStringArray(R.array.dataSpeedIssue)[Integer.parseInt(lst.get(arg2)
						.getDataSpeedIssue())]));
				dia_coverageIssue.setText(String.valueOf(res.getStringArray(R.array.coverageIssue)[Integer.parseInt(lst.get(arg2).getCoverageIssue())]));
				dia_lockHanset.setText(String.valueOf(res.getStringArray(R.array.lockHandsetIssue)[Integer.parseInt(lst.get(arg2)
						.getLockHandsetIssue())]));
				dia_errorDevice.setText(String.valueOf(lst.get(arg2).getErrorDevice()));
				
				dialog.show();
				
				Button btn_close = (Button) dialog.findViewById(R.id.btn_dialog_close);
				btn_close.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						dialog.dismiss();
						
					}
				});
				
				Button btn_send = (Button) dialog.findViewById(R.id.btn_dialog_send);
				btn_send.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						//Gui di
						
					}
				});
			}

		});
	}

}
