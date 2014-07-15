package andy.study.dailyrecord;

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
import andy.study.dailyrecord.util.ActivityAgent;
import andy.study.dailyrecord.util.ConfigLoader;

public class MainActivity extends Activity {

	private Button button;
	
	private DataManager dm;
	
	private TextView costTodayTitle;
	
	private MainCostChart mcc;
	
	private LinearLayout chartContainer;
	
	private Button buttomButton;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityAgent.getInstance().addActivity(this);
        
        dm = new DataManager(this);
        costTodayTitle = (TextView) this.findViewById(R.id.cost_today_title);
        button = (Button)this.findViewById(R.id.addRecord);
        button.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, AddRecord.class);
				startActivity(intent);
			}
		});
        
        initMainTextView(); 
        initChartView();     
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
    public void initChartView() {
    	mcc = new MainCostChart();
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
