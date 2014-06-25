package andy.study.dailyrecord.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ToolUtils {

	public static String getDateBySpecFormat(String formatString) {
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		return format.format(new Date());
	}
	
	public static void main(String[] args) {
		System.out.println(getDateBySpecFormat("yyyy-MM-dd"));    //
	}	
}
