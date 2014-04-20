/**
 * 
 */
package com.nms.dms;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Text;

import com.nms.adapter.DMSQuery;
import com.nms.adapter.InfoArrayAdapter;
import com.nms.adapter.SessionManager;
import com.nms.adapter.TitleNavigationAdapter;
import com.nms.database.DatabaseHandler;
import com.nms.model.DMSInfo;
import com.nms.model.GPSTracker;
import com.nms.model.ResponseBase;
import com.nms.model.SpinnerNavItem;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Hung
 * 
 */
@SuppressLint({ "NewApi", "ShowToast" })
public class ActivityHome extends BaseActivity implements OnClickListener
{

	public final String			ACTION_DETECT_LOCATION			= "DetectLocation";
	public final String			ACTION_DETECT_SIGNAL			= "DetectSignal";
	private static final int	MIN_DISTANCE_CHANGE_FOR_UPDATES	= 5;												// 10
																													// meters
	private static final int	MIN_TIME_BW_UPDATES				= 1000 * 60 * 1;									// 1
																													// minute
	protected TelephonyManager	telephonyManager;
	protected LocationManager	locationManager;
	public int					signal;
	public double				latitude;
	public double				longitude;
	TextView					signal1							= null;
	TextView					gps								= null;
	TextView					operator						= null;
	TextView					type							= null;
	TextView					lac								= null;
	TextView					cID								= null;
	TextView					rnc								= null;
	TextView					psc								= null;
	TextView					handset							= null;
	TextView					mcc								= null;
	TextView					mnc								= null;
	Button						send							= null;
	Button						save							= null;
	Button						selectPic						= null;
	GPSTracker					gpsTracker;
	Spinner						spinSyncIssue;
	Spinner						spinOuletIssue;
	Spinner						spinLoginStatus;
	Spinner						spinCheckStocks;
	Spinner						spinVoiceIssue;
	Spinner						spinSmsIssue;
	Spinner						spinDataIssue;
	Spinner						spinCoverageIssue;
	Spinner						spinLockHandsetIssue;
	EditText					routePlanIssue					= null;
	EditText					errorDevice						= null;
	EditText					uploadFile						= null;
	ListView					lv;
	List<DMSInfo>				lst								= null;

	DatabaseHandler				db								= new DatabaseHandler(this);
	SessionManager				session;
	private static final int	SELECT_PICTURE					= 1;
	String						selectedImagePath;
	// ADDED
	String						filemanagerstring;
	String						imagePath;

	int							column_index;

	String						path							= "";
	ImageView					imgView;
	public static String		username						= "";
	public static String		sessionId						= "";
	public static String		tranId							= "";

