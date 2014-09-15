package andy.study.dailyrecord.chart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer.FillOutsideLine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.View;
import andy.study.dailyrecord.R;
import andy.study.dailyrecord.util.ConfigLoader;

public class MainCostChart extends AbstractDemoChart {

	@Override
	public String getName() {
		return "MainCostChart";
	}

	@Override
	public String getDesc() {
		return "MainCostChart";
	}
	
	public double[] xArray;
	public double[] valueArray;
	public String[] dateArray;
	
	public MainCostChart(double[] valueArray, String[] dateArray) {
		this.valueArray = valueArray;
		this.dateArray = dateArray;
		xArray = new double[dateArray.length];
		for (int i=0;i<dateArray.length;i++) {
			xArray[i] = i;
		}
	}

	@Override
	public View executeWithView(Context context) {
		String[] titles = new String[] {"收益图表"};
		List<double[]> x = new ArrayList<double[]>();
		x.add(xArray);

		List<double[]> values = new ArrayList<double[]>();
		values.add(valueArray);

		int[] colors = new int[] { context.getResources().getColor(R.color.cost_chart_line) };
		PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE };
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
					.setFillPoints(true);
		}
		
		double valueMin = 0, valueMax = 0;
		if (valueArray.length == 0) {
			valueMin = 0;
			valueMax = 10;
		} else {
			double[] valueArray2 = Arrays.copyOf(valueArray, valueArray.length);
			Arrays.sort(valueArray2);
			valueMin = valueArray2[0] - 10;			
			valueMax = valueArray2[valueArray2.length - 1] + 10;
			if (valueMax >= 100 && valueMax < 1000) {
				valueMax = valueMax + 100;
			} else if (valueMax >= 1000 && valueMax < 10000) {
				valueMax = valueMax + 300;
			} else if (valueMax >= 10000 && valueMax < 100000) {
				valueMax = valueMax + 500;
			}
		}
		Log.i(ConfigLoader.TAG, "valueMin:" + valueMin + ", valueMax:" + valueMax);
		double xvMin = 1, xvMax = 8;
		if (xArray.length != 0) {
			xvMin = xArray[0];
			xvMax = xArray[xArray.length - 1] + 1;
		}
		setChartSettings(renderer, "近7天消费记录", "日期", "金额", xvMin, xvMax,
				valueMin, valueMax, context.getResources().getColor(R.color.cost_today_tcolor)
				, Color.GRAY); // 设置X,Y坐标最大值
		//设置X轴近似坐标个数.		
		renderer.setXLabels(8);
		//设置Y轴近似坐标个数.
		renderer.setYLabels(8);
		//设置X,Y轴最大最小数据
		renderer.setXAxisMax(xvMax);
		renderer.setXAxisMin(xvMin);
		renderer.setYAxisMax(valueMax);
		renderer.setYAxisMin(valueMin);
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(context.getResources().getColor(
				R.color.cost_today_tcolor));
		renderer.setMarginsColor(context.getResources().getColor(R.color.cost_today_tcolor));
		renderer.setShowGrid(true);
		renderer.setPanEnabled(false, false);//设置不能左右上下拖动
		renderer.setShowLegend(false);       //显示图标下方说明
		//隐藏X轴坐标坐标显示，并设置每一个位置的标签（实现自定义X轴坐标的功能）
		renderer.setXLabels(0);
		for (double xValue : xArray) {
			int value = (int)xValue;
			renderer.addXTextLabel(value, dateArray[value]);
		}
//		renderer.addXTextLabel(1, "测试");
//		renderer.addXTextLabel(2, "测试1");
//		renderer.addXTextLabel(3, "测试2");
//		renderer.addXTextLabel(4, "测试3");		
		// renderer.setXTitle("goods test"); //设置X轴标题
		
		// renderer.setDisplayValues(true);
		XYSeriesRenderer r = (XYSeriesRenderer) renderer.getSeriesRendererAt(0);
		FillOutsideLine line = new FillOutsideLine(FillOutsideLine.Type.BELOW);
		line.setColor(context.getResources().getColor(R.color.cost_chart_bg));
		r.addFillOutsideLine(line);
		r.setDisplayChartValues(true); // 设置在上方显示值
		r.setChartValuesTextSize(40);
		r.setChartValuesTextAlign(Align.LEFT);
		r.setLineWidth(6);		//设置线条宽度

		XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
		return ChartFactory.getLineChartView(context, dataset, renderer);
	}

	@Override
	public Intent execute(Context context) {
		return null;
	}
}
