package andy.study.dailyrecord.listener;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import andy.study.dailyrecord.MainActivity;
import andy.study.dailyrecord.chart.CostDetailChart;
import andy.study.dailyrecord.dao.DataManager;
import andy.study.dailyrecord.util.ConfigLoader;
import andy.study.dailyrecord.util.ToolUtils;

/**
 * 消费数字点击跳转到饼图的动作监听类
 * @author xuqing
 *
 */
public class CostTextClickListener implements OnClickListener{

	private MainActivity activity;
	
	private DataManager dm;
	
	public CostTextClickListener(MainActivity activity, DataManager dm) {
		this.activity = activity;
		this.dm = dm;
	}
	
	@Override
	public void onClick(View view) {
		//where datetime(t.recorddate) >= datetime(?) and datetime(t.recorddate) <= datetime(?)
		String whereCondition = " #where_condition# ";
		String sql = "select t.type_name, sum(t.record_value) recordValue from t_daily_record t " + whereCondition + " group by t.type_name";
		
		try {
			List<Map<String, Object>> costList = null;
			String[] args = null;
			if (view == activity.getCostAllTotal()) {
				sql = sql.replace(whereCondition, "");			
			} else if (view == activity.getCostTodayCost()) {
				sql = sql.replace(whereCondition, "where datetime(t.recorddate) = datetime(?)");
				args = new String[]{ToolUtils.getDateBySpecFormat("yyyy-MM-dd")};			
			} else {
				sql = sql.replace(whereCondition, "where datetime(t.recorddate) >= datetime(?) and datetime(t.recorddate) <= datetime(?)");
				if (view == activity.getCostSummaryHYear()) {		
					args = new String[]{ToolUtils.getfirstDayOfHalfYear(), ToolUtils.getLastDayOfHalfYear()};
				} else if (view == activity.getCostSummaryMonth()) {
					args = new String[]{ToolUtils.getFirstDayOfMonth(null), ToolUtils.getLastDayOfMonth(null)};
				} else if (view == activity.getCostSummaryWeek()) {
					args = new String[]{ToolUtils.getMondayOfWeek(null), ToolUtils.getSundayOfWeek(null)};
				} else if (view == activity.getCostSummaryYear()) {
					args = new String[]{ToolUtils.getFirstDayOfYear(), ToolUtils.getLastDayOfYear()};
				}
			}
			costList = dm.findRecord(sql, args);
			if (costList != null && costList.size() !=0) {
				CostDetailChart cdc = new CostDetailChart(costList);
				Intent intent = cdc.execute(activity);
				activity.startActivity(intent);
			} else {
				Toast.makeText(activity, "未获取到详细消费数据", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(ConfigLoader.TAG, e.getMessage());
			Toast.makeText(activity, "获取详细数据出错", Toast.LENGTH_LONG).show();
		}
	}
}
