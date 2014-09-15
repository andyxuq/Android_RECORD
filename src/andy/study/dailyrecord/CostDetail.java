package andy.study.dailyrecord;

import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import andy.study.dailyrecord.dao.DataManager;
import andy.study.dailyrecord.model.Record;
import andy.study.dailyrecord.util.ActivityAgent;
import andy.study.dailyrecord.util.ToolUtils;

/**
 * 消费明细
 * @author xuqing
 *
 */
public class CostDetail extends Activity{
	
	private TextView dateText;
	private LinearLayout detailPanel;
	
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
		initDataView();
		
		initCostDetail();
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
	private void initDataView() {		
		String dateToday = ToolUtils.getDateBySpecFormat("yyyy-MM-dd");
		String[] dateArray = dateToday.split("-");
		this.year = Integer.parseInt(dateArray[0]);
		this.month = Integer.parseInt(dateArray[1]);
		this.day = Integer.parseInt(dateArray[2]);
		dateText.setText(dateToday);
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
	
	public void initCostDetail() {
		String sql = "select * from t_daily_record t where datetime(t.recorddate) = datetime(?) order by t.id asc";		
		String date = dateText.getText().toString();		
		
		List<Record> recordList = dm.findRecordBean(sql, new String[]{date});
		for (Record record : recordList) {
			appCostDetail(record);
		}
	}
	
	public void appCostDetail(Record record) {
		String text = "";
	}
}
