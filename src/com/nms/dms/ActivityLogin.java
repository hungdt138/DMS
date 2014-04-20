/**
 * 
 */
package com.nms.dms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.nms.adapter.DMSQuery;
import com.nms.adapter.SessionManager;
import com.nms.model.ResponseBase;
import com.nms.model.SessionId;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.backup.RestoreObserver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Hung
 * 
 */
@SuppressLint("NewApi")
public class ActivityLogin extends BaseActivity implements OnClickListener
{

	EditText					username;
	EditText					password;
	Button						login;
	Button						forgot;
	CheckBox					checkbox;
	private ActionBar			actionBar;
	SessionManager				session;

	public final static String	URL	= "http://192.168.88.44:8181/DMSWS/services/DMSWS";

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		actionBar = getActionBar();
		actionBar.hide();
		// Session Manager
		session = new SessionManager(getApplicationContext());
	}

	String	USERNAME_DEMO	= "admin";
	String	PASSWORD_DEMO	= "admin";
	Socket	socket			= null;

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.login:
			login();
			break;

		case R.id.forgot:

			break;

		default:
			break;
		}

	}

	protected void login()
	{
		String u = username.getText().toString().trim();
		String p = password.getText().toString().trim();

		if (u.equals("") || p.equals(""))
			Toast.makeText(getBaseContext(), "Bạn cần nhập đầy đủ thông tin",
					Toast.LENGTH_LONG).show();
		else
		{
			loginTask task = new loginTask();
			task.execute(URL);
		}
	}

	private class loginTask extends AsyncTask<String, Void, ResponseBase>
	{

		@Override
		protected ResponseBase doInBackground(String... params)
		{

			ResponseBase loginResp = new ResponseBase();
			try
			{
				loginResp = DMSQuery.login(username.getText().toString().trim(), password.getText().toString().trim(),
						new SessionId().get(getApplicationContext()), URL, 30000);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return loginResp;
		}

		protected void onPostExecute(ResponseBase resp)
		{
			if (resp.equals("1"))
			{
				Toast.makeText(getApplicationContext(), "Tài khoản hoặc mật khẩu không đúng.", Toast.LENGTH_SHORT).show();
			}
			else
			{

				session.createLoginSession(username.getText().toString().trim(), resp.getSessionId(), resp.getTransactionId());

				startActivity(new Intent(ActivityLogin.this, ActivityHome.class));
			}
		}

	}

	@Override
	public void initView()
	{
		setContentView(R.layout.activity_login);
		checkbox = (CheckBox) findViewById(R.id.remember);
		username = (EditText) findViewById(R.id.edt_username);
		password = (EditText) findViewById(R.id.edt_password);

		findViewById(R.id.login).setOnClickListener(this);
		findViewById(R.id.forgot).setOnClickListener(this);

		restoreUIState();

	}

	private final String	ACCOUNT			= "ACCOUNT";
	private final String	PASSWORD		= "PASSWORD";
	private final String	SAVE_PASSWORD	= "SAVE_PASSWORD";

	public void saveUIState()
	{

		SharedPreferences saveUI = getPreferences(Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = saveUI.edit();

		editor.putString(ACCOUNT, username.getText().toString());
		editor.putString(PASSWORD, password.getText().toString());
		editor.putBoolean(SAVE_PASSWORD, checkbox.isChecked());
		editor.commit();
	}

	public void restoreUIState()
	{
		SharedPreferences savedUI = getPreferences(Activity.MODE_PRIVATE);
		boolean bool = savedUI.getBoolean(SAVE_PASSWORD, true);
		checkbox.setChecked(bool);
		if (bool)
		{
			String usr = savedUI.getString(ACCOUNT, "");
			username.setText(usr);
			String pwd = savedUI.getString(PASSWORD, "");
			password.setText(pwd);
		}
	}

}
