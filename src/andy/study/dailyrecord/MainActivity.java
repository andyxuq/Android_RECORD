package andy.study.dailyrecord;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import andy.study.dailyrecord.chart.MainCostChart;
import andy.study.dailyrecord.dao.DataManager;
import andy.study.dailyrecord.listener.CostTextClickListener;
import andy.study.dailyrecord.model.Record;
import andy.study.dailyrecord.util.ActivityAgent;
import andy.study.dailyrecord.util.ConfigLoader;
import andy.study.dailyrecord.util.ToolUtils;

public class MainActivity extends Activity {

	private Button button;
	
	private DataManager dm;
	
	private TextView costTodayTitle;
	
	private MainCostChart mcc;
	
	private LinearLayout chartContainer;
	
	private Button buttomButton;
	
	private TextView costTodayCost, costAllTotal, costSummaryWeek, costSummaryMonth, costSummaryHYear, costSummaryYear;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityAgent.getInstance().addActivity(this);
        
        dm = new DataManager(this);
        costTodayTitle = (TextView) this.findViewById(R.id.cost_today_title);
        costTodayCost = (TextView) this.findViewById(R.id.costTodayCost);
        costAllTotal = (TextView) this.findViewById(R.id.costAllTotal);
        costSummaryWeek = (TextView) this.findViewById(R.id.costSummaryWeek);
        costSummaryMonth = (TextView) this.findViewById(R.id.costSummaryMonth);
        costSummaryHYear = (TextView) this.findViewById(R.id.costSummaryHYear);
        costSummaryYear = (TextView) this.findViewById(R.id.costSummaryYear);
        
        CostTextClickListener clickListener = new CostTextClickListener(this, dm);
        costTodayCost.setOnClickListener(clickListener);
        costAllTotal.setOnClickListener(clickListener);
        costSummaryWeek.setOnClickListener(clickListener);
        costSummaryMonth.setOnClickListener(clickListener);
        costSummaryHYear.setOnClickListener(clickListener);
        costSummaryYear.setOnClickListener(clickListener);
        
        button = (Button)this.findViewById(R.id.addRecord);
        button.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, AddRecord.class);
				startActivity(intent);
			}
		});
        
        initMainTextView();
        getCostedValues();
