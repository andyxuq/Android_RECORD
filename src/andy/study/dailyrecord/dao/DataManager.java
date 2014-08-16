package andy.study.dailyrecord.dao;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import andy.study.dailyrecord.model.Record;
import andy.study.dailyrecord.util.ConfigLoader;

/**
 * 数据库操作类
 * @author Administrator
 *
 */
public class DataManager {

	private SqliteManager sqliteManager;
	
	private SQLiteDatabase db;
	
	public DataManager(Context context) {
		sqliteManager = new SqliteManager(context);
		db = sqliteManager.getWritableDatabase();
	}
	
	/**
	 * add data to db
	 * @param map
	 * @return
	 */
	public Object addRecord(Map<String, Object> map){
		ContentValues value = new ContentValues();
		for (String key : map.keySet()) {
			if (!key.equals(ConfigLoader.TABLE_NAME_KEY)) {
				value.put(key, map.get(key).toString());
			}
		}		
		return db.insert(map.get(ConfigLoader.TABLE_NAME_KEY).toString(), "", value);
	}
	
	public Object addRecordByBean(Record record) {		
		ContentValues value = new ContentValues();
		
		value.put("type_id", record.getType_id());
		value.put("type_name", record.getType_name());
		value.put("note", record.getNote());
		
		double recordValue = record.getRecord_value();
		if (String.valueOf(recordValue).contains(".")) {
			DecimalFormat format = new DecimalFormat("#.00");
			recordValue = Double.parseDouble(format.format(recordValue));
		}
		value.put("record_value", recordValue);
		value.put("recorddate", record.getRecorddate());
		
		return db.insert(ConfigLoader.RECORD_TABLE, "", value);
	}
	/**
	 * delete data
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Object deleteRecord(Map<String, Object> map) throws Exception{
		String tableName = map.get(ConfigLoader.TABLE_NAME_KEY)==null?"":map.get(ConfigLoader.TABLE_NAME_KEY).toString();
		if (tableName.equals("")) {
			throw new Exception ("can't find table name in parameter map");
		}
		
		String deleteCondition = map.get(ConfigLoader.WHERE_CLAUSE_KEY)==null?"":map.get(ConfigLoader.WHERE_CLAUSE_KEY).toString();
		String deleteArgs = map.get(ConfigLoader.WHERE_ARGS_KEY)==null?"":map.get(ConfigLoader.WHERE_ARGS_KEY).toString();
		if (!deleteCondition.equals("")) {	
			if (deleteArgs.equals("")) {
				throw new Exception("got delete condition, but can't find condition values");
			}
			return db.delete(tableName, deleteCondition, deleteArgs.split(","));			
		} else {
			return db.delete(tableName, null, null);
		}
	}
	
	/**
	 * update data
	 * @throws Exception 
	 */
	public Object updateRecord(Map<String, Object> condition, Map<String, Object> dataMap) throws Exception{
		String tableName = condition.get(ConfigLoader.TABLE_NAME_KEY)==null?"":condition.get(ConfigLoader.TABLE_NAME_KEY).toString();
		if (tableName.equals("")) {
			throw new Exception ("can't find table name in parameter map");
		}
		
		String updateCondition = condition.get(ConfigLoader.WHERE_CLAUSE_KEY)==null?"":condition.get(ConfigLoader.WHERE_CLAUSE_KEY).toString();
		String updateArgs = condition.get(ConfigLoader.WHERE_ARGS_KEY)==null?"":condition.get(ConfigLoader.WHERE_ARGS_KEY).toString();
		
		ContentValues value = new ContentValues();
		for (String key : dataMap.keySet()) {
			value.put(key, dataMap.get(key).toString());
		}
		
		if (!updateCondition.equals("")) {
			if (updateArgs.equals("")) {
				throw new Exception("got update condition, but can't find condition values");
			}
			return db.update(tableName, value, updateCondition, updateArgs.split(","));
		} else {
			return db.update(tableName, value, null, null);
		}
	}
	
