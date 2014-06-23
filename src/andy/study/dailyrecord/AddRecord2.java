package andy.study.dailyrecord;
import android.app.Activity;
import android.os.Bundle;
import andy.study.dailyrecord.util.ActivityAgent;


public class AddRecord2 extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_record2);
		ActivityAgent.getInstance().addActivity(this);
	}	
}