//        initChartView();        
    }
    
    /** 获取以往消费记录 */
    public void getCostedValues() {
    	try {
	    	//当日消费记录
	    	String sql = "select sum(record_value) from t_daily_record t where datetime(t.recorddate) = datetime(?)";
	    	String[] args = new String[]{ToolUtils.getDateBySpecFormat("yyyy-MM-dd")};
	    	Object costToday = dm.findRecordForObject(sql, args);
//	    	costTodayCost.setText(ToolUtils.getCostString(costToday));
	    	ToolUtils.setViewText(costTodayCost, costToday);
	    	
	    	//总消费记录
	    	sql = "select sum(record_value) from t_daily_record t ";
	    	Object costAll = dm.findRecordForObject(sql, null);
//	    	costAllTotal.setText(ToolUtils.getCostString(costAll));
	    	ToolUtils.setViewText(costAllTotal, costAll);
	    	
	    	//本周消费
	    	sql = "select sum(record_value) from t_daily_record t where datetime(t.recorddate) >= datetime(?) and datetime(t.recorddate) <= datetime(?)";
	    	String[] weekArgs = new String[]{ToolUtils.getMondayOfWeek(null), ToolUtils.getSundayOfWeek(null)};
	    	Object costWeek = dm.findRecordForObject(sql, weekArgs);
//	    	costSummaryWeek.setText(ToolUtils.getCostString(costWeek));
	    	ToolUtils.setViewText(costSummaryWeek, costWeek);
	    	
	    	//本月消费
	    	String[] monthArgs = new String[]{ToolUtils.getFirstDayOfMonth(null), ToolUtils.getLastDayOfMonth(null)};
	    	Object costMonth = dm.findRecordForObject(sql, monthArgs);
//	    	costSummaryMonth.setText(ToolUtils.getCostString(costMonth));
	    	ToolUtils.setViewText(costSummaryMonth, costMonth);
	    	
	    	//半年消费
	    	String[] halfArgs = new String[]{ToolUtils.getfirstDayOfHalfYear(), ToolUtils.getLastDayOfHalfYear()};
	    	Object costHalf = dm.findRecordForObject(sql, halfArgs);
//	    	costSummaryHYear.setText(ToolUtils.getCostString(costHalf));
	    	ToolUtils.setViewText(costSummaryHYear, costHalf);
	    	
	    	//一年消费
	    	String[] yearArgs = new String[]{ToolUtils.getFirstDayOfYear(), ToolUtils.getLastDayOfYear()};
	    	Object costYear = dm.findRecordForObject(sql, yearArgs);
//	    	costSummaryYear.setText(ToolUtils.getCostString(costYear));
	    	ToolUtils.setViewText(costSummaryYear, costYear);
	    	
	    	Log.i(ConfigLoader.TAG, "today:" + costToday + ", total:" + costAll + ", week:" + costWeek + ", month:" + costMonth + "" +
	    			", half:" + costHalf + ", year:" + costYear + ",today:" + ToolUtils.getDateBySpecFormat("yyyy-MM-dd"));
	    	
	    	//查找近7天记录
	    	sql = "select a.recorddate, sum(a.record_value) record_value from (select t.recorddate, t.record_value from t_daily_record t order by t.recorddate desc limit 0, 50 )a group by a.recorddate order by a.recorddate desc limit 0, 7";
	    	List<Record> sevenRecords = dm.findRecordBean(sql, null);
	    	Collections.reverse(sevenRecords);
	    	String[] dateArray = new String[sevenRecords.size()];
	    	double[] valueArray = new double[sevenRecords.size()];
	    	int i = 0;
	    	for (Record record : sevenRecords) {
	    		dateArray[i] = record.getRecorddate();
	    		valueArray[i] = record.getRecord_value();
	    		i++;
	    	}
	        initChartView(dateArray, valueArray);
    	} catch (ParseException e) {
    		e.printStackTrace();
    		Toast.makeText(this, "Bad Info:获取记录出错", Toast.LENGTH_LONG).show();
    	}
    }

    
    private int widthPixels;
    private int heightPixels;
    
    public int getWidthPixels() {
		return widthPixels;
	}
	public int getHeightPixels() {
		return heightPixels;
	}
	/**
     * 设置图表
     */
    public void initChartView(String[] dateArray, double[] valueArray) {
    	mcc = new MainCostChart(valueArray, dateArray);
    	//获取屏幕高度
    	DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		widthPixels = dm.widthPixels;
		heightPixels = dm.heightPixels;
    	chartContainer = (LinearLayout) this.findViewById(R.id.chartRow);   
    	buttomButton = (Button) this.findViewById(R.id.buttomForward);
		View chartView = mcc.executeWithView(this);
		
		 
		chartContainer.addView(chartView, LayoutParams.WRAP_CONTENT, heightPixels/4);
		buttomButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, AddRecord.class);
				startActivity(intent);
			}
		});
	}

	public void initMainTextView() {
    	String text = "<img src='" +R.drawable.before_cost_title+ "' />" + getResources().getString(R.string.cost_today_title);
    	Spanned costTitleHtml = Html.fromHtml(text, imageGetter, null);
    	costTodayTitle.setText(costTitleHtml);
    }

    ImageGetter imageGetter = new Html.ImageGetter() {
		
		@Override
		public Drawable getDrawable(String arg0) {
			int rId=Integer.parseInt(arg0);
		    Drawable drawable=getResources().getDrawable(rId);
		    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		    return drawable;
		}
	};
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
       
    public TextView getCostTodayTitle() {
		return costTodayTitle;
	}

	public TextView getCostTodayCost() {
		return costTodayCost;
	}

	public TextView getCostAllTotal() {
		return costAllTotal;
	}

	public TextView getCostSummaryWeek() {
		return costSummaryWeek;
	}

	public TextView getCostSummaryMonth() {
		return costSummaryMonth;
	}

	public TextView getCostSummaryHYear() {
		return costSummaryHYear;
	}

	public TextView getCostSummaryYear() {
		return costSummaryYear;
	}


	private boolean isExist = false;
	private boolean hasTask = false;
	
	/** 点击两下退出程序 */
	ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
	private Runnable runnabel = new Runnable() {
		public void run() {
			isExist = false;
			hasTask = false;
		}
	};
	
	/** 点击两下退出程序 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i(ConfigLoader.TAG, "OO U want exist now");
			if (!isExist) {
				isExist = true;
				Toast.makeText(getApplicationContext(), "再按一次退出键退出程序哟O(∩_∩)O~", Toast.LENGTH_SHORT).show();
				if(!hasTask) {
					hasTask = true;
					exec.schedule(runnabel, 3000, TimeUnit.MILLISECONDS);
				}				
			} else {
				Log.i(ConfigLoader.TAG, "OO U really want exist now");
				//添加退出代码
				ActivityAgent.getInstance().onTerminate();
			}
		}
		return true;
	}
}
