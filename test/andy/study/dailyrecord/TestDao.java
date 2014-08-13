package andy.study.dailyrecord;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.test.AndroidTestCase;
import android.util.Log;
import andy.study.dailyrecord.dao.DataManager;
import andy.study.dailyrecord.model.Record;
import andy.study.dailyrecord.util.ConfigLoader;

public class TestDao extends AndroidTestCase{
	
	public void insertRecord() {
		DataManager dataDao = new DataManager(getContext());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type_id", "2");
		map.put("sum_id", "1");
		map.put("record_value", "3.54");
		map.put("note", "好便宜的大盘鸡啊。。。");
		map.put(ConfigLoader.TABLE_NAME_KEY, ConfigLoader.RECORD_TABLE);
		
		Log.i(ConfigLoader.TAG, "insert result:	" + dataDao.addRecord(map));
	}
	
	public void batchAddRecord() throws ParseException {
		DataManager dataDao = new DataManager(getContext());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		
		String date = "2014-08-13";
		cal.setTime(format.parse(date));
		for (int i=0;i<365;i++) {			
			cal.add(Calendar.DAY_OF_YEAR, -1);
			Random random = new Random();
			int randomType = random.nextInt(5) + 1;
			String recordDate = format.format(cal.getTime());
			
			Record record = new Record();
			Map<String, Object> map = ConfigLoader.SPINNER_LIST.get(randomType);
			int typeid = Integer.parseInt(map.get("type_id").toString());
			String typeName = map.get("type_name").toString();
			record.setType_id(typeid);
			record.setType_name(typeName);
			
			record.setNote("买了" + typeName);
			record.setRecord_value(2);
			record.setRecorddate(recordDate);
			dataDao.addRecordByBean(record);
			Log.i(ConfigLoader.TAG, recordDate + "==》" + record.toString());
		}
	}
	
	public void insertRecordByBean() {
		Record record = new Record();
		record.setType_id(2);
		record.setNote("阔以");
		record.setRecord_value(2.23f);
		record.setRecorddate("2014-06-14");
		record.setType_name("衣物");
		
		DataManager dataDao = new DataManager(getContext());
		dataDao.addRecordByBean(record);
	}
	
	public void insertSumTableRecord() {
		DataManager dataDao = new DataManager(getContext());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sum_value", "2.5");
		map.put("sum_id", "1");
		map.put(ConfigLoader.TABLE_NAME_KEY, ConfigLoader.SUM_TABLE);
		
		Log.i(ConfigLoader.TAG, "insert result:	" + dataDao.addRecord(map));
	}
	
	public void findRecord() {
		DataManager dataDao = new DataManager(getContext());
		List<Map<String, Object>> findRecord = dataDao.findRecord("SELECT * from " + ConfigLoader.RECORD_TABLE, null);
		for (Map<String, Object> map : findRecord) {
			Log.i(ConfigLoader.TAG, "new line");
			for (String key : map.keySet()) {
				Log.i(ConfigLoader.TAG, key + "=" +map.get(key).toString());
			}
		}
	}
	
	public void findMaxSumId() {
		DataManager dataDao = new DataManager(getContext());
		List<Map<String, Object>> findRecord = dataDao.findRecord("SELECT max(sum_id) maxid from " + ConfigLoader.SUM_TABLE, null);
		for (Map<String, Object> map : findRecord) {
			Log.i(ConfigLoader.TAG, "new line");
			for (String key : map.keySet()) {
				Log.i(ConfigLoader.TAG, key + "=" +map.get(key).toString());
			}
		}
	}
	
	public void findRecordBean() {
		DataManager dataDao = new DataManager(getContext());
		List<Record> findRecord = dataDao.findRecordBean("SELECT * from " + ConfigLoader.RECORD_TABLE, null);
		for (Record record : findRecord) {			
			Log.i(ConfigLoader.TAG, record.toString());
		}
	}
	
	public void deleteRecord() throws Exception {
		DataManager dataDao = new DataManager(getContext());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tablename", ConfigLoader.RECORD_TABLE);
		map.put("whereClause", "id=?"); 
		map.put("whereArgs", 1);
		Log.i(ConfigLoader.TAG, "delete result : " + dataDao.deleteRecord(map));
	}
	
	public void updateRecord() throws Exception {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put(ConfigLoader.TABLE_NAME_KEY, ConfigLoader.RECORD_TABLE);
		conditionMap.put(ConfigLoader.WHERE_CLAUSE_KEY, "id=?");
		conditionMap.put(ConfigLoader.WHERE_ARGS_KEY, "1");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("record_value", "122.3");
		DataManager dataDao = new DataManager(getContext());
		Log.i(ConfigLoader.TAG, "update data : " + dataDao.updateRecord(conditionMap, paramMap));
	}
}
