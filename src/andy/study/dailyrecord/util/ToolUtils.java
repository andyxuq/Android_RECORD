package andy.study.dailyrecord.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.widget.TextView;

public class ToolUtils {

	public static String getDateBySpecFormat(String formatString) {
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		return format.format(new Date());
	}
	
	/**
	 * 获取date所在周的周一，若date为null，则获取本周周一
	 * @param date 日期字符串，需要用yyyy-MM-dd格式
	 * @return
	 * @throws ParseException 
	 */
	public static String getMondayOfWeek(String date) throws ParseException {		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar cal = Calendar.getInstance();
		
		if (date != null) {	
			cal.setTime(format.parse(date));			
		}
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return format.format(cal.getTime());		
	}
	
	/**
	 * 获取date所在周的周日，若date为null，则获取本周周日
	 * @param date 日期字符串，需要用yyyy-MM-dd格式
	 * @return
	 * @throws ParseException 
	 */
	public static String getSundayOfWeek(String date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar cal = Calendar.getInstance();

		String monday = getMondayOfWeek(date);
		cal.setTime(format.parse(monday));
		cal.add(Calendar.DAY_OF_WEEK, 6);
		return format.format(cal.getTime());
	}
	
	/**
	 * 获取date所有月的第一天，若date为null，则获取本月第一天
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public static String getFirstDayOfMonth(String date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar cal = Calendar.getInstance();
		
		if (null != date) {
			cal.setTime(format.parse(date));
		}
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return format.format(cal.getTime());
	}
	/**
	 * 获取date所有月的最后一天，若date为null，则获取本月最后一天
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public static String getLastDayOfMonth(String date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar cal = Calendar.getInstance();
		
		if (null != date) {
			cal.setTime(format.parse(date));
		}		
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, -1);
		return format.format(cal.getTime());
	}
	
	/**
	 * 获取近半年的第一天
	 * @return
	 */
	public static String getfirstDayOfHalfYear() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar cal = Calendar.getInstance();
		
		int month = cal.get(Calendar.MONTH);
		if (month <= 5) {
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.DAY_OF_MONTH, 1);
		} else {
			cal.set(Calendar.MONTH, Calendar.JULY);
			cal.set(Calendar.DAY_OF_MONTH, 1);
		}		
		return format.format(cal.getTime());
	}
	
	/**
	 * 获取近半年的最后一天
	 * @return
	 */
	public static String getLastDayOfHalfYear() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar cal = Calendar.getInstance();
		
		int month = cal.get(Calendar.MONTH);
		if (month <= 5) {
			cal.set(Calendar.MONTH, Calendar.JUNE);
			cal.set(Calendar.DAY_OF_MONTH, 30);
		} else {
			cal.set(Calendar.MONTH, Calendar.DECEMBER);
			cal.set(Calendar.DAY_OF_MONTH, 31);
		}		
		return format.format(cal.getTime());
	}
	
	/**
	 * 获取当年的第一天
	 * @return
	 */
	public static String getFirstDayOfYear() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar cal = Calendar.getInstance();		
		
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);			
		return format.format(cal.getTime());
	}
	/**
	 * 获取当年的最后一天
	 * @return
	 */
	public static String getLastDayOfYear() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar cal = Calendar.getInstance();		
		
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 31);			
		return format.format(cal.getTime());
	}
	
	public static void setViewText(TextView view, Object cost) {
		String result = getCostString(cost);
		if ("" != result) {
			view.setText(result);
		}
	}
	
	public static String getCostString(Object cost) {
		String result = "";		
		if (null != cost && !cost.equals("")) {			
			String costString = cost.toString();			
			String subfix = "";
			String prefix = costString;
			
			if (costString.contains(".")) {
				subfix = costString.substring(costString.indexOf("."));
				prefix = costString.substring(0, costString.indexOf("."));
			}
			char[] preArray = new StringBuilder(prefix).reverse().toString().toCharArray();
			StringBuilder preBuilder = new StringBuilder();
			for (int i=0; i<preArray.length; i++) {			
				if (i%3 == 0 && i != 0) {
					preBuilder.append(",");
				} 
				preBuilder.append(preArray[i]);
			}
			
			result = preBuilder.reverse().toString();
			if (result.startsWith(",")) {
				result = result.substring(1);
			}
			if (result.endsWith(",")) {
				result = result.substring(0, result.lastIndexOf(","));
			}
			result = result + subfix;									
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(getDateBySpecFormat("yyyy-MM-dd"));    //
	}	
}
