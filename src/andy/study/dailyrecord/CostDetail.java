package andy.study.dailyrecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import andy.study.dailyrecord.dao.DataManager;
import andy.study.dailyrecord.model.Record;
import andy.study.dailyrecord.util.ActivityAgent;
import andy.study.dailyrecord.util.ConfigLoader;
import andy.study.dailyrecord.util.ToolUtils;

/**
 * 消费明细
 * @author xuqing
 *
 */
public class CostDetail extends Activity{
	
	private TextView dateText;
	private LinearLayout detailPanel;
	
	private TextView totalCost;
	
	private DataManager dm;
	
	private int year,month,day;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cost_detail);
		ActivityAgent.getInstance().addActivity(this);
		
		dm = new DataManager(this);
		dateText = (TextView) this.findViewById(R.id.costDetailDate);
		detailPanel = (LinearLayout) this.findViewById(R.id.costDetailPanel);
		totalCost = (TextView)this.findViewById(R.id.costDetailCost);
		
		Intent intent = this.getIntent();		
		String date = intent.getStringExtra("date");
		String value = intent.getStringExtra("value");
		
		initDataView(date, value);		
		initCostDetail(value);
	}
	
	private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker arg0, int year, int month, int day) {
			CostDetail.this.year = year;
			CostDetail.this.month = month;
			CostDetail.this.day = day;
			updateDisplay(year, month, day);
		}
	};
	
	private void updateDisplay(int year, int month, int day) {
		dateText.setVisibility(TextView.VISIBLE);
		
		StringBuilder builder = new StringBuilder();
		builder.append(year);
		builder.append("-" + ((month + 1) < 10 ? "0" + (month + 1) : (month + 1)));
		builder.append("-" + (day < 10 ? "0" + day : day));
		dateText.setText(builder);
		
		initCostDetail("-1");
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
	
	int dataClickTimes = 0;
	private void initDataView(String date, String value) {		
		String dateToday = ToolUtils.getDateBySpecFormat("yyyy-MM-dd");
		if (null != date || !"".equals(date)) {
			dateToday = date;
		}
		String[] dateArray = dateToday.split("-");
		this.year = Integer.parseInt(dateArray[0]);
		this.month = Integer.parseInt(dateArray[1]);
		this.day = Integer.parseInt(dateArray[2]);
		dateText.setText(dateToday);
		totalCost.setText("共：" + value);
		dateText.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View view) {
				dateText.setVisibility(TextView.INVISIBLE);
				if (dataClickTimes++ == 0) {
					CostDetail.this.month = CostDetail.this.month - 1; 
				}
				showDialog(dateText.getId());
			}
		});
	}
	
	public void initCostDetail(String value) {
		detailPanel.removeAllViews();
		detailPanel.invalidate();
		String sql = "select * from t_daily_record t where datetime(t.recorddate) = datetime(?) order by t.id asc";		
		String date = dateText.getText().toString();		
		
		List<Record> recordList = dm.findRecordBean(sql, new String[]{date});
		for (Record record : recordList) {
			appCostDetail(record);
		}
		
		if ("-1".equals(value)) {
			sql = "select sum(record_value) records from t_daily_record t where datetime(t.recorddate) = datetime(?)";
			Object records = dm.findRecordForObject(sql, new String[]{date});
			
			totalCost.setText("共：" + records.toString());
		}
		
	}
	
	public void appCostDetail(Record record) {		
		LinearLayout layout = new LinearLayout(this);
		TextView typeView = new TextView(this);
		typeView.setText(record.getType_name());
		typeView.setTextSize(20);		
		typeView.setGravity(Gravity.CENTER_HORIZONTAL);
		typeView.setBackgroundColor(getResources().getColor(R.color.cost_today_tcolor));
		
		TextView valueView = new TextView(this);
		valueView.setText(String.valueOf(record.getRecord_value()));
		valueView.setTextSize(20);
		valueView.setGravity(Gravity.CENTER_HORIZONTAL);
		valueView.setBackgroundColor(getResources().getColor(R.color.cost_today_tcolor));
		
		TextView button = new TextView(this);
		button.setId(record.getId());
		button.setText("删除");
		button.setTextSize(20);
		button.setGravity(Gravity.CENTER_HORIZONTAL);		
		button.setBackgroundColor(getResources().getColor(R.color.cost_today_tcolor));
		button.setOnClickListener(deleteListener);
		
		LayoutParams left = new TableLayout.LayoutParams();
		left.width = 0;
		left.height = LayoutParams.WRAP_CONTENT;		
		left.weight = 2;
		left.rightMargin = 1;
		left.bottomMargin = 1;
		LayoutParams center = new TableLayout.LayoutParams();
		center.width = 0;
		center.height = LayoutParams.WRAP_CONTENT;
		center.weight = 5;
		center.rightMargin = 1;
		center.bottomMargin = 1;
		LayoutParams right = new TableLayout.LayoutParams();
		right.width = 0;
		right.height = LayoutParams.WRAP_CONTENT;
		right.weight = 2;
		right.bottomMargin = 1;
		layout.addView(typeView, left);
		layout.addView(valueView, left);
		layout.addView(button, right);
		detailPanel.addView(layout, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));	
		
		LinearLayout noteLayout = new LinearLayout(this);		
		TextView noteView = new TextView(this);
		noteView.setText(record.getNote());
		noteView.setTextSize(20);
		noteView.setGravity(Gravity.CENTER_HORIZONTAL);
		if (record.getRecord_value() < 50) {
			noteView.setBackgroundColor(getResources().getColor(R.color.cost_detail_info));
		} else if (record.getRecord_value() >=50 && record.getRecord_value() < 100) {
			noteView.setBackgroundColor(getResources().getColor(R.color.cost_detail_warning));
		} else {
			noteView.setBackgroundColor(getResources().getColor(R.color.cost_detail_error));
		}
		TextView noteViewSpace = new TextView(this);
		noteViewSpace.setText("");
		noteViewSpace.setTextSize(20);
		noteViewSpace.setGravity(Gravity.CENTER_HORIZONTAL);	
				
		LayoutParams noteParams = new TableLayout.LayoutParams();
		noteParams.width = 0;
		noteParams.height = LayoutParams.WRAP_CONTENT;
		noteParams.weight = 6;
		noteParams.leftMargin = 1;
		noteParams.bottomMargin = 1;
		LayoutParams noteSpace = new TableLayout.LayoutParams();
		noteSpace.width = 0;
		noteSpace.height = LayoutParams.WRAP_CONTENT;
		noteSpace.weight = 1;		
		noteSpace.bottomMargin = 1;
		noteLayout.addView(noteViewSpace, noteSpace);
		noteLayout.addView(noteView, noteParams);
		
		detailPanel.addView(noteLayout, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	private View.OnClickListener deleteListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View view) {
			final int recordId = view.getId();
			Builder dialog = new AlertDialog.Builder(CostDetail.this);
			dialog.setTitle("确认操作");
			dialog.setMessage("确定删除记录吗？");
			dialog.setIcon(R.drawable.ic_launcher);
			dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					try {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("tablename", ConfigLoader.RECORD_TABLE);
						map.put("whereClause", "id=?"); 
						map.put("whereArgs", recordId);
						Log.i(ConfigLoader.TAG, "delete result : " + dm.deleteRecord(map));
						Toast.makeText(CostDetail.this, "删除成功", Toast.LENGTH_LONG).show();
						
						CostDetail.this.initCostDetail("-1");
					} catch (Exception e) {
						e.printStackTrace();
						Log.i(ConfigLoader.TAG, "delete result error:" + e.getMessage());
						Toast.makeText(CostDetail.this, "删除记录失败", Toast.LENGTH_LONG).show();
					}
				}
			});
			dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					Log.i(ConfigLoader.TAG, "delete operation cancel");
				}
			});	
			
			dialog.create();
			dialog.show();
		}
		
	};
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);			
		}
		return true;
	};
}

