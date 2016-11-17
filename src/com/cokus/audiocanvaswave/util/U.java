package com.cokus.audiocanvaswave.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.os.Environment;

public class U
{

	public static final String DATA_DIRECTORY = Environment.getExternalStorageDirectory() + "/record/";

	/**
	 * ������ͬ��Ŀ¼
	 */
	public static void createDirectory()
	{
		if(sdCardExists())
		{
			File file = new File(DATA_DIRECTORY);
			if( !file.exists())
			{
				file.mkdirs();
			}
		}
		else
		{
			File file = new File(DATA_DIRECTORY);
			if( !file.exists())
			{
				file.mkdirs();
			}
		}
	}

	/**
	 * �ж��ֻ��Ƿ���SD����
	 * 
	 * @return ��SD������true��û�з���false��
	 */
	public static boolean hasSDCard()
	{
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	/**
	 * ɾ�������ļ�
	 * 
	 * @param filePath
	 *            ��ɾ���ļ����ļ���
	 * @return �ļ�ɾ���ɹ�����true�����򷵻�false
	 */
	public static boolean deleteFile(String filePath )
	{
		File file = new File(filePath);
		if(file.isFile() && file.exists())
		{
			return file.delete();
		}
		return false;
	}

	/**
	 * ����ļ���
	 * 
	 * @param childtaskid
	 * @return �ļ�ɾ���ɹ�����true�����򷵻�false
	 */
	public static boolean deleteDirs(String childtaskid )
	{
		if(sdCardExists())
		{
			File file = new File(DATA_DIRECTORY + "/" + childtaskid);
			if(file.exists())
			{
				try
				{
					clearCache(file);
				}
				catch(Exception e)
				{
				}
			}
		}
		return false;
	}

	/**
	 * ����ļ���
	 * 
	 * @return �ļ�ɾ���ɹ�����true�����򷵻�false
	 */
	public static boolean deleteDirs()
	{
		if(sdCardExists())
		{
			File file = new File(DATA_DIRECTORY);
			if(file.exists())
			{
				try
				{
					clearCache(file);
				}
				catch(Exception e)
				{
				}
			}
		}
		return false;
	}

	/**
	 * @Description ����ļ���
	 * @author DataTang
	 */
	private static void clearCache(File file ) throws Exception
	{
		File [] files = file.listFiles();
		for(int i = 0 ; i < files.length ; i ++ )
		{
			if(files[i].isDirectory())
				clearCache(files[i]);
			else
			{
				files[i].delete();
			}
		}
		file.delete();
	}

	/**
	 * ������ͬ��Ŀ¼ //
	 */
	public static void createDirectory(String childtaskid )
	{
		if(sdCardExists())
		{
			File file = new File(DATA_DIRECTORY + "/" + childtaskid);
			if( !file.exists())
			{
				file.mkdirs();
			}
		}
	}

	public static boolean sdCardExists()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	@SuppressLint("SimpleDateFormat")
	public static String millis2CalendarString(long millis , String format )
	{
		if(millis > 0)
		{
			SimpleDateFormat yFormat = new SimpleDateFormat(format);
			Calendar yCalendar = Calendar.getInstance();
			yCalendar.setTimeInMillis(millis);

			try
			{
				return yFormat.format(yCalendar.getTime());
			}
			catch(Exception e)
			{

			}
			finally
			{
				yCalendar = null;
				yFormat = null;
				format = null;
			}
		}

		return "";
	}

}
