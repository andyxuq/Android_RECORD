package andy.study.dailyrecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import andy.study.dailyrecord.dao.DataManager;
import andy.study.dailyrecord.listener.AddRecordClickListener;
import andy.study.dailyrecord.model.Record;
import andy.study.dailyrecord.util.ActivityAgent;
import andy.study.dailyrecord.util.ConfigLoader;
import andy.study.dailyrecord.util.ToolUtils;

/**
 * 添加记录
 * @author Administrator
 *
 */
public class AddRecord extends Activity{
	
	private Button backButton;
	private Button saveButton;
	private Button backButton_bottom;
	private Button saveButton_bottom;
	private Button showRecord_button;
	private EditText costText;
	private EditText noteText;
	private TextView suggestView;
	
	private TextView showDateView;
	
	private DataManager dm;
	
	private Spinner spiner;
	private List<Record> saveRecord = new ArrayList<Record>();
	
	private int year,month,day;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_record);		
		ActivityAgent.getInstance().addActivity(this);
		
		backButton = (Button)this.findViewById(R.id.backpage);
		saveButton = (Button)this.findViewById(R.id.savepage);
		backButton_bottom = (Button)this.findViewById(R.id.backpage_bottom);
		saveButton_bottom = (Button)this.findViewById(R.id.savepage_bottom);
		showRecord_button = (Button)this.findViewById(R.id.showrecord);
		noteText = (EditText)this.findViewById(R.id.note);
		showDateView = (TextView) this.findViewById(R.id.showDateView);
		
		suggestView = (TextView)this.findViewById(R.id.suggestnote);
		initCostEditText();		
		initSpinner();
		initDataView();		
		
		dm = new DataManager(getApplicationContext());
		
		AddRecordClickListener clickListener = new AddRecordClickListener(this);
		backButton.setOnClickListener(clickListener);
		saveButton.setOnClickListener(clickListener);
		backButton_bottom.setOnClickListener(clickListener);
		saveButton_bottom.setOnClickListener(clickListener);		
		showRecord_button.setOnClickListener(clickListener);
	}
	
	private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker arg0, int year, int month, int day) {
			AddRecord.this.year = year;
			AddRecord.this.month = month;
			AddRecord.this.day = day;
			updateDisplay(year, month, day);
		}
	};
	
	private void updateDisplay(int year, int month, int day) {
		showDateView.setVisibility(TextView.VISIBLE);
		
		StringBuilder builder = new StringBuilder();
		builder.append(year);
		builder.append("-" + ((month+1) < 10 ? "0" + (month+1) : (month+1)));
		builder.append("-" + (day < 10 ? "0" + day : day));
		showDateView.setText(builder);
	}
	
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this, dateSetListener, year, month, day);
	}
	
	@Override
	@Deprecated
	protected void onPrepareDialog(int id, Dialog dialog) {
		((DatePickerDialog)dialog).updateDate(year, month, day);
	}
	
	private void initDataView() {		
		String dateToday = ToolUtils.getDateBySpecFormat("yyyy-MM-dd");
		String[] dateArray = dateToday.split("-");
		this.year = Integer.parseInt(dateArray[0]);
		this.month = Integer.parseInt(dateArray[1]);
		this.day = Integer.parseInt(dateArray[2]);
		showDateView.setText(dateToday);
		showDateView.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View view) {
				showDateView.setVisibility(TextView.INVISIBLE);
				showDialog(showDateView.getId());
			}
		});
	}
	
	private void initCostEditText() {
		costText = (EditText)this.findViewById(R.id.cost);
		costText.setOnFocusChangeListener(new View.OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					//get focus event..
					suggestView.setText("");
				} else {
					//lose focus event..
				}
			}
		});
		costText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				suggestView.setText("");
			}
		});
	}
	
	/** init seletion for spinner */
	private void initSpinner() {
		spiner = (Spinner)this.findViewById(R.id.type);
//		String configType = ConfigLoader.getString("db.config.type");		
//		
//		if (configType != null && !configType.trim().equals("")) {
//			String[] types = configType.trim().split(",");
//			for (String type : types) {
//				try {
//					String[] current = type.trim().split(":");
//					if (Integer.parseInt(current[0]) > 10) {
//						Map<String, Object> map = new HashMap<String, Object>();
//						map.put("type_id", current[0]);
//						map.put("type_name", current[1]);
//						ConfigLoader.SPINNER_LIST.add(map);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
		
		Set<String> spinnerList = new HashSet<String>();
		for (Map<String, Object> map : ConfigLoader.SPINNER_LIST) {
			spinnerList.add(map.get("type_name").toString());
		}
		SpinnerAdapter sadapter = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_1, spinnerList.toArray());
		spiner.setAdapter(sadapter);
	}
	
	/**
	 * 获取当前页面的表单值
	 * @return
	 */
	public Record getCurrentUiRecord() {
		Record record = null;
		try {		
			record = new Record();
			String type_name = spiner.getSelectedItem().toString();
			String costValueText = costText.getText().toString();
			if (null == costValueText || "".equals(costValueText)) {
				return null;
			}
			float record_value = Float.parseFloat(costValueText);
			String note = noteText.getText().toString();
			String date = showDateView.getText().toString();
			
			
			record.setRecorddate(date);
			record.setNote(note);
			record.setRecord_value(record_value);
			record.setType_name(type_name);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "保存记录失败[" + e.getMessage() + "]", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			Log.i(ConfigLoader.TAG, e.getMessage());
		}
		return record;
	}
	
	/**
	 * 清空当前表单
	 */
	public void clearFormTabel() {
		spiner.setSelection(0);
		costText.setText("");
		noteText.setText("");
	}

	public Button getBackButton() {
		return backButton;
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public Button getBackButton_bottom() {
		return backButton_bottom;
	}

	public Button getSaveButton_bottom() {
		return saveButton_bottom;
	}

	public Button getShowRecord_button() {
		return showRecord_button;
	}

	public EditText getCostText() {
		return costText;
	}

	public EditText getNoteText() {
		return noteText;
	}

	public TextView getSuggestView() {
		return suggestView;
	}

	public DataManager getDm() {
		return dm;
	}

	public Spinner getSpiner() {
		return spiner;
	}

	public List<Record> getSaveRecord() {
		return saveRecord;
	}	
}
