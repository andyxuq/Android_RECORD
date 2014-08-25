package andy.study.dailyrecord.chart;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import andy.study.dailyrecord.R;

public class CostDetailChart extends AbstractDemoChart{

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getDesc() {
		return null;
	}

	@Override
	public View executeWithView(Context context) {
		return null;
	}
	
	private double[] chartValues;
	private double totalValues = 0;
	public CostDetailChart(double[] chartValues) {
		this.chartValues = chartValues;
	}
	
	private List<Map<String, Object>> costList;
	public CostDetailChart(List<Map<String, Object>> costList) {
		this.costList = costList;
		chartValues = new double[costList.size()];
		int i = 0;
		for (Map<String, Object> map : costList) {
			chartValues[i++] = map.get("recordValue") == null ? 0.0d : Double.parseDouble(map.get("recordValue").toString());			
		}
	}

	@Override
	public Intent execute(Context context) {
//		double[] values = new double[] { 12, 14, 11, 10, 19 };
	    int[] colors = buildColors(chartValues, context);
	    DefaultRenderer renderer = buildCategoryRenderer(colors);    
	    renderer.setZoomButtonsVisible(true);
	    renderer.setZoomEnabled(false);
	    renderer.setChartTitleTextSize(20);
	    renderer.setDisplayValues(true);
//	    renderer.setShowLabels(false);
	    renderer.setShowLabels(true); 	    
	    renderer.setApplyBackgroundColor(true);
	    renderer.setBackgroundColor(Color.BLACK);
	    
	    double maxMe = 0;	    
	    for (double max : chartValues) {
	    	totalValues += max;
	    	if (max > maxMe) {
	    		maxMe = max;	    		
	    	}	    	
	    }
	    
	    int maxIndex = 0;
	    for (double max : chartValues) {
	    	if (max == maxMe) {
	    	    SimpleSeriesRenderer r = renderer.getSeriesRendererAt(maxIndex);
	    	    r.setGradientEnabled(true);
	    	    r.setGradientStart(0, Color.BLUE);
	    	    r.setGradientStop(0, Color.GREEN);
	    	    r.setHighlighted(true);
	    	}
	    	maxIndex++;
	    }
//	    SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
//	    r.setGradientEnabled(true);
//	    r.setGradientStart(0, Color.BLUE);
//	    r.setGradientStop(0, Color.GREEN);
//	    r.setHighlighted(true);
	    Intent intent = ChartFactory.getPieChartIntent(context,
	        buildCategoryDataset("Project budget", chartValues), renderer, "Budget");
	    return intent;
	}
	

	protected CategorySeries buildCategoryDataset(String title, double[] values) {
		CategorySeries series = new CategorySeries(title);
	    int k = 0;
	    NumberFormat format = NumberFormat.getPercentInstance(Locale.CHINA);
	    format.setMaximumFractionDigits(2);
	    for (double value : values) {
	    	Map<String, Object> map = costList.get(k++);
	    	String typeName = map.get("type_name") == null ? "哪里冒出来的" : map.get("type_name").toString();
	    		    	
	    	series.add(typeName + ":" + format.format(value/totalValues), value);
	    }
	    return series;
	}
	
	public int[] buildColors(double[] values, Context context) {		
		int[] colors = new int[values.length];
		int i = 0;
		for (double value : values) {
			if ( i==0 ) {
				colors[i] = context.getResources().getColor(R.color.cost_chart_01);
			} else if (i == 1) {
				colors[i] = context.getResources().getColor(R.color.cost_chart_02);
			} else if (i == 2) {
				colors[i] = context.getResources().getColor(R.color.cost_chart_03);
			} else if (i == 3) {
				colors[i] = context.getResources().getColor(R.color.cost_chart_04);
			} else if (i == 4) {
				colors[i] = context.getResources().getColor(R.color.cost_chart_05);
			} else if (i == 5) {
				colors[i] = context.getResources().getColor(R.color.cost_chart_06);
			} else {
				colors[i] = context.getResources().getColor(R.color.cost_chart_06);
			}
			i++;
		}
		return colors;
	}
}