	public final static String	URL								= "http://192.168.88.44:8181/DMSWS/services/DMSWS";

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	}

	private final PhoneStateListener	phoneListener	= new PhoneStateListener()
														{
															@Override
															public void onSignalStrengthsChanged(SignalStrength sStrength)
														{
															String ssignal = sStrength.toString();
															String[] parts = ssignal.split(" ");
															int dbm = 0;
															if (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE)
															{
																dbm = Integer.parseInt(parts[8]) * 2 - 113;
															}
															else
															{
																if (sStrength.getGsmSignalStrength() != 99)
																{
																	dbm = -113 + 2 * sStrength.getGsmSignalStrength();
																}
															}
															setSignal(dbm);
															signal1.setText(String.valueOf(dbm) + " dBm");
															super.onSignalStrengthsChanged(sStrength);
														}
														};

	@Override
	public void onClick(View v)
	{

	}

	@Override
	public void initView()
	{
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_main);
		session = new SessionManager(getApplicationContext());

		session.checkLogin();

		HashMap<String, String> user = session.getUserDetails();

		username = user.get(SessionManager.KEY_NAME);
		sessionId = user.get(SessionManager.SESSIONID);
		tranId = user.get(SessionManager.TRANSACTIONID);

		// load tab
		loadTabs();
		loadButton();
		initSpinner();
		operator = (TextView) findViewById(R.id.txt_operator);
		type = (TextView) findViewById(R.id.txt_type);
		lac = (TextView) findViewById(R.id.txt_lac);
		cID = (TextView) findViewById(R.id.txt_cid);
		rnc = (TextView) findViewById(R.id.txt_rnc);
		psc = (TextView) findViewById(R.id.txt_psc);
		handset = (TextView) findViewById(R.id.txt_handset);
		signal1 = (TextView) findViewById(R.id.txt_signal);
		gps = (TextView) findViewById(R.id.txt_gps);
		mcc = (TextView) findViewById(R.id.txt_mcc);
		mnc = (TextView) findViewById(R.id.txt_mnc);
		routePlanIssue = (EditText) findViewById(R.id.edt_routeplan);
		errorDevice = (EditText) findViewById(R.id.edt_errordevice);
		send = (Button) findViewById(R.id.btn_send);
		save = (Button) findViewById(R.id.btn_save);
		selectPic = (Button) findViewById(R.id.btn_upfile);
		uploadFile = (EditText) findViewById(R.id.edt_uploadfile);
		loadData();

		send.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				DMSInfo info = createObject();
				db.createData(info);
				sendTask task = new sendTask();
				task.execute(info);

			}
		});

		save.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				insertLog();
				Toast.makeText(getApplicationContext(), "Ghi log thành công.", Toast.LENGTH_SHORT).show();

			}
		});

		selectPic.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// select a file
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"),
						SELECT_PICTURE);

			}
		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{

		if (resultCode == RESULT_OK)
		{
			Uri uri = intent.getData();

			if (uri.getScheme().toString().compareTo("content") == 0)
			{
				Cursor cursor = getContentResolver().query(uri, null, null, null, null);
				if (cursor.moveToFirst())
				{
					int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					Uri filePathUri = Uri.parse(cursor.getString(column_index));
					String file_name = filePathUri.getLastPathSegment().toString();
					String file_path = filePathUri.getPath();
					uploadFile.setText(file_path);
				}
			}
		}

	}

	public static String encodeTobase64(Bitmap image)
	{
		Bitmap immagex = image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
		return imageEncoded;
	}

	public static Bitmap decodeBase64(String input)
	{
		byte[] decodedByte = Base64.decode(input, 0);
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

	public void loadLog()
	{
		final CharSequence[] items = { "Gửi", "Xóa" };
		lv = (ListView) findViewById(R.id.listview);

		lst = db.getAllInfo();

		InfoArrayAdapter myArr = new InfoArrayAdapter(this, R.layout.list_row, lst);
		lv.setAdapter(myArr);

		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3)
			{
				AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivityHome.this);
				builder1.setTitle(lst.get(arg2).getCreateDate());
				builder1.setItems(items, new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int item)
					{
						if (item == 0)
						{
							sendTaskFromLog task = new sendTaskFromLog();
							task.execute(lst.get(arg2));

							loadLog();
							// Toast.makeText(getApplicationContext(),
							// "Gửi thành công.", Toast.LENGTH_SHORT).show();
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
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3)
			{
				final Dialog dialog = new Dialog(ActivityHome.this);
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
				imgView = (ImageView) dialog.findViewById(R.id.img_View);
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
				Bitmap bm = decodeBase64(lst.get(arg2).getImage());

				imgView.setImageBitmap(bm);

				dialog.show();

				Button btn_close = (Button) dialog.findViewById(R.id.btn_dialog_close);
				btn_close.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						dialog.dismiss();
						loadLog();
					}
				});

				Button btn_send = (Button) dialog.findViewById(R.id.btn_dialog_send);
				if (lst.get(arg2).getStatus().equals("1"))
				{
					btn_send.setEnabled(false);
				}
				btn_send.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{

						sendTaskFromLog task = new sendTaskFromLog();
						task.execute(lst.get(arg2));
					}
				});
			}

		});
	}

	private class sendTask extends AsyncTask<DMSInfo, DMSInfo, ResponseBase>
	{

		protected void onPostExecute(ResponseBase resp)
		{
			if (resp.getResponseCode().equals("0"))
			{
				Toast.makeText(getApplicationContext(), "Gửi thông tin thành công", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Gửi thông tin không thành công, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
			}

		}

		@Override
		protected ResponseBase doInBackground(DMSInfo... params)
		{
			ResponseBase sendResp = new ResponseBase();
			try
			{
				db.createData(params[0]);
				sendResp = DMSQuery.survey(username, URL, 30000, params[0], sessionId);
				if (sendResp.getResponseCode().equals("0"))
				{
					db.updateInfo1(params[0]);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return sendResp;
		}

	}

	private class sendTaskFromLog extends AsyncTask<DMSInfo, DMSInfo, ResponseBase>
	{

		protected void onPostExecute(ResponseBase resp)
		{
			if (resp.getResponseCode().equals("0"))
			{
				Toast.makeText(getApplicationContext(), "Gửi thông tin thành công", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Gửi thông tin không thành công, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
			}

		}

		@Override
		protected ResponseBase doInBackground(DMSInfo... params)
		{
			ResponseBase sendResp = new ResponseBase();
			try
			{
				sendResp = DMSQuery.survey(username, URL, 30000, params[0], sessionId);
				if (sendResp.getResponseCode().equals("0"))
				{
					db.updateInfo(params[0]);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return sendResp;
		}

	}

	public void loadButton()
	{
		Button btnSoftIssue = (Button) findViewById(R.id.software_issue);
		Button btnNetworkIssue = (Button) findViewById(R.id.network_issue);
		Button btnHandsetIssue = (Button) findViewById(R.id.handset_issue);

		View panelProfile = findViewById(R.id.panelProfile);
		panelProfile.setVisibility(View.GONE);

		View panelSettings = findViewById(R.id.panelSettings);
		panelSettings.setVisibility(View.GONE);

		View panelPrivacy = findViewById(R.id.panelPrivacy);
		panelPrivacy.setVisibility(View.GONE);

		btnSoftIssue.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// DO STUFF
				View panelProfile = findViewById(R.id.panelProfile);
				panelProfile.setVisibility(View.VISIBLE);

				View panelSettings = findViewById(R.id.panelSettings);
				panelSettings.setVisibility(View.GONE);

				View panelPrivacy = findViewById(R.id.panelPrivacy);
				panelPrivacy.setVisibility(View.GONE);

			}
		});

		btnNetworkIssue.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// DO STUFF
				View panelProfile = findViewById(R.id.panelProfile);
				panelProfile.setVisibility(View.GONE);

				View panelSettings = findViewById(R.id.panelSettings);
				panelSettings.setVisibility(View.VISIBLE);

				View panelPrivacy = findViewById(R.id.panelPrivacy);
				panelPrivacy.setVisibility(View.GONE);

			}
		});

		btnHandsetIssue.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// DO STUFF
				View panelProfile = findViewById(R.id.panelProfile);
				panelProfile.setVisibility(View.GONE);

				View panelSettings = findViewById(R.id.panelSettings);
				panelSettings.setVisibility(View.GONE);

				View panelPrivacy = findViewById(R.id.panelPrivacy);
				panelPrivacy.setVisibility(View.VISIBLE);

			}
		});
	}

	public void loadData()
	{

		// load GSM
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();

		String operatorStr = telephonyManager.getNetworkOperatorName();
		if (operatorStr == null || operatorStr.length() == 0)
		{
			operatorStr = telephonyManager.getSimOperatorName();
		}
		if (operatorStr == null || operatorStr.length() == 0)
		{
			operatorStr = "UNKNOWN";
		}

		type.setText(getNetworkTypeName(telephonyManager));

		lac.setText(String.valueOf(cellLocation.getLac()));
		cID.setText(String.valueOf(cellLocation.getCid()));

		int _rnc = (cellLocation.getCid() >> 16) & 0xffff;
		rnc.setText(String.valueOf(_rnc));
		psc.setText(String.valueOf(cellLocation.getPsc()));
		mcc.setText(String.valueOf(telephonyManager.getNetworkOperator().substring(0, 3)));
		mnc.setText(String.valueOf(telephonyManager.getNetworkOperator().substring(3)));
		operator.setText(String.valueOf(telephonyManager.getNetworkOperator().substring(0, 3))
				+ String.valueOf(telephonyManager.getNetworkOperator().substring(3)) + "(" + operatorStr + ")");
		int events = PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
				| PhoneStateListener.LISTEN_DATA_ACTIVITY
				| PhoneStateListener.LISTEN_CELL_LOCATION
				| PhoneStateListener.LISTEN_CALL_STATE
				| PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR
				| PhoneStateListener.LISTEN_DATA_CONNECTION_STATE
				| PhoneStateListener.LISTEN_SERVICE_STATE;

		telephonyManager.listen(phoneListener, events);
		long curentTime = System.currentTimeMillis();

		while (signal == 0)
		{
			if (isTimeout(curentTime, 1000))
			{
				break;
			}
		}

		gpsTracker = new GPSTracker(ActivityHome.this);

		// check if GPS enabled
		if (gpsTracker.canGetLocation())
		{

			latitude = gpsTracker.getLatitude();
			longitude = gpsTracker.getLongitude();

		}
		else
		{
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gpsTracker.showSettingsAlert();
		}

		gps.setText(String.valueOf(latitude + "/" + longitude));

		String info = Build.MODEL + " - Android version: "
				+ Build.VERSION.RELEASE;

		handset.setText(info);

	}

	public void initSpinner()
	{
		spinSyncIssue = (Spinner) findViewById(R.id.spinner_syncissue);
		ArrayAdapter<CharSequence> syncIssueAdapter = ArrayAdapter.createFromResource(this, R.array.syncIssue, android.R.layout.simple_spinner_item);
		syncIssueAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spinSyncIssue.setAdapter(syncIssueAdapter);

		// Outlet Issue
		spinOuletIssue = (Spinner) findViewById(R.id.spinner_outletissue);
		ArrayAdapter<CharSequence> outletIssueAdapter = ArrayAdapter.createFromResource(this, R.array.outletIssue,
						android.R.layout.simple_spinner_item);
		outletIssueAdapter
				.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spinOuletIssue.setAdapter(outletIssueAdapter);

		//
		spinLoginStatus = (Spinner) findViewById(R.id.spinner_loginstatusissue);
		ArrayAdapter<CharSequence> loginStatusAdapter = ArrayAdapter.createFromResource(this, R.array.loginStatus,
						android.R.layout.simple_spinner_item);
		loginStatusAdapter
				.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spinLoginStatus.setAdapter(loginStatusAdapter);

		//
		spinCheckStocks = (Spinner) findViewById(R.id.spinner_checkstock);
		ArrayAdapter<CharSequence> checkStockAdapter = ArrayAdapter.createFromResource(this, R.array.checkStocks,
						android.R.layout.simple_spinner_item);
		checkStockAdapter
				.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spinCheckStocks.setAdapter(checkStockAdapter);

		//
		spinVoiceIssue = (Spinner) findViewById(R.id.spinner_voiceissue);
		ArrayAdapter<CharSequence> voiceIssueAdapter = ArrayAdapter.createFromResource(this, R.array.voiceIssue,
						android.R.layout.simple_spinner_item);
		voiceIssueAdapter
				.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spinVoiceIssue.setAdapter(voiceIssueAdapter);

		//
		spinSmsIssue = (Spinner) findViewById(R.id.spinner_smsissue);
		ArrayAdapter<CharSequence> voiceSmsAdapter = ArrayAdapter.createFromResource(this, R.array.smsIssue,
						android.R.layout.simple_spinner_item);
		voiceSmsAdapter
				.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spinSmsIssue.setAdapter(voiceSmsAdapter);

		//
		spinDataIssue = (Spinner) findViewById(R.id.spinner_dataissue);
		ArrayAdapter<CharSequence> voiceDataAdapter = ArrayAdapter.createFromResource(this, R.array.dataSpeedIssue,
						android.R.layout.simple_spinner_item);
		voiceDataAdapter
				.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spinDataIssue.setAdapter(voiceDataAdapter);

		//
		spinCoverageIssue = (Spinner) findViewById(R.id.spinner_coverageissue);
		ArrayAdapter<CharSequence> voiceCoverageAdapter = ArrayAdapter.createFromResource(this, R.array.coverageIssue,
						android.R.layout.simple_spinner_item);
		voiceCoverageAdapter
				.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spinCoverageIssue.setAdapter(voiceCoverageAdapter);

		//
		spinLockHandsetIssue = (Spinner) findViewById(R.id.spinner_lockhandsetissue);
		ArrayAdapter<CharSequence> voiceLockHandsetAdapter = ArrayAdapter.createFromResource(this, R.array.lockHandsetIssue,
						android.R.layout.simple_spinner_item);
		voiceLockHandsetAdapter
				.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spinLockHandsetIssue.setAdapter(voiceLockHandsetAdapter);

		// spin.setOnItemSelectedListener(new )
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

		tab.setCurrentTab(0);
		tab.setOnTabChangedListener(new TabHost.OnTabChangeListener()
		{
			public void onTabChanged(String arg0)
			{
				if (arg0.equals("t1"))
				{
					initView();
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

	private String getNetworkTypeName(TelephonyManager telephonyManager)
	{
		int networkType = telephonyManager.getNetworkType();
		switch (networkType)
		{
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return "1xRTT";
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return "CDMA";
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return "EDGE";
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return "eHRPD";
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return "EVDO rev. 0";
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return "EVDO rev. A";
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return "EVDO rev. B";
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return "GPRS";
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return "HSDPA";
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return "HSPA";
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return "HSPA+";
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return "HSUPA";
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return "iDen";
		case TelephonyManager.NETWORK_TYPE_LTE:
			return "LTE";
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return "UMTS";
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return "Unknown";
		}
		throw new RuntimeException("New type of network");
	}

	public int getSignal()
	{
		return signal;
	}

	public void setSignal(int signal)
	{
		this.signal = signal;
	}

	private boolean isTimeout(long currentTime, long timeout)
	{
		long nextTime = System.currentTimeMillis();
		if ((nextTime - currentTime) > timeout)
		{
			return true;
		}
		return false;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.action_refresh:
			initView();
			return true;
		case R.id.action_logout:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Thoát");// Set tiêu đề
			builder.setMessage("Bạn muốn thoát ứng dụng?");// Set nội dung cho
															// Dialog
			builder.setCancelable(false);

			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// session.logoutUser();
					finish();
					System.exit(0);
				}
			});

			builder.setNegativeButton("No", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.cancel();

				}
			});
			builder.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void insertLog()
	{
		DMSInfo info = createObject();

		db.createData(info);
	}

	private static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight)
	{
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth)
		{

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public DMSInfo createObject()
	{
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		DMSInfo info = new DMSInfo();
		String strAdd = "";
		Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
		try
		{
			List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
			if (addresses != null)
			{
				Address returnedAddress = addresses.get(0);
				StringBuilder strReturnedAddress = new StringBuilder("");

				for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++)
				{
					strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
				}
				strAdd = strReturnedAddress.toString();

			}
			else
			{

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		info.setAddress(strAdd);
		info.setSignal(String.valueOf(signal1.getText()));
		info.setOperator(String.valueOf(operator.getText()));
		info.setType(String.valueOf(type.getText()));
		info.setLac(String.valueOf(lac.getText()));
		info.setCid(String.valueOf(cID.getText()));
		info.setRnc(String.valueOf(rnc.getText()));
		info.setPsc(String.valueOf(psc.getText()));
		info.setHandset(String.valueOf(handset.getText()));
		info.setMcc(String.valueOf(mcc.getText()));
		info.setMnc(String.valueOf(mnc.getText()));
		info.setLat(String.valueOf(latitude));
		info.setLon(String.valueOf(longitude));
		info.setSyncIssue(String.valueOf(spinSyncIssue.getSelectedItemPosition()));
		info.setCreateOutletIssue(String.valueOf(spinOuletIssue.getSelectedItemPosition()));
		info.setLoginStatus(String.valueOf(spinLoginStatus.getSelectedItemPosition()));
		info.setCheckStocks(String.valueOf(spinCheckStocks.getSelectedItemPosition()));
		info.setVoiceIssue(String.valueOf(spinVoiceIssue.getSelectedItemPosition()));
		info.setSmsIssue(String.valueOf(spinSmsIssue.getSelectedItemPosition()));
		info.setDataSpeedIssue(String.valueOf(spinDataIssue.getSelectedItemPosition()));
		info.setCoverageIssue(String.valueOf(spinCoverageIssue.getSelectedItemPosition()));
		info.setLockHandsetIssue(String.valueOf(spinLockHandsetIssue.getSelectedItemPosition()));
		info.setRoutePlanIssue(String.valueOf(routePlanIssue.getText()));
		info.setErrorDevice(String.valueOf(errorDevice.getText()));
		info.setStatus("0");
		String image = "no image";
		if (!uploadFile.getText().toString().equals(""))
		{
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(uploadFile.getText().toString(), options);

			options.inSampleSize = calculateInSampleSize(options, 380, 500);
			options.inJustDecodeBounds = false;
			Bitmap bm = BitmapFactory.decodeFile(uploadFile.getText().toString(), options);
			image = encodeTobase64(bm);

		}
		info.setImage(image);
		info.setCreateDate(df.format(Calendar.getInstance().getTime()));
		info.setTranId(tranId + sdf.format(Calendar.getInstance().getTime()));
		return info;
	}

}
