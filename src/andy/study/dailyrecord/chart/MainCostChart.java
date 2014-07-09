package andy.study.dailyrecord.chart;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer.FillOutsideLine;

import android.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;
import andy.study.dailyrecord.MainActivity;

public class MainCostChart extends AbstractDemoChart {

	@Override
	public String getName() {
		return "MainCostChart";
	}

	@Override
	public String getDesc() {
		return "MainCostChart";
	}
		
	@Override
	public View executeWithView(Context context) {
		String[] titles = new String[] { "Crete"};
		List<double[]> x = new ArrayList<double[]>();	    
	    x.add(new double[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });	   
	    
	    List<double[]> values = new ArrayList<double[]>();
	    values.add(new double[] { 23, 12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2,
	        13.9 });
	    
	    int[] colors = new int[] { Color.GREEN};
	    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE};
	    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);	    
	    int length = renderer.getSeriesRendererCount();
	    for (int i = 0; i < length; i++) {
	        ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
	      }
	      setChartSettings(renderer, "近7天消费记录", "Month", "Temperature", 0, 14, 10, 30,
	          Color.LTGRAY, Color.LTGRAY); //设置X,Y坐标最大值
	      renderer.setXLabels(12);
	      renderer.setYLabels(10);
	      renderer.setApplyBackgroundColor(true);
	      renderer.setBackgroundColor(context.getResources().getColor(R.color.white));
	      renderer.setMarginsColor(context.getResources().getColor(R.color.white));
	      renderer.setShowGrid(true);
//	      renderer.setXTitle("goods test"); //设置X轴标题
	      
//	      renderer.setDisplayValues(true);
	      XYSeriesRenderer r = (XYSeriesRenderer)renderer.getSeriesRendererAt(0);
	      FillOutsideLine line = new FillOutsideLine(FillOutsideLine.Type.BELOW);
	      line.setColor(Color.BLUE);
	      r.addFillOutsideLine(line);
	      r.setDisplayChartValues(true); //设置在上方显示值
	      r.setChartValuesTextSize(20);	      
	      r.setChartValuesTextAlign(Align.LEFT);
	      
	      XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
	      XYSeries series = dataset.getSeriesAt(0);	      
	      return ChartFactory.getLineChartView(context, dataset, renderer);		
	}
}
