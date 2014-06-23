package andy.study.dailyrecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;
import andy.study.dailyrecord.dao.DataManager;
import andy.study.dailyrecord.model.Record;
import andy.study.dailyrecord.util.ConfigLoader;
import andy.study.dailyrecord.util.ToolUtils;

/**
 * 保存记录的帮助类
 * @author Administrator
 *
 */
public class RecordHelper {
	
	private static List<Record> recordList = new ArrayList<Record>();	
	
	public static String[] getListArray() {
		if (recordList.size() == 0) {
			return null;
		} else {
			String[] recordArray = new String[recordList.size()];
			int i = 0;
			for (Record record : recordList) {
				recordArray[i++] = record.getType_name() + ConfigLoader.RECORD_SEPARATE + record.getRecord_value();
			}
			return recordArray;
		}
	}
	
	public static boolean[] getListArrayChecked() {
		if (recordList.size() == 0) {
			return null;
		} else {
			boolean[] recordArray = new boolean[recordList.size()];
			int i = 0;
			for (Record record : recordList) {
				recordArray[i++] = false;
			}
			return recordArray;
		}
	}
	
	public static List<Record> getRecordList() {
		return recordList;
	}

	public static void addNewRecordToList(Record record)	 {
		if (record != null) {
			recordList.add(record);
		}
	}
	
	/**
	 * 保存所有列表里的记录
	 * @param dm
	 * @return
	 */
	public static boolean saveAllRecord(DataManager dm) {
		boolean result = false;
		try {			
			String dateToday = ToolUtils.getDateBySpecFormat("yyyy-MM-dd");
			for (Record record : recordList) {
				if (record.getRecorddate() == null || "".equals(record.getRecorddate())) {
					record.setRecorddate(dateToday);
				}				
				dm.addRecordByBean(record);
			}
			result = true;
			recordList.clear();
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
			Log.i(ConfigLoader.TAG, "save data encounter error..[" + e.getMessage() + "]" );
		}		
		return result;
	}
}
