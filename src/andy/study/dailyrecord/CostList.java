package andy.study.dailyrecord;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import andy.study.dailyrecord.dao.DataManager;
import andy.study.dailyrecord.util.ActivityAgent;
import andy.study.dailyrecord.util.ConfigLoader;

public class CostList extends Activity{

	private LinearLayout costListPanel;
	
	private DataManager dm;
	
	private Button initMore;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cost_list);
		ActivityAgent.getInstance().addActivity(this);
		dm = new DataManager(this);
		
		costListPanel = (LinearLayout) this.findViewById(R.id.costListPanel);
		initMore = (Button)this.findViewById(R.id.buttonMore);
		initListPanel();		
		initMoreClick();
	}
	
	public void initMoreClick() {
		initMore.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View view) {
				limitLine += 10;
				initListPanel();
			}
		});
	}
	
	private int limitLine = 0;
	public void initListPanel() {
		String sql = "select a.recorddate, sum(a.record_value) records from t_daily_record a group by a.recorddate" +
				" order by a.recorddate desc limit " + limitLine + ", 10";
		List<Map<String, Object>> recordList = dm.findRecord(sql, null);
		int loop = 0;
		if (null == recordList || recordList.size() == 0) {
			Toast.makeText(this, "no more record...", Toast.LENGTH_LONG).show();
		}
		for (Map<String, Object> map : recordList) {			
			String date = map.get("recorddate").toString();
			String values = map.get("records").toString();
			
			appendListPanelContent(date, values, loop++);
		}	
	}
	
	private View.OnClickListener layoutListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View view) {
			LinearLayout clickView = (LinearLayout) view;
			TextView textView = (TextView) clickView.getChildAt(0);
			TextView valueView = (TextView) clickView.getChildAt(1);
			
			String childDate = textView.getText().toString();
			String childValue = valueView.getText().toString();
			
			Intent intent = new Intent(CostList.this, CostDetail.class);
			intent.putExtra("date", childDate);
			intent.putExtra("value", childValue);
			startActivity(intent);
		}
	};
	
	public void appendListPanelContent(String date, String values, int loop) {
		 LinearLayout layout = new LinearLayout(this);
		 android.widget.LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		 layout.setOrientation(LinearLayout.HORIZONTAL);		  
		 if (loop%2 == 0) {
			 layout.setBackgroundColor(this.getResources().getColor(R.color.button_addrecord_bg));
		 }
		 
		 layout.setOnClickListener(layoutListener);
		 
		 TextView textView = new TextView(this);
		 textView.setText(date);
		 textView.setGravity(Gravity.LEFT);
		 textView.setTextSize(35);		
		 TextView valueView = new TextView(this);
		 valueView.setText(values);
		 valueView.setGravity(Gravity.RIGHT);
		 valueView.setTextSize(35);	
		 
		 android.widget.LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
		 textParams.weight = 2;
		 textParams.gravity = Gravity.LEFT;
		 android.widget.LinearLayout.LayoutParams valuesParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
		 valuesParams.weight = 1;
		 valuesParams.gravity = Gravity.RIGHT;
		 layout.addView(textView, textParams);
		 layout.addView(valueView, valuesParams);
		 
		 
		 costListPanel.addView(layout, layoutParams);
	}
}