	/**
	 * find data
	 * @param sql
	 * @param args
	 * @return
	 */
	public List<Map<String, Object>> findRecord(String sql, String[] args){
		Cursor rawQuery = db.rawQuery(sql, args);
		List<Map<String, Object>> recordList = new ArrayList<Map<String,Object>>();
		
		String[] columnNames = rawQuery.getColumnNames();
		while (rawQuery.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (String column : columnNames) {	
				String value = rawQuery.getString(rawQuery.getColumnIndex(column));
				map.put(column, value == null?"":value);				
			}
			recordList.add(map);
		}
		Log.i(ConfigLoader.TAG, "exec sql : " + sql);
		return recordList;
	}
	
	/**
	 * 查找结果数据（适用于查找单个数据记录）
	 * @param sql
	 * @param args
	 * @return Object
	 */
	public Object findRecordForObject(String sql, String[] args) {
		Cursor rawQuery = db.rawQuery(sql, args);
		
		String[] columnNames = rawQuery.getColumnNames();
		String value = "";
		while (rawQuery.moveToNext()) {
			for (String column : columnNames) {	
				value = rawQuery.getString(rawQuery.getColumnIndex(column));
			}
		}
		Log.i(ConfigLoader.TAG, "exec sql : " + sql);
		return value;
	}
	
	/**
	 * find database record...
	 * @param sql
	 * @param args
	 * @return record object list
	 */
	public List<Record> findRecordBean(String sql, String[] args) {
		List<Record> resultList = new ArrayList<Record>();		
		Cursor rawQuery = db.rawQuery(sql, args);
		
		String[] columnNames = rawQuery.getColumnNames();
		while (rawQuery.moveToNext()) {
			try {
				Record record = createRecordFromDbLine(columnNames, rawQuery);
				resultList.add(record);
			} catch (Exception e) {
				Log.i(ConfigLoader.TAG, "set value for record object by reflection encounter error..[" + e.getMessage() + "]");
				e.printStackTrace();
			}
		}
		Log.i(ConfigLoader.TAG, "exec sql : " + sql);
		return resultList;
	}
	
	private Record createRecordFromDbLine(String[] columnNames, Cursor rawQuery) throws IllegalArgumentException, IllegalAccessException {
		Record record = new Record();
		Field[] fields = record.getClass().getDeclaredFields();
		for (Field currentField : fields) {
			currentField.setAccessible(true);
			for (String name : columnNames) {
				if (name.equalsIgnoreCase(currentField.getName())) {
					String value = rawQuery.getString(rawQuery.getColumnIndex(name));
					if (currentField.getType() == int.class || currentField.getType() == Integer.class) {
						currentField.setInt(record, Integer.parseInt(value == null ? "0":value));
					} else if (currentField.getType() == Double.class || currentField.getType() == double.class) {
						DecimalFormat format = new DecimalFormat("#.00");
						currentField.setDouble(record, Double.parseDouble(format.format(Double.parseDouble(value == null ? "0":value))));
					} else if (currentField.getType() == boolean.class || currentField.getType() == Boolean.class) {
						currentField.setBoolean(record, Boolean.parseBoolean(value == null ? "false":value));
					} else {
						currentField.set(record, value == null ? "":value);
					}
					break;
				}
			}
		}
		return record;
	}
	
	/**
	 * get all column name for specify table
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> getTableColumn(String tablename) {
		String sql = "PRAGMA table_info(" +tablename+ ")";
		Cursor rawQuery = db.rawQuery(sql, null);
		List<Map<String, Object>> recordList = new ArrayList<Map<String,Object>>();
		
		String[] columnNames = rawQuery.getColumnNames();
		while (rawQuery.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (String column : columnNames) {		
				if (column.equals(ConfigLoader.COLUMN_NAME_KEY)) {
					map.put(column, rawQuery.getString(rawQuery.getColumnIndex(column)));
				}
			}
			recordList.add(map);
		}
		Log.i(ConfigLoader.TAG, "exec sql : " + sql);
		return recordList;
	}

	public void runCostomSql(String sql) {
		Log.i(ConfigLoader.TAG, "exec sql :	" + sql);
		db.execSQL(sql);
	}
}
