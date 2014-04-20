/**
 * 
 */
package com.nms.dms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.nms.database.DatabaseHandler;

import android.annotation.SuppressLint;

import android.app.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TabHost;
import android.widget.Toast;

/**
 * @author Hung
 * 
 */
@SuppressLint("NewApi")
public abstract class BaseActivity extends Activity
{
	public Gson	gson	= new Gson();

	public abstract void initView();

	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		initView();

	}

	public void writeFile(String str, String filename)
	{
		try
		{
			FileOutputStream outputStream = openFileOutput(filename,
					Context.MODE_PRIVATE);
			outputStream.write(str.getBytes());
			outputStream.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String readFile(String filename)
	{
		File file = new File(getBaseContext().getFilesDir(), filename);
		BufferedReader br = null;

		try
		{

			String sCurrentLine;

			br = new BufferedReader(new FileReader(file));
			String strLine = "";

			while ((sCurrentLine = br.readLine()) != null)
			{
				System.out.println(sCurrentLine);
				strLine += sCurrentLine;
			}

			return strLine;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (br != null)
					br.close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		return null;
	}

}
