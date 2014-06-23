package andy.study.dailyrecord.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import android.os.Environment;
import android.util.Log;
	
public class ConfigLoader {

	private static Properties properties = new Properties();
	
	public final static String TAG = "dailyrecord";
	
	/**
	 * 数据库名
	 */
	public static final String DATABASE_NAME = "dailyrecord.db";
	/**
	 * 程序的数据库版本
	 */
	public static final int DATABASE_VERSION = 1;
	
	/**
	 * 记账表
	 */
	public static final String RECORD_TABLE = "T_DAILY_RECORD";
	/**
	 * 每天的总计表
	 */
	public static final String SUM_TABLE = "T_DAILY_TOTAL";
	
	/**
	 * 用于构建操作数据库Map的tablename key
	 */
	public static final String TABLE_NAME_KEY = "tablename";
	
	/**
	 * 用于构建操作数据库Map的whereClause key
	 */
	public static final String WHERE_CLAUSE_KEY = "whereClause";
	
	/**
	 * 用于构建操作数据库Map的whereArgs key
	 */
	public static final String WHERE_ARGS_KEY = "whereArgs";
	
	/**
	 * 取得数据库表中的列的时候， 对应列名的KEY
	 */
	public static final String COLUMN_NAME_KEY = "name";
	
	/**
	 * 配置文件中，配置的列表项的KEY值
	 */
	public static final String DB_RECORD_COLUMNS = "db.record.columns";	
	/**
	 * 字段的中文解释
	 */
	public static final String DB_RECORD_EXPLAIN = "db.record.explain";
	
	/**配置文件中的分隔符*/
	public static final String SEPARATOR = ",";
	
	public static final int EDIT_TEXT_EMS = 6;
	
	/** 默认的下拉选择数组 */
	public static final List<Map<String, Object>> SPINNER_LIST = new ArrayList<Map<String, Object>>();
	
	/** 显示以填写项时， 分割类型，和值 */
	public static final String RECORD_SEPARATE = "-";
	static {
		try {
//			if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {//judge sd card is exists				
//				String path = Environment.getExternalStorageDirectory().getAbsolutePath(); //get root path of sd card
//				path = path + File.separator + "smoney.properties";	
//				
//				Log.i(TAG, "sdcard file path:" + path);
//				InputStream is = new BufferedInputStream(new FileInputStream(path));
//				InputStreamReader isr = new InputStreamReader(is, "gbk");
//				properties.load(isr);			
//			} else {
//				throw new Exception("no sd card exists");
//			}
		} catch (Exception e) {
			Log.i(TAG, "Read properties files filed :" +e.getMessage());
			e.printStackTrace();
		} finally {
			for (int i = 1;i <= 6; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("type_id", i);
				switch (i) {
				case 1:		
					map.put("type_name", "吃饭");
					break;
				case 2:
					map.put("type_name", "交通");
					break;
				case 3:
					map.put("type_name", "衣物");
					break;
				case 4:
					map.put("type_name", "住宿");
					break;
				case 5:
					map.put("type_name", "网购");
					break;
				case 6:
					map.put("type_name", "其他");
					break;
				default:
					map.put("type_name", "其他");
					break;
				}
				SPINNER_LIST.add(map);
			}
		}
	}
	
	public static String getString(String key) {
		String s = "";
		try {
			s = properties.getProperty(key, "");
		} catch (Exception e) {
			Log.i(TAG, "read key error[" + key + "] : " + e.toString());
			e.printStackTrace();
		}
		return s;
	}
	
	public static int getInt(String key) {
		int i = 0;
		try {
			i = Integer.parseInt(properties.getProperty(key, "0")); 
		} catch (Exception e) {
			Log.i(TAG, "read key error[" + key + "] : " + e.toString());
			e.printStackTrace();
		}
		return i;
	}
}
