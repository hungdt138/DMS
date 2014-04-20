/**
 * 
 */
package com.nms.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.nms.model.DMSInfo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Hung
 * 
 */
public class DatabaseHandler extends SQLiteOpenHelper
{

	private static final int	DATABASE_VERSION	= 1;

	private static final String	DATABASE_NAME		= "DMS";

	private static final String	TABLE_LOG			= "DMS_LOG";

	public DatabaseHandler(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS "
					+ TABLE_LOG
					+ "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
							"mcc,mnc,operator,type,lac,cid," +
							"rnc,psc,signal,lon,lat," +
							"address,status,sV,sM,sS," +
							"sNW,handset,syncIssue,createOutletIssue,loginStatus,checkStocks,routePlanIssue," +
							"lockHandsetIssue,errorDevice,created_date,image, tranid)";
		db.execSQL(CREATE_CONTACTS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOG);
		onCreate(db);

	}

	@SuppressLint("SimpleDateFormat")
	public void createData(DMSInfo info)
	{

		SQLiteDatabase db = this.getReadableDatabase();

		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

		Calendar cal = Calendar.getInstance();

		ContentValues cv = new ContentValues();
		cv.put("mcc", info.getMcc());
		cv.put("mnc", info.getMnc());
		cv.put("operator", info.getOperator());
		cv.put("type", info.getType());
		cv.put("lac", info.getLac());
		cv.put("cid", info.getCid());
		cv.put("rnc", info.getRnc());
		cv.put("psc", info.getPsc());
		cv.put("signal", info.getSignal());
		cv.put("lon", info.getLon());
		cv.put("lat", info.getLat());
		cv.put("address", info.getAddress());
		cv.put("status", info.getStatus());
		cv.put("sV", info.getVoiceIssue());
		cv.put("sM", info.getSmsIssue());
		cv.put("sS", info.getDataSpeedIssue());
		cv.put("sNW", info.getCoverageIssue());
		cv.put("handset", info.getHandset());
		cv.put("syncIssue", info.getSyncIssue());
		cv.put("createOutletIssue", info.getCreateOutletIssue());
		cv.put("loginStatus", info.getLoginStatus());
		cv.put("checkStocks", info.getCheckStocks());
		cv.put("routePlanIssue", info.getRoutePlanIssue());
		cv.put("lockHandsetIssue", info.getLockHandsetIssue());
		cv.put("errorDevice", info.getErrorDevice());
		cv.put("created_date", df.format(cal.getTime()));
		cv.put("image", info.getImage());
		cv.put("tranid", info.getTranId());
		db.insert(TABLE_LOG, null, cv);
		db.close();

	}

	public DMSInfo getInfoById(int id)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		DMSInfo info = new DMSInfo();

		Cursor cursor = db.query(TABLE_LOG, new String[] { "id",
				"mcc", "mnc", "operator", "type", "lac", "cid",
				"rnc", "psc", "signal", "lon", "lat",
				"address", "status", "sV", "sM", "sS",
				"sNW", "handset", "syncIssue", "createOutletIssue", "loginStatus", "checkStocks", "routePlanIssue",
				"lockHandsetIssue", "errorDevice", "created_date", "image", "tranid" }, "id=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		info.setId(id);
		info.setMcc(cursor.getString(1));
		info.setMnc(cursor.getString(2));
		info.setOperator(cursor.getString(3));
		info.setType(cursor.getString(4));
		info.setLac(cursor.getString(5));
		info.setCid(cursor.getString(6));
		info.setRnc(cursor.getString(7));
		info.setPsc(cursor.getString(8));
		info.setSignal(cursor.getString(9));
		info.setLon(cursor.getString(10));
		info.setLat(cursor.getString(11));
		info.setAddress(cursor.getString(12));
		info.setStatus(cursor.getString(13));
		info.setVoiceIssue(cursor.getString(14));
		info.setSmsIssue(cursor.getString(15));
		info.setDataSpeedIssue(cursor.getString(16));
		info.setCoverageIssue(cursor.getString(17));
		info.setHandset(cursor.getString(18));
		info.setSyncIssue(cursor.getString(19));
		info.setCreateOutletIssue(cursor.getString(20));
		info.setLoginStatus(cursor.getString(21));
		info.setCheckStocks(cursor.getString(22));
		info.setRoutePlanIssue(cursor.getString(23));
		info.setLockHandsetIssue(cursor.getString(24));
		info.setErrorDevice(cursor.getString(25));
		info.setCreateDate(cursor.getString(26));
		info.setImage(cursor.getString(27));
		info.setTranId(cursor.getString(28));
		return info;

	}

	public List<DMSInfo> getAllInfo()
	{
		List<DMSInfo> lst = new ArrayList<DMSInfo>();
		String sql = "select * from " + TABLE_LOG + " order by id desc";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		DMSInfo info = null;
		if (cursor.moveToFirst())
		{
			do
			{
				info = new DMSInfo();
				info.setId(Integer.parseInt(cursor.getString(0)));
				info.setMcc(cursor.getString(1));
				info.setMnc(cursor.getString(2));
				info.setOperator(cursor.getString(3));
				info.setType(cursor.getString(4));
				info.setLac(cursor.getString(5));
				info.setCid(cursor.getString(6));
				info.setRnc(cursor.getString(7));
				info.setPsc(cursor.getString(8));
				info.setSignal(cursor.getString(9));
				info.setLon(cursor.getString(10));
				info.setLat(cursor.getString(11));
				info.setAddress(cursor.getString(12));
				info.setStatus(cursor.getString(13));
				info.setVoiceIssue(cursor.getString(14));
				info.setSmsIssue(cursor.getString(15));
				info.setDataSpeedIssue(cursor.getString(16));
				info.setCoverageIssue(cursor.getString(17));
				info.setHandset(cursor.getString(18));
				info.setSyncIssue(cursor.getString(19));
				info.setCreateOutletIssue(cursor.getString(20));
				info.setLoginStatus(cursor.getString(21));
				info.setCheckStocks(cursor.getString(22));
				info.setRoutePlanIssue(cursor.getString(23));
				info.setLockHandsetIssue(cursor.getString(24));
				info.setErrorDevice(cursor.getString(25));
				info.setCreateDate(cursor.getString(26));
				info.setImage(cursor.getString(27));
				info.setTranId(cursor.getString(28));
				lst.add(info);

			}
			while (cursor.moveToNext());
		}

		return lst;
	}

	public void deleteAllRecord()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_LOG, null, null);
		db.close();
	}
	
	public void deleteRecord(DMSInfo info)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		db.delete(TABLE_LOG, "id = ?",
				new String[] { String.valueOf(info.getId()) });
		db.close();
	}
	
	public void updateInfo(DMSInfo info)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		 
		ContentValues values = new ContentValues();
		values.put("status", 1);
 
		db.update(TABLE_LOG, values, "id = ?",
				new String[] { String.valueOf(info.getId()) });
	}
	
	public void updateInfo1(DMSInfo info)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		 
		ContentValues values = new ContentValues();
		values.put("status", 1);
 
		db.update(TABLE_LOG, values, "tranid = ?",
				new String[] { String.valueOf(info.getTranId()) });
	}

}
