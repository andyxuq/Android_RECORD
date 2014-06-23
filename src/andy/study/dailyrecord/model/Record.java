package andy.study.dailyrecord.model;

import java.util.Map;

import andy.study.dailyrecord.util.ConfigLoader;

public class Record {

	private int id;
	
	private int type_id;
	
	private String type_name;
	
	private int sum_id;
	
	private float record_value;
	
	private String note;
	
	private String recorddate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
		for (Map<String, Object> map : ConfigLoader.SPINNER_LIST) {
			if (map.get("type_name").toString().equals(type_name)) {
				this.type_id = Integer.parseInt(map.get("type_id").toString());
				break;
			}
		}		
	}

	public int getSum_id() {
		return sum_id;
	}

	public void setSum_id(int sum_id) {
		this.sum_id = sum_id;
	}

	public float getRecord_value() {
		return record_value;
	}

	public void setRecord_value(float record_value) {
		this.record_value = record_value;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getRecorddate() {
		return recorddate;
	}

	public void setRecorddate(String recorddate) {
		this.recorddate = recorddate;
	}

	@Override
	public String toString() {
		return "Record [id=" + id + ", type_id=" + type_id + ", type_name="
				+ type_name + ", sum_id=" + sum_id + ", record_value="
				+ record_value + ", note=" + note + ", recorddate="
				+ recorddate + "]";
	}	
}
