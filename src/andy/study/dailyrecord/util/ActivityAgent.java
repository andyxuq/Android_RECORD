package andy.study.dailyrecord.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

/**
 * 为Activity程序完全退出提供解决方案    
 * @author Administrator
 *
 */
public class ActivityAgent extends Application {

	private List<Activity> activityList = new ArrayList<Activity>();
	
	private ActivityAgent(){}
	
	private static ActivityAgent activityAgent;
	
	public synchronized static ActivityAgent getInstance() {
		if (null == activityAgent) {
			activityAgent = new ActivityAgent();
		}
		return activityAgent;
	}
	
	public void addActivity(Activity activity) {
		if (!activityList.contains(activity)) {
			activityList.add(activity);
		}
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		
		for (Activity activity : activityList) {
			activity.finish();			
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}